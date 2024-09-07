package pl.blackwater.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.blackwater.core.data.User;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.core.settings.CoreConfig;
import pl.blackwaterapi.commands.Command;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.sectors.Sector;
import pl.justsectors.sectors.SectorManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ListCommand extends Command implements Colors{

	public ListCommand() {
		super("list", "lista graczy na serwerze", "/list", "core.list");
	}

	@Override
	public boolean onExecute(CommandSender sender, String[] args) {
		Set<String> v = new HashSet<>();
		Set<String> players = new HashSet<>();
		Set<String> admin = new HashSet<>();

		Set<String> globalOnline = new HashSet<>();
		for(Sector sector : SectorManager.getSectors().values())
		{
			globalOnline.addAll(sector.getOnlinePlayers());
		}
		for (String ponline: globalOnline){
			User user = UserManager.getUser(ponline);
			if (user.getRank().equalsIgnoreCase("svip") || user.getRank().equalsIgnoreCase("vip")|| user.getRank().equalsIgnoreCase("mvip") || user.getRank().equalsIgnoreCase("yt")){
				v.add(ponline);
			}
			if (user.getRank().equalsIgnoreCase("HeadAdmin") || user.getRank().equalsIgnoreCase("admin") ||user.getRank().equalsIgnoreCase("Moderator") ||user.getRank().equalsIgnoreCase("helper")){
				admin.add(ponline);
			}
			players.add(ponline);
		}
		int i = (int) (globalOnline.size() * CoreConfig.SLOTMANAGER_MULTIPLER);
        sender.sendMessage(Util.fixColor(SpecialSigns + "▬▬ " + ImportantColor + "LISTA GRACZY ONLINE " + SpecialSigns + "▬▬"));
		Util.sendMsg(sender, SpecialSigns + "• " + MainColor + "Ogolnie: " + ImportantColor + i);
		if (sender.hasPermission("core.list.admin")){
        sender.sendMessage(Util.fixColor(SpecialSigns + "-  " + ImportantColor + "" + players.toString().replace("[", "").replace("]", "")));
		}
		sender.sendMessage(Util.fixColor(" "));
        sender.sendMessage(Util.fixColor(SpecialSigns + "• " + MainColor + "VIP " + SpecialSigns + "| " + ImportantColor + "S" + MainColor + "VIP " + SpecialSigns + "| " + ImportantColor + "Y" + ImportantColor + "T " + SpecialSigns + "| " + ImportantColor + "M" + MainColor + "VIP " + SpecialSigns + "» " + ImportantColor + "" + v.size()));
        if (sender.hasPermission("core.list.admin")){
        sender.sendMessage(Util.fixColor(SpecialSigns + "-  " + ImportantColor + "" + v.toString().replace("[", "").replace("]", "")));
        }
        sender.sendMessage(Util.fixColor(" "));
        sender.sendMessage(Util.fixColor(SpecialSigns + "• " + ImportantColor + "Administracja" + ImportantColor + " " + SpecialSigns + "» " + ImportantColor + "" + admin.size()));
        if (sender.getName().equalsIgnoreCase("CzarnaWoda")){
            sender.sendMessage(Util.fixColor(SpecialSigns + "-  " + ImportantColor + "" + admin.toString().replace("[", "").replace("]", "")));
            sender.sendMessage(Util.fixColor("" + ImportantColor + BOLD + "TAK NAPRAWDE JEST: " + globalOnline.size()));
        }
        sender.sendMessage(Util.fixColor(SpecialSigns + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"));
		return false;
	}

}
