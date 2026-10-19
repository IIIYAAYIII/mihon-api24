package java.time;

import java.time.chrono.ChronoZonedDateTime;
import java.time.format.DateTimeFormatter;

public final class ZonedDateTime implements ChronoZonedDateTime<LocalDate> {
    final j$.time.ZonedDateTime delegate;

    ZonedDateTime(j$.time.ZonedDateTime delegate) {
        this.delegate = delegate;
    }

    public static ZonedDateTime parse(CharSequence text) {
        return wrap(j$.time.ZonedDateTime.parse(text));
    }

    public static ZonedDateTime parse(CharSequence text, DateTimeFormatter formatter) {
        if (formatter == null) {
            return parse(text);
        }
        return wrap(j$.time.ZonedDateTime.parse(text, formatter.unwrap()));
    }

    public static ZonedDateTime now() {
        return wrap(j$.time.ZonedDateTime.now());
    }

    public static ZonedDateTime now(ZoneId zone) {
        return wrap(j$.time.ZonedDateTime.now(zone != null ? zone.unwrap() : j$.time.ZoneId.systemDefault()));
    }

    public static ZonedDateTime ofInstant(Instant instant, ZoneId zone) {
        return wrap(j$.time.ZonedDateTime.ofInstant(
            instant != null ? instant.unwrap() : j$.time.Instant.now(),
            zone != null ? zone.unwrap() : j$.time.ZoneId.systemDefault()
        ));
    }

    @Override
    public Instant toInstant() {
        return Instant.wrap(delegate != null ? delegate.toInstant() : null);
    }

    @Override
    public long toEpochSecond() {
        return delegate != null ? delegate.toEpochSecond() : 0L;
    }

    public ZoneId getZone() {
        return ZoneId.wrap(delegate != null ? delegate.getZone() : null);
    }

    public LocalDateTime toLocalDateTime() {
        return LocalDateTime.wrap(delegate != null ? delegate.toLocalDateTime() : null);
    }

    public LocalDate toLocalDate() {
        return LocalDate.wrap(delegate != null ? delegate.toLocalDate() : null);
    }

    public ZonedDateTime withZoneSameInstant(ZoneId zone) {
        return wrap(delegate != null ? delegate.withZoneSameInstant(zone != null ? zone.unwrap() : j$.time.ZoneId.systemDefault()) : null);
    }

    public ZonedDateTime plusDays(long days) {
        return wrap(delegate != null ? delegate.plusDays(days) : null);
    }

    public ZonedDateTime minusDays(long days) {
        return wrap(delegate != null ? delegate.minusDays(days) : null);
    }

    public ZonedDateTime plusHours(long hours) {
        return wrap(delegate != null ? delegate.plusHours(hours) : null);
    }

    public ZonedDateTime minusHours(long hours) {
        return wrap(delegate != null ? delegate.minusHours(hours) : null);
    }

    public ZonedDateTime plusMinutes(long minutes) {
        return wrap(delegate != null ? delegate.plusMinutes(minutes) : null);
    }

    public ZonedDateTime minusMinutes(long minutes) {
        return wrap(delegate != null ? delegate.minusMinutes(minutes) : null);
    }

    public ZonedDateTime plusSeconds(long seconds) {
        return wrap(delegate != null ? delegate.plusSeconds(seconds) : null);
    }

    public ZonedDateTime minusSeconds(long seconds) {
        return wrap(delegate != null ? delegate.minusSeconds(seconds) : null);
    }

    public j$.time.ZonedDateTime unwrap() {
        return delegate;
    }

    public static ZonedDateTime wrap(j$.time.ZonedDateTime d) {
        return d != null ? new ZonedDateTime(d) : null;
    }

    @Override
    public int compareTo(ChronoZonedDateTime<?> other) {
        if (other instanceof ZonedDateTime) {
            ZonedDateTime o = (ZonedDateTime) other;
            if (delegate == null) return o.delegate == null ? 0 : -1;
            if (o.delegate == null) return 1;
            return delegate.compareTo(o.delegate);
        }
        return Long.compare(toEpochSecond(), other != null ? other.toEpochSecond() : 0L);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ZonedDateTime)) return false;
        ZonedDateTime other = (ZonedDateTime) obj;
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
