package pl.blackwater.core.commands;

import org.bukkit.entity.Player;

import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.inventories.ChatInventory;
import pl.blackwaterapi.commands.PlayerCommand;

public class ChatCommand extends PlayerCommand implements Colors{

	public ChatCommand()
	{
		super("chat", "chatmanager command", "/chat", "core.chat");
	}

	@Override
	public boolean onCommand(Player p, String[] args) {
		ChatInventory.openMenu(p);
		return false;
	}

}
