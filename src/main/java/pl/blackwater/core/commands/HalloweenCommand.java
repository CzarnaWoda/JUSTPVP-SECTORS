package pl.blackwater.core.commands;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.blackwater.core.Core;
import pl.blackwater.core.data.Rank;
import pl.blackwater.core.halloween.HalloweenManager;
import pl.blackwater.core.halloween.HalloweenPumpkin;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.settings.MessageConfig;
import pl.blackwaterapi.commands.Command;
import pl.blackwaterapi.utils.GsonUtil;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.impl.halloween.AddPumpkinPacket;
import pl.justsectors.packets.impl.ranks.RemovePermissionPacket;
import pl.justsectors.redis.channels.RedisChannel;
import pl.justsectors.redis.client.RedisClient;
import pl.supereasy.bpaddons.bossbar.BarColor;
import pl.supereasy.bpaddons.bossbar.BarStyle;
import pl.supereasy.bpaddons.bossbar.BlazingBossBar;
import pl.supereasy.bpaddons.bossbar.BossBarBuilder;

import java.util.UUID;

public class HalloweenCommand extends Command implements Colors {

    public HalloweenCommand() {
        super("halloween", "Zarzadza halloween", "/halloween add/remove/on/off/info <Optional<ID>>", "core.halloween");
    }

    @Override
    public boolean onExecute(CommandSender sender, String[] args) {
        if(args.length != 1){
            return Util.sendMsg(sender, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}", getUsage()));
        }
        final Player player = (Player) sender;
        switch (args[0]) {
            case "add":
                RedisChannel.INSTANCE.HALLOWEEN_LOCATIONS.add(GsonUtil.toJson(player.getLocation()));
                final RedisPacket packet = new AddPumpkinPacket(player.getLocation());
                RedisClient.sendSectorsPacket(packet);
                Util.sendMsg(player, "&7Poprawnie dodano resp dyni!");
                break;
            case "remove":
                break;
            case "on":
                for(Player p : Bukkit.getOnlinePlayers()) {
                    new BlazingBossBar(BossBarBuilder.add(UUID.randomUUID())
                            .style(BarStyle.SOLID)
                            .color(BarColor.BLUE)
                            .title(TextComponent.fromLegacyText(Util.fixColor(" &6&lHALLOWEEN &7-> EVENT zostal &aWLACZONY!")))
                            .buildPacket()
                    ).sendNotification(p, 5);
                }
                RedisChannel.INSTANCE.HALLOWEEN_BOOLEAN.set(0, true);
                Util.sendMsg(player, "&7Poprawnie wlaczono resp dyni!");
                break;
            case "off":
                for(Player p : Bukkit.getOnlinePlayers()) {
                    new BlazingBossBar(BossBarBuilder.add(UUID.randomUUID())
                            .style(BarStyle.SOLID)
                            .color(BarColor.BLUE)
                            .title(TextComponent.fromLegacyText(Util.fixColor(" &6&lHALLOWEEN &7-> EVENT zostal &cWYLACZONY!")))
                            .buildPacket()
                    ).sendNotification(p, 5);
                }
                RedisChannel.INSTANCE.HALLOWEEN_BOOLEAN.set(0, false);
                Util.sendMsg(player, "&7Poprawnie wylaczono resp dyni!");
                break;
        }
    return true;
    }
}