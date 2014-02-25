package hr.fer.zemris.optjava.dz7.ais;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import hr.fer.zemris.optjava.dz7.function.IFunction;
import hr.fer.zemris.optjava.dz7.opt.DoubleArraySolution;
import hr.fer.zemris.optjava.dz7.opt.IOptAlgorithm;

/**
 * Implementacija optimizacijskog algoritma klonalne selekcije
 * @author Luka Sterbic
 * @version 0.3
 */
public class ClonAlg implements IOptAlgorithm<DoubleArraySolution>{

	private static final double DELTA = 1.0;
	
	private int populationSize;
	private int d;
	private double beta;
	private double allowedMaxValue;
	private int maxIterations;
	private IFunction<DoubleArraySolution> function;
	private Comparator<DoubleArraySolution> reverseCompartor;	
	
	/**
	 * KOnstruktor za ClonAlg
	 * @param populationSize velicina populacije
	 * @param d velicina random generirane populacije u svakoj iteraciji
	 * @param beta paramter kloniranja beta
	 * @param allowedMaxValue maksimalna dopustena vrijednost
	 * @param maxIterations maksimalni broj iteracija
	 * @param function funkcija koja se optimira
	 */
	public ClonAlg(int populationSize, int d, double beta,
			double allowedMaxValue, int maxIterations,
			IFunction<DoubleArraySolution> function) {
		this.populationSize = populationSize;
		this.d = d;
		this.beta = beta;
		this.allowedMaxValue = allowedMaxValue;
		this.maxIterations = maxIterations;
		this.function = function;
		
		this.reverseCompartor = new Comparator<DoubleArraySolution>() {
			
			@Override
			public int compare(DoubleArraySolution o1, DoubleArraySolution o2) {
				return -o1.compareTo(o2);
			}
			
		};
	}

	@Override
	public DoubleArraySolution run() {
		DoubleArraySolution[] population = createNewSolutions(populationSize);
		
		for(int iter = 1; iter <= maxIterations; iter++) {
			DoubleArraySolution best = evaluate(population);
			
			if(best.value <= allowedMaxValue) {
				return best;
			}
			
			Arrays.sort(population, reverseCompartor);
			
			List<DoubleArraySolution> clones = new ArrayList<>();
			
			for(int i = 0; i < populationSize; i++) {
				int clone = (int) Math.floor(beta * populationSize / (i + 1));
				
				for(int j = 0; j < clone; j++) {
					clones.add(population[i].duplicate());
				}
			}
			
			hypermutate(clones);
			
			evaluate(clones);
			
			Collections.sort(clones, reverseCompartor);
			clones.subList(0, populationSize).toArray(population);
			
			DoubleArraySolution[] newAntibodies = createNewSolutions(d);
			System.arraycopy(newAntibodies, 0, population, populationSize - d, d);
		}
		
		return evaluate(population);
	}

	/**
	 * Hipermutiraj zadanu populaciju clonova
	 * @param clones populacija klonova
	 */
	private void hypermutate(List<DoubleArraySolution> clones) {
		Random rand = new Random();
		
		for(int i = 0; i < clones.size(); i++) {
			double[] values = clones.get(i).values;
			
			for(int j = 0; j < i; j++) {
				values[rand.nextInt(values.length)] += (rand.nextDouble() - 0.5);
			}
		}
	}

	/**
	 * Evaluiraj danu populaciju
	 * @param population populacija antitijela
	 * @return najbolja jedinka u promatranoj populaciji
	 */
	private DoubleArraySolution evaluate(DoubleArraySolution[] population) {
		DoubleArraySolution best = null;
		
		for(int i = 0; i < population.length; i++) {
			population[i].value = function.valueAt(population[i]);
			population[i].fitness = -population[i].value;
			
			if(best == null || population[i].compareTo(best) > 0) {
				best = population[i];
			}
		}
		
		return best;
	}

	/**
	 * Evaluiraj danu populaciju
	 * @param population populacija antitijela
	 */
	private void evaluate(List<DoubleArraySolution> population) {
		for(DoubleArraySolution solution : population) {
			solution.value = function.valueAt(solution);
			solution.fitness = -solution.value;
		}
	}
	
	/**
	 * Stvori novu populaciju zadane velicine i random inicijaliziraj
	 * @param size velicina populacije
	 * @return nova populacija
	 */
	private DoubleArraySolution[] createNewSolutions(int size) {
		DoubleArraySolution[] population = new DoubleArraySolution[size];
		Random rand = new Random();
		
		for(int i = 0; i < size; i++) {
			population[i] = new DoubleArraySolution(function.dimensions(), rand, DELTA);
		}
		
		return population;
	}
	
}
