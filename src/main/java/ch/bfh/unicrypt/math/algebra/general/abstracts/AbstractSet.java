package ch.bfh.unicrypt.math.algebra.general.abstracts;

import ch.bfh.unicrypt.math.algebra.additive.interfaces.AdditiveSemiGroup;
import ch.bfh.unicrypt.math.algebra.concatenative.interfaces.ConcatenativeSemiGroup;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrime;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.Field;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.Ring;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.SemiRing;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Monoid;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.ZStarMod;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.ZStarModPrime;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeSemiGroup;
import ch.bfh.unicrypt.math.helper.Compound;
import ch.bfh.unicrypt.math.helper.UniCrypt;
import ch.bfh.unicrypt.math.utility.MathUtil;
import java.math.BigInteger;
import java.util.Random;

/**
 * This abstract class provides a basis implementation for atomic sets.
 * <p>
 * @param <E>
 * @see AbstractElement
 * <p>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public abstract class AbstractSet<E extends Element>
			 extends UniCrypt
			 implements Set {

	private BigInteger order, lowerBound, upperBound, minimum;

	@Override
	public final boolean isSemiGroup() {
		return this instanceof SemiGroup;
	}

	@Override
	public final boolean isMonoid() {
		return this instanceof Monoid;
	}

	@Override
	public final boolean isGroup() {
		return this instanceof Group;
	}

	@Override
	public final boolean isSemiRing() {
		return this instanceof SemiRing;
	}

	@Override
	public final boolean isRing() {
		return this instanceof Ring;
	}

	@Override
	public final boolean isField() {
		return this instanceof Field;
	}

	@Override
	public final boolean isCyclic() {
		return this instanceof CyclicGroup;
	}

	@Override
	public boolean isAdditive() {
		return this instanceof AdditiveSemiGroup;
	}

	@Override
	public boolean isMultiplicative() {
		return this instanceof MultiplicativeSemiGroup;
	}

	@Override
	public boolean isConcatenative() {
		return this instanceof ConcatenativeSemiGroup;
	}

	@Override
	public final boolean isProduct() {
		return this instanceof Compound;
	}

	@Override
	public final boolean isFinite() {
		return !this.getOrder().equals(Set.INFINITE_ORDER);
	}

	@Override
	public final boolean hasKnownOrder() {
		return !this.getOrder().equals(Set.UNKNOWN_ORDER);
	}

	@Override
	public final BigInteger getOrder() {
		if (this.order == null) {
			this.order = this.abstractGetOrder();
		}
		return this.order;
	}

	@Override
	public final BigInteger getOrderLowerBound() {
		if (this.lowerBound == null) {
			if (this.hasKnownOrder()) {
				this.lowerBound = this.getOrder();
			} else {
				this.lowerBound = this.standardGetOrderLowerBound();
			}
		}
		return this.lowerBound;
	}

	@Override
	public final BigInteger getOrderUpperBound() {
		if (this.upperBound == null) {
			if (this.hasKnownOrder()) {
				this.upperBound = this.getOrder();
			} else {
				this.upperBound = this.standardGetOrderUpperBound();
			}
		}
		return this.upperBound;
	}

	@Override
	public final BigInteger getMinimalOrder() {
		if (this.minimum == null) {
			this.minimum = this.standardGetMinimalOrder();
		}
		return this.minimum;
	}

	@Override
	public final boolean isEmpty() {
		return this.getOrder().equals(BigInteger.ZERO);
	}

	@Override
	public final boolean isSingleton() {
		return this.getOrder().equals(BigInteger.ONE);
	}

	@Override
	public final ZMod getZModOrder() {
		if (!(this.isFinite() && this.hasKnownOrder())) {
			throw new UnsupportedOperationException();
		}
		if (MathUtil.isPrime(this.getOrder())) {
			return ZModPrime.getInstance(this.getOrder());
		}
		return ZMod.getInstance(this.getOrder());
	}

	@Override
	public final ZStarMod getZStarModOrder() {
		if (!(this.isFinite() && this.hasKnownOrder())) {
			throw new UnsupportedOperationException();
		}
		if (MathUtil.isPrime(this.getOrder())) {
			return ZStarModPrime.getInstance(this.getOrder());
		}
		return ZStarMod.getInstance(this.getOrder());
	}

	@Override
	public final boolean contains(final int value) {
		return this.contains(BigInteger.valueOf(value));
	}

	@Override
	public final boolean contains(final BigInteger value) {
		if (value == null) {
			throw new IllegalArgumentException();
		}
		return this.abstractContains(value);
	}

	@Override
	public final boolean contains(final Element element) {
		if (element == null) {
			throw new IllegalArgumentException();
		}
		return this.isEqual(element.getSet());
	}

	@Override
	public final E getElement(final int value) {
		return this.getElement(BigInteger.valueOf(value));
	}

	@Override
	public final E getElement(BigInteger value) {
		if (value == null || !this.contains(value)) {
			throw new IllegalArgumentException();
		}
		return this.abstractGetElement(value);
	}

	@Override
	public final E getElement(final Element element) {
		if (element == null) {
			throw new IllegalArgumentException();
		}
		if (this.contains(element)) {
			return (E) element;
		}
		return this.getElement(element.getValue());
	}

	@Override
	public final E getRandomElement() {
		return this.getRandomElement(null);
	}

	@Override
	public final E getRandomElement(Random random) {
		return this.abstractGetRandomElement(random);
	}

	@Override
	public final boolean areEqual(final Element element1, final Element element2) {
		if (!this.contains(element1) || !this.contains(element2)) {
			throw new IllegalArgumentException();
		}
		return element1.isEqual(element2);
	}

	@Override
	public final boolean isCompatible(Set set) {
		if (set == null) {
			throw new IllegalArgumentException();
		}
		return standardIsCompatible(set);
	}

	@Override
	public final boolean isEqual(final Set set) {
		if (set == null) {
			throw new IllegalArgumentException();
		}
		if (this == set) {
			return true;
		}
		if (!this.isCompatible(set)) {
			return false;
		}
		return this.standardIsEqual(set);
	}

	//
	// The following protected methods are standard implementations for sets.
	// They may need to be changed in certain sub-classes.
	//
	protected BigInteger standardGetOrderLowerBound() {
		return BigInteger.ZERO;
	}

	protected BigInteger standardGetOrderUpperBound() {
		return Set.INFINITE_ORDER;
	}

	protected BigInteger standardGetMinimalOrder() {
		return this.getOrderLowerBound();
	}

	protected boolean standardIsCompatible(Set set) {
		return this.getClass() == set.getClass();
	}

	protected boolean standardIsEqual(Set set) {
		return true;
	}

	//
	// The following protected abstract method must be implemented in every direct
	// sub-class.
	//
	protected abstract BigInteger abstractGetOrder();

	protected abstract E abstractGetElement(BigInteger value);

	protected abstract E abstractGetRandomElement(Random random);

	protected abstract boolean abstractContains(BigInteger value);

}
