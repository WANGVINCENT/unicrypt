/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.product.classes;

import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.product.abstracts.AbstractCompoundElement;

/**
 *
 * @author rolfhaenni
 */
public class ProductCyclicGroupElement extends AbstractCompoundElement<ProductCyclicGroup, ProductCyclicGroupElement, CyclicGroup, Element> {

  protected ProductCyclicGroupElement(ProductCyclicGroup productCyclicGroup, Element[] elements) {
    super(productCyclicGroup, elements);
  }

  @Override
  protected ProductCyclicGroupElement abstractRemoveAt(Element[] elements) {
    return ProductCyclicGroupElement.getInstance(elements);
  }

  /**
   * This is a static factory method to construct a composed element without the
   * need of constructing the corresponding product or power group beforehand.
   * The input elements are given as an array.
   *
   * @param elements The array of input elements
   * @return The corresponding tuple element
   * @throws IllegalArgumentException if {@code elements} is null or contains
   * null
   */
  public static ProductCyclicGroupElement getInstance(Element... elements) {
    if (elements == null) {
      throw new IllegalArgumentException();
    }
    int arity = elements.length;
    final CyclicGroup[] cyclicGroups = new CyclicGroup[arity];
    for (int i = 0; i < arity; i++) {
      if (elements[i] == null) {
        throw new IllegalArgumentException();
      }
      cyclicGroups[i] = (CyclicGroup) elements[i].getSet();
    }
    return ProductCyclicGroup.getInstance(cyclicGroups).getElement(elements);
  }

}
