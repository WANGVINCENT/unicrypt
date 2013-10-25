package ch.bfh.unicrypt.crypto.proofgenerator.interfaces;

import java.util.List;
import java.util.Random;

import ch.bfh.unicrypt.math.algebra.general.classes.ProductGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;

public interface ProductCoDomainProofGenerator extends ProofGenerator {

  public Tuple generate(Element secretInput, List<Element> publicInputs);

  public Tuple generate(Element secretInput, List<Element> publicInputs, Element otherInput);

  public Tuple generate(Element secretInput, List<Element> publicInputs, Random random);

  public Tuple generate(Element secretInput, List<Element> publicInputs, Element otherInput, Random random);

  public boolean verify(Tuple proof, List<Element> publicInputs);

  public boolean verify(Tuple proof, List<Element> publicInputs, Element otherInput);

  @Override
  public ProductGroup getCoDomain();

}