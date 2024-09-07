package pl.blackwater.core.commands;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.EasyScoreboardManager;
import pl.blackwaterapi.commands.PlayerCommand;
import pl.blackwaterapi.utils.Util;

public class SideBarCommand extends PlayerCommand implements Colors{

	public SideBarCommand() {
		super("sidebar", "/sidebar", "/sidebar", "core.sidebar");
	}

	@Override
	public boolean onCommand(Player p, String[] args) {
		if(EasyScoreboardManager.getScoreboard(p) == null)
			EasyScoreboardManager.createScoreBoard(p);
		else
			EasyScoreboardManager.removeScoreBoard(p);
		return Util.sendMsg(p, Util.replaceString(SpecialSigns + ">> " + (EasyScoreboardManager.getScoreboard(p) != null ? ChatColor.GREEN + "Wlaczono" : ChatColor.DARK_RED + "Wylaczono") + MainColor + " sidebar!"));
	}
}
