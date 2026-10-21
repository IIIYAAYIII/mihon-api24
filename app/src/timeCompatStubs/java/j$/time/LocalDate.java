package j$.time;

import j$.time.chrono.ChronoLocalDate;
import j$.time.format.DateTimeFormatter;
import j$.time.temporal.Temporal;
import j$.time.temporal.TemporalAccessor;

public final class LocalDate implements ChronoLocalDate, Temporal, TemporalAccessor, Comparable<ChronoLocalDate> {
    public static LocalDate parse(CharSequence text) {
        return null;
    }

    public static LocalDate parse(CharSequence text, DateTimeFormatter formatter) {
        return null;
    }

    public static LocalDate now() {
        return null;
    }

    public static LocalDate now(ZoneId zone) {
        return null;
    }

    public static LocalDate of(int year, int month, int dayOfMonth) {
        return null;
    }

    public LocalDateTime atStartOfDay() {
        return null;
    }

    public ZonedDateTime atStartOfDay(ZoneId zone) {
        return null;
    }

    public int getYear() {
        return 0;
    }

    public int getMonthValue() {
        return 0;
    }

    public int getDayOfMonth() {
        return 0;
    }

    public DayOfWeek getDayOfWeek() {
        return null;
    }

    public LocalDate plusDays(long days) {
        return null;
    }

    public LocalDate minusDays(long days) {
        return null;
    }

    public LocalDate plusWeeks(long weeks) {
        return null;
    }

    public LocalDate minusWeeks(long weeks) {
        return null;
    }

    public LocalDate plusMonths(long months) {
        return null;
    }

    public LocalDate minusMonths(long months) {
        return null;
    }

    public LocalDate plusYears(long years) {
        return null;
    }

    public LocalDate minusYears(long years) {
        return null;
    }

    @Override
    public int compareTo(ChronoLocalDate o) {
        return 0;
    }
}
