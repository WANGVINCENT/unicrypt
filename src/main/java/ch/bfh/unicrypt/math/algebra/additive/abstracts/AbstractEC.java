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
package ch.bfh.unicrypt.math.algebra.additive.abstracts;

import ch.bfh.unicrypt.helper.Point;
import ch.bfh.unicrypt.math.MathUtil;
import ch.bfh.unicrypt.math.algebra.additive.classes.ECElement;
import ch.bfh.unicrypt.math.algebra.additive.interfaces.EC;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.FiniteField;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.random.interfaces.RandomByteSequence;
import java.math.BigInteger;

public abstract class AbstractEC<F extends FiniteField, V extends Object>
	   extends AbstractAdditiveCyclicGroup<ECElement<V>, Point<DualisticElement<V>>>
	   implements EC<V> {

	private final F finiteField;
	private final DualisticElement<V> a, b;
	private final ECElement<V> givenGenerator;
	private final BigInteger givenOrder, coFactor;
	private final Point<DualisticElement<V>> infinityPoint = Point.<DualisticElement<V>>getInstance();

	protected AbstractEC(F finiteField, DualisticElement<V> a, DualisticElement<V> b, DualisticElement<V> gx, DualisticElement<V> gy, BigInteger givenOrder, BigInteger coFactor) {
		super(Point.class);
		this.finiteField = finiteField;
		this.a = a;
		this.b = b;
		this.givenOrder = givenOrder;
		this.coFactor = coFactor;
		this.givenGenerator = this.getElement(gx, gy);
	}

	protected AbstractEC(F finitefield, DualisticElement<V> a, DualisticElement<V> b, BigInteger givenOrder, BigInteger coFactor) {
		super(Pair.class);
		this.finiteField = finitefield;
		this.a = a;
		this.b = b;
		this.givenOrder = givenOrder;
		this.coFactor = coFactor;
		this.givenGenerator = this.computeGenerator();
	}

	@Override
	public final F getFiniteField() {
		return this.finiteField;
	}

	@Override
	public final DualisticElement<V> getB() {
		return this.b;
	}

	@Override
	public final DualisticElement<V> getA() {
		return this.a;
	}

	@Override
	public final BigInteger getCoFactor() {
		return this.coFactor;
	}

	@Override
	public final boolean contains(DualisticElement xValue) {
		if (xValue == null || !this.getFiniteField().contains(xValue)) {
			throw new IllegalArgumentException();
		}
		return this.abstractContains((DualisticElement<V>) xValue);
	}

	protected abstract boolean abstractContains(DualisticElement<V> xValue);

	@Override
	public final boolean contains(DualisticElement xValue, DualisticElement yValue) {
		if (xValue == null || yValue == null || !this.getFiniteField().contains(xValue) || !this.getFiniteField().contains(yValue)) {
			throw new IllegalArgumentException();
		}
		return this.abstractContains((DualisticElement<V>) xValue, (DualisticElement<V>) yValue);
	}

	protected abstract boolean abstractContains(DualisticElement<V> xValue, DualisticElement<V> yValue);

	@Override
	public final ECElement<V> getElement(DualisticElement xValue, DualisticElement yValue) {
		if (!this.contains(xValue, yValue)) {
			throw new IllegalArgumentException();
		}
		return this.abstractGetElement(Point.getInstance((DualisticElement<V>) xValue, (DualisticElement<V>) yValue));
	}

	@Override
	protected BigInteger abstractGetOrder() {
		return this.givenOrder;
	}

	@Override
	protected boolean abstractContains(Point<DualisticElement<V>> value) {
		return this.abstractContains(value.getX(), value.getY());
	}

	@Override
	protected ECElement<V> abstractGetElementFrom(BigInteger integerValue) {
		if (integerValue.equals(BigInteger.ZERO)) {
			return this.getZeroElement();
		}
		BigInteger[] result = MathUtil.unpair(integerValue.subtract(BigInteger.ONE));
		DualisticElement<V> xValue = this.getFiniteField().getElementFrom(result[0]);
		DualisticElement<V> yValue = this.getFiniteField().getElementFrom(result[1]);
		if (xValue == null || yValue == null) {
			return null; // no such element
		}
		return this.getElement(xValue, yValue);
	}

	@Override
	protected BigInteger abstractGetBigIntegerFrom(ECElement<V> element) {
		Point<DualisticElement<V>> point = element.getValue();
		if (point.equals(this.infinityPoint)) {
			return BigInteger.ZERO;
		}
		return MathUtil.pair(point.getX().getBigInteger(), point.getY().getBigInteger()).add(BigInteger.ONE);
	}

	@Override
	protected ECElement<V> abstractGetDefaultGenerator() {
		return this.givenGenerator;
	}

	private ECElement<V> computeGenerator() {
		ECElement<V> element = this.selfApply(this.getRandomElement(), this.getCoFactor());
		while (!this.isGenerator(element)) {
			element = this.getRandomElement();
		}
		return element;
	}

	@Override
	protected boolean abstractIsGenerator(ECElement<V> element) {
		return MathUtil.isPrime(this.getOrder()) && this.selfApply(element, this.getOrder()).isZero();
	}

	@Override
	protected ECElement<V> abstractGetRandomElement(RandomByteSequence randomByteSequence) {
		if (this.getDefaultGenerator() != null) {
			DualisticElement<V> r = this.getFiniteField().getRandomElement(randomByteSequence);
			// TODO ?!?
			//return this.getDefaultGenerator().selfApply(r);
			return this.getDefaultGenerator().selfApply(r.getBigInteger());
		} else {
			return this.getRandomElementWithoutGenerator(randomByteSequence);
		}
	}

	@Override
	protected boolean abstractEquals(Set set) {
		AbstractEC<F, V> other = (AbstractEC<F, V>) set;
		if (!this.finiteField.isEquivalent(other.finiteField)) {
			return false;
		}
		if (!this.a.equals(other.a)) {
			return false;
		}
		if (!this.b.equals(other.b)) {
			return false;
		}
		if (!this.givenOrder.equals(other.givenOrder)) {
			return false;
		}
		if (!this.coFactor.equals(other.coFactor)) {
			return false;
		}
		if (!this.givenGenerator.equals(other.givenGenerator)) {
			return false;
		}
		return true;
	}

	@Override
	protected int abstractHashCode() {
		int hash = 7;
		hash = 47 * hash + this.finiteField.hashCode();
		hash = 47 * hash + this.a.hashCode();
		hash = 47 * hash + this.b.hashCode();
		hash = 47 * hash + this.givenOrder.hashCode();
		hash = 47 * hash + this.coFactor.hashCode();
		hash = 47 * hash + this.givenGenerator.hashCode();
		return hash;
	}

	@Override
	protected boolean defaultIsEquivalent(Set set) {
		AbstractEC<F, V> other = (AbstractEC<F, V>) set;
		if (!this.finiteField.isEquivalent(other.finiteField)) {
			return false;
		}
		if (!this.a.isEquivalent(other.a)) {
			return false;
		}
		if (!this.b.isEquivalent(other.b)) {
			return false;
		}
		if (!this.givenOrder.equals(other.givenOrder)) {
			return false;
		}
		if (!this.coFactor.equals(other.coFactor)) {
			return false;
		}
		return true;
	}

	/**
	 * Returns random element witcoFactorout knowing a generator of tcoFactore group
	 * <p>
	 * @param randomByteSequence
	 * @return
	 */
	protected abstract ECElement<V> getRandomElementWithoutGenerator(RandomByteSequence randomByteSequence);

	@Override
	protected String defaultToStringValue() {
		return this.getA().getValue() + "," + this.getB().getValue();
	}

}
