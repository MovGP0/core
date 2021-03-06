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
package cc.redberry.core.parser;

import cc.redberry.core.context.NameAndStructureOfIndices;
import cc.redberry.core.indices.IndicesFactory;
import cc.redberry.core.indices.SimpleIndices;
import cc.redberry.core.indices.StructureOfIndices;
import cc.redberry.core.tensor.Tensor;
import cc.redberry.core.tensor.Tensors;

/**
 * AST node for tensor field.
 *
 * @author Dmitry Bolotin
 * @author Stanislav Poslavsky
 * @since 1.0
 */
public class ParseTokenTensorField extends ParseTokenSimpleTensor {
    /**
     * Indices of arguments.
     */
    public SimpleIndices[] argumentsIndices;

    /**
     * @param indices          indices of field
     * @param name             string name of field
     * @param content          child nodes
     * @param argumentsIndices indices of arguments.
     */
    public ParseTokenTensorField(SimpleIndices indices,
                                 String name,
                                 ParseToken[] content,
                                 SimpleIndices[] argumentsIndices) {
        super(indices, name, TokenType.TensorField, content);
        this.argumentsIndices = argumentsIndices;
    }

    @Override
    public NameAndStructureOfIndices getIndicesTypeStructureAndName() {
        StructureOfIndices[] typeStructures = new StructureOfIndices[1 + argumentsIndices.length];
        typeStructures[0] = new StructureOfIndices(indices);
        for (int i = 0; i < argumentsIndices.length; ++i) {
            if (argumentsIndices[i] == null)
                argumentsIndices[i] = IndicesFactory.createSimple(null, content[i].getIndices().getFree());
            typeStructures[i + 1] = new StructureOfIndices(argumentsIndices[i]);
        }
        return new NameAndStructureOfIndices(name, typeStructures);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append('[');
        for (ParseToken node : content)
            sb.append(node.toString()).append(", ");
        sb.deleteCharAt(sb.length() - 1).deleteCharAt(sb.length() - 1).append(']');
        return sb.toString();
    }

    @Override
    public Tensor toTensor() {
        Tensor[] arguments = contentToTensors();
        for (int i = 0; i < arguments.length; ++i)
            if (argumentsIndices[i] == null)
                argumentsIndices[i] = IndicesFactory.createSimple(null, arguments[i].getIndices().getFree());
        return Tensors.field(name, indices, argumentsIndices, contentToTensors());
    }
}
