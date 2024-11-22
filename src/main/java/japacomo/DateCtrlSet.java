package japacomo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateCtrlSet {
    private DateCtrlSet(){}

    public static Date getToday(Date date, String timeZone) {
        if (date==null) return null;

        Calendar calendar = initializeCalendar(timeZone);
        calendar.setTime(date);

        calendar.set(Calendar.HOUR_OF_DAY, 00);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);
        calendar.set(Calendar.MILLISECOND, 000);

        return calendar.getTime();
    }

    public static Date getYesterday(Date date, int diff, String timeZone) {
        //usage:if you want to get yesterday, set diff to -1.

        if (date==null) return null;

        Calendar calendar = initializeCalendar(timeZone);
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, diff);

        calendar.set(Calendar.HOUR_OF_DAY, 00);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);
        calendar.set(Calendar.MILLISECOND, 000);

        return calendar.getTime();
    }
    public static String takeTodayAsYYYYMMDD(String timeZone){
        Date targetDate = getToday(new Date(), timeZone);
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        return dateFormat.format(targetDate);
    }

    public static String takeYesterdayAsYYYYMMDD(int diff, String timeZone){
        Date targetDate = getYesterday(new Date(), diff, timeZone);
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        return dateFormat.format(targetDate);
    }

    private static Calendar initializeCalendar(String timeZone){
        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getTimeZone(timeZone);
        calendar.setTimeZone(tz);
        return calendar;
    }

    public static Date getFirstDateOfLastMonth(Date date, String timeZone){
        if (date==null) return null;

        Calendar calendar = initializeCalendar(timeZone);
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -1);

        calendar.set(Calendar.HOUR_OF_DAY, 00);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);
        calendar.set(Calendar.MILLISECOND, 000);

        return getFirstDateOfMonth(calendar.getTime(), timeZone);
    }

    public static Date getLastDateOfLastMonth(Date date, String timeZone){
        if (date==null) return null;

        Calendar calendar = initializeCalendar(timeZone);
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -1);

        calendar.set(Calendar.HOUR_OF_DAY, 00);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);
        calendar.set(Calendar.MILLISECOND, 000);

        return getLastDateOfMonth(calendar.getTime(), timeZone);
    }

    public static Date getFirstDateOfMonth(Date date, String timeZone) {

        if (date==null) return null;

        Calendar calendar = initializeCalendar(timeZone);
        calendar.setTime(date);
        int first = calendar.getActualMinimum(Calendar.DATE);
        calendar.set(Calendar.DATE, first);

        calendar.set(Calendar.HOUR_OF_DAY, 00);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);
        calendar.set(Calendar.MILLISECOND, 000);

        return calendar.getTime();
    }
    public static Date getLastDateOfMonth(Date date, String timeZone) {

        if (date == null) return null;

        Calendar calendar = initializeCalendar(timeZone);
        calendar.setTime(date);
        int last = calendar.getActualMaximum(Calendar.DATE);
        calendar.set(Calendar.DATE, last);

        calendar.set(Calendar.HOUR_OF_DAY, 00);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);
        calendar.set(Calendar.MILLISECOND, 000);

        return calendar.getTime();
    }

}
