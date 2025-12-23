package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * not thread-safe
 */
public class DateUtils {
    private static final SimpleDateFormat SDF_DATE = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat SDF_DATE_TIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String getTime() {
        return SDF_DATE_TIME.format(new Date());
    }

    public static long getTime(String str) throws ParseException {
        return SDF_DATE.parse(str).getTime();
    }

    public static String getTime(long ts) {
        return SDF_DATE_TIME.format(new Date(ts));
    }

    public static String getTime(long ts, String format) {
        return new SimpleDateFormat(format).format(new Date(ts));
    }

    public static String addDay(int num) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, num);
        return SDF_DATE.format(calendar.getTime());
    }

    public static String addDay(String str, int num) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(SDF_DATE.parse(str));
        calendar.add(Calendar.DATE, num);
        return SDF_DATE.format(calendar.getTime());
    }

    public static void main(String[] args) throws ParseException {
        System.out.println(addDay("2017-12-31", 1));
        System.out.println(getTime(1542024278466L));
    }

}
