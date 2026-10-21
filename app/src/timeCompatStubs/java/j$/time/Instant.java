package j$.time;

import j$.time.temporal.Temporal;
import j$.time.temporal.TemporalAccessor;

public final class Instant implements Temporal, TemporalAccessor, Comparable<Instant> {
    public static Instant ofEpochMilli(long epochMilli) {
        return null;
    }

    public static Instant ofEpochSecond(long epochSecond) {
        return null;
    }

    public static Instant ofEpochSecond(long epochSecond, long nanoAdjustment) {
        return null;
    }

    public static Instant now() {
        return null;
    }

    public static Instant parse(CharSequence text) {
        return null;
    }

    public long toEpochMilli() {
        return 0L;
    }

    public long getEpochSecond() {
        return 0L;
    }

    public int getNano() {
        return 0;
    }

    @Override
    public int compareTo(Instant o) {
        return 0;
    }
}
