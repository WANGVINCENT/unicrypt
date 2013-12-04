package ch.bfh.unicrypt.crypto.proofgenerator.classes;

import ch.bfh.unicrypt.crypto.proofgenerator.abstracts.AbstractTCSSetMembershipProofGenerator;
import ch.bfh.unicrypt.crypto.schemes.encryption.classes.ElGamalEncryptionScheme;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.function.classes.ApplyFunction;
import ch.bfh.unicrypt.math.function.classes.CompositeFunction;
import ch.bfh.unicrypt.math.function.classes.InvertFunction;
import ch.bfh.unicrypt.math.function.classes.MultiIdentityFunction;
import ch.bfh.unicrypt.math.function.classes.ProductFunction;
import ch.bfh.unicrypt.math.function.classes.SelectionFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.helper.HashMethod;

public class ElGamalEncryptionValidityProofGenerator
	   extends AbstractTCSSetMembershipProofGenerator<ProductGroup, Pair> {

	private final ElGamalEncryptionScheme elGamalES;
	private final Element publicKey;

	protected ElGamalEncryptionValidityProofGenerator(ElGamalEncryptionScheme elGamalES, Element publicKey, Element[] plaintexts, HashMethod hashMethod) {
		super(plaintexts, hashMethod);
		this.elGamalES = elGamalES;
		this.publicKey = publicKey;
	}

	public static ElGamalEncryptionValidityProofGenerator getInstance(ElGamalEncryptionScheme elGamalES, Element publicKey, Element[] plaintexts) {
		return ElGamalEncryptionValidityProofGenerator.getInstance(elGamalES, publicKey, plaintexts, HashMethod.DEFAULT);
	}

	public static ElGamalEncryptionValidityProofGenerator getInstance(ElGamalEncryptionScheme elGamalES, Element publicKey, Element[] plaintexts, HashMethod hashMethod) {
		if (elGamalES == null || publicKey == null || !elGamalES.getCyclicGroup().contains(publicKey)
			   || plaintexts == null || plaintexts.length < 1 || hashMethod == null) {
			throw new IllegalArgumentException();
		}

		return new ElGamalEncryptionValidityProofGenerator(elGamalES, publicKey, plaintexts, hashMethod);
	}

	@Override
	protected Function abstractGetSetMembershipFunction() {
		return this.elGamalES.getEncryptionFunction().partiallyApply(this.publicKey, 0);
	}

	@Override
	protected Function abstractGetDeltaFunction() {
		CyclicGroup elGamalCyclicGroup = this.elGamalES.getCyclicGroup();
		ProductSet deltaFunctionDomain = ProductSet.getInstance(elGamalCyclicGroup, this.getSetMembershipProofFunction().getCoDomain());
		Function deltaFunction =
			   CompositeFunction.getInstance(MultiIdentityFunction.getInstance(deltaFunctionDomain, 2),
											 ProductFunction.getInstance(SelectionFunction.getInstance(deltaFunctionDomain, 1, 0),
																		 CompositeFunction.getInstance(MultiIdentityFunction.getInstance(deltaFunctionDomain, 2),
																									   ProductFunction.getInstance(SelectionFunction.getInstance(deltaFunctionDomain, 1, 1),
																																   CompositeFunction.getInstance(SelectionFunction.getInstance(deltaFunctionDomain, 0),
																																								 InvertFunction.getInstance(elGamalCyclicGroup))),
																									   ApplyFunction.getInstance(elGamalCyclicGroup))));
		return deltaFunction;
	}

}