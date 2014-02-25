package hr.fer.zemris.optjava.dz8.de;

import java.util.Random;

import hr.fer.zemris.optjava.dz8.opt.DoubleArraySolution;

/**
 * Implementacija DE eksponencijalnog krizanja
 * @author Luka Sterbic
 * @version 0.1
 */
public class DEExpCrossover implements ICrossover {

	private double cr;
	private Random rand;
	
	/**
	 * Konstruktor za DEExpCrossover
	 * @param cr parametar cr
	 */
	public DEExpCrossover(double cr) {
		this.cr = cr;
		this.rand = new Random();
	}
	
	@Override
	public DoubleArraySolution cross(DoubleArraySolution mutant,
			DoubleArraySolution target) {
		int dimension = mutant.values.length;
		DoubleArraySolution child = new DoubleArraySolution(dimension);
		
		int position = rand.nextInt(dimension);
		child.values[position] = mutant.values[position];
		
		boolean flag = true;
		for(int i = 1; i < dimension; i++) {
			int index = (position + i) % dimension;
			
			if(flag && rand.nextDouble() <= cr) {
				child.values[index] = mutant.values[index];
			} else {
				child.values[index] = target.values[index];
			}
		}
		
		return child;
	}

}
