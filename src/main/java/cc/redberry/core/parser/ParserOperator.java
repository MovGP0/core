/*
 * Redberry: symbolic tensor computations.
 *
 * Copyright (c) 2010-2012:
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
package cc.redberry.core.parser;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dmitry Bolotin
 * @author Stanislav Poslavsky
 */
abstract class ParserOperator implements NodeParser {
    private char operatorSymbol;
    private char operatorInverseSymbol;

    protected ParserOperator(char operatorSymbol, char operatorInverseSymbol) {
        this.operatorSymbol = operatorSymbol;
        this.operatorInverseSymbol = operatorInverseSymbol;
    }

    protected final boolean canParse(String expression) {
        char[] expressionChars = expression.toCharArray();
        int level = 0;
        for (char c : expressionChars) {
            if (c == '(' || c == '[')
                level++;
            if (c == ')' || c == ']')
                level--;
            if (level < 0)
                throw new BracketsError();
            if (c == operatorSymbol && level == 0)
                return true;
            if (c == operatorInverseSymbol && level == 0)
                return true;
        }
        return false;
    }

    private enum Mode {
        Direct, Inverse
    };

    @Override
    public ParseNode parseNode(String expression, Parser parser) {
        if (!canParse(expression))
            return null;
        char[] expressionChars = expression.toCharArray();

        StringBuffer buffer = new StringBuffer();
        List<ParseNode> nodes = new ArrayList<>();
        int level = 0, indicesLevel = 0;
        Mode mode = Mode.Direct;

        for (char c : expressionChars) {
            if (c == '(' || c == '[')
                level++;
            if (c == '{')
                indicesLevel++;
            if (c == '}')
                indicesLevel--;
            if (c == ')' || c == ']')
                level--;
            if (level < 0)
                throw new BracketsError();
            if (c == ' ' && indicesLevel == 0)
                continue;
            if (c == operatorSymbol && level == 0) {
                String toParse = buffer.toString();
                if (!toParse.isEmpty())
                    modeParser(toParse, mode, parser, nodes);
                buffer = new StringBuffer();
                mode = Mode.Direct;
            } else if (c == operatorInverseSymbol && level == 0) {
                String toParse = buffer.toString();
                if (!toParse.isEmpty())
                    modeParser(toParse, mode, parser, nodes);
                buffer = new StringBuffer();
                mode = Mode.Inverse;
            } else
                buffer.append(c);
        }
        modeParser(buffer.toString(), mode, parser, nodes);
        return compile(nodes);
    }

    private void modeParser(String expression, Mode mode, Parser parser, List<ParseNode> nodes) {
        if (mode == Mode.Direct) {
            nodes.add(parser.parse(expression));
            return;
        }
        if (mode == Mode.Inverse)
            nodes.add(inverseOperation(parser.parse(expression)));
        else
            throw new ParserException("unrepoted operator parser mode");
    }

    protected abstract ParseNode compile(List<ParseNode> nodes);

    protected abstract ParseNode inverseOperation(ParseNode tensor);
}