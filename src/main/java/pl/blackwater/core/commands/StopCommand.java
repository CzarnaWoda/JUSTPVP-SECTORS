package pl.blackwater.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import pl.blackwater.core.Core;
import pl.blackwater.core.data.Backup;
import pl.blackwater.core.data.User;
import pl.blackwater.core.enums.BackupType;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.CombatManager;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.core.settings.CoreConfig;
import pl.blackwater.market.data.Auction;
import pl.blackwater.market.managers.AuctionManager;
import pl.blackwaterapi.commands.Command;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.packets.impl.backups.BackupCreatePacket;
import pl.justsectors.packets.impl.others.SectorDisablePacket;
import pl.justsectors.packets.impl.others.SectorEnablePacket;
import pl.justsectors.packets.impl.others.StopServerPacket;
import pl.justsectors.redis.client.RedisClient;
import pl.justsectors.sectors.SectorManager;

public class StopCommand extends Command implements Colors{

	public StopCommand() {
		super("zatrzymaj", "/zatrzymaj", "/zatrzymaj <this/all>", "core.stop", "stop");
	}

	@Override
	public boolean onExecute(CommandSender sender, String[] args) {
		if(args[0].equalsIgnoreCase("this")) {
			RedisClient.sendSectorsPacket(new SectorDisablePacket(CoreConfig.CURRENT_SECTOR_NAME));
			SectorManager.getCurrentSector().ifPresent(sector -> sector.getRedisOnlinePlayers().clear());
			Bukkit.savePlayers();
			for (Player online : Bukkit.getOnlinePlayers()) {
				final Backup backup = Core.getBackupManager().createBackup(online, BackupType.CLOSESERVER, "CONSOLE");
				backup.insert();
				RedisClient.sendSectorsPacket(new BackupCreatePacket(backup));
				User onlineUser = UserManager.getUser(online);
				onlineUser.update(true);
				online.saveData();
				CombatManager.removeCombat(online);
				UserManager.leaveFromGame(online, false);
				online.kickPlayer(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Serwer jest w trakcie " + WarningColor + BOLD + "wylaczania\n" + SpecialSigns + ">> " + MainColor + "Wejdz ponownie za chwile!")));
			}
			for(Auction auction : AuctionManager.getAuctions().values())
			{
				auction.update(true);
			}
			for (World w : Bukkit.getWorlds())
				w.save();
			Bukkit.shutdown();
			return false;
		}else if (args[0].equalsIgnoreCase("all")){
			final StopServerPacket packet = new StopServerPacket(sender.getName(),System.currentTimeMillis());
			RedisClient.sendSectorsPacket(packet);
		}
		return true;
	}


}
