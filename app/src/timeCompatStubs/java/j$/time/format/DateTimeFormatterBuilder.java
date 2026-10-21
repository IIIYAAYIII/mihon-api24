package j$.time.format;

import java.util.Locale;

public final class DateTimeFormatterBuilder {
    public DateTimeFormatterBuilder() {}

    public DateTimeFormatterBuilder appendPattern(String pattern) {
        return this;
    }

    public DateTimeFormatterBuilder appendInstant() {
        return this;
    }

    public DateTimeFormatterBuilder appendInstant(int fractionalDigits) {
        return this;
    }

    public DateTimeFormatterBuilder appendLiteral(String literal) {
        return this;
    }

    public DateTimeFormatterBuilder appendLiteral(char literal) {
        return this;
    }

    public DateTimeFormatterBuilder optionalStart() {
        return this;
    }

    public DateTimeFormatterBuilder optionalEnd() {
        return this;
    }

    public DateTimeFormatterBuilder parseCaseInsensitive() {
        return this;
    }

    public DateTimeFormatterBuilder parseCaseSensitive() {
        return this;
    }

    public DateTimeFormatterBuilder parseLenient() {
        return this;
    }

    public DateTimeFormatterBuilder parseStrict() {
        return this;
    }

    public DateTimeFormatter toFormatter() {
        return null;
    }

    public DateTimeFormatter toFormatter(Locale locale) {
        return null;
    }
}
