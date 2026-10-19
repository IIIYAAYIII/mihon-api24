package java.time;

import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;

public final class LocalDate implements ChronoLocalDate, Comparable<ChronoLocalDate> {
    final j$.time.LocalDate delegate;

    LocalDate(j$.time.LocalDate delegate) {
        this.delegate = delegate;
    }

    public static LocalDate parse(CharSequence text) {
        return wrap(j$.time.LocalDate.parse(text));
    }

    public static LocalDate parse(CharSequence text, DateTimeFormatter formatter) {
        if (formatter == null) {
            return parse(text);
        }
        return wrap(j$.time.LocalDate.parse(text, formatter.unwrap()));
    }

    public static LocalDate now() {
        return wrap(j$.time.LocalDate.now());
    }

    public static LocalDate now(ZoneId zone) {
        return wrap(j$.time.LocalDate.now(zone != null ? zone.unwrap() : j$.time.ZoneId.systemDefault()));
    }

    public static LocalDate of(int year, int month, int dayOfMonth) {
        return wrap(j$.time.LocalDate.of(year, month, dayOfMonth));
    }

    public LocalDateTime atStartOfDay() {
        return LocalDateTime.wrap(delegate.atStartOfDay());
    }

    public ZonedDateTime atStartOfDay(ZoneId zone) {
        return ZonedDateTime.wrap(delegate.atStartOfDay(zone != null ? zone.unwrap() : j$.time.ZoneId.systemDefault()));
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

    public int getDayOfWeek() {
        return delegate != null ? delegate.getDayOfWeek().getValue() : 0;
    }

    public LocalDate plusDays(long days) {
        return wrap(delegate != null ? delegate.plusDays(days) : null);
    }

    public LocalDate minusDays(long days) {
        return wrap(delegate != null ? delegate.minusDays(days) : null);
    }

    public LocalDate plusWeeks(long weeks) {
        return wrap(delegate != null ? delegate.plusWeeks(weeks) : null);
    }

    public LocalDate minusWeeks(long weeks) {
        return wrap(delegate != null ? delegate.minusWeeks(weeks) : null);
    }

    public LocalDate plusMonths(long months) {
        return wrap(delegate != null ? delegate.plusMonths(months) : null);
    }

    public LocalDate minusMonths(long months) {
        return wrap(delegate != null ? delegate.minusMonths(months) : null);
    }

    public LocalDate plusYears(long years) {
        return wrap(delegate != null ? delegate.plusYears(years) : null);
    }

    public LocalDate minusYears(long years) {
        return wrap(delegate != null ? delegate.minusYears(years) : null);
    }

    public j$.time.LocalDate unwrap() {
        return delegate;
    }

    public static LocalDate wrap(j$.time.LocalDate d) {
        return d != null ? new LocalDate(d) : null;
    }

    @Override
    public int compareTo(ChronoLocalDate other) {
        if (other instanceof LocalDate) {
            LocalDate o = (LocalDate) other;
            if (delegate == null) return o.delegate == null ? 0 : -1;
            if (o.delegate == null) return 1;
            return delegate.compareTo(o.delegate);
        }
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof LocalDate)) return false;
        LocalDate other = (LocalDate) obj;
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
