package pl.blackwater.core.commands;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import pl.blackwater.chestpvpdrop.settings.Config;
import pl.blackwater.core.data.User;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.EventManager;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.core.settings.MessageConfig;
import pl.blackwater.guilds.data.Guild;
import pl.blackwater.guilds.data.Member;
import pl.blackwater.guilds.managers.GuildManager;
import pl.blackwater.guilds.managers.MemberManager;
import pl.blackwaterapi.commands.Command;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.packets.impl.global.GlobalEventPacket;
import pl.justsectors.packets.impl.user.UserGiveEventPacket;
import pl.justsectors.packets.impl.user.UserRemoveEventPacket;
import pl.justsectors.redis.client.RedisClient;

public class EventCommand extends Command implements Colors{

	public EventCommand() {
		super("event", "zarzadzanie eventami na serwerze", "/event <turbomoney/turbodrop/turboexp> <gracz/gildia/graczremove> <gracz/gildia> [time] || /event <turbomoney/turbodrop/turboexp> <all> [time] || /event <drop/exp/infinitystone> <amount> [time]" , "core.event");
	}

	@Override
	public boolean onExecute(CommandSender sender, String[] args) {
		if(args.length < 3)
			return Util.sendMsg(sender, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}", getUsage()));
		if(args[0].equalsIgnoreCase("turbomoney")) {
			if(args[1].equalsIgnoreCase("all")) {
				final long eventtime = Util.parseDateDiff(args[2], true);
				final GlobalEventPacket packet = new GlobalEventPacket("turbomoney",eventtime,sender.getName(),"1");
				RedisClient.sendSectorsPacket(packet);
			}else if (args[1].equalsIgnoreCase("gracz")) {
				if(args.length == 4) {
					final User u = UserManager.getUser(args[2]);
					if(u == null) {
						return Util.sendMsg(sender, MessageConfig.MESSAGE_COMMAND_UNKNOWNPLAYER);
					}
					if(u.getEventTurboMoney() == 2)
						return Util.sendMsg(sender, Util.fixColor(Util.replaceString(WarningColor + "Blad: " + WarningColor_2 + "Ten gracz posiada juz turbomoney, wpisz /event turbomoney graczremove <gracz> aby anulowac event turbomoney!")));
					final long eventtime = Util.parseDateDiff(args[3], true);
					final UserGiveEventPacket packet = new UserGiveEventPacket(u.getLastName(),eventtime,"turbomoney");
					RedisClient.sendSectorsPacket(packet);
					return eventtime == 0L ? Util.sendMsg(sender, Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Dano graczowi " + ImportantColor + u.getLastName() + MainColor + " usluge TurboMoney na " + ImportantColor + "zawsze"))) : Util.sendMsg(sender, Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Dano graczowi " + ImportantColor + u.getLastName() + MainColor +  " usluge TurboMoney do dnia " + ImportantColor + Util.getDate(eventtime))));		
				}else
					return Util.sendMsg(sender, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}", getUsage()));
			}else if (args[1].equalsIgnoreCase("graczremove")) {
				final User u = UserManager.getUser(args[2]);
				if(u == null) {
					return Util.sendMsg(sender, MessageConfig.MESSAGE_COMMAND_UNKNOWNPLAYER);
				}
				if(u.getEventTurboMoney() == 1)
					return Util.sendMsg(sender, Util.fixColor(Util.replaceString(WarningColor + "Blad: " + WarningColor_2 + "Ten gracz nie posiada turbomoney, wpisz /event turbomoney gracz <gracz> [time] aby nadac event turbomoney!")));
				final UserRemoveEventPacket packet = new UserRemoveEventPacket(u.getLastName(),"turbomoney");
				RedisClient.sendSectorsPacket(packet);
				return Util.sendMsg(sender, Util.replaceString(SpecialSigns + ">> " + MainColor + "Zabrano usluge TurboMoney dla gracza " + ImportantColor + u.getLastName()));
			}else if (args[1].equalsIgnoreCase("gildia")) {
				if(args.length == 4) {
					final Guild g = GuildManager.getGuild(args[2]);
					if(g == null) {
						return Util.sendMsg(sender, Util.fixColor(WarningColor + "Blad: " + WarningColor_2 + "Gildia o takim tagu/nazwie nie istnieje!"));
					}
					final long eventtime = Util.parseDateDiff(args[3], true);
					for(Member m : g.getGuildMembers().values()) {
						final User u = UserManager.getUser(m.getUuid());
						final UserGiveEventPacket packet = new UserGiveEventPacket(u.getLastName(),eventtime,"turbomoney");
						RedisClient.sendSectorsPacket(packet);
					}
					return eventtime == 0L ? Util.sendMsg(sender, Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Dano gildii " + ImportantColor + g.getTag() + MainColor + " usluge TurboMoney na " + ImportantColor + "zawsze"))) : Util.sendMsg(sender, Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Dano gildii " + ImportantColor + g.getTag() + MainColor +  " usluge TurboMoney do dnia " + ImportantColor + Util.getDate(eventtime))));
				}
			}else
				return Util.sendMsg(sender, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}", getUsage()));
		}else if(args[0].equalsIgnoreCase("turbodrop")) {
			if(args[1].equalsIgnoreCase("all")) {
				final long eventtime = Util.parseDateDiff(args[2], true);
				final GlobalEventPacket packet = new GlobalEventPacket("turbodrop",eventtime,sender.getName(),"1");
				RedisClient.sendSectorsPacket(packet);
			}else if (args[1].equalsIgnoreCase("gracz")) {
				if(args.length == 4) {
					final User u = UserManager.getUser(args[2]);
					if(u == null) {
						return Util.sendMsg(sender, MessageConfig.MESSAGE_COMMAND_UNKNOWNPLAYER);
					}
					if(u.isTurboDrop())
						return Util.sendMsg(sender, Util.fixColor(Util.replaceString(WarningColor + "Blad: " + WarningColor_2 + "Ten gracz posiada juz turbodrop, wpisz /event turbodrop graczremove <gracz> aby anulowac event turbodrop!")));
					final long eventtime = Util.parseDateDiff(args[3], true);
					final UserGiveEventPacket packet = new UserGiveEventPacket(u.getLastName(),eventtime,"turbodrop");
					RedisClient.sendSectorsPacket(packet);
					return eventtime == 0L ? Util.sendMsg(sender, Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Dano graczowi " + ImportantColor + u.getLastName() + MainColor + " usluge TurboDrop na " + ImportantColor + "zawsze"))) : Util.sendMsg(sender, Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Dano graczowi " + ImportantColor + u.getLastName() + MainColor +  " usluge TurboDrop do dnia " + ImportantColor + Util.getDate(eventtime))));		
				}else
					return Util.sendMsg(sender, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}", getUsage()));
			}else if (args[1].equalsIgnoreCase("graczremove")) {
				final User u = UserManager.getUser(args[2]);
				if(u == null) {
					return Util.sendMsg(sender, MessageConfig.MESSAGE_COMMAND_UNKNOWNPLAYER);
				}
				if(!u.isTurboDrop())
					return Util.sendMsg(sender, Util.fixColor(Util.replaceString(WarningColor + "Blad: " + WarningColor_2 + "Ten gracz nie posiada turbodrop, wpisz /event turbodrop gracz <gracz> [time] aby nadac event turbodrop!")));
				final UserRemoveEventPacket packet = new UserRemoveEventPacket(u.getLastName(),"turbodrop");
				RedisClient.sendSectorsPacket(packet);
				return Util.sendMsg(sender, Util.replaceString(SpecialSigns + ">> " + MainColor + "Zabrano usluge TurboDrop dla gracza " + ImportantColor + u.getLastName()));
			}else if (args[1].equalsIgnoreCase("gildia")) {
				if(args.length == 4) {
					final Guild g = GuildManager.getGuild(args[2]);
					if(g == null) {
						return Util.sendMsg(sender, Util.fixColor(WarningColor + "Blad: " + WarningColor_2 + "Gildia o takim tagu/nazwie nie istnieje!"));
					}
					final long eventtime = Util.parseDateDiff(args[3], true);
					for(Member m : g.getGuildMembers().values()) {
						final User u = UserManager.getUser(m.getName());
						final UserGiveEventPacket packet = new UserGiveEventPacket(u.getLastName(),eventtime,"turbodrop");
						RedisClient.sendSectorsPacket(packet);
					}
					assert g != null;
					return eventtime == 0L ? Util.sendMsg(sender, Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Dano gildii " + ImportantColor + g.getTag() + MainColor + " usluge TurboDrop na " + ImportantColor + "zawsze"))) : Util.sendMsg(sender, Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Dano gildii " + ImportantColor + g.getTag() + MainColor +  " usluge TurboDrop do dnia " + ImportantColor + Util.getDate(eventtime))));
				}
			}else
				return Util.sendMsg(sender, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}", getUsage()));
		}else if(args[0].equalsIgnoreCase("turboexp")) {
			if(args[1].equalsIgnoreCase("all")) {
				final long eventtime = Util.parseDateDiff(args[2], true);
				final GlobalEventPacket packet = new GlobalEventPacket("turboexp",eventtime,sender.getName(),"1");
				RedisClient.sendSectorsPacket(packet);
			}else if (args[1].equalsIgnoreCase("gracz")) {
				if(args.length == 4) {
					final User u = UserManager.getUser(args[2]);
					if(u == null) {
						return Util.sendMsg(sender, MessageConfig.MESSAGE_COMMAND_UNKNOWNPLAYER);
					}
					if(u.isTurboExp())
						return Util.sendMsg(sender, Util.fixColor(Util.replaceString(WarningColor + "Blad: " + WarningColor_2 + "Ten gracz posiada juz turboEXP, wpisz /event turboexp graczremove <gracz> aby anulowac event turboEXP!")));
					final long eventtime = Util.parseDateDiff(args[3], true);
					final UserGiveEventPacket packet = new UserGiveEventPacket(u.getLastName(),eventtime,"turboexp");
					RedisClient.sendSectorsPacket(packet);
					return eventtime == 0L ? Util.sendMsg(sender, Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Dano graczowi " + ImportantColor + u.getLastName() + MainColor + " usluge TurboEXP na " + ImportantColor + "zawsze"))) : Util.sendMsg(sender, Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Dano graczowi " + ImportantColor + u.getLastName() + MainColor +  " usluge TurboEXP do dnia " + ImportantColor + Util.getDate(eventtime))));		
				}else
					return Util.sendMsg(sender, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}", getUsage()));
			}else if (args[1].equalsIgnoreCase("graczremove")) {
				final User u = UserManager.getUser(args[2]);
				if(u == null) {
					return Util.sendMsg(sender, MessageConfig.MESSAGE_COMMAND_UNKNOWNPLAYER);
				}
				if(!u.isTurboExp())
					return Util.sendMsg(sender, Util.fixColor(Util.replaceString(WarningColor + "Blad: " + WarningColor_2 + "Ten gracz nie posiada turboexp, wpisz /event turboexp gracz <gracz> [time] aby nadac event turboexp!")));
				final UserRemoveEventPacket packet = new UserRemoveEventPacket(u.getLastName(),"turboexp");
				RedisClient.sendSectorsPacket(packet);
				return Util.sendMsg(sender, Util.replaceString(SpecialSigns + ">> " + MainColor + "Zabrano usluge TurboEXP dla gracza " + ImportantColor + u.getLastName()));
			}else if (args[1].equalsIgnoreCase("gildia")) {
				if(args.length == 4) {
					final Guild g = GuildManager.getGuild(args[2]);
					if(g == null) {
						return Util.sendMsg(sender, Util.fixColor(WarningColor + "Blad: " + WarningColor_2 + "Gildia o takim tagu/nazwie nie istnieje!"));
					}
					final long eventtime = Util.parseDateDiff(args[3], true);
					for(Member m : g.getGuildMembers().values()) {
						final User u = UserManager.getUser(m.getName());
						final UserGiveEventPacket packet = new UserGiveEventPacket(u.getLastName(),eventtime,"turboexp");
						RedisClient.sendSectorsPacket(packet);
					}
					assert g != null;
					return eventtime == 0L ? Util.sendMsg(sender, Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Dano gildii " + ImportantColor + g.getTag() + MainColor + " usluge TurboEXP na " + ImportantColor + "zawsze"))) : Util.sendMsg(sender, Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Dano gildii " + ImportantColor + g.getTag() + MainColor +  " usluge TurboEXP do dnia " + ImportantColor + Util.getDate(eventtime))));
				}
			}else
				return Util.sendMsg(sender, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}", getUsage()));
		}else if(args[0].equalsIgnoreCase("drop")) {
			if(Util.isInteger(args[1])) {
				final long eventtime = Util.parseDateDiff(args[2], true);
				final GlobalEventPacket packet = new GlobalEventPacket("drop",eventtime,sender.getName(),args[1]);
				RedisClient.sendSectorsPacket(packet);
			}else
				return Util.sendMsg(sender, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}", getUsage()));
		}else if(args[0].equalsIgnoreCase("exp")) {
			if(Util.isInteger(args[1])) {
				final long eventtime = Util.parseDateDiff(args[2], true);
				final GlobalEventPacket packet = new GlobalEventPacket("drop",eventtime,sender.getName(),args[1]);
				RedisClient.sendSectorsPacket(packet);
			}else
				return Util.sendMsg(sender, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}", getUsage()));
		}else if(args[0].equalsIgnoreCase("infinitystone")) {
			if(Util.isInteger(args[1])) {
				final long eventtime = Util.parseDateDiff(args[2], true);
				final GlobalEventPacket packet = new GlobalEventPacket("infinitystone",eventtime,sender.getName(),args[1]);
				RedisClient.sendSectorsPacket(packet);
			}else
				return Util.sendMsg(sender, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}", getUsage()));
		}
		return false;
	}

}
