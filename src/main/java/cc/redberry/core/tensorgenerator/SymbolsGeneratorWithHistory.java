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
package cc.redberry.core.tensorgenerator;

import cc.redberry.concurrent.OutputPortUnsafe;
import cc.redberry.core.tensor.SimpleTensor;
import cc.redberry.core.tensor.Tensor;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dmitry Bolotin
 * @author Stanislav Poslavsky
 */
public class SymbolsGeneratorWithHistory implements OutputPortUnsafe<Tensor> {

    final SymbolsGenerator generator;
    final List<SimpleTensor> generated = new ArrayList<>();

    public SymbolsGeneratorWithHistory(String name, Tensor... forbiddenTensors) {
        generator = new SymbolsGenerator(name, forbiddenTensors);
    }

    public SymbolsGeneratorWithHistory(String name) {
        generator = new SymbolsGenerator(name);
    }

    @Override
    public SimpleTensor take() {
        SimpleTensor st = generator.take();
        generated.add(st);
        return st;
    }
}