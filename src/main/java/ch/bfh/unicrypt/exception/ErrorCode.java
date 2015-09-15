/*
 * UniCrypt
 *
 *  UniCrypt(tm): Cryptographic framework allowing the implementation of cryptographic protocols, e.g. e-voting
 *  Copyright (C) 2015 Bern University of Applied Sciences (BFH), Research Institute for
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
package ch.bfh.unicrypt.exception;

/**
 *
 * @author rolfhaenni
 */
public enum ErrorCode {

	// error codes for checked exceptions
	ELEMENT_CONVERSION_FAILURE,
	// error codes for runtime exceptions
	DIVISION_BY_ZERO,
	DUPLICATE_VALUE,
	ELEMENT_CONSTRUCTION_FAILURE,
	IMPOSSIBLE_STATE,
	INCOMPATIBLE_ARGUMENTS,
	INVALID_AMOUNT,
	INVALID_ARGUMENT,
	INVALID_BITLENGTH,
	INVALID_ELEMENT,
	INVALID_DEGREE,
	INVALID_INDEX,
	INVALID_LENGTH,
	INVALID_METHOD_CALL,
	JAVA_AES_FAILURE,
	NEGATIVE_VALUE,
	NO_SOLUTION,
	NOT_YET_IMPLEMENTED,
	NULL_POINTER,
	OBJECT_NOT_FOUND,
	PROBABILISTIC_ENCODING_FAILURE,
	SET_CONSTRUCTION_FAILURE,
	UNSUPPORTED_OPERATION

}
