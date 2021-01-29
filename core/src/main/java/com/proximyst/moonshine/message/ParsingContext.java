//
// moonshine - A localisation library for Java.
// Copyright (C) 2021 Mariell Hoversholm
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU Lesser General Public License as published
// by the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public License
// along with this program.  If not, see <https://www.gnu.org/licenses/>.
//

package com.proximyst.moonshine.message;

import java.util.Map;

public final class ParsingContext<R> {
  private final Map<String, String> placeholders;
  private final R receiver;

  public ParsingContext(final Map<String, String> placeholders, final R receiver) {
    this.placeholders = placeholders;
    this.receiver = receiver;
  }

  public Map<String, String> placeholders() {
    return this.placeholders;
  }

  public R receiver() {
    return this.receiver;
  }
}