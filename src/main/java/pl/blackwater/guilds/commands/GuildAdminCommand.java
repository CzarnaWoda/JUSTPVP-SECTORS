package pl.blackwater.guilds.commands;

import lombok.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import pl.blackwater.core.interfaces.*;
import pl.blackwater.guilds.commands.admin.*;
import pl.blackwaterapi.commands.*;
import pl.blackwaterapi.utils.*;

import java.util.*;

public class GuildAdminCommand extends PlayerCommand implements Colors {

    @Getter
    private static final LinkedHashSet<PlayerCommand> subCommands = new LinkedHashSet<>();


    public GuildAdminCommand(){
        super("gildiaadmin", "glowna komenda administratora do systemu gildii", "/ga <subkomenda>", "guild.admin", "ga");
        getSubCommands().add(new BanCommand());
        getSubCommands().add(new UnBanCommand());
        getSubCommands().add(new DatabaseReloadCommand());
        getSubCommands().add(new SetCommand());
    }
    @Override
    public boolean onCommand(Player player, String[] strings) {
        if(strings.length == 0){
            return sendHelp(player);
        }
        String subCommand = strings[0];
        PlayerCommand command = getSubCommand(subCommand);
        return (command == null) ? sendHelp(player) : (player.hasPermission(command.getPermission()) ? command.onCommand(player, Arrays.copyOfRange(strings, 1, strings.length)) : Util.sendMsg(player,WarningColor + "Blad: " + WarningColor_2 + "Nie posiadasz uprawnien do tej komendy!"));
    }

    private boolean sendHelp(CommandSender sender){
        Util.sendMsg(sender,Util.replaceString(SpecialSigns + "|->> " + WarningColor + "Komendy systemu administratora gildii:"));
        for(PlayerCommand command : getSubCommands()){
            Util.sendMsg(sender,Util.replaceString("  ->> " + ImportantColor + command.getUsage() + SpecialSigns + " * " + MainColor + command.getDescription()));
        }
        Util.sendMsg(sender,Util.replaceString("    ->> " + ImportantColor + "Gildie " + SpecialSigns + " * " + MainColor + "napisane przez BlackWater"));
        Util.sendMsg(sender,Util.replaceString("    ->> " + ImportantColor + "Jebac " + SpecialSigns + " * " + MainColor + "BaoBao96 w dupe"));
        Util.sendMsg(sender,"");
        return true;
    }

    private PlayerCommand getSubCommand(String subCommand){
        for(PlayerCommand command : getSubCommands()){
            if(command.getName().equalsIgnoreCase(subCommand) || command.getAliases().contains(subCommand)){
                return command;
            }
        }
        return null;
    }
}
