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

import cc.redberry.core.context.*;
import cc.redberry.core.indices.*;
import cc.redberry.core.tensor.functions.*;

/**
 *
 * @author Dmitry Bolotin
 * @author Stanislav Poslavsky
 */
public class TensorsFactory {

    public static Tensor pow(Tensor argument, Tensor power) {
        PowerBuilder pb = new PowerBuilder();
        pb.put(argument);
        pb.put(power);
        return pb.buid();
    }

    public static Tensor multiply(Tensor... factors) {
        ProductBuilder pb = new ProductBuilder();
        for (Tensor t : factors)
            pb.put(t);
        return pb.buid();
    }

    public static Tensor sum(Tensor... tensors) {
        SumBuilder sb = new SumBuilder();
        for (Tensor t : tensors)
            sb.put(t);
        return sb.buid();
    }

    public static SimpleTensor simpleTensor(String name, SimpleIndices indices) {
        NameDescriptor descriptor = CC.getNameManager().mapNameDescriptor(name, indices.getIndicesTypeStructure());
        return new SimpleTensor(descriptor.getId(),
                                UnsafeIndicesFactory.createOfTensor(descriptor.getSymmetries(),
                                                                    indices));
    }

    public static SimpleTensor simpleTensor(int name, SimpleIndices indices) {
        NameDescriptor descriptor = CC.getNameDescriptor(name);
        if (descriptor == null)
            throw new IllegalArgumentException("This name is not registered in the system.");
        return new SimpleTensor(name,
                                UnsafeIndicesFactory.createOfTensor(descriptor.getSymmetries(),
                                                                    indices));
    }

    public static TensorField field(String name, SimpleIndices indices, Tensor[] arguments) {
        SimpleIndices[] argIndices = new SimpleIndices[arguments.length];
        for (int i = 0; i < argIndices.length; ++i)
            argIndices[i] = IndicesFactory.createSimple(null, arguments[i].getIndices().getFreeIndices());
        return field(name, indices, argIndices, arguments);
    }

    public static TensorField field(String name, SimpleIndices indices, SimpleIndices[] argIndices, Tensor[] arguments) {
        if (argIndices.length != arguments.length)
            throw new IllegalArgumentException("Argument indices array and arguments array have different length.");
        if (arguments.length == 0)
            throw new IllegalArgumentException("No arguments in field.");
        for (int i = 0; i < argIndices.length; ++i)
            if (!arguments[i].getIndices().getFreeIndices().equalsIgnoreOrder(argIndices[i]))
                throw new IllegalArgumentException("Arguments indices are inconsistent with arguments.");

        IndicesTypeStructure[] structures = new IndicesTypeStructure[argIndices.length + 1];
        structures[0] = indices.getIndicesTypeStructure();
        for (int i = 0; i < argIndices.length; ++i)
            structures[i + 1] = argIndices[i].getIndicesTypeStructure();
        NameDescriptor descriptor = CC.getNameManager().mapNameDescriptor(name, structures);
        return new TensorField(descriptor.getId(),
                               UnsafeIndicesFactory.createOfTensor(descriptor.getSymmetries(), indices),
                               arguments.clone(), argIndices.clone());
    }

    public static TensorField field(int name, SimpleIndices indices, SimpleIndices[] argIndices, Tensor[] arguments) {
        if (argIndices.length != arguments.length)
            throw new IllegalArgumentException("Argument indices array and arguments array have different length.");
        if (arguments.length == 0)
            throw new IllegalArgumentException("No arguments in field.");
        NameDescriptor descriptor = CC.getNameDescriptor(name);
        if (descriptor == null)
            throw new IllegalArgumentException("This name is not registered in the system.");
        if (!descriptor.isField())
            throw new IllegalArgumentException("Name correspods to simple tensor (not a field).");
        if (descriptor.getIndicesTypeStructures().length - 1 != argIndices.length)
            throw new IllegalArgumentException("This name corresponds to field with different number of arguments.");
        for (int i = 0; i < argIndices.length; ++i) {
            if (!descriptor.getIndicesTypeStructures()[i + 1].isStructureOf(argIndices[i]))
                throw new IllegalArgumentException("Arguments indices are inconsistent with field signature.");
            if (!arguments[i].getIndices().getFreeIndices().equalsIgnoreOrder(argIndices[i]))
                throw new IllegalArgumentException("Arguments indices are inconsistent with arguments.");
        }
        return new TensorField(name, indices, arguments, argIndices);
    }

    public static TensorField field(int name, SimpleIndices indices, Tensor[] arguments) {
        if (arguments.length == 0)
            throw new IllegalArgumentException("No arguments in field.");
        SimpleIndices[] argIndices = new SimpleIndices[arguments.length];
        for (int i = 0; i < arguments.length; ++i)
            argIndices[i] = IndicesFactory.createSimple(null, arguments[i].getIndices().getFreeIndices());
        return new TensorField(name, indices, arguments, argIndices);
    }

    public static Tensor sin(Tensor argument) {
        return new Sin(argument);
    }

    public static Tensor cos(Tensor argument) {
        return new Cos(argument);
    }

    public static Tensor tan(Tensor argument) {
        return new Tan(argument);
    }

    public static Tensor cotan(Tensor argument) {
        return new CoTan(argument);
    }

    public static Tensor log(Tensor argument) {
        return new Log(argument);
    }
}
