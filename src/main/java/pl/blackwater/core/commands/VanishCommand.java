package pl.blackwater.core.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.entity.Player;

import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwaterapi.commands.PlayerCommand;
import pl.blackwaterapi.utils.Util;

public class VanishCommand extends PlayerCommand implements Colors{

	public VanishCommand() {
		super("vanish", "vanish", "/vanish <on/off>", "core.vanish", "v");
	}
	
	@Getter public static List<Player> vanished = new ArrayList<>();
	
	@Override
	public boolean onCommand(Player p, String[] args) {
		if(getVanished().contains(p)) {
			getVanished().remove(p);
			for(Player online : Bukkit.getOnlinePlayers()) {
				if(!online.hasPermission("core.vanish"))
					online.showPlayer(p);
			}
			p.getWorld().spigot().playEffect(p.getLocation().clone().add(0.0, 1.5, 0.0), Effect.COLOURED_DUST, 5, 10, 0.8f, 0.4f, 0.7f, 0.05f, 120, 750);
			Util.sendMsg(Bukkit.getOnlinePlayers(), Util.replaceString(SpecialSigns + "->> " + MainColor + p.getName() + SpecialSigns + " * " + ChatColor.BLUE + "VANISH" + SpecialSigns + " |->> " + ChatColor.GREEN + "%V%"), "core.vanish");
		}else {
			getVanished().add(p);
			for(Player online : Bukkit.getOnlinePlayers()) {
				if(!online.hasPermission("core.vanish"))
					online.hidePlayer(p);
			}
			p.getWorld().spigot().playEffect(p.getLocation().clone().add(0.0, 1.5, 0.0), Effect.COLOURED_DUST, 5, 10, 0.3f, 0.3f, 0.3f, 0.05f, 120, 750);
			Util.sendMsg(Bukkit.getOnlinePlayers(), Util.replaceString(SpecialSigns + "->> " + MainColor + p.getName() + SpecialSigns + " * " + ChatColor.BLUE + "VANISH" + SpecialSigns + " |->> " + ChatColor.DARK_RED + "%X%"), "core.vanish");
		}
		return false;
	}

}
