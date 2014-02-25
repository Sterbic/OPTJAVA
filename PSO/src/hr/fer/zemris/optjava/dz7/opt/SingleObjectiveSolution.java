package hr.fer.zemris.optjava.dz7.opt;

/**
 * Klasa koja modelira rjesenje problema optimizacije
 * @author Luka Sterbic
 * @version 0.2
 */
public class SingleObjectiveSolution implements Comparable<SingleObjectiveSolution> {

	public double fitness;
	public double value;
	
	/**
	 * Konstruktor za SingleObjectiveSolution
	 */
	public SingleObjectiveSolution() {
		this(0, 0);
	}
	
	/**
	 * Konstruktor za SingleObjectiveSolution
	 * @param fitness dobrota rjesenja
	 * @param value vrijednost rjesenja
	 */
	public SingleObjectiveSolution(double fitness, double value) {
		this.fitness = fitness;
		this.value = value;
	}

	@Override
	public int compareTo(SingleObjectiveSolution o) {
		double diff = fitness - o.fitness;
		
		if(Math.abs(fitness) < 1E-9) {
			return 0;
		} else if(diff > 0) {
			return 1;
		} else {
			return -1;
		}
	}
	
}
