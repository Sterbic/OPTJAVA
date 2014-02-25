package hr.fer.zemris.optjava.rng;

import java.util.Properties;

/**
 * Klasa koja pamti referencu na provider generatora slucjanih brojeva
 * i daje staticki pristup do neke instance generatora
 * @author Luka Sterbic
 * @version 0.1
 */
public class RNG {
	
	private static IRNGProvider rngProvider;
	
	/**
	 * Staticka inicijalizacija providera
	 */
	static {
		try {
			Properties prop = new Properties();
			ClassLoader loader = RNG.class.getClassLoader();
			
			prop.load(loader.getResourceAsStream("rng-config.properties"));
			String rngName = prop.getProperty("rng-provider");
		
			rngProvider = (IRNGProvider) loader.loadClass(rngName).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	/**
	 * Dohavati generator slucajnih brojeva
	 * @return generator slucajnih brojeva
	 */
	public static IRNG getRNG() {
		return rngProvider.getRNG();
	}
}
