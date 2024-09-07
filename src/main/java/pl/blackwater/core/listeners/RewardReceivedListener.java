package pl.blackwater.core.listeners;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import net.lightshard.itemcases.cases.CaseType;
import net.lightshard.itemcases.cases.ItemCase;
import net.lightshard.itemcases.cases.reward.ItemReward;
import net.lightshard.itemcases.event.RewardReceivedEvent;
import net.md_5.bungee.api.ChatColor;
import pl.blackwater.core.data.ChatControlUser;
import pl.blackwater.core.data.User;
import pl.blackwater.core.enums.ChatControlType;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.ChatControlUserManager;
import pl.blackwater.core.managers.EnchantManager;
import pl.blackwater.core.managers.ItemManager;
import pl.blackwater.core.managers.UserManager;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.packets.impl.chat.ChatControlGlobalMessagePacket;
import pl.justsectors.redis.client.RedisClient;

public class RewardReceivedListener implements Listener,Colors{
	
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onRewardReceived(RewardReceivedEvent e) {
		final ItemCase itemCase = e.getItemCase();
		final User u = UserManager.getUser(e.getPlayer());
		u.addOpenPremiumChestViaPacket(1);
		if(itemCase.getType().equals(CaseType.CS_GO)) {
			if(itemCase.getName().equalsIgnoreCase("diamond") ||itemCase.getName().equalsIgnoreCase("speciale")) return;
			final ItemReward reward = e.getItemReward();
			final Map<Enchantment, Integer> enchants = reward.getItem().getEnchantments();
			if(enchants.size() > 0) {
				final ItemStack item = reward.getItem();
				final ChatControlGlobalMessagePacket packet = new ChatControlGlobalMessagePacket(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Gracz " + ImportantColor + u.getLastName() + MainColor + " otworzyl skrzynke " + ImportantColor + itemCase.getName() + MainColor + " i wylosowal " + ImportantColor + ItemManager.get(item.getType()) + ChatColor.LIGHT_PURPLE + " " + EnchantManager.getEnchantsFromItemStack(item) + ImportantColor + " " + item.getAmount() + "szt. " + WarningColor + e.getRewardChance() + "%")), ChatControlType.CASEOPEN);
				RedisClient.sendSectorsPacket(packet);
			}else {
				final ItemStack item = reward.getItem();
				final ChatControlGlobalMessagePacket packet = new ChatControlGlobalMessagePacket(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Gracz " + ImportantColor + u.getLastName() + MainColor + " otworzyl skrzynke " + ImportantColor + itemCase.getName() + MainColor + " i wylosowal " + ImportantColor + ItemManager.get(item.getType()) + ChatColor.LIGHT_PURPLE + " " + item.getAmount() + "szt. " + WarningColor + e.getRewardChance() + "%")), ChatControlType.CASEOPEN);
				RedisClient.sendSectorsPacket(packet);
			}
		}
	}
}
