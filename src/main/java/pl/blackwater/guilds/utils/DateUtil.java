package pl.blackwater.guilds.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtil {

    public static String getCurrentHour() {
        Calendar cal = Calendar.getInstance();
        String HOUR_FORMAT = "HH:mm";
        SimpleDateFormat sdfHour = new SimpleDateFormat(HOUR_FORMAT);
        return sdfHour.format(cal.getTime());
    }

    /**
     * @param  target  hour to check
     * @param  start   interval start
     * @param  end     interval end
     * @return true    true if the given hour is between
     */
    public static boolean isHourInInterval(String target, String start, String end) {
        return ((target.compareTo(start) >= 0)
                && (target.compareTo(end) <= 0));
    }

    /**
     * @param  start   interval start
     * @param  end     interval end
     * @return true    true if the current hour is between
     */
    public static boolean isNowInInterval(String start, String end) {
        return DateUtil.isHourInInterval
                (DateUtil.getCurrentHour(), start, end);
    }
}
