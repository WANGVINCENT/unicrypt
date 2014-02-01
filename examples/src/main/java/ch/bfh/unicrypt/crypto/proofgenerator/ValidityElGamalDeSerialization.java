package ch.bfh.unicrypt.crypto.proofgenerator;

import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces.SigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.classes.ElGamalEncryptionValidityProofGenerator;
import ch.bfh.unicrypt.crypto.schemes.encryption.classes.ElGamalEncryptionScheme;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.StringElement;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.StringMonoid;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import ch.bfh.unicrypt.math.algebra.general.classes.Subset;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModElement;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModSafePrime;
import ch.bfh.unicrypt.math.helper.Alphabet;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.math.BigInteger;

public class ValidityElGamalDeSerialization {

	public static void main(String[] args) {

		GStarModSafePrime gQ = GStarModSafePrime.getInstance(new BigInteger("1187"));
		ZMod zQ = gQ.getZModOrder();

		Tuple recoveredTuple = null;
		try {
			InputStream file = new FileInputStream("proofs.ser");
			InputStream buffer = new BufferedInputStream(file);
			ObjectInput input = new ObjectInputStream(buffer);
			recoveredTuple = (Tuple) input.readObject();
			input.close();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		Tuple proof = (Tuple) recoveredTuple.getAt(0);
		Tuple cipherText = (Tuple) recoveredTuple.getAt(1);
		GStarModElement publicKey = (GStarModElement) recoveredTuple.getAt(2);
		StringElement proverId = StringMonoid.getInstance(Alphabet.BASE64)
			   .getElement("Prover1");

		System.out.println("proof: " + proof);
		System.out.println("cipherText: " + cipherText);
		System.out.println("publicKey: " + publicKey);

		System.out.println("P: " + gQ.getModulus());
		System.out.println("Q: " + zQ.getModulus());
		System.out.println("proverId: " + proverId);

		GStarModElement[] possibleMessages = new GStarModElement[4];
		ElGamalEncryptionScheme elGamal = ElGamalEncryptionScheme
			   .getInstance(gQ);

		possibleMessages[0] = gQ.getElementFrom(((GStarModElement) gQ.getDefaultGenerator()).power(0));
		possibleMessages[1] = gQ.getElementFrom(((GStarModElement) gQ.getDefaultGenerator()).power(1));
		possibleMessages[2] = gQ.getElementFrom(((GStarModElement) gQ.getDefaultGenerator()).power(2));
		possibleMessages[3] = gQ.getElementFrom(((GStarModElement) gQ.getDefaultGenerator()).power(3));

		SigmaChallengeGenerator scg = ElGamalEncryptionValidityProofGenerator
			   .createNonInteractiveChallengeGenerator(elGamal,
													   possibleMessages.length, proverId);

		Subset plaintexts = Subset.getInstance(gQ, possibleMessages);

		ElGamalEncryptionValidityProofGenerator pg = ElGamalEncryptionValidityProofGenerator
			   .getInstance(scg, elGamal, publicKey, plaintexts);

		BooleanElement v = pg.verify(proof, cipherText);

		System.out.println("Proof valid: " + v);

	}

}
