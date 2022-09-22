/*
 * moonshine - A localisation library for Java.
 * Copyright (C) Mariell Hoversholm
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package net.kyori.moonshine.message;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Function;

/**
 * A standard formatter for strings using {@link String#replace(CharSequence, CharSequence)} with {@code
 * "${prefix}${value}${suffix}"} where {@code value} is converted using {@link #placeholderValueToStringConverter}.
 * <p>
 * Most people will want a more advanced formatter for Discord messages/embeds, Minecraft component messages, etc.
 */
public final class StringMessageRenderer<R, I, O, F> implements IMessageRenderer<R, I, O, F> {
    private final String prefix;
    private final String suffix;
    private final Function<I, String> intermediateToStringConverter;
    private final Function<String, O> stringToOutputConverter;
    private final Function<F, String> placeholderValueToStringConverter;

    /**
     *
     */
    public StringMessageRenderer(
            final String prefix,
            final String suffix,
            final Function<I, String> intermediateToStringConverter,
            final Function<String, O> stringToOutputConverter,
            final Function<F, String> placeholderValueToStringConverter
    ) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.intermediateToStringConverter = intermediateToStringConverter;
        this.stringToOutputConverter = stringToOutputConverter;
        this.placeholderValueToStringConverter = placeholderValueToStringConverter;
    }

    @Override
    public O render(
            final R receiver,
            final I intermediateMessage,
            final Map<String, ? extends F> resolvedPlaceholders,
            final Method method,
            final Type owner
    ) {
        String intermediate = this.intermediateToStringConverter.apply(intermediateMessage);
        for (final Entry<String, ? extends F> entry : resolvedPlaceholders.entrySet()) {
            intermediate = intermediate.replace(this.prefix + entry.getKey() + this.suffix,
                    this.placeholderValueToStringConverter.apply(entry.getValue()));
        }
        return this.stringToOutputConverter.apply(intermediate);
    }

    public String prefix() {
        return this.prefix;
    }

    public String suffix() {
        return this.suffix;
    }

    public Function<I, String> intermediateToStringConverter() {
        return this.intermediateToStringConverter;
    }

    public Function<String, O> stringToOutputConverter() {
        return this.stringToOutputConverter;
    }

    public Function<F, String> placeholderValueToStringConverter() {
        return this.placeholderValueToStringConverter;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        StringMessageRenderer that = (StringMessageRenderer) obj;
        return Objects.equals(this.prefix, that.prefix) &&
                Objects.equals(this.suffix, that.suffix) &&
                Objects.equals(this.intermediateToStringConverter, that.intermediateToStringConverter) &&
                Objects.equals(this.stringToOutputConverter, that.stringToOutputConverter) &&
                Objects.equals(this.placeholderValueToStringConverter, that.placeholderValueToStringConverter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.prefix, this.suffix, this.intermediateToStringConverter, this.stringToOutputConverter, this.placeholderValueToStringConverter);
    }

    @Override
    public String toString() {
        return "StringMessageRenderer[" +
                "prefix=" + this.prefix + ", " +
                "suffix=" + this.suffix + ", " +
                "intermediateToStringConverter=" + this.intermediateToStringConverter + ", " +
                "stringToOutputConverter=" + this.stringToOutputConverter + ", " +
                "placeholderValueToStringConverter=" + this.placeholderValueToStringConverter + ']';
    }

}
