package org.teamsoft.controller.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class MyDateUtils {
    public static Date convertLocalDateToDate(LocalDate dateToConvert) {
        return java.util.Date.from(dateToConvert.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

    public static LocalDate generateRandomDate() {
        return getRandomLocalDate(LocalDate.ofYearDay(1980, 1), LocalDate.now());
    }

    /**
     * le añade al int de solo un digito, o sea, menor que 10 un 0 delante
     * para su representacion como string. Ej. 1 -> 01
     */
    public static String getStringByNumber(int value) {
        return value < 10 ? "0" + value : String.valueOf(value);
    }

    /**
     * dado un <code>int<code/> de 4 digitos que representa el año
     * se obtienen la representacion en dos digitos (los ultimos dos digitos)
     */
    public static String getYearAsString(int year) {
        String str = String.valueOf(year);
        return str.substring(str.length() - 2);
    }

    public static LocalDate getRandomLocalDate(LocalDate startInclusive, LocalDate endExclusive) {
        long startEpochDay = startInclusive.toEpochDay();
        long endEpochDay = endExclusive.toEpochDay();
        long randomDay = ThreadLocalRandom
                .current()
                .nextLong(startEpochDay, endEpochDay);

        return LocalDate.ofEpochDay(randomDay);
    }
}
