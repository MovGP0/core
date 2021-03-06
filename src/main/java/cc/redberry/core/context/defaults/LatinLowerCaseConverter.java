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
package cc.redberry.core.context.defaults;

import cc.redberry.core.context.IndexConverterException;
import cc.redberry.core.context.IndexSymbolConverter;
import cc.redberry.core.context.OutputFormat;

/**
 * {@link IndexSymbolConverter} for latin lower case letters.
 *
 * @author Dmitry Bolotin
 * @author Stanislav Poslavsky
 * @since 1.0
 */
public final class LatinLowerCaseConverter implements IndexSymbolConverter {
    /**
     * Index type = 0
     */
    public static final byte TYPE = 0;
    /**
     * Singleton instance
     */
    public static final LatinLowerCaseConverter INSTANCE = new LatinLowerCaseConverter();

    private LatinLowerCaseConverter() {
    }

    @Override
    public boolean applicableToSymbol(String symbol) {
        if (symbol.length() == 1) {
            char sym = symbol.charAt(0);
            if (sym >= 0x61 && sym <= 0x7A)
                return true;
        }
        return false;
    }

    @Override
    public int getCode(String symbol) {
        return symbol.charAt(0) - 0x61;
    }

    @Override
    public String getSymbol(int code, OutputFormat mode) throws IndexConverterException {
        int number = code + 0x61;
        if (number > 0x7A)
            throw new IndexConverterException();
        return Character.toString((char) number);
    }

    /**
     * @return 0
     */
    @Override
    public byte getType() {
        return TYPE;
    }

    @Override
    public int maxNumberOfSymbols() {
        return 25;
    }
}
