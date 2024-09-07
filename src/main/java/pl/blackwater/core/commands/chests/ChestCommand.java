package pl.blackwater.core.commands.chests;

import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwaterapi.commands.PlayerCommand;
import pl.blackwaterapi.utils.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

public class ChestCommand extends PlayerCommand implements Colors {

    @Getter private static final LinkedHashSet<PlayerCommand> chestCommands = new LinkedHashSet<>();
    public ChestCommand() {
        super("chest", "chest command", "/chest", "core.chest", "ustaw");

        chestCommands.add(new SetCommand());
        chestCommands.add(new DeleteCommand());
        chestCommands.add(new ListCommand());
        chestCommands.add(new PowerCommand());
    }

    @Override
    public boolean onCommand(Player sender, String[] args) {
        if(args.length == 0){
            return sendUsage(sender);
        }
        String subCommand = args[0];
        PlayerCommand command = getSubCommand(subCommand);
        return (command == null) ? sendUsage(sender) : (sender.hasPermission(command.getPermission())) ? command.onExecute(sender, Arrays.copyOfRange(args, 1 , args.length)) : Util.sendMsg(sender , WarningColor + "Blad: " + WarningColor_2 + "Nie posiadasz uprawnien do tej komendy");
    }
    private boolean sendUsage(CommandSender sender){
        List<String> usages = new ArrayList<>();
        usages.add(SpecialSigns + "▬▬ " + ImportantColor + "KOMENDY SYSTEMU SKRZYN " + SpecialSigns + "▬▬");
        usages.add("");
        for(PlayerCommand command : chestCommands){
            usages.add(SpecialSigns + "->> " + ImportantColor + command.getUsage() + SpecialSigns + " >> " + MainColor + command.getDescription());
        }
        usages.add("");
        usages.add(SpecialSigns + "▬▬ " + ImportantColor + "KOMENDY SYSTEMU SKRZYN " + SpecialSigns + "▬▬");
        usages.forEach(message -> Util.sendMsg(sender, Util.fixColor(Util.replaceString(message))));
        return true;
    }

    private PlayerCommand getSubCommand(String command){
        for(PlayerCommand cmd : chestCommands){
            if(cmd.getName().equalsIgnoreCase(command) || cmd.getAliases().contains(command)){
                return cmd;
            }
        }
        return null;
    }
}
