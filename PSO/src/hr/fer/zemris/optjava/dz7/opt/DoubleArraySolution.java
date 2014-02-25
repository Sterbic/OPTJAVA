package hr.fer.zemris.optjava.dz7.opt;

import java.util.Arrays;
import java.util.Random;

/**
 * Klasa koja predstavlja rjesenje optimitacijskog
 * problema u obliku polja realnih brojeva.
 * @author Luka Sterbic
 * @version 0.6
 */
public class DoubleArraySolution extends SingleObjectiveSolution {
	
	public double[] values;
	
	/**
	 * Konstruktor za DoubleArraySolution
	 * @param dimension dimenzija polja
	 */
	public DoubleArraySolution(int dimension) {
		if(dimension < 1) {
			throw new IllegalArgumentException("Dimenzija polja mora biti veca od 0!");
		}
		
		this.values = new double[dimension];
	}
	
	/**
	 * Konstruktor za slucajno inicijaliziran DoubleArraySolution
	 * @param dimensions dimenzija polja
	 * @param rand Random objekt
	 * @param delta maksimalno odstupanje svakog elementa
	 */
	public DoubleArraySolution(int dimensions, Random rand, double delta) {
		this(dimensions);
		
		if(rand == null) {
			throw new IllegalArgumentException("Random objekt ne smije biti null!");
		}
		
		for(int i = 0; i < dimensions; i++) {
			this.values[i] = rand.nextDouble() * (rand.nextBoolean() ? delta : -delta);
		}
	}
	
	/**
	 * Kreiraj duplikat ovog rjesenja
	 * @return duplikat
	 */
	public DoubleArraySolution duplicate() {
		int dimension = values.length;
		
		DoubleArraySolution clone = new DoubleArraySolution(dimension);
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
