package pl.blackwater.core.commands.chests;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import pl.blackwater.core.Core;
import pl.blackwater.core.data.TreasureChest;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.utils.TreasureChestUtil;
import pl.blackwaterapi.commands.PlayerCommand;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.packets.impl.treasures.TreasureCreatePacket;
import pl.justsectors.packets.impl.treasures.TreasureDeletePacket;
import pl.justsectors.redis.client.RedisClient;

public class DeleteCommand extends PlayerCommand implements Colors {
    public DeleteCommand() {
        super("delete", "Usuwa skrzynke z systemu skrzyn", "/chest delete", "core.chest", "usun");
    }

    @Override
    public boolean onCommand(Player player, String[] args) {
        final Block b = TreasureChestUtil.getTargetedContainerBlock(player);
        if(b == null){
            return Util.sendMsg(player, WarningColor + "Blad: " + WarningColor_2 + "Nie patrzysz sie na skrzynke");
        }
        final TreasureChest treasureChest = Core.getTreasureChestManager().getChest(b.getLocation());
        if(treasureChest == null){
            return Util.sendMsg(player, WarningColor + "Blad: " + WarningColor_2 + "Nie patrzysz sie na skrzynke która jest w systemie skrzyn");
        }
        RedisClient.sendSectorsPacket(new TreasureDeletePacket(treasureChest.getUuid(), treasureChest.getType().getType().toUpperCase()));
        treasureChest.delete();
        return true;
    }
}
