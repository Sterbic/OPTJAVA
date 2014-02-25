package hr.fer.zemris.optjava.dz9.opt;

import java.util.Arrays;

/**
 * Klasa koja modelira rjesenje problema visekriterijske optimizacije
 * @author Luka Sterbic
 * @version 0.3
 */
public class MultipleObjectiveSolution {

	public double fitness;
	public int dimension;
	public double[] values;
	public double[] objectives;
	
	/**
	 * Konstruktor za MultipleObjectiveSolution
	 * @param values tocka iz decision space-a
	 */
	public MultipleObjectiveSolution(double[] values) {
		this.values = values;
		this.dimension = values.length;
	}
	
	/**
	 * Provjeri da li ovo rjesenje dominira nad danim rjesenjem
	 * @param other drugo rjesenje
	 * @return true ako ovo rjesenje dominira, false inace
	 */
	public boolean dominates(MultipleObjectiveSolution other) {
		boolean dominates = false;
		
		for(int i = 0; i < objectives.length; i++) {
			if(objectives[i] > other.objectives[i]) {
				return false;
			} else if(objectives[i] < other.objectives[i]) {
				dominates = true;
			}
		}
		
		return dominates;
	}
	
	public MultipleObjectiveSolution duplicate() {
		return new MultipleObjectiveSolution(Arrays.copyOf(values, dimension));
	}
	
}
