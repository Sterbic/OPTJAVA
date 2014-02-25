package hr.fer.zemris.optjava.dz10.genetic;

import hr.fer.zemris.optjava.dz10.opt.MultipleObjectiveSolution;

import java.util.Random;

/**
 * Implementacija krizanja s jednom tockom prijeloma
 * @author Luka Sterbic
 * @version 0.1
 */
public class OnePointCrossover implements ICrossover<MultipleObjectiveSolution> {

	private Random rand;
	
	/**
	 * Konstruktor za OnePointCrossover
	 */
	public OnePointCrossover() {
		this.rand = new Random();
	}
	
	@Override
	public MultipleObjectiveSolution cross(MultipleObjectiveSolution firstParent,
			MultipleObjectiveSolution secondParent) {
		if(rand.nextBoolean()) {
			MultipleObjectiveSolution tmp = firstParent;
			firstParent = secondParent;
			secondParent = tmp;
		}
		
		int point = rand.nextInt(firstParent.values.length - 1) + 1;
		
		MultipleObjectiveSolution child = firstParent.duplicate();
		for(int i = point; i < child.values.length; i++) {
			child.values[i] = secondParent.values[i];
		}
		
		return child;
	}

}
