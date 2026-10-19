package java.time;

public class ZoneId {
    final j$.time.ZoneId delegate;

    ZoneId(j$.time.ZoneId delegate) {
        this.delegate = delegate;
    }

    public static ZoneId systemDefault() {
        return wrap(j$.time.ZoneId.systemDefault());
    }

    public static ZoneId of(String zoneId) {
        return wrap(j$.time.ZoneId.of(zoneId));
    }

    public String getId() {
        return delegate != null ? delegate.getId() : "";
    }

    public j$.time.ZoneId unwrap() {
        return delegate;
    }

    public static ZoneId wrap(j$.time.ZoneId d) {
        return d != null ? new ZoneId(d) : null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ZoneId)) return false;
        ZoneId other = (ZoneId) obj;
        return delegate != null ? delegate.equals(other.delegate) : other.delegate == null;
    }

    @Override
    public int hashCode() {
        return delegate != null ? delegate.hashCode() : 0;
    }

    @Override
    public String toString() {
        return delegate != null ? delegate.toString() : "ZoneId";
    }
}
