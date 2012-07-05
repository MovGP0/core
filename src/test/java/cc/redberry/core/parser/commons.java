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

import cc.redberry.core.indices.IndicesUtils;
import cc.redberry.core.utils.ArraysUtils;
import cc.redberry.core.utils.IntArrayList;
import cc.redberry.core.utils.StringFormat;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.math.BigInteger;
import java.util.Arrays;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.fraction.BigFraction;
import org.apache.commons.math3.fraction.Fraction;
import org.junit.Test;

/**
 *
 * @author Dmitry Bolotin
 * @author Stanislav Poslavsky
 */
public class commons {

    @Test
    public void sadasd() {
        Fraction f = new Fraction(2, 3);
        System.out.println(f);
        System.out.println(f.multiply(new Fraction(3, 5)));
        System.out.println(f.multiply(new Fraction(3, 17)));
        System.out.println(f.multiply(new Fraction(3.3242342142342342)));
        System.out.println(new Fraction(3.324234214234231, Integer.MAX_VALUE));

        System.out.println(((double) 294613997.0) / (double) 88626125.0);
        System.out.println(Long.MAX_VALUE);
        System.out.println(1 + Integer.MAX_VALUE);
        System.out.println(new BigInteger("12").multiply(BigInteger.ONE));
//        new BigFraction(2, 3).d
//                NumberFormat.getCurrencyInstance().format(f)
        Double d = new Double("1.2");
        System.out.println(d + 1.0 + " " + d);
        System.out.println(Double.MAX_VALUE);
        System.out.println(3 * 2.1);

        System.out.println(new BigFraction(2, 3).subtract(BigFraction.TWO).toString());
        System.out.println(BigFraction.ZERO.doubleValue() == 0.0);
        System.out.println(3 / 0.0);
        System.out.println(Double.parseDouble("Infinity"));
        System.out.println(1.0 == 1);
        double dd = Double.NaN;
        System.out.println(1 / 0.0);
        Complex cc1 = new Complex(0.0);
        Complex cc2 = new Complex(Double.POSITIVE_INFINITY);
        System.out.println(cc1.multiply(cc2));
        System.out.println(0.0 * Double.POSITIVE_INFINITY);
        System.out.println(0.0 * Double.POSITIVE_INFINITY - 0.0 * 0.0);
        System.out.println(0.0 * Double.POSITIVE_INFINITY + 0.0 * Double.POSITIVE_INFINITY);

//        new Double("NaN").r
        System.out.println(Long.toHexString(Double.doubleToRawLongBits(Double.NaN)));
        System.out.println(Long.toHexString(Double.doubleToRawLongBits(-Double.NaN)));
        System.out.println(Long.toHexString(Double.doubleToLongBits(Double.NaN)));
        System.out.println(Long.toHexString(Double.doubleToLongBits(-Double.NaN)));
        System.out.println(Double.longBitsToDouble(0x7ff8000000000000L));
        System.out.println(0x7ff8000000000000L);

        new IntArrayList().add(new Integer(2));


        Integer D = new Integer(2);
        System.out.println(D + 3);
        int dsf = D + 3;
        Integer sadf = D + 5;
        System.out.println(D);
        System.out.println(dsf);
        System.out.println(sadf);
        System.out.println(D ^ 1);


        System.out.println(new String("a").hashCode());
        System.out.println("a".hashCode());
        System.out.println(new String("b").hashCode());
        System.out.println("b".hashCode());
        System.out.println(new String("c").hashCode());
        System.out.println("c".hashCode());


        System.out.println(new Integer(3).compareTo(new Integer(2)));
        int[] as = new int[12];
    }

    @Test
    public void teasjhd() {
        try {
            // Create file 
            FileWriter fstream = new FileWriter("/home/stas/Projects/test.txt");
            BufferedWriter out = new BufferedWriter(fstream);
            out.write("Hello Java\n");
            out.write("Hello Java\n");
            out.write("Hello Java\n");
            out.write("Hello Java");
            //Close the output stream
            out.close();
        } catch (Exception e) {//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
    }

    public interface Converter {

        int transform(int i);
        public static Converter INVERSE = new Converter() {

            @Override
            public int transform(int i) {
                return IndicesUtils.inverseIndexState(i);
            }
        };
    }

    int[] transform(int[] a, Converter c) {
        int[] b = new int[a.length];
        for (int i = 0; i < a.length; ++i)
            b[i] = c.transform(a[i]);
        return b;
    }

    @Test
    public void aA() {
        System.out.println("a".hashCode());
        System.out.println("b".hashCode());
        System.out.println("cc".hashCode());
    }
}