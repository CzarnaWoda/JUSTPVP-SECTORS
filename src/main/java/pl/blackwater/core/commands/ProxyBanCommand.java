package pl.blackwater.core.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.blackwater.core.data.User;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.UserManager;
import pl.blackwaterapi.commands.Command;
import pl.blackwaterapi.commands.PlayerCommand;
import pl.blackwaterapi.utils.GsonUtil;
import pl.blackwaterapi.utils.Util;
import pl.blackwaterapi.utils.objects.Ban;
import pl.justsectors.packets.impl.bans.BanAddPacket;
import pl.justsectors.redis.channels.RedisChannel;
import pl.justsectors.redis.client.RedisClient;

public class ProxyBanCommand extends Command implements Colors {

    public ProxyBanCommand() {
        super("proxyban", "Banuje uzytkownika", "/proxyban", "core.ban");
    }

    @Override
    public boolean onExecute(CommandSender sender, String[] args) {
        if(args.length != 2)
        {
            return Util.sendMsg(sender, "&4Poprawne uzycie: &c/ban <nick> <reason>");
        }
        final User u = UserManager.getUser(args[0]);
        if(u == null)
        {
            return Util.sendMsg(sender, "&cTakiego gracza nie bylo nigdy na serwerze!");
        }
        RedisClient.sendProxiesPacket(new BanAddPacket(u.getUuid(), args[1], (sender instanceof Player ? sender.getName() : "CONSOLE"), 0L));
        RedisChannel.INSTANCE.BANS.put(u.getUuid(), GsonUtil.toJson(new Ban(u.getUuid(), args[1], (sender instanceof Player ? sender.getName() : "CONSOLE"), System.currentTimeMillis(), 0L)));
        return true;
    }

}
