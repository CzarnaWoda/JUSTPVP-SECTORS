package pl.blackwater.core.commands.ranks;

import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.settings.CoreConfig;
import pl.blackwaterapi.commands.Command;
import pl.blackwaterapi.data.APIConfig;
import pl.blackwaterapi.utils.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

public class RankCommand extends Command implements Colors {

    @Getter private static final LinkedHashSet<Command> rankCommands = new LinkedHashSet<>();
    public RankCommand() {
        super("rank", "rank command", "/rank", "core.rank");

        rankCommands.add(new CreateCommand());
        rankCommands.add(new SetCommand());
        rankCommands.add(new RemoveCommand());
        rankCommands.add(new UserInfoCommand());
        rankCommands.add(new SetPrefixCommand());
        rankCommands.add(new SetSuffixCommand());
        rankCommands.add(new AddPermissionCommand());
        rankCommands.add(new RemovePermissionCommand());
        rankCommands.add(new RankInfoCommand());
    }

    @Override
    public boolean onExecute(CommandSender sender, String[] args) {
        if(sender instanceof Player){
            final Player player = (Player)sender;
            if(!APIConfig.SUPERADMINSYSTEM_ADMINUUID.contains(player.getUniqueId().toString())){
                return Util.sendMsg(player, WarningColor + "Blad: " + WarningColor_2 + "Wykryto komende specjalną, uprawnienia do tej komendy są zablokowane");
            }
        }
        if(args.length == 0){
            return sendUsage(sender);
        }
        String subCommand = args[0];
        Command command = getSubCommand(subCommand);
        return (command == null) ? sendUsage(sender) : (sender.hasPermission(command.getPermission())) ? command.onExecute(sender, Arrays.copyOfRange(args, 1 , args.length)) : Util.sendMsg(sender , WarningColor + "Blad: " + WarningColor_2 + "Nie posiadasz uprawnien do tej komendy");
    }
    private boolean sendUsage(CommandSender sender){
        List<String> usages = new ArrayList<>();
        usages.add(SpecialSigns + "▬▬ " + ImportantColor + "KOMENDY SYSTEMU RANG " + SpecialSigns + "▬▬");
        usages.add("");
        for(Command command : rankCommands){
            usages.add(SpecialSigns + "->> " + ImportantColor + command.getUsage() + SpecialSigns + " >> " + MainColor + command.getDescription());
        }
        usages.add("");
        usages.add(SpecialSigns + "▬▬ " + ImportantColor + "KOMENDY SYSTEMU RANG " + SpecialSigns + "▬▬");
        usages.forEach(message -> Util.sendMsg(sender, Util.fixColor(Util.replaceString(message))));
        return true;
    }

    private Command getSubCommand(String command){
        for(Command cmd : rankCommands){
            if(cmd.getName().equalsIgnoreCase(command) || cmd.getAliases().contains(command)){
                return cmd;
            }
        }
        return null;
    }
}
