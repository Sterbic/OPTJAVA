package hr.fer.zemris.optjava.genetic;

import hr.fer.zemris.optjava.functions.ITSFunction;
import hr.fer.zemris.optjava.genetic.operators.ICrossover;
import hr.fer.zemris.optjava.genetic.operators.IMutation;
import hr.fer.zemris.optjava.genetic.operators.IPopulationCreator;
import hr.fer.zemris.optjava.genetic.operators.ISelection;
import hr.fer.zemris.optjava.genetic.parallel.ParallelEvaluator;
import hr.fer.zemris.optjava.opt.IOptAlgorithm;
import hr.fer.zemris.optjava.opt.SingleObjectiveSolution;
import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.RNG;

import java.util.List;

/**
 * Klasa koja implementira generacijski genetski
 * algoritam s paralelnom evalucijom jedinki.
 * @author Luka Sterbic
 * @version 0.9
 * @param <T> tip jedinke
 */
public class ParallelEvaluationGA<T extends SingleObjectiveSolution>
	implements IOptAlgorithm<T> {
	
	private int populationSize;
	private IPopulationCreator<T> populationCreator;
	private ISelection<T> selection;
	private List<ICrossover<T>> crossovers;
	private List<IMutation<T>> mutations;
	private double minAllowedFitness;
	private int maxGenerations;
	private T best;
	private ParallelEvaluator<T> evaluator;
	
	/**
	 * Konstruktor za ParallelEvaluationGA
	 * @param function funkcija koja se optimira
	 * @param populationSize velicina populacije
	 * @param populationCreator objekt koji ce stvoriti pocetnu populaciju
	 * @param selection objekt koji vrsi selekciju roditalja
	 * @param crossovers lista objekata koji vrse krizanje
	 * @param mutations lista objekata koji vrse mutaciju
	 * @param minAllowedFitness minimalna dopustena dobrota
	 * @param maxGenerations maksimalni broj generacija
	 */
	public ParallelEvaluationGA(
			ITSFunction<T> function, int populationSize,
			IPopulationCreator<T> populationCreator,
			ISelection<T> selection,
			List<ICrossover<T>> crossovers,
			List<IMutation<T>> mutations, double minAllowedFitness,
			int maxGenerations) {
		this.populationSize = populationSize;
		this.populationCreator = populationCreator;
		this.selection = selection;
		this.crossovers = crossovers;
		this.mutations = mutations;
		this.minAllowedFitness = minAllowedFitness;
		this.maxGenerations = maxGenerations;
		this.best = null;
		this.evaluator = new ParallelEvaluator<>(function);
	}
	
	@Override
	public T run() {
		IRNG rand = RNG.getRNG();
		T[] population = populationCreator.create(populationSize);
		T[] newPopulation = populationCreator.createEmpty(populationSize);
		
		evaluate(population);
		
		for(int generation = 1; generation <= maxGenerations; generation++) {
			if(generation % 100 == 0) {
				System.out.println(generation + ". generation, best value: " + best.value);
			}
			
			newPopulation[0] = best;
			
			for(int i = 1; i < populationSize; i++) {
				T r1 = population[selection.select(population)];
				T r2 = population[selection.select(population)];
				
				int limit = 0;
				while(r1 == r2) {
					if(limit > populationSize) {
						break;
					}
					
					limit++;
					r2 = population[selection.select(population)];
				}
				
				int crossover = rand.nextInt(0, crossovers.size());
				T child = crossovers.get(crossover).cross(r1, r2);
				
				int mutation = rand.nextInt(0, mutations.size());
				mutations.get(mutation).mutate(child);
				
				newPopulation[i] = child;
			}
			
			T[] tmp = population;
			population = newPopulation;
			newPopulation = tmp;
			
			evaluate(population);

			if(best.fitness >= minAllowedFitness) {
				break;
			}			
		}

		evaluator.shutdown();
		
		return best;
	}

	/**
	 * Evaluiraj zadanu populaciju
	 * @param population populacija
	 */
	private void evaluate(T[] population) {
		for(T solution : population) {
			evaluator.push(solution);
		}
		
		for(int i = 0; i < populationSize; i++) {
			population[i] = evaluator.pull();
			
			if(best == null || population[i].compareTo(best) > 0) {
				best = population[i];
			}
		}
	}

}
