package ch.bfh.unicrypt.crypto.proofgenerator.classes;

import ch.bfh.unicrypt.crypto.proofgenerator.abstracts.AbstractProofGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.classes.StandardNonInteractiveSigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces.NonInteractiveSigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces.SigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.interfaces.SigmaProofGenerator;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSemiGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Triple;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.classes.ApplyInverseFunction;
import ch.bfh.unicrypt.math.function.classes.CompositeFunction;
import ch.bfh.unicrypt.math.function.classes.GeneratorFunction;
import ch.bfh.unicrypt.math.function.classes.MultiIdentityFunction;
import ch.bfh.unicrypt.math.function.classes.ProductFunction;
import ch.bfh.unicrypt.math.function.classes.SelectionFunction;
import ch.bfh.unicrypt.math.function.classes.SelfApplyFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.helper.HashMethod;
import java.util.Random;

public class InequalityProofGenerator
	   extends AbstractProofGenerator<SemiGroup, Element, ProductGroup, Pair, Set, Pair> {

	private final SigmaChallengeGenerator challengeGenerator;
	private final Function firstFunction;
	private final Function secondFunction;

	protected InequalityProofGenerator(final SigmaChallengeGenerator challengeGenerator, final Function firstFunction, final Function secondFunction) {
		this.challengeGenerator = challengeGenerator;
		this.firstFunction = firstFunction;
		this.secondFunction = secondFunction;

	}

	public static InequalityProofGenerator getInstance(final SigmaChallengeGenerator challengeGenerator, final Function firstFunction, final Function secondFunction) {
		if (challengeGenerator == null || firstFunction == null || secondFunction == null || !firstFunction.getDomain().isEqual(secondFunction.getDomain())
			   || !firstFunction.getCoDomain().isCyclic() || !secondFunction.getCoDomain().isCyclic()) {
			throw new IllegalArgumentException();
		}

		return new InequalityProofGenerator(challengeGenerator, firstFunction, secondFunction);
	}

	// Service method to prove inequality fo descrete logarithms
	public static InequalityProofGenerator getInstance(final SigmaChallengeGenerator challengeGenerator, final Element firstGenerator, final Element secondGenerator) {
		if (challengeGenerator == null || firstGenerator == null || secondGenerator == null || !firstGenerator.getSet().isEqual(secondGenerator.getSet())
			   || !firstGenerator.getSet().isCyclic() || !firstGenerator.isGenerator() || !secondGenerator.isGenerator()) {
			throw new IllegalArgumentException();
		}
		Function f1 = GeneratorFunction.getInstance(firstGenerator);
		Function f2 = GeneratorFunction.getInstance(secondGenerator);

		return new InequalityProofGenerator(challengeGenerator, f1, f2);
	}

	@Override
	protected SemiGroup abstractGetPrivateInputSpace() {
		return (SemiGroup) this.getFirstFunction().getDomain();
	}

	@Override
	protected ProductGroup abstractGetPublicInputSpace() {
		return ProductGroup.getInstance((Group) this.getFirstFunction().getCoDomain(), (Group) this.getSecondFunction().getCoDomain());
	}

	@Override
	protected ProductSet abstractGetProofSpace() {
		return ProductSet.getInstance(this.getPreimageProofSpace(), this.getSecondFunction().getCoDomain());
	}

	public ZMod getChallengeSpace() {
		return this.getFirstFunction().getDomain().getZModOrder();
	}

	public ProductSet getPreimageProofSpace() {
		return ProductSet.getInstance(ProductGroup.getInstance(this.getFirstFunction().getCoDomain(), this.getSecondFunction().getCoDomain()),
									  this.getChallengeSpace(),
									  ProductGroup.getInstance(this.getPrivateInputSpace(), 2));
	}

	public Triple getPreimageProof(Pair proof) {
		return (Triple) proof.getFirst();
	}

	public Element getProofCommitment(Pair proof) {
		return proof.getSecond();
	}

	public Function getFirstFunction() {
		return this.firstFunction;
	}

	public Function getSecondFunction() {
		return this.secondFunction;
	}

	public SigmaChallengeGenerator getChallengeGenerator() {
		return this.challengeGenerator;
	}

	@Override
	protected Pair abstractGenerate(Element privateInput, Pair publicInput, Random random) {

		// 1. Create commitment C = (h^x/z)^r with random r
		Element r = this.getSecondFunction().getCoDomain().getZModOrder().getRandomElement(random);
		Element x = privateInput;
		Element z = publicInput.getSecond();

		Element c = this.secondFunction.apply(x).apply(z.invert()).selfApply(r);

		// 2. Create preimage proof:
		//    f(a,b) = (f2(a)/z^b, f1(a)/y^b) = (C,1)  // a=xr, b=r
		SigmaProofGenerator preimageProofGenerator = this.createPreimageProofGenerator(publicInput);

		Triple preimageProof = preimageProofGenerator.generate(
			   Tuple.getInstance(x.selfApply(r), r),
			   Tuple.getInstance(c, ((CyclicGroup) this.getFirstFunction().getCoDomain()).getIdentityElement()));

		return Pair.getInstance(preimageProof, c);
	}

	@Override
	protected BooleanElement abstractVerify(Pair proof, Pair publicInput) {

		// 1. Verify preimage proof
		SigmaProofGenerator preimageProofGenerator = this.createPreimageProofGenerator(publicInput);
		BooleanElement v = preimageProofGenerator.verify(
			   this.getPreimageProof(proof),
			   Tuple.getInstance(this.getProofCommitment(proof), ((CyclicGroup) this.getFirstFunction().getCoDomain()).getIdentityElement()));

		// 2. Check C != 1
		boolean c = !this.getProofCommitment(proof).isEqual(((CyclicGroup) this.getFirstFunction().getCoDomain()).getIdentityElement());

		return BooleanSet.getInstance().getElement(v.getBoolean() && c);
	}

	// f(a,b) = (f2(a)/z^b, f1(a)/y^b)
	private SigmaProofGenerator createPreimageProofGenerator(Pair publicInput) {

		Element y = publicInput.getFirst();
		Element z = publicInput.getSecond();

		Function[] functions = new Function[2];
		ProductSet domain = ProductSet.getInstance(this.getPrivateInputSpace(), 2);
		functions[0] = this.createSinglePreimageProofFunction(domain, this.getSecondFunction(), z);
		functions[1] = this.createSinglePreimageProofFunction(domain, this.getFirstFunction(), y);

		return PreimageEqualityProofGenerator.getInstance(this.getChallengeGenerator(), functions);
	}

	// f(a,b) = f(a)/y^b
	private Function createSinglePreimageProofFunction(ProductSet domain, Function f, Element y) {
		return CompositeFunction.getInstance(MultiIdentityFunction.getInstance(domain, 2),
											 ProductFunction.getInstance(CompositeFunction.getInstance(SelectionFunction.getInstance(domain, 0),
																									   f),
																		 CompositeFunction.getInstance(SelectionFunction.getInstance(domain, 1),
																									   MultiIdentityFunction.getInstance(domain.getAt(1), 1),
																									   SelfApplyFunction.getInstance((Group) y.getSet()).partiallyApply(y, 0))),
											 ApplyInverseFunction.getInstance((Group) f.getCoDomain()));
	}

	public static NonInteractiveSigmaChallengeGenerator createNonInteractiveChallengeGenerator(final Function firstFunction, final Function secondFunction) {
		return InequalityProofGenerator.createNonInteractiveChallengeGenerator(firstFunction, secondFunction, HashMethod.DEFAULT);
	}

	public static NonInteractiveSigmaChallengeGenerator createNonInteractiveChallengeGenerator(final Function firstFunction, final Function secondFunction, final Element proverId) {
		return InequalityProofGenerator.createNonInteractiveChallengeGenerator(firstFunction, secondFunction, proverId, HashMethod.DEFAULT);
	}

	public static NonInteractiveSigmaChallengeGenerator createNonInteractiveChallengeGenerator(final Function firstFunction, final Function secondFunction, final HashMethod hashMethod) {
		return InequalityProofGenerator.createNonInteractiveChallengeGenerator(firstFunction, secondFunction, (Element) null, hashMethod);
	}

	public static NonInteractiveSigmaChallengeGenerator createNonInteractiveChallengeGenerator(final Function firstFunction, final Function secondFunction, final Element proverId, final HashMethod hashMethod) {
		if (firstFunction == null || secondFunction == null || hashMethod == null
			   || !firstFunction.getCoDomain().isSemiGroup() || !secondFunction.getCoDomain().isSemiGroup()) {
			throw new IllegalArgumentException();
		}
		ProductSemiGroup codomain = ProductSemiGroup.getInstance((SemiGroup) secondFunction.getCoDomain(), (SemiGroup) firstFunction.getCoDomain());
		ZMod cs = ZMod.getInstance(ProductSet.getInstance(firstFunction.getDomain(), secondFunction.getDomain()).getMinimalOrder());
		return StandardNonInteractiveSigmaChallengeGenerator.getInstance(codomain, codomain, cs, proverId, hashMethod);

	}

}
