package hr.fer.zemris.optjava.rng.provimpl;

import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.IRNGProvider;
import hr.fer.zemris.optjava.rng.rngimpl.RNGRandomImpl;

/**
 * Implementacija providera genratora slucajnih brojeva
 * koja pretpostavlja da pozivna dretva implementira
 * sucelje IRNGProvider.
 * @author Luka Sterbic
 * @version 0.1
 */
public class ThreadBoundRNGProvider implements IRNGProvider {
	
	private static final RNGRandomImpl defaultRand = new RNGRandomImpl();

	@Override
	public IRNG getRNG() {
		try {
			return ((IRNGProvider) Thread.currentThread()).getRNG();
		} catch(ClassCastException ex) {
			return defaultRand;
		}
	}

}
