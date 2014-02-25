package hr.fer.zemris.optjava.opt;

/**
 * Sucelje koje modelira skalarnu funkciju
 * @author Luka Sterbic
 * @version 0.2
 * @param <T> tip jedinke
 */
public interface IFunction<T> {
	
	/**
	 * Vrati vrijednost funkcije u zadanoj tocki
	 * @param point tocka
	 * @return vrijednost funkcije u tocki
	 */
	public double valueAt(T point);

}
