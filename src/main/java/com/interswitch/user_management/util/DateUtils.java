package com.interswitch.user_management.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by emmanuel on 12/16/16.
 */
public class DateUtils {

    public static DateTimeFormatter df = DateTimeFormat.forPattern("yyyy/MM/dd");
    public static DateTimeFormatter df2 = DateTimeFormat.forPattern("yyMMddHHmmss");
    public static DateTimeFormatter df3 = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss");
    public static DateTimeFormatter df4 = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ");
    public static DateTimeFormatter df5 = DateTimeFormat.forPattern("dd MMM yyy");
    public static DateTimeFormatter df6 = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    public static DateTimeFormatter df7 = DateTimeFormat.forPattern("yyMMdd");
    public static DateTimeFormatter df8 = DateTimeFormat.forPattern("yyyy-MM-dd");
    public static DateTimeFormatter df9 = DateTimeFormat.forPattern("ddMMyyyy");
    public static DateTimeFormatter df10 = DateTimeFormat.forPattern("dd MMM, yyyy");
    public static DateTimeFormatter df11 = DateTimeFormat.forPattern("MMM dd, 'yy");
    public static DateTimeFormatter dYear = DateTimeFormat.forPattern("yyyy");


    public static Date getDateInUTC(Date date) {
        DateFormat formatterUTC = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        formatterUTC.setTimeZone(TimeZone.getTimeZone("UTC")); // UTC timezone

        return getDateFromString3(formatterUTC.format(date));
    }

    public static Date getDateFromString(String date) {
        if (StringUtils.isEmpty(date)) {
            return null;
        }

        return DateTime.parse(date, df).toDate();
    }

    public static Date getDateFromString2(String date) {
        if (StringUtils.isEmpty(date)) {
            return null;
        }

        return DateTime.parse(date, df6).toDate();
    }

    public static Date getDateFromString3(String date) {
        if (StringUtils.isEmpty(date)) {
            return null;
        }

        return DateTime.parse(date, df3).toDate();
    }

    public static Date getDateFromString4(String date) {
        if (StringUtils.isEmpty(date)) {
            return null;
        }

        return DateTime.parse(date, df8).toDate();
    }

    public static String getYearAsString(Date date) {
        if (null == date) {
            return null;
        }

        String s = dYear.print(new DateTime(date));
        return s;
    }

    public static String getDateAsString(Date date) {
        if (null == date) {
            return null;
        }

        String s = df.print(new DateTime(date));
        return s;
    }

    public static String getDateAsString2(Date date) {
        if (null == date) {
            return null;
        }

        String s = df2.print(new DateTime(date));
        return s;
    }

    public static Date getWeekStartDate() {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("Africa/Lagos"));
        System.out.print("Current time " + c.getTime());
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        System.out.print("Clear time " + c.getTime());

        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Date start = c.getTime();
        System.out.println("Week Start Date =" + start);
        return start;
    }

    public static Date getPreviousWeekStartDate() {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("Africa/Lagos"));
        System.out.println("Current time " + c.getTime());
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        System.out.println("Clear time " + c.getTime());
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        System.out.println("Day of week time " + c.getTime());
        c.add(Calendar.WEEK_OF_YEAR, -1);
        Date start = c.getTime();
        System.out.println("Week Start Date =" + start);
        return start;
    }

    public static Map<String, Date> getWeekInReviewDates() {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("Africa/Lagos"));
        System.out.println("Current time " + c.getTime());
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        c.add(Calendar.WEEK_OF_YEAR, -1);

        Map<String, Date> map = new HashMap<String, Date>();
        map.put("start", c.getTime());
        c.add(Calendar.WEEK_OF_YEAR, 1);
        // As start date was monday 12:00am, adding 1 week would still get it to next monday 12:00 am.
        // To ensure that Sunday's date is sent as end date we are subtracting 1 second
        c.add(Calendar.SECOND, -1);
        map.put("end", c.getTime());
        return map;
    }

    /**
     *
     * Return the end date and end time of the given date
     *
     * @param date
     *
     * @return
     *
     */
    public static Date getEndOfDay(Date date) {

        return org.apache.commons.lang3.time.DateUtils.addMilliseconds(org.apache.commons.lang3.time.DateUtils.ceiling(date, Calendar.DATE), -1);

    }

    /**
     * Return the date minutes before a given date
     * @param date
     * @return
     */
    public static Date getDateMinutesBefore(int minutes, Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, -1 * minutes);
        return cal.getTime();
    }

    /**
     * Return the start date of n days before a given date
     * @param date
     * @return
     */
    public static Date getPreviousNdayStartDate(int n, Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, (-1 * n));
        setTimeToBeginningOfDay(cal);
        return cal.getTime();
    }

    /**
     * Return the date of n days after a given date
     * @param date
     * @return
     */
    public static Date getNextNdayDate(int n, Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, (n));
        return cal.getTime();
    }

    /**
     *
     * * Return the start date and start time of the given date
     *
     * @param date
     *
     * @return
     *
     */
    public static Date getStartOfDay(Date date) {

        return org.apache.commons.lang3.time.DateUtils.truncate(date, Calendar.DATE);

    }

    /**
     *
     * Returns the start date of the week beginning on monday.
     *
     *
     * @return
     *
     */
    public static Date getWeekStartDateMonday() {

        Calendar cal = GregorianCalendar.getInstance();

        String startDateString = "", endDateString = "", todaysDateString = "";

        Date startDate = null;

        try {

            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            todaysDateString = sdf.format(cal.getTime());

            Date todaysDate = sdf.parse(todaysDateString);

            //Sets the first day of the current week to MONDAY
            cal.setFirstDayOfWeek(Calendar.MONDAY);

            cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());

            startDateString = sdf.format(cal.getTime());

            startDate = sdf.parse(startDateString);

        } catch (ParseException pe) {
            System.out.println("Exception:" + pe.getMessage());
        }
        return startDate;
    }

    /**
     *
     * Returns the end date of the week ending on sunday.
     *
     * @param date
     * @return
     *
     */
    public static Date getWeekEndDateSunday() {

        Calendar cal = GregorianCalendar.getInstance();

        String startDateString = "", endDateString = "", todaysDateString = "";

        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        Date endDate = null;

        try {

            todaysDateString = sdf.format(cal.getTime());

            Date todaysDate = sdf.parse(todaysDateString);

            //Sets the first day of the current week to MONDAY
            cal.setFirstDayOfWeek(Calendar.MONDAY);

            cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());

            startDateString = sdf.format(cal.getTime());

            Date startDate = sdf.parse(startDateString);

            cal.add(Calendar.DATE, 6);

            endDateString = sdf.format(cal.getTime());

            endDate = sdf.parse(endDateString);

        } catch (ParseException pe) {
            pe.getMessage();
        }
        return endDate;
    }

    public static List<Date> getDaysOfCurrentWeek() {

        List<Date> dow = new ArrayList<>();
        DateTime dt = DateTime.now();

        dow.add(dt.withDayOfWeek(1).toDate());
        dow.add(dt.withDayOfWeek(2).toDate());
        dow.add(dt.withDayOfWeek(3).toDate());
        dow.add(dt.withDayOfWeek(4).toDate());
        dow.add(dt.withDayOfWeek(5).toDate());
        dow.add(dt.withDayOfWeek(6).toDate());
        dow.add(dt.withDayOfWeek(7).toDate());

        return dow;
    }

    public static List<Date> getRangeofDays(Date startDate, Date endDate) {
        List<Date> rod = new ArrayList<>();
        DateTime dt = new DateTime(startDate);
        rod.add(dt.toDate());
        for (int i = 1; i < daysDiff(startDate, endDate); i++) {
            rod.add(dt.plusDays(i).toDate());
        }

        return rod;
    }

    public static long daysDiff(Date from, Date to) {
        return Math.round((to.getTime() - from.getTime()) / 86400000D);
    }

    /**
     * add days to date in java
     * @param date
     * @param days
     * @return
     */
    public static Date addDays(Date date, int days) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }

    /**
     * Return the start date of a given week that this date falls within
     * @param date
     * @return
     */
    public static Date getWeekStartDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        Calendar cal2 = Calendar.getInstance();
        cal2.setWeekDate(cal.get(Calendar.YEAR), cal.get(Calendar.WEEK_OF_YEAR), cal.getMinimum(Calendar.DAY_OF_WEEK));
        return cal2.getTime();
    }


    /**
     * * Return the end date of a given week that this date falls within
     * @param date
     * @return
     */
    public static Date getWeekEndDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        Calendar cal2 = Calendar.getInstance();
        cal2.setWeekDate(cal.get(Calendar.YEAR), cal.get(Calendar.WEEK_OF_YEAR), cal.getMaximum(Calendar.DAY_OF_WEEK));
        return cal2.getTime();
    }

    public static Date getMonthStartDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));

        return getStartOfDay(cal.getTime());
    }

    public static Date getMonthEndDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));

        return getEndOfDay(cal.getTime());
    }

    public static Date getYearStartDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_YEAR, 1);

        return getStartOfDay(cal.getTime());
    }

    public static Date getYearEndDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.getInstance().get(Calendar.YEAR);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, 11); // 11 = december
        cal.set(Calendar.DAY_OF_MONTH, 31); // new years eve

        return getEndOfDay(cal.getTime());
    }


    /**
     * return duration from date created
     *
     * @param number
     * @param type
     * @param createdDate
     * @return
     */
    public static Date getDuration(int number, String type, Date createdDate) {

        Date newDate = new Date();

        switch (type) {
            case "second":
                newDate = org.apache.commons.lang3.time.DateUtils.addSeconds(createdDate, number);
                break;
            case "minute":
                newDate = org.apache.commons.lang3.time.DateUtils.addMinutes(createdDate, number);
                break;
            case "hour":
                newDate = org.apache.commons.lang3.time.DateUtils.addHours(createdDate, number);
                break;
            case "day":
                newDate = org.apache.commons.lang3.time.DateUtils.addDays(createdDate, number);
                break;
            case "week":
                newDate = org.apache.commons.lang3.time.DateUtils.addWeeks(createdDate, number);
                break;
            case "month":
                newDate = org.apache.commons.lang3.time.DateUtils.addMonths(createdDate, number);
                break;
            case "year":
                newDate = org.apache.commons.lang3.time.DateUtils.addYears(createdDate, number);
                break;
        }

        return newDate;
    }

    private static void setTimeToBeginningOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    private static void setTimeToEndofDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
    }

    public static String getDateAsString7(Date date) {
        if (null == date) {
            return null;
        }

        String s = df7.print(new DateTime(date));
        return s;
    }

    /**
     * Return the date minutes after a given date
     * @param date
     * @return
     */
    public static Date getDateMinutesAfter(int minutes, Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, minutes);
        return cal.getTime();
    }
}

