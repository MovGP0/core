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

import java.util.Collection;
import java.util.List;

/**
 * This interface is a representation of {@link Symmetry} set. It common
 * functionality: adding {@code Symmetry} and iterating over all possible
 * symmetries, which can be generated by compositions of keeping symmetries.
 * Also, it watches that all keeping symmetries are consistent, i.e. it throws
 * {@link InconsistentGeneratorsException} when trying to add symmetry, which is
 * inconsistent with already added symmetries. All adding permutations must have
 * the same dimension, which specified at initialization. For more details see
 * method summary.
 *
 * @see Symmetry
 * @see PermutationsSpanIterator
 *
 * @author Dmitry Bolotin
 * @author Stanislav Poslavsky
 */
public interface Symmetries extends Iterable<Symmetry> {

    int dimension();

    boolean isEmpty();

    /**
     * Appends the specified {@code Symmetry} to this {@code Symmetries}. If
     * inconsistent {@code Symmetry} adding it throws
     * {@code InconsistentGeneratorsException}. Note, that some times it is
     * difficult for human to understand that adding symmetry is inconsistent.
     *
     * <h4><a name="Symmetries">Examples:</a></h4> Trying to add
     * {@code new Symmetry(new int[]{0,1,2},true)}, i.e. identical permutation
     * which change sign, will throws. <p> Assume, we have already added
     * {@code new Symmetry(new int[]{0,2,1},false)}, which is transposition of
     * the second and the third element. If the we try to add
     * {@code new Symmetry(new int[]{0,2,1},true)}, which is the same
     * transposition, but changing sign, this method will throws. <p> More
     * difficult example: adding {@code new Symmetry(new int[]{2,0,1},true)}
     * will throws. Discover, what's wrong here?
     *
     * @param symmetry {@code Symmetry} to be added
     *
     * @throws InconsistentGeneratorsException if adding {@code Symmetry} is
     * inconsistent with already added symmetries
     */
    boolean add(Symmetry symmetry) throws InconsistentGeneratorsException;

    /**
     * Appends the specified {@code Symmetry}, which is represented by its
     * permutation array in one-line notation and boolean sign, to this
     * {@code Symmetries}. If inconsistent {@code Symmetry} adding it throws
     * {@code InconsistentGeneratorsException}. Note, that some times it is
     * difficult for human to understand that adding symmetry is inconsistent.
     *
     * <h4><a name="Symmetries">Examples:</a></h4> Trying to add
     * {@code new Symmetry(new int[]{0,1,2},true)}, i.e. identical permutation
     * which change sign, will throws. <p> Assume, we have already added
     * {@code new Symmetry(new int[]{0,2,1},false)}, which is transposition of
     * the second and the third element. If the we try to add
     * {@code new Symmetry(new int[]{0,2,1},true)}, which is the same
     * transposition, but changing sign, this method will throws. <p> More
     * difficult example: adding {@code new Symmetry(new int[]{2,0,1},true)}
     * will throws. Discover, what's wrong here?
     *
     * @param symmetry {@code Symmetry} permutation in one-line notation to be
     * added
     * @param sign     {@code Symmetry} sign
     *
     * @throws InconsistentGeneratorsException if adding {@code Symmetry} is
     * inconsistent with already added symmetries
     */
    boolean add(boolean sign, int... symmetry)
            throws InconsistentGeneratorsException;

    /**
     * Appends the specified array of {@code Symmetry} to this
     * {@code Symmetries}. If inconsistent {@code Symmetry} adding it throws
     * {@code InconsistentGeneratorsException}. Note, that some times it is
     * difficult for human to understand that adding symmetry is inconsistent.
     *
     * <h4><a name="Symmetries">Examples:</a></h4> Trying to add
     * {@code new Symmetry(new int[]{0,1,2},true)}, i.e. identical permutation
     * which change sign, will throws. <p> Assume, we have already added
     * {@code new Symmetry(new int[]{0,2,1},false)}, which is transposition of
     * the second and the third element. If the we try to add
     * {@code new Symmetry(new int[]{0,2,1},true)}, which is the same
     * transposition, but changing sign, this method will throws. <p> More
     * difficult example: adding {@code new Symmetry(new int[]{2,0,1},true)}
     * will throws. Discover, what's wrong here?
     *
     * @param symmetry {@code Symmetry} to be added
     *
     * @throws InconsistentGeneratorsException if adding {@code Symmetry} is
     * inconsistent with already added symmetries
     */
    boolean addAll(Symmetry... symmetries) throws InconsistentGeneratorsException;

    /**
     * Appends the specified array of {@code Symmetry} to this
     * {@code Symmetries}. If inconsistent {@code Symmetry} adding it throws
     * {@code InconsistentGeneratorsException}. Note, that some times it is
     * difficult for human to understand that adding symmetry is inconsistent.
     *
     * <h4><a name="Symmetries">Examples:</a></h4> Trying to add
     * {@code new Symmetry(new int[]{0,1,2},true)}, i.e. identical permutation
     * which change sign, will throws. <p> Assume, we have already added
     * {@code new Symmetry(new int[]{0,2,1},false)}, which is transposition of
     * the second and the third element. If the we try to add
     * {@code new Symmetry(new int[]{0,2,1},true)}, which is the same
     * transposition, but changing sign, this method will throws. <p> More
     * difficult example: adding {@code new Symmetry(new int[]{2,0,1},true)}
     * will throws. Discover, what's wrong here?
     *
     * @param symmetry {@code Symmetry} to be added
     *
     * @throws InconsistentGeneratorsException if adding {@code Symmetry} is
     * inconsistent with already added symmetries
     */
    boolean addAll(Collection<Symmetry> symmetries)
            throws InconsistentGeneratorsException;

    List<Symmetry> getBaseSymmetries();

    Symmetries clone();

    boolean addAllUnsafe(Symmetry... symmetries);

    boolean addAllUnsafe(Collection<Symmetry> symmetries);

    boolean addAllUnsafe(Symmetries symmetries);

    boolean addUnsafe(Symmetry symmetry);
}
