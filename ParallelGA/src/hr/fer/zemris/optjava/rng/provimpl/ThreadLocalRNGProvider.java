package hr.fer.zemris.optjava.rng.provimpl;

import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.IRNGProvider;
import hr.fer.zemris.optjava.rng.rngimpl.RNGRandomImpl;

/**
 * Klasa koja implementra sucelje IRNGProvider tako da za svaku
 * dretvu pamti jednu instancu genertora slucajnih brojeva.
 * Koristi se implemntacija generatora koja koristi ugradjenu
 * klasu java.util.Random
 * @author Luka Sterbic
 * @version 0.1
 */
public class ThreadLocalRNGProvider implements IRNGProvider {

	private ThreadLocal<IRNG> threadLocal;
	
	/**
	 * Konstruktor za ThreadLocalRNGProvider
	 */
	public ThreadLocalRNGProvider() {
		this.threadLocal = new ThreadLocal<>();
	}
	
	@Override
	public IRNG getRNG() {
		IRNG rng = threadLocal.get();
		
		if(rng == null) {
			rng = new RNGRandomImpl();
			threadLocal.set(rng);
		}
		
		return rng;
	}

}
