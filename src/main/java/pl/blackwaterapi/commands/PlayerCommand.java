package pl.blackwaterapi.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.blackwaterapi.utils.Util;

public abstract class PlayerCommand extends Command
{
    public PlayerCommand(String name, String desc, String usage, String permission, String... aliases) {
        super(name, desc, usage, permission, aliases);
    }

    @Override
    public boolean onExecute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            return Util.sendMsg(sender, "&cYou must be a player to run that command!");
        }
        return this.onCommand((Player)sender, args);
    }

    public abstract boolean onCommand(Player p0, String[] p1);
}
