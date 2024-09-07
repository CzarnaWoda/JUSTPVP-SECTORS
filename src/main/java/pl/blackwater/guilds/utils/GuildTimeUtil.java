package pl.blackwater.guilds.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.command.CommandSender;

public class GuildTimeUtil {
	public static boolean sendMsg(CommandSender sender, String msg, boolean returnValue) {
		sender.sendMessage(msg);
		return returnValue;
	}

	public static String getDate(long time) {
		return new SimpleDateFormat("dd/MM/yyyy, HH:mm:ss").format(new Date(time));
	}

	public static String getDurationBreakdown(long millis) {
		if (millis == 0L) {
			return "0";
		}
		long days = TimeUnit.MILLISECONDS.toDays(millis);
		if (days > 0L) {
			millis -= TimeUnit.DAYS.toMillis(days);
		}
		long hours = TimeUnit.MILLISECONDS.toHours(millis);
		if (hours > 0L) {
			millis -= TimeUnit.HOURS.toMillis(hours);
		}
		long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
		if (minutes > 0L) {
			millis -= TimeUnit.MINUTES.toMillis(minutes);
		}
		long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
		if (seconds > 0L) {
			millis -= TimeUnit.SECONDS.toMillis(seconds);
		}
		StringBuilder sb = new StringBuilder();
		if (days > 0L) {
			sb.append(days);
			long i = days % 10L;
			if (i == 1L) {
				sb.append(" dzien ");
			} else {
				sb.append(" dni ");
			}
		}
		if (hours > 0L) {
			sb.append(hours);
			long i = hours % 10L;
			if (i == 1L) {
				sb.append(" godzine ");
			} else if (i < 4L) {
				sb.append(" godziny ");
			} else {
				sb.append(" godzin ");
			}
		}
		if (minutes > 0L) {
			sb.append(minutes);
			long i = minutes % 10L;
			if (i == 1L) {
				sb.append(" minute ");
			} else if (i < 4L) {
				sb.append(" minuty ");
			} else {
				sb.append(" minut ");
			}
		}
		if (seconds > 0L) {
			sb.append(seconds);
			long i = seconds % 10L;
			if (i == 1L) {
				sb.append(" sekunde");
			} else if (i < 5L) {
				sb.append(" sekundy");
			} else {
				sb.append(" sekund");
			}
		}
		return sb.toString();
	}

	public static String getCzasGry(long millis) {
		if (millis == 0L) {
			return "0";
		}
		long days = TimeUnit.MILLISECONDS.toDays(millis);
		if (days > 0L) {
			millis -= TimeUnit.DAYS.toMillis(days);
		}
		long hours = TimeUnit.MILLISECONDS.toHours(millis);
		if (hours > 0L) {
			millis -= TimeUnit.HOURS.toMillis(hours);
		}
		long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
		if (minutes > 0L) {
			millis -= TimeUnit.MINUTES.toMillis(minutes);
		}
		long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
		if (seconds > 0L) {
			millis -= TimeUnit.SECONDS.toMillis(seconds);
		}
		StringBuilder sb = new StringBuilder();
		if (days > 0L) {
			sb.append(days);
			long i = days % 10L;
			if (i == 1L) {
				sb.append(" dzien ");
			} else {
				sb.append(" dni ");
			}
		}
		if (hours > 0L) {
			sb.append(hours);
			long i = hours % 10L;
			if (i == 1L) {
				sb.append(" godzina ");
			} else if (i < 5L) {
				sb.append(" godziny ");
			} else {
				sb.append(" godzin ");
			}
		}
		if (minutes > 0L) {
			sb.append(minutes);
			long i = minutes % 10L;
			if (i == 1L) {
				sb.append(" minuta ");
			} else if (i < 5L) {
				sb.append(" minuty ");
			} else {
				sb.append(" minut ");
			}
		}
		if (seconds > 0L) {
			sb.append(seconds);
			long i = seconds % 10L;
			if (i == 1L) {
				sb.append(" sekunda");
			} else if (i < 5L) {
				sb.append(" sekundy");
			} else {
				sb.append(" sekund");
			}
		}
		return sb.toString();
	}

	public static long parseTimeDiffInMillis(String time) throws Exception {
		Pattern timePattern = Pattern.compile(
				"(?:([0-9]+)\\s*y[a-z]*[,\\s]*)?(?:([0-9]+)\\s*mo[a-z]*[,\\s]*)?(?:([0-9]+)\\s*w[a-z]*[,\\s]*)?(?:([0-9]+)\\s*d[a-z]*[,\\s]*)?(?:([0-9]+)\\s*h[a-z]*[,\\s]*)?(?:([0-9]+)\\s*m[a-z]*[,\\s]*)?(?:([0-9]+)\\s*(?:s[a-z]*)?)?",

				Pattern.CASE_INSENSITIVE);
		Matcher m = timePattern.matcher(time);
		long seconds = 0L;
		boolean found = false;
		while (m.find()) {
			if ((m.group() != null) && (!m.group().isEmpty())) {
				for (int i = 0; i < m.groupCount(); i++) {
					if ((m.group(i) != null) && (!m.group(i).isEmpty())) {
						found = true;
						break;
					}
				}
				if (found) {
					if ((m.group(1) != null) && (!m.group(1).isEmpty())) {
						seconds += 31556926 * Integer.parseInt(m.group(1));
					}
					if ((m.group(2) != null) && (!m.group(2).isEmpty())) {
						seconds += 2629743 * Integer.parseInt(m.group(2));
					}
					if ((m.group(3) != null) && (!m.group(3).isEmpty())) {
						seconds += 604800 * Integer.parseInt(m.group(3));
					}
					if ((m.group(4) != null) && (!m.group(4).isEmpty())) {
						seconds += 86400 * Integer.parseInt(m.group(4));
					}
					if ((m.group(5) != null) && (!m.group(5).isEmpty())) {
						seconds += 3600 * Integer.parseInt(m.group(5));
					}
					if ((m.group(6) != null) && (!m.group(6).isEmpty())) {
						seconds += 60 * Integer.parseInt(m.group(6));
					}
					if ((m.group(7) == null) || (m.group(7).isEmpty())) {
						break;
					}
					seconds += Integer.parseInt(m.group(7));

					break;
				}
			}
		}
		if (!found) {
			throw new Exception("illegalDate");
		}
		return seconds * 1000L;
	}
}
