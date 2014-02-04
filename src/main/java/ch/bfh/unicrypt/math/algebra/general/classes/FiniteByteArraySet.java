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
package ch.bfh.unicrypt.math.algebra.general.classes;

import ch.bfh.unicrypt.crypto.random.interfaces.RandomByteSequence;
import ch.bfh.unicrypt.math.algebra.general.abstracts.AbstractSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.helper.ByteArray;
import ch.bfh.unicrypt.math.helper.bytetree.ByteTree;
import ch.bfh.unicrypt.math.helper.bytetree.ByteTreeLeaf;
import ch.bfh.unicrypt.math.utility.ArrayUtil;
import java.math.BigInteger;
import java.util.LinkedList;

/**
 *
 * @author rolfhaenni
 */
public class FiniteByteArraySet
	   extends AbstractSet<FiniteByteArrayElement, ByteArray> {

	private final int minLength;
	private final int maxLength;

	protected FiniteByteArraySet(int minLength, int maxLength) {
		super(ByteArray.class);
		this.minLength = minLength;
		this.maxLength = maxLength;
	}

	public int getMinLength() {
		return this.minLength;
	}

	public int getMaxLength() {
		return this.maxLength;
	}

	public boolean fixedLength() {
		return this.getMinLength() == this.getMaxLength();
	}

	public final FiniteByteArrayElement getElement(byte[] bytes) {
		return this.getElement(ByteArray.getInstance(bytes));
	}

	@Override
	protected boolean abstractContains(ByteArray value) {
		return value.getLength() >= this.getMinLength() && value.getLength() <= this.getMaxLength();
	}

	@Override
	protected FiniteByteArrayElement abstractGetElement(ByteArray value) {
		return new FiniteByteArrayElement(this, value);
	}

	@Override
	protected FiniteByteArrayElement abstractGetElementFrom(BigInteger value) {
		if (value.compareTo(this.getOrder()) >= 0) {
			return null; // no such element
		}
		int minLength = this.getMinLength();
		BigInteger size = BigInteger.valueOf(1 << Byte.SIZE);
		LinkedList<Byte> byteList = new LinkedList<Byte>();
		while (!value.equals(BigInteger.ZERO) || byteList.size() < minLength) {
			if (byteList.size() >= minLength) {
				value = value.subtract(BigInteger.ONE);
			}
			byteList.addFirst(value.mod(size).byteValue());
			value = value.divide(size);
		}
		return this.abstractGetElement(ByteArray.getInstance(ArrayUtil.byteListToByteArray(byteList)));
	}

	@Override
	protected BigInteger abstractGetBigIntegerFrom(ByteArray value) {
		int length = value.getLength();
		int minLength = this.getMinLength();
		BigInteger result = BigInteger.ZERO;
		BigInteger size = BigInteger.valueOf(1 << Byte.SIZE);
		for (int i = 0; i < length; i++) {
			int intValue = value.getAt(i) & 0xFF;
			if (i < length - minLength) {
				intValue++;
			}
			result = result.multiply(size).add(BigInteger.valueOf(intValue));
		}
		return result;
	}

	@Override
	protected FiniteByteArrayElement abstractGetElementFrom(ByteTree bytTree) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	protected ByteTree abstractGetByteTreeFrom(ByteArray value) {
		return ByteTreeLeaf.getInstance(ByteArray.getInstance(value.getAll()));
	}

	@Override
	protected BigInteger abstractGetOrder() {
		BigInteger size = BigInteger.valueOf(1 << Byte.SIZE);
		BigInteger order = BigInteger.ONE;
		for (int i = 0; i < this.getMaxLength() - this.getMinLength(); i++) {
			order = order.multiply(size).add(BigInteger.ONE);
		}
		return order.multiply(size.pow(this.getMinLength()));
	}

	@Override
	protected FiniteByteArrayElement abstractGetRandomElement(RandomByteSequence randomByteSequence) {
		// this seems to be unnecessarly complicated, but is needed to generate shorer byte arrays with equal probability
		return this.abstractGetElementFrom(randomByteSequence.getRandomNumberGenerator().nextBigInteger(this.getOrder().subtract(BigInteger.ONE)));
	}

	@Override
	public boolean abstractEquals(final Set set) {
		final FiniteByteArraySet other = (FiniteByteArraySet) set;
		return this.getMinLength() == other.getMinLength() && this.getMaxLength() == other.getMaxLength();
	}

	@Override
	protected int abstractHashCode() {
		int hash = 7;
		hash = 47 * hash + this.getMinLength();
		hash = 47 * hash + this.getMaxLength();
		return hash;
	}

	//
	// STATIC FACTORY METHODS
	//
	public static FiniteByteArraySet getInstance(final int maxLength) {
		return FiniteByteArraySet.getInstance(0, maxLength);
	}

	public static FiniteByteArraySet getInstance(final int minLength, final int maxLength) {
		if (minLength < 0 || maxLength < minLength) {
			throw new IllegalArgumentException();
		}
		return new FiniteByteArraySet(minLength, maxLength);
	}

	public static FiniteByteArraySet getInstance(final BigInteger minOrder) {
		return FiniteByteArraySet.getInstance(minOrder, 0);
	}

	public static FiniteByteArraySet getInstance(final BigInteger minOrder, int minLength) {
		if (minOrder == null || minOrder.signum() < 0 || minLength < 0) {
			throw new IllegalArgumentException();
		}
		int maxLength = minLength;
		BigInteger size = BigInteger.valueOf(1 << Byte.SIZE);
		BigInteger order1 = size.pow(minLength);
		BigInteger order2 = BigInteger.ONE;
		while (order1.multiply(order2).compareTo(minOrder) < 0) {
			order2 = order2.multiply(size).add(BigInteger.ONE);
			maxLength++;
		}
		return new FiniteByteArraySet(minLength, maxLength);
	}

}
