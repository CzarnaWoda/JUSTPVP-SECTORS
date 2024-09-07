package pl.blackwater.core.managers;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import lombok.Getter;
import pl.blackwater.core.data.EasyScoreboard;
import pl.blackwater.core.data.User;
import pl.blackwater.core.events.CustomEventManager;
import pl.blackwater.core.events.EventType;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwaterapi.utils.Util;

public class EasyScoreboardManager implements Colors{
    @Getter private static final HashMap<String, EasyScoreboard> scoreboards = new HashMap<>();

    public static EasyScoreboard getScoreboard(final Player player){
        return scoreboards.get(player.getName());
    }
    public static void removeScoreBoard(final Player player){
    	if(scoreboards.get(player.getName()) != null){
    	scoreboards.remove(player.getName());
		player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
    	}
    }
    public static void createScoreBoard(final Player p){
    	final User u = UserManager.getUser(p);
		if(CustomEventManager.getActiveEvent() != null){
			final EasyScoreboard sidebar = new EasyScoreboard(p, Util.fixColor(Util.replaceString("&8(>> &9&lEVENT &8<<)")));
			sidebar.addLine(Util.replaceString(SpecialSigns + "|->> " + ScoreBoard_ValueColor + "/sidebar " + SpecialSigns + " <<-|"));
			sidebar.addBlankLine();
			sidebar.addLine(Util.replaceString(" &8->> &d&n "  + CustomEventManager.getActiveEvent().getEventName() + " &8<<- " ));
			sidebar.addLine( Util.replaceString(" &8->> &d&n " + Util.getDate(CustomEventManager.getActiveEvent().getEventTime()) + "&8<<- "));
			sidebar.addBlankLine();
			for(int i = 0 ; i < 10 ; i ++){
				if(9-i >= CustomEventManager.getUuidEventList().size()){
					sidebar.addLine(Util.replaceString("&8 * &6" + (1 + 9-i) + " &8>> &cBRAK &8(&40 " + CustomEventManager.getActiveEvent().getEventType().getKeyVaule() + "&8)"));
				}else{
					final User user = UserManager.getUser(CustomEventManager.getUuidEventList().get(9-i));
					sidebar.addLine(Util.replaceString("&8 * &6" + (1 +9-i) + " &8>> &c" + user.getLastName() + " &8(&4 " + CustomEventManager.getStatCount().get(user.getUuid()) + " " + CustomEventManager.getActiveEvent().getEventType().getKeyVaule() + "&8)"));
				}
			}
		}else {
			final EasyScoreboard sidebar = new EasyScoreboard(p, Util.fixColor(Util.replaceString("&8(>> &9&lJustPvP.PL &8<<)")));
			sidebar.addLine(Util.replaceString(SpecialSigns + "|->> " + ScoreBoard_ValueColor + "/sidebar " + SpecialSigns + " <<-|"));
			sidebar.addBlankLine();
			sidebar.addLine(Util.replaceString(SpecialSigns + ">>->> " + ScoreBoard_MainColor + "Level: " + ScoreBoard_ValueColor + u.getLevel()));
			sidebar.addLine(Util.replaceString(SpecialSigns + ">>->> " + ScoreBoard_MainColor + "EXP: " + ScoreBoard_ValueColor + (int) u.getExp() + "/" + (int) u.getExpToLevel()));
			sidebar.addLine(Util.replaceString(SpecialSigns + ">>->> " + ScoreBoard_MainColor + "Kasa: " + ScoreBoard_ValueColor + u.getMoney()));
			sidebar.addLine(Util.replaceString(SpecialSigns + ">>->> " + ScoreBoard_MainColor + "KillStreak: " + ScoreBoard_ValueColor + u.getKillStreak()));
			sidebar.addLine(Util.replaceString(SpecialSigns + ">>->> " + ScoreBoard_MainColor + "Smierci: " + ScoreBoard_ValueColor +u.getDeaths()));
			sidebar.addLine(Util.replaceString(SpecialSigns + ">>->> " + ScoreBoard_MainColor + "Zabojstwa: " + ScoreBoard_ValueColor + u.getKills()));
			sidebar.addBlankLine();
		}
	}
    public static void updateScoreBoard(final Player p){
    	final EasyScoreboard sc = EasyScoreboardManager.getScoreboard(p);
		if(sc != null){
			int[] indexes = {14,13,12,11,10,9,8,7,6,5};
			if(CustomEventManager.getActiveEvent() != null){
				for(int i = 0 ; i < 10 ; i ++){
					if(i >= CustomEventManager.getUuidEventList().size()){
						sc.updateLine(indexes[i],Util.replaceString("&8 * &6" + (i+1) + " &8>> &cBRAK &8(&40 " + CustomEventManager.getActiveEvent().getEventType().getKeyVaule() + "&8) &5"));
					}else{
						final User user = UserManager.getUser(CustomEventManager.getUuidEventList().get(i));
						sc.updateLine(indexes[i],Util.replaceString("&8 * &6" + (i+1) + " &8>> &c" + user.getLastName() + " &8(&4" + CustomEventManager.getStatCount().get(user.getUuid()) + " " + CustomEventManager.getActiveEvent().getEventType().getKeyVaule() + "&8)"));
					}
				}
			}else {
				final User u = UserManager.getUser(p);
				sc.updateLine(2, Util.replaceString(SpecialSigns + ">>->> " + ScoreBoard_MainColor + "Level: " + ScoreBoard_ValueColor + +u.getLevel()));
				sc.updateLine(3, Util.replaceString(SpecialSigns + ">>->> " + ScoreBoard_MainColor + "EXP: " + ScoreBoard_ValueColor + +(int) u.getExp() + "/" + (int) u.getExpToLevel()));
				sc.updateLine(4, Util.replaceString(SpecialSigns + ">>->> " + ScoreBoard_MainColor + "Kasa: " + ScoreBoard_ValueColor + +u.getMoney()));
				sc.updateLine(5, Util.replaceString(SpecialSigns + ">>->> " + ScoreBoard_MainColor + "KillStreak: " + ScoreBoard_ValueColor + +u.getKillStreak()));
				sc.updateLine(6, Util.replaceString(SpecialSigns + ">>->> " + ScoreBoard_MainColor + "Smierci: " + ScoreBoard_ValueColor + +u.getDeaths()));
				sc.updateLine(7, Util.replaceString(SpecialSigns + ">>->> " + ScoreBoard_MainColor + "Zabojstwa: " + ScoreBoard_ValueColor + +u.getKills()));
			}
		}
	}
    

}