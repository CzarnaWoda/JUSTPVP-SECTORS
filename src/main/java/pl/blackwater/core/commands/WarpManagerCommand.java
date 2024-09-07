package pl.blackwater.core.commands;

import org.bukkit.entity.Player;

import pl.blackwater.core.data.Warp;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.WarpManager;
import pl.blackwater.core.settings.MessageConfig;
import pl.blackwaterapi.commands.PlayerCommand;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.impl.warps.WarpDeletePacket;
import pl.justsectors.packets.impl.warps.WarpRegisterPacket;
import pl.justsectors.redis.client.RedisClient;

public class WarpManagerCommand extends PlayerCommand implements Colors{

	public WarpManagerCommand() {
		super("warpmanager", "ustaw warp/usun warp", "/warpmanager [create/delete] [nazwa] [pex]", "core.warpmanager", "setwarp","delwarp");
	}

	@Override
	public boolean onCommand(Player p, String[] args) {
		if(args.length < 2)
			return Util.sendMsg(p, Util.fixColor(Util.replaceString(MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}", getUsage()))));
		if(args[0].equalsIgnoreCase("create")) {
			if(args.length == 3) {
				final Warp w = WarpManager.getWarp(args[1]);
				if(w == null) {
					final Warp warp = new Warp(args[1], p.getLocation(), args[2]);
					final RedisPacket packet = new WarpRegisterPacket(p.getUniqueId(), warp);
					RedisClient.sendSectorsPacket(packet);
				}else
					return Util.sendMsg(p, Util.fixColor(Util.replaceString(WarningColor + "Blad: " + WarningColor_2 + "Taki warp juz istnieje!")));
			}else
				return Util.sendMsg(p, Util.fixColor(Util.replaceString(MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}", getUsage()))));
		}else
			if(args[0].equalsIgnoreCase("delete")) {
				if(args.length == 2) {
					final Warp w = WarpManager.getWarp(args[1]);
					if(w != null) {
						w.delete();
						final RedisPacket packet = new WarpDeletePacket(w.getName(), p.getUniqueId());
						RedisClient.sendSectorsPacket(packet);
					}else
						return Util.sendMsg(p, Util.fixColor(Util.replaceString(WarningColor + "Blad: " + WarningColor_2 + "Taki warp nie istnieje!")));
				}else
					return Util.sendMsg(p, Util.fixColor(Util.replaceString(MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}", getUsage()))));
			}
		return false;
	}

}
