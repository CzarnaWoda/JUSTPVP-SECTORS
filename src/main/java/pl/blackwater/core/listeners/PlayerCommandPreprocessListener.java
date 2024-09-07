package pl.blackwater.core.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.help.HelpTopic;
import pl.blackwater.core.data.Combat;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.CombatManager;
import pl.blackwater.core.settings.CoreConfig;
import pl.blackwater.core.settings.MessageConfig;
import pl.blackwaterapi.data.APIConfig;
import pl.blackwaterapi.utils.TitleUtil;
import pl.blackwaterapi.utils.Util;

import java.util.HashMap;

public class PlayerCommandPreprocessListener implements Listener,Colors {

	private static final HashMap<Player, Long> times = new HashMap<>();

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent e) {
		final Player p = e.getPlayer();
		if (e.getMessage().startsWith("/op") && !e.getMessage().startsWith("open") || e.getMessage().startsWith("/deop") || e.getMessage().startsWith("/banip") || e.getMessage().startsWith("/tempbanip") || e.getMessage().startsWith("/kill") || e.getMessage().startsWith("wl") || e.getMessage().startsWith("/whitelist") || e.getMessage().startsWith("/minecraft:") || e.getMessage().startsWith("/bukkit:") || e.getMessage().startsWith("/spigot:") || e.getMessage().startsWith("/tempbanip") || e.getMessage().startsWith("/ban-ip") || e.getMessage().startsWith("/pardon-ip")) {
			if (!APIConfig.SUPERADMINSYSTEM_ADMINUUID.contains(p.getUniqueId().toString())) {
				Util.sendMsg(p, WarningColor + "Blad: " + WarningColor_2 + "Wykryto komende specjalną, uprawnienia do tej komendy są zablokowane");
				e.setCancelled(true);
				return;
			}
		}
		final Long t = times.get(p);
		if (t != null && System.currentTimeMillis() - t < 5000L && !p.hasPermission("core.admin")) {
			e.setCancelled(true);
			final int seconds = 5 - (int) (t / 1000 + 2 - (System.currentTimeMillis() / 1000));
			Util.sendMsg(p, WarningColor + "Blad: " + WarningColor_2 + "Kolejna komende bedziesz mogl wykonac za: " + WarningColor + seconds + WarningColor_2 + "s");
			return;
		}
		times.put(p, System.currentTimeMillis());
		Combat c = CombatManager.getCombat(p);
		final String acmd = e.getMessage();
		if (c != null && c.hasFight() && !p.hasPermission("core.combat.bypass")) {
			if (!CoreConfig.COMBATMANAGER_BLOCKEDCMD.contains(acmd.split(" ")[0])) {
				e.setCancelled(true);
				Util.sendMsg(p, WarningColor + "Blad: " + WarningColor_2 + "Nie mozesz uzywac tej komendy podczas walki");
				return;
			}
		}
		if(!e.isCancelled()) {
			final String command = e.getMessage().split(" ")[0];
			final HelpTopic htopic = Bukkit.getServer().getHelpMap().getHelpTopic(command);
			if (htopic == null) {
				Util.sendMsg(p, MessageConfig.MESSAGE_COMMAND_UNKNOWNCOMMAND);
				e.setCancelled(true);
			}
		}
	}
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerCommandPreProcess1(PlayerCommandPreprocessEvent e) {
		if (!e.isCancelled()) {
			final String command = e.getMessage().split(" ")[0];
			final Player p = e.getPlayer();
			if (!p.hasPermission("core.plugins")) {
				String[] pluginCommands = {"/ver", "/bukkit:ver", "/about", "/me", "/bukkit:me", "/bukkit:help", "/kill", "/?", "/bukkit:?", "/pl", "/plugins", "/bukkit:pl", "/bukkit:plugins", "/version", "/bukkit:version"};
				if (Util.containsIgnoreCase(pluginCommands, command)) {
					e.setCancelled(true);
					Util.sendMsg(p, Util.replaceString(SpecialSigns + ">> " + MainColor + "Pluginy zostaly napisane przez " + ImportantColor + BOLD + "WUJKA GOOGLE"));
					Util.sendMsg(p, Util.replaceString(SpecialSigns + ">> " + MainColor + "Serwer dziala na silniku " + WarningColor + "PAPERSPIGOT 1.8.8 " + MainColor + "paczka antycheat " + ImportantColor + "BLAZINGPACK 1.8.8"));
				}
			}
		}
	}
	}
