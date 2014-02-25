package hr.fer.zemris.optjava.rng;

/**
 * Sučelje koje predstavlja generator slučajnih brojeva.
 * @author marcupic
 */
public interface IRNG {
	
	/**
	 * Vraća decimalni broj iz intervala [0,1) prema uniformnoj distribuciji.
	 * @return slučajno generirani decimalni broj
	 */
	public double nextDouble(); 
	
	/**
	 * Vraća decimalni broj iz intervala [min,max) prema uniformnoj distribuciji. *
	 * @param min donja granica intervala (uključiva)
	 * @param max gornja granica intervala (isključiva)
	 * @return slučajno generirani decimalni broj
	 */
	public double nextDouble(double min, double max);
	
	/**
￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼￼	 * Vraća decimalni broj iz intervala [0,1) prema uniformnoj distribuciji.
￼￼￼￼￼￼￼￼	 * @return slučajno generirani decimalni broj
	 */
	public float nextFloat();
	
	/**
	 * Vraća decimalni broj iz intervala [min,max) prema uniformnoj distribuciji.
	 * @param min donja granica intervala (uključiva)
	 * @param max gornja granica intervala (isključiva)
	 * @return slučajno generirani decimalni broj
	 */
	public float nextFloat(float min, float max);
	
	/**
	 * Vraća cijeli broj iz intervala svih mogućih cijelih brojeva prema uniformnoj distribuciji.
	 * @return slučajno generirani cijeli broj
	 */
	public int nextInt(); 
	
	/**
	 * Vraća cijeli broj iz intervala [min,max) prema uniformnoj distribuciji.
	 * @param min donja granica intervala (uključiva)
	 * @param max gornja granica intervala (isključiva)
	 * @return slučajno generirani cijeli broj
	 * */
	public int nextInt(int min, int max);
	
	/**
	 * Vraća slučajno generiranu boolean vrijednost. Vrijednosti se izvlače
	 * iz uniformne distribucije.
	 * @return slučajno generirani boolean
	 */
	public boolean nextBoolean();

	/**
	 * Vraća decimalni broj iz normalne distribucije s parametrima (0,1).
	 * @return slučajno generirani decimalni broj
	 */
	public double nextGaussian();
	
}
