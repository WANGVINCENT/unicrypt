/*
 * UniCrypt
 *
 *  UniCrypt(tm) : Cryptographical framework allowing the implementation of cryptographic protocols e.g. e-voting
 *  Copyright (C) 2014 Bern University of Applied Sciences (BFH), Research Institute for
 *  Security in the Information Society (RISIS), E-Voting Group (EVG)
 *  Quellgasse 21, CH-2501 Biel, Switzerland
 *
 *  Licensed under Dual License consisting of:
 *  1. GNU Affero General Public License (AGPL) v3
 *  and
 *  2. Commercial license
 *
 *
 *  1. This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 *  2. Licensees holding valid commercial licenses for UniCrypt may use this file in
 *   accordance with the commercial license agreement provided with the
 *   Software or, alternatively, in accordance with the terms contained in
 *   a written agreement between you and Bern University of Applied Sciences (BFH), Research Institute for
 *   Security in the Information Society (RISIS), E-Voting Group (EVG)
 *   Quellgasse 21, CH-2501 Biel, Switzerland.
 *
 *
 *   For further information contact <e-mail: unicrypt@bfh.ch>
 *
 *
 * Redistributions of files must retain the above copyright notice.
 */
package ch.bfh.unicrypt.math.algebra.additive.interfaces;

import ch.bfh.unicrypt.helper.bytetree.ByteTree;
import ch.bfh.unicrypt.helper.numerical.Numerical;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;
import ch.bfh.unicrypt.random.interfaces.RandomByteSequence;
import java.math.BigInteger;

/**
 * This interface provides the renaming of {@link SemiGroup} methods for the case of an additively written semigroup. No
 * functionality is added.
 * <p>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 * @param <V> Generic type of values stored in the elements of this semigroup
 */
public interface AdditiveSemiGroup<V extends Object>
	   extends SemiGroup<V> {

	/**
	 * This method is a synonym for {@link #Group.apply(Element, Element)}.
	 * <p>
	 * @param element1 the same as in {@link #Group.apply(Element, Element)}
	 * @param element2 the same as in {@link #Group.apply(Element, Element)}
	 * @return the same as in {@link #Group.apply(Element, Element)}
	 */
	public AdditiveElement<V> add(Element element1, Element element2);

	/**
	 * This method is a synonym for {@link #Group.apply(Element...)}.
	 * <p>
	 * @param elements the same as in {@link #Group.apply(Element...)}
	 * @return the same as in {@link #Group.apply(Element...)}
	 */
	public AdditiveElement<V> add(Element... elements);

	/**
	 * This method is a synonym for {@link #Group.selfApply(Element, BigInteger)}.
	 * <p>
	 * @param element the same as in {@link #Group.selfApply(Element, BigInteger)}
	 * @param amount  the same as in {@link #Group.selfApply(Element, BigInteger)}
	 * @return the same as in {@link #Group.selfApply(Element, BigInteger)}
	 */
	public AdditiveElement<V> times(Element element, BigInteger amount);

	/**
	 * This method is a synonym for {@link #Group.selfApply(Element, Element)}.
	 * <p>
	 * @param element the same as in {@link #Group.selfApply(Element, Element)}
	 * @param amount  the same as in {@link #Group.selfApply(Element, Element)}
	 * @return the same as in {@link #Group.selfApply(Element, Element)}
	 */
	public AdditiveElement<V> times(Element element, Element<Numerical> amount);

	/**
	 * This method is a synonym for {@link #Group.selfApply(Element, int)}.
	 * <p>
	 * @param element the same as in {@link #Group.selfApply(Element, int)}
	 * @param amount  the same as in {@link #Group.selfApply(Element, int)}
	 * @return the same as in {@link #Group.selfApply(Element, int)}
	 */
	public AdditiveElement<V> times(Element element, int amount);

	/**
	 * Applies the group operation to two instances of a given group element. This is equivalent to
	 * {@code selfApply(element, 2)}.
	 * <p>
	 * @param element A given group element
	 * @return The result of applying the group operation to the input element
	 * @throws IllegalArgumentException if {@code element} does not belong to the group
	 */
	public AdditiveElement<V> timesTwo(Element element);

	/**
	 * Applies the binary operation pair-wise sequentially to the results of computing
	 * {@link #selfApply(Element, BigInteger)} multiple times. In an additive group, this operation is sometimes called
	 * 'weighed sum', and 'product-of-powers' in a multiplicative group.
	 * <p>
	 * @param elements A given array of elements
	 * @param amounts  Corresponding amounts
	 * @return The result of this operation
	 * @throws IllegalArgumentException if one of the elements of {@code elements} does not belong to the group
	 * @throws IllegalArgumentException if {@code elements} and {@code amounts} have different lengths
	 */
	public AdditiveElement<V> sumOfProducts(Element[] elements, BigInteger[] amounts);

	// The following methods are overridden from Set with an adapted return type
	@Override
	public AdditiveElement<V> getElementFrom(int value);

	@Override
	public AdditiveElement<V> getElementFrom(BigInteger value);

	@Override
	public AdditiveElement<V> getElementFrom(ByteTree byteTree);

	@Override
	public AdditiveElement<V> getElementFrom(Element element);

	@Override
	public AdditiveElement<V> getRandomElement();

	@Override
	public AdditiveElement<V> getRandomElement(RandomByteSequence randomByteSequence);

	// The following methods are overridden from SemiGroup with an adapted return type
	@Override
	public AdditiveElement<V> apply(Element element1, Element element2);

	@Override
	public AdditiveElement<V> apply(Element... elements);

	@Override
	public AdditiveElement<V> selfApply(Element element, BigInteger amount);

	@Override
	public AdditiveElement<V> selfApply(Element element, Element<Numerical> amount);

	@Override
	public AdditiveElement<V> selfApply(Element element, int amount);

	@Override
	public AdditiveElement<V> selfApply(Element element);

	@Override
	public AdditiveElement<V> multiSelfApply(Element[] elements, BigInteger[] amounts);

}
