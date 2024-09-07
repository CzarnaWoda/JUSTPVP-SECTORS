package pl.blackwater.chestpvpdrop.listeners;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.scheduler.BukkitRunnable;

import pl.blackwater.core.Core;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwaterapi.utils.Util;

public class GeneratorListener implements Listener, Colors{
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onBlockPlace(BlockPlaceEvent e) {
		final Block b = e.getBlock();
		final Block under = b.getLocation().subtract(0.0D, 1.0D, 0.0D).getBlock();
		final Player p = e.getPlayer();
		if(b.getType().equals(Material.ENDER_STONE)){
			if(under != null)
				Util.sendMsg(p, SpecialSigns + "* " + MainColor + "Utworzono generator, teraz wystarczy polozyc nad nim " + ImportantColor + "STONE" + MainColor + " lub " + ImportantColor + "OBSIDIAN");
			else
				Util.sendMsg(p, WarningColor + "Blad: " + WarningColor_2 + "Nad generatorem nie moze byc bloku!");
		}
		assert under != null;
		if(under.getType().equals(Material.ENDER_STONE) && !b.getType().equals(Material.STONE) && !b.getType().equals(Material.OBSIDIAN)) {
			Util.sendMsg(p, WarningColor + "Blad: " + WarningColor_2 + "Do stworzenia generatora mozna uzyc tylko STONE lub OBSIDIAN");
			e.setCancelled(true);
		}
		if(under.getType().equals(Material.ENDER_STONE)) {
			if(b.getType().equals(Material.STONE) || b.getType().equals(Material.OBSIDIAN)) {
				Util.sendMsg(p, SpecialSigns + "* " + MainColor + "Od teraz generator bedzie generowal " + ImportantColor + b.getType());
			}
		}
	}
	@EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
	public void onBlockBreak(BlockBreakEvent e) {
		final Block b = e.getBlock();
		final Block under = b.getLocation().subtract(0.0D, 1.0D, 0.0D).getBlock();
		if(!under.getType().equals(Material.ENDER_STONE))
			return;
		if(b.getType().equals(Material.STONE) || b.getType().equals(Material.OBSIDIAN)) {
			final long delay = b.getType().equals(Material.STONE) ? 25L : 300L;
			final Material type = b.getType();
			new BukkitRunnable() {
				
				@Override
				public void run() {
					if(under.getType().equals(Material.ENDER_STONE)) {
						b.setType(type);
					}
					
				}
			}.runTaskLater(Core.getPlugin(), delay);
		}
	}
	@EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
	public void onGeneratorBreak(BlockBreakEvent e) {
		final Block b = e.getBlock();
		final Player p = e.getPlayer();
		if(b.getType().equals(Material.ENDER_STONE) && !p.getGameMode().equals(GameMode.SURVIVAL)) {
			Util.sendMsg(p, SpecialSigns + "* " + MainColor + "Zniczyles " + ImportantColor + "generator");
			b.setType(Material.AIR);
			return;
		}
		if(b.getType().equals(Material.ENDER_STONE)) {
			Util.sendMsg(p, SpecialSigns + "* " + MainColor + "Zniczyles " + ImportantColor + "generator");
			b.setType(Material.AIR);
		}
	}
}
