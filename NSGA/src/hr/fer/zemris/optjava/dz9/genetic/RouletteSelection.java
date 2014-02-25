package hr.fer.zemris.optjava.dz9.genetic;

import hr.fer.zemris.optjava.dz9.opt.MultipleObjectiveSolution;

import java.util.Random;

/**
 * Klasa koja implementira proporcijonlnu selekciju
 * @author Luka Sterbic
 * @version 0.2
 * @param <T> tip jedinkes
 */
public class RouletteSelection implements ISelection<MultipleObjectiveSolution> {

	private Random rand;
	
	/**
	 * Konstruktor za RouletteSelection
	 */
	public RouletteSelection() {
		this.rand = new Random();
	}
	
	@Override
	public int select(MultipleObjectiveSolution[] population) {		
		double probability = rand.nextDouble();
		double offset = 0.0;
		double sum = 0.0;
		
		for(MultipleObjectiveSolution solution : population) {
			sum += solution.fitness;
		}
		
		for(int i = 0; i < population.length; i++) {
			offset += population[i].fitness / sum;
			
			if(offset >= probability) {
				return i;
			}
		}
		
		return -1;
	}

}
