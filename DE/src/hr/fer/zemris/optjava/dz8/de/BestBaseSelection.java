package hr.fer.zemris.optjava.dz8.de;

import hr.fer.zemris.optjava.dz8.opt.DoubleArraySolution;

/**
 * Implmentacija odabira baznog vektora kao najbolje jedinke
 * @author Luka Sterbic
 * @version 0.1
 */
public class BestBaseSelection implements IBaseSelection {

	@Override
	public DoubleArraySolution select(DoubleArraySolution target,
			DoubleArraySolution[] population) {
		DoubleArraySolution best = null;
		
		for(DoubleArraySolution solution : population) {
			if(best == null || solution.compareTo(best) > 0) {
				best = solution;
			}
		}
		
		return best;
	}

}
