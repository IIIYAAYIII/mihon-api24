package java.time.format;

import java.time.ZoneId;
import java.util.Locale;

public final class DateTimeFormatter {
    final j$.time.format.DateTimeFormatter delegate;

    DateTimeFormatter(j$.time.format.DateTimeFormatter delegate) {
        this.delegate = delegate;
    }

    public static DateTimeFormatter ofPattern(String pattern) {
        return wrap(j$.time.format.DateTimeFormatter.ofPattern(pattern));
    }

    public static DateTimeFormatter ofPattern(String pattern, Locale locale) {
        return wrap(j$.time.format.DateTimeFormatter.ofPattern(pattern, locale));
    }

    public static final DateTimeFormatter ISO_LOCAL_DATE = wrap(j$.time.format.DateTimeFormatter.ISO_LOCAL_DATE);
    public static final DateTimeFormatter ISO_LOCAL_TIME = wrap(j$.time.format.DateTimeFormatter.ISO_LOCAL_TIME);
    public static final DateTimeFormatter ISO_LOCAL_DATE_TIME = wrap(j$.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    public static final DateTimeFormatter ISO_ZONED_DATE_TIME = wrap(j$.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME);
    public static final DateTimeFormatter ISO_DATE_TIME = wrap(j$.time.format.DateTimeFormatter.ISO_DATE_TIME);
    public static final DateTimeFormatter ISO_INSTANT = wrap(j$.time.format.DateTimeFormatter.ISO_INSTANT);
    public static final DateTimeFormatter BASIC_ISO_DATE = wrap(j$.time.format.DateTimeFormatter.BASIC_ISO_DATE);

    public ZoneId getZone() {
        return ZoneId.wrap(delegate != null ? delegate.getZone() : null);
    }

    public DateTimeFormatter withZone(ZoneId zone) {
        return wrap(delegate != null ? delegate.withZone(zone != null ? zone.unwrap() : null) : null);
    }

    public DateTimeFormatter withLocale(Locale locale) {
        return wrap(delegate != null ? delegate.withLocale(locale) : null);
    }

    public String format(java.time.temporal.TemporalAccessor temporal) {
        if (delegate == null || temporal == null) return "";
        if (temporal instanceof java.time.LocalDate) {
            return delegate.format(((java.time.LocalDate) temporal).unwrap());
        }
        if (temporal instanceof java.time.LocalDateTime) {
            return delegate.format(((java.time.LocalDateTime) temporal).unwrap());
        }
        if (temporal instanceof java.time.ZonedDateTime) {
            return delegate.format(((java.time.ZonedDateTime) temporal).unwrap());
        }
        if (temporal instanceof java.time.Instant) {
            return delegate.format(((java.time.Instant) temporal).unwrap());
        }
        return temporal.toString();
    }

    public j$.time.format.DateTimeFormatter unwrap() {
        return delegate;
    }

    public static DateTimeFormatter wrap(j$.time.format.DateTimeFormatter d) {
        return d != null ? new DateTimeFormatter(d) : null;
    }

    @Override
    public String toString() {
        return delegate != null ? delegate.toString() : "";
    }
}
