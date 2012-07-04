/*
 * Redberry: symbolic current computations.
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
package cc.redberry.core.tensor.iterator;

import cc.redberry.core.number.*;
import cc.redberry.core.tensor.Sum;
import cc.redberry.core.tensor.Tensor;
import cc.redberry.core.tensor.Tensors;
import cc.redberry.core.tensor.functions.Sin;
import cc.redberry.core.utils.TensorUtils;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

import static cc.redberry.core.tensor.Tensors.parse;

/**
 *
 * @author Dmitry Bolotin
 * @author Stanislav Poslavsky
 */
public class TensorTreeIteratorTest {

    @Test
    public void test1() {
        Tensor t = parse("a+b+Sin[x]");
        Tensor[] expectedSequence = {t,//a+b+Sin[x]
                                     t.get(0),//a
                                     t.get(0),//a
                                     t.get(1),//b
                                     t.get(1),//b
                                     t.get(2),//Sin[x]
                                     t.get(2),//Sin[x]
                                     t//a+b+Sin[x]
        };
        TraverseGuide guide = new TraverseGuide() {

            @Override
            public TraversePermission getPermission(Tensor parent, int indexInParent, Tensor tensor) {
                if (parent.getClass() == Sin.class)
                    return TraversePermission.DontShow;
                return TraversePermission.Enter;
            }
        };
        List<Tensor> list = new ArrayList<>();
        TreeTraverseIterator iterator = new TreeTraverseIterator(t, guide);
        while (iterator.next() != null)
            list.add(iterator.current());
        Tensor[] actual = list.toArray(new Tensor[0]);
        Assert.assertTrue(TensorUtils.equals(expectedSequence, actual));
    }

    @Test
    public void test2() {
        Tensor t = parse("Cos[a+b+Sin[x]]");
        Tensor[] expectedSequence = {t,//Cos[a+b+Sin[x]]
                                     t.get(0),//a+b+Sin[x]
                                     t.get(0).get(0),//a
                                     t.get(0).get(0),//a
                                     t.get(0).get(1),//b
                                     t.get(0).get(1),//b
                                     t.get(0).get(2),//Sin[x]
                                     t.get(0).get(2),//Sin[x]
                                     t.get(0),//a+b+Sin[x]
                                     t//Cos[a+b+Sin[x]]
        };
        TraverseGuide guide = new TraverseGuide() {

            @Override
            public TraversePermission getPermission(Tensor parent, int indexInParent, Tensor tensor) {
                if (parent.getClass() == Sin.class)
                    return TraversePermission.DontShow;
                return TraversePermission.Enter;
            }
        };
        List<Tensor> list = new ArrayList<>();
        TreeTraverseIterator iterator = new TreeTraverseIterator(t, guide);
        while (iterator.next() != null)
            list.add(iterator.current());
        Tensor[] actual = list.toArray(new Tensor[0]);
        Assert.assertTrue(TensorUtils.equals(expectedSequence, actual));
    }

    @Test
    public void test3() {
        Tensor t = parse("Cos[Sin[x+y]]");
        Tensor[] expectedSequence = {t,//Cos[Sin[x+y]]
                                     t.get(0),//Sin[x+y]
                                     t.get(0).get(0),//x+y
                                     t.get(0).get(0).get(0),//x
                                     t.get(0).get(0).get(0),//x
                                     t.get(0).get(0).get(1),//y
                                     t.get(0).get(0).get(1),//y
                                     t.get(0).get(0),//x+y
                                     t.get(0),//Sin[x+y]
                                     t//Cos[Sin[x+y]]
        };
        List<Tensor> list = new ArrayList<>();
        TreeTraverseIterator iterator = new TreeTraverseIterator(t);
        while (iterator.next() != null)
            list.add(iterator.current());
        Tensor[] actual = list.toArray(new Tensor[0]);
        Assert.assertTrue(TensorUtils.equals(expectedSequence, actual));
    }

    @Test
    public void test4() {
        Tensor t = parse("Cos[Sin[x+y]]");
        Tensor[] expectedSequence = {t,//Cos[Sin[x+y]]
                                     t.get(0),//Sin[x+y]                                    
                                     t.get(0),//Sin[x+y]
                                     t//Cos[Sin[x+y]]
        };
        TraverseGuide guide = new TraverseGuide() {

            @Override
            public TraversePermission getPermission(Tensor parent, int indexInParent, Tensor tensor) {
                if (parent.getClass() == Sin.class)
                    return TraversePermission.DontShow;
                return TraversePermission.Enter;
            }
        };
        List<Tensor> list = new ArrayList<>();
        TreeTraverseIterator iterator = new TreeTraverseIterator(t, guide);
        while (iterator.next() != null)
            list.add(iterator.current());
        Tensor[] actual = list.toArray(new Tensor[0]);
        Assert.assertTrue(TensorUtils.equals(expectedSequence, actual));

        //equivalent guide
        guide = new TraverseGuide() {

            @Override
            public TraversePermission getPermission(Tensor parent, int indexInParent, Tensor tensor) {
                if (tensor.getClass() == Sum.class)
                    return TraversePermission.DontShow;
                return TraversePermission.Enter;
            }
        };
        list.clear();
        iterator = new TreeTraverseIterator(t, guide);
        while (iterator.next() != null)
            list.add(iterator.current());
        actual = list.toArray(new Tensor[0]);
        Assert.assertTrue(TensorUtils.equals(expectedSequence, actual));

        //equivalent guide
        guide = new TraverseGuide() {

            @Override
            public TraversePermission getPermission(Tensor parent, int indexInParent, Tensor tensor) {
                if (tensor.getClass() == Sum.class && parent.getClass() == Sin.class && indexInParent == 0)
                    return TraversePermission.DontShow;
                return TraversePermission.Enter;
            }
        };
        list.clear();
        iterator = new TreeTraverseIterator(t, guide);
        while (iterator.next() != null)
            list.add(iterator.current());
        actual = list.toArray(new Tensor[0]);
        Assert.assertTrue(TensorUtils.equals(expectedSequence, actual));
    }

    @Test
    public void test5() {
        Tensor t = parse("Cos[x]");
        Tensor[] expectedSequence = {};
        TraverseGuide guide = new TraverseGuide() {

            @Override
            public TraversePermission getPermission(Tensor parent, int indexInParent, Tensor tensor) {
                if (indexInParent == 0)
                    return TraversePermission.DontShow;
                return TraversePermission.Enter;
            }
        };
        List<Tensor> list = new ArrayList<>();
        TreeTraverseIterator iterator = new TreeTraverseIterator(t, guide);
        while (iterator.next() != null)
            list.add(iterator.current());
        Tensor[] actual = list.toArray(new Tensor[0]);
        Assert.assertTrue(TensorUtils.equals(expectedSequence, actual));

        expectedSequence = new Tensor[]{t,//Cos[x]
                                        t//Cos[x]
        };

        guide = new TraverseGuide() {

            @Override
            public TraversePermission getPermission(Tensor parent, int indexInParent, Tensor tensor) {
                if (indexInParent == 0 && tensor.size() == 0)
                    return TraversePermission.DontShow;
                return TraversePermission.Enter;
            }
        };
        list.clear();
        iterator = new TreeTraverseIterator(t, guide);
        while (iterator.next() != null)
            list.add(iterator.current());
        actual = list.toArray(new Tensor[0]);
        Assert.assertTrue(TensorUtils.equals(expectedSequence, actual));
    }

    @Test
    public void test6() {
        Tensor t = parse("Cos[Sin[x+y]]");
        Tensor[] expectedSequence = {t,//Cos[Sin[x+y]]
                                     t.get(0),//Sin[x+y]
                                     t.get(0).get(0),//x+y
                                     t.get(0).get(0),//x+y
                                     t.get(0),//Sin[x+y]
                                     t//Cos[Sin[x+y]]
        };
        TraverseGuide guide = new TraverseGuide() {

            @Override
            public TraversePermission getPermission(Tensor parent, int indexInParent, Tensor tensor) {
                if (parent.getClass() == Sin.class)
                    return TraversePermission.ShowButNotEnter;
                return TraversePermission.Enter;
            }
        };
        List<Tensor> list = new ArrayList<>();
        TreeTraverseIterator iterator = new TreeTraverseIterator(t, guide);
        while (iterator.next()
                != null)
            list.add(iterator.current());
        Tensor[] actual = list.toArray(new Tensor[0]);
        Assert.assertTrue(TensorUtils.equals(expectedSequence, actual));


        //equivalent guide
        guide = new TraverseGuide() {

            @Override
            public TraversePermission getPermission(Tensor parent, int indexInParent, Tensor tensor) {
                if (tensor.getClass() == Sum.class)
                    return TraversePermission.ShowButNotEnter;
                return TraversePermission.Enter;
            }
        };
        list.clear();
        iterator = new TreeTraverseIterator(t, guide);
        while (iterator.next() != null)
            list.add(iterator.current());
        actual = list.toArray(new Tensor[0]);
        Assert.assertTrue(TensorUtils.equals(expectedSequence, actual));

        //equivalent guide
        guide = new TraverseGuide() {

            @Override
            public TraversePermission getPermission(Tensor parent, int indexInParent, Tensor tensor) {
                if (tensor.getClass() == Sum.class && parent.getClass() == Sin.class && indexInParent == 0)
                    return TraversePermission.ShowButNotEnter;
                return TraversePermission.Enter;
            }
        };
        list.clear();
        iterator = new TreeTraverseIterator(t, guide);
        while (iterator.next() != null)
            list.add(iterator.current());
        actual = list.toArray(new Tensor[0]);
        Assert.assertTrue(TensorUtils.equals(expectedSequence, actual));
    }

    @Test
    public void test7() {
        Tensor t = Tensors.parse("(x*y+(a-2*b)*Sin[x]-Cos[x+Sin[x]/Power[a,2]])*(a*a*Power[a,Sin[x-Cos[x]+1]] -32*a*a*2)/63");
        TreeTraverseIterator iterator = new TreeTraverseIterator(t);
        while (iterator.next() != null)
            if (TensorUtils.equals(iterator.current(), Tensors.parse("x")))
                iterator.set(Complex.ZERO);
        Tensor actual = iterator.result();
        Tensor expected = Tensors.parse("Power[a,2]");
        Assert.assertTrue(TensorUtils.equals(expected, actual));
    }

    @Test
    public void test8() {
        Tensor t = Tensors.parse("9*Power[M, 20] + 18*Power[M, 18]*(-3*s + 2*Power[pT, 2]) + 3*Power[M, 16]*(-70*s*Power[pT, 2] + 18*Power[pT, 4] + 51*Power[s, 2]) + 6*Power[M, 14]*(-54*s*Power[pT, 4] + 6*Power[pT, 6] + 98*Power[pT, 2]*Power[s, 2] - 45*Power[s, 3]) + Power[pT, 4]*Power[Power[pT,2] - s, 4]*Power[s, 4] - 2*s*Power[M, 10]*(-377*s*Power[pT, 6] + 51*Power[pT, 8] + 753*Power[pT, 4]*Power[s, 2] - 561*Power[pT, 2]*Power[s, 3] + 135*Power[s, 4]) + Power[M, 12]*(-252*s*Power[pT, 6] + 9*Power[pT, 8] + 920*Power[pT, 4]*Power[s, 2] - 1008*Power[pT, 2]*Power[s, 3] + 324*Power[s, 4]) + 2*Power[M, 6]*Power[s, 2]*(-227*s*Power[pT, 8] + 42*Power[pT, 10] + 456*Power[pT, 6]*Power[s, 2] - 425*Power[pT, 4]*Power[s, 3] + 180*Power[pT, 2]*Power[s, 4] - 27*Power[s, 5]) + s*Power[M, 8]*(344*s*Power[pT, 8] - 18*Power[pT, 10] - 1142*Power[pT, 6]*Power[s, 2] + 1476*Power[pT, 4]*Power[s, 3] - 810*Power[pT, 2]*Power[s, 4] + 153*Power[s, 5]) + Power[M, 4]*Power[s, 2]*(-86*s*Power[pT, 10] + 9*Power[pT, 12] + 269*Power[pT, 8]*Power[s, 2] - 374*Power[pT, 6]*Power[s, 3] + 263*Power[pT, 4]*Power[s, 4] - 84*Power[pT, 2]*Power[s, 5] + 9*Power[s, 6]) + 2*Power[M, 2]*Power[s, 3]*(10*s*Power[pT, 4] - 3*Power[pT, 6] - 11*Power[pT, 2]*Power[s, 2] + 3*Power[s, 3])*Power[Power[pT,3] - pT*s, 2]");
        TreeTraverseIterator iterator = new TreeTraverseIterator(t);
        Tensor M = Tensors.parse("M");

        while (iterator.next() != null)
            if (TensorUtils.equals(iterator.current(), Tensors.parse("pT")))
                iterator.set(M);
            else if (TensorUtils.equals(iterator.current(), Tensors.parse("s")))
                iterator.set(Tensors.pow(M, Complex.TWO));
        Tensor actual = iterator.result();
        Tensor expected = Tensors.parse("16*Power[M,20]");
        Assert.assertTrue(TensorUtils.equals(expected, actual));
    }
//    @Test
//    public void testSequence0WithDepthCheck() {
//        Tensor tensor = Tensors.parse("a+b+d*g*(m+f)");
//        String[] assertedSequence = {"a",
//                                     "b",
//                                     "d",
//                                     "g",
//                                     "m",
//                                     "f",
//                                     "m+f",
//                                     "d*g*(m+f)",
//                                     "a+b+d*g*(m+f)"};
//        int[] depths = {1, 1, 2, 2, 3, 3, 2, 1, 0};
//        int i = -1;
//        TreeTraverseIterator iterator = new TreeTraverseIterator(tensor);
//        while (iterator.hasNext()) {
//            Tensor t = iterator.next();
//            assertEquals(t, assertedSequence[++i]);
//            assertEquals(iterator.depth(), depths[i]);
//        }
//    }
}
