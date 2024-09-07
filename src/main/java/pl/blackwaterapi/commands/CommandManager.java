package pl.blackwaterapi.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.SimplePluginManager;
import pl.blackwaterapi.utils.Reflection;

import java.util.HashMap;


public class CommandManager
{
    public static HashMap<String, Command> commands;
    private static Reflection.FieldAccessor<SimpleCommandMap> f;
    private static CommandMap cmdMap;
    
    public static void register(Command cmd) {
        if (CommandManager.cmdMap == null) {
            CommandManager.cmdMap = CommandManager.f.get(Bukkit.getServer().getPluginManager());
        }
        CommandManager.cmdMap.register(cmd.getName(), cmd);
        CommandManager.commands.put(cmd.getName(), cmd);
    }
    
    static {
        commands = new HashMap<>();
        f = Reflection.getField(SimplePluginManager.class, "commandMap", SimpleCommandMap.class);
        CommandManager.cmdMap = CommandManager.f.get(Bukkit.getServer().getPluginManager());
    }
}
