package java.time;

public final class Duration implements Comparable<Duration> {
    final j$.time.Duration delegate;

    Duration(j$.time.Duration delegate) {
        this.delegate = delegate;
    }

    public static Duration ofMillis(long millis) {
        return wrap(j$.time.Duration.ofMillis(millis));
    }

    public static Duration ofSeconds(long seconds) {
        return wrap(j$.time.Duration.ofSeconds(seconds));
    }

    public static Duration ofMinutes(long minutes) {
        return wrap(j$.time.Duration.ofMinutes(minutes));
    }

    public static Duration ofHours(long hours) {
        return wrap(j$.time.Duration.ofHours(hours));
    }

    public static Duration ofDays(long days) {
        return wrap(j$.time.Duration.ofDays(days));
    }

    public static Duration between(java.time.temporal.TemporalAccessor startInclusive, java.time.temporal.TemporalAccessor endExclusive) {
        if (startInclusive instanceof Instant && endExclusive instanceof Instant) {
            return wrap(j$.time.Duration.between(((Instant) startInclusive).unwrap(), ((Instant) endExclusive).unwrap()));
        }
        try {
            return wrap(j$.time.Duration.ZERO);
        } catch (Throwable t) {
            return ofSeconds(0);
        }
    }

    public long toMillis() {
        return delegate != null ? delegate.toMillis() : 0L;
    }

    public long getSeconds() {
        return delegate != null ? delegate.getSeconds() : 0L;
    }

    public long toMinutes() {
        return delegate != null ? delegate.toMinutes() : 0L;
    }

    public long toHours() {
        return delegate != null ? delegate.toHours() : 0L;
    }

    public long toDays() {
        return delegate != null ? delegate.toDays() : 0L;
    }

    public j$.time.Duration unwrap() {
        return delegate;
    }

    public static Duration wrap(j$.time.Duration d) {
        return d != null ? new Duration(d) : null;
    }

    @Override
    public int compareTo(Duration other) {
        if (delegate == null) return other.delegate == null ? 0 : -1;
        if (other.delegate == null) return 1;
        return delegate.compareTo(other.delegate);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Duration)) return false;
        Duration other = (Duration) obj;
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
