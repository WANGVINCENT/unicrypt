/*
 * UniCrypt
 *
 *  UniCrypt(tm): Cryptographical framework allowing the implementation of cryptographic protocols e.g. e-voting
 *  Copyright (c) 2016 Bern University of Applied Sciences (BFH), Research Institute for
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
package ch.bfh.unicrypt.math.algebra.multiplicative.abstracts;

import ch.bfh.unicrypt.math.algebra.general.abstracts.AbstractElement;
import ch.bfh.unicrypt.math.algebra.general.abstracts.AbstractSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeElement;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeSemiGroup;
import java.math.BigInteger;

/**
 * This abstract class provides a basis implementation for objects of type {@link MultiplicativeElement}.
 * <p>
 * @param <S> The generic type of the {@link MultiplicativeSemiGroup} of this element
 * @param <E> The generic type of this element
 * @param <V> The generic type of the value stored in this element
 * <p>
 * @author R. Haenni
 */
public abstract class AbstractMultiplicativeElement<S extends MultiplicativeSemiGroup<V>, E extends MultiplicativeElement<V>, V>
	   extends AbstractElement<S, E, V>
	   implements MultiplicativeElement<V> {

	private static final long serialVersionUID = 1L;

	protected AbstractMultiplicativeElement(final AbstractSet<E, V> semiGroup, final V value) {
		super(semiGroup, value);
	}

	@Override
	public final E multiply(final Element exponent) {
		return this.apply(exponent);
	}

	@Override
	public final E power(final long exponent) {
		return this.selfApply(exponent);
	}

	@Override
	public final E power(final Element<BigInteger> exponent) {
		return this.selfApply(exponent);
	}

	@Override
	public final E power(final BigInteger exponent) {
		return this.selfApply(exponent);
	}

	@Override
	public final E square() {
		return this.selfApply();
	}

	@Override
	public boolean isOne() {
		return this.isIdentity();
	}

	@Override
	public final E oneOver() {
		return this.invert();
	}

	@Override
	public final E divide(final Element element) {
		return this.applyInverse(element);
	}

	@Override
	public final E nthRoot(long amount) {
		return this.invertSelfApply(amount);
	}

	@Override
	public final E nthRoot(BigInteger amount) {
		return this.invertSelfApply(amount);
	}

	@Override
	public final E nthRoot(Element<BigInteger> amount) {
		return this.invertSelfApply(amount);
	}

	@Override
	public final E squareRoot() {
		return this.invertSelfApply();
	}

}
