package java.time.chrono;

import java.time.Instant;
import java.time.temporal.TemporalAccessor;

public interface ChronoZonedDateTime<D extends ChronoLocalDate> extends TemporalAccessor, Comparable<ChronoZonedDateTime<?>> {
    Instant toInstant();
    long toEpochSecond();
}
