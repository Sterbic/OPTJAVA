package hr.fer.zemris.optjava.genetic.operators;

import hr.fer.zemris.optjava.opt.IntegerArraySolution;
import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.RNG;

/**
 * Klasa koja implementira BLX-Alpha krizanje
 * @author Luka Sterbic
 * @version 0.2
 */
public class BLXAlphaCrossover implements ICrossover<IntegerArraySolution> {
	
	private float alpha;
	
	/**
	 * Konstruktor za BLXAlphaCross
	 * @param alpha parametar alpha
	 */
	public BLXAlphaCrossover(float alpha) {
		this.alpha = alpha;
	}

	@Override
	public IntegerArraySolution cross(IntegerArraySolution firstParent, 
			IntegerArraySolution secondParent) {
		if(firstParent == null || secondParent == null) {
			throw new IllegalArgumentException("Roditelji ne smiju biti null!");
		}
		
		IRNG rand = RNG.getRNG();
		
		int dimensions = firstParent.values.length;
		
		if(dimensions != secondParent.values.length) {
			throw new IllegalArgumentException("Dimenzije roditelja se ne podudaraju!");
		}
		
		IntegerArraySolution child = new IntegerArraySolution(dimensions);
		
		for(int j = 0; j < dimensions; j++) {
			int cMin = Math.min(firstParent.values[j], secondParent.values[j]);
			int cMax = Math.max(firstParent.values[j], secondParent.values[j]);
			int I = Math.round(alpha * (cMax - cMin)) + 1;
			
			cMin -= I;
			cMax += I;

			
			child.values[j] = rand.nextInt(cMin, cMax);
		}
		
		return child;
	}

}
