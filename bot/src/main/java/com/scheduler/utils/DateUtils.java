package com.scheduler.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Serhii_Udaltsov on 4/26/2021
 */
public class DateUtils {
    public static final String UTC = "UTC";
    private static final AtomicLong LAST_TIME_MS = new AtomicLong();
    private static final String SIMPLE_DATE_TIME_FORMAT = "MMM dd, yyyy HH:mm";


    public static String convertToUserDate(long timeInMillis) {
        SimpleDateFormat formatter = new SimpleDateFormat(SIMPLE_DATE_TIME_FORMAT);
        formatter.setTimeZone(TimeZone.getTimeZone(UTC));
        return formatter.format(new Date(timeInMillis));
    }

    public static long getStartOfCurrentMonth() {
        LocalDate firstDay = LocalDate.now().withDayOfMonth(1);
        return firstDay.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    public static long getEndOfCurrentMonth() {
        LocalDate firstDay = LocalDate.now().withDayOfMonth(1);
        return firstDay.plusMonths(1).atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    public static long uniqueCurrentTime() {
        long now = System.currentTimeMillis();
        while (true) {
            long lastTime = LAST_TIME_MS.get();
            if (lastTime >= now)
                now = lastTime + 1;
            if (LAST_TIME_MS.compareAndSet(lastTime, now))
                return now;
        }
    }
}
