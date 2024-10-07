package org.example.helpers;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Date;

public class DateHelper {

    private DateHelper() {}

    public static Date convertLocalDateToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static long getDayBetweenDates(Temporal dateFrom, Temporal dateTo) {
        return -1 * ChronoUnit.DAYS.between(dateFrom, dateTo);
    }

}
