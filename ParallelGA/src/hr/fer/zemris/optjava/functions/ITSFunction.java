package hr.fer.zemris.optjava.functions;

/**
 * Sucelje koje modelira skalarnu funkciju nad n-dimenzijskim vektorom.
 * VAZNO: Funkcija mora biti visedretveno sigurna kako bi optimizacija
 * paralelnim genetskim algoritmom dala ispravne rezultate.
 * @author Luka Sterbic
 * @version 0.1
 */
public interface ITSFunction<T> {
	
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
