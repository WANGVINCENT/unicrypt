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
package ch.bfh.unicrypt.helper.converter.classes.biginteger;

import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.converter.abstracts.AbstractBigIntegerConverter;
import ch.bfh.unicrypt.helper.math.MathUtil;
import ch.bfh.unicrypt.helper.array.classes.BitArray;
import java.math.BigInteger;

/**
 * The single instance of this class converts bit arrays into non-negative {@code BigInteger} values 0, 1, 2, ...
 * <p>
 * @author R. Haenni
 * @version 2.0
 */
public class BitArrayToBigInteger
	   extends AbstractBigIntegerConverter<BitArray> {

	private static BitArrayToBigInteger instance = null;

	protected BitArrayToBigInteger() {
		super(BitArray.class);
	}

	/**
	 * Returns the single instance of this class.
	 * <p>
	 * @return The single instance
	 */
	public static BitArrayToBigInteger getInstance() {
		if (instance == null) {
			instance = new BitArrayToBigInteger();
		}
		return instance;
	}

	@Override
	protected boolean defaultIsValidOutput(BigInteger value) {
		return value.signum() >= 0;
	}

	@Override
	public BigInteger abstractConvert(BitArray bitarray) {
		BigInteger value1 = MathUtil.powerOfTwo(bitarray.getLength()).subtract(BigInteger.ONE);
		BigInteger value2 = new BigInteger(1, bitarray.reverse().getByteArray().reverse().getBytes());
		return value1.add(value2);
	}

	@Override
	public BitArray abstractReconvert(BigInteger value) {
		int length = value.add(BigInteger.ONE).bitLength() - 1;
		BigInteger value1 = MathUtil.powerOfTwo(length).subtract(BigInteger.ONE);
		BigInteger value2 = value.subtract(value1);
		ByteArray byteArray = ByteArray.getInstance(value2.toByteArray()).removePrefix();
		BitArray result = BitArray.getInstance(byteArray.reverse()).reverse();
		return result.shiftRight(length - result.getLength());
	}

}
