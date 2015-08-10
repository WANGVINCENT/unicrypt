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
package ch.bfh.unicrypt.helper.converter.classes.bytearray;

import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.converter.abstracts.AbstractByteArrayConverter;

/**
 * Instance of this class convert {@code ByteArray} values into {@code ByteArray} values. There are four operating
 * modes, one in which the byte arrays remain unchanged, one in which the bytes of the byte arrays are reversed, one in
 * which the bits of the bytes in the byte arrays are reversed, and one in which both the bytes and the bits in the byte
 * arrays are reversed.
 * <p>
 * @author R. Haenni
 * @version 2.0
 */
public class ByteArrayToByteArray
	   extends AbstractByteArrayConverter<ByteArray> {
	private static final long serialVersionUID = 1L;

	private final boolean reverse;
	private final boolean bitReverse;

	private ByteArrayToByteArray(boolean reverse, boolean bitReverse) {
		super(ByteArray.class);
		this.reverse = reverse;
		this.bitReverse = bitReverse;
	}

	/**
	 * Returns a new default {@link ByteArrayToByteArray} converter, which leaves the byte arrays unchanged.
	 * <p>
	 * @return The default converter
	 */
	public static ByteArrayToByteArray getInstance() {
		return new ByteArrayToByteArray(false, false);
	}

	/**
	 * Returns a new {@link ByteArrayToByteArray} converter for a given flag {@code reverse} indicating if the byte
	 * arrays are reversed. The bits remain unchanged.
	 * <p>
	 * @param reverse The flag indicating if the byte arrays are reversed
	 * @return The new converter
	 */
	public static ByteArrayToByteArray getInstance(boolean reverse) {
		return new ByteArrayToByteArray(reverse, false);
	}

	/**
	 * Returns a new {@link ByteArrayToByteArray} converter for a given flag {@code reverse} indicating if the bytes are
	 * reversed and another flag {@code bitReverse} indicating if the bits in each byte are reversed.
	 * <p>
	 * @param reverse    A flag indicating if the bytes are reversed
	 * @param bitReverse A flag indicating if the bits in each byte are reversed
	 * @return The new converter
	 */
	public static ByteArrayToByteArray getInstance(boolean reverse, boolean bitReverse) {
		return new ByteArrayToByteArray(reverse, bitReverse);
	}

	@Override
	protected ByteArray abstractConvert(ByteArray byteArray) {
		ByteArray result = this.bitReverse ? byteArray.bitReverse() : byteArray;
		if (this.reverse) {
			return result.reverse();
		}
		return result;
	}

	@Override
	protected ByteArray abstractReconvert(ByteArray byteArray) {
		ByteArray result = this.bitReverse ? byteArray.bitReverse() : byteArray;
		if (this.reverse) {
			return result.reverse();
		}
		return result;
	}

}
