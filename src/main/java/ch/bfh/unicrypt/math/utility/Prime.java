/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.utility;

import java.math.BigInteger;
import java.util.Random;

/**
 *
 * @author rolfhaenni
 */
public class Prime extends SpecialFactorization {

  private Prime(BigInteger prime) {
    super(prime);
  }

  public static Prime getInstance(BigInteger prime) {
    return new Prime(prime);
  }

  public static Prime getRandomInstance(int bitLength) {
    return Prime.getRandomInstance(bitLength, (Random) null);
  }

  public static Prime getRandomInstance(int bitLength, Random random) {
    return Prime.getInstance(RandomUtil.createRandomPrime(bitLength, random));
  }

}
