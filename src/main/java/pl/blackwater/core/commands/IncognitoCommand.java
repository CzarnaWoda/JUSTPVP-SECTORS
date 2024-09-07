package pl.blackwater.core.commands;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.blackwater.guilds.scoreboard.ScoreBoardNameTag;
import pl.blackwaterapi.commands.PlayerCommand;
import pl.blackwaterapi.incognitothreads.IncognitoActionType;
import pl.blackwaterapi.incognitothreads.IndependentThread;
import pl.blackwaterapi.objects.IncognitoUser;
import pl.blackwaterapi.utils.IncognitoUserUtil;

public class IncognitoCommand extends PlayerCommand {

    public IncognitoCommand() {
        super("incognito", "incognito", "/incognito", "core.incognito", "inc");
    }

    @Override
    public boolean onCommand(Player player, String[] args) {
        final IncognitoUser user = IncognitoUserUtil.get(player);
        final String name = "JUSTPVP.PL";
        if(user.getChangeNick().equalsIgnoreCase(name)){
            user.setChangeNick(user.getName());
            IndependentThread.action(IncognitoActionType.UPDATE_RESTART, player);
            player.sendMessage(ChatColor.RED + "Twoj nick zostal ustawiony na domyslny!");
        }else {
            user.setChangeNick(name);
            IndependentThread.action(IncognitoActionType.UPDATE_COMMAND, player, false);
            player.sendMessage(ChatColor.RED + "Twoj nick zostal zmieniony na: " + ChatColor.GREEN + name);
        }
        if(player.hasPermission("core.incognito.admin")){
            if(args.length == 2){
                if(args[0].equalsIgnoreCase("restart")){
                    final Player target = Bukkit.getPlayer(args[1]);
                    if(target != null){
                        IncognitoUserUtil.get(target).setChangeNick(target.getName());
                        IndependentThread.action(IncognitoActionType.UPDATE_RESTART, target);
                        player.sendMessage(ChatColor.RED + "Poprawnie resetowano nick gracz " + target.getName() + "!");
                    }
                }
            }
        }
        ScoreBoardNameTag.updateBoard(player);
        return false;
    }
}
