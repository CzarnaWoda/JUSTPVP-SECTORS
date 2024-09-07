package pl.blackwater.guilds.commands;

import lombok.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import pl.blackwater.core.interfaces.*;
import pl.blackwater.guilds.commands.user.*;
import pl.blackwater.guilds.data.War;
import pl.blackwaterapi.commands.*;
import pl.blackwaterapi.utils.*;

import java.util.*;

public class GuildCommand extends PlayerCommand implements Colors {

    @Getter private static final LinkedHashSet<PlayerCommand> subCommands = new LinkedHashSet<>();

    public GuildCommand() {
        super("guild", "glowna komenda systemu gildii", "/guild <subkomenda>", "guild.main", "g","gildia","gildie");
        getSubCommands().add(new CreateCommand());
        getSubCommands().add(new DeleteCommand());
        getSubCommands().add(new ExpireCommand());
        getSubCommands().add(new GuildInfoCommand());
        getSubCommands().add(new InviteCommand());
        getSubCommands().add(new JoinCommand());
        getSubCommands().add(new KickCommand());
        getSubCommands().add(new LeaveCommand());
        getSubCommands().add(new MineCommand());
        getSubCommands().add(new OwnerCommand());
        getSubCommands().add(new PlayerLimitCommand());
        getSubCommands().add(new PreOwnerCommand());
        getSubCommands().add(new PvPCommand());
        //getSubCommands().add(new WarCommand());
        getSubCommands().add(new WarInfoCommand());
        //getSubCommands().add(new AllianceCommand());
        getSubCommands().add(new GuildItemsCommand());
        getSubCommands().add(new ShopCommand());
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
        Util.sendMsg(sender,Util.replaceString(SpecialSigns + "|->> " + MainColor + "Komendy systemu gildii:"));
        for(PlayerCommand command : getSubCommands()){
            Util.sendMsg(sender,Util.replaceString(SpecialSigns + "  ->> " + ImportantColor + command.getUsage() + SpecialSigns + " * " + MainColor + command.getDescription()));
        }
        Util.sendMsg(sender,Util.replaceString(SpecialSigns + "    ->> " + ImportantColor + "!" + SpecialSigns + " * " + MainColor + "Chat gildyjny"));
        Util.sendMsg(sender,Util.replaceString(SpecialSigns + "    ->> " + ImportantColor + "!!" + SpecialSigns + " * " + MainColor + "Chat sojuszniczy"));
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
