package pl.blackwater.core.listeners;

import lombok.Getter;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Button;
import pl.blackwater.core.Core;
import pl.blackwater.core.commands.SpecialItemCommand;
import pl.blackwater.core.data.Combat;
import pl.blackwater.core.data.Rank;
import pl.blackwater.core.data.TreasureChest;
import pl.blackwater.core.data.User;
import pl.blackwater.core.enums.ChestType;
import pl.blackwater.core.events.CustomEventManager;
import pl.blackwater.core.events.EventType;
import pl.blackwater.core.halloween.HalloweenManager;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.*;
import pl.blackwater.core.settings.CoreConfig;
import pl.blackwaterapi.utils.ItemUtil;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.packets.impl.customevents.AddStatCustomEventPacket;
import pl.justsectors.redis.client.RedisClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

public class PlayerInteractListener implements Listener,Colors{
	
	@Getter private static final HashMap<Player, Long> enderPearlCooldown = new HashMap<>();
	@Getter private static HashMap<UUID,Long> times = new HashMap<>();

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		final Action a = e.getAction();
		final Player p = e.getPlayer();
		if(a.equals(Action.LEFT_CLICK_AIR)){
			int count = 1;
			if(AntyMacroManager.getClickCount().get(p.getUniqueId()) != null) {
				count += AntyMacroManager.getClickCount().get(p.getUniqueId());
			}
			AntyMacroManager.clickCount.put(p.getUniqueId(), count);
		}
		if(a.equals(Action.RIGHT_CLICK_BLOCK)) {
			final Block b = e.getClickedBlock();
			if (b.getType().equals(Material.STONE_BUTTON)) {
				final Button button = (Button) b.getState().getData();
				if (b.getRelative(button.getAttachedFace()).getType().equals(Material.ENDER_PORTAL_FRAME)) {
					if (WarpManager.getWarpByPex(e.getPlayer(), "core.mapa").size() < 1) {
						return;
					}
					WarpManager.getRandomWarpFastTP(e.getPlayer());
				}
			}
			if(b.getType().equals(Material.BED)){
				if (!p.hasPermission("core.admin")) {
					Util.sendMsg(p, WarningColor + "Blad: " + WarningColor_2 + "Nie posiadasz uprawnien aby to zrobic!");
					e.setCancelled(true);
					return;
				}
			}
			if (b.getType().equals(Material.ENDER_CHEST)) {
				final Combat c = CombatManager.getCombat(p);
				if (c.hasFight()) {
					Util.sendMsg(p, WarningColor + "Blad: " + WarningColor_2 + "Nie mozesz otwierac enderchest'a podczas walki!");
					e.setCancelled(true);
					return;
				}
			}
			if (b.getType().equals(Material.BREWING_STAND)) {
				if (!p.hasPermission("core.admin")) {
					Util.sendMsg(p, WarningColor + "Blad: " + WarningColor_2 + "Nie posiadasz uprawnien aby to zrobic!");
					e.setCancelled(true);
					return;
				}
			}
			if (b.getType().equals(Material.PUMPKIN) && HalloweenManager.deletePumpkin(b.getLocation())) {
				ItemUtil.giveItems(Collections.singletonList(Core.getSpecialItemsManager().getPumpkin(1)), p);
				if(CustomEventManager.getActiveEvent() != null && CustomEventManager.getActiveEvent().getEventType() == EventType.PUMPKIN){
					final AddStatCustomEventPacket packet = new AddStatCustomEventPacket(p.getUniqueId(),1);
					RedisClient.sendSectorsPacket(packet);
				}
			}

			if (b.getType().equals(Material.ANVIL)) {
				b.setType(Material.ANVIL);
				p.getWorld().spigot().playEffect(b.getLocation(), Effect.HEART, 145, 0, 0.3f, 0.3f, 0.3f, 0.05f, 10, 100);
			}
			/*if (b.getType().equals(Material.ENCHANTMENT_TABLE)) {
				if (CoreConfig.CREATEMANAGER_ENCHANT_TIME > System.currentTimeMillis()) {
					Util.sendMsg(p, WarningColor + "Blad: " + WarningColor_2 + "Enchantownie przedmiotow bedzie mozliwe dopiero za: " + WarningColor + Util.secondsToString(((int) (CoreConfig.CREATEMANAGER_ENCHANT_TIME - System.currentTimeMillis()) / 1000)));
					e.setCancelled(true);
				}
			}*/
			if (b.getState() instanceof InventoryHolder) {
				if(times.get(p.getUniqueId()) != null && times.get(p.getUniqueId()) > System.currentTimeMillis()){
					Util.sendMsg(p, "&4Blad: &cNie mozesz tego robic tak szybko!");
					e.setCancelled(true);
					return;
				}
				times.put(p.getUniqueId(), System.currentTimeMillis()+1000L);
				final Location location = e.getClickedBlock().getLocation();
				final TreasureChest chest = Core.getTreasureChestManager().getChest(location);
				if (chest != null) {
					p.playEffect(chest.getLocation(), Effect.POTION_BREAK, 16450);
					if (chest.getType() == ChestType.GEN_NORMAL) {
						chest.openNormalChest((Chest) e.getClickedBlock().getState(), UserManager.getUser(p));
					}
					if (chest.getType() == ChestType.UNLIMITED) {
						e.setCancelled(true);
						chest.openUnlimitedChest(p);
					}
					if (chest.getType() == ChestType.STATIC) {
						e.setCancelled(true);
						chest.openStaticChest(p);
					}
				}
			}
		}
		if(!CoreConfig.ENABLEMANAGER_PUNCH_STATUS) {
			if(e.getItem() != null && e.getItem().getType().equals(Material.ENDER_PEARL)){
				if(getEnderPearlCooldown().get(p) != null) {
					final long secondsleft = getEnderPearlCooldown().get(p) / 1000 + CoreConfig.ENABLEMANAGER_ENDERPEARL_DELAY - (System.currentTimeMillis() / 1000);
					if(secondsleft > 0) {
						Util.sendMsg(p, WarningColor + "Blad: " + WarningColor_2 + "Perle endermana mozesz rzucic dopiero za " + WarningColor + secondsleft + WarningColor_2 + "s");
						e.setCancelled(true);
						return;
					}else
						enderPearlCooldown.put(p, System.currentTimeMillis());
				}
			}
			if(a.equals(Action.RIGHT_CLICK_AIR) || a.equals(Action.RIGHT_CLICK_BLOCK)) {
				final ItemStack itemStack = e.getPlayer().getItemInHand();
				if(itemStack.getType().equals(Material.POTION) && (itemStack.getDurability() == (short)41 || itemStack.getDurability() == (short)16425 || itemStack.getDurability() == (short)16457 || itemStack.getDurability() == (short)8265 || itemStack.getDurability() == (short)8233 || itemStack.getDurability() == (short)8201)) {
					Util.sendMsg(p, WarningColor + "Blad: " + WarningColor_2 + "Ta mikstura zostala zablokowana na serwerze!");
					p.getInventory().remove(itemStack);
					e.setCancelled(true);
					return;
				}
				if(Core.getSpecialItemsManager().checkDropVoucher(itemStack)){
					double addDrop = Double.parseDouble(itemStack.getItemMeta().getLore().get(1).replace(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Voucher dodaje: " + ImportantColor)), ""));
					final Player player = e.getPlayer();
					final User user = UserManager.getUser(player);
					if(user.getBonusDrop() > 5.0){
						user.setBonusDropViaPacket((float) 5.0);
					}
					if(user.getBonusDrop() >= 5.0){
						Util.sendMsg(player, Util.fixColor(Util.replaceString(WarningColor + "Blad: " + WarningColor_2 + "Maksymalnie vouchery na drop moga dodac ci w sumie 5.0%")));
						return;
					}
					addDrop = addDrop*itemStack.getAmount();
					ItemUtil.removeItems(Collections.singletonList(itemStack),player);
					user.addBonusDropViaPacket((float) addDrop);
					player.getWorld().spigot().playEffect(player.getLocation().clone().add(0.0, 1.5, 0.0), Effect.COLOURED_DUST, 5, 10, 0.8f, 0.4f, 0.7f, 0.05f, 35, 750);
					Util.sendMsg(player, Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "Uzyles vouchera na" + ImportantColor + " drop " + MainColor + ", dodal on tobie " + ImportantColor + addDrop + MainColor + " % do dropu!")));
					if(user.getBonusDrop() >= 5.0){
						user.setBonusDropViaPacket((float) 5.0);
					}
				}
				if(Core.getSpecialItemsManager().checkMoneyVoucher(itemStack)){
					final int money = itemStack.getAmount() * Integer.parseInt(itemStack.getItemMeta().getLore().get(1).replace(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Voucher dodaje: " + ImportantColor)), ""));
					final Player player = e.getPlayer();
					final User user = UserManager.getUser(player);
					ItemUtil.removeItems(Collections.singletonList(itemStack),player);
					user.addMoneyViaPacket(money);
					player.getWorld().spigot().playEffect(player.getLocation().clone().add(0.0, 1.5, 0.0), Effect.MAGIC_CRIT, 5, 10, 0.8f, 0.4f, 0.7f, 0.05f, 100, 750);
					Util.sendMsg(player, Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "Uzyles vouchera na" + ImportantColor + " monety " + MainColor + ", dodal on tobie " + ImportantColor + money + MainColor + " monet!")));
				}
				if(Core.getSpecialItemsManager().checkRankVoucher(itemStack)){
					final Rank rank = Core.getRankManager().getRank(itemStack.getItemMeta().getLore().get(1).replace(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Voucher nadaje: " + ImportantColor)), ""));
					final Player player = e.getPlayer();
					final User user = UserManager.getUser(player);
					final long time = Util.parseDateDiff(itemStack.getItemMeta().getLore().get(0).replace(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Voucher nadaje daną range " + ImportantColor + "na ")), ""), true);
					if(RankSetManager.getSetRank(user) != null){
						RankSetManager.removeRank(user, RankSetManager.getSetRank(user));
					}
					ItemUtil.removeItems(Collections.singletonList(itemStack),player);
					RankSetManager.setRank(user,"VOUCHER",rank,time);
					player.getWorld().spigot().playEffect(player.getLocation().clone().add(0.0, 1.5, 0.0), Effect.FLAME, 5, 10, 0.8f, 0.4f, 0.7f, 0.05f, 125, 750);
					Util.sendMsg(player, Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "Uzyles vouchera na" + ImportantColor + " range " + MainColor + ", nadal on tobie range " + ImportantColor + rank.getName() + MainColor + " do " + ImportantColor + Util.getDate(time) + MainColor +"!")));
				}
				if(itemStack.getType().equals(Material.BOW)) {
					if (itemStack.getItemMeta().hasEnchant(Enchantment.ARROW_KNOCKBACK)) {
						if (itemStack.getEnchantments().get(Enchantment.ARROW_DAMAGE) > 3) {
							itemStack.addEnchantment(Enchantment.ARROW_DAMAGE, 3);
						}
					}
					if (itemStack.getItemMeta().hasEnchant(Enchantment.ARROW_KNOCKBACK) && !CoreConfig.ENABLEMANAGER_PUNCH_STATUS) {
						Util.sendMsg(p, WarningColor + "Blad: " + WarningColor_2 + "Punch jest wylaczony na serwerze!");
						e.setCancelled(true);
					}
				}
					if(itemStack.getType().equals(Material.FISHING_ROD) && !CoreConfig.ENABLEMANAGER_FISHINGROD){
						Util.sendMsg(p, WarningColor + "Blad: " + WarningColor_2 + "Wedki sa wylaczone!");
						e.setCancelled(true);
					}else if(itemStack.getType().equals(Material.FISHING_ROD)){
						if(itemStack.getDurability() >= (short)20){
							itemStack.setDurability((short)19);
							p.updateInventory();
						}
					}
					if(itemStack.getType().equals(Material.SNOW_BALL) && !CoreConfig.ENABLEMANAGER_SNOWBALL){
						Util.sendMsg(p, WarningColor + "Blad: " + WarningColor_2 + "Sniezki sa wylaczone!");
						e.setCancelled(true);
					}
			}
		}
	}
}
