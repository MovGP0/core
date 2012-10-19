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
package cc.redberry.core.combinatorics;

import org.junit.Test;

import java.util.Arrays;

/**
 *
 * @author Dmitry Bolotin
 * @author Stanislav Poslavsky
 */
public class IntCombinationPermutationGeneratorTest {
    @Test
    public void test1() {
        for (int[] cp : new IntCombinationPermutationGenerator(1, 1))
            System.out.println(Arrays.toString(cp));
    }

    @Test
    public void test2() {
        for (int[] cp : Combinatorics.createIntGenerator(0, 0))
            System.out.println(Arrays.toString(cp));
    }

    @Test
    public void test3() {
        for (int[] cp : new IntCombinationPermutationGenerator(5, 1))
            System.out.println(Arrays.toString(cp));
    }

    @Test
    public void test4() {
        for (int[] cp : new IntPermutationsGenerator(1))
            System.out.println(Arrays.toString(cp));
    }

    @Test
    public void test5() {
        for (int[] cp : new IntCombinationPermutationGenerator(3, 0))
            System.out.println(Arrays.toString(cp));
    }
}
