package pl.blackwater.storage.listeners;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import pl.blackwater.core.Core;
import pl.blackwater.core.data.User;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.core.settings.MessageConfig;
import pl.blackwater.storage.data.StorageRent;
import pl.blackwater.storage.managers.StorageRentManager;
import pl.blackwater.storage.settings.StorageConfig;
import pl.blackwaterapi.utils.TitleUtil;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.packets.impl.storage.StorageAddMemberPacket;
import pl.justsectors.redis.client.RedisClient;

public class AsyncPlayerChatListener implements Listener,Colors{
	
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.LOWEST)
	public void onAsyncPlayerChat(AsyncPlayerChatEvent e) {
		final Player p = e.getPlayer();
		if(StorageRentManager.getAddMembersAction().containsKey(p)) {
			e.setCancelled(true);
			final StorageRent storage = StorageRentManager.getAddMembersAction().get(p);
			StorageRentManager.getAddMembersAction().remove(p);
			final String prePlayer = e.getMessage();
			final User otherPlayer = UserManager.getUser(prePlayer);
			if(otherPlayer == null) {
				Util.sendMsg(p, Util.fixColor(MessageConfig.MESSAGE_COMMAND_UNKNOWNPLAYER));
				return;
			}
			List<String> regionMembers = storage.getMembersList();
			if(regionMembers.contains(otherPlayer.getUuid().toString())) {
				Util.sendMsg(p, Util.fixColor(WarningColor + "Blad: " + WarningColor_2 + "Ten gracz jest juz czlonkiem magazynu!"));
				return;
			}
			final User u = UserManager.getUser(p);
			if(u.getMoney() >= StorageConfig.STORAGE_ADDMEMBERCOST) {
				u.removeMoneyViaPacket(StorageConfig.STORAGE_ADDMEMBERCOST);
				storage.addMember(otherPlayer.getUuid().toString());
				storage.update(true);
				final StorageAddMemberPacket packet = new StorageAddMemberPacket(otherPlayer.getUuid(),storage.getId());
				RedisClient.sendSectorsPacket(packet);
				TitleUtil.sendFullTitle(p, 16, 10, 20,"", Util.fixColor(ChatColor.DARK_GREEN + "Dodano " + MainColor + "gracza do magazynu storage-" + storage.getId()));
			}else {
				Util.sendMsg(p, Util.fixColor(WarningColor + "Blad: " + WarningColor_2 + "Nie posiadasz wystarczajacej ilosci monet!"));
			}
			
		}
	}

}
