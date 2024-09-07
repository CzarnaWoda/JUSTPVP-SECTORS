package pl.blackwater.core.listeners;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.Getter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import pl.blackwater.core.data.Combat;
import pl.blackwater.core.data.User;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.CombatManager;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.core.settings.CoreConfig;
import pl.blackwater.core.settings.MessageConfig;
import pl.blackwater.guilds.data.Alliance;
import pl.blackwater.guilds.data.Guild;
import pl.blackwater.guilds.managers.AllianceManager;
import pl.blackwater.guilds.managers.GuildManager;
import pl.blackwater.guilds.settings.GuildConfig;
import pl.blackwaterapi.commands.Command;
import pl.blackwaterapi.utils.TimeUtil;
import pl.blackwaterapi.utils.Util;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class EntityDamageByEntityListener implements Listener,Colors{
	
	@Getter private static final HashMap<Player, Long> punchCooldown = new HashMap<>();
	@Getter private static final Cache<User, Boolean> refreshsword = CacheBuilder.newBuilder().expireAfterWrite(15, TimeUnit.SECONDS).build();


	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPlayerDamageByPlayerHighest(final EntityDamageByEntityEvent event) {
		if(!(event.getEntity() instanceof Player)) {
			return;
		}
		final Player damaged = (Player) event.getEntity();
		if(!(event.getDamager() instanceof Player)) {
			return;
		}
		final Player damager = (Player) event.getDamager();
		if(damaged.isBlocking()){
			final ItemStack damagedItem = damaged.getItemInHand().clone();
			if(damagedItem != null && damagedItem.getType().toString().contains("SWORD")){
				damaged.setItemInHand(null);
				damaged.setItemInHand(damagedItem);
			}
		}
		if(damager.isBlocking()){
			final ItemStack damagerItem = damager.getItemInHand().clone();
			if(damagerItem != null && damagerItem.getType().toString().contains("SWORD")){
				damager.setItemInHand(null);
				damager.setItemInHand(damagerItem);
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
		if (e.isCancelled())
			return;
		if (!(e.getEntity() instanceof Player))
			return;
		if (e.getDamage() < 0.0D) {
			return;
		}
		final Player d = Util.getDamager(e);
		if (d == null)
			return;
		final Player p = (Player) e.getEntity();
		if (p == null)
			return;
		final Combat u = CombatManager.getCombat(p);
		if (u == null)
			return;
		final Combat u1 = CombatManager.getCombat(d);
		if(u1 == null)
			return;
		final User user = UserManager.getUser((Player) e.getEntity());
		if (e.getEntity() instanceof Player) {
			if (user.isGod()) {
				e.setCancelled(true);
				return;
			}
		}
		if (user.isProtection()) {
			e.setCancelled(true);
			Util.sendMsg(d, WarningColor + "Blad: " + WarningColor_2 + "Gracz posiada ochrone !");
			return;
		}
		final User userd = UserManager.getUser(d);
		if (userd.isProtection()) {
			e.setCancelled(true);
			Util.sendMsg(d, WarningColor + "Blad: " + WarningColor_2 + "Posiadasz ochrone, nie mozesz sie bic!");
			Util.sendMsg(p, WarningColor + "Blad: " + WarningColor_2 + "Ten gracz posiada ochrone!");
			//TODO check
			return;
		}
		final Boolean refresh = getRefreshsword().getIfPresent(userd);
		if(refresh == null){
			final ItemStack itemStack = d.getItemInHand();
			if(itemStack.getEnchantments().get(Enchantment.FIRE_ASPECT) != null && itemStack.getEnchantments().get(Enchantment.FIRE_ASPECT) > 1){
				d.getItemInHand().addEnchantment(Enchantment.FIRE_ASPECT, 1);
			}
			if(itemStack.getEnchantments().get(Enchantment.DAMAGE_ALL) != null && itemStack.getEnchantments().get(Enchantment.DAMAGE_ALL) > 4){
				d.getItemInHand().addEnchantment(Enchantment.DAMAGE_ALL, 4);
			}
			if(itemStack.getEnchantments().get(Enchantment.ARROW_DAMAGE) != null && itemStack.getEnchantments().get(Enchantment.ARROW_DAMAGE) > 3){
				d.getItemInHand().addEnchantment(Enchantment.ARROW_DAMAGE, 3);
			}
			if(itemStack.getEnchantments().get(Enchantment.KNOCKBACK) != null){
				d.getItemInHand().removeEnchantment(Enchantment.KNOCKBACK);
			}
			d.updateInventory();
			getRefreshsword().put(userd,true);
		}
		/*if(d.equals(p)) {
			if(!CoreConfig.ENABLEMANAGER_PUNCH_STATUS) {
				Util.sendMsg(p, Util.replaceString(WarningColor + "Blad: " + WarningColor_2 + "Punch jest zablokowany na serwerze!"));
				e.setCancelled(true);
				return;
			}else {
				if(CoreConfig.ENABLEMANAGER_PUNCH_COOLDOWN) {
					if(punchCooldown.containsKey(p)) {
						final long secondsLeft = ((getPunchCooldown().get(p) / 1000) + CoreConfig.ENABLEMANAGER_PUNCH_DELAY) - (System.currentTimeMillis() / 1000);
						if(secondsLeft > 0) {
							Util.sendMsg(p, WarningColor + "Blad: " + WarningColor_2 + "Puncha mozesz uzyc dopiero za " + WarningColor + secondsLeft + WarningColor_2 + "s!");
							e.setCancelled(true);
							return;
						}
					}
					getPunchCooldown().put(p, System.currentTimeMillis());
					return;
				}
			}
		}*/
		if (!u.hasFight())
			Util.sendMsg(p, MessageConfig.ANTYLOGOUTMESSAGE_STARTFIGHT.replace("{SECOND}", String.valueOf(CoreConfig.ESCAPE_TIME)));
		u.setLastAttactTime(System.currentTimeMillis() + TimeUtil.SECOND.getTime(CoreConfig.ESCAPE_TIME));
		u.setLastAttactkPlayer(d);
		if(!u1.hasFight())
			Util.sendMsg(d, MessageConfig.ANTYLOGOUTMESSAGE_STARTFIGHT.replace("{SECOND}", String.valueOf(CoreConfig.ESCAPE_TIME)));
		u1.setLastAttactTime(System.currentTimeMillis() + TimeUtil.SECOND.getTime(CoreConfig.ESCAPE_TIME));
		u1.setLastAttactkPlayer(p);
		if (p != d) {
			if (GuildConfig.GUILDS_CREATE_TIME <= System.currentTimeMillis()) {
				final Guild pg = GuildManager.getGuild(p);
				final Guild dg = GuildManager.getGuild(d);
				if ((dg != null) || (pg != null)) {
					if (dg != pg) {
						final Alliance alliance = AllianceManager.getAlliance(pg, dg);
						if (alliance != null) {
							if (alliance.isPvp()) {
								e.setDamage(0.0D);
							} else {
								e.setCancelled(true);
							}
						}
						return;
					}
					if (pg.isPvp()) {
						e.setDamage(0.0D);
					} else {
						e.setCancelled(true);
					}
				}
			}

		}

	}
}
