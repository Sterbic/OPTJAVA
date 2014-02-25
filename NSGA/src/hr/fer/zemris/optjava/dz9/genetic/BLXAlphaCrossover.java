package hr.fer.zemris.optjava.dz9.genetic;

import hr.fer.zemris.optjava.dz9.opt.MultipleObjectiveSolution;

import java.util.Random;

/**
 * Klasa koja implementira BLX-Alpha krizanje
 * @author Luka Sterbic
 * @version 0.2
 */
public class BLXAlphaCrossover implements ICrossover<MultipleObjectiveSolution> {
	
	private double alpha;
	private Random rand;
	
	/**
	 * Konstruktor za BLXAlphaCross
	 * @param alpha parametar alpha
	 */
	public BLXAlphaCrossover(double alpha) {
		this.alpha = alpha;
		this.rand = new Random();
	}

	@Override
	public MultipleObjectiveSolution cross(MultipleObjectiveSolution firstParent, 
			MultipleObjectiveSolution secondParent) {
		if(firstParent == null || secondParent == null) {
			throw new IllegalArgumentException("Roditelji ne smiju biti null!");
		}
		
		double[] child = new double[firstParent.dimension];
		
		for(int j = 0; j < child.length; j++) {
			double cMin = Math.min(firstParent.values[j], secondParent.values[j]);
			double cMax = Math.max(firstParent.values[j], secondParent.values[j]);
			double I = alpha * (cMax - cMin);
			
			cMin -= I;
			cMax += I;
			
			child[j] = cMin + rand.nextDouble() * (cMax - cMin);
		}
		
		return new MultipleObjectiveSolution(child);
	}

}
