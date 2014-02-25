package hr.fer.zemris.optjava.dz8.de;

import hr.fer.zemris.optjava.dz8.opt.DoubleArraySolution;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

/**
 * Linearni kreator mutanta
 * @author Luka Sterbic
 * @version 0.1
 */
public class LinearMutantCreator implements IMutantCreator {

	private int level;
	private Random rand;
	private UnivariateFunction scaleFunction;
	
	/**
	 * Konstruktor za LinearMutantCreator
	 * @param level broj linearnih clanova
	 */
	public LinearMutantCreator(int level) {
		this.level = level;
		this.rand = new Random();
		
		this.scaleFunction = new UnivariateFunction() {
			
			private Random rand = new Random();
			
			@Override
			public double value(double x) {
				return x * (0.5 + 0.001 * (rand.nextDouble() - 0.5));
			}
			
		};
	}

	@Override
	public DoubleArraySolution create(DoubleArraySolution target,
			DoubleArraySolution base, DoubleArraySolution[] population) {
		int dimension = target.values.length;
		RealVector mutant = new ArrayRealVector(dimension);
		
		List<DoubleArraySolution> illegal = new ArrayList<>();
		illegal.add(target);
		illegal.add(base);
		
		for(int l = 0; l < level; l++) {
			DoubleArraySolution x1 = randDiffPick(population, illegal);
			illegal.add(x1);
			
			DoubleArraySolution x2 = randDiffPick(population, illegal);
			illegal.add(x2);
			
			mutant = mutant.add(new ArrayRealVector(x1.values));
			mutant = mutant.subtract(new ArrayRealVector(x2.values));
		}
		
		mutant.mapToSelf(scaleFunction);
		mutant = mutant.add(new ArrayRealVector(base.values));
		
		return new DoubleArraySolution(mutant.toArray());
	}
	
	/**
	 * Vrati integer iz intervala [0, range - 1] koji nije u polju ilegalnih vrijednosti
	 * @param population raspon intervala
	 * @param illegal lista ilegalnih vrijednosti
	 * @return jedinka koja nije u listi zabranjenih
	 */
	private DoubleArraySolution randDiffPick(DoubleArraySolution[] population,
			List<DoubleArraySolution> illegal) {
		int next = rand.nextInt(population.length);
		
		for(DoubleArraySolution solution : illegal) {
			if(solution == population[next]) {
				return randDiffPick(population, illegal);
			}
		}
		
		return population[next];
	}

}
