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
package ch.bfh.unicrypt.math.function.classes;

import ch.bfh.unicrypt.helper.converter.classes.TrivialConverter;
import ch.bfh.unicrypt.helper.converter.interfaces.Converter;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.random.interfaces.RandomByteSequence;

/**
 * This class represents the the concept of a function f:X->Y, which outputs the element of Y that corresponds to the
 * integer value of the input element.
 * <p/>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public class ConvertFunction
	   extends AbstractFunction<ConvertFunction, Set, Element, Set, Element> {

	private static final long serialVersionUID = 1L;

	private final Converter domainConverter;
	private final Converter coDomainConverter;

	public enum Mode {

		BIG_INTEGER, STRING, BYTE_ARRAY

	};

	private ConvertFunction(final Set domain, final Set coDomain, Converter domainConverter, Converter coDomainConverter) {
		super(domain, coDomain);
		this.domainConverter = domainConverter;
		this.coDomainConverter = coDomainConverter;
	}

	@Override
	protected Element abstractApply(Element element, RandomByteSequence randomByteSequence) {
		Object value = element.convertTo(this.domainConverter);
		Element result = this.getCoDomain().getElementFrom(value, this.coDomainConverter);
		if (result == null) {
			throw new IllegalArgumentException();
		}
		return result;
	}

	public static <V> ConvertFunction getInstance(Set<V> domain, Set<V> coDomain) {
		if (domain == null || coDomain == null) {
			throw new IllegalArgumentException();
		}
		return new ConvertFunction(domain, coDomain, TrivialConverter.<V>getInstance(), TrivialConverter.<V>getInstance());
	}

	public static <V, W> ConvertFunction getInstance(final Set<V> domain, final Set<W> coDomain, Converter<V, W> converter) {
		if (domain == null || coDomain == null || converter == null) {
			throw new IllegalArgumentException();
		}
		return new ConvertFunction(domain, coDomain, converter, TrivialConverter.<W>getInstance());
	}

	public static <V, W, X> ConvertFunction getInstance(Set<V> domain, Set<W> domainConverter, Converter<V, X> converter1, Converter<W, X> coDomainConverter) {
		if (domain == null || domainConverter == null || converter1 == null || coDomainConverter == null) {
			throw new IllegalArgumentException();
		}
		return new ConvertFunction(domain, domainConverter, converter1, coDomainConverter);
	}

	public static ConvertFunction getInstance(Set domain, Set coDomain, Mode mode) {
		if (domain == null || coDomain == null || mode == null) {
			throw new IllegalArgumentException();
		}
		Converter converter1;
		Converter converter2;
		switch (mode) {
			case BIG_INTEGER:
				converter1 = domain.getBigIntegerConverter();
				converter2 = coDomain.getBigIntegerConverter();
				break;
			case STRING:
				converter1 = domain.getStringConverter();
				converter2 = coDomain.getStringConverter();
				break;
			case BYTE_ARRAY:
				converter1 = domain.getByteArrayConverter();
				converter2 = coDomain.getByteArrayConverter();
				break;
			default:
				throw new IllegalStateException();
		}
		return new ConvertFunction(domain, coDomain, converter1, converter2);
	}

}
