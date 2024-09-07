package pl.blackwater.core.inventories;

import com.google.common.collect.ImmutableList;
import com.sk89q.worldguard.protection.managers.storage.StorageException;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import pl.blackwater.chestpvpdrop.managers.DropFile;
import pl.blackwater.chestpvpdrop.managers.DropManager;
import pl.blackwater.chestpvpdrop.settings.Config;
import pl.blackwater.core.Core;
import pl.blackwater.core.commands.AdminPanelCommand;
import pl.blackwater.core.commands.FanPageCommand;
import pl.blackwater.core.commands.YouTubeCommand;
import pl.blackwater.core.data.User;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.ServerStatsManager;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.core.settings.*;
import pl.blackwater.core.utils.ColoredMaterialsUtil;
import pl.blackwater.guilds.data.Guild;
import pl.blackwater.guilds.managers.GuildManager;
import pl.blackwater.guilds.settings.EffectsConfig;
import pl.blackwater.guilds.settings.GuildConfig;
import pl.blackwater.storage.settings.StorageConfig;
import pl.blackwaterapi.configs.ConfigCreator;
import pl.blackwaterapi.configs.ConfigManager;
import pl.blackwaterapi.gui.actions.InventoryGUI;
import pl.blackwaterapi.utils.ActionBarUtil;
import pl.blackwaterapi.utils.ItemBuilder;
import pl.blackwaterapi.utils.ItemUtil;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.packets.impl.config.LoadConfigPacket;
import pl.justsectors.packets.impl.config.SetConfigBooleanPacket;
import pl.justsectors.redis.client.RedisClient;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class AdminPanelInventory implements Colors
{
	public static void openMenu(Player player) {
		final InventoryGUI inv = new InventoryGUI(Core.getPlugin(), Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Panel" + ImportantColor + " Administracyjny" + SpecialSigns + " <<")), 3);
		final ItemBuilder chat = new ItemBuilder(Material.BOOK).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Zarzadzanie " + ImportantColor + "chatem" + SpecialSigns + " *")));
		final ItemBuilder others = new ItemBuilder(Material.SLIME_BLOCK).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Zarzadzanie " + ImportantColor + "funkcjami administratora" + SpecialSigns + " *")));
		final ItemBuilder global = new ItemBuilder(Material.CHEST).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Zarzadzanie " + ImportantColor + "funkcjami globalnymi" + SpecialSigns + " *")));
		final ItemBuilder configs = new ItemBuilder(Material.PAPER).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Zarzadzanie " + ImportantColor + "konfiguracjami serwer'a" + SpecialSigns + " *")));
		final ItemBuilder server = new ItemBuilder(Material.OBSIDIAN).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Zarzadzanie " + ImportantColor + "serwerem" + SpecialSigns + " *")));
		final ItemBuilder purple = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)10).setTitle("§2");
		final ItemBuilder cyan = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)9).setTitle("§2");
		final ItemBuilder black = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)15).setTitle("§2");
		final ItemBuilder red = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)14).setTitle("§2");
		final ItemBuilder brown = ColoredMaterialsUtil.getStainedGlassPane((short)12);
		inv.setItem(0, purple.build(), null);
		inv.setItem(9, chat.build(), (p, arg1, arg2, arg3) -> {
			p.closeInventory();
			ChatInventory.openMenu(p);

		});
		inv.setItem(18, purple.build(), null);
		inv.setItem(2, cyan.build(), null);
		inv.setItem(11, others.build(), (p, arg1, arg2, arg3) -> {
			p.closeInventory();
			openOthers(p);

		});
		inv.setItem(20, cyan.build(), null);
		inv.setItem(4, red.build(), null);
		inv.setItem(13, global.build(), (p, arg1, arg2, arg3) -> {
			p.closeInventory();
			openGlobal(p);

		});
		inv.setItem(22, red.build(), null);
		inv.setItem(6, black.build(), null);
		inv.setItem(15, configs.build(), (p, arg1, arg2, arg3) -> {
			p.closeInventory();
			openConfigsMenu(p);
		});
		inv.setItem(24, black.build(), null);
		inv.setItem(8, brown.build(), null);
		inv.setItem(17, server.build(), (p, arg1, arg2, arg3) -> {
			p.closeInventory();
			openServerManagmentMenu(p);
		});
		inv.setItem(26, brown.build(), null);
		inv.openInventory(player);
		
		
	}
	private static void openOthers(Player player) {
		final InventoryGUI inv = new InventoryGUI(Core.getPlugin(), Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Panel " + ImportantColor + "funkcji administratora" + SpecialSigns + " <<")), 1);
		final ItemBuilder fly = new ItemBuilder(Material.FEATHER).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Fly toggle")))
					.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "    * " + MainColor + "Status: " + (player.getAllowFlight() ? ChatColor.GREEN + "%V%" : ChatColor.DARK_RED + "%X%"))));
		final ItemBuilder god = new ItemBuilder(Material.BEDROCK).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "God toggle")))
					.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "    * " + MainColor + "Status: " + (UserManager.getUser(player).isGod() ? ChatColor.GREEN + "%V%" : ChatColor.DARK_RED + "%X%"))));
		final ItemBuilder heal = new ItemBuilder(Material.POTION,1,(short)16421).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Ulecz sie")));
		final ItemBuilder back = new ItemBuilder(Material.WOOL,1,(short)14).setTitle(Util.replaceString(SpecialSigns + "* " + WarningColor_2 + "Powrot" + SpecialSigns + " *"));
		final boolean status = AdminPanelCommand.getAR_ADMINS().contains(player);
		final ItemBuilder ar = new ItemBuilder(Material.ANVIL).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Anty Reklama toggle")))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "    * " + MainColor + "Status: " + (status ? ChatColor.GREEN + "%V%" : ChatColor.DARK_RED + "%X%"))));
		final boolean statuss = AdminPanelCommand.getSS_ADMINS().contains(player);
		final ItemStack ss = ItemUtil.getPlayerHead("CzarnaWoda");
		final ItemMeta ssMeta = ss.getItemMeta();
		ssMeta.setDisplayName(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "SocialSpy toggle")));
		ssMeta.setLore(Collections.singletonList(Util.fixColor(Util.replaceString(SpecialSigns + "    * " + MainColor + "Status: " + (statuss ? ChatColor.GREEN + "%V%" : ChatColor.DARK_RED + "%X%")))));
		ss.setItemMeta(ssMeta);
		inv.setItem(0, fly.build(), (p, arg1, arg2, item) -> {
			p.setAllowFlight(!p.getAllowFlight());
			ItemMeta meta = item.getItemMeta();
			meta.setLore(Collections.singletonList(Util.fixColor(Util.replaceString(SpecialSigns + "    * " + MainColor + "Status: " + (p.getAllowFlight() ? ChatColor.GREEN + "%V%" : ChatColor.DARK_RED + "%X%")))));
			item.setItemMeta(meta);
		});
		inv.setItem(1, god.build(), (p, arg1, arg2, item) -> {
			User u = UserManager.getUser(p);
			u.setGod(!u.isGod());
			ItemMeta meta = item.getItemMeta();
			meta.setLore(Collections.singletonList(Util.fixColor(Util.replaceString(SpecialSigns + "    * " + MainColor + "Status: " + (u.isGod() ? ChatColor.GREEN + "%V%" : ChatColor.DARK_RED + "%X%")))));
			item.setItemMeta(meta);
		});
		inv.setItem(inv.get().getSize() - 1, back.build(), (p, arg1, arg2, arg3) -> {
			p.closeInventory();
			openMenu(p);
		});
		inv.setItem(2, heal.build(), (p, arg1, arg2, arg3) -> {
			p.setHealth(20.0);
			p.setFoodLevel(20);
			p.setFireTicks(0);
			for (PotionEffect potionEffect : p.getActivePotionEffects()) {
				p.removePotionEffect(potionEffect.getType());
			}

		});
		inv.setItem(3, ar.build(), (p, arg1, arg2, item) -> {
			final boolean status1 = AdminPanelCommand.getAR_ADMINS().contains(p);
			if(status1)
				AdminPanelCommand.getAR_ADMINS().remove(p);
			else
				AdminPanelCommand.getAR_ADMINS().add(p);
			ItemMeta meta = item.getItemMeta();
			final boolean finalStatus = AdminPanelCommand.getAR_ADMINS().contains(p);
			meta.setLore(Collections.singletonList(Util.fixColor(Util.replaceString(SpecialSigns + "    * " + MainColor + "Status: " + (finalStatus ? ChatColor.GREEN + "%V%" : ChatColor.DARK_RED + "%X%")))));
			item.setItemMeta(meta);
		});
		inv.setItem(4, ss, (p, arg1, arg2, item) -> {
			final boolean status12 = AdminPanelCommand.getSS_ADMINS().contains(p);
			if(status12)
				AdminPanelCommand.getSS_ADMINS().remove(p);
			else
				AdminPanelCommand.getSS_ADMINS().add(p);
			ItemMeta meta = item.getItemMeta();
			final boolean finalStatus = AdminPanelCommand.getSS_ADMINS().contains(p);
			meta.setLore(Collections.singletonList(Util.fixColor(Util.replaceString(SpecialSigns + "    * " + MainColor + "Status: " + (finalStatus ? ChatColor.GREEN + "%V%" : ChatColor.DARK_RED + "%X%")))));
			item.setItemMeta(meta);
		});
		inv.openInventory(player);
	}
	public static void openGlobal(Player player){
		final InventoryGUI inv = new InventoryGUI(Core.getPlugin(), Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Panel " + ImportantColor + "funkcji globalnych" + SpecialSigns + " <<")), 1);
		final ItemBuilder pvp = new ItemBuilder(Material.DIAMOND_SWORD).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Global PvP toggle")))
					.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "    * " + MainColor + "Status: " + (player.getWorld().getPVP() ? ChatColor.GREEN + "%V%" : ChatColor.DARK_RED + "%X%"))));
		final ItemBuilder back = new ItemBuilder(Material.WOOL,1,(short)14).setTitle(Util.replaceString(SpecialSigns + "* " + WarningColor_2 + "Powrot" + SpecialSigns + " *"));
		//final ItemBuilder unbanall = new ItemBuilder(Material.BARRIER).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "UnBan ALL")))
					//.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "    * " + MainColor + "Zwykle bany na serwerze: " + ImportantColor + BanManager.getBans().size())))
					//.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "    * " + MainColor + "Bany IP na serwerze: " + ImportantColor + BanIPManager.getBansIp().size())));
		final ItemBuilder kits = new ItemBuilder(Material.DIAMOND_CHESTPLATE).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Zarzadzanie zestawami")))
					.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "    * " + MainColor + "Kit Jedzenie: " + ImportantColor + (CoreConfig.KIT_JEDZENIE_STATUS ? ChatColor.GREEN + "%V%" : ChatColor.DARK_RED + "%X%"))))
					.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "    * " + MainColor + "Kit VIP: " + ImportantColor + (CoreConfig.KIT_VIP_STATUS ? ChatColor.GREEN + "%V%" : ChatColor.DARK_RED + "%X%"))))
					.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "    * " + MainColor + "Kit SVIP: " + ImportantColor + (CoreConfig.KIT_SVIP_STATUS ? ChatColor.GREEN + "%V%" : ChatColor.DARK_RED + "%X%"))))
					.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "    * " + MainColor + "Kit Admin: " + ImportantColor + (CoreConfig.KIT_ADMIN_STATUS ? ChatColor.GREEN + "%V%" : ChatColor.DARK_RED + "%X%"))));
		final ItemBuilder timed = new ItemBuilder(Material.DIAMOND).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Zarzadzanie " + ChatColor.LIGHT_PURPLE + ">>->>" + ImportantColor + " tworzenie diam. itemów")))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "    * " + MainColor + "Aktualnie: " + ImportantColor + (CoreConfig.CREATEMANAGER_DIAMOND_TIME >  System.currentTimeMillis() ? ChatColor.RED + "%X%," + Util.secondsToString((int)((int)(CoreConfig.CREATEMANAGER_DIAMOND_TIME - System.currentTimeMillis())/1000L)) : "%V%"))));
		final ItemBuilder timee = new ItemBuilder(Material.ENCHANTMENT_TABLE).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Zarzadzanie " + ChatColor.LIGHT_PURPLE + ">>->>" + ImportantColor + " zaklinanie itemów")))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "    * " + MainColor + "Aktualnie: " + ImportantColor + (CoreConfig.CREATEMANAGER_ENCHANT_TIME >  System.currentTimeMillis() ? ChatColor.RED + "%X%," + Util.secondsToString((int)((int)(CoreConfig.CREATEMANAGER_ENCHANT_TIME - System.currentTimeMillis())/1000L)) : "%V%"))));
		final ItemBuilder timeg = new ItemBuilder(Material.DRAGON_EGG).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Zarzadzanie " + ChatColor.LIGHT_PURPLE + ">>->>" + ImportantColor + " tworzenie gildii")))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "    * " + MainColor + "Aktualnie: " + ImportantColor + (GuildConfig.GUILDS_CREATE_TIME >  System.currentTimeMillis() ? ChatColor.RED + "%X%," + Util.secondsToString((int)((int)(GuildConfig.GUILDS_CREATE_TIME - System.currentTimeMillis())/1000L)) : "%V%"))));
		final ItemBuilder resetstone = new ItemBuilder(Material.DIAMOND_PICKAXE).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Zarzadzanie " + ChatColor.LIGHT_PURPLE + ">>->>" + ImportantColor + " Zresetowanie wykopanego kamienia")));
		final ItemBuilder teleportOptions = new ItemBuilder(Material.ENDER_PEARL).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Zarzadzanie " + ImportantColor + "funkcjami teleportacji")));
		inv.setItem(0, pvp.build(), (p, arg1, arg2, item) -> {
			World w = p.getWorld();
			w.setPVP(!w.getPVP());
			ItemMeta meta = item.getItemMeta();
			meta.setLore(Collections.singletonList(Util.fixColor(Util.replaceString(SpecialSigns + "    * " + MainColor + "Status: " + (w.getPVP() ? ChatColor.GREEN + "%V%" : ChatColor.DARK_RED + "%X%")))));
			item.setItemMeta(meta);
		});
		inv.setItem(inv.get().getSize() - 1, back.build(), (p, arg1, arg2, arg3) -> {
			p.closeInventory();
			openMenu(p);
		});
		/* TODO inv.setItem(1, unbanall.build(), (p, arg1, arg2, item) -> {
			InventoryGUI inv1 = new InventoryGUI(Core.getPlugin(), Util.fixColor(Util.replaceString(SpecialSigns + "* " + ImportantColor + UnderLined + "Potwierdzenie UnBanAll" + SpecialSigns + " *")), 1);
			ItemBuilder accept = new ItemBuilder(Material.STAINED_CLAY,1,(short)13).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + " >>" + MainColor + " Kliknij aby " + ImportantColor + BOLD + "POTWIERDZIC")));
			ItemBuilder decline = new ItemBuilder(Material.STAINED_CLAY,1,(short)14).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + " >>" + MainColor + " Kliknij aby " + WarningColor + BOLD + "ANULOWAC")));
			inv1.setItem(0, accept.build(), (p1, arg11, arg21, item1) -> {
				Bukkit.broadcastMessage(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Administrator " + ImportantColor +  p1.getName() + MainColor + " usunal wszystkie bany na serwerze " + SpecialSigns + "(" + ImportantColor + BOLD + (BanManager.getBans().size() + BanIPManager.getBansIp().size()) + SpecialSigns + ")")));
				for(Ban b : BanManager.getBans().values())
					BanManager.deleteBan(b);
				for(BanIP ip : BanIPManager.getBansIp().values())
					BanIPManager.deleteBan(ip);
				p1.closeInventory();
				openGlobal(p1);
			});
			inv1.setItem(8, decline.build(), (p12, arg112, arg212, item12) -> {
				p12.closeInventory();
				openGlobal(p12);
			});
			inv1.openInventory(p);
		});*/
		inv.setItem(2, kits.build(), (p, arg1, arg2, arg3) -> {
			p.closeInventory();
			final InventoryGUI inv12 = new InventoryGUI(Core.getPlugin(), Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Panel " + ImportantColor + "zarzadzania zestawami" + SpecialSigns + " <<")), 1);
			final ItemBuilder jedzenie = new ItemBuilder(Material.COOKED_BEEF).setTitle(Util.replaceString(SpecialSigns + ">> " + MainColor + "Kit JEDZENIE toggle"))
						.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "    * " + MainColor + "Status: " + ImportantColor + (CoreConfig.KIT_JEDZENIE_STATUS ? ChatColor.GREEN + "%V%" : ChatColor.DARK_RED + "%X%"))));
			final ItemBuilder vip = new ItemBuilder(Material.IRON_SWORD).setTitle(Util.replaceString(SpecialSigns + ">> " + MainColor + "Kit VIP toggle"))
						.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "    * " + MainColor + "Status: " + ImportantColor + (CoreConfig.KIT_VIP_STATUS ? ChatColor.GREEN + "%V%" : ChatColor.DARK_RED + "%X%"))));
			final ItemBuilder svip = new ItemBuilder(Material.GOLD_SWORD).setTitle(Util.replaceString(SpecialSigns + ">> " + MainColor + "Kit SVIP toggle"))
						.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "    * " + MainColor + "Status: " + ImportantColor + (CoreConfig.KIT_SVIP_STATUS ? ChatColor.GREEN + "%V%" : ChatColor.DARK_RED + "%X%"))));
			final ItemBuilder admin = new ItemBuilder(Material.COMPASS).setTitle(Util.replaceString(SpecialSigns + ">> " + MainColor + "Kit ADMIN toggle"))
						.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "    * " + MainColor + "Status: " + ImportantColor + (CoreConfig.KIT_ADMIN_STATUS ? ChatColor.GREEN + "%V%" : ChatColor.DARK_RED + "%X%"))));
			final ItemBuilder back1 = new ItemBuilder(Material.WOOL,1,(short)14).setTitle(Util.replaceString(SpecialSigns + "* " + WarningColor_2 + "Powrot" + SpecialSigns + " *"));
			inv12.setItem(0, jedzenie.build(), (p13, arg113, arg213, item) -> {
				p13.closeInventory();
				final SetConfigBooleanPacket packet = new SetConfigBooleanPacket("coreconfig.yml",!CoreConfig.KIT_JEDZENIE_STATUS,"kit.jedzenie.status",true,player.getDisplayName());
				RedisClient.sendSectorsPacket(packet);
				final LoadConfigPacket packet1 = new LoadConfigPacket("coreconfig.yml");
				RedisClient.sendSectorsPacket(packet1);
				final ItemMeta meta = item.getItemMeta();
				meta.setLore(Collections.singletonList(Util.fixColor(Util.replaceString(SpecialSigns + "    * " + MainColor + "Status: " + ImportantColor + (CoreConfig.KIT_JEDZENIE_STATUS ? ChatColor.GREEN + "%V%" : ChatColor.DARK_RED + "%X%")))));
				item.setItemMeta(meta);
			});
			inv12.setItem(1, vip.build(), (p14, arg114, arg214, item) -> {
				p14.closeInventory();
				final SetConfigBooleanPacket packet = new SetConfigBooleanPacket("coreconfig.yml",!CoreConfig.KIT_VIP_STATUS,"kit.vip.status",true,p14.getDisplayName());
				final LoadConfigPacket packet1 = new LoadConfigPacket("coreconfig.yml");
				RedisClient.sendSectorsPacket(packet);
				RedisClient.sendSectorsPacket(packet1);
				final ItemMeta meta = item.getItemMeta();
				meta.setLore(Collections.singletonList(Util.fixColor(Util.replaceString(SpecialSigns + "    * " + MainColor + "Status: " + ImportantColor + (CoreConfig.KIT_VIP_STATUS ? ChatColor.GREEN + "%V%" : ChatColor.DARK_RED + "%X%")))));
				item.setItemMeta(meta);
			});
			inv12.setItem(2, svip.build(), (p15, arg115, arg215, item) -> {
				p15.closeInventory();
				final SetConfigBooleanPacket packet = new SetConfigBooleanPacket("coreconfig.yml",!CoreConfig.KIT_SVIP_STATUS,"kit.svip.status",true,p15.getDisplayName());
				final LoadConfigPacket packet1 = new LoadConfigPacket("coreconfig.yml");
				RedisClient.sendSectorsPacket(packet);
				RedisClient.sendSectorsPacket(packet1);
				final ItemMeta meta = item.getItemMeta();
				meta.setLore(Collections.singletonList(Util.fixColor(Util.replaceString(SpecialSigns + "    * " + MainColor + "Status: " + ImportantColor + (CoreConfig.KIT_SVIP_STATUS ? ChatColor.GREEN + "%V%" : ChatColor.DARK_RED + "%X%")))));
				item.setItemMeta(meta);
			});
			inv12.setItem(3, admin.build(), (p17, arg117, arg217, item) -> {
				p17.closeInventory();
				final SetConfigBooleanPacket packet = new SetConfigBooleanPacket("coreconfig.yml",!CoreConfig.KIT_ADMIN_STATUS,"kit.admin.status",true,p17.getDisplayName());
				final LoadConfigPacket packet1 = new LoadConfigPacket("coreconfig.yml");
				RedisClient.sendSectorsPacket(packet);
				RedisClient.sendSectorsPacket(packet1);
				final ItemMeta meta = item.getItemMeta();
				meta.setLore(Collections.singletonList(Util.fixColor(Util.replaceString(SpecialSigns + "    * " + MainColor + "Status: " + ImportantColor + (CoreConfig.KIT_ADMIN_STATUS ? ChatColor.GREEN + "%V%" : ChatColor.DARK_RED + "%X%")))));
				item.setItemMeta(meta);
			});
			inv12.setItem(inv12.get().getSize() - 1, back1.build(), (p18, arg118, arg218, arg31) -> {
				p18.closeInventory();
				openGlobal(p18);
			});
			inv12.openInventory(p);
		});
		inv.setItem(3, timed.build(), (p, arg1, arg2, arg3) -> {
			p.closeInventory();
			ActionBarUtil.sendActionBar(p, Util.fixColor(Util.replaceString(SpecialSigns + ">> " + WarningColor + "Wpisz czas albo 'cancel'" + SpecialSigns + " <<")));
			AdminPanelCommand.getAdmin_changeTIMED().add(p);
		});
		inv.setItem(4, timee.build(), (p, arg1, arg2, arg3) -> {
			p.closeInventory();
			ActionBarUtil.sendActionBar(p, Util.fixColor(Util.replaceString(SpecialSigns + ">> " + WarningColor + "Wpisz czas albo 'cancel'" + SpecialSigns + " <<")));
			AdminPanelCommand.getAdmin_changeTIMEE().add(p);

		});
		inv.setItem(5, timeg.build(), (p, arg1, arg2, arg3) -> {
			p.closeInventory();
			ActionBarUtil.sendActionBar(p, Util.fixColor(Util.replaceString(SpecialSigns + ">> " + WarningColor + "Wpisz czas albo 'cancel'" + SpecialSigns + " <<")));
			AdminPanelCommand.getAdmin_changeTIMEG().add(p);

		});
		inv.setItem(6, resetstone.build(), (p, inventory, i, itemStack) -> {
			for(User user : UserManager.getUsers().values()){
				user.setBreakStoneDay(0);
				user.update(true);
			}
			ItemMeta itemMeta = itemStack.getItemMeta();
			itemMeta.setLore(Collections.singletonList(Util.fixColor(Util.replaceString("&8->> &2Zresetowano!"))));
			itemStack.setItemMeta(itemMeta);
		});
		inv.setItem(7, teleportOptions.build(), (p, inventory, i, itemStack) -> {
			final InventoryGUI inv12 = new InventoryGUI(Core.getPlugin(), Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Panel " + ImportantColor + "zarzadzania funkcjami teleportacji" + SpecialSigns + " <<")), 1);
			final ItemBuilder back1 = new ItemBuilder(Material.WOOL,1,(short)14).setTitle(Util.replaceString(SpecialSigns + "* " + WarningColor_2 + "Powrot" + SpecialSigns + " *"));

			final ItemBuilder randomSpawnOnJoin = new ItemBuilder(Material.EMERALD_BLOCK).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "Funkcja " + ImportantColor + "Teleportacja na losowy spawn przy dolaczeniu")))
					.addLore(Util.fixColor(Util.replaceString(SpecialSigns + " ->> " + MainColor + "Status: " + (CoreConfig.TELEPORTMANAGER_RANDOMSPAWNONJOIN ? ChatColor.GREEN + "%V%" : ChatColor.DARK_RED + "%X%"))));
			final ItemBuilder randomWarpOnJoin = new ItemBuilder(Material.DIAMOND_BLOCK).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "Funkcja " + ImportantColor + "Teleportacja na losowy warp przy dolaczeniu")))
					.addLore(Util.fixColor(Util.replaceString(SpecialSigns + " ->> " + MainColor + "Status: " + (CoreConfig.TELEPORTMANAGER_RANDOMWARPONJOIN ? ChatColor.GREEN + "%V%" : ChatColor.DARK_RED + "%X%"))));
			inv12.setItem(0,randomSpawnOnJoin.build(),(player1, inventory1, i1, item) -> {
				player1.closeInventory();
				final SetConfigBooleanPacket packet = new SetConfigBooleanPacket("coreconfig.yml",!CoreConfig.TELEPORTMANAGER_RANDOMSPAWNONJOIN,"teleportmanager.randomspawnonjoin",true,player1.getDisplayName());
				final LoadConfigPacket packet1 = new LoadConfigPacket("coreconfig.yml");
				RedisClient.sendSectorsPacket(packet);
				RedisClient.sendSectorsPacket(packet1);
				final ItemMeta meta = item.getItemMeta();
				meta.setLore(Collections.singletonList(Util.fixColor(Util.replaceString(SpecialSigns + " ->> " + MainColor + "Status: " + (CoreConfig.TELEPORTMANAGER_RANDOMSPAWNONJOIN ? ChatColor.GREEN + "%V%" : ChatColor.DARK_RED + "%X%")))));
				item.setItemMeta(meta);
			});
			inv12.setItem(1,randomWarpOnJoin.build(),(player1, inventory1, i1, item) -> {
				player1.closeInventory();
				final SetConfigBooleanPacket packet = new SetConfigBooleanPacket("coreconfig.yml",!CoreConfig.TELEPORTMANAGER_RANDOMWARPONJOIN,"teleportmanager.randomwarponjoin",true,player1.getDisplayName());
				final LoadConfigPacket packet1 = new LoadConfigPacket("coreconfig.yml");
				RedisClient.sendSectorsPacket(packet);
				RedisClient.sendSectorsPacket(packet1);
				final ItemMeta meta = item.getItemMeta();
				meta.setLore(Collections.singletonList(Util.fixColor(Util.replaceString(SpecialSigns + " ->> " + MainColor + "Status: " + (CoreConfig.TELEPORTMANAGER_RANDOMWARPONJOIN ? ChatColor.GREEN + "%V%" : ChatColor.DARK_RED + "%X%")))));
				item.setItemMeta(meta);
			});

			inv12.setItem(inv12.get().getSize() - 1, back1.build(), (p18, arg118, arg218, arg31) -> {
				p18.closeInventory();
				openGlobal(p18);
			});
			p.closeInventory();
			inv12.openInventory(p);
		});
		inv.openInventory(player);
	}
	private static void openConfigsMenu(Player player)
	{
		final InventoryGUI inv = new InventoryGUI(Core.getPlugin(), Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + " Panel " + ImportantColor + "konfiguracji" + SpecialSigns + " <<")), 1);
		final ItemBuilder finalcoreConfigs = new ItemBuilder(Material.GOLD_BLOCK).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Konfiguracje " + ImportantColor + "JustPvP-FinalCore")));
		final ItemBuilder storageConfigs = new ItemBuilder(Material.CHEST).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Konfiguracje " + ImportantColor + "JustPvP-Storage")));
		final ItemBuilder guildConfigs = new ItemBuilder(Material.DRAGON_EGG).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Konfiguracje " + ImportantColor + "JustPvP-FinalGuilds")));
		final ItemBuilder enchantShopConfigs = new ItemBuilder(Material.ENCHANTMENT_TABLE).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Konfiguracje " + ImportantColor + "JustPvP-EnchantShop")));
		final ItemBuilder back = new ItemBuilder(Material.WOOL,1,(short)14).setTitle(Util.replaceString(SpecialSigns + "* " + WarningColor_2 + "Powrot" + SpecialSigns + " *"));
		inv.setItem(0, finalcoreConfigs.build(), (p, arg1, arg2, arg3) -> {
			p.closeInventory();
			openFinalCoreConfigsMenu(p);

		});
		inv.setItem(1, storageConfigs.build(), (p, arg1, arg2, arg3) -> {
			p.closeInventory();
			openStorageConfigsMenu(p);

		});
		inv.setItem(2, guildConfigs.build(), (p, arg1, arg2, arg3) -> {
			p.closeInventory();
			openGuildConfigsMenu(p);

		});
		inv.setItem(3, enchantShopConfigs.build(), (p, arg1, arg2, arg3) -> {
			p.closeInventory();
			openEnchantShopConfigsMenu(p);

		});
		inv.setItem(inv.get().getSize() - 1, back.build(), (p, arg1, arg2, arg3) -> {
			p.closeInventory();
			openMenu(p);
		});
		inv.openInventory(player);
	}
	private static void openStorageConfigsMenu(Player player) {
		final InventoryGUI inv = new InventoryGUI(Core.getPlugin(), Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + " Panel " + ImportantColor + "konfiguracji" + SpecialSigns + " <<")), 3);
		final ItemBuilder storageConfigs = new ItemBuilder(Material.CHEST).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Konfiguracje " + ImportantColor + "JustPvP-Storage" + SpecialSigns + " <<" )));
		final ItemBuilder config = new ItemBuilder(Material.EMERALD_BLOCK).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Konfiguracja: " + ImportantColor + UnderLined + "JustPvP-Storage/storageconfig.yml")))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Przeladowano: " + ChatColor.DARK_RED + "%X%")));
		final ItemBuilder cyan = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)9).setTitle("§2");
		final ItemBuilder back = new ItemBuilder(Material.WOOL,1,(short)14).setTitle(Util.replaceString(SpecialSigns + "* " + WarningColor_2 + "Powrot" + SpecialSigns + " *"));
		for(int i = 9; i <18;i++)
			inv.setItem(i, cyan.build(), null);
		inv.setItem(4, storageConfigs.build(), (p, arg1, arg2, arg3) -> {
			p.closeInventory();
			openStorageConfigsMenu(p);
		});
		inv.setItem(18, config.build(), (arg0, arg1, arg2, item) -> {
			ConfigCreator config1 = ConfigManager.getConfig("storageconfig.yml");
			config1.reloadConfig();
			new StorageConfig();
			final ItemMeta meta = item.getItemMeta();
			meta.setLore(Collections.singletonList(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Przeladowano: " + ChatColor.GREEN + "%V%" + MainColor + "," + ImportantColor + Util.getTime(System.currentTimeMillis())))));
			item.setItemMeta(meta);
		});
		inv.setItem(inv.get().getSize() - 1, back.build(), (p, arg1, arg2, arg3) -> {
			p.closeInventory();
			openConfigsMenu(p);
		});
		inv.openInventory(player);
	}
	private static void openEnchantShopConfigsMenu(Player player) {
		final InventoryGUI inv = new InventoryGUI(Core.getPlugin(), Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + " Panel " + ImportantColor + "konfiguracji" + SpecialSigns + " <<")), 3);
		final ItemBuilder enchantShopConfigs = new ItemBuilder(Material.ENCHANTMENT_TABLE).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Konfiguracje " + ImportantColor + "JustPvP-EnchantShop" + SpecialSigns + " <<" )));
		final ItemBuilder config = new ItemBuilder(Material.ENCHANTED_BOOK).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Konfiguracja: " + ImportantColor + UnderLined + "JustPvP-EnchantShop/config.yml")))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Przeladowano: " + ChatColor.DARK_RED + "%X%")));
		final ItemBuilder cyan = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)9).setTitle("§2");
		final ItemBuilder back = new ItemBuilder(Material.WOOL,1,(short)14).setTitle(Util.replaceString(SpecialSigns + "* " + WarningColor_2 + "Powrot" + SpecialSigns + " *"));
		for(int i = 9; i <18;i++)
			inv.setItem(i, cyan.build(), null);
		inv.setItem(4, enchantShopConfigs.build(), (p, arg1, arg2, arg3) -> {
			p.closeInventory();
			openEnchantShopConfigsMenu(p);
		});
		inv.setItem(18, config.build(), (arg0, arg1, arg2, item) -> {
			//TODO Main.getInstance().reloadConfig();
			//Main.getInstance().saveDefaultConfig();
			//new Settings();
			final ItemMeta meta = item.getItemMeta();
			meta.setLore(Collections.singletonList(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Przeladowano: " + ChatColor.GREEN + "%V%" + MainColor + "," + ImportantColor + Util.getTime(System.currentTimeMillis())))));
			item.setItemMeta(meta);
		});
		inv.setItem(inv.get().getSize() - 1, back.build(), (p, arg1, arg2, arg3) -> {
			p.closeInventory();
			openConfigsMenu(p);
		});
		inv.openInventory(player);
	}
	private static void openGuildConfigsMenu(Player player) {
		final InventoryGUI inv = new InventoryGUI(Core.getPlugin(), Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + " Panel " + ImportantColor + "konfiguracji" + SpecialSigns + " <<")), 3);
		final ItemBuilder guildConfigs = new ItemBuilder(Material.DRAGON_EGG).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Konfiguracje " + ImportantColor + "JustPvP-FinalGuilds" + SpecialSigns + " <<" )));
		final ItemBuilder config = new ItemBuilder(Material.ANVIL).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Konfiguracja: " + ImportantColor + UnderLined + "JustPvP-FinalGuilds/config.yml")))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Przeladowano: " + ChatColor.DARK_RED + "%X%")));
		final ItemBuilder effects = new ItemBuilder(Material.POTION,1,(short)8265).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Konfiguracja: " + ImportantColor + UnderLined + "JustPvP-FinalGuilds/guildeffects.yml")))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Przeladowano: " + ChatColor.DARK_RED + "%X%")));
		final ItemBuilder cyan = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)9).setTitle("§2");
		final ItemBuilder back = new ItemBuilder(Material.WOOL,1,(short)14).setTitle(Util.replaceString(SpecialSigns + "* " + WarningColor_2 + "Powrot" + SpecialSigns + " *"));
		for(int i = 9; i <18;i++)
			inv.setItem(i, cyan.build(), null);
		inv.setItem(4, guildConfigs.build(), (p, arg1, arg2, arg3) -> {
			p.closeInventory();
			openGuildConfigsMenu(p);
		});
		inv.setItem(18, config.build(), (arg0, arg1, arg2, item) -> {
			ConfigCreator config1 = ConfigManager.getConfig("guildconfig.yml");
			config1.reloadConfig();
			new GuildConfig();
			final ItemMeta meta = item.getItemMeta();
			meta.setLore(ImmutableList.of(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Przeladowano: " + ChatColor.GREEN + "%V%" + MainColor + "," + ImportantColor + Util.getTime(System.currentTimeMillis())))));
			item.setItemMeta(meta);
		});
		inv.setItem(19, effects.build(), (arg0, arg1, arg2, item) -> {
			ConfigCreator config1 = ConfigManager.getConfig("guildeffects.yml");
			config1.reloadConfig();
			new EffectsConfig();
			final ItemMeta meta = item.getItemMeta();
			meta.setLore(Collections.singletonList(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Przeladowano: " + ChatColor.GREEN + "%V%" + MainColor + "," + ImportantColor + Util.getTime(System.currentTimeMillis())))));
			item.setItemMeta(meta);
		});
		inv.setItem(inv.get().getSize() - 1, back.build(), (p, arg1, arg2, arg3) -> {
			p.closeInventory();
			openConfigsMenu(p);
		});
		inv.openInventory(player);
	}
	private static void openFinalCoreConfigsMenu(Player player)
	{
		final InventoryGUI inv = new InventoryGUI(Core.getPlugin(), Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + " Panel " + ImportantColor + "konfiguracji" + SpecialSigns + " <<")), 5);
		final ItemBuilder finalcoreConfigs = new ItemBuilder(Material.GOLD_BLOCK).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Konfiguracje " + ImportantColor + "JustPvP-FinalCore" + SpecialSigns + " <<" )));
		final ItemBuilder config = new ItemBuilder(Material.EMERALD_BLOCK).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Konfiguracja: " + ImportantColor + UnderLined + "JustPvP-FinalCore/coreconfig.yml")))
					.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Przeladowano: " + ChatColor.DARK_RED + "%X%")));
		final ItemBuilder message = new ItemBuilder(Material.BOOK).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Konfiguracja: " + ImportantColor + UnderLined + "JustPvP-FinalCore/message.yml")))
					.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Przeladowano: " + ChatColor.DARK_RED + "%X%")));
		final ItemBuilder shops = new ItemBuilder(Material.CHEST).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Konfiguracja: " + ImportantColor + UnderLined + "JustPvP-FinalCore/shops.yml")))
					.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Przeladowano: " + ChatColor.DARK_RED + "%X%")));
		final ItemBuilder osiagniecia = new ItemBuilder(Material.GOLD_BLOCK).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Konfiguracja: " + ImportantColor + UnderLined + "JustPvP-FinalCore/osiagniecia.yml")))
					.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Przeladowano: " + ChatColor.DARK_RED + "%X%")));
		final ItemBuilder effect = new ItemBuilder(Material.POTION).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Konfiguracja: " + ImportantColor + UnderLined + "JustPvP-FinalCore/effect.yml")))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Przeladowano: " + ChatColor.DARK_RED + "%X%")));
		final ItemBuilder drops = new ItemBuilder(Material.DIAMOND_PICKAXE).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Konfiguracja: " + ImportantColor + UnderLined + "JustPvP-FinalCore/drops.yml")))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Przeladowano: " + ChatColor.DARK_RED + "%X%")));
		final ItemBuilder dropconfig = new ItemBuilder(Material.STONE).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Konfiguracja: " + ImportantColor + UnderLined + "JustPvP-FinalCore/dropconfig.yml")))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Przeladowano: " + ChatColor.DARK_RED + "%X%")));
		final ItemBuilder kits = new ItemBuilder(Material.ENDER_CHEST).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Konfiguracja: " + ImportantColor + UnderLined + "JustPvP-FinalCore/kits.yml")))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Przeladowano: " + ChatColor.DARK_RED + "%X%")));
		final ItemBuilder tablist = new ItemBuilder(Material.WATCH).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Konfiguracja: " + ImportantColor + UnderLined + "JustPvP-FinalCore/tablist.yml")))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Przeladowano: " + ChatColor.DARK_RED + "%X%")));
		final ItemBuilder cyan = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)9).setTitle("§2");
		final ItemBuilder back = new ItemBuilder(Material.WOOL,1,(short)14).setTitle(Util.replaceString(SpecialSigns + "* " + WarningColor_2 + "Powrot" + SpecialSigns + " *"));
		for(int i = 9; i <18;i++)
			inv.setItem(i, cyan.build(), null);
		inv.setItem(4, finalcoreConfigs.build(), (p, arg1, arg2, arg3) -> {
			p.closeInventory();
			openFinalCoreConfigsMenu(p);
		});
		inv.setItem(18, config.build(), (arg0, arg1, arg2, item) -> {
			ConfigCreator config1 = ConfigManager.getConfig("config.yml");
			config1.reloadConfig();
			new CoreConfig();
			final ItemMeta meta = item.getItemMeta();
			meta.setLore(Collections.singletonList(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Przeladowano: " + ChatColor.GREEN + "%V%" + MainColor + "," + ImportantColor + Util.getTime(System.currentTimeMillis())))));
			item.setItemMeta(meta);
		});
		inv.setItem(26, tablist.build(), (arg0, arg1, arg2, item) -> {
			ConfigCreator config1 = ConfigManager.getConfig("tablist.yml");
			config1.reloadConfig();
			new TabListConfig();
			final ItemMeta meta = item.getItemMeta();
			meta.setLore(Collections.singletonList(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Przeladowano: " + ChatColor.GREEN + "%V%" + MainColor + "," + ImportantColor + Util.getTime(System.currentTimeMillis())))));
			item.setItemMeta(meta);
		});
		inv.setItem(19, message.build(), (arg0, arg1, arg2, item) -> {
			ConfigCreator config12 = ConfigManager.getConfig("message.yml");
			config12.reloadConfig();
			new MessageConfig();
			final ItemMeta meta = item.getItemMeta();
			meta.setLore(ImmutableList.of(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Przeladowano: " + ChatColor.GREEN + "%V%" + MainColor + "," + ImportantColor + Util.getTime(System.currentTimeMillis())))));
			item.setItemMeta(meta);
		});
		inv.setItem(20, shops.build(), (arg0, arg1, arg2, item) -> {
			ConfigCreator config13 = ConfigManager.getConfig("shops.yml");
			config13.reloadConfig();
			new ShopConfig();
			final ItemMeta meta = item.getItemMeta();
			meta.setLore(Collections.singletonList(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Przeladowano: " + ChatColor.GREEN + "%V%" + MainColor + "," + ImportantColor + Util.getTime(System.currentTimeMillis())))));
			item.setItemMeta(meta);
		});
		inv.setItem(21, osiagniecia.build(), (arg0, arg1, arg2, item) -> {
			ConfigCreator config14 = ConfigManager.getConfig("osiagniecia.yml");
			config14.reloadConfig();
			new OsiagnieciaConfig();
			final ItemMeta meta = item.getItemMeta();
			meta.setLore(Collections.singletonList(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Przeladowano: " + ChatColor.GREEN + "%V%" + MainColor + "," + ImportantColor + Util.getTime(System.currentTimeMillis())))));
			item.setItemMeta(meta);
		});
		inv.setItem(22, effect.build(), (arg0, arg1, arg2, item) -> {
			ConfigCreator config14 = ConfigManager.getConfig("effect.yml");
			config14.reloadConfig();
			new EffectConfig();
			new EffectShopInventory();
			final ItemMeta meta = item.getItemMeta();
			meta.setLore(Collections.singletonList(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Przeladowano: " + ChatColor.GREEN + "%V%" + MainColor + "," + ImportantColor + Util.getTime(System.currentTimeMillis())))));
			item.setItemMeta(meta);
		});
		inv.setItem(23, drops.build(), (arg0, arg1, arg2, item) -> {
			DropFile.reloadConfig();
			DropManager.setup();
			final ItemMeta meta = item.getItemMeta();
			meta.setLore(Collections.singletonList(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Przeladowano: " + ChatColor.GREEN + "%V%" + MainColor + "," + ImportantColor + Util.getTime(System.currentTimeMillis())))));
			item.setItemMeta(meta);
		});
		inv.setItem(24, dropconfig.build(), (arg0, arg1, arg2, item) -> {
			Config.reloadConfig();
			DropManager.setup();
			final ItemMeta meta = item.getItemMeta();
			meta.setLore(Collections.singletonList(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Przeladowano: " + ChatColor.GREEN + "%V%" + MainColor + "," + ImportantColor + Util.getTime(System.currentTimeMillis())))));
			item.setItemMeta(meta);
		});
		inv.setItem(inv.get().getSize() - 1, back.build(), (p, arg1, arg2, arg3) -> {
			p.closeInventory();
			openConfigsMenu(p);
		});
		inv.setItem(25, kits.build(), (arg0, arg1, arg2, item) -> {
			ConfigCreator config14 = ConfigManager.getConfig("kits.yml");
			config14.reloadConfig();
			new KitsConfig();
			final ItemMeta meta = item.getItemMeta();
			meta.setLore(Collections.singletonList(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Przeladowano: " + ChatColor.GREEN + "%V%" + MainColor + "," + ImportantColor + Util.getTime(System.currentTimeMillis())))));
			item.setItemMeta(meta);
		});
		inv.openInventory(player);
	}
	private static void openServerManagmentMenu(Player player) {
		final InventoryGUI inv = new InventoryGUI(Core.getPlugin(), Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + " Panel zarzadzania" + ImportantColor + " serwerem" + SpecialSigns + " <<")), 1);
		final ItemBuilder save = new ItemBuilder(Material.SIGN).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Zapisz dane na serwerze")))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + " * " + MainColor + "Zapisuje: " + ImportantColor + "Wszystkich uzytkownikow online i offline")))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + " * " + MainColor + "Zapisuje: " + ImportantColor + "Wszystkich graczy online")))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + " * " + MainColor + "Zapisuje: " + ImportantColor + "Wszystkie regiony")))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + " * " + MainColor + "Zapisuje: " + ImportantColor + "Wszystkie gildie i ich czlonkow")));
		final ItemBuilder globalKills = new ItemBuilder(Material.DIAMOND_SWORD).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "ReSuma Globalnych Zabojstw")))
				.addLore(Util.fixColor(Util.replaceString( SpecialSigns + " * " + MainColor + "Aktualnie zabojstw: " + ImportantColor + ServerStatsManager.getGlobalKills())));
		final ItemBuilder fanpage = new ItemBuilder(Material.LAPIS_BLOCK).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + ImportantColor + "Wlacz/Wylacz Fanpage Command")))
				.addLore(Util.fixColor(Util.replaceString(MainColor + "Status: " + ImportantColor + (FanPageCommand.isFanpage() ? "%V%" : "%X%"))));
		final ItemBuilder youtube = new ItemBuilder(Material.GOLD_BLOCK).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + ImportantColor + "Wlacz/Wylacz YouTube Command")))
				.addLore(Util.fixColor(Util.replaceString(MainColor + "Status: " + ImportantColor + (YouTubeCommand.isYoutube() ? "%V%" : "%X%"))));
		final ItemBuilder back = new ItemBuilder(Material.WOOL,1,(short)14).setTitle(Util.replaceString(SpecialSigns + "* " + WarningColor_2 + "Powrot" + SpecialSigns + " *"));
		inv.setItem(0, save.build(), (p, arg1, arg2, item) -> {
			for(User u : UserManager.getUsers().values())
				u.update(true);
			for(Player online : Bukkit.getOnlinePlayers())
				online.saveData();
			Bukkit.savePlayers();
			try {
				Objects.requireNonNull(Core.getWorldGuard()).getRegionManager(Bukkit.getWorld("world")).save();
			} catch (StorageException e) {
				e.printStackTrace();
			}
			for(Guild g : GuildManager.getGuilds().values())
				g.update(true);
			ItemMeta meta = item.getItemMeta();
			List<String> lore = Collections.singletonList(Util.fixColor(Util.replaceString(SpecialSigns + " * " + WarningColor + BOLD + "Zapis zostal wykonany!")));
			meta.setLore(lore);
			item.setItemMeta(meta);
		});
		inv.setItem(1, globalKills.build(), (player1, inventory, i, itemStack) -> {
			int kills = 0;
			for(User user : UserManager.getUsers().values()){
				kills += user.getKills();
			}
			ServerStatsManager.globalKills = kills;
			player.closeInventory();
			openServerManagmentMenu(player);
		});
		inv.setItem(inv.get().getSize() - 1, back.build(), (p, arg1, arg2, arg3) -> {
			p.closeInventory();
			openMenu(p);
		});
		inv.setItem(2, youtube.build(), (player1, inventory, i, itemStack) -> {
			YouTubeCommand.setYoutube(!YouTubeCommand.isYoutube());
			ItemMeta meta = itemStack.getItemMeta();
			meta.setLore(Collections.singletonList(Util.fixColor(Util.replaceString(MainColor + "Status: " + ImportantColor + (YouTubeCommand.isYoutube() ? "%V%" : "%X%")))));
			itemStack.setItemMeta(meta);
		});
		inv.setItem(3, fanpage.build(), (player1, inventory, i, itemStack) -> {
			FanPageCommand.setFanpage(!FanPageCommand.isFanpage());
			ItemMeta meta = itemStack.getItemMeta();
			meta.setLore(Collections.singletonList(Util.fixColor(Util.replaceString(MainColor + "Status: " + ImportantColor + (FanPageCommand.isFanpage() ? "%V%" : "%X%")))));
			itemStack.setItemMeta(meta);
		});
		player.openInventory(inv.get());
	}
	//C#, C++ Java PHP NODE.JS JavaScipt 

}
