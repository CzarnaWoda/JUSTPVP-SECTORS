package pl.blackwater.core.commands.chests;

import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import pl.blackwater.core.Core;
import pl.blackwater.core.data.TreasureChest;
import pl.blackwater.core.enums.ChestType;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.settings.MessageConfig;
import pl.blackwater.core.utils.TreasureChestUtil;
import pl.blackwaterapi.commands.PlayerCommand;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.packets.impl.treasures.TreasureCreatePacket;
import pl.justsectors.redis.client.RedisClient;

public class SetCommand extends PlayerCommand implements Colors {
    public SetCommand() {
        super("set", "Ustawia nowa skrzynke w systemie skrzyn", "/chest set [type] [time]", "core.chest");

    }

    @Override
    public boolean onCommand(Player player, String[] args) {
        if(args.length < 2){
            return Util.sendMsg(player, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}", getUsage()));
        }
        final ChestType type = ChestType.getByType(args[0]);
        if(type == null){
            return Util.sendMsg(player, WarningColor + "Blad: " +WarningColor_2 + "Taki typ skrzynki nie istnieje");
        }
        final long time = Util.parseDateDiff(args[1], true) - System.currentTimeMillis();
        Block block = TreasureChestUtil.getTargetedContainerBlock(player);
        if(block == null){
            return Util.sendMsg(player, WarningColor + "Blad: " + WarningColor_2 + "Nie patrzysz sie na skrzynke");
        }
        Chest chest = (org.bukkit.block.Chest) block.getState();
        TreasureChest treasureChest = Core.getTreasureChestManager().createChest(block,type,chest.getInventory().getContents(),time);
        RedisClient.sendSectorsPacket(new TreasureCreatePacket(player.getUniqueId(), treasureChest));
        treasureChest.insert();
        return true;
    }
}
