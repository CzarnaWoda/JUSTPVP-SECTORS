package pl.blackwater.core;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandMap;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import pl.blackwater.bossx.commands.BossCommand;
import pl.blackwater.bossx.listeners.BossDamageListener;
import pl.blackwater.bossx.listeners.BossDeathListener;
import pl.blackwater.bossx.settings.BossConfig;
import pl.blackwater.bossx.settings.RewardsConfig;
import pl.blackwater.chestpvpdrop.listeners.DropBlockBreakListener;
import pl.blackwater.chestpvpdrop.listeners.GeneratorListener;
import pl.blackwater.chestpvpdrop.managers.DropFile;
import pl.blackwater.chestpvpdrop.managers.DropManager;
import pl.blackwater.chestpvpdrop.settings.Config;
import pl.blackwater.core.commands.*;
import pl.blackwater.core.commands.chests.ChestCommand;
import pl.blackwater.core.commands.ranks.RankCommand;
import pl.blackwater.core.data.Backup;
import pl.blackwater.core.data.Rank;
import pl.blackwater.core.enums.BackupType;
import pl.blackwater.core.enums.TabListType;
import pl.blackwater.core.events.EventComparatorTask;
import pl.blackwater.core.halloween.HalloweenManager;
import pl.blackwater.core.halloween.PumpkinTask;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.inventories.BackupInventory;
import pl.blackwater.core.inventories.EffectShopInventory;
import pl.blackwater.core.inventories.ServerInventory;
import pl.blackwater.core.listeners.*;
import pl.blackwater.core.managers.*;
import pl.blackwater.core.settings.*;
import pl.blackwater.core.tasks.*;
import pl.blackwater.core.tasks.api.ScheduledTask;
import pl.blackwater.enchantgui.GUI;
import pl.blackwater.enchantgui.configuration.Settings;
import pl.blackwater.enchantgui.listeners.InventoryClick;
import pl.blackwater.enchantgui.listeners.InventoryClose;
import pl.blackwater.enchantgui.listeners.InventoryDrag;
import pl.blackwater.guilds.commands.GuildAdminCommand;
import pl.blackwater.guilds.commands.GuildCommand;
import pl.blackwater.guilds.data.GuildShopItem;
import pl.blackwater.guilds.managers.GuildManager;
import pl.blackwater.guilds.managers.WarManager;
import pl.blackwater.guilds.ranking.RankingManager;
import pl.blackwater.guilds.settings.EffectsConfig;
import pl.blackwater.guilds.settings.GuildConfig;
import pl.blackwater.guilds.tasks.CheckValidityTask;
import pl.blackwater.market.commands.MarketCommand;
import pl.blackwater.market.data.Auction;
import pl.blackwater.market.listeners.AuctionListener;
import pl.blackwater.market.managers.AuctionManager;
import pl.blackwater.storage.commands.StorageCommand;
import pl.blackwater.storage.commands.StorageCreateCommand;
import pl.blackwater.storage.managers.StorageRentManager;
import pl.blackwater.storage.settings.StorageConfig;
import pl.blackwater.storage.tasks.ExpireTimeStorageTask;
import pl.blackwater.storage.tasks.SignUpdateTask;
import pl.blackwaterapi.commands.APICommand;
import pl.blackwaterapi.commands.Command;
import pl.blackwaterapi.commands.CommandManager;
import pl.blackwaterapi.configs.ConfigCreator;
import pl.blackwaterapi.configs.ConfigManager;
import pl.blackwaterapi.data.APIConfig;
import pl.blackwaterapi.data.CommandLogStorage;
import pl.blackwaterapi.gui.listeners.InventoryListener;
import pl.blackwaterapi.incognitothreads.IndependentThread;
import pl.blackwaterapi.sockets.SocketTask;
import pl.blackwaterapi.timer.TimerManager;
import pl.blackwaterapi.utils.Logger;
import pl.blackwaterapi.utils.Ticking;
import pl.blackwaterapi.utils.TimeUtil;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.packets.impl.backups.BackupCreatePacket;
import pl.justsectors.packets.impl.others.SectorDisablePacket;
import pl.justsectors.packets.impl.others.SectorEnablePacket;
import pl.justsectors.redis.RedisManager;
import pl.justsectors.redis.channels.RedisChannel;
import pl.justsectors.redis.client.RedisClient;
import pl.justsectors.redis.enums.ChannelType;
import pl.justsectors.redis.listeners.*;
import pl.justsectors.redis.listeners.api.RedisListener;
import pl.justsectors.sectors.SectorManager;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Core extends JavaPlugin implements Colors{
	@Getter private static Core plugin;
	@Getter private static ChatManager chatManager;
	@Getter EffectShopInventory effectShopInventory;
	@Getter private static RankManager rankManager;
	@Getter private static RankConfig rankConfig;
	@Getter private static TreasureChestManager treasureChestManager;
	@Getter private static YouTubeConfig youTubeConfig;
	@Getter private static SpecialItemsManager specialItemsManager;
	@Getter private static BackupManager backupManager;
	@Getter private static BossBarManager bossBarManager;
	private static CommandMap cmdMap;
	@Getter
	private static PluginManager pluginManager;
	private static SocketTask socketTask;
	public static String nmsver;
	@Getter
	private static APIConfig apiConfig;
	@Getter
	private static CommandLogStorage commandLogStorage;
	@Getter
	private static List<Listener> listeners = new ArrayList<>();
	public static Map<String, Integer> boss = new HashMap<>();

	@Getter
	private static List<GuildShopItem> gshopitems;

	@Override
	public void onEnable(){
		final long start = System.currentTimeMillis();
		sendSystemMessage("Enabling FinalCore...");
		plugin = this;
		//API
		getServer().getMessenger().registerOutgoingPluginChannel(this, "bp:waypoint");
		new IndependentThread().start();
		Logger.info("BWAPI - Registering nmsver...");
		nmsver = Bukkit.getServer().getClass().getPackage().getName();
		nmsver = nmsver.substring(nmsver.lastIndexOf(".") + 1);
		Logger.info("BWAPI - Nmsver registered, " + nmsver);

		Logger.info("BWAPI - Ticking infection - start");
		new Ticking().start();
		Logger.info("BWAPI - Start ticking !");

		Logger.info("BWAPI - Registering configs...");
		registerConfig( apiConfig = new APIConfig());
		registerConfig(commandLogStorage = new CommandLogStorage());
		Logger.info("BWAPI - Configs registered");


		Logger.info("BWAPI - Registering commands...");
		registerCommand(new APICommand());
		Logger.info("BWAPI - Command registered");

		Logger.info("BWAPI - Registering listeners...");
		registerListener(this, new TimerManager(), new InventoryListener(), new TeleportManager());
		Logger.info("BWAPI - Listeners registered");
		rankManager = new RankManager();
		treasureChestManager = new TreasureChestManager();
		//tables
		/*addMYSQLTable("CREATE TABLE IF NOT EXISTS `%P%users` (" + "`id` int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT," + " `uuid` varchar(255) NOT NULL, `lastName` varchar(16) NOT NULL, `firstIP` varchar(20) NOT NULL, `lastIP` varchar(20) NOT NULL, `chatControl` text NOT NULL, `gameMode` int(1) NOT NULL, `fly` int(1) NOT NULL, `god` int(1) NOT NULL, `kills` int(5), `deaths` int(5) NOT NULL, `logouts` int(3) NOT NULL, `killStreak` int(4) NOT NULL, `timePlay` int(16) NOT NULL, `money` int(12)  NOT NULL, `eatKox` int(3) NOT NULL, `eatRef` int(3)  NOT NULL, `throwPearl` int(3)  NOT NULL, `openPremiumChest` int(3) NOT NULL, `safeKox` int(4) NOT NULL, `safeRef` int(4) NOT NULL, `safePearl` int(4) NOT NULL, `stoneLimit` int(5) NOT NULL, `placeChest`  int(2) NOT NULL, `boostMoney` int(1) NOT NULL, `boostLvL` int(1) NOT NULL, `boostKox` int(1) NOT NULL, `boostRef` int(1) NOT NULL, `boostChest` int(1) NOT NULL, `boostKill` int(1) NOT NULL, `eventTurboMoney` int(1) NOT NULL, `eventTurboMoneyTime` bigint(20) NOT NULL, `level` int(3) NOT NULL, `lastJoin` bigint(20)  NOT NULL, `kitVIPTime` bigint(20) NOT NULL, `kitSVIPTime` bigint(20) NOT NULL, `exp` float(24) NOT NULL, `lastLocation` text NOT NULL, `homeLocation` text NOT NULL, `protection` bigint(20) NOT NULL, `takenAchievement` text NOT NULL, `rank` varchar(20) NOT NULL, `drops` text NOT NULL, `turboDropTime` bigint(22) NOT NULL, `turboExpTime` bigint(22) NOT NULL, `turboDrop` int(1) NOT NULL, `turboExp` int(1) NOT NULL, `bonusDrop` double NOT NULL, `breakStoneDay` int(6) NOT NULL, `chesttimes` text NOT NULL);");
		addMYSQLTable("CREATE TABLE IF NOT EXISTS `%P%whitelisted` (" + "`id` int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT," + " `uuid` varchar(255) NOT NULL NOT NULL);");
		addMYSQLTable("CREATE TABLE IF NOT EXISTS `%P%bans` (" + "`id` int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT," + " `uuid` varchar(255) NOT NULL,`reason` varchar(255) NOT NULL,`admin` varchar(255) NOT NULL,`createtime` bigint(22) NOT NULL,`expiretime` bigint(22) NOT NULL,`unban` int(1) NOT NULL NOT NULL);");
		addMYSQLTable("CREATE TABLE IF NOT EXISTS `%P%ipbans` (" + "`id` int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT," + " `ip` varchar(32) NOT NULL,`reason` varchar(255) NOT NULL,`admin` varchar(255) NOT NULL,`createtime` bigint(22) NOT NULL,`expiretime` bigint(22) NOT NULL,`unban` int(1) NOT NULL NOT NULL);");
		addMYSQLTable("CREATE TABLE IF NOT EXISTS `%P%warp` (" + "`id` int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT," + " `name` varchar(32) NOT NULL,`location` text NOT NULL, `pex` varchar(32) NOT NULL NOT NULL);");
		addMYSQLTable("CREATE TABLE IF NOT EXISTS `%P%mine` (" + "`id` int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT," + "`index` int(16) NOT NULL,`name` varchar(64) NOT NULL,`type` varchar(6) NOT NULL,`cost` int(16) NOT NULL,`location` text NOT NULL,`level` int(16) NOT NULL,`mainRegion` varchar(32) NOT NULL,`stoneRegion` varchar(32) NOT NULL,`minestonelimit` int(16) NOT NULL,`guild` varchar(32) NOT NULL NOT NULL);");
		addMYSQLTable("CREATE TABLE IF NOT EXISTS `%P%headhunter` (" + "`id` int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT," + " `victim` varchar(32) NOT NULL,`employer` varchar(32) NOT NULL, `payCheck` int(12) NOT NULL NOT NULL);");
		addMYSQLTable("CREATE TABLE IF NOT EXISTS `%P%ranksets` (" + "`id` int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT," + " `user` varchar(255) NOT NULL,`rank` varchar(16) NOT NULL, `previousRank` varchar(16) NOT NULL, `admin` varchar(32) NOT NULL, `expireTime` bigint(22) NOT NULL NOT NULL);");
		addMYSQLTable("CREATE TABLE IF NOT EXISTS `%P%chests` (" + "`id` int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT," + " `uuid` varchar(255) NOT NULL,`location` text NOT NULL, `type` varchar(32) NOT NULL, `inventory` text NOT NULL,`time` bigint(22) NOT NULL NOT NULL);");
		addMYSQLTable("CREATE TABLE IF NOT EXISTS `%P%backups` (" + "`id` int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT," + " `backupUUID` varchar(255) NOT NULL, `owner` varchar(255) NOT NULL, `armor` text NOT NULL, `inventory` text NOT NULL, `backupTime` bigint(22) NOT NULL, `takeBackupTime` bigint(22) NOT NULL, `backupType` varchar(12) NOT NULL, `backupCreator` varchar(16) NOT NULL NOT NULL);");
		*/
		//configs
		registerConfig(new CoreConfig());
		new CoreConfig();
		//redis jak najwiczesniej
		RedisManager.setup();
		RedisChannel.INSTANCE.setupChannels();
		final Set<RedisListener> redisListeners = new HashSet<>(Arrays.asList(
				new GlobalPacketListener(ChannelType.GLOBAL_PACKETS, RedisChannel.INSTANCE.globalPacketTopic),
				new SectorPacketListener(ChannelType.PACKET_TO_SINGLE_SECTOR, RedisChannel.INSTANCE.currentSectorTopic),
				new SpigotPacketListener(ChannelType.PACKET_TO_SPIGOTS, RedisChannel.INSTANCE.globalSpigotPacketTopic),
				new UserListener(ChannelType.USER, RedisChannel.INSTANCE.userTopic),
				new ProxiesPacketListener<>(ChannelType.PACKET_TO_PROXIES, RedisChannel.INSTANCE.proxiesPacketTopic)
		));
		registerConfig(new SectorConfig());
		new SectorConfig();
		for (RedisListener redisListener : redisListeners) {
			RedisManager.registerListener(redisListener);
		}
		RedisClient.sendSectorsPacket(new SectorEnablePacket(CoreConfig.CURRENT_SECTOR_NAME));
		SectorManager.getCurrentSector().ifPresent(sector -> {
			sector.getRedisOnlinePlayers().clear();
		});
		//REDIS NEW SYSTEM <- SEARCH
		/*for(Sector sector : SectorManager.getSectors().values())
		{
			RSet<String> redisList = RedisManager.getRedisConnection().getSet(sector.getSectorName() + "-online");
			for (String userName : redisList) {
				sector.getOnlinePlayers().add(userName);
			}
			getLogger().log(Level.INFO, "Sektor " + sector.getSectorName() + " zaladowal " + sector.getOnlinePlayers().size() + " graczy online!");
		}*/
		registerConfig(new MessageConfig());
		new MessageConfig();
		registerConfig(new OsiagnieciaConfig());
		new OsiagnieciaConfig();
		registerConfig(new ShopConfig());
		new ShopConfig();
		registerConfig(new KitsConfig());
		new KitsConfig();
		registerConfig(new EffectConfig());
		new EffectConfig();
		registerConfig(new FanPageConfig());
		new FanPageConfig();
		registerConfig(new RankConfig());
		registerConfig(new BackupConfig());
		new BackupConfig();
		rankConfig = new RankConfig();
		registerConfig(new YouTubeConfig());
		youTubeConfig = new YouTubeConfig();
		DropFile.saveDefaultConfig();
		Config.reloadConfig();
		registerConfig(new TabListConfig());
		new TabListConfig();
		registerConfig(new Settings());
		new Settings();
		//command
		registerCommand(new ChatCommand());
		registerCommand(new RankingCommand());
		registerCommand(new ACommand());
		registerCommand(new AddStatisticCommand());
		registerCommand(new BroadcastCommand());
		registerCommand(new BroadCastTitleCommand());
		registerCommand(new ChangeItemCommand());
		registerCommand(new ClearCommand());
		registerCommand(new EnchantCommand());
		registerCommand(new FlyCommand());
		registerCommand(new GamemodeCommand());
		registerCommand(new GcCommand());
		registerCommand(new GiftCommand());
		registerCommand(new GiveCommand());
		registerCommand(new GodCommand());
		registerCommand(new HeadCommand());
		registerCommand(new HealCommand());
		registerCommand(new HelpOpCommand());
		registerCommand(new HomeCommand());
		registerCommand(new HelpCommand());
		registerCommand(new KickallCommand());
		registerCommand(new KickCommand());
		registerCommand(new OpenCommand());
		registerCommand(new PayCommand());
		registerCommand(new PvPCommand());
		registerCommand(new RankingCommand());
		registerCommand(new RegulaminCommand());
		registerCommand(new AdminPanelCommand());
		registerCommand(new ChatControlCommand());
		registerCommand(new ShopCommand());
		registerCommand(new RepairCommand());
		registerCommand(new KitCommand());
		registerCommand(new SetSpawnCommand());
		registerCommand(new SpawnCommand());
		registerCommand(new WarpManagerCommand());
		registerCommand(new WarpCommand());
		registerCommand(new TeleportCommand());
		registerCommand(new TeleportHereCommand());
		registerCommand(new SpeedCommand());
		registerCommand(new ItemCommand());
		registerCommand(new VIPCommand());
		registerCommand(new SVIPCommand());
		registerCommand(new MineCommand());
		registerCommand(new MineCreateCommand());
		registerCommand(new MineSetCommand());
		registerCommand(new MoneyCommand());
		registerCommand(new OsiagnieciaCommand());
		registerCommand(new RefreshCommand());
		registerCommand(new SetHomeCommand());
		registerCommand(new ListCommand());
		registerCommand(new TellCommand());
		registerCommand(new ReplyCommand());
		registerCommand(new DropCommand());
		registerCommand(new EventCommand());
		registerCommand(new SchowekCommand());
		registerCommand(new IgnoreCommand());
		registerCommand(new TopCommand());
		registerCommand(new RankingResetCommand());
		registerCommand(new RankingSetCommand());
		registerCommand(new TimeCommand());
		registerCommand(new WhoisCommand());
		registerCommand(new LevelCommand());
		registerCommand(new SideBarCommand());
		registerCommand(new SlotCommand());
		registerCommand(new StopCommand());
		registerCommand(new VanishCommand());
		registerCommand(new ItemShopCommand());
		registerCommand(new EffectShopCommand());
		registerCommand(new ProtectionCommand());
		registerCommand(new RankCommand());
		registerCommand(new ChestCommand());
		registerCommand(new SpecialCommand());
		registerCommand(new SpecialItemCommand());
		registerCommand(new ServerCommand());
		registerCommand(new BackupCommand());
		registerCommand(new FunCommand());
		registerCommand(new IncognitoCommand());
		registerCommand(new ChannelCommand());
		registerCommand(new ProxyBanCommand());
		registerCommand(new HalloweenCommand());
		//managers
		UserManager.setup();
		//BanManager.setup();
		//BanIPManager.setup();
		WarpManager.setup();
		//WhiteListManager.setup();
		MineManager.setup();
		DropManager.setup();
		rankConfig.loadRanks();
		chatManager = new ChatManager();
		RankSetManager.setup();
		treasureChestManager.setup();
		backupManager = new BackupManager();
		backupManager.load();
		new BackupInventory();
		bossBarManager = new BossBarManager();
		//listeners
		registerListener(getPlugin(),new PlayerCloseInventoryListener(),new InventoryClick(), new InventoryClose(), new InventoryDrag(),new PortalPlayerListener(),new AsyncPlayerChatListener(),new DropBlockBreakListener(), new PlayerJoinQuitListener(),new InventoryClickListener(),new CraftItemListener(),new BlockBreakListener(), new BlockStrenghtListener(), new BlockPlaceListener(),new EnchantItemListener(), new PlayerInteractListener(), new PlayerItemConsumeListener(), new PlayerTeleportListener(), new GodListener(), new EntityDamageByEntityListener(), new RewardReceivedListener(), new PlayerCommandPreprocessListener(), new PlayerDeathListener(), new ItemSpawnListener(), new PlayerLoginListener(), new VanishListener(), new GeneratorListener(), new InventoryClickListener());
		//tasks
		GUI.initSecond();
		final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
		final Set<ScheduledTask> tasks = new HashSet<>(Arrays.asList(new DataSaveTask(executorService), new SectorStatusTask(executorService), new PumpkinTask(executorService)));
		for (ScheduledTask task : tasks) {
			task.runTask();
		}
		new EventsCheckTask().runTaskTimerAsynchronously(getPlugin(), 400L, TimeUtil.SECOND.getTick(15));
		new SchowekTask().runTaskTimerAsynchronously(getPlugin(), 400L, TimeUtil.SECOND.getTick(10));
		TabUpdateTask.type = TabListType.KILLS;
		new TabUpdateTask().runTaskTimerAsynchronously(getPlugin(), 400L, TimeUtil.SECOND.getTick(12));
		new CombatActionBarTask().runTaskTimerAsynchronously(getPlugin(), 20L, TimeUtil.SECOND.getTick(1));
		new CombatEndTask().runTaskTimerAsynchronously(getPlugin(), 40L , TimeUtil.SECOND.getTick(1));
		new EasyScoreboardUpdateTask().runTaskTimerAsynchronously(getPlugin(), 200L, TimeUtil.SECOND.getTick(8));
		new TopUpdateTask().runTaskTimerAsynchronously(getPlugin(), 400L, TimeUtil.SECOND.getTick(30));
		new AutoMessageTask().runTaskTimerAsynchronously(getPlugin(), 400L, TimeUtil.SECOND.getTick(25));
		new RankExpireTask().runTaskTimerAsynchronously(getPlugin(), 200L, TimeUtil.SECOND.getTick(20));
		new BackupTask().runTaskTimerAsynchronously(this, 20L, TimeUtil.SECOND.getTick(BackupConfig.BACKUP_AUTOMATIC_DELAY));
		new ResetBreakStoneDayTask().runTaskTimerAsynchronously(getPlugin(), 40L, TimeUtil.SECOND.getTick(40));
		new EventComparatorTask().runTaskTimerAsynchronously(this, 40L, TimeUtil.SECOND.getTick(5));
		new AddTimePlayTask().runTaskTimerAsynchronously(this,100L,100L);
		AntyMacroTask.getRate();
		//others
		//effectShopInventory = new EffectShopInventory();
		specialItemsManager = new SpecialItemsManager();
		new ServerInventory();
		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		final long end = System.currentTimeMillis() - start;

		//BOSSES
		registerCommand((Command)new BossCommand());
		registerListener(this, (Listener) new BossDamageListener(), new BossDeathListener(), new pl.blackwater.bossx.listeners.InventoryClickListener());
		registerConfig(new BossConfig());
		registerConfig(new RewardsConfig());

		//GUILDS
		registerConfig(new GuildConfig());
		//`id`,`guild1`,`guild2`,`kills1`,`kills2`,`deaths1`,`deaths2`,`startTime`,`endTime`
		//API.addMYSQLTable("CREATE TABLE IF NOT EXISTS `%P%alliances` (" + (API.getStore().getStoreMode() == StoreMode.MYSQL ? "`id` int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT," : "`id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,") + " `guild1` varchar(5) NOT NULL, `guild2` varchar(5) NOT NULL NOT NULL);");
		registerCommand(new GuildCommand());
		registerCommand(new GuildAdminCommand());
		registerCommand(new ChujCommand());
		registerListener(this, new pl.blackwater.guilds.listeners.AsyncPlayerChatListener());
		RankingManager.sortGuildRankings();
		RankingManager.sortUserRankings();
		GuildManager.setup();
		WarManager.enable();
		//AllianceManager.enable();
		registerConfig(new EffectsConfig());
		new EffectsConfig();
		new CheckValidityTask().runTaskTimerAsynchronously(getPlugin(), TimeUtil.MINUTE.getTick(15), TimeUtil.MINUTE.getTick(15));
		sendSystemMessage("Enable FinalCore in " + end + "ms");
		//MARKETS
		Logger.info("Auction Plugin Enable (BlackWater Edition)");
		AuctionManager.loadAuctions();
		registerCommand(new MarketCommand());
		registerListener(this,new AuctionListener());
		//STORAGES
		StorageRentManager.setup();
		registerConfig(new StorageConfig());
		new StorageConfig();
		registerCommand(new StorageCreateCommand());
		registerCommand(new StorageCommand());
		registerCommand(new CustomEventCommand());
		registerListener(getPlugin(), new pl.blackwater.storage.listeners.PlayerInteractListener(), new pl.blackwater.storage.listeners.AsyncPlayerChatListener());
		new SignUpdateTask().runTaskTimer(getPlugin(), 500L, TimeUtil.SECOND.getTick(30));
		new ExpireTimeStorageTask().runTaskTimer(getPlugin(), 500L, TimeUtil.SECOND.getTick(120));
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		HalloweenManager.setup();
		gshopitems = loadGuildShop();
	}
	@Override
	public void onDisable() {
		Logger.info("BWAPI - API system disabled");
		SectorManager.getCurrentSector().get().getOnlinePlayers().clear();
		Bukkit.getScheduler().cancelTasks(getPlugin());
		RedisClient.sendSectorsPacket(new SectorDisablePacket(CoreConfig.CURRENT_SECTOR_NAME));
		SectorManager.getCurrentSector().ifPresent(sector -> sector.getRedisOnlinePlayers().clear());
		for(Player online : Bukkit.getOnlinePlayers()) {
			CombatManager.removeCombat(online);
			UserManager.leaveFromGame(online, false);
			final Backup backup = getBackupManager().createBackup(online, BackupType.CLOSESERVER, "CONSOLE");
			backup.insert();
			RedisClient.sendSectorsPacket(new BackupCreatePacket(backup));
			online.kickPlayer(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + CoreColor + BOLD + "FINAL-CORE" + SpecialSigns + " <<\n" + SpecialSigns + "* " + MainColor + "Serwer jest w trakcie wylaczania lub FINALCORE wykryl przeladowanie serwera!\n" + SpecialSigns + ">> " + CoreColor + BOLD + "FINAL-CORE" + SpecialSigns + " <<")));
		}
		for(Auction auction : AuctionManager.getAuctions().values())
		{
			auction.update(true);
		}
		for(Rank r : getRankManager().getRanks().values()){
			r.update(true);
			sendSystemMessage("RANKS -> SAVE DATA FOR " + r.getName() + " SET TO 'DONE' !");
		}
		this.getServer().getMessenger().unregisterOutgoingPluginChannel(this, "BungeeCord");
	}
	public static void sendSystemMessage(final String msg){
		Logger.fixColorSend(Util.replaceString(SpecialSigns + "* " + CoreColor + "FinalCore " + SpecialSigns + BOLD + ">> " + CoreColor + msg));
	}

	//Methods
	public static WorldGuardPlugin getWorldGuard() {
		Plugin plugin = getPlugin().getServer().getPluginManager().getPlugin("WorldGuard");

		// WorldGuard may not be loaded
		if (!(plugin instanceof WorldGuardPlugin)) {
			return null; // Maybe you want throw an exception instead
		}

		return (WorldGuardPlugin) plugin;
	}

	public static void registerListener(Plugin plugin, Listener... listeners) {
		if (pluginManager == null) {
			pluginManager = Bukkit.getPluginManager();
		}
		for (Listener listener : listeners) {
			getListeners().add(listener);
			pluginManager.registerEvents(listener, plugin);
		}
	}
	public static void error(String content) {
		Bukkit.getLogger().severe("[Server thread/ERROR] #!# " + content);
	}
	public static boolean exception(String cause, StackTraceElement[] ste) {
		error("");
		error("[NanoIncognito] Severe error:");
		error("");
		error("Server Information:");
		error("  Bukkit: " + Bukkit.getBukkitVersion());
		error("  Java: " + System.getProperty("java.version"));
		error("  Thread: " + Thread.currentThread());
		error("  Running CraftBukkit: " + Bukkit.getServer().getClass().getName().equals("org.bukkit.craftbukkit.CraftServer"));
		error("");
		if (cause == null || ste == null || ste.length < 1) {
			error("Stack trace: no/empty exception given, dumping current stack trace instead!");
			return true;
		}
		else {
			error("Stack trace: ");
		}
		error("Caused by: " + cause);
		for (StackTraceElement st : ste) {
			error("    at " + st.toString());
		}
		error("");
		error("End of Error.");
		error("");
		return false;
	}
	public static void registerCommand(Command command) {
		CommandManager.register(command);
	}
	public static void registerConfig(ConfigCreator config){
		ConfigManager.register(config);
	}


	public static SocketTask getSocketTask() {
		return socketTask;
	}

	private List<GuildShopItem> loadGuildShop(){

		final List<GuildShopItem> list = new ArrayList<>();
		final ItemStack i1 = new ItemStack(Material.IRON_CHESTPLATE);
		i1.addEnchantment(Enchantment.DURABILITY,3);
		i1.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,4);
		final ItemStack i2 = new ItemStack(Material.IRON_HELMET);
		i2.addEnchantment(Enchantment.DURABILITY,3);
		i2.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,4);
		final ItemStack i3 = new ItemStack(Material.IRON_LEGGINGS);
		i3.addEnchantment(Enchantment.DURABILITY,3);
		i3.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,4);
		final ItemStack i4 = new ItemStack(Material.IRON_BOOTS);
		i4.addEnchantment(Enchantment.DURABILITY,3);
		i4.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,4);
		final GuildShopItem gs1 = new GuildShopItem(i1,50);
		final GuildShopItem gs2 = new GuildShopItem(i2,50);
		final GuildShopItem gs3 = new GuildShopItem(i3,50);
		final GuildShopItem gs4 = new GuildShopItem(i4,50);
		final ItemStack i5 = new ItemStack(Material.IRON_SWORD);
		i5.addEnchantment(Enchantment.FIRE_ASPECT,1);
		i5.addEnchantment(Enchantment.DAMAGE_ALL,5);
		final GuildShopItem gs5 = new GuildShopItem(i5,50);

		list.add(gs1);
		list.add(gs2);
		list.add(gs3);
		list.add(gs4);
		list.add(gs5);
		return list;
	}
}