package pl.blackwater.core.commands;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.blackwater.core.Core;
import pl.blackwater.core.data.Backup;
import pl.blackwater.core.enums.BackupType;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.inventories.BackupInventory;
import pl.blackwater.core.settings.MessageConfig;
import pl.blackwaterapi.commands.PlayerCommand;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.packets.impl.backups.BackupCreatePacket;
import pl.justsectors.redis.client.RedisClient;

import java.util.ArrayList;
import java.util.List;

public class BackupCommand extends PlayerCommand implements Colors {
    public BackupCommand() {
        super("backup", "backup command", "/backup [menu/create] [player]", "core.backup");
    }
    @Getter
    private static List<Player> findPlayer = new ArrayList<>();
    @Override
    public boolean onCommand(Player player, String[] args) {
        if(args.length < 1){
            return Util.sendMsg(player, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}",getUsage()));
        }
        if (!args[0].equalsIgnoreCase("menu")) {
            if (args[0].equalsIgnoreCase("create") && (args.length >= 2)){
                Player o = Bukkit.getPlayer(args[1]);
                if(o != null){
                    Util.sendMsg(player, Util.replaceString(ImportantColor + "&nBACKUPMANAGER" + SpecialSigns + " ->> " + MainColor + "Trwa tworzenie zapisu ekwipunku gracza " + ImportantColor + o.getName()));
                    final Backup backup = Core.getBackupManager().createBackup(o, BackupType.MANUAL, player.getName());
                    backup.insert();
                    RedisClient.sendSectorsPacket(new BackupCreatePacket(backup));
                    return Util.sendMsg(player, Util.replaceString(ImportantColor + "&nBACKUPMANAGER" + SpecialSigns + " ->> " + MainColor + "Stworzono zapis ekwipunku gracza " + ImportantColor + o.getName()));
                }else{
                    return Util.sendMsg(player, MessageConfig.MESSAGE_COMMAND_UNKNOWNPLAYER);
                }
            }else{
                return Util.sendMsg(player, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}",getUsage()));
            }
        } else {
            BackupInventory.BackupInventory(player);
            return true;
        }
    }
}
