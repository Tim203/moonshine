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
package net.kyori.moonshine.util;

import net.kyori.moonshine.annotation.meta.ThreadSafe;

import java.util.Objects;

/**
 * A weighted value.
 */
@ThreadSafe
public final class Weighted<V> implements Comparable<Weighted<V>> {
  private final V value;
  private final int weight;

  public Weighted(final V value, final int weight) {
    this.value = value;
    this.weight = weight;
  }

  @Override
  public int compareTo(final Weighted<V> o) {
    return Integer.compare(this.weight(), o.weight());
  }

  public V value() {
    return this.value;
  }

  public int weight() {
    return this.weight;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    final Weighted<?> that = (Weighted<?>) obj;
    return Objects.equals(this.value, that.value) &&
            this.weight == that.weight;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.value, this.weight);
  }

  @Override
  public String toString() {
    return "Weighted[" +
            "value=" + this.value + ", " +
            "weight=" + this.weight + ']';
  }

}
