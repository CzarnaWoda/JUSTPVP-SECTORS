package pl.blackwater.core.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import pl.blackwater.core.interfaces.Colors;

public class PlayerLoginListener implements Listener,Colors{



	@EventHandler
	public void onPlayerPreLogin(AsyncPlayerPreLoginEvent e) {

	}
		/*TODO on bungee
		if(Bukkit.getOnlinePlayers().size() >= CoreConfig.SLOTMANAGER_SLOTS) {
			final User user = UserManager.getUser(e.getUniqueId());
			if(user == null || user.getRank().equalsIgnoreCase("gracz")) {
				ByteArrayDataOutput out = ByteStreams.newDataOutput();
				String cancelReason = Util.fixColor(Util.replaceString(">> " + MainColor + "Serwer jest " + ImportantColor + "pelen!\n" + SpecialSigns + ">> " + MainColor + "Sprobuj dolaczyc pozniej lub zakup range " + ImportantColor + "PREMIUM"));
				out.writeUTF("KickPlayer");
				out.writeUTF(e.getName());
				out.writeUTF(cancelReason);
				e.setKickMessage(cancelReason);
				e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_FULL);
				final Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
				assert player != null;
				player.sendPluginMessage(Core.getPlugin(), "BungeeCord", out.toByteArray());
				return;
			}
		}
		final BanIP banip = BanIPManager.getBanIP(e.getAddress().getHostAddress());
		if (banip != null) {
			if (banip.isAlive()) {
				BanIPManager.deleteBan(banip);
				return;
			}
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			String cancelReason = Util.fixColor(Util.replaceString(MessageConfig.SERVERNAME_TAG + "\n \n" + WarningColor + "       Zostales zbanowany\n" + SpecialSigns + "» " + MainColor + "Dnia: " + ImportantColor + Util.getDate(banip.getCreateTime()) + ((banip.getExpireTime() > 0L) ? "\n" + SpecialSigns + "» " + MainColor + "Do: " + ImportantColor + Util.getDate(banip.getExpireTime()) : "\n " + SpecialSigns + "» Do: " + ImportantColor + "na zawsze") + "\n" + SpecialSigns + "» " + MainColor + "Przez: " + ImportantColor + banip.getAdmin() + "\n" + SpecialSigns + "» " + MainColor + "Powod: " + ImportantColor + UnderLined + banip.getReason() + "\n\n" + SpecialSigns + "» " + MainColor + "Kontakt TeamSpeak3:" + ImportantColor + "justpvp.pl"));
			out.writeUTF("KickPlayer");
			out.writeUTF(e.getName());
			out.writeUTF(cancelReason);
			e.setKickMessage(cancelReason);
			e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);
			final Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
			assert player != null;
			player.sendPluginMessage(Core.getPlugin(), "BungeeCord", out.toByteArray());
			return;
		}
		final Ban ban = BanManager.getBan(e.getUniqueId());
		if (ban != null) {
			if (ban.isAlive()) {
				BanManager.deleteBan(ban);
				return;
			}
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			String cancelReason = Util.fixColor(MessageConfig.SERVERNAME_TAG + "\n \n" + WarningColor + "       Zostales zbanowany\n" + SpecialSigns + "» " + MainColor + "Dnia: " + ImportantColor + Util.getDate(ban.getCreateTime()) + ((ban.getExpireTime() > 0L) ? "\n" + SpecialSigns + "» " + MainColor + "Do: " + ImportantColor + Util.getDate(ban.getExpireTime()) : "\n " + SpecialSigns + "» " + MainColor + "Do: " + ImportantColor + "na zawsze") + "\n" + SpecialSigns + "» " + MainColor + "Przez: " + ImportantColor + ban.getAdmin() + "\n" + SpecialSigns + "» " + MainColor + "Powod: " + ImportantColor + UnderLined + ban.getReason() + "\n\n" + SpecialSigns + "» " + MainColor + "Kontakt TeamSpeak3: " + ImportantColor + "justpvp.pl");
			out.writeUTF("KickPlayer");
			out.writeUTF(e.getName());
			out.writeUTF(cancelReason);
			e.setKickMessage(cancelReason);
			e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);
			final Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
			assert player != null;
			player.sendPluginMessage(Core.getPlugin(), "BungeeCord", out.toByteArray());
			return;
		}
		if (WhiteListManager.isWhitelist() && !WhiteListManager.isWhitelist(e.getUniqueId())) {
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			String cancelReason = MessageConfig.MESSAGE_WHITELIST_KICKREASON;
			out.writeUTF("KickPlayer");
			out.writeUTF(e.getName());
			out.writeUTF(cancelReason);
			e.setKickMessage(cancelReason);
			e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST);
			final Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
			assert player != null;
			player.sendPluginMessage(Core.getPlugin(), "BungeeCord", out.toByteArray());
		}
	}

		 */
}
