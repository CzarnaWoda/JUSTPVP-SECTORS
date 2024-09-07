package pl.blackwater.core.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import pl.blackwater.core.Core;
import pl.blackwater.core.data.ChatControlUser;
import pl.blackwater.core.data.Combat;
import pl.blackwater.core.data.User;
import pl.blackwater.core.enums.ChatControlType;
import pl.blackwater.core.events.CustomEventManager;
import pl.blackwater.core.events.EventType;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.*;
import pl.blackwater.core.settings.CoreConfig;
import pl.blackwater.core.utils.LocationUtil;
import pl.blackwater.core.utils.PlayerInventoryUtil;
import pl.blackwater.guilds.data.Guild;
import pl.blackwater.guilds.data.War;
import pl.blackwater.guilds.managers.GuildManager;
import pl.blackwater.guilds.managers.WarManager;
import pl.blackwaterapi.utils.RandomUtil;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.packets.impl.chat.ChatControlGlobalMessagePacket;
import pl.justsectors.packets.impl.chat.ChatMessagePacket;
import pl.justsectors.packets.impl.customevents.AddStatCustomEventPacket;
import pl.justsectors.packets.impl.guild.GuildAddSoulsPacket;
import pl.justsectors.redis.client.RedisClient;

public class PlayerDeathListener implements Listener,Colors{

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerDeath(PlayerDeathEvent e) {
			e.setDeathMessage(null);
			final Player victim = e.getEntity();
			final Combat c = CombatManager.getCombat(victim);
			final User victimu = UserManager.getUser(victim);
			final ChatControlUser victimcc = ChatControlUserManager.getUser(victimu);
			victimu.addDeathsViaPacket(1); victimu.removeMoneyViaPacket(10); victimu.setKillStreakViaPacket(0);
			ChatControlUserManager.sendMsg(victim, Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Straciles " + WarningColor + "10" + MainColor + " monet z powodu smierci")), victimcc.isDeathMessage());
			Player killer = victim.getKiller();
			if(killer == null && c != null) {
				final Player last = c.getLastAttactkPlayer();
				if(last != null) {
					killer = last;
				}
			}
			CombatManager.removeFight(c);
			if(killer != null) {
				if (killer.getName().equalsIgnoreCase(victim.getName())) {
					Util.sendMsg(killer, WarningColor + "Blad: " + WarningColor_2 + "Nie mozesz zabic samego siebie!");
					return;
				}
				killPlayer(victim, killer);
			}
			respawn(victim);
	}

	
	
	private static void killPlayer(final Player victim, final Player k) {
		final User killer = UserManager.getUser(k);
		final ChatControlUser cc = ChatControlUserManager.getUser(killer);
		//TODO ServerStatsManager.addKills(1);
		killer.addKillsViaPacket(1);
		k.playSound(k.getLocation(), Sound.LEVEL_UP, 2.0F, 3.0F);
		//TODO GUILD VIA PACKET
		final Guild g = killer.getGuild();
		if(g != null) {
			int souls = 1;
			final Guild o = UserManager.getUser(victim.getUniqueId()).getGuild();
			if(o != null && g != o){
				souls += RandomUtil.getRandInt(1,4);
			}
			final GuildAddSoulsPacket packet = new GuildAddSoulsPacket(g.getTag(),souls);
			RedisClient.sendSectorsPacket(packet);
		}
		final int rankmoney = (killer.getRank().equalsIgnoreCase("SVIP") ? CoreConfig.DROPMANAGER_MONEY_SVIP : (killer.getRank().equalsIgnoreCase("VIP") ? CoreConfig.DROPMANAGER_MONEY_VIP : CoreConfig.DROPMANAGER_MONEY_DEFAULT));
		int earnmoney = (int)(rankmoney + ((rankmoney * Math.min(1,killer.getKillStreak()*0.004D))));
		//250 killstreak max
		if(killer.getBoostMoney() != 0){
			earnmoney += (killer.getBoostMoney() == 1 ? 10 : (killer.getBoostMoney() == 2 ? 20 : 30));
		}
		killer.addKillStreakViaPacket(1);
		ChatControlUserManager.sendMsg(k, Util.replaceString(SpecialSigns + ">> " + MainColor + "Aktualnie posiadasz " + WarningColor_2 + killer.getKillStreak() + MainColor + " killstreak!"), cc.isKillKillStreakMessage());
		ChatControlUserManager.sendMsg(k, Util.replaceString(SpecialSigns + ">> " + MainColor + "Otrzymales " + WarningColor_2 + earnmoney + " monet " + MainColor + "za zabicie gracza! " + (killer.isPremium() ? SpecialSigns + "(" + ImportantColor + "Premium" + SpecialSigns + ")" : "")), cc.isKillMoneyLevelMessage());
		killer.addMoneyViaPacket(earnmoney);
		int levelAmount = (killer.getRank().equalsIgnoreCase("SVIP") ? CoreConfig.DROPMANAGER_LEVEL_SVIP : (killer.getRank().equalsIgnoreCase("VIP") ? CoreConfig.DROPMANAGER_LEVEL_VIP : CoreConfig.DROPMANAGER_LEVEL_DEFAULT));
		if(killer.getBoostLvL() != 0){
			final int boostlvl = killer.getBoostLvL();
			levelAmount += boostlvl;
		}
		if(levelAmount > 0) {
			ChatControlUserManager.sendMsg(k, Util.replaceString(SpecialSigns + ">> " + MainColor + "Otrzymales " + WarningColor_2 + levelAmount + " lvl exp " + MainColor + "za zabicie gracza! " + (killer.isPremium() ? SpecialSigns + "(" + ImportantColor + "Premium" + SpecialSigns + ")" : "")), cc.isKillMoneyLevelMessage());
			k.giveExpLevels(levelAmount);
		}
		final float exp = RandomUtil.getRandFloat(CoreConfig.DROPMANAGER_EXP_MIN, CoreConfig.DROPMANAGER_EXP_MAX);
		killer.addExpViaPacket(exp);
		if(killer.getExp() >= killer.getExpToLevel()) {
			killer.removeExpViaPacket((float) killer.getExpToLevel());
			killer.addLevelViaPacket(1);
			ChatControlUserManager.sendMsg(k, Util.replaceString(SpecialSigns + ">> " + MainColor + "Awansowales na " + ImportantColor + killer.getLevel() + MainColor + " level"), cc.isKillMoneyLevelMessage());
			k.spigot().playEffect(k.getLocation().add(0.0, 2.0, 0.0), Effect.COLOURED_DUST, 8, 0, 0.8f, 0.8f, 0.8f, 0.04f, 125, 100);
			k.playSound(k.getLocation(), Sound.ANVIL_USE, 40, 50);
			final int money = (killer.getLevel() > 15 ? 100 : 50);
			killer.addMoneyViaPacket(money);
			if(CoreConfig.LEVELMANAGER_BROADCASTATLEVEL.contains(killer.getLevel())) {
				k.getWorld().spigot().playEffect(k.getLocation().add(0.0, 2.0, 0.0), Effect.MAGIC_CRIT, 5, 10, 0.8f, 0.4f, 0.7f, 0.05f, 120, 750);
				final ChatMessagePacket c = new ChatMessagePacket(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + WarningColor_2 +  BOLD + "Gratulacje! " + MainColor + "gracz " + ImportantColor + killer.getLastName() + MainColor + " zdobyl " + ImportantColor + killer.getLevel() + MainColor + " level")));
				RedisClient.sendSectorsPacket(c);
			}
		}
		final double keyChance = (killer.getRank().equalsIgnoreCase("SVIP") ? CoreConfig.DROPMANAGER_KEY_CHANCE_SVIP : (killer.getRank().equalsIgnoreCase("VIP") ? CoreConfig.DROPMANAGER_KEY_CHANCE_VIP : CoreConfig.DROPMANAGER_KEY_CHANCE_DEFAULT));
		if(RandomUtil.getChance(keyChance)) {
			ChatControlUserManager.sendMsg(k, Util.replaceString(SpecialSigns + ">> " + MainColor + "Otrzymales " + WarningColor_2 + "klucz " + MainColor + "za zabicie gracza! " + (killer.isPremium() ? SpecialSigns + "(" + ImportantColor + "Premium" + SpecialSigns + ")" : "")), cc.isKillKeyMessage());
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "case givekey " + k.getName() + " justpvp 1");
		}
		final int diamondAmount = (killer.getRank().equalsIgnoreCase("SVIP") ? CoreConfig.DROPMANAGER_DIAMOND_SVIP : (killer.getRank().equalsIgnoreCase("VIP") ? CoreConfig.DROPMANAGER_DIAMOND_VIP : CoreConfig.DROPMANAGER_DIAMOND_DEFAULT));
		final ItemStack item = new ItemStack(Material.DIAMOND,diamondAmount);
		k.getInventory().addItem(item);
		ChatControlUserManager.sendMsg(k, Util.replaceString(SpecialSigns + ">> " + MainColor + "Otrzymales " + WarningColor_2 + "diament " + MainColor + "za zabicie gracza! " + (killer.isPremium() ? SpecialSigns + "(" + ImportantColor + "Premium" + SpecialSigns + ")" : "")), cc.isKillDiamondMessage());
		if(killer.getEventTurboMoney() == 2) {
			final int money = (killer.isPremium() ? 25 : 15);
			killer.addMoneyViaPacket(money);
			ChatControlUserManager.sendMsg(k, Util.replaceString(SpecialSigns + ">> " + MainColor + "Otrzymujesz dodatkowe " + ImportantColor + money + MainColor + " monet " + SpecialSigns + "(" + WarningColor + "TurboMoney Tryb GRACZ" + SpecialSigns + ")"), cc.isKillTurboMoneyMessage());
		}
		if(EventManager.isOnTurboMoney()) {
			final int money = (killer.isPremium() ? 25 : 15);
			killer.addMoneyViaPacket(money);
			ChatControlUserManager.sendMsg(k, Util.replaceString(SpecialSigns + ">> " + MainColor + "Otrzymujesz dodatkowe " + ImportantColor + money + MainColor + " monet " + SpecialSigns + "(" + WarningColor + "TurboMoney Tryb ALL" + SpecialSigns + ")"), cc.isKillTurboMoneyMessage());
		}
		if(CoreConfig.KILLSTREAKMANAGER_BROADCASTKILLSTREAK.contains(killer.getKillStreak())) {
			final ChatControlGlobalMessagePacket packet = new ChatControlGlobalMessagePacket(Util.replaceString(SpecialSigns + ">> " + WarningColor_2 +  BOLD + "Gratulacje! " + MainColor + "gracz " + ImportantColor + killer.getLastName() + MainColor + " zdobyl " + ImportantColor + killer.getKillStreak() + MainColor + " killstreak"), ChatControlType.KILLSTREAKGLOBAL);
			RedisClient.sendSectorsPacket(packet);
			k.getWorld().spigot().playEffect(k.getLocation().add(0.0, 2.0, 0.0), Effect.FLAME, 5, 10, 0.8f, 0.4f, 0.7f, 0.05f, 120, 750);
		}
		if(killer.getBoostKill() == 1) {
			k.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100,0));
		}else if(killer.getBoostKill() == 2) {
      	  	k.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200,0));
      	  	k.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100,0));
		}else if(killer.getBoostKill() == 3) {
			k.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 300,0));
			k.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200,0));
		}
		if(CustomEventManager.getActiveEvent() != null  && CustomEventManager.getActiveEvent().getEventType().equals(EventType.KILLS)){
			final AddStatCustomEventPacket packet = new AddStatCustomEventPacket(killer.getUuid(), 1);
			RedisClient.sendSectorsPacket(packet);
		}
		Bukkit.getScheduler().runTaskLaterAsynchronously(Core.getPlugin(), () -> {
			final Combat c = CombatManager.getCombat(victim);
			c.setLastAttactTime(0L);
			c.setLastAttactkPlayer(null);
		},15L);
	}
	private static void respawn(final Player victim) {
		Bukkit.getScheduler().runTaskLater(Core.getPlugin(), () -> {
			if(victim != null && victim.isOnline() && victim.isDead()) {
				victim.spigot().respawn();
				PlayerInventoryUtil.DefaultPlayerInventory(victim);
				LocationUtil.TeleportToLocationWithDelay(victim,LocationUtil.getRandSpawnLocation(CoreConfig.getSpawnLocations()),1);
			}
		},5L);
		}
	}

