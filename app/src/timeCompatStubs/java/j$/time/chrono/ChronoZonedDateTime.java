package j$.time.chrono;

import j$.time.temporal.Temporal;
import j$.time.temporal.TemporalAccessor;

public interface ChronoZonedDateTime<D extends ChronoLocalDate> extends Temporal, TemporalAccessor, Comparable<ChronoZonedDateTime<?>> {
}
