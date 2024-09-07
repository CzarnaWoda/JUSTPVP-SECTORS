package pl.blackwater.guilds.commands.admin;


import net.lightshard.itemcases.ItemCases;
import net.lightshard.itemcases.cases.ItemCase;
import org.bukkit.entity.Player;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.settings.MessageConfig;
import pl.blackwater.guilds.data.Guild;
import pl.blackwater.guilds.managers.GuildManager;
import pl.blackwaterapi.commands.PlayerCommand;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.packets.impl.guild.GuildSetSoulsPacket;
import pl.justsectors.redis.client.RedisClient;

public class SetCommand extends PlayerCommand implements Colors {
    public SetCommand() {
        super("set", "Ustawia wartosc dla danej gildii", "/ga set [gildia] [soul] [value]", "guild.admin", new String[0]);
    }
    @Override
    public boolean onCommand(Player sender, String[] args) {
        if(args.length != 3){
            return Util.sendMsg(sender, Util.replaceString(MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}", getUsage())));
        }
        final Guild guild = GuildManager.getGuild(args[0]);
        if(guild == null){
            return Util.sendMsg(sender, WarningColor + "Blad: " + WarningColor_2 + "Taka gildia nie istnieje!");
        }
        if(!Util.isInteger(args[2])){
            return Util.sendMsg(sender, WarningColor + "Blad: " + WarningColor_2 + "Podana wartosc nie jest liczba!");
        }
        final int value = Integer.parseInt(args[2]);
        final String message = MainColor + "Ustawiono na " + ImportantColor +  UnderLined + value + MainColor + " do wartosci " + ImportantColor + UnderLined + args[1].toUpperCase() + MainColor + " dla gildii " + ImportantColor + BOLD + guild.getTag();
        switch (args[1]){
            case "soul":
                final GuildSetSoulsPacket packet = new GuildSetSoulsPacket(guild.getTag(),value);
                RedisClient.sendSectorsPacket(packet);
                Util.sendMsg(sender, message);
                break;
            default:
                Util.sendMsg(sender, Util.replaceString(MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}", getUsage())));
                break;
        }
        return false;
    }
}
