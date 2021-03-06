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
package cc.redberry.core.tensor;

import cc.redberry.core.context.OutputFormat;
import cc.redberry.core.indices.Indices;

/**
 * Technical class.
 *
 * @author Dmitry Bolotin
 * @author Stanislav Poslavsky
 * @since 1.0
 */
public final class TensorWrapper extends Tensor {

    private final Tensor innerTensor;

    private TensorWrapper(Tensor innerTensor) {
        this.innerTensor = innerTensor;
    }

    @Override
    public Tensor get(int i) {
        if (i != 0)
            throw new IndexOutOfBoundsException();
        return innerTensor;
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public TensorBuilder getBuilder() {
        return new Builder();
    }

    @Override
    public TensorFactory getFactory() {
        return Factory.FACTORY;
    }

    private static final class Builder implements TensorBuilder {

        private Tensor innerTensor;

        Builder() {
        }

        Builder(Tensor innerTensor) {
            this.innerTensor = innerTensor;
        }

        @Override
        public Tensor build() {
            if (innerTensor == null)
                throw new IllegalStateException("No elements added.");
            return new TensorWrapper(innerTensor);
        }

        @Override
        public void put(Tensor tensor) {
            if (tensor == null)
                throw new NullPointerException();
            if (innerTensor != null)
                throw new TensorException("Wrapper have only one element!");
            innerTensor = tensor;
        }

        @Override
        public TensorBuilder clone() {
            return new Builder(innerTensor);
        }
    }

    private static final class Factory implements TensorFactory {

        private static final Factory FACTORY = new Factory();

        private Factory() {
        }

        @Override
        public Tensor create(Tensor... tensors) {
            if (tensors.length != 1)
                throw new IllegalArgumentException();
            if (tensors[0] == null)
                throw new NullPointerException();
            return new TensorWrapper(tensors[0]);
        }
    }

    public static TensorWrapper wrap(Tensor tensor) {
        return new TensorWrapper(tensor);
    }

    @Override
    public Indices getIndices() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected int hash() {
        return -1;
    }

    @Override
    public String toString(OutputFormat mode) {
        return "@[" + innerTensor.toString(mode) + "]";
    }
}
