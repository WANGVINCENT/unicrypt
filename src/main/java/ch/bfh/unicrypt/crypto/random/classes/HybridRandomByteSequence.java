/*
 * UniCrypt
 *
 *  UniCrypt(tm) : Cryptographical framework allowing the implementation of cryptographic protocols e.g. e-voting
 *  Copyright (C) 2014 Bern University of Applied Sciences (BFH), Research Institute for
 *  Security in the Information Society (RISIS), E-Voting Group (EVG)
 *  Quellgasse 21, CH-2501 Biel, Switzerland
 *
 *  Licensed under Dual License consisting of:
 *  1. GNU Affero General Public License (AGPL) v3
 *  and
 *  2. Commercial license
 *
 *
 *  1. This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 *  2. Licensees holding valid commercial licenses for UniCrypt may use this file in
 *   accordance with the commercial license agreement provided with the
 *   Software or, alternatively, in accordance with the terms contained in
 *   a written agreement between you and Bern University of Applied Sciences (BFH), Research Institute for
 *   Security in the Information Society (RISIS), E-Voting Group (EVG)
 *   Quellgasse 21, CH-2501 Biel, Switzerland.
 *
 *
 *   For further information contact <e-mail: unicrypt@bfh.ch>
 *
 *
 * Redistributions of files must retain the above copyright notice.
 */
package ch.bfh.unicrypt.crypto.random.classes;

import ch.bfh.unicrypt.crypto.random.distributionsampler.classes.DistributionSamplerCollector;
import ch.bfh.unicrypt.crypto.random.interfaces.TrueRandomByteSequence;
import ch.bfh.unicrypt.math.helper.ByteArray;
import ch.bfh.unicrypt.math.helper.HashMethod;

/**
 * This class allows the generation of ephemeral keys. Hence it provides (backward-)security and forward-security to the
 * generated random strings. Its security is based on the quality of the DistributionSamplerCollector and on the
 * feedback of the PseudoRandomNumberGeneratorCounterMode. The injection of new random bits into the randomization
 * process allows (backward-)security, whilst The feedback (in this case internally requesting a byte[] which is only
 * used for re-seeding) allows forward-security.
 * <p>
 * <p>
 * @author Reto E. Koenig <reto.koenig@bfh.ch>
 */
public class HybridRandomByteSequence
	   extends OutputFeedbackRandomByteSequence
	   implements TrueRandomByteSequence {

	private static HybridRandomByteSequence DEFAULT;
	private final DistributionSamplerCollector distributionSampler;
	private final int securityParameterInBytes;

	/**
	 *
	 * Creates a new instance of a TrueRandomNumberGenerator.
	 * <p>
	 * @param distributionSampler Channel to the different distributions. This guarantees freshness and thus
	 *                            (backward-)security
	 */
	protected HybridRandomByteSequence(HashMethod hashMethod, int forwardSecurityInBytes, int securityParameterInBytes) {
		super(hashMethod, forwardSecurityInBytes, ByteArray.getInstance());

		this.securityParameterInBytes = securityParameterInBytes;

		this.distributionSampler = DistributionSamplerCollector.getInstance(this);
		super.setSeed(this.distributionSampler.getDistributionSamples(securityParameterInBytes));
	}

	@Override
	public int getSecurityParameterInBytes() {
		return this.securityParameterInBytes;
	}

	@Override
	public void setFreshData(ByteArray byteArray) {
		if (byteArray == null) {
			throw new IllegalArgumentException();
		}
		super.update(byteArray);
	}

	@Override
	protected byte[] getNextBytes(int length) {
		//This will trigger the DistributionSamplerCollector to inject fresh data.
		//This data will then be injected to the setFreshData method, as soon as it is ready.
		this.distributionSampler.collectDistributionSamples();
		return super.getNextBytes(length);
	}

	/**
	 * This will return the default HybridRandomByteSequence.
	 * <p>
	 * @return
	 */
	public static HybridRandomByteSequence getInstance() {
		if (DEFAULT == null) {
			DEFAULT = HybridRandomByteSequence.getInstance(HashMethod.DEFAULT, HashMethod.DEFAULT.getLength() / 2, HashMethod.DEFAULT.getLength());
		}
		return DEFAULT;
	}

	public static HybridRandomByteSequence getInstance(HashMethod hashMethod, int forwardSecurityInBytes, int securityParameterInBytes) {
		if (hashMethod == null) {
			throw new IllegalArgumentException();
		}
		if (forwardSecurityInBytes < 0 || forwardSecurityInBytes > hashMethod.getLength() - 1) {
			throw new IllegalArgumentException();
		}
		if (securityParameterInBytes < 0) {
			throw new IllegalArgumentException();
		}
		return new HybridRandomByteSequence(hashMethod, forwardSecurityInBytes, securityParameterInBytes);
	}

	@Override
	public DistributionSamplerCollector getDistributionSampler() {
		return this.distributionSampler;
	}

}
