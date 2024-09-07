package pl.blackwater.core.commands.chests;

import org.bukkit.entity.Player;
import pl.blackwater.core.settings.MessageConfig;
import pl.blackwaterapi.commands.PlayerCommand;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.packets.impl.config.LoadConfigPacket;
import pl.justsectors.packets.impl.config.SetConfigDoublePacket;
import pl.justsectors.packets.impl.config.SetConfigIntPacket;
import pl.justsectors.redis.client.RedisClient;

public class PowerCommand extends PlayerCommand {
    public PowerCommand() {
        super("power", "Ustawia moc skrzynek", "/power [percent]", "core.chest", "moc");
    }

    @Override
    public boolean onCommand(Player p, String[] args) {
        if(args.length < 1){
            return Util.sendMsg(p, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}",getUsage()));
        }
        if(!Util.isInteger(args[0])){
            return Util.sendMsg(p, "&4Blad: &cMusisz podać liczbe procentów!");
        }
        final int value = Integer.parseInt(args[0]);

        final SetConfigIntPacket packet = new SetConfigIntPacket("coreconfig.yml", value, "treasuremanager.power",false,p.getDisplayName());
        RedisClient.sendSectorsPacket(packet);
        final LoadConfigPacket loadConfigPacket = new LoadConfigPacket("coreconfig.yml");
        RedisClient.sendSectorsPacket(loadConfigPacket);

        return Util.sendMsg(p, "&8->> &7Ustawiles moc skrzynek na &a" + value/100 + "&8%");
    }
}
