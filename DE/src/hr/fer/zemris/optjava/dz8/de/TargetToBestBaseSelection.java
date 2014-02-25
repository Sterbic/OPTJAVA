package hr.fer.zemris.optjava.dz8.de;

import java.util.Random;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

import hr.fer.zemris.optjava.dz8.opt.DoubleArraySolution;

/**
 * Implmentacija slucajnog baznog vektora strategijom Target-to-Best
 * @author Luka Sterbic
 * @version 0.1
 */
public class TargetToBestBaseSelection implements IBaseSelection {

	private UnivariateFunction scaleFunction;
	
	/**
	 * Konstruktor za TargetToBestBaseSelection
	 */
	public TargetToBestBaseSelection() {
		this.scaleFunction = new UnivariateFunction() {
			
			private Random rand = new Random();
			
			@Override
			public double value(double x) {
				return x * (2.0 + 0.001 * (rand.nextDouble() - 0.5));
			}
			
		};
	}
	
	@Override
	public DoubleArraySolution select(DoubleArraySolution target,
			DoubleArraySolution[] population) {
		RealVector targetVector = new ArrayRealVector(target.values);
		DoubleArraySolution best = null;
		
		for(DoubleArraySolution solution : population) {
			if(best == null || solution.compareTo(best) > 0) {
				best = solution;
			}
		}
		
		RealVector base = new ArrayRealVector(best.values);
		base = base.subtract(targetVector);
		
		base.mapToSelf(scaleFunction);
		base = base.add(targetVector);
		
		return new DoubleArraySolution(base.toArray());		
	}

}
