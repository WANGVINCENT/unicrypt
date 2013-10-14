package ch.bfh.unicrypt.math.algebra.additive.interfaces;

import java.math.BigInteger;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;

/**
 * This interface provides the renaming of some group operations for the case of
 * an additively written semigroup. No functionality is added.
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public interface AdditiveSemiGroup extends SemiGroup {

  /**
   * This method is a synonym for {@link #Group.apply(Element, Element)}.
   *
   * @param element1 the same as in {@link #Group.apply(Element, Element)}
   * @param element2 the same as in {@link #Group.apply(Element, Element)}
   * @return the same as in {@link #Group.apply(Element, Element)}
   */
  public AdditiveElement add(Element element1, Element element2);

  /**
   * This method is a synonym for {@link #Group.apply(Element...)}.
   *
   * @param elements the same as in {@link #Group.apply(Element...)}
   * @return the same as in {@link #Group.apply(Element...)}
   */
  public AdditiveElement add(Element... elements);

  /**
   * This method is a synonym for {@link #Group.selfApply(Element, BigInteger)}.
   *
   * @param element the same as in {@link #Group.selfApply(Element, BigInteger)}
   * @param amount the same as in {@link #Group.selfApply(Element, BigInteger)}
   * @return the same as in {@link #Group.selfApply(Element, BigInteger)}
   */
  public AdditiveElement times(Element element, BigInteger amount);

  /**
   * This method is a synonym for {@link #Group.selfApply(Element, Element)}.
   *
   * @param element the same as in {@link #Group.selfApply(Element, Element)}
   * @param amount the same as in {@link #Group.selfApply(Element, Element)}
   * @return the same as in {@link #Group.selfApply(Element, Element)}
   */
  public AdditiveElement times(Element element, Element amount);

  /**
   * This method is a synonym for {@link #Group.selfApply(Element, int)}.
   *
   * @param element the same as in {@link #Group.selfApply(Element, int)}
   * @param amount the same as in {@link #Group.selfApply(Element, int)}
   * @return the same as in {@link #Group.selfApply(Element, int)}
   */
  public AdditiveElement times(Element element, int amount);

  /**
   * Applies the group operation to two instances of a given group element. This
   * is equivalent to {@code selfApply(element, 2)}.
   *
   * @param element A given group element
   * @return The result of applying the group operation to the input element
   * @throws IllegalArgumentException if {@code element} is null or does not
   * belong to the group
   */
  public AdditiveElement timesTwo(Element element);

  /**
   * Applies the binary operation pair-wise sequentially to the results of
   * computing {@link #selfApply(Element, BigInteger)} multiple times. In an
   * additive group, this operation is sometimes called 'weighed sum', and
   * 'product-of-powers' in a multiplicative group.
   *
   * @param elements A given array of elements
   * @param amounts Corresponding amounts
   * @return The result of this operation
   * @throws IllegalArgumentException if {@code elements} or one of its elements
   * is null
   * @throws IllegalArgumentException if {@code amounts} or one of its values is
   * null
   * @throws IllegalArgumentException if one of the elements of {@code elements}
   * does not belong to the group
   * @throws IllegalArgumentException if {@code elements} and {@code amounts}
   * have different lengths
   */
  public AdditiveElement sumOfProducts(Element[] elements, BigInteger[] amounts);

  // The following methods are overridden from SemiGroup with an adapted return type
  @Override
  public AdditiveElement apply(Element element1, Element element2);

  @Override
  public AdditiveElement apply(Element... elements);

  @Override
  public AdditiveElement selfApply(Element element, BigInteger amount);

  @Override
  public AdditiveElement selfApply(Element element, Element amount);

  @Override
  public AdditiveElement selfApply(Element element, int amount);

  @Override
  public AdditiveElement selfApply(Element element);

  @Override
  public AdditiveElement multiSelfApply(Element[] elements, BigInteger[] amounts);

}
