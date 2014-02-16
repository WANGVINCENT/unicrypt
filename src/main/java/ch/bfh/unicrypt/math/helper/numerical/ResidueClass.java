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
package ch.bfh.unicrypt.math.helper.numerical;

import ch.bfh.unicrypt.math.utility.MathUtil;
import java.math.BigInteger;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class ResidueClass
	   extends Numerical<ResidueClass> {

	private final BigInteger modulus;

	protected ResidueClass(BigInteger bigInteger, BigInteger modulus) {
		super(bigInteger);
		this.modulus = modulus;
	}

	public BigInteger getModulus() {
		return this.modulus;
	}

	public boolean isRelativelyPrime() {
		return MathUtil.areRelativelyPrime(this.bigInteger, this.modulus);
	}

	public ResidueClass invert() {
		// test for relative primality is included in modInverse
		return new ResidueClass(this.bigInteger.modInverse(this.modulus), this.modulus);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		ResidueClass other = (ResidueClass) obj;
		return this.bigInteger.equals(other.bigInteger) && this.modulus.equals(other.modulus);
	}

	@Override
	public int hashCode() {
		int hash = 17;
		hash = 17 * hash + this.bigInteger.hashCode();
		hash = 17 * hash + this.modulus.hashCode();
		return hash;
	}

	@Override
	protected String defaultToStringValue() {
		return this.bigInteger.toString() + "mod" + this.modulus.toString();
	}

	@Override
	protected boolean abstractIsCompatible(ResidueClass other) {
		return this.modulus.equals(other.modulus);
	}

	@Override
	protected ResidueClass abstractAdd(ResidueClass other) {
		return new ResidueClass(this.bigInteger.add(other.bigInteger).mod(this.modulus), this.modulus);
	}

	@Override
	protected ResidueClass abstractMultiply(ResidueClass other) {
		return new ResidueClass(this.bigInteger.multiply(other.bigInteger).mod(this.modulus), this.modulus);
	}

	@Override
	protected ResidueClass abstractSubtract(ResidueClass other) {
		return new ResidueClass(this.bigInteger.subtract(other.bigInteger).mod(this.modulus), this.modulus);
	}

	@Override
	protected ResidueClass abstractNegate() {
		return new ResidueClass(this.modulus.subtract(this.bigInteger).mod(this.modulus), this.modulus);
	}

	@Override
	protected ResidueClass abstractTimes(BigInteger factor) {
		return new ResidueClass(this.bigInteger.multiply(factor).mod(this.modulus), this.modulus);
	}

	@Override
	protected ResidueClass abstractPower(BigInteger exponent) {
		return new ResidueClass(this.bigInteger.modPow(exponent, this.modulus), this.modulus);
	}

	public static ResidueClass getInstance(int integer, int modulus) {
		return ResidueClass.getInstance(BigInteger.valueOf(integer), BigInteger.valueOf(modulus));
	}

	public static ResidueClass getInstance(BigInteger integer, BigInteger modulus) {
		if (integer == null || modulus == null || modulus.signum() <= 0) {
			throw new IllegalArgumentException();
		}
		return new ResidueClass(integer.mod(modulus), modulus);
	}

}