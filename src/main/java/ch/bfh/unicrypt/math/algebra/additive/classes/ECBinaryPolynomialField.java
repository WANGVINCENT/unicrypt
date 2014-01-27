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
package ch.bfh.unicrypt.math.algebra.additive.classes;

import ch.bfh.unicrypt.crypto.random.interfaces.RandomByteSequence;
import ch.bfh.unicrypt.math.algebra.additive.abstracts.AbstractEC;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.BinaryPolynomialElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.BinaryPolynomialField;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.helper.Point;
import ch.bfh.unicrypt.math.utility.MathUtil;
import java.math.BigInteger;

public class ECBinaryPolynomialField
	   extends AbstractEC<ECBinaryPolynomialFieldElement, BinaryPolynomialField, BinaryPolynomialElement> {

	public ECBinaryPolynomialField(BinaryPolynomialField finiteField, BinaryPolynomialElement a,
		   BinaryPolynomialElement b, BinaryPolynomialElement gx, BinaryPolynomialElement gy,
		   BigInteger givenOrder, BigInteger coFactor) {
		super(finiteField, a, b, gx, gy, givenOrder, coFactor);
		// this test should be moved to getInstance
		if (!isValid()) {
			throw new IllegalArgumentException("Curve parameters are not valid");
		}
	}

	public ECBinaryPolynomialField(BinaryPolynomialField finiteField, BinaryPolynomialElement a, BinaryPolynomialElement b,
		   BigInteger givenOrder, BigInteger coFactor) {
		super(finiteField, a, b, givenOrder, coFactor);
		// this test should be moved to getInstance
		if (!isValid()) {
			throw new IllegalArgumentException("Curve parameters are not valid");
		}
	}

	@Override
	protected boolean abstractContains(BinaryPolynomialElement x) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean abstractContains(BinaryPolynomialElement x, BinaryPolynomialElement y) {
		BinaryPolynomialElement left = y.power(2).add(x.multiply(getA()));
		BinaryPolynomialElement right = x.power(3).add(x.power(2).multiply(getA())).add(getB());
		return left.isEquivalent(right);
	}

	@Override
	protected ECBinaryPolynomialFieldElement abstractGetElement(Point<BinaryPolynomialElement> value) {
		return new ECBinaryPolynomialFieldElement(this, value);
	}

	@Override
	protected ECBinaryPolynomialFieldElement abstractGetIdentityElement() {
		return new ECBinaryPolynomialFieldElement(this);
	}

	@Override
	protected ECBinaryPolynomialFieldElement abstractApply(ECBinaryPolynomialFieldElement element1, ECBinaryPolynomialFieldElement element2) {
		if (element1.isZero()) {
			return element2;
		}
		if (element2.isZero()) {
			return element1;
		}
		if (element1.equals(element2.invert())) {
			return this.getIdentityElement();
		}
		BinaryPolynomialElement s, rx, ry;
		BinaryPolynomialElement px = element1.getX();
		BinaryPolynomialElement py = element1.getY();
		BinaryPolynomialElement qx = element2.getX();
		BinaryPolynomialElement qy = element2.getY();
		if (element1.equals(element2)) {
			final DualisticElement one = this.getFiniteField().getElement(1);
			s = px.add(py.divide(px));
			rx = s.power(2).add(s).add(this.getA());
			ry = px.power(2).add((s.add(one)).multiply(rx));
		} else {
			// TODO: Why is two not used???
			DualisticElement two = this.getFiniteField().getElement(2);
			s = py.add(qy).divide(px.add(qx));
			rx = s.power(2).add(s).add(px).add(qx).add(this.getA());
			ry = s.multiply(px.add(rx)).add(rx).add(py);
		}
		return this.abstractGetElement(Point.getInstance(rx, ry));
	}

	@Override
	protected ECBinaryPolynomialFieldElement abstractInvert(ECBinaryPolynomialFieldElement element) {
		if (element.isZero()) {
			return this.getZeroElement();
		}
		// TODO: The following seems to be incorrect!!!
		return this.abstractGetElement(Point.getInstance(element.getX(), element.getX().add(element.getY())));
	}

	@Override
	protected ECBinaryPolynomialFieldElement getRandomElementWithoutGenerator(RandomByteSequence randomByteSequence) {
		// TODO Auto-generated method stub
		return null;
	}

	private boolean isValid() {
		BigInteger m = new BigInteger("131"); //Must be set correctly
		final BigInteger TWO = new BigInteger("2");
		boolean c1, c2, c3, c4, c5, c6, c7, c81, c82;
		c1 = true; //-> Must be added!!
		c2 = !this.getB().equals(this.getFiniteField().getZeroElement());
		c3 = contains(this.getDefaultGenerator());
		c4 = MathUtil.arePrime(this.getOrder());
		c5 = 0 >= this.getCoFactor().compareTo(new BigInteger("4"));
		c6 = this.getDefaultGenerator().selfApply(getOrder()).isZero();
		c7 = this.getCoFactor().equals(MathUtil.sqrt(TWO.pow(m.intValue())).add(BigInteger.ONE).pow(2).divide(getOrder()));
		c81 = true;
		c82 = true;
		for (int i = 1; i < 20; i++) {
			BigInteger b = new BigInteger(String.valueOf(i));
			if (TWO.modPow(m.multiply(b), getOrder()).equals(BigInteger.ONE)) {
				c81 = false;
			}
			if (getOrder().multiply(getCoFactor()).equals(TWO.pow(m.intValue()))) {
				c82 = false;
			}
		}
		return c1 && c2 && c3 && c4 && c5 && c6 && c7 && c81 && c82;
	}

	/**
	 * Returns an elliptic curve over Fp y²=x³+ax+b
	 * <p>
	 * @param f          Finite field of type ZModPrime
	 * @param a          Element of F_p representing a in the curve equation
	 * @param b          Element of F_p representing b in the curve equation
	 * @param givenOrder Order of the the used subgroup
	 * @param coFactor   Co-factor h*order= N -> total order of the group
	 * @return
	 */
	public static ECBinaryPolynomialField getInstance(BinaryPolynomialField f, BinaryPolynomialElement a, BinaryPolynomialElement b, BigInteger givenOrder, BigInteger coFactor) {
		return new ECBinaryPolynomialField(f, a, b, givenOrder, coFactor);
	}

	/**
	 * Returns an elliptic curve over Fp y²=x³+ax+b
	 * <p>
	 * @param f          Finite field of type ZModPrime
	 * @param a          Element of F_p representing a in the curve equation
	 * @param b          Element of F_p representing b in the curve equation
	 * @param gx         x-coordinate of the generator
	 * @param gy         y-coordinate of the generator
	 * @param givenOrder Order of the the used subgroup
	 * @param coFactor   Co-factor h*order= N -> total order of the group
	 * @return
	 */
	public static ECBinaryPolynomialField getInstance(BinaryPolynomialField f, BinaryPolynomialElement a, BinaryPolynomialElement b, BinaryPolynomialElement gx, BinaryPolynomialElement gy, BigInteger givenOrder, BigInteger coFactor) {
		return new ECBinaryPolynomialField(f, a, b, gx, gy, givenOrder, coFactor);
	}

}
