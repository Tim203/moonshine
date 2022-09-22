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
package net.kyori.moonshine.internal;

import io.leangen.geantyref.GenericTypeReflector;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * Utilities for handling reflective operations.
 */
public final class ReflectiveUtils {
  private static final ConcurrentMap<Method, MethodHandle> METHOD_CACHE = new ConcurrentHashMap<>();
  private static final Constructor<Lookup> LOOKUP_CONSTRUCTOR;

  static {
    try {
      LOOKUP_CONSTRUCTOR = Lookup.class.getDeclaredConstructor(Class.class);
      LOOKUP_CONSTRUCTOR.setAccessible(true);
    } catch (final NoSuchMethodException ex) {
      ThrowableUtils.sneakyThrow(ex);
      throw new RuntimeException(ex);
    }
  }

  private ReflectiveUtils() {
  }

  /**
   * Find a single {@code default} method for the given interfaces of a {@link java.lang.reflect.Proxy proxy}.
   *
   * @param method the method to find
   * @param proxy  the proxy to find the method within
   * @return the found method
   * @throws IllegalAccessException if the method is inaccessible
   */
  @SuppressWarnings({"RedundantThrows", "java:S1130"}) // This is sneakily thrown.
  public static MethodHandle findMethod(final Method method, final Object proxy)
      throws IllegalAccessException {
    final Class<?> type = method.getDeclaringClass();

    return METHOD_CACHE.computeIfAbsent(method, methodParam -> {
      try {
        return LOOKUP_CONSTRUCTOR.newInstance(type)
                .in(type)
                .unreflectSpecial(method, type)
                .bindTo(proxy);
      } catch (final ReflectiveOperationException ex) {
        ThrowableUtils.sneakyThrow(ex);
        throw new RuntimeException(ex);
      }
    });
  }

  /**
   * Formats a method name in the following style: {@code a.b.c.Owner<T>#methodName(T, String,
   * int)a.b.c.ReturnType<R>}.
   *
   * @param owner  the owner/declaring type of this method
   * @param method the method to format
   * @return the formatted method descriptor
   */
  public static String formatMethodName(final Type owner, final Method method) {
    return GenericTypeReflector.getTypeName(owner)
        + '#'
        + method.getName()
        + formatMethodTypeParameters(method)
        + '('
        + formatMethodParameters(method)
        + ')'
        + GenericTypeReflector.getTypeName(method.getGenericReturnType());
  }

  private static String formatMethodParameters(final Method method) {
    if (method.getParameterCount() == 0) {
      return "";
    }

    return Arrays.stream(method.getGenericParameterTypes())
        .map(GenericTypeReflector::getTypeName)
        .collect(Collectors.joining(", "));
  }

  private static String formatMethodTypeParameters(final Method method) {
    final TypeVariable<Method>[] typeParameters = method.getTypeParameters();
    if (typeParameters.length == 0) {
      return "";
    }

    return Arrays.stream(typeParameters)
        .map(GenericTypeReflector::getTypeName)
        .collect(Collectors.joining(", ", "<", ">"));
  }
}
