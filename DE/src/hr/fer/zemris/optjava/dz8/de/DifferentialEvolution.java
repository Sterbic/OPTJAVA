package hr.fer.zemris.optjava.dz8.de;

import hr.fer.zemris.optjava.dz8.function.IFunction;
import hr.fer.zemris.optjava.dz8.opt.DoubleArraySolution;
import hr.fer.zemris.optjava.dz8.opt.IOptAlgorithm;

import java.util.Random;

/**
 * Implementacija diferencijske evolucije
 * Strategija: DE/rand/1
 * @author Luka Sterbic
 * @version 0.3
 */
public class DifferentialEvolution implements IOptAlgorithm<DoubleArraySolution> {
	
	private static final double DELTA = 1.0;
	
	private int populationSize;
	private double allowedMaxValue;
	private int maxIterations;
	private IFunction<DoubleArraySolution> function;
	private IBaseSelection baseSelection;
	private ICrossover crossover;
	private IMutantCreator mutationCreator;
	private Random rand;
	
	/**
	 * Konstruktor za DifferentialEvolution
	 * @param populationSize velicina populacije
	 * @param allowedMaxValue maksimalna dopustena vrijednost
	 * @param maxIterations maksimalni broj iteracija
	 * @param function funkcija koja se optimira
	 * @param baseSelection objekt koji vrsi selekciju baznog vektora
	 * @param crossover objekt koji vrsi krizanje
	 * @param mutationCreator objekt koji generira vektor mutannt
	 */
	public DifferentialEvolution(int populationSize, double allowedMaxValue,
			int maxIterations, IFunction<DoubleArraySolution> function,
			IBaseSelection baseSelection, ICrossover crossover,
			IMutantCreator mutationCreator) {
		this.populationSize = populationSize;
		this.allowedMaxValue = allowedMaxValue;
		this.maxIterations = maxIterations;
		this.function = function;
		this.baseSelection = baseSelection;
		this.crossover = crossover;
		this.mutationCreator = mutationCreator;
		
		this.rand = new Random();
	}

	@Override
	public DoubleArraySolution run() {
		int dimension = function.dimensions();

		DoubleArraySolution[] population = new DoubleArraySolution[populationSize];
		
		for(int i = 0; i < populationSize; i++) {
			population[i] = new DoubleArraySolution(dimension, rand, DELTA);
			evaluate(population[i]);
		}
		
		for(int iter = 1; iter <= maxIterations; iter++) {			
			DoubleArraySolution[] newPopulation = new DoubleArraySolution[populationSize];
			
			for(int i = 0; i < populationSize; i++) {
				DoubleArraySolution target = population[i];
				
				DoubleArraySolution base = baseSelection.select(target, population);
				
				DoubleArraySolution mutant = mutationCreator.create(target, base, population);
				
				DoubleArraySolution test = crossover.cross(mutant, target);
				
				evaluate(test);
				
				if(test.value <= allowedMaxValue) {
					return test;
				}
				
				if(test.fitness >= target.fitness) {
					newPopulation[i] = test;
					System.out.println(iter + ": new val: " + newPopulation[i].value);
				} else {
					newPopulation[i] = target;
				}
			}
			
			population = newPopulation;
		}
		
		DoubleArraySolution best = null;
		
		for(DoubleArraySolution solution : population) {
			if(best == null || solution.compareTo(best) > 0) {
				best = solution;
			}
		}
		
		return best;
	}
	
	/**
	 * Evaluiraj zadano rjesenje
	 * @param solution rjesenje
	 */
	private void evaluate(DoubleArraySolution solution) {
		solution.value = function.valueAt(solution);
		solution.fitness = -solution.value;
	}

}
