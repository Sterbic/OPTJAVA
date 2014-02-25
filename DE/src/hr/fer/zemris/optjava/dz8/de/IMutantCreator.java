package hr.fer.zemris.optjava.dz8.de;

import hr.fer.zemris.optjava.dz8.opt.DoubleArraySolution;

/**
 * Sucelje koje predstavlja generiranje mutanta u algoritmu diferencijske evolucije
 * @author Luka Sterbic
 * @version 0.2
 */
public interface IMutantCreator {
	
	/**
	 * Kreiraj mutanta s obzirom na zadanu populaciju
	 * @param target ciljni vektor
	 * @param base bazni vektor
	 * @param population trenutna populacija
	 * @return vektor mutanta
	 */
	public DoubleArraySolution create(DoubleArraySolution target,
			DoubleArraySolution base, DoubleArraySolution[] population);

}
