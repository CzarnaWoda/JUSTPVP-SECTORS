package pl.blackwater.core.commands;

import org.bukkit.entity.Player;

import pl.blackwater.core.inventories.ChatControlInventory;
import pl.blackwaterapi.commands.PlayerCommand;

public class ChatControlCommand extends PlayerCommand
{

	public ChatControlCommand()
	{
		super("chatcontrol", "chatcontrol menu", "/chatcontrol", "core.chatcontrol", "cc","chatc","chatmanager","ccontrol");
	}

	@Override
	public boolean onCommand(Player p, String[] arg1) {
		ChatControlInventory.openChatControlInventory(p);
		return false;
	}

}
