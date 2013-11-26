package ch.bfh.unicrypt.math.function.classes;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import java.util.Random;

/**
 * This class represents the the concept of a function f:X->Y, which outputs the
 * element of Y that corresponds to the integer value of the input element.
 * <p/>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public class ConvertFunction
	   extends AbstractFunction<Set, Set, Element> {

	private ConvertFunction(final Set domain, final Set coDomain) {
		super(domain, coDomain);
	}

	@Override
	protected Element abstractApply(final Element element, final Random random) {
		return this.getCoDomain().getElement(element);
	}

	/**
	 * This is the general factory method for this class. It creates an function
	 * that converts values from the domain into values from the co-domain.
	 * <p/>
	 * @param domain   The given domain
	 * @param coDomain The given co-domain
	 * @return The resulting function
	 * @throws IllegalArgumentException if the domain or coDomain is null
	 */
	public static ConvertFunction getInstance(final Set domain, final Set coDomain) {
		return new ConvertFunction(domain, coDomain);
	}

	@Override
	protected boolean abstractIsEqual(Function function) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
