package pl.blackwaterapi.utils;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import pl.blackwater.core.Core;

public class Logger
{
	private static ConsoleCommandSender console = Bukkit.getConsoleSender();
    public static void info(String... logs) {
        for (String s : logs) {
            log(Level.INFO, s);
        }
    }
    
    public static void warning(String... logs) {
        for (String s : logs) {
            log(Level.WARNING, s);
        }
    }
    
    public static void severe(String... logs) {
        for (String s : logs) {
            log(Level.SEVERE, s);
        }
    }
    
    public static void log(Level level, String log) {
        Core.getPlugin().getLogger().log(level, log);
    }
    
    public static void exception(Throwable cause) {
        cause.printStackTrace();
    }
    public static void fixColorSend(String... logs){
    	for(String s : logs){
    		console.sendMessage(Util.fixColor(s));
    	}
    }
}
