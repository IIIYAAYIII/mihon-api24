package java.time.format;

import java.util.Locale;

public final class DateTimeFormatterBuilder {
    final j$.time.format.DateTimeFormatterBuilder delegate;

    public DateTimeFormatterBuilder() {
        this.delegate = new j$.time.format.DateTimeFormatterBuilder();
    }

    public DateTimeFormatterBuilder appendPattern(String pattern) {
        delegate.appendPattern(pattern);
        return this;
    }

    public DateTimeFormatterBuilder appendLiteral(String literal) {
        delegate.appendLiteral(literal);
        return this;
    }

    public DateTimeFormatterBuilder appendLiteral(char literal) {
        delegate.appendLiteral(literal);
        return this;
    }

    public DateTimeFormatterBuilder optionalStart() {
        delegate.optionalStart();
        return this;
    }

    public DateTimeFormatterBuilder optionalEnd() {
        delegate.optionalEnd();
        return this;
    }

    public DateTimeFormatterBuilder parseCaseInsensitive() {
        delegate.parseCaseInsensitive();
        return this;
    }

    public DateTimeFormatterBuilder parseCaseSensitive() {
        delegate.parseCaseSensitive();
        return this;
    }

    public DateTimeFormatterBuilder parseLenient() {
        delegate.parseLenient();
        return this;
    }

    public DateTimeFormatterBuilder parseStrict() {
        delegate.parseStrict();
        return this;
    }

    public DateTimeFormatter toFormatter() {
        return DateTimeFormatter.wrap(delegate.toFormatter());
    }

    public DateTimeFormatter toFormatter(Locale locale) {
        return DateTimeFormatter.wrap(delegate.toFormatter(locale));
    }
}
