package hr.fer.zemris.optjava.dz8.de;

import hr.fer.zemris.optjava.dz8.opt.DoubleArraySolution;

import java.util.Random;

/**
 * Implmentacija slucajnog odabira baznog vektora
 * @author Luka Sterbic
 * @version 0.1
 */
public class RandBaseSelection implements IBaseSelection {
	
	private Random rand;
	
	/**
	 * Konstruktor za RandBaseSelection
	 */
	public RandBaseSelection() {
		this.rand = new Random();
	}

	@Override
	public DoubleArraySolution select(DoubleArraySolution target,
			DoubleArraySolution[] population) {
		DoubleArraySolution base = population[rand.nextInt(population.length)];
			
		if(target == base) {
			return select(target, population);
		}
		
		return base;
	}

}
