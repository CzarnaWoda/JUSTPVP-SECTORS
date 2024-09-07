package pl.blackwater.core.data;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import lombok.Data;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import pl.blackwater.core.Core;
import pl.blackwater.core.managers.MineManager;
import pl.blackwater.core.settings.CoreConfig;
import pl.blackwater.core.settings.RankConfig;
import pl.blackwater.core.utils.ListUtil;
import pl.blackwater.core.utils.LocationUtil;
import pl.blackwater.core.utils.MapUtil;
import pl.blackwater.guilds.data.Guild;
import pl.blackwater.guilds.managers.GuildManager;
import pl.blackwaterapi.store.Entry;
import pl.blackwaterapi.utils.GsonUtil;
import pl.blackwaterapi.utils.MathUtil;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.packets.impl.user.AddStatisticPacket;
import pl.justsectors.packets.impl.user.RemoveStatisticPacket;
import pl.justsectors.packets.impl.user.SetStatisticPacket;
import pl.justsectors.redis.channels.RedisChannel;
import pl.justsectors.redis.client.RedisClient;
import pl.justsectors.sectors.Sector;
import pl.justsectors.sectors.SectorManager;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;

import static pl.blackwater.core.managers.MineManager.regionManager;

@Data
public class User implements Entry, Serializable {

	private UUID uuid;
	private String nbtString;
	private String lastName,firstIP,lastIP,chatControl,rank,sectorName;
	private GameMode gameMode;
	private boolean fly,god,turboDrop,turboExp;
	private int kills;
	private int deaths;
	private int logouts;
	private int killStreak;
	private int timePlay;
	private int money;
	private int eatKox;
	private int eatRef;
	private int throwPearl;
	private int openPremiumChest;
	private int safeKox;
	private int safeRef;
	private int safePearl;
	private int safeSnowBall;
	private int safeArrow;
	private int stoneLimit;
	private int placeChest;
	private int boostMoney;
	private int boostLvL;
	private int boostKox;
	private int boostRef;
	private int boostChest;
	private int boostKill;
	private int eventTurboMoney;
	private int level;
	private double bonusDrop;
	private long lastJoin,kitVIPTime,kitSVIPTime,kitJEDZENIETime,eventTurboMoneyTime,protection,turboDropTime,turboExpTime;
	private double exp;
	private Location lastLocation,homeLocation;
	private List<String> takenAchievement;
	private List<Integer> drops;
	private int breakStoneDay;
	private HashMap<UUID,Long> chesttimes;
	private Long lastUpdate = System.currentTimeMillis();
	private List<String> sellConfig;

	public User() {
	}

	public User(final String userName, final UUID uuid, final String address, final GameMode gameMode, final boolean allowFlight, final Location lastLocation, final Location homeLocation)
	{
		super();
		this.uuid = uuid;
		lastName = userName;
		firstIP = address;
		lastIP = address;
		chatControl = "";
		this.gameMode = gameMode;
		fly = allowFlight;
		god = (gameMode == GameMode.CREATIVE);
		kills = 0;
		deaths = 0;
		logouts = 0;
		killStreak = 0;
		timePlay = 0;
		money = 0;
		eatKox = 0;
		eatRef = 0;
		throwPearl = 0;
		openPremiumChest = 0;
		safeKox = 0;
		safeRef = 0;
		safePearl = 0;
		stoneLimit = 0;
		placeChest = 0;
		boostMoney = 0;
		boostLvL = 0;
		boostKox = 0;
		boostRef = 0;
		boostChest = 0;
		boostKill = 0;
		eventTurboMoney = 1;
		eventTurboMoneyTime = 0L;
		level = 0;
		lastJoin = System.currentTimeMillis();
		kitVIPTime = 0L;
		kitSVIPTime = 0L;
		kitJEDZENIETime = 0L;
		exp = 0;
		this.lastLocation = lastLocation;
		this.homeLocation = homeLocation;
		protection = System.currentTimeMillis() + CoreConfig.PROTECTIONMANAGER_PROTECTIONTIME * 60 * 1000;
		takenAchievement = new ArrayList<>();
		rank = RankConfig.DEFAULT_RANK;
		drops = Arrays.asList(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0);
		turboDropTime = 0L;
		turboExpTime = 0L;
		turboDrop = false;
		turboExp = false;
		bonusDrop = 0.0;
		breakStoneDay = 0;
		safeSnowBall = 0;
		safeArrow = 0;
		chesttimes = new HashMap<>();
		sectorName = CoreConfig.CURRENT_SECTOR_NAME;
		sellConfig = Collections.singletonList("");
		insert();
	}



	public User(final Player player)
	{
		super();
		uuid = player.getUniqueId();
		lastName = player.getName();
		firstIP = player.getAddress().getHostString();
		lastIP = player.getAddress().getHostString();
		chatControl = "";
		gameMode = player.getGameMode();
		fly = player.getAllowFlight();
		god = (player.getGameMode() == GameMode.CREATIVE);
		kills = 0;
		deaths = 0;
		logouts = 0;
		killStreak = 0;
		timePlay = 0;
		money = 0;
		eatKox = 0;
		eatRef = 0;
		throwPearl = 0;
		openPremiumChest = 0;
		safeKox = 0;
		safeRef = 0;
		safePearl = 0;
		stoneLimit = 0;
		placeChest = 0;
		boostMoney = 0;
		boostLvL = 0;
		boostKox = 0;
		boostRef = 0;
		boostChest = 0;
		boostKill = 0;
		eventTurboMoney = 1;
		eventTurboMoneyTime = 0L;
		level = 0;
		lastJoin = System.currentTimeMillis();
		kitVIPTime = 0L;
		kitSVIPTime = 0L;
		kitJEDZENIETime = 0L;
		exp = 0;
		lastLocation = player.getLocation();
		homeLocation = player.getWorld().getSpawnLocation();
		protection = System.currentTimeMillis() + CoreConfig.PROTECTIONMANAGER_PROTECTIONTIME * 60 * 1000;
		takenAchievement = new ArrayList<>();
		rank = RankConfig.DEFAULT_RANK;
		drops = Arrays.asList(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0);
		turboDropTime = 0L;
		turboExpTime = 0L;
		turboDrop = false;
		turboExp = false;
		bonusDrop = 0.0;
		breakStoneDay = 0;
		safeSnowBall = 0;
		safeArrow = 0;
		chesttimes = new HashMap<>();
		sectorName = CoreConfig.CURRENT_SECTOR_NAME;
		sellConfig =  new ArrayList<>();
		insert();
	}

	public void delete() {
		RedisChannel.INSTANCE.USERS.removeAsync(this.uuid);
	}

	public void insert() {
		RedisChannel.INSTANCE.USERS.putAsync(this.uuid, GsonUtil.toJson(this));
	}

	public void update(final boolean now) {
		insert();
	}
	
	//Methods
	public void addTimePlay(int timeplay){
		timePlay += timeplay;
	}
    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }
    public OfflinePlayer getOfflinePlayer() {
    	return Bukkit.getOfflinePlayer(uuid);
    }
    public double getExpToLevel()
    {
    	if (getLevel() >= 0 && getLevel() < 5){
    	return MathUtil.round(45.2 * getLevel(), 2);
    	}else 
    		if (getLevel() >= 5 && getLevel() < 20){
    			return MathUtil.round(65.4 * getLevel(), 2);
    	}	else 
    			if (getLevel() >= 20 && getLevel() < 35){
    			return MathUtil.round(72.1 * getLevel(), 2);
    	}		else 
    				if (getLevel() >= 35 && getLevel() < 60){
    					return MathUtil.round(83.5 * getLevel(), 2);
    	}
    				else 
    					if (getLevel() >= 60 && getLevel() < 100){
    						return MathUtil.round(103.7 * getLevel(), 2);
    	}
    					else{
    						return MathUtil.round(125.4 * getLevel(), 2);
    	}
    }
    public double getKd() {
        if (getKills() == 0 && getDeaths() == 0) {
            return 0;
        }
        else 
        	if (getKills() > 0 && getDeaths() == 0) {
        		return getKills();
        }
        	else 
        		if (getDeaths() > 0 && getKills() == 0) {
        			return -getDeaths();
        }
        		else {
        			return MathUtil.round(getKills() / (double) getDeaths(), 2);
        }
    }
    
    public List<String> getChatControl(){
    	return ListUtil.convertStringToList(chatControl);
    }
    
    void setChatControl(List<String> list){
    	this.chatControl = ListUtil.convertListToString(list);
    }
    
    public void addMoney(int money){
    	this.money += money;
    }
    
    public void addLevel(int level){
    	this.level += level;
    }
    
    public void addExp(double exp){
    	this.exp += exp;
    }

    public Sector getSector(){
		return SectorManager.getSector(getSectorName());
	}

	public boolean isOnline(){
		return getSector().isOnline() && getSector().getOnlinePlayers().contains(getLastName());
	}
    
    public void addBoostChest(int boost){
    	this.boostChest += boost;
    	update(false);
    }
    public void addBoostMoney(int boost){
    	this.boostMoney += boost;
    	update(false);
    }
    public void addBoostKox(int boost){
    	this.boostKox += boost;
    	update(false);
    }
    public void addBoostKill(int boost){
    	this.boostKill += boost;
    	update(false);
    }
    public void addBoostLvL(int boost){
    	this.boostLvL += boost;
    	update(false);
    }
    public void addBoostRef(int boost){
    	this.boostRef += boost;
    	update(false);
    }
    public void addKills(int kills) {
    	this.kills += kills;
    }
    public void addSafeKox(int kox) {
		this.safeKox += kox;
		update(false);
	}
    public void addSafeRef(int ref) {
		this.safeRef += ref;
		update(false);
	}
    public void addSafePearl(int pearl) {
		this.safePearl += pearl;
		update(false);
	}
	public void addSafeArrow(int arrows){
		this.safeArrow += arrows;
		update(false);
	}
	public void removeSafeArrow(int arrows){
		this.safeArrow -= arrows;
		update(false);
	}
    public void addLogouts(int lamounts) {
		this.logouts += lamounts;
	}
    public void addPlaceChest(int chest) {
		this.placeChest += chest;
	}

	//Tu zaczynam
	public void addBreakStoneDay(int stone){
		this.breakStoneDay += stone;
	}
    public void addEatKox(int kox) {
		this.eatKox += kox;
	}
    public void addEatRef(int ref) {
		this.eatRef += ref;
	}
    public void addThrowPearl(int pearl) {
    	this.throwPearl += pearl;
    }
    public void addOpenPremiumChest(int chest) {
		this.openPremiumChest += chest;
	}
    public void addKillStreak(int killstreak) {
		this.killStreak += killstreak;
	}
    public void addDeaths(int deaths) {
		this.deaths += deaths;
	}
    public void removeMoney(int money){
    	this.money -= money;
    }
    public void removeLevel(int level) {
    	this.level -= level;
    	update(false);
    }
    public void removeSafeKox(int kox) {
		this.safeKox -= kox;
		update(false);
	}
    public void removeSafeRef(int ref) {
		this.safeRef -= ref;
		update(false);
	}
    public void removeSafePearl(int pearl) {
		this.safePearl -= pearl;
		update(false);
	}
    public void removePlaceChest(int chest) {
		this.placeChest -= chest;
		update(false);
	}
    public void removeExp(double exp) {
		this.exp -= exp;
	}
    public boolean isBoughtMine(final Mine m){
    	return m.getAllowedMembers().contains(this.uuid);
	}
    public int getStoneLimitUser(){
    	final int i = getStoneLimit();
    	int ii;
    	int iii = 0;
    	if(getPlayer().hasPermission("core.vip") || getPlayer().hasPermission("core.svip")|| getPlayer().hasPermission("core.mvip")){
    		ii = getKills() * 5;
    	}else{
    		ii = getKills() * 3;
    	}
    	Guild g = GuildManager.getGuild(getPlayer());
    	if(g != null){
    		final Mine m = MineManager.getMineByGuild(g);
    		if(m != null){
    			iii = m.getMineStoneLimit();
    		}
    	}
    	return (i + ii + iii);
    }
    public boolean isPremium() {
    	final Player p = getPlayer();
    	return (p.hasPermission("core.vip") || p.hasPermission("core.svip") || p.hasPermission("core.mvip") || p.hasPermission("core.premium"));
    }
    public boolean isProtection(){
		return protection >= System.currentTimeMillis();
	}
    public int getBoostEarnMoney() {
    	return (getBoostMoney() == 3 ? 35 : (getBoostMoney() == 2 ? 20 : (getBoostMoney() == 1 ? 10 : 0)));
    }
    public String getOnlineTime() {
    	return Util.secondsToString((int)((int)(System.currentTimeMillis() - getLastJoin())/1000L)).replace(" ", "");
    }
    public Rank getUserRank(){
		return Core.getRankManager().getRank(getRank());
	}

	public int getDropDiamonds(){
		return drops.get(0);
	}
	public int getDropEmeralds(){
		return drops.get(1);
	}
	public int getDropGolds(){
		return drops.get(2);
	}
	public int getDropIrons(){
		return drops.get(3);
	}
	public int getDropSands(){
		return drops.get(4);
	}
	public int getDropCoals(){
		return drops.get(5);
	}
	public int getDropRedStones(){
		return drops.get(6);
	}
	public int getDropLapises(){
		return drops.get(7);
	}
	public int getDropGunPowders(){
		return drops.get(8);
	}
	public int getDropPearls(){
		return drops.get(9);
	}
	public int getDropSlimeBalls(){
		return drops.get(10);
	}
	public int getDropApples(){
		return drops.get(11);
	}
	public int getDropBooks(){
		return drops.get(12);
	}
	public int getDropChests(){
		return drops.get(13);
	}
	public int getDropStones(){
		return drops.get(14);
	}

	public void setDropDiamonds(int diamonds){
		drops.set(0,diamonds);
	}
	public void setDropEmeralds(int emeralds){
		drops.set(1,emeralds);
	}
	public void setDropGolds(int golds){
		drops.set(2,golds);
	}
	public void setDropIrons(int irons){
		drops.set(3, irons);
	}
	public void setDropSands(int sands){
		drops.set(4,sands);
	}
	public void setDropCoals(int coals){
		drops.set(5,coals);
	}
	public void setDropRedStones(int redStones){
		drops.set(6,redStones);
	}
	public void setLapises(int lapises){
		drops.set(7,lapises);
	}
	public void setDropGunPowdres(int gunPowdres){
		drops.set(8,gunPowdres);
	}
	public void setDropPearls(int pearls){
		drops.set(9,pearls);
	}
	public void setDropSlimeBalls(int slimeBalls){
		drops.set(10,slimeBalls);
	}
	public void setDropApples(int apples){
		drops.set(11,apples);
	}
	public void setDropBooks(int books){
		drops.set(12,books);
	}
	public void setDropChests(int chests){
		drops.set(13,chests);
	}
	public void setDropStones(int stones){
		drops.set(14,stones);
	}

	public void addDropDiamonds(int diamonds){
		drops.set(0,getDropDiamonds() + diamonds);
	}
	public void addDropEmeralds(int emeralds){
		drops.set(1,getDropEmeralds() + emeralds);
	}
	public void addDropGolds(int golds){
		drops.set(2,getDropGolds() + golds);
	}
	public void addDropIrons(int irons){
		drops.set(3, getDropIrons() + irons);
	}
	public void addDropSands(int sands){
		drops.set(4,getDropSands() + sands);
	}
	public void addDropCoals(int coals){
		drops.set(5,getDropCoals() + coals);
	}
	public void addDropRedStones(int redStones){
		drops.set(6,getDropRedStones() + redStones);
	}
	public void addLapises(int lapises){
		drops.set(7,getDropLapises() + lapises);
	}
	public void addDropGunPowdres(int gunPowdres){
		drops.set(8,getDropGunPowders() + gunPowdres);
	}
	public void addDropPearls(int pearls){
		drops.set(9,getDropPearls() + pearls);
	}
	public void addDropSlimeBalls(int slimeBalls){
		drops.set(10,getDropSlimeBalls() + slimeBalls);
	}
	public void addDropApples(int apples){
		drops.set(11,getDropApples() + apples);
	}
	public void addDropBooks(int books){
		drops.set(12,getDropBooks() + books);
	}
	public void addDropChests(int chests){
		drops.set(13,getDropChests() + chests);
	}
	public void addDropStones(int stones){
		drops.set(14,getDropStones() + stones);
	}

	public int getLimitStone(){
		int limit = 0;
		for(Mine mine : MineManager.getMines().values()){
			if(mine.getAllowedMembers().contains(this.uuid))
			{
				limit= limit + mine.getMineStoneLimit();
			}
		}
		limit = limit + ((isPremium()) ? 5*getKills() : 3*getKills());
		return limit;
	}
	public void addBounusDrop(double bonusDrop){
		this.bonusDrop += bonusDrop;
		update(false);
	}

	public Player asPlayer() {
		return Bukkit.getPlayer(this.uuid);
	}

	public EntityPlayer asEntityPlayer() {
		final Player p = asPlayer();
		if (p == null) {
			return null;
		}
		return ((CraftPlayer) p).getHandle();
	}

	public Long getLastUpdate() {
		return lastUpdate;
	}

	public void addSafeSnowBall(int value){
		this.safeSnowBall += value;
	}
	public void removeSafeSnowBall(int value){
		this.safeSnowBall -= value;
	}

	public void setLastUpdate(Long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public void addMoneyViaPacket(int money){
		final AddStatisticPacket packet = new AddStatisticPacket(getUuid(),"money",money);
		RedisClient.sendSectorsPacket(packet);
	}
	public void addLevelViaPacket(int level){
		final AddStatisticPacket packet = new AddStatisticPacket(getUuid(),"level",level);
		RedisClient.sendSectorsPacket(packet);
	}
	public void addExpViaPacket(float exp){
		final AddStatisticPacket packet = new AddStatisticPacket(getUuid(),"exp",exp);
		RedisClient.sendSectorsPacket(packet);
	}
	public void addKillsViaPacket(int kills){
		final AddStatisticPacket packet = new AddStatisticPacket(getUuid(),"kills",kills);
		RedisClient.sendSectorsPacket(packet);
	}
	public void addThrowPearlViaPacket(int throwPearl){
		final AddStatisticPacket packet = new AddStatisticPacket(getUuid(),"throwpearl",throwPearl);
		RedisClient.sendSectorsPacket(packet);
	}
	public void addEatRefViaPacket(int eatRef){
		final AddStatisticPacket packet = new AddStatisticPacket(getUuid(),"eatref",eatRef);
		RedisClient.sendSectorsPacket(packet);
	}
	public void addEatKoxViaPacket(int eatKox){
		final AddStatisticPacket packet = new AddStatisticPacket(getUuid(),"eatkox",eatKox);
		RedisClient.sendSectorsPacket(packet);
	}
	public void addOpenPremiumChestViaPacket(int openPremiumChest){
		final AddStatisticPacket packet = new AddStatisticPacket(getUuid(),"openpremiumchest",openPremiumChest);
		RedisClient.sendSectorsPacket(packet);
	}
	public void addKillStreakViaPacket(int add){
		final AddStatisticPacket packet = new AddStatisticPacket(getUuid(),"killstreak",add);
		RedisClient.sendSectorsPacket(packet);
	}
	public void addBoostLvLViaPacket(int add){
		final AddStatisticPacket packet = new AddStatisticPacket(getUuid(),"boostlvl",add);
		RedisClient.sendSectorsPacket(packet);
	}
	public void addBoostMoneyViaPacket(int add){
		final AddStatisticPacket packet = new AddStatisticPacket(getUuid(),"boostmoney",add);
		RedisClient.sendSectorsPacket(packet);
	}
	public void addBoostKoxViaPacket(int add){
		final AddStatisticPacket packet = new AddStatisticPacket(getUuid(),"boostkox",add);
		RedisClient.sendSectorsPacket(packet);
	}
	public void addBoostRefViaPacket(int add){
		final AddStatisticPacket packet = new AddStatisticPacket(getUuid(),"boostref",add);
		RedisClient.sendSectorsPacket(packet);
	}
	public void addBoostChestViaPacket(int add){
		final AddStatisticPacket packet = new AddStatisticPacket(getUuid(),"boostchest",add);
		RedisClient.sendSectorsPacket(packet);
	}
	public void addBoostKillViaPacket(int add){
		final AddStatisticPacket packet = new AddStatisticPacket(getUuid(),"boostkill",add);
		RedisClient.sendSectorsPacket(packet);
	}
	public void addBonusDropViaPacket(float add){
		final AddStatisticPacket packet = new AddStatisticPacket(getUuid(),"bonusdrop",add);
		RedisClient.sendSectorsPacket(packet);
	}
	public void addLogoutViaPacket(float add){
		final AddStatisticPacket packet = new AddStatisticPacket(getUuid(),"logout",add);
		RedisClient.sendSectorsPacket(packet);
	}
	public void addTimePlayViaPacket(float add){
		final AddStatisticPacket packet = new AddStatisticPacket(getUuid(),"timeplay",add);
		RedisClient.sendSectorsPacket(packet);
	}
	public void addPlaceChestViaPacket(float add){
		final AddStatisticPacket packet = new AddStatisticPacket(getUuid(),"placechest",add);
		RedisClient.sendSectorsPacket(packet);
	}
	public void addDeathsViaPacket(float add){
		final AddStatisticPacket packet = new AddStatisticPacket(getUuid(),"deaths",add);
		RedisClient.sendSectorsPacket(packet);
	}
	public void setKillsViaPacket(int set){
		final SetStatisticPacket packet = new SetStatisticPacket(getUuid(),set,"kills");
		RedisClient.sendSectorsPacket(packet);
	}
	public void setDeathsViaPacket(int set){
		final SetStatisticPacket packet = new SetStatisticPacket(getUuid(),set,"deaths");
		RedisClient.sendSectorsPacket(packet);
	}
	public void setLogoutsViaPacket(int set){
		final SetStatisticPacket packet = new SetStatisticPacket(getUuid(),set,"logouts");
		RedisClient.sendSectorsPacket(packet);
	}
	public void setKillStreakViaPacket(int set){
		final SetStatisticPacket packet = new SetStatisticPacket(getUuid(),set,"killstreak");
		RedisClient.sendSectorsPacket(packet);
	}
	public void setLevelViaPacket(int set){
		final SetStatisticPacket packet = new SetStatisticPacket(getUuid(),set,"level");
		RedisClient.sendSectorsPacket(packet);
	}
	public void setExpViaPacket(int set){
		final SetStatisticPacket packet = new SetStatisticPacket(getUuid(),set,"exp");
		RedisClient.sendSectorsPacket(packet);
	}
	public void setTimePlayViaPacket(int set){
		final SetStatisticPacket packet = new SetStatisticPacket(getUuid(),set,"timeplay");
		RedisClient.sendSectorsPacket(packet);
	}
	public void setMoneyViaPacket(int set){
		final SetStatisticPacket packet = new SetStatisticPacket(getUuid(),set,"money");
		RedisClient.sendSectorsPacket(packet);
	}
	public void setMineStoneLimitViaPacket(int set){
		final SetStatisticPacket packet = new SetStatisticPacket(getUuid(),set,"minestonelimit");
		RedisClient.sendSectorsPacket(packet);
	}
	public void setBreakStonesViaPacket(int set){
		final SetStatisticPacket packet = new SetStatisticPacket(getUuid(),set,"breakstone");
		RedisClient.sendSectorsPacket(packet);
	}
	public void setBonusDropViaPacket(float set){
		final SetStatisticPacket packet = new SetStatisticPacket(getUuid(),set,"bonusdrop");
		RedisClient.sendSectorsPacket(packet);
	}
	public void removeMoneyViaPacket(float remove){
		final RemoveStatisticPacket packet = new RemoveStatisticPacket(getUuid(),"money",remove);
		RedisClient.sendSectorsPacket(packet);
	}
	public void removeExpViaPacket(float remove){
		final RemoveStatisticPacket packet = new RemoveStatisticPacket(getUuid(),"exp",remove);
		RedisClient.sendSectorsPacket(packet);
	}
	public void removeLevelViaPacket(int level) {
		final RemoveStatisticPacket packet = new RemoveStatisticPacket(getUuid(),"level",level);
		RedisClient.sendSectorsPacket(packet);
	}
	public Guild getGuild(){
		return GuildManager.getGuild(getPlayer());
	}
}
