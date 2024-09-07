package pl.blackwater.core.managers;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

import lombok.Getter;
import pl.blackwater.core.data.ChatControlUser;
import pl.blackwater.core.data.User;
import pl.blackwater.core.enums.ChatControlType;
import pl.blackwaterapi.utils.Logger;
import pl.blackwaterapi.utils.Util;

public class ChatControlUserManager {
	@Getter public static HashMap<UUID, ChatControlUser> chatcontrolusers = new HashMap<>();
	
	private static ChatControlUser CreateUser(final User u){
		final ChatControlUser cc = new ChatControlUser(u, u.getChatControl());
		chatcontrolusers.put(u.getUuid(), cc);
		return cc;
	}
	public static boolean getStatus(ChatControlUser cc, ChatControlType type){
		switch (type){
			case DEATH:
				return cc.isDeathMessage();
			case KILLKEY:
				return cc.isKillKeyMessage();
			case CASEOPEN:
				return cc.isCaseOpenMessage();
			case ITEMSHOP:
				return cc.isItemShopMessage();
			case KILLTURBO:
				return cc.isKillTurboMoneyMessage();
			case HEADHUNTER:
				return cc.isHeadHunterGlobalMessage();
			case AUTOMESSAGE:
				return cc.isAutoMessage();
			case KILLDIAMOND:
				return cc.isKillDiamondMessage();
			case MARKETGLOBAL:
				return cc.isMarketGlobalMessage();
			case KILLKILLSTREAK:
				return cc.isKillKillStreakMessage();
			case KILLMONEYLEVEL:
				return cc.isKillMoneyLevelMessage();
			case KILLSTREAKGLOBAL:
				return cc.isKillStreakGlobalMessage();
			default:
				return true;
		}
	}
	public static ChatControlUser getUser(final UUID u){
		ChatControlUser cc = chatcontrolusers.get(u);
		if(cc == null){
			cc = CreateUser(UserManager.getUser(u));
		}
		return cc;
	}
	public static ChatControlUser getUser(final User u){
		ChatControlUser cc = chatcontrolusers.get(u.getUuid());
		if(cc == null){
			cc = CreateUser(u);
		}
		return cc;
	}
	public static ChatControlUser getUser(final Player p){
		ChatControlUser cc = chatcontrolusers.get(p.getUniqueId());
		if(cc == null){
			cc = CreateUser(UserManager.getUser(p));
		}
		return cc;
	}
	public static void joinToGame(final User u){
		if(u == null){
			Logger.fixColorSend("&4Error - ChatControlUser can't be create because User is null");
			return;
		}
		if(chatcontrolusers.get(u.getUuid()) == null){
		CreateUser(u);
		}
	}
	public static void joinToGame(final Player p){
		joinToGame(UserManager.getUser(p));
	}
	public static boolean sendMsg(final Player p,final String s,final boolean b){
		if(b){
			Util.sendMsg(p, s);
			return true;
		}
		return false;
	}
	}
