package pl.blackwater.guilds.commands.user;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.settings.MessageConfig;
import pl.blackwater.guilds.data.Guild;
import pl.blackwater.guilds.managers.AllianceManager;
import pl.blackwater.guilds.managers.GuildManager;
import pl.blackwater.guilds.scoreboard.ScoreBoardNameTag;
import pl.blackwaterapi.commands.PlayerCommand;
import pl.blackwaterapi.utils.Util;

public class AllianceCommand extends PlayerCommand implements Colors {

    public AllianceCommand() {
        super("sojusz", "Zarzadzanie sojuszami gildii", "/g sojusz <gildia>", "guild.gracz", "alliance");
    }

    @Override
    public boolean onCommand(Player player, String[] args) {
        if(args.length != 1){
            return Util.sendMsg(player, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}" , getUsage()));
        }
        Guild g = GuildManager.getGuild(player);
        if(g == null){
            return Util.sendMsg(player, WarningColor + "Blad: " + WarningColor_2 + "Nie posiadasz gildii!");
        }
        if(!g.isOwner(player.getUniqueId())){
            return Util.sendMsg(player, WarningColor + "Blad: " + WarningColor_2 + "Nie jestes zalozycielem gildii!");
        }
        Guild o = GuildManager.getGuild(args[0]);
        if(o == null){
            return Util.sendMsg(player, WarningColor + "Blad: " + WarningColor_2 + "Taka gildia nie istnieje!");
        }
        if(g.equals(o)){
            return Util.sendMsg(player, WarningColor + "Blad: " + WarningColor_2 + "Nie mozesz zaprosic tej gildii do sojuszu!");
        }
        if(AllianceManager.hasAlliance(g,o)){
            ScoreBoardNameTag.updateBoard(g);
            ScoreBoardNameTag.updateBoard(o);
            AllianceManager.removeAlliance(g,o);
            Bukkit.broadcastMessage(Util.fixColor(Util.replaceString(SpecialSigns + "|->> " + MainColor + "Gildia " + SpecialSigns + "[" + ImportantColor + g.getTag() + SpecialSigns + "]" + ImportantColor + g.getName() + MainColor + " zerwala sojusz z gildia " + SpecialSigns + "[" + ImportantColor + o.getTag() + SpecialSigns + "]" + ImportantColor + o.getName())));
            return true;
        }
        if(AllianceManager.getGuildAlliances(g).size() >= 2){
            return Util.sendMsg(player, WarningColor + "Blad: " + WarningColor_2 + "Twoja gildia posiada maksymalna ilosc sojuszy!");
        }
        if(AllianceManager.getInvites().contains(o.getTag() + ":" + g.getTag())){
            AllianceManager.getInvites().remove(o.getTag() + ":" + g.getTag());
            AllianceManager.createAlliance(g,o);
            ScoreBoardNameTag.updateBoard(g);
            ScoreBoardNameTag.updateBoard(o);
            Bukkit.broadcastMessage(Util.fixColor(Util.replaceString(SpecialSigns + "|->> " + MainColor + "Gildia " + SpecialSigns + "[" + ImportantColor + g.getTag() + SpecialSigns + "]" + ImportantColor + g.getName() + MainColor + " zawarla sojusz z gildia " + SpecialSigns + "[" + ImportantColor + o.getTag() + SpecialSigns + "]" + ImportantColor + o.getName())));
            return true;
        }
        OfflinePlayer owner = Bukkit.getOfflinePlayer(o.getOwner());
        if(!owner.isOnline()){
            return Util.sendMsg(player, WarningColor + "Blad: " + WarningColor_2 + "Zalozyciel przeciwnej gildii nie jest online!");
        }
        AllianceManager.getInvites().add(g.getTag() + ":" + o.getTag());
        Util.sendMsg(owner.getPlayer(), MainColor + "Twoja gildia zostala zaproszona do sojuszu przez gildie " + SpecialSigns + "[" + ImportantColor + g.getTag() + SpecialSigns + "]" + ImportantColor + g.getName() + MainColor + "! Zaakceptuj sojusz: /g sojusz " + g.getTag());
        return Util.sendMsg(player,MainColor + "Zaprosiles gildie " + SpecialSigns + "[" + ImportantColor + o.getTag() + SpecialSigns + "]" + ImportantColor + o.getName() + MainColor + " do sojuszu");
    }
}
