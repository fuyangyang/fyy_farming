package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalField;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtilsJDK8 {
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");
    public static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final ZoneOffset TIMEZONE_EAST_8 = ZoneOffset.of("+8");
    public static final DateTimeFormatter DATE_TIME_FORMAT_WITH_T = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Formats current date using the formatter "yyyy-MM-dd"
     * @return formatted string
     */
    public static String getFormattedDate() {
        return DATE_FORMAT.format(LocalDate.now());
    }

    /**
     * Formats current date-time using the formatter "yyyy-MM-dd HH:mm:ss"
     * @return formatted string
     */
    public static String getFormattedDateTime() {
        return DATE_TIME_FORMAT.format(LocalDateTime.now());
    }

    /**
     * Formats specific long timestamp using the formatter "yyyy-MM-dd HH:mm:ss"
     * @param ts in seconds
     * @return formatted string
     */
    public static String getFormattedDateTimeFromSeconds(long ts) {
        return DATE_TIME_FORMAT.format(LocalDateTime.ofInstant(Instant.ofEpochSecond(ts), TIMEZONE_EAST_8));
    }

    /**
     * Formats this long timestamp using given formatter
     * @param ts in seconds
     * @return formatted string
     */
    public static String getFormattedDateTimeFromSeconds(long ts, String formatter) {
        return DateTimeFormatter.ofPattern(formatter).
                format(LocalDateTime.ofInstant(Instant.ofEpochSecond(ts), TIMEZONE_EAST_8));
    }

    /**
     * Formats this long timestamp using given formatter
     * @param ts in millioseconds
     * @return formatted string
     */
    public static String getFormattedDateTimeFromMs(long ts, String format) {
        return DateTimeFormatter.ofPattern(format).
                format(LocalDateTime.ofInstant(Instant.ofEpochMilli(ts), TIMEZONE_EAST_8));
    }

    /**
     * Formats this long timestamp using the formatter "yyyy-MM-dd HH:mm:ss"
     * @param ts in milliseconds
     * @return formatted string
     */
    public static String getFormattedDateTimeFromMs(long ts) {
        return DATE_TIME_FORMAT.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(ts), TIMEZONE_EAST_8));
    }

    /**
     * @param date formatted string of now like "yyyy-MM-dd"
     * @return
     * @throws ParseException
     */
    public static long parseFromDate(String date) throws ParseException {
        return LocalDate.parse(date, DATE_FORMAT).atStartOfDay().toInstant(TIMEZONE_EAST_8).toEpochMilli();
    }

    /**
     * @param dateTime formatted string of now like "yyyy-MM-dd HH:mm:ss"
     * @return the number of milliseconds since the epoch of 1970-01-01T00:00:00Z
     * @throws ParseException
     */
    public static long parseFromDateTime(String dateTime) throws ParseException {
        return LocalDateTime.parse(dateTime, DATE_TIME_FORMAT).toInstant(TIMEZONE_EAST_8).toEpochMilli();
    }

    /**
     * Add specific days to current date
     * @param daysToAdd
     * @return  date string formmated by "yyyy-MM-dd"
     */
    public static String plusDay(long daysToAdd) {
        return LocalDate.now().plusDays(daysToAdd).format(DATE_FORMAT);
    }

    /**
     * Add specific days to given date
     * @param daysToAdd
     * @return date string formmated by "yyyy-MM-dd"
     */
    public static String plusDay(String date, long daysToAdd) throws ParseException {
        return LocalDate.parse(date, DATE_FORMAT).plusDays(daysToAdd).format(DATE_FORMAT);
    }

    public static void main(String[] args) throws ParseException {
        System.out.println(getFormattedDate());
        System.out.println(getFormattedDateTime());
        String date = "2014-04-12";
        String dateTime = "2014-04-12 12:12:12";
        System.out.println(parseFromDate(date));
        System.out.println(parseFromDateTime(dateTime));
        System.out.println(plusDay(1));
        System.out.println(plusDay(date, 1));
        System.out.println(getFormattedDateTimeFromMs(1397232000000L));
        System.out.println(getFormattedDateTimeFromSeconds(1397232000L));
        System.out.println(getFormattedDateTimeFromSeconds(1397232000L, "yyyy_MM_dd"));
        System.out.println(getFormattedDateTimeFromMs(1397232000000L, "yyyy_MM_dd_HH_mm_ss"));
    }
}
