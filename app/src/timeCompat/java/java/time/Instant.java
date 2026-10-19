package java.time;

import java.time.temporal.TemporalAccessor;

public final class Instant implements TemporalAccessor, Comparable<Instant> {
    final j$.time.Instant delegate;

    private Instant(j$.time.Instant delegate) {
        this.delegate = delegate;
    }

    public static Instant ofEpochMilli(long epochMilli) {
        return wrap(j$.time.Instant.ofEpochMilli(epochMilli));
    }

    public static Instant ofEpochSecond(long epochSecond) {
        return wrap(j$.time.Instant.ofEpochSecond(epochSecond));
    }

    public static Instant ofEpochSecond(long epochSecond, long nanoAdjustment) {
        return wrap(j$.time.Instant.ofEpochSecond(epochSecond, nanoAdjustment));
    }

    public static Instant now() {
        return wrap(j$.time.Instant.now());
    }

    public static Instant parse(CharSequence text) {
        return wrap(j$.time.Instant.parse(text));
    }

    public long toEpochMilli() {
        return delegate != null ? delegate.toEpochMilli() : 0L;
    }

    public long getEpochSecond() {
        return delegate != null ? delegate.getEpochSecond() : 0L;
    }

    public int getNano() {
        return delegate != null ? delegate.getNano() : 0;
    }

    public j$.time.Instant unwrap() {
        return delegate;
    }

    public static Instant wrap(j$.time.Instant d) {
        return d != null ? new Instant(d) : null;
    }

    @Override
    public int compareTo(Instant other) {
        if (delegate == null) return other.delegate == null ? 0 : -1;
        if (other.delegate == null) return 1;
        return delegate.compareTo(other.delegate);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Instant)) return false;
        Instant other = (Instant) obj;
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
