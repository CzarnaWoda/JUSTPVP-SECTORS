package pl.blackwater.core.tablist;

import org.bukkit.*;
import java.util.*;

public final class StringUtils
{
    public static String replace(final String text, final String searchString, String replacement) {
        if (text == null || text.isEmpty() || searchString.isEmpty()) {
            return text;
        }
        if (replacement == null) {
            replacement = "";
        }
        int start = 0;
        int max = -1;
        int end = text.indexOf(searchString, start);
        if (end == -1) {
            return text;
        }
        final int replLength = searchString.length();
        int increase = replacement.length() - replLength;
        increase = ((increase < 0) ? 0 : increase);
        increase *= 16;
        final StringBuilder sb = new StringBuilder(text.length() + increase);
        while (end != -1) {
            sb.append(text.substring(start, end)).append(replacement);
            start = end + replLength;
            if (--max == 0) {
                break;
            }
            end = text.indexOf(searchString, start);
        }
        sb.append(text.substring(start));
        return sb.toString();
    }

    public static String colored(final String s) {
        return (s != null) ? ChatColor.translateAlternateColorCodes('&', s) : null;
    }

    public static List<String> colored(final List<String> list) {
        final List<String> colored = new ArrayList<String>();
        for (final String s : list) {
            colored.add(colored(s));
        }
        return colored;
    }

    public static String toString(final Collection<String> list, final boolean send) {
        final StringBuilder builder = new StringBuilder();
        for (final String s : list) {
            builder.append(s);
            builder.append(',');
            if (send) {
                builder.append(' ');
            }
        }
        String s2 = builder.toString();
        if (send) {
            if (s2.length() > 2) {
                s2 = s2.substring(0, s2.length() - 2);
            }
            else if (s2.length() > 1) {
                s2 = s2.substring(0, s2.length() - 1);
            }
        }
        return s2;
    }

    public static List<String> fromString(final String s) {
        List<String> list = new ArrayList<String>();
        if (s == null || s.isEmpty()) {
            return list;
        }
        list = Arrays.asList(s.split(","));
        return list;
    }

    public static String appendDigit(final int number) {
        return (number > 9) ? ("" + number) : ("0" + number);
    }

    public static String getPercent(final double dividend, final double divisor) {
        return getPercent(dividend / divisor);
    }

    public static String getPercent(final double fraction) {
        return String.format(Locale.US, "%.1f", 100.0 * fraction);
    }
}

