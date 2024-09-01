package org.example.services;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class LocalDateToDate {
    public static Date convert(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
