package hr.fer.zemris.optjava.opt;

import java.util.Arrays;

/**
 * Klasa koja predstavlja rjesenje optimitacijskog
 * problema u obliku polja cijelih brojeva.
 * @author Luka Sterbic
 * @version 0.3
 */
public class IntegerArraySolution extends SingleObjectiveSolution {
	
	public int[] values;
	
	/**
	 * Konstruktor za IntegerArraySolution, prazno rjesenje
	 */
	public IntegerArraySolution() {
		this(0);
	}
	
	/**
	 * Konstruktor za IntegerArraySolution, inicijalizacija na nulu
	 * @param dimension dimenzija polja
	 */
	public IntegerArraySolution(int dimension) {
		this.values = new int[dimension];
	}
	
	/**
	 * Konstruktor za IntegerArraySolution, inicijalizacija preko polja podataka
	 * @param values polje podataka
	 */
	public IntegerArraySolution(int[] values) {
		if(values == null) {
			throw new IllegalArgumentException("Polje podataka ne smije biti null!");
		}
		
		this.values = values;
	}
	
	/**
	 * Kreiraj duplikat ovog rjesenja
	 * @return duplikat
	 */
	public IntegerArraySolution duplicate() {
		int dimension = values.length;
		
		IntegerArraySolution clone = new IntegerArraySolution(dimension);
		System.arraycopy(values, 0, clone.values, 0, dimension);
		
		clone.value = value;
		clone.fitness = fitness;
		
		return clone;
	}
	
	@Override
	public String toString() {
		return Arrays.toString(values);
	}

}
