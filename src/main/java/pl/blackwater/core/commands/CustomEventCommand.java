package pl.blackwater.core.commands;

import org.bukkit.entity.Player;
import pl.blackwater.core.events.CustomEventManager;
import pl.blackwater.core.events.EventType;
import pl.blackwater.core.settings.MessageConfig;
import pl.blackwaterapi.commands.PlayerCommand;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.packets.impl.customevents.StartCustomEventPacket;
import pl.justsectors.redis.client.RedisClient;

public class CustomEventCommand extends PlayerCommand {
    public CustomEventCommand() {
        super("customevent", "komenda ktora zarzadza customowymi eventami", "/customevent [kills/breakstone/openchest/pumpkin] <time>", "core.customevent", "ce");
    }

    @Override
    public boolean onCommand(Player p, String[] args) {

        if(args.length < 2){
            return Util.sendMsg(p, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}",getUsage()));
        }
        if(args[0].equals("kills")){
            final long time = Util.parseDateDiff(args[1],true);

            if(time < System.currentTimeMillis()){
                return Util.sendMsg(p, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}",getUsage()));
            }
            final StartCustomEventPacket packet = new StartCustomEventPacket(EventType.KILLS, time);
            RedisClient.sendSectorsPacket(packet);

            return Util.sendMsg(p,Util.replaceString("&6&nCUSTOMEVENT&8 ->> &7Wlaczyles event &cKILLS &7do &c" + Util.getDate(time)));
        }
        else if(args[0].equalsIgnoreCase("breakstone")){
            final long time = Util.parseDateDiff(args[1],true);

            if(time < System.currentTimeMillis()){
                return Util.sendMsg(p, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}",getUsage()));
            }
            final StartCustomEventPacket packet = new StartCustomEventPacket(EventType.BREAK_STONE, time);
            RedisClient.sendSectorsPacket(packet);

            return Util.sendMsg(p,Util.replaceString("&6&nCUSTOMEVENT&8 ->> &7Wlaczyles event &cBREAKSTONE &7do &c" + Util.getDate(time)));
        }        else if(args[0].equalsIgnoreCase("openchest")){
            final long time = Util.parseDateDiff(args[1],true);

            if(time < System.currentTimeMillis()){
                return Util.sendMsg(p, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}",getUsage()));
            }
            final StartCustomEventPacket packet = new StartCustomEventPacket(EventType.OPEN_CHEST, time);
            RedisClient.sendSectorsPacket(packet);

            return Util.sendMsg(p,Util.replaceString("&6&nCUSTOMEVENT&8 ->> &7Wlaczyles event &cOPENCHEST &7do &c" + Util.getDate(time)));
        }        else if(args[0].equalsIgnoreCase("pumpkin")){
            final long time = Util.parseDateDiff(args[1],true);

            if(time < System.currentTimeMillis()){
                return Util.sendMsg(p, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}",getUsage()));
            }
            final StartCustomEventPacket packet = new StartCustomEventPacket(EventType.PUMPKIN, time);
            RedisClient.sendSectorsPacket(packet);

            return Util.sendMsg(p,Util.replaceString("&6&nCUSTOMEVENT&8 ->> &7Wlaczyles event &cPUMPKIN &7do &c" + Util.getDate(time)));
        }
        else{
            return Util.sendMsg(p, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}",getUsage()));
        }
    }
}
