/*
 * Redberry: symbolic tensor computations.
 *
 * Copyright (c) 2010-2013:
 *   Stanislav Poslavsky   <stvlpos@mail.ru>
 *   Bolotin Dmitriy       <bolotin.dmitriy@gmail.com>
 *
 * This file is part of Redberry.
 *
 * Redberry is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Redberry is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Redberry. If not, see <http://www.gnu.org/licenses/>.
 */
package cc.redberry.core.utils.stretces;

import java.util.Iterator;

/**
 *
 * @author Dmitry Bolotin
 * @author Stanislav Poslavsky
 */
public class StretchIteratorS implements Iterator<Stretch>, Iterable<Stretch> {
    private final short[] values;
    private int pointer = 0;

    public StretchIteratorS(short[] values) {
        this.values = values;
    }

    @Override
    public boolean hasNext() {
        return pointer < values.length;
    }

    @Override
    public Stretch next() {
        int i = pointer;
        final int value = values[i];
        final int begin = i;
        while ((++i < values.length) && values[i] == value);
        pointer = i;
        return new Stretch(begin, i - begin);
    }

    @Override
    public Iterator<Stretch> iterator() {
        return this;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
