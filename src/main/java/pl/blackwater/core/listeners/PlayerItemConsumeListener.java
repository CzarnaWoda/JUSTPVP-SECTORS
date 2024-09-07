package pl.blackwater.core.listeners;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import lombok.Getter;
import pl.blackwater.core.Core;
import pl.blackwater.core.data.User;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.core.settings.CoreConfig;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.packets.impl.user.UserEatKoxPacket;
import pl.justsectors.packets.impl.user.UserEatRefPacket;
import pl.justsectors.redis.client.RedisClient;

public class PlayerItemConsumeListener implements Listener, Colors {
	
	@Getter private static Collection<PotionEffect> boostKoxList_1 = Arrays.asList(new PotionEffect(PotionEffectType.SPEED, 15 * 20, 0), new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 60*20, 2));
	@Getter private static Collection<PotionEffect> boostKoxList_2 = Arrays.asList(new PotionEffect(PotionEffectType.SPEED, 20*20, 0), new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 2*60*20, 2));
	@Getter private static Collection<PotionEffect> boostKoxList_3 = Arrays.asList(new PotionEffect(PotionEffectType.SPEED, 35*20, 0), new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 3*60*20, 2));
	@Getter private static Collection<PotionEffect> boostRefList_1 = Collections.singletonList(new PotionEffect(PotionEffectType.SPEED, 5 * 20, 0));
	@Getter private static Collection<PotionEffect> boostRefList_2 = Collections.singletonList(new PotionEffect(PotionEffectType.SPEED, 10 * 20, 0));
	@Getter private static Collection<PotionEffect> boostRefList_3 = Collections.singletonList(new PotionEffect(PotionEffectType.SPEED, 20 * 20, 0));


	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerItemConsume(PlayerItemConsumeEvent e) {
		final ItemStack item = e.getItem();
		if(item.getType().equals(Material.GOLDEN_APPLE) && item.getDurability() == (short)1) {
			final Player p = e.getPlayer();
			final User u = UserManager.getUser(p);
			RedisClient.sendSectorsPacket(new UserEatKoxPacket(u.getUuid()));
			p.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
			Bukkit.getScheduler().runTaskLater(Core.getPlugin(),() -> {
				p.removePotionEffect(PotionEffectType.REGENERATION);
				p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,3),true);
			},1L);
			if(u.getBoostKox() == 1)
				p.addPotionEffects(getBoostKoxList_1());
			else
				if (u.getBoostKox() == 2)
					p.addPotionEffects(getBoostKoxList_2());
				else
					if(u.getBoostKox() == 3)
						p.addPotionEffects(getBoostKoxList_3());
		}
		if(item.getType().equals(Material.GOLDEN_APPLE) && item.getDurability() == (short)0) {
			final Player p = e.getPlayer();
			final User u = UserManager.getUser(p);
			RedisClient.sendSectorsPacket(new UserEatRefPacket(u.getUuid()));
			p.removePotionEffect(PotionEffectType.ABSORPTION);
			p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 120*20, 1), true);
			if(u.getBoostRef() == 1)
				p.addPotionEffects(getBoostRefList_1());
			else
				if (u.getBoostRef() == 2)
					p.addPotionEffects(getBoostRefList_2());
				else
					if(u.getBoostRef() == 3)
						p.addPotionEffects(getBoostRefList_3());
		}
		if(!CoreConfig.ENABLEMANAGER_STRENGHT) {
			if (item.getType().equals(Material.POTION) && (item.getDurability() == (short) 41 || item.getDurability() == (short) 16425 || item.getDurability() == (short) 16457 || item.getDurability() == (short) 8265 || item.getDurability() == (short) 8233 || item.getDurability() == (short) 8201)) {
				final Player p = e.getPlayer();
				p.getInventory().remove(item);
				Util.sendMsg(p, WarningColor + "Blad: " + WarningColor_2 + "Ta mikstura zostala zablokowana na serwerze!");
				e.setCancelled(true);
			}
		}
	}
}
