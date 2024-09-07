package pl.blackwater.core.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import lombok.Getter;
import pl.blackwater.core.inventories.AdminPanelInventory;
import pl.blackwaterapi.commands.PlayerCommand;

public class AdminPanelCommand extends PlayerCommand
{
	@Getter private static final List<Player> AR_ADMINS = new ArrayList<>();
	@Getter private static final List<Player> SS_ADMINS = new ArrayList<>();
	@Getter private static final List<Player> admin_changeTIMED = new ArrayList<>();
	@Getter private static final List<Player> admin_changeTIMEE = new ArrayList<>();
	@Getter private static final List<Player> admin_changeTIMEG = new ArrayList<>();
	public AdminPanelCommand()
	{
		super("adminpanel", "otwiera adminpanel", "/adminpanel", "core.adminpanel", "admin","panel");
	}

	@Override
	public boolean onCommand(Player player, String[] args) {
		AdminPanelInventory.openMenu(player);
		return false;
	}

}
