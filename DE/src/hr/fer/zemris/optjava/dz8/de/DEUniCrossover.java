package hr.fer.zemris.optjava.dz8.de;

import java.util.Random;

import hr.fer.zemris.optjava.dz8.opt.DoubleArraySolution;

/**
 * Implementacija DE uniformnog krizanja
 * @author Luka Sterbic
 * @version 0.1
 */
public class DEUniCrossover implements ICrossover {

	private double cr;
	private Random rand;
	
	/**
	 * Konstruktor za DEExpCrossover
	 * @param cr parametar cr
	 */
	public DEUniCrossover(double cr) {
		this.cr = cr;
		this.rand = new Random();
	}
	
	@Override
	public DoubleArraySolution cross(DoubleArraySolution mutant,
			DoubleArraySolution target) {
		int dimension = mutant.values.length;
		DoubleArraySolution child = new DoubleArraySolution(dimension);
		
		int position = rand.nextInt(dimension);
		
		for(int i = 0; i < dimension; i++) {
			if(i == position || rand.nextDouble() <= cr) {
				child.values[i] = mutant.values[i];
			} else {
				child.values[i] = target.values[i];
			}
		}
		
		return child;
	}

}
