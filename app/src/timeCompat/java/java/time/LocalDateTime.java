package java.time;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public final class LocalDateTime implements TemporalAccessor, Comparable<LocalDateTime> {
    final j$.time.LocalDateTime delegate;

    LocalDateTime(j$.time.LocalDateTime delegate) {
        this.delegate = delegate;
    }

    public static LocalDateTime parse(CharSequence text) {
        return wrap(j$.time.LocalDateTime.parse(text));
    }

    public static LocalDateTime parse(CharSequence text, DateTimeFormatter formatter) {
        if (formatter == null) {
            return parse(text);
        }
        return wrap(j$.time.LocalDateTime.parse(text, formatter.unwrap()));
    }

    public static LocalDateTime now() {
        return wrap(j$.time.LocalDateTime.now());
    }

    public static LocalDateTime now(ZoneId zone) {
        return wrap(j$.time.LocalDateTime.now(zone != null ? zone.unwrap() : j$.time.ZoneId.systemDefault()));
    }

    public static LocalDateTime of(int year, int month, int dayOfMonth, int hour, int minute) {
        return wrap(j$.time.LocalDateTime.of(year, month, dayOfMonth, hour, minute));
    }

    public static LocalDateTime of(int year, int month, int dayOfMonth, int hour, int minute, int second) {
        return wrap(j$.time.LocalDateTime.of(year, month, dayOfMonth, hour, minute, second));
    }

    public ZonedDateTime atZone(ZoneId zone) {
        return ZonedDateTime.wrap(delegate.atZone(zone != null ? zone.unwrap() : j$.time.ZoneId.systemDefault()));
    }

    public LocalDate toLocalDate() {
        return LocalDate.wrap(delegate.toLocalDate());
    }

    public int getYear() {
        return delegate != null ? delegate.getYear() : 0;
    }

    public int getMonthValue() {
        return delegate != null ? delegate.getMonthValue() : 0;
    }

    public int getDayOfMonth() {
        return delegate != null ? delegate.getDayOfMonth() : 0;
    }

    public int getHour() {
        return delegate != null ? delegate.getHour() : 0;
    }

    public int getMinute() {
        return delegate != null ? delegate.getMinute() : 0;
    }

    public int getSecond() {
        return delegate != null ? delegate.getSecond() : 0;
    }

    public LocalDateTime plusDays(long days) {
        return wrap(delegate != null ? delegate.plusDays(days) : null);
    }

    public LocalDateTime minusDays(long days) {
        return wrap(delegate != null ? delegate.minusDays(days) : null);
    }

    public LocalDateTime plusHours(long hours) {
        return wrap(delegate != null ? delegate.plusHours(hours) : null);
    }

    public LocalDateTime minusHours(long hours) {
        return wrap(delegate != null ? delegate.minusHours(hours) : null);
    }

    public LocalDateTime plusMinutes(long minutes) {
        return wrap(delegate != null ? delegate.plusMinutes(minutes) : null);
    }

    public LocalDateTime minusMinutes(long minutes) {
        return wrap(delegate != null ? delegate.minusMinutes(minutes) : null);
    }

    public LocalDateTime plusSeconds(long seconds) {
        return wrap(delegate != null ? delegate.plusSeconds(seconds) : null);
    }

    public LocalDateTime minusSeconds(long seconds) {
        return wrap(delegate != null ? delegate.minusSeconds(seconds) : null);
    }

    public j$.time.LocalDateTime unwrap() {
        return delegate;
    }

    public static LocalDateTime wrap(j$.time.LocalDateTime d) {
        return d != null ? new LocalDateTime(d) : null;
    }

    @Override
    public int compareTo(LocalDateTime other) {
        if (delegate == null) return other.delegate == null ? 0 : -1;
        if (other.delegate == null) return 1;
        return delegate.compareTo(other.delegate);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof LocalDateTime)) return false;
        LocalDateTime other = (LocalDateTime) obj;
        return delegate != null ? delegate.equals(other.delegate) : other.delegate == null;
    }

    @Override
    public int hashCode() {
        return delegate != null ? delegate.hashCode() : 0;
    }

    @Override
    public String toString() {
        return delegate != null ? delegate.toString() : "";
    }
}
