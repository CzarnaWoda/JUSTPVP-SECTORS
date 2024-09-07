package pl.blackwater.core.managers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import pl.blackwater.core.Core;
import pl.blackwater.core.data.Mine;
import pl.blackwater.core.data.User;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.guilds.data.Guild;
import pl.blackwaterapi.gui.actions.InventoryGUI;
import pl.blackwaterapi.timer.TimerUtil;
import pl.blackwaterapi.utils.*;
import pl.justsectors.packets.impl.mines.MineBuyPacket;
import pl.justsectors.packets.impl.mines.MineRegisterPacket;
import pl.justsectors.redis.channels.RedisChannel;
import pl.justsectors.redis.client.RedisClient;
import pl.justsectors.sectors.SectorManager;

public class MineManager implements Colors{
	
	public static ConcurrentHashMap<Integer, Mine> mines = new ConcurrentHashMap<>();
	public static RegionManager regionManager;
	static{
		regionManager = Objects.requireNonNull(Core.getWorldGuard()).getRegionManager(Bukkit.getWorld("world"));
	}

	@SuppressWarnings("deprecation")
	public static void OpenMineMenu(Player p){
		final InventoryGUI inv = new InventoryGUI(Core.getPlugin(), Util.fixColor(Util.replaceString(SpecialSigns + ">> " + ImportantColor + "Mine-Manager " + SpecialSigns + "<<")), 3);
		int position = 9;
		final User u = UserManager.getUser(p);
		for(int i = 1;i<(mines.size()+1);i++){
			final Mine m = getMine(i);
			if(m == null){
				p.openInventory(inv.get());
				return;
			}
			if(m.getType().equalsIgnoreCase("NORMAL")){
				final boolean hasMine = u.isBoughtMine(m);
				final ItemBuilder b = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short) (hasMine ? 5 : 14)).setTitle(Util.fixColor(Util.replaceString(hasMine ? "&2&l%V%" : "&c&l%X%")));
			final ItemBuilder a = new ItemBuilder(Material.DIAMOND_PICKAXE);
			a.setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + ImportantColor + m.getName())));
			a.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "ID Kopalni: " + ImportantColor + m.getIndex())));
			a.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Koszt (kasa): " + ImportantColor + m.getCost())));
			a.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Koszt (level): " + ImportantColor + m.getLevel())));
			a.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Region glowny: " + ImportantColor + m.getMainRegion())));
			a.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Region kopalniany: " + ImportantColor + m.getStoneRegion())));
			a.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Limit kamienia: " + ImportantColor + m.getMineStoneLimit())));
			a.addLore(Util.fixColor(Util.replaceString(UserManager.getUser(p).isBoughtMine(m) ? SpecialSigns + ">> " + MainColor + "Kliknij " + ImportantColor + BOLD +  "LPM " + MainColor + "aby przejsc dalej" : SpecialSigns + ">> " + MainColor + "Kliknij " + ImportantColor + BOLD + "LPM " + MainColor + "aby kupic")));
			inv.setItem(position - 9, b.build(),null);
			inv.setItem(position, a.build(), (p1, inventory, arg2, arg3) -> {
				p1.closeInventory();
				if(u.isBoughtMine(m))
					OpenMineMenuWhenPlayerBought(p1, m);
				else {
					final InventoryGUI inv1 = new InventoryGUI(Core.getPlugin(), Util.fixColor(Util.replaceString(SpecialSigns + ">> " + ImportantColor + "Mine-Manager " + SpecialSigns + "<<")), 1);
					final ItemBuilder accept = new ItemBuilder(Material.STAINED_CLAY,1,(short)13).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Kliknij aby " + ChatColor.GREEN + "POTWIERDZIC " + MainColor + "zakup kopalni")));
					final ItemBuilder decline = new ItemBuilder(Material.STAINED_CLAY,1,(short)14).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Kliknij aby " + ChatColor.DARK_RED + "ANULOWAC " + MainColor + "zakup kopalni")));
					inv1.setItem(0, accept.build(), (p11, arg1, arg21, arg31) -> {
						if(u.getMoney() < m.getCost() || u.getLevel() < m.getLevel()) {
							p11.closeInventory();
							TitleUtil.sendFullTitle(p11, 20, 100, 30, Util.fixColor(Util.replaceString(SpecialSigns + ">> " + ImportantColor + "Mine-Manager " + SpecialSigns + "<<")), Util.fixColor(MainColor + "Nie posiadasz wystarczajaco " + ImportantColor + "pieniedzy/levela"));
							return;
						}
						RedisClient.sendSectorsPacket(new MineBuyPacket(m.getIndex(), p11.getUniqueId()));
						m.addPlayer(u);
						m.insert();
					});
					inv1.setItem(8, decline.build(), (p112, arg1, arg212, arg312) -> {
						TitleUtil.sendFullTitle(p112, 20, 100, 30, Util.fixColor(Util.replaceString(SpecialSigns + ">> " + ImportantColor + "Mine-Manager " + SpecialSigns + "<<")), Util.fixColor(MainColor + "Anulowano zakup kopalni: " + ImportantColor + UnderLined + m.getName()));
						p112.closeInventory();
					});
					inv1.openInventory(p1);
				}

			});
			inv.setItem(position + 9, b.build(),null);
			position += 1;
			}
		}
		position++;
		final ItemBuilder c = new ItemBuilder(Material.BOOK).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + ImportantColor + "Informacje")))
					.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Aktualnie wykopales: " + ImportantColor + u.getBreakStoneDay())))
					.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Aktualny limit: " + ImportantColor + u.getLimitStone())))
					.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  >> " + MainColor + "Limit kopania powieksza sie wraz z zabijaniem graczy")))
					.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "Dla gracza jest to: " + ImportantColor + "liczba zabojstw * 3 + limit kopalnii")))
					.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "Dla gracza vip, svip jest to: " + ImportantColor + "liczba zabojstw * 5 + limit kopalnii")));
		final ItemBuilder air = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)7).setTitle("§6");
		inv.setItem(17-9,air.build(),null);
		inv.setItem(17, c.build(),null);
		inv.setItem(17+9,air.build(),null);
		p.openInventory(inv.get());
	}
	public static void OpenMineMenuWhenPlayerBought(Player p, final Mine m){
		final InventoryGUI inv = new InventoryGUI(Core.getPlugin(), Util.fixColor(Util.replaceString(SpecialSigns + ">> " + ImportantColor + "Mine-Manager " + SpecialSigns + "<<")), 1);
		final ItemBuilder b = new ItemBuilder(Material.ENDER_PORTAL_FRAME).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Teleportacja do koplanii")))
					.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Kliknij aby przeteleportowac sie !")));
		final ItemBuilder a = new ItemBuilder(Material.DIAMOND_PICKAXE);
		a.setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + ImportantColor + m.getName())));
		a.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "ID Kopalni: " + ImportantColor + m.getIndex())));
		a.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Region glowny: " + ImportantColor + m.getMainRegion())));
		a.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Region kopalniany: " + ImportantColor + m.getStoneRegion())));
		inv.setItem(4, b.build(), (p1, arg1, arg2, arg3) -> {
			p1.closeInventory();
			//TimerUtil.teleport(p1, m.getLocation(), 10);
			final User u = UserManager.getUser(p.getUniqueId());
			TeleportManager.teleportPlayer(u, m.getLocation(), SectorManager.getSector("kopalnia"));
		});
		inv.setItem(8, a.build(),null);
		p.openInventory(inv.get());
	}
	public static Mine getMine(int index){
		for(Mine m : mines.values()){
			if(m.getIndex() == index){
				return m;
			}
		}
		return null;
	}
	public static Mine getMineByGuild(Guild g){
		for(Mine m : mines.values()){
			if (m.getGuild().equalsIgnoreCase(g.getTag())){
				return m;
			}
		}
		return null;
	}

	public static void registerMine(final Mine m)
	{
		mines.put(m.getIndex(), m);
	}

	public static void CreateMine(String name,String type,int cost,Location loc,int level,String mainregion,String stoneregion,int stonelimit){
		final ProtectedRegion mainRegion = regionManager.getRegion(mainregion);
		assert mainRegion != null;
		mainRegion.setFlag(DefaultFlag.ENTITY_PAINTING_DESTROY, StateFlag.State.DENY);
        mainRegion.setFlag(DefaultFlag.ENTITY_ITEM_FRAME_DESTROY, StateFlag.State.DENY);
        mainRegion.setFlag(DefaultFlag.PVP, StateFlag.State.DENY);
        mainRegion.setFlag(DefaultFlag.ENDERDRAGON_BLOCK_DAMAGE, StateFlag.State.DENY);
        mainRegion.setFlag(DefaultFlag.ENDERPEARL, StateFlag.State.DENY);
        mainRegion.setFlag(DefaultFlag.CREEPER_EXPLOSION, StateFlag.State.DENY);
        mainRegion.setFlag(DefaultFlag.BUILD, StateFlag.State.DENY);
        mainRegion.setFlag(DefaultFlag.BLOCK_BREAK, StateFlag.State.DENY);
        mainRegion.setFlag(DefaultFlag.CHEST_ACCESS, StateFlag.State.ALLOW);
        mainRegion.setFlag(DefaultFlag.FIRE_SPREAD, StateFlag.State.DENY);
        mainRegion.setFlag(DefaultFlag.LAVA_FIRE, StateFlag.State.DENY);
        mainRegion.setFlag(DefaultFlag.GHAST_FIREBALL, StateFlag.State.DENY);
        mainRegion.setFlag(DefaultFlag.INVINCIBILITY, StateFlag.State.ALLOW);
        mainRegion.setFlag(DefaultFlag.ICE_MELT, StateFlag.State.DENY);
        mainRegion.setFlag(DefaultFlag.LIGHTER, StateFlag.State.DENY);
        mainRegion.setFlag(DefaultFlag.LIGHTNING, StateFlag.State.DENY);
        mainRegion.setFlag(DefaultFlag.MOB_DAMAGE, StateFlag.State.DENY);
        mainRegion.setFlag(DefaultFlag.MOB_SPAWNING, StateFlag.State.DENY);
        mainRegion.setFlag(DefaultFlag.SNOW_FALL, StateFlag.State.DENY);
        mainRegion.setFlag(DefaultFlag.PLACE_VEHICLE, StateFlag.State.DENY);
        mainRegion.setFlag(DefaultFlag.POTION_SPLASH, StateFlag.State.DENY);
        mainRegion.setFlag(DefaultFlag.BLOCK_PLACE, StateFlag.State.DENY);
        mainRegion.setFlag(DefaultFlag.ENTRY, StateFlag.State.DENY);
        mainRegion.setFlag(DefaultFlag.ENTRY.getRegionGroupFlag(),RegionGroup.NON_MEMBERS);
        mainRegion.setFlag(DefaultFlag.USE, StateFlag.State.ALLOW);
        mainRegion.setFlag(DefaultFlag.INTERACT, StateFlag.State.ALLOW);
        mainRegion.setFlag(DefaultFlag.TNT, StateFlag.State.DENY);
        mainRegion.setPriority(100);
        final ProtectedRegion stoneRegion = regionManager.getRegion(stoneregion);
		assert stoneRegion != null;
		stoneRegion.setFlag(DefaultFlag.BLOCK_BREAK, StateFlag.State.ALLOW);
        stoneRegion.setFlag(DefaultFlag.BLOCK_BREAK.getRegionGroupFlag(),RegionGroup.MEMBERS);
        stoneRegion.setFlag(DefaultFlag.BLOCK_PLACE, StateFlag.State.DENY);
        stoneRegion.setPriority(200);
		int index = mines.size() + 1;
		final Mine m = new Mine(index, name,type, cost, loc, level,mainregion,stoneregion,stonelimit);
		mines.put(index, m);
		m.insert();
		RedisClient.sendSectorsPacket(new MineRegisterPacket(m));
	}

	public static void deleteMine(int index)
	{
		Mine m = getMine(index);
		assert m != null;
		m.delete();
		mines.remove(index);
	}

	public static void setup(){
		RedisChannel.INSTANCE.MINES.forEach(((integer, s) -> {
			Mine mine = GsonUtil.fromJson(s, Mine.class);
			mines.put(integer, mine);
			Logger.info("Loaded " + mines.size() + " mines");
		}));
	}

	public static ConcurrentHashMap<Integer, Mine> getMines(){
		return mines;
	}

}
