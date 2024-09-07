package pl.blackwater.core.commands;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import pl.blackwater.core.data.User;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.UserManager;
import pl.blackwaterapi.commands.PlayerCommand;
import pl.blackwaterapi.utils.Ticking;
import pl.blackwaterapi.utils.Util;

public class GcCommand extends PlayerCommand implements Colors
{
    public GcCommand() {
        super("gc", "statystki serwera", "/gc", "core.gc");
    }
    
    
    public boolean onCommand(Player player, String[] args)
    {

        player.sendMessage(Util.fixColor(Util.replaceString(SpecialSigns + "-------------------------------------")));
        player.sendMessage(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor +"TPS: " + ImportantColor + Ticking.getTPS())));
        player.sendMessage(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor +"Dostepne Rdzenie: " + ImportantColor + Runtime.getRuntime().availableProcessors())));
        player.sendMessage(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor +"Pamiec:")));
        player.sendMessage(Util.fixColor(Util.replaceString(SpecialSigns + "  >> " + MainColor + " Calkowita: " + ImportantColor + Runtime.getRuntime().maxMemory() / 1024L / 1024L + "MB")));
        player.sendMessage(Util.fixColor(Util.replaceString(SpecialSigns + "  >> " + MainColor + " Zarezerwowana: " + ImportantColor + Runtime.getRuntime().totalMemory() / 1024L / 1024L + "MB")));
        player.sendMessage(Util.fixColor(Util.replaceString(SpecialSigns + "  >> " + MainColor + " Wolna: " + ImportantColor + Runtime.getRuntime().freeMemory() / 1024L / 1024L + "MB")));
        World world = Bukkit.getWorld("world");
        player.sendMessage(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor +"Mapa:")));
        player.sendMessage(Util.fixColor(Util.replaceString(SpecialSigns + "  >> " + MainColor + " Chunki: " + ImportantColor + world.getLoadedChunks().length)));
        player.sendMessage(Util.fixColor(Util.replaceString(SpecialSigns + "  >> " + MainColor + " Wszystkie Entites: " + ImportantColor + world.getEntities().size())));
        player.sendMessage(Util.fixColor(Util.replaceString(SpecialSigns + "  >> " + MainColor + " Zywe Entites: " + ImportantColor + world.getLivingEntities().size())));
        player.sendMessage(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor +"Watki:")));
        ThreadMXBean tmxb = ManagementFactory.getThreadMXBean();
        long full = 0L;
        for (Thread t : Thread.getAllStackTraces().keySet()) {
          full += tmxb.getThreadCpuTime(t.getId());
        }
        for (Thread t : Thread.getAllStackTraces().keySet()) {
          if (tmxb.getThreadCpuTime(t.getId()) > 0L)
          {
            long l = tmxb.getThreadCpuTime(t.getId()) * 100L / full;
            if (l > 0.0D) {
              player.sendMessage(Util.fixColor(Util.replaceString(SpecialSigns + "  >> " + MainColor + " " + t.getName() + ": " + ImportantColor + l + "%")));
            }
          }
        }
        player.sendMessage(Util.fixColor(Util.replaceString(SpecialSigns + "-------------------------------------")));
		return false;
    }
    
}