package hr.fer.zemris.optjava.dz8.function;

/**
 * Sucelje koje modelira skalarnu funkciju nad n-dimenzijskim vektorom
 * @author Luka Sterbic
 * @version 0.1
 */
public interface IFunction<T> {
	
	/**
	 * Vrati broj varijabli nad kojima je funkcija definirana
	 * @return broj varijabli
	 */
	public int dimensions();
	
	/**
	 * Vrati vrijednost funkcije u zadanoj tocki
	 * @param point tocka
	 * @return vrijednost funkcije u tocki
	 */
	public double valueAt(T point);

}
