package ch.bfh.unicrypt.math.algebra.general.interfaces;

import ch.bfh.unicrypt.crypto.random.interfaces.RandomGenerator;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.Field;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.ZStarMod;
import java.math.BigInteger;

/**
 * This interface represents the concept a mathematical set of elements. The number of elements in the set is called
 * order. The order may be infinite or unknown. It is assumed that each element of a set corresponds to a unique
 * BigInteger value. Therefore, the interface provides methods for converting elements into corresponding BigInteger
 * values and back.
 * <p>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public interface Set {

	/**
	 * A constant value that represents an infinite order.
	 */
	public static final BigInteger INFINITE_ORDER = BigInteger.valueOf(-1);
	/**
	 * A constant value that represents an unknown order.
	 */
	public static final BigInteger UNKNOWN_ORDER = BigInteger.ZERO;

	/**
	 * Returns {@code true} if this set is an instance of {@link SemiGroup}.
	 * <p>
	 * @return {@code true} if this set is a semigroup
	 */
	public boolean isSemiGroup();

	/**
	 * Returns {@code true} if this set is an instance of {@link Monoid}.
	 * <p>
	 * @return {@code true} if this set is a monoid
	 */
	public boolean isMonoid();

	/**
	 * Returns {@code true} if this set is an instance of {@link Group}.
	 * <p>
	 * @return {@code true} if this set is a group
	 */
	public boolean isGroup();

	/**
	 * Returns {@code true} if this set is an instance of {@link SemiRing}.
	 * <p>
	 * @return {@code true} if this set is a semiring
	 */
	public boolean isSemiRing();

	/**
	 * Returns {@code true} if this set is an instance of {@link Ring}.
	 * <p>
	 * @return {@code true} if this set is a ring
	 */
	public boolean isRing();

	/**
	 * Returns {@code true} if this set is an instance of {@link Field}.
	 * <p>
	 * @return {@code true} if this set is a field
	 */
	public boolean isField();

	/**
	 * Returns {@code true} if this set is an instance of {@link CyclicGroup}.
	 * <p>
	 * @return {@code true} if this set is cyclic
	 */
	public boolean isCyclic();

	/**
	 * Returns {@code true} if this set is an instance of {@link AdditiveSemiGroup}.
	 * <p>
	 * @return {@code true} if this set is additive
	 */
	public boolean isAdditive();

	/**
	 * Returns {@code true} if this set is an instance of {@link MultiplicativeSemiGroup}.
	 * <p>
	 * @return {@code true} if this set is multiplicative
	 */
	public boolean isMultiplicative();

	/**
	 * Returns {@code true} if this set is an instance of {@link ConcatenativeSemiGroup}.
	 * <p>
	 * @return {@code true} if this set is concatenative
	 */
	public boolean isConcatenative();

	/**
	 * TODO Returns {@code true} if this set is an instance of {@link Compound}.
	 * <p>
	 * @return {@code true} if this set is compound
	 */
	public boolean isProduct();

	/**
	 * TODO Returns {@code true} if this set is of finite order.
	 * <p>
	 * @return {@code true} if this set is finite
	 */
	public boolean isFinite();

	/**
	 * TODO Returns {@code true} if this set has a known order.
	 * <p>
	 * @return {@code true} if this set has a known order
	 */
	public boolean hasKnownOrder();

	/**
	 * Returns the group order. If the group order is unknown, {@link #UNKNOWN_ORDER} is returned. If the group order is
	 * infinite, {@link #INFINITE_ORDER} is returned.
	 * <p>
	 * @see "Handbook of Applied Cryptography, Definition 2.163"
	 * @return The group order
	 */
	public BigInteger getOrder();

	/**
	 * TODO Returns a lower bound for the group order in case the exact group order is unknown. The least return value is
	 * 0. Otherwise, if the exact group order is known (or infinite), the exact group order is returned.
	 * <p>
	 * @return A lower bound for the group order
	 */
	public BigInteger getOrderLowerBound();

	/**
	 * TODO Returns an upper bound for the group order in case the exact group order is unknown. The heighest return value
	 * is -1. Otherwise, if the exact group order is known (or infinite), the exact group order is returned.
	 * <p>
	 * @return A upper bound for the group order
	 */
	public BigInteger getOrderUpperBound();

	/**
	 * TODO Returns the minimal order. The least value is 0.
	 * <p>
	 * @return
	 */
	public BigInteger getMinimalOrder();

	/**
	 * Checks if the set is of order 0.
	 * <p>
	 * @return {@literal true} if the order is 0, {@literal false} otherwise
	 */
	public boolean isEmpty();

	/**
	 * Checks if the set is of order 1.
	 * <p>
	 * @return {@literal true} if the order is 1, {@literal false} otherwise
	 */
	public boolean isSingleton();

	/**
	 * Returns an additive integer group of type {@link ZPlusMod} with the same group order. For this to work, the group
	 * order must be finite and known.
	 * <p>
	 * @return The resulting additive group.
	 * @throws UnsupportedOperationException if the group order is infinite or unknown
	 */
	public ZMod getZModOrder();

	/**
	 * Returns an multiplicative integer group of type {@link ZTimesMod} with the same group order. For this to work, the
	 * group order must be finite and known.
	 * <p>
	 * @return The resulting multiplicative group.
	 * @throws UnsupportedOperationException if the group order is infinite or unknown
	 */
	public ZStarMod getZStarModOrder();

	/**
	 * Checks if {@literal this} set contains an element that corresponds to a given integer value.
	 * <p>
	 * @param value The given integer value
	 * @return {@literal true} if such an element exists, {@literal false} otherwise
	 */
	public boolean contains(int value);

	/**
	 * Checks if {@literal this} set contains an element that corresponds to a given BigInteger value.
	 * <p>
	 * @param value The given BigInteger value
	 * @return {@literal true} if such an element exists, {@literal false} otherwise
	 * @throws IllegalArgumentException if {@literal value} is null
	 */
	public boolean contains(BigInteger value);

	/**
	 * Checks if a given element belongs to the group.
	 * <p>
	 * @param element The given element
	 * @return {@literal true} if {@literal element} belongs to the group, {@literal false} otherwise
	 * @throws IllegalArgumentException if {@literal element} is null
	 */
	public boolean contains(Element element);

	/**
	 * Creates and returns the element that corresponds to a given integer (if one exists).
	 * <p>
	 * @param value The given integer
	 * @return The corresponding element
	 * @throws IllegalArgumentException if no such element exists
	 */
	public Element getElement(int value);

	/**
	 * Creates and returns the group element that corresponds to a given BigInteger value (if one exists).
	 * <p>
	 * @param value The given BigInteger value
	 * @return The corresponding group element
	 * @throws IllegalArgumentException if {@literal value} is null or if no such element exists in {@literal this} group
	 */
	public Element getElement(BigInteger value);

	/**
	 * Creates and returns the group element that corresponds to the integer value of or some other group element (if one
	 * exists).
	 * <p>
	 * @param element The given group element
	 * @return The corresponding group element of this set
	 * @throws IllegalArgumentException if {@literal element} is null or if no such element exists in {@literal this}
	 *                                  group
	 */
	public Element getElement(Element element);

	/**
	 * Selects and returns a random group element using the default random generator. For finite order group, it is
	 * selected uniformly at random. For groups of infinite or unknown order, the underlying probability distribution is
	 * not further specified.
	 * <p>
	 * @return A random group element
	 */
	public Element getRandomElement();

	/**
	 * Selects and returns a random group element using a given random generator. If no random generator is specified,
	 * i.e., if {@literal random} is null, then the system-wide random generator is taken. For finite order group, it is
	 * selected uniformly at random. For groups of infinite or unknown order, the underlying probability distribution is
	 * not generally specified.
	 * <p>
	 * @param randomGenerator Either {@literal null} or a given random generator
	 * @return A random group element
	 */
	public Element getRandomElement(RandomGenerator randomGenerator);

	/**
	 * Checks if two given elements of this group are equal.
	 * <p>
	 * @param element1 The first element
	 * @param element2 The second element
	 * @return {@literal true} if the elements are equal and belong to the group, {@literal false} otherwise
	 * @throws IllegalArgumentException if {@literal element1} or {@literal element2} is null
	 */
	public boolean areEqual(Element element1, Element element2);

	/**
	 *
	 * @param set
	 * @return
	 */
	public boolean isCompatible(Set set);

	/**
	 *
	 * @param set
	 * @return
	 */
	public boolean isEqual(Set set);

}
