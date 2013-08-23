/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.element.abstracts;

import ch.bfh.unicrypt.math.element.interfaces.Element;
import ch.bfh.unicrypt.math.group.classes.ProductSet;
import ch.bfh.unicrypt.math.group.interfaces.Set;
import ch.bfh.unicrypt.math.helper.Compound;
import ch.bfh.unicrypt.math.utility.MathUtil;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @author rolfhaenni
 */
public abstract class CompoundElement extends AbstractElement<CompoundElement> implements Compound<Element> {

  private final Element[] elements;
  private final int arity;

  protected CompoundElement(final Set set, final Element[] elements) {
    super(set);
    this.elements = elements;
    this.arity = elements.length;
  }

  @Override
  protected BigInteger standardGetValue() {
    int arity = this.getArity();
    BigInteger[] values = new BigInteger[arity];
    for (int i = 0; i < arity; i++) {
      values[i] = this.elements[i].getValue();
    }
    return MathUtil.elegantPair(values);
  }

  @Override
  protected byte[] standardGetRecursiveHashValue(MessageDigest messageDigest) {
    int arity = this.getArity();
    byte[][] hashValues = new byte[arity][];
    for (int i = 0; i < arity; i++) {
      hashValues[i] = this.getAt(i).getRecursiveHashValue(messageDigest);
    }
    for (byte[] hashValue : hashValues) {
      messageDigest.update(hashValue);
    }
    return messageDigest.digest();
  }

  @Override
  public int getArity() {
    return this.arity;
  }

  @Override
  public final boolean isNull() {
    return this.getArity() == 0;
  }

  @Override
  public final boolean isUniform() {
    return this.elements.length <= 1;
  }

  @Override
  public Element getFirst() {
    return this.getAt(0);

  }

  @Override
  public Element getAt(int index) {
    if (index < 0 || index >= this.getArity()) {
      throw new IndexOutOfBoundsException();
    }
    if (this.isUniform()) {
      return this.elements[0];
    }
    return this.elements[index];
  }

  @Override
  public Element getAt(int... indices) {
    if (indices == null) {
      throw new IllegalArgumentException();
    }
    Element element = this;
    for (final int index : indices) {
      if (element instanceof Compound) {
        element = ((Compound<Element>) element).getAt(index);
      } else {
        throw new IllegalArgumentException();
      }
    }
    return element;
  }

  @Override
  public Element[] getAll() {
    int arity = this.getArity();
    Element[] result = new Element[arity];
    for (int i = 0; i < arity; i++) {
      result[i] = this.getAt(i);
    }
    return result;
  }

  @Override
  public Iterator<Element> iterator() {
    final CompoundElement compoundElement = this;
    return new Iterator<Element>() {
      int currentIndex = 0;

      @Override
      public boolean hasNext() {
        return currentIndex >= compoundElement.getArity();
      }

      @Override
      public Element next() {
        if (this.hasNext()) {
          return compoundElement.getAt(this.currentIndex);
        }
        throw new NoSuchElementException();
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException("Not supported yet.");
      }
    };
  }

  @Override
  protected boolean standardEquals(Element element) {
    CompoundElement other = (CompoundElement) element;
    for (int i = 0; i < this.getArity(); i++) {
      if (!this.getAt(i).equals(other.getAt(i))) {
        return false;
      }
    }
    return true;
  }

  @Override
  protected int standardHashCode() {
    final int prime = 31;
    int result = 1;
    for (Element element : this) {
      result = prime * result + element.hashCode();
    }
    return result;
  }

  @Override
  protected String standardToString() {
    String result = "";
    String separator = "";
    for (Element element : this) {
      result = result + separator + element.toString();
      separator = ",";
    }
    return result;
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
  public static CompoundElement getInstance(Element... elements) {
    if (elements == null) {
      throw new IllegalArgumentException();
    }
    int arity = elements.length;
    final Set[] sets = new Set[arity];
    for (int i = 0; i < arity; i++) {
      if (elements[i] == null) {
        throw new IllegalArgumentException();
      }
      sets[i] = elements[i].getSet();
    }
    return ProductSet.getInstance(sets).getElement(elements);
  }
}