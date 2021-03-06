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
package cc.redberry.core;

import cc.redberry.concurrent.OutputPortUnsafe;
import cc.redberry.core.indexmapping.IndexMappingBuffer;
import cc.redberry.core.indexmapping.IndexMappings;
import cc.redberry.core.indices.Indices;
import cc.redberry.core.tensor.Tensor;
import cc.redberry.core.tensor.Tensors;
import cc.redberry.core.utils.IntArray;
import cc.redberry.core.utils.TensorUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Dmitry Bolotin
 * @author Stanislav Poslavsky
 */
public class TAssert {

    public static void assertEqualsExactly(Tensor actual, Tensor expected) {
        org.junit.Assert.assertTrue(TensorUtils.equalsExactly(actual, expected));
    }

    public static void assertEqualsExactly(Tensor actual, String expected) {
        assertEqualsExactly(actual, Tensors.parse(expected));
    }

    public static void assertEquals(Tensor actual, Tensor expected) {
        org.junit.Assert.assertTrue(TensorUtils.equals(actual, expected));
    }

    public static void assertNotEquals(Tensor actual, Tensor expected) {
        org.junit.Assert.assertFalse(TensorUtils.equals(actual, expected));
    }

    public static void assertEquals(Tensor actual, String expected) {
        assertEquals(actual, Tensors.parse(expected));
    }

    public static void assertEquals(Tensor[] actual, Tensor[] expected) {
        org.junit.Assert.assertTrue(actual.length == expected.length);
        for (int i = 0; i < actual.length; ++i)
            assertEquals(actual[i], expected[i]);
    }

    public static void assertEquals(Set<int[]> a, Set<int[]> b) {
        org.junit.Assert.assertEquals(a.size(), b.size());
        Set<IntArray> aSet = convert(a), bSet = convert(b);
        org.junit.Assert.assertTrue(aSet.containsAll(bSet));
        org.junit.Assert.assertTrue(bSet.containsAll(aSet));
    }

    private static Set<IntArray> convert(Set<int[]> set) {
        Set<IntArray> aSet = new HashSet<>(set.size());
        for (int[] array : set)
            aSet.add(new IntArray(array));
        return aSet;
    }


    public static void assertEquals(Tensor[] actual, String[] expected) {
        org.junit.Assert.assertTrue(actual.length == expected.length);
        for (int i = 0; i < actual.length; ++i)
            assertEquals(actual[i], expected[i]);
    }

    public static void assertIndicesParity(Tensor... tensors) {
        for (int i = 1; i < tensors.length; ++i)
            org.junit.Assert.assertTrue(tensors[0].getIndices().equalsRegardlessOrder(tensors[i].getIndices()));
    }

    public static void assertIndicesParity(Indices... indiceses) {
        for (int i = 1; i < indiceses.length; ++i)
            org.junit.Assert.assertTrue(indiceses[0].equalsRegardlessOrder(indiceses[i]));
    }

    public static boolean isEqualsExactly(Tensor tensor, String what) {
        return TensorUtils.equalsExactly(tensor, Tensors.parse(what));
    }

    public static boolean parity(Tensor tensor, String what) {
        return TensorUtils.equals(tensor, Tensors.parse(what));
    }

    public static void assertTrue(boolean condition) {
        org.junit.Assert.assertTrue(condition);
    }

    public static void assertFalse(boolean condition) {
        org.junit.Assert.assertFalse(condition);
    }

    public static void assertEquals(long expected, long actual) {
        org.junit.Assert.assertEquals(expected, actual);
    }

    public static void assertEquals(int expected, int actual) {
        org.junit.Assert.assertEquals(expected, actual);
    }

    public static void assertEquals(String expected, String actual) {
        org.junit.Assert.assertEquals(expected, actual);
    }

    public static void assertTensorEquals(String expected, String actual) {
        assertEquals(Tensors.parse(expected), Tensors.parse(actual));
    }

    public static Tensor _(String tensor) {
        return Tensors.parse(tensor);
    }

    public static void soutMappingsOP(Tensor from, Tensor to) {
        final OutputPortUnsafe<IndexMappingBuffer> opu =
                IndexMappings.createPort(from, to);
        IndexMappingBuffer buffer;
        int count = 0;
        while ((buffer = opu.take()) != null) {
            System.out.println(buffer);
            count++;
        }
        System.out.println("Total mappings count " + count);
    }

    public static void soutMappingsOP(String from, String to) {
        soutMappingsOP(_(from), _(to));
    }

    public static void assertIndicesConsistency(Tensor t) {
        TensorUtils.assertIndicesConsistency(t);
    }
//    public static void assertOpposite(Tensor target, Tensor expected) {
//        assertTrue(TTest.testOpposite(target, expected));
//    }
//
//    public static void assertOpposite(Tensor target, String expected) {
//        assertOpposite(target, CC.parse(expected));
//    }
}
