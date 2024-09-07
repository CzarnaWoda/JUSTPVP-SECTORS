package pl.blackwater.core.inventories;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import pl.blackwater.core.Core;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.settings.CoreConfig;
import pl.blackwater.core.utils.LocationUtil;
import pl.blackwaterapi.configs.ConfigCreator;
import pl.blackwaterapi.configs.ConfigManager;
import pl.blackwaterapi.gui.actions.InventoryGUI;
import pl.blackwaterapi.utils.ItemBuilder;
import pl.blackwaterapi.utils.TitleUtil;
import pl.blackwaterapi.utils.Util;

public class SetSpawnInventory implements Colors{
	@SuppressWarnings("deprecation")
	public static InventoryView openMenu(Player player)
	{
		final InventoryGUI inv = new InventoryGUI(Core.getPlugin(), Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Ustawianie spawn'u")), 1);
		final ItemBuilder set = new ItemBuilder(Material.STAINED_CLAY,1,(short)13).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Ustaw nowy " + ImportantColor + "spawn")));
		final ItemBuilder remove = new ItemBuilder(Material.STAINED_CLAY,1,(short)14).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Usun" + ImportantColor + " spawn")));
		inv.setItem(0, set.build(), (p, arg1, arg2, arg3) -> {
			final Location loc = p.getLocation();
			final ConfigCreator config = ConfigManager.getConfig("coreconfig.yml");
			config.addToListField("spawnmanager.spawnlist", LocationUtil.convertLocationToString(loc));
			config.reloadConfig();
			new CoreConfig();
			p.closeInventory();
			TitleUtil.sendFullTitle(p, 15, 8, 15, Util.fixColor(Util.replaceString(SpecialSigns + ">> " + ImportantColor + "SpawnManager" + SpecialSigns + " <<")), Util.fixColor(Util.replaceString(MainColor + "Ustawiono nowy " + ImportantColor + "spawn")));
		});
		inv.setItem(8, remove.build(), (p, arg1, arg2, arg3) -> {
			final InventoryGUI inv1 = new InventoryGUI(Core.getPlugin(), Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Ustawianie spawn'u")), 6);
			int position = 1;
			for(final String s : CoreConfig.SPAWNMANAGER_SPAWNLIST) {
				Location loc = LocationUtil.convertStringToLocation(s);
				ItemBuilder spawn = new ItemBuilder(Material.STAINED_CLAY,1,(short)11).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Spawn " + SpecialSigns + " >> " + ImportantColor + position)))
						.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "    >> " + ImportantColor +  loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ())));
				inv1.setItem(position - 1, spawn.build(), (p1, arg11, arg21, arg31) -> {
					final ConfigCreator config = ConfigManager.getConfig("coreconfig.yml");
					config.removeToListField("spawnmanager.spawnlist", s);
					config.reloadConfig();
					new CoreConfig();
					p1.closeInventory();
					TitleUtil.sendFullTitle(p1, 15, 8, 15, Util.fixColor(Util.replaceString(SpecialSigns + ">> " + ImportantColor + "SpawnManager" + SpecialSigns + " <<")), Util.fixColor(Util.replaceString(MainColor + "Usunieto " + ImportantColor + "spawn")));
				});
				position++;
			}
			inv1.openInventory(p);
		});
		return player.openInventory(inv.get());
	}

}
