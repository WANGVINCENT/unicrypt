package ch.bfh.unicrypt.crypto.keygenerator.abstracts;

import java.math.BigInteger;
import java.util.Random;

import ch.bfh.unicrypt.crypto.keygenerator.interfaces.KeyGenerator;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.helper.UniCrypt;

public abstract class AbstractKeyGenerator<S extends Set, E extends Element> extends UniCrypt implements KeyGenerator {

  private final S keySpace;

  protected AbstractKeyGenerator(final S keySpace) {
    this.keySpace = keySpace;
  }

  @Override
  public final S getKeySpace() {
    return this.keySpace;
  }

  @Override
  public final E getKey(BigInteger value) {
    return (E) this.getKeySpace().getElement(value);
  }

  @Override
  public E generateKey() {
    return (E) this.getKeySpace().getRandomElement();
  }

  @Override
  public E generateKey(Random random) {
    return (E) this.getKeySpace().getRandomElement(random);
  }

  @Override
  protected String standardToStringContent() {
    return this.getKeySpace().toString();
  }

}