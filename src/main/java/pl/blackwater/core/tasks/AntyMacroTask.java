package pl.blackwater.core.tasks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import pl.blackwater.core.Core;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.AntyMacroManager;
import pl.blackwater.core.settings.CoreConfig;
import pl.blackwater.core.settings.MessageConfig;
import pl.blackwaterapi.utils.TitleUtil;
import pl.blackwaterapi.utils.Util;

import java.util.LinkedList;

public class AntyMacroTask implements Colors {
	
    public static void getRate() {
    	Bukkit.getScheduler().scheduleAsyncRepeatingTask(Core.getPlugin(), new Runnable() {
        LinkedList<Double> ll = new LinkedList<>();
        double sum = 0.0;
		@SuppressWarnings("unused")
		double total = 0.0;
		@SuppressWarnings("deprecation")
		@Override
        public void run() {
            for (Player p : Bukkit.getOnlinePlayers())
                if (AntyMacroManager.clickCount.containsKey(p.getUniqueId())) {
                    double count = AntyMacroManager.clickCount.get(p.getUniqueId());
                    if (count > CoreConfig.ANTYMACROMANAGER_CPSAMOUNT) {
                        if (AntyMacroManager.notifyCount.get(p) == null) {
                            AntyMacroManager.notifyCount.put(p, 1);
                        } else {
                            int i = AntyMacroManager.notifyCount.get(p);
                            i++;
                            AntyMacroManager.notifyCount.replace(p, i);
                            if (i == 5) {
                                TitleUtil.sendFullTitle(p, 10, 100, 5, Util.fixColor(Util.replaceString("&8&l>> &c&lAnty&2&lMacro &8&l<<")), "     &8&l* &7Wykryto zbyt duza ilosc CPS &8(&65 ostrzezenie &8) &8&l*");
                            }
                            if (i == 10) {
                                TitleUtil.sendFullTitle(p, 10, 100, 5, Util.fixColor(Util.replaceString("&8&l>> &c&lAnty&2&lMacro &8&l<<")), "     &8&l* &7Wykryto zbyt duza ilosc CPS &8(&610 ostrzezenie &8) &8&l*");
                            }
                            if (i >= 15) {
                                //long time = Util.parseDateDiff("2h", true);
                                if (!p.hasPermission("AntyMacroManager.admin")) {
                                    //TODO BanManager.createBan(p.getUniqueId(), "AntyMacro (Powyzej 15CPS)", "AntyMacro-Bot", time);
                                    p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 80, 4));
                                    p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 80, 4));
                                    TitleUtil.sendTitle(p,10,60,20, ImportantColor + "Ostrzezenie!", MainColor + "Wylacz " + ImportantColor + "makro!");
                                    AntyMacroManager.notifyCount.remove(p);
                                }
                            }
                        }
                        for (Player on : Bukkit.getOnlinePlayers()) {
                            if (on.hasPermission("AntyMacroManager.antycheat")) {
                                on.sendMessage(ChatColor.translateAlternateColorCodes('&', Util.replaceString(MessageConfig.MESSAGE_ANTYMACRO
                                        .replace("{ALERTCOUNT}", (AntyMacroManager.notifyCount.get(p) == null ? "0" : String.valueOf(AntyMacroManager.notifyCount.get(p))))
                                        .replace("{PLAYER}", p.getName()).replace("{CLICKS}", String.valueOf(count)))));
                            }
                        }
                    }
                    if (count > 0.0) {
                        ll.add(count);
                        for (double x : ll) {
                            x = count;
                            total = sum + x;
                        }
                    }
                    total = 0.0;
                    try {
                        ll.remove();
                    } catch (Exception ignored) {
                    }
                    AntyMacroManager.clickCount.put(p.getUniqueId(), 0);
                }
        }
    }, 1L, 20L);
}

}
