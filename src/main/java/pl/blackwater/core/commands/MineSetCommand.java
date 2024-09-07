package pl.blackwater.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import pl.blackwater.core.Core;
import pl.blackwater.core.data.Mine;
import pl.blackwater.core.managers.MineManager;
import pl.blackwater.core.utils.LocationUtil;
import pl.blackwater.guilds.data.Guild;
import pl.blackwater.guilds.data.Member;
import pl.blackwater.guilds.managers.GuildManager;
import pl.blackwater.guilds.managers.MemberManager;
import pl.blackwaterapi.commands.Command;
import pl.blackwaterapi.utils.Util;

import java.util.Objects;

public class MineSetCommand extends Command{

	public MineSetCommand() {
		super("mineset", "zarzadzaniem wartosciami kopalni", "/mineset <id kopalni> <name|type|cost|location|level|mainregion|stoneregion|stonelimit|guild> <wartosc/0 for location>", "core.mineset");
	}

	@Override
	public boolean onExecute(CommandSender p, String[] args) {
		if (args.length != 3)
        	return Util.sendMsg(p, "&6Prawidlowe uzycie: &6" + getUsage());

        Mine m = MineManager.getMine(Integer.parseInt(args[0]));
        String value = args[2];
        switch(args[1]) {
        	case "name":
            if(m == null)
                return Util.sendMsg(p, "&4Blad: &6Koplania o takim id nie istnieje!");
            m.setName(value);
            Util.sendMsg(p, "&7Ustawiono wartosc na: &6" + args[2].toLowerCase());
            break;
        	case "type":
                if(m == null)
                    return Util.sendMsg(p, "&4Blad: &6Koplania o takim id nie istnieje!");
                m.setType(value);
                Util.sendMsg(p, "&7Ustawiono wartosc na: &6" + args[2].toLowerCase());
                break;
            case "cost":
                if(m == null)
                    return Util.sendMsg(p, "&4Blad: &6Koplania o takim id nie istnieje!");
                //sprawdzenie czy jest integerem
                if (!Util.isInteger(args[2])) {
                	return Util.sendMsg(p, "&6Prawidlowe uzycie: &6" + getUsage());
                }
                //pamietaj o set wartosci !
                m.setCost(Integer.parseInt(args[2]));
            break;
            case "location":
                if(m == null)
                    return Util.sendMsg(p, "&4Blad: &6Koplania o takim id nie istnieje!");
                Player player = (Player)p;
                m.setLocation(player.getLocation());
                Util.sendMsg(p, "&7Ustawiono wartosc na: &6" + LocationUtil.convertLocationToString(player.getLocation()));
            break;
            case "level":
                if(m == null)
                    return Util.sendMsg(p, "&4Blad: &6Koplania o takim id nie istnieje!");
                if (!Util.isInteger(args[2])) {
                	return Util.sendMsg(p, "&6Prawidlowe uzycie: &6" + getUsage());
                }
                m.setLevel(Integer.parseInt(args[2]));
                Util.sendMsg(p, "&7Ustawiono wartosc na: &6" + args[2].toLowerCase());
            break;
            case "mainregion":
                if(m == null)
                    return Util.sendMsg(p, "&4Blad: &6Koplania o takim id nie istnieje!");
                m.setMainRegion(value);
                Util.sendMsg(p, "&7Ustawiono wartosc na: &6" + args[2].toLowerCase());
                break;
            case "stoneregion":
                if(m == null)
                    return Util.sendMsg(p, "&4Blad: &6Koplania o takim id nie istnieje!");
                m.setStoneRegion(value);
                Util.sendMsg(p, "&7Ustawiono wartosc na: &6" + args[2].toLowerCase());
                break;
            case "stonelimit":
                if(m == null)
                    return Util.sendMsg(p, "&4Blad: &6Koplania o takim id nie istnieje!");
                //sprawdzenie czy jest integerem
                if (!Util.isInteger(args[2])) {
                	return Util.sendMsg(p, "&6Prawidlowe uzycie: &6" + getUsage());
                }
                //pamietaj o set wartosci !
                m.setMineStoneLimit(Integer.parseInt(args[2]));
            break;
            case "guild":
                if(m == null)
                    return Util.sendMsg(p, "&4Blad: &6Koplania o takim id nie istnieje!");
            	Guild g = GuildManager.getGuild(args[2]);
            	if(g == null)
            		return Util.sendMsg(p, "&4Blad: &cGildia nie istnieje");
                if(!m.getGuild().equals("")){
                	ProtectedRegion mainRegion = m.getProtectedMainRegion();
                	ProtectedRegion stoneRegion = m.getProtectedStoneRegion();
                	mainRegion.getMembers().removeAll();
                	stoneRegion.getMembers().removeAll();
                }
            	ProtectedRegion mainRegion = m.getProtectedMainRegion();
            	ProtectedRegion stoneRegion = m.getProtectedStoneRegion();
            	for(Member member : g.getGuildMembers().values()){
            		LocalPlayer localplayer = Objects.requireNonNull(Core.getWorldGuard()).wrapOfflinePlayer(Bukkit.getOfflinePlayer(member.getUuid()));
            		mainRegion.getMembers().addPlayer(localplayer);
            		stoneRegion.getMembers().addPlayer(localplayer);
            	}
                m.setGuild(value);
            break;
            default:
            	return Util.sendMsg(p, "&6Prawidlowe uzycie: &6" + getUsage());
        }
        m.update(true);
        return Util.sendMsg(p, "&7Ustawiono wartosc: &6" + args[1].toLowerCase());
    }
}
