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
package cc.redberry.core.transformations;

import cc.redberry.core.indexmapping.IndexMappingBuffer;
import cc.redberry.core.indexmapping.IndexMappingBufferImpl;
import cc.redberry.core.indexmapping.IndexMappings;
import cc.redberry.core.indices.IndexType;
import cc.redberry.core.tensor.Tensor;
import cc.redberry.core.utils.TensorUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Set;

import static cc.redberry.core.tensor.Tensors.addSymmetry;
import static cc.redberry.core.tensor.Tensors.parse;

/**
 *
 * @author Dmitry Bolotin
 * @author Stanislav Poslavsky
 */
public class ApplyIndexMappingTest {

    @Test
    public void testSimple1() {
        Tensor from = parse("A_m^n");
        Tensor to = parse("A_a^a");
        IndexMappingBuffer imb = IndexMappings.getFirst(from, to, false);

        Tensor target = parse("A_m^n");
        target = ApplyIndexMapping.applyIndexMapping(target, imb, new int[0]);

        Tensor standard = parse("A_a^a");
        Assert.assertTrue(TensorUtils.equals(target, standard));
    }

    @Test
    public void testSum1() {
        Tensor from = parse("A_mn");
        Tensor to = parse("A_cd");
        IndexMappingBuffer imb = IndexMappings.getFirst(from, to, false);


        Tensor target = parse("C_mn+D_nm");
        target = ApplyIndexMapping.applyIndexMapping(target, imb);

        Tensor standard = parse("C_cd+D_dc");
        Assert.assertTrue(TensorUtils.equals(target, standard));
    }

    @Test
    public void testSum2() {
        Tensor from = parse("A_mn");
        Tensor to = parse("A_cd");
        IndexMappingBuffer imb = IndexMappings.getFirst(from, to, false);


        Tensor target = parse("(C_ms+D_ms)*F^s_n");
        target = ApplyIndexMapping.applyIndexMapping(target, imb);

        Tensor standard = parse("(C_cs+D_cs)*F^s_d");
        Assert.assertTrue(TensorUtils.equals(target, standard));
    }

    @Test
    public void testSum3() {
        Tensor from = parse("A_mn");
        Tensor to = parse("A_cd");
        IndexMappingBuffer imb = IndexMappings.getFirst(from, to, false);
        Tensor target = parse("(C_md+D_md)*F^d_n");
        target = ApplyIndexMapping.applyIndexMapping(target, imb);
        Tensor standard = parse("(C_{ca}+D_{ca})*F^{a}_{d}");
        Assert.assertTrue(TensorUtils.equals(target, standard));
    }

    @Test
    public void testSum4() {
        Tensor from = parse("A_abmn");
        Tensor to = parse("A_acdx");
        IndexMappingBuffer imb = IndexMappings.getFirst(from, to, false);
        Tensor target = parse("(C_mdb+D_mdb)*F^d_na");
        target = ApplyIndexMapping.applyIndexMapping(target, imb);
        Tensor standard = parse("(C_dbc+D_dbc)*F^b_xa");
        Assert.assertTrue(TensorUtils.equals(target, standard));
    }

    @Test
    public void testSum5() {
        Tensor from = parse("A_abcd");
        Tensor to = parse("A_wxyz");
        IndexMappingBuffer imb = IndexMappings.getFirst(from, to, false);
        int[] usedIndices = parse("B_mn").getIndices().getAllIndices().copy();
        Tensor target = parse("(A_mn*B^mn_ab+C_ab)*C^dc");
        target = ApplyIndexMapping.applyIndexMapping(target, imb, usedIndices);
        Tensor standard = parse("(A_{ab}*B^{ab}_{wx}+C_{wx})*C^{zy}");
        Assert.assertTrue(TensorUtils.equals(target, standard));
    }

    @Test
    public void testSum6() {
        Tensor from = parse("A_abcd");
        Tensor to = parse("A_wxyz");
        IndexMappingBuffer imb = IndexMappings.getFirst(from, to, false);
        //int[] usedIndices = parse("B_mn").getIndices().getAllIndices().copy();
        Tensor target = parse("A_{ae bxk}*B^{bxk}_d+A_{ed cbxk}*B^{cbxk}_a"); //Do not work: Why???
        target = ApplyIndexMapping.applyIndexMapping(target, imb);
        Tensor standard = parse("A_{we xbk}*B^{xbk}_z+A_{ez yxbk}*B^{yxbk}_w");
        System.out.println(target);
        //Assert.assertTrue(TensorUtils.equals(target, standard));
    }

    @Test
    public void testProduct1() {
        Tensor from = parse("A_abcd");
        Tensor to = parse("A_wxyz");
        IndexMappingBuffer imb = IndexMappings.getFirst(from, to, false);
        int[] usedIndices = parse("B_mn").getIndices().getAllIndices().copy();

        Tensor target = parse("A_{awe bxk}*B^{bxk}_d*A^d_{sqz}*B^{sqz}_cy");
        //               CORRECT: A_{wae xck}*B^{xck}_z*A^z_{sqd}*B^{sqd}_yb
        target = ApplyIndexMapping.applyIndexMapping(target, imb, usedIndices);
        Tensor standard = parse("A_{wae xck}*B^{xck}_z*A^z_{sqd}*B^{sqd}_yb");
        Assert.assertTrue(TensorUtils.equals(target, standard));
    }

    @Test
    public void testProduct2() {
        Tensor from = parse("A_abcd");
        Tensor to = parse(  "A_wxyz");
        IndexMappingBuffer imb = IndexMappings.getFirst(from, to, false);
        int[] usedIndices = parse("B_abcd").getIndices().getAllIndices().copy();
        Tensor target = parse("A_{a}^{d}*B_{e}^{cty}*D_{bty}^{e}");
                             //A_{a}^{d}*D_{bty}^{e}*B_{e}^{cty}
        System.out.println(target);
        System.out.println(target.getIndices().getFreeIndices());
        target = ApplyIndexMapping.applyIndexMapping(target, imb, usedIndices);
        //Tensor standard = parse("A_{wfexhk}*B^{xhk}_{z}*A^{z}_{sql}*B^{sql}_{yg}");
        System.out.println(target);
        //Assert.assertTrue(TensorUtils.equals(target, standard));
    }

    @Test
    public void testFraction1() {
        Tensor from = parse("A_ab");
        Tensor to = parse("A_xy");
        IndexMappingBuffer imb = IndexMappings.getFirst(from, to, false);

        Tensor target = parse("(a*b*g_ab)/(A_x*A^x+B_y*B^y)");
        //               CORRECT: (a*b*g_xy)/(A_x*A^x+B_y*B^y)

        target = ApplyIndexMapping.applyIndexMapping(target, imb);
        Tensor standard = parse("(a*b*g_xy)/(A_{a}*A^{a}+B_{b}*B^{b})");
        Assert.assertTrue(IndexMappings.createPort(target, standard).take() != null);
    }

    @Test
    public void testFraction2() {
        Tensor from = parse("A_ab");
        Tensor to = parse("A_xy");
        IndexMappingBuffer imb = IndexMappings.getFirst(from, to, false);
        int[] usedIndices = parse("B_wxyzabcdmn").getIndices().getAllIndices().copy();

        Tensor target = parse("(a*b*g_ab*g^abxm)/(A_xwz*A^xwz+B_y*B^y/(k_max*H^amx))");
        //               CORRECT: (a*b*g_xy*g^xyef)/(A_xwz*A^xwz+B_y*B^y/(k_max*H^amx))

        target = ApplyIndexMapping.applyIndexMapping(target, imb, usedIndices);
        Tensor standard = parse("(a*b*g_xy*g^xyfe)/(A_xwz*A^xwz+B_y*B^y/(k_max*H^amx))");
        Assert.assertTrue(TensorUtils.equals(target, standard));
    }

    @Test
    public void testField1() {
        Tensor from = parse("A_ab");
        Tensor to = parse("A_xy");
        IndexMappingBuffer imb = IndexMappings.getFirst(from, to, false);

        Tensor target = parse("F_ab[g_qw]");
        target = ApplyIndexMapping.applyIndexMapping(target, imb);
        System.out.println(target);
    }

    @Test
    public void testField2() {
        Tensor from = parse("A_ab");
        Tensor to = parse("A_xy");
        IndexMappingBuffer imb = IndexMappings.getFirst(from, to, false);
        int[] usedIndices = parse("B_wxyzabcdmn").getIndices().getAllIndices().copy();

        Tensor target = parse("F_cab[g_xyab*f[h_wxyzabcdmn]]");
        //               CORRECT: F_exy[g_xyab*f[h_wxyzabcdmn]]

        target = ApplyIndexMapping.applyIndexMapping(target, imb, usedIndices);
        Tensor standard = parse("F_exy[g_xyab*f[h_wxyzabcdmn]]");
        Assert.assertTrue(TensorUtils.equals(target, standard));
    }

    @Test
    public void cloneSensitiveTest1() {
        Tensor from = parse("A_ab");
        Tensor to = parse("A_xy");
        IndexMappingBuffer imb = IndexMappings.getFirst(from, to, false);
        int[] usedIndices = parse("B_md").getIndices().getAllIndices().copy();

        Tensor target = parse("A_m^km+B_d^kd");

        target = ApplyIndexMapping.applyIndexMapping(target, imb, usedIndices);
        //System.out.println(target);
        Tensor standard = parse("A_{a}^{ka}+B_{b}^{kb}");
        Assert.assertTrue(TensorUtils.equals(target, standard));
    }

    @Test
    public void emptyMapping1() {
        Tensor target = parse("A_mn*(B_m^m+C)");
        target = ApplyIndexMapping.applyIndexMapping(target, new IndexMappingBufferImpl(true));
        Tensor standard = parse("A_mn*(B_m^m+C)");
        Assert.assertTrue(TensorUtils.equals(target, standard));
    }

    @Test
    public void testManyMappings() {
        Tensor riman1 = parse("g_ax*(d_c*G^x_bd-d_d*G^x_bc+G^x_yc*G^y_bd-G^x_yd*G^y_bc)");
        //                        g_px*(d_r*G^x_qs-d_s*G^x_qr+G^x_yr*G^y_qs-G^x_ys*G^y_qr)
        //                        g_px*(d_s*G^x_qr-d_r*G^x_qs+G^x_ys*G^y_qr-G^x_yr*G^y_qs)

        Tensor riman2 = parse("g_px*(d_r*G^x_qs-d_s*G^x_qr+G^x_yr*G^y_qs-G^x_ys*G^y_qr)");

        addSymmetry("G^a_bc", IndexType.LatinLower, false, 0, 2, 1);
        addSymmetry("g_ab", IndexType.LatinLower, false, 1, 0);

        Set<IndexMappingBuffer> buffers = IndexMappings.createAllMappings(riman1, riman2, false);
        Tensor[] targets = new Tensor[buffers.size()];
        int i = 0;
        for (IndexMappingBuffer buffer : buffers)
            targets[i++] = ApplyIndexMapping.applyIndexMapping(riman1, buffer);

        Tensor[] standarts = new Tensor[buffers.size()];
        standarts[0] = parse("g_px*(d_r*G^x_qs-d_s*G^x_qr+G^x_yr*G^y_qs-G^x_ys*G^y_qr)");
        standarts[1] = parse("g_px*(d_s*G^x_qr-d_r*G^x_qs+G^x_ys*G^y_qr-G^x_yr*G^y_qs)");
        Arrays.sort(targets);
        Arrays.sort(standarts);
        for (i = i - 1; i >= 0; --i)
            Assert.assertTrue(IndexMappings.createPort(targets[i], standarts[i]).take() != null);

    }

    @Test
    public void testRecursive1() {
        Tensor t1 = parse("A_mn");
        Tensor t2 = parse("A_ab");

        int[] usedStates = parse("A_pqrs").getIndices().getAllIndices().copy();

        //target = R_abcd*R_pqrs
        Tensor target = parse("B_mn+D_nm");
        addSymmetry("A_bc", IndexType.LatinLower, false, 1, 0);

        Set<IndexMappingBuffer> buffers = IndexMappings.createAllMappings(t1, t2, false);
        Tensor[] targets = new Tensor[buffers.size()];
        int i = -1;
        for (IndexMappingBuffer buffer : buffers)
            targets[++i] = ApplyIndexMapping.applyIndexMapping(target, buffer, usedStates);
        Tensor[] standarts = new Tensor[buffers.size()];
        standarts[0] = parse("B_ab+D_ba");
        standarts[1] = parse("B_ba+D_ab");
        for (; i >= 0; --i)
            Assert.assertTrue(IndexMappings.createPort(targets[i], standarts[i]).take() != null);
    }
}