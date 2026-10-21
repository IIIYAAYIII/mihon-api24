package j$.time;

import j$.time.format.DateTimeFormatter;
import j$.time.temporal.Temporal;
import j$.time.temporal.TemporalAccessor;

public final class LocalDateTime implements Temporal, TemporalAccessor, Comparable<LocalDateTime> {
    public static LocalDateTime parse(CharSequence text) {
        return null;
    }

    public static LocalDateTime parse(CharSequence text, DateTimeFormatter formatter) {
        return null;
    }

    public static LocalDateTime now() {
        return null;
    }

    public static LocalDateTime now(ZoneId zone) {
        return null;
    }

    public static LocalDateTime of(int year, int month, int dayOfMonth, int hour, int minute) {
        return null;
    }

    public static LocalDateTime of(int year, int month, int dayOfMonth, int hour, int minute, int second) {
        return null;
    }

    public ZonedDateTime atZone(ZoneId zone) {
        return null;
    }

    public LocalDate toLocalDate() {
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

    public int getHour() {
        return 0;
    }

    public int getMinute() {
        return 0;
    }

    public int getSecond() {
        return 0;
    }

    public LocalDateTime plusDays(long days) {
        return null;
    }

    public LocalDateTime minusDays(long days) {
        return null;
    }

    public LocalDateTime plusHours(long hours) {
        return null;
    }

    public LocalDateTime minusHours(long hours) {
        return null;
    }

    public LocalDateTime plusMinutes(long minutes) {
        return null;
    }

    public LocalDateTime minusMinutes(long minutes) {
        return null;
    }

    public LocalDateTime plusSeconds(long seconds) {
        return null;
    }

    public LocalDateTime minusSeconds(long seconds) {
        return null;
    }

    @Override
    public int compareTo(LocalDateTime o) {
        return 0;
    }
}
