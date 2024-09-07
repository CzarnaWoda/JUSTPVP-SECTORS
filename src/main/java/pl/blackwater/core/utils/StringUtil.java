package pl.blackwater.core.utils;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;

import pl.blackwaterapi.utils.RandomUtil;

public class StringUtil {
	public static List<ChatColor> colors = Arrays.asList(ChatColor.AQUA,ChatColor.BLACK,ChatColor.BLUE,ChatColor.DARK_AQUA,ChatColor.DARK_BLUE,ChatColor.DARK_GRAY,ChatColor.DARK_GREEN,ChatColor.DARK_PURPLE,ChatColor.DARK_RED,ChatColor.GOLD,ChatColor.GRAY,ChatColor.GREEN,ChatColor.LIGHT_PURPLE,ChatColor.RED,ChatColor.WHITE,ChatColor.YELLOW);

	public static String replaceText(String text, String searchString, String replacement) {
		if (text == null || text.isEmpty() || searchString.isEmpty() || replacement == null) {
			return text;
		}
		int start = 0;
		int max = -1;
		int end = text.indexOf(searchString, start);
		if (end == -1) {
			return text;
		}
		int replLength = searchString.length();
		int increase = replacement.length() - replLength;
		increase = ((increase < 0) ? 0 : increase);
		increase *= 16;
		StringBuilder sb = new StringBuilder(text.length() + increase);
		while (end != -1) {
			sb.append(text, start, end).append(replacement);
			start = end + replLength;
			if (--max == 0) {
				break;
			}
			end = text.indexOf(searchString, start);
		}
		sb.append(text.substring(start));
		return sb.toString();
	}

	public static int stringToInt(String string) {
		return Integer.decode(string);
	}

	public static boolean isAlphaNumeric(String s) {
		return s.matches("^[a-zA-Z0-9_]*$");
	}

	public static boolean isFloat(String string) {
		return Pattern.matches("([0-9]*)\\.([0-9]*)", string);
	}

	public static boolean isInteger(String string) {
		return Pattern.matches("-?[0-9]+", string.subSequence(0, string.length()));
	}
    public static String coloredString(final String string) {
        StringBuilder nju = new StringBuilder("§7");
        for (int i = 0; i < string.length(); i++) {
        	final int ii = RandomUtil.getRandInt(0, colors.size()-1);
            nju.append(colors.get(ii)).append(string.charAt(i));
        }
        return nju.toString();
    }
	public static String appendDigit(int number) {
		return number > 9 ? "" + number : "0" + number;
	}

	public static String appendDigit(String number) {
		return number.length() > 1 ? "" + number : "0" + number;
	}
}
