package pl.blackwater.core.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import pl.blackwater.core.Core;
import pl.blackwater.core.commands.AdminPanelCommand;
import pl.blackwater.core.commands.BackupCommand;
import pl.blackwater.core.data.Rank;
import pl.blackwater.core.data.User;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.interfaces.SpecialCharacters;
import pl.blackwater.core.inventories.AdminPanelInventory;
import pl.blackwater.core.inventories.BackupInventory;
import pl.blackwater.core.inventories.ChatInventory;
import pl.blackwater.core.managers.ChatManager;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.core.settings.CoreConfig;
import pl.blackwater.core.settings.MessageConfig;
import pl.blackwater.core.utils.StringUtil;
import pl.blackwater.guilds.data.Guild;
import pl.blackwater.guilds.managers.GuildManager;
import pl.blackwater.guilds.settings.GuildConfig;
import pl.blackwaterapi.configs.ConfigManager;
import pl.blackwaterapi.utils.ActionBarUtil;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.impl.chat.ChatMessagePacket;
import pl.justsectors.packets.impl.config.LoadConfigPacket;
import pl.justsectors.packets.impl.config.SetConfigLongPacket;
import pl.justsectors.redis.client.RedisClient;

import java.util.regex.Pattern;

public class AsyncPlayerChatListener implements Listener,Colors,SpecialCharacters{
	private static final Pattern URL_PATTERN = Pattern.compile("((?:(?:https?)://)?[\\w-_.]{2,})\\.([a-zA-Z]{2,3}(?:/\\S+)?)");
	private static final Pattern IPPATTERN = Pattern.compile("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
	private static final Pattern BANNED_FLAMEWAR = Pattern.compile(".*(kurva|hui|gale|chuj|chuja|chujek|chuju|chujem|chujnia|chujowy|chujowa|chujowe|cipa|cipe|cipie|dojebac|dojebal|dojebala|dojebalem|dojebalam|dojebie|dopieprzac|dopierdalac|dopierdala|dopierdalal|dopierdalala|dopierdolil|dopierdole|dopierdoli|dopierdalajacy|dopierdolic|dupa|dupie|dupcia|dupeczka|dupy|dupe|huj|hujek|hujnia|huja|huje|hujem|huju|jebac|jebak|jebaka|jebal|jebany|jebane|jebanka|jebanko|jebankiem|jebanym|jebanej|jebana|jebani|jebanych|jebanymi|jebcie|jebiacy|jebiaca|jebiacego|jebiacej|jebia|jebie|jebliwy|jebnac|jebnal|jebna|jebnela|jebnie|jebnij|jebut|koorwa|korwa|kurestwo|kurew|kurewskiej|kurewska|kurewsko|kurewstwo|kurwa|kurwaa|kurwe|kurwie|kurwiska|kurwo|kurwy|kurwach|kurwami|kurewski|kurwiarz|kurwi\u0105cy|kurwica|kurwic|kurwido\u0142ek|kurwik|kurwiki|kurwiszcze|kurwiszon|kurwiszona|kurwiszonem|kurwiszony|kutas|kutasa|kutasie|kutasem|kutasy|kutasow|kutasach|kutasami|matkojebcy|matkojebca|matkojebcami|matkojebcach|najebac|najebal|najebane|najebany|najebana|najebie|najebia|naopierdalac|naopierdalal|naopierdalala|napierdalac|napierdalajacy|napierdolic|nawpierdalac|nawpierdalal|nawpierdalala|obsrywac|obsrywajacy|odpieprzac|odpieprzy|odpieprzyl|odpieprzyla|odpierdalac|odpierdol|odpierdolil|odpierdolila|odpierdalajacy|odpierdalajaca|odpierdolic|odpierdoli|opieprzaj\u0105cy|opierdalac|opierdala|opierdalajacy|opierdol|opierdolic|opierdoli|opierdola|piczka|pieprzniety|pieprzony|pierdel|pierdlu|pierdolacy|pierdolaca|pierdol|pierdole|pierdolenie|pierdoleniem|pierdoleniu|pierdolec|pierdola|pierdolicie|pierdolic|pierdolil|pierdolila|pierdoli|pierdolniety|pierdolisz|pierdolnac|pierdolnal|pierdolnela|pierdolnie|pierdolnij|pierdolnik|pierdolona|pierdolone|pierdolony|pierdz\u0105cy|pierdziec|pizda|pizde|pizdzie|pizdnac|pizdu|podpierdalac|podpierdala|podpierdalajacypodpierdolic|podpierdoli|pojeb|pojeba|pojebami|pojebanego|pojebanemu|pojebani|pojebany|pojebanych|pojebanym|pojebanymi|pojebem|pojebac|pojebalo|popierdala|popierdalac|popierdolic|popierdoli|popierdolonego|popierdolonemu|popierdolonym|popierdolone|popierdoleni|popierdolony|porozpierdala|porozpierdalac|poruchac|przejebane|przejebac|przyjebali|przepierdalac|przepierdala|przepierdalajacy|przepierdalajaca|przepierdolic|przyjebac|przyjebie|przyjebala|przyjebal|przypieprzac|przypieprzajacy|przypieprzajaca|przypierdalac|przypierdala|przypierdoli|przypierdalajacy|przypierdolic|qrwa|rozjebac|rozjebie|rozjeba\u0142a|rozpierdalac|rozpierdala|rozpierdolic|rozpierdole|rozpierdoli|rozpierducha|skurwiel|skurwiela|skurwielem|skurwielu|skurwysyn|skurwysynow|skurwysyna|skurwysynem|skurwysynu|skurwysyny|skurwysynski|skurwysynstwo|spieprzac|spieprza|spieprzaj|spieprzajcie|spieprzaja|spieprzajacy|spieprzajaca|spierdalac|spierdala|spierdalal|spierdalalcie|spierdalala|spierdalajacy|spierdolic|spierdoli|spierdol\u0105|spierdola|srac|srajacy|srajac|sraj|sukinsyn|sukinsyny|sukinsynom|sukinsynowi|sukinsynow|ujebac|ujebal|ujebana|ujebany|ujebie|ujeba\u0142a|ujebala|upierdalac|upierdala|upierdolic|upierdoli|upierdola|upierdoleni|wjebac|wjebie|wjebia|wjebiemy|wjebiecie|wkurwiac|wkurwial|wkurwiajacy|wkurwiajaca|wkurwi|wkurwiali|wkurwimy|wkurwicie|wkurwic|wpierdalac|wpierdalajacy|wpierdol|wpierdolic|wpizdu|wyjebali|wyjebac|wyjebia|wyjebiesz|wyjebie|wyjebiecie|wyjebiemy|wypieprzac|wypieprza|wypieprzal|wypieprzala|wypieprzy|wypieprzyla|wypieprzyl|wypierdal|wypierdalac|wypierdala|wypierdalaj|wypierdalal|wypierdalala|wypierdolic|wypierdoli|wypierdolimy|wypierdolicie|wypierdola|wypierdolili|wypierdolil|wypierdolila|zajebac|zajebie|zajebia|zajebial|zajebiala|zajebali|zajebana|zajebani|zajebane|zajebany|zajebanych|zajebanym|zajebanymi|zajebiste|zajebisty|zajebistych|zajebista|zajebistym|zajebistymi|zajebiscie|zapieprzyc|zapieprzy|zapieprzyl|zapieprzyla|zapieprza|zapieprz|zapieprzymy|zapieprzycie|zapieprzysz|zapierdala|zapierdalac|zapierdalaja|zapierdalaj|zapierdalajcie|zapierdalala|zapierdalali|zapierdalajacy|zapierdolic|zapierdoli|zapierdolil|zapierdolila|zapierdola|zapierniczac|zapierniczajacy|zasrac|zasranym|zasrywajacy|zesrywac|zesrywajac|zjebac|zjebal|zjebala|zjebana|zjebia|zjebali|zjeby).*");
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onAsyncPlayerChat(AsyncPlayerChatEvent e) {
		if(e.isCancelled())
			return;
		if(e.getMessage().contains("%"))
		{
			e.setCancelled(true);
			return;
		}
		final ChatManager manager = Core.getChatManager();
		Player p = e.getPlayer();
		if(!manager.isChat() && !p.hasPermission("core.chat.lock.bypass")) {
			Util.sendMsg(p, Util.fixColor(WarningColor + "Chat na serwerze jest aktualnie " + WarningColor_2 + "wylaczony"));
			e.setCancelled(true);
			return;
		}
		if(manager.isVipChat() && !p.hasPermission("core.chat.vip")) {
			Util.sendMsg(p, Util.fixColor(WarningColor + "Chat na serwerze jest aktualnie dostepny tylko dla rang " + WarningColor_2 + "PREMIUM"));
			e.setCancelled(true);
			return;
		}
		if(ChatInventory.getChangeKillsLimit().contains(p)) {
			if(e.getMessage().equalsIgnoreCase("cancel")) {
				ChatInventory.getChangeKillsLimit().remove(p);
				e.setCancelled(true);
				ActionBarUtil.sendActionBar(p, Util.fixColor(Util.replaceString(WarningColor + "Anulowano ustawienie nowego limitu " + WarningColor_2 + smile_3)));
				return;
			}else
			if(Util.isInteger(e.getMessage())) {
				ChatInventory.getChangeKillsLimit().remove(p);
				final int i = Integer.parseInt(e.getMessage());
				final CoreConfig config = new CoreConfig();
				CoreConfig.CHATMANAGER_KILLS = i;
				config.setField("chatmanager.kills", i);
				ChatInventory.openMenu(p);
				Bukkit.broadcastMessage(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Administrator " + ImportantColor + p.getName() + MainColor + " ustawil nowy limit zabojstw do pisania na chacie " + SpecialSigns + "(" + WarningColor + i + SpecialSigns + ")")));
				e.setCancelled(true);
				return;
			}
		}
		if(BackupCommand.getFindPlayer().contains(p)){
			e.setCancelled(true);
			BackupCommand.getFindPlayer().remove(p);
			if(e.getMessage().equalsIgnoreCase("cancel")) {
				ActionBarUtil.sendActionBar(p, Util.fixColor(Util.replaceString(WarningColor + "Anulowano szukanie gracza")));
			}else{
				Player o = Bukkit.getPlayer(e.getMessage());
				if(o == null){
					ActionBarUtil.sendActionBar(p, Util.fixColor(Util.replaceString(MessageConfig.MESSAGE_COMMAND_UNKNOWNPLAYER)));
				}else{
					BackupInventory.openPlayerMenu(p, o);
				}
			}
			return;
		}
		if(AdminPanelCommand.getAdmin_changeTIMED().contains(p)) {
			if(e.getMessage().equalsIgnoreCase("cancel")) {
				AdminPanelCommand.getAdmin_changeTIMED().remove(p);
				e.setCancelled(true);
				ActionBarUtil.sendActionBar(p, Util.fixColor(Util.replaceString(WarningColor + "Anulowano ustawienie nowego czasu " + WarningColor_2 + smile_3)));
			}else {
				AdminPanelCommand.getAdmin_changeTIMED().remove(p);
				final long time = Util.parseDateDiff(e.getMessage(), true);
				CoreConfig.CREATEMANAGER_DIAMOND_TIME = time;
				ConfigManager.getConfig("coreconfig.yml").setField("createmanager.diamond.time", time);

				final SetConfigLongPacket packet = new SetConfigLongPacket("coreconfig.yml",time,"createmanager.diamond.time", true, p.getDisplayName());
				RedisClient.sendSectorsPacket(packet);
				final LoadConfigPacket packet1 = new LoadConfigPacket("coreconfig.yml");
				RedisClient.sendSectorsPacket(packet1);
				e.setCancelled(true);
			}
			return;
		}
		if(AdminPanelCommand.getAdmin_changeTIMEE().contains(p)) {
			if(e.getMessage().equalsIgnoreCase("cancel")) {
				AdminPanelCommand.getAdmin_changeTIMEE().remove(p);
				e.setCancelled(true);
				ActionBarUtil.sendActionBar(p, Util.fixColor(Util.replaceString(WarningColor + "Anulowano ustawienie nowego czasu " + WarningColor_2 + smile_3)));
			}else {
				AdminPanelCommand.getAdmin_changeTIMEE().remove(p);
				final long time = Util.parseDateDiff(e.getMessage(), true);
				CoreConfig.CREATEMANAGER_ENCHANT_TIME = time;
				ConfigManager.getConfig("coreconfig.yml").setField("createmanager.enchant.time", time);
				final SetConfigLongPacket packet = new SetConfigLongPacket("coreconfig.yml",time,"createmanager.enchant.time", true, p.getDisplayName());
				RedisClient.sendSectorsPacket(packet);
				final LoadConfigPacket packet1 = new LoadConfigPacket("coreconfig.yml");
				RedisClient.sendSectorsPacket(packet1);
				e.setCancelled(true);
			}
			return;
		}
		if(AdminPanelCommand.getAdmin_changeTIMEG().contains(p)) {
			if(e.getMessage().equalsIgnoreCase("cancel")) {
				AdminPanelCommand.getAdmin_changeTIMEG().remove(p);
				e.setCancelled(true);
				ActionBarUtil.sendActionBar(p, Util.fixColor(Util.replaceString(WarningColor + "Anulowano ustawienie nowego czasu " + WarningColor_2 + smile_3)));
			}else {
				AdminPanelCommand.getAdmin_changeTIMEG().remove(p);
				final long time = Util.parseDateDiff(e.getMessage(), true);
				GuildConfig.setGuildCreate(time);
				final SetConfigLongPacket packet = new SetConfigLongPacket("guildconfig.yml",time,"guilds.create.time", true, p.getDisplayName());
				RedisClient.sendSectorsPacket(packet);
				final LoadConfigPacket packet1 = new LoadConfigPacket("guildconfig.yml");
				RedisClient.sendSectorsPacket(packet1);

				e.setCancelled(true);
			}
			return;
		}
		User u = UserManager.getUser(p);
		if(u.getKills() < CoreConfig.CHATMANAGER_KILLS && !p.hasPermission("core.chat.kills.bypass")) {
			Util.sendMsg(p, Util.fixColor(WarningColor + "Blad: " + WarningColor_2 + "Nie posiadasz wystarczajacej liczby zabojstw aby pisac na chacie " + SpecialSigns + "(" + WarningColor + CoreConfig.CHATMANAGER_KILLS + SpecialSigns + ")"));
			e.setCancelled(true);
			return;
		}
		if(!p.hasPermission("core.chat.bypass") && (URL_PATTERN.matcher(e.getMessage()).find() || IPPATTERN.matcher(e.getMessage()).find())) {
			Util.sendMsg(p, Util.fixColor(WarningColor + "Blad: " + WarningColor_2 + "Twoja wiadomosc zawiera niedozwolone tresci! " + SpecialSigns + "(" + WarningColor + "REKLAMA" + SpecialSigns + ")"));
			e.setCancelled(true);
			for(final Player admin : Bukkit.getOnlinePlayers()) {
				if(AdminPanelCommand.getAR_ADMINS().contains(admin)) {
					Util.SendRun_CommandTextComponent(admin, Util.replaceString(SpecialSigns + "->>| " + MainColor + "&9&lWykryto REKLAME!"), "/tp " + p.getName(), ImportantColor + e.getMessage());
					Util.sendMsg(p, "");
					Util.SendRun_CommandTextComponent(admin, Util.replaceString(SpecialSigns + "   ->> " + WarningColor + "Zbanuj za REKLAME!"), "/ban " + p.getName() + " REKLAMA", ImportantColor + e.getMessage());
					Util.SendRun_CommandTextComponent(admin, Util.replaceString(SpecialSigns + "   ->> " + WarningColor + "Wyrzuc za REKLAME!"), "/kick " + p.getName() + " REKLAMA", ImportantColor + e.getMessage());
				}
			}
			return;
		}
		if(!p.hasPermission("core.chat.bypass") && (BANNED_FLAMEWAR.matcher(e.getMessage()).find())) {
			Util.sendMsg(p, Util.fixColor(WarningColor + "Blad: " + WarningColor_2 + "Twoja wiadomosc zawiera niedozwolone tresci! " + SpecialSigns + "(" + WarningColor + "OBRAZA" + SpecialSigns + ")"));
			e.setCancelled(true);
			for(final Player admin : Bukkit.getOnlinePlayers()) {
				if(AdminPanelCommand.getAR_ADMINS().contains(admin)) {
					Util.SendRun_CommandTextComponent(admin, Util.replaceString(SpecialSigns + "->>| " + MainColor + "&9&lWykryto OBRAZE!" + SpecialSigns), "/tp " + p.getName(), ImportantColor + e.getMessage());
					Util.SendRun_CommandTextComponent(admin, Util.replaceString(SpecialSigns + "   ->> " + WarningColor + "Zbanuj za OBRAZE!" + SpecialSigns), "/ban " + p.getName() + " OBRAZA", ImportantColor + e.getMessage());
					Util.SendRun_CommandTextComponent(admin, Util.replaceString(SpecialSigns + "   ->> " + WarningColor + "Wyrzuc za OBRAZE!" + SpecialSigns), "/kick " + p.getName() + " OBRAZA", ImportantColor + e.getMessage());
				}
			}
			return;
		}
		if(!manager.canSendMessage(p)) {
			final int elapsed = (int) (CoreConfig.CHATMANAGER_SLOWMODE - ((System.currentTimeMillis() - manager.getTimes().get(p.getUniqueId())) / 1000L));
			Util.sendMsg(p, Util.fixColor(WarningColor + "Blad: " + WarningColor_2 + "Na chacie bedziesz mogl napisac dopiero za: " + elapsed + "s"));
			e.setCancelled(true);
			return;
		}
		final Guild g = GuildManager.getGuild(p);
		Rank rank = Core.getRankManager().getRank(u.getRank());
		String globalFormat = Util.fixColor(CoreConfig.CHATMANAGER_FORMAT_GLOBAL
				.replace("{KILLS}", String.valueOf(u.getKills()))
				.replace("{TAG}", (g != null ? CoreConfig.CHATMANAGER_FORMAT_GUILD.replace("{TAG}", g.getTag()) : ""))
				.replace("{PREFIX}", rank.getPrefix())
				.replace("{SUFFIX}", rank.getSuffix())
				.replace("{PLAYER}", (!p.hasPermission("core.mvip") ? p.getDisplayName() : StringUtil.coloredString(p.getDisplayName())))
				.replace("{MESSAGE}", e.getMessage().replaceAll("&","")));
		String aglobalFormat = Util.fixColor(CoreConfig.CHATMANAGER_FORMAT_AGLOBAL
				.replace("{KILLS}", String.valueOf(u.getKills()))
				.replace("{TAG}", (g != null ? CoreConfig.CHATMANAGER_FORMAT_GUILD.replace("{TAG}", g.getTag()) : ""))
				.replace("{PREFIX}", rank.getPrefix())
				.replace("{SUFFIX}", rank.getSuffix())
				.replace("{PLAYER}", p.getDisplayName())
				.replace("{MESSAGE}", e.getMessage().replaceAll("&","")));
		if(p.hasPermission("core.chat.nopoints")) {
			e.setFormat(aglobalFormat);
			final ChatMessagePacket packet = new ChatMessagePacket(aglobalFormat);
			RedisClient.sendSectorsPacket(packet);
			e.setCancelled(true);
		}
		else {
			e.setFormat(globalFormat);
			final ChatMessagePacket packet = new ChatMessagePacket(globalFormat);
			RedisClient.sendSectorsPacket(packet);
			e.setCancelled(true);
		}
		manager.getTimes().put(p.getUniqueId(), System.currentTimeMillis());
	}
}