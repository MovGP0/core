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
package cc.redberry.core.tensor;

import cc.redberry.core.indices.IndicesFactory;
import cc.redberry.core.utils.*;

/**
 *
 * @author Dmitry Bolotin
 * @author Stanislav Poslavsky
 */
public class ExpressionFactory implements TensorFactory {

    public static final ExpressionFactory FACTORY = new ExpressionFactory();

    private ExpressionFactory() {
    }

    @Override
    public Expression create(Tensor... tensors) {
        if (tensors.length != 2)
            throw new IllegalArgumentException("Wrong number of arguments.");
        if (tensors[0] == null || tensors[1] == null)
            throw new NullPointerException();
        if (!tensors[0].getIndices().getFreeIndices().equalsRegardlessOrder(tensors[1].getIndices().getFreeIndices()) && !TensorUtils.isZero(tensors[1]))
            throw new TensorException("Inconsistent indices in expression.");
        return new Expression(IndicesFactory.createSorted(tensors[0].getIndices().getFreeIndices()), tensors[0], tensors[1]);
    }    
}
