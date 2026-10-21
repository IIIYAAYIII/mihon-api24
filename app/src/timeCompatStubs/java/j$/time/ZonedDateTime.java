package j$.time;

import j$.time.chrono.ChronoLocalDate;
import j$.time.chrono.ChronoZonedDateTime;
import j$.time.format.DateTimeFormatter;
import j$.time.temporal.Temporal;
import j$.time.temporal.TemporalAccessor;

public final class ZonedDateTime implements ChronoZonedDateTime<ChronoLocalDate>, Temporal, TemporalAccessor {
    public static ZonedDateTime parse(CharSequence text) {
        return null;
    }

    public static ZonedDateTime parse(CharSequence text, DateTimeFormatter formatter) {
        return null;
    }

    public static ZonedDateTime now() {
        return null;
    }

    public static ZonedDateTime now(ZoneId zone) {
        return null;
    }

    public static ZonedDateTime ofInstant(Instant instant, ZoneId zone) {
        return null;
    }

    public Instant toInstant() {
        return null;
    }

    public long toEpochSecond() {
        return 0L;
    }

    public ZoneId getZone() {
        return null;
    }

    public LocalDateTime toLocalDateTime() {
        return null;
    }

    public LocalDate toLocalDate() {
        return null;
    }

    public ZonedDateTime withZoneSameInstant(ZoneId zone) {
        return null;
    }

    public ZonedDateTime plusDays(long days) {
        return null;
    }

    public ZonedDateTime minusDays(long days) {
        return null;
    }

    public ZonedDateTime plusHours(long hours) {
        return null;
    }

    public ZonedDateTime minusHours(long hours) {
        return null;
    }

    public ZonedDateTime plusMinutes(long minutes) {
        return null;
    }

    public ZonedDateTime minusMinutes(long minutes) {
        return null;
    }

    public ZonedDateTime plusSeconds(long seconds) {
        return null;
    }

    public ZonedDateTime minusSeconds(long seconds) {
        return null;
    }

    @Override
    public int compareTo(ChronoZonedDateTime<?> o) {
        return 0;
    }
}
