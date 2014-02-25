package hr.fer.zemris.optjava.genetic;

import hr.fer.zemris.optjava.genetic.operators.ICrossover;
import hr.fer.zemris.optjava.genetic.operators.ICrossover.CrossoverResult;
import hr.fer.zemris.optjava.genetic.operators.IMutation;
import hr.fer.zemris.optjava.genetic.operators.IPopulationCreator;
import hr.fer.zemris.optjava.genetic.operators.ISelection;
import hr.fer.zemris.optjava.opt.IFunction;
import hr.fer.zemris.optjava.opt.IOptAlgorithm;
import hr.fer.zemris.optjava.opt.SingleObjectiveSolution;

import java.util.Random;

/**
 * Klasa koja implementira algoritam genetskog programiranja.
 * @author Luka Sterbic
 * @version 0.5
 * @param <T> tip jedinke
 */
public class GP<T extends SingleObjectiveSolution>
	implements IOptAlgorithm<T> {

	private IFunction<T> function;
	private int populationSize;
	private IPopulationCreator<T> populationCreator;
	private ISelection<T> selection;
	private ICrossover<T> crossover;
	private IMutation<T> mutation;
	private double minAllowedFitness;
	private int maxGenerations;
	private boolean minimize;
	private double pm;
	private double pc;
	private T best;
	
	/**
	 * Konstruktor za GP
	 * @param function funkcija koja se optimira
	 * @param populationSize velicina populacije
	 * @param populationCreator objekt koji ce stvoriti pocetnu populaciju
	 * @param selection objekt koji vrsi selekciju roditalja
	 * @param crossover objekat koji vrse krizanje
	 * @param mutation objekat koji vrse mutaciju
	 * @param minAllowedFitness minimalna dopustena dobrota
	 * @param maxGenerations maksimalni broj generacija
	 * @param pm vjerojatnost mutacije
	 * @param pc vjerojatnost krizanja
	 * @param minimize true ako je problem minimizacijski, false ako je maksimizacijski
	 */
	public GP(
			IFunction<T> function, int populationSize,
			IPopulationCreator<T> populationCreator,
			ISelection<T> selection, ICrossover<T> crossover,
			IMutation<T> mutation, double minAllowedFitness,
			int maxGenerations, double pm, double pc, boolean minimize) {
		this.function = function;
		this.populationSize = populationSize;
		this.populationCreator = populationCreator;
		this.selection = selection;
		this.crossover = crossover;
		this.mutation = mutation;
		this.minAllowedFitness = minAllowedFitness;
		this.maxGenerations = maxGenerations;
		this.pm = pm;
		this.pc = pc;
		this.minimize  = minimize;
		this.best = null;
	}
	
	@Override
	public T run() {
		Random rand = new Random();
		T[] population = populationCreator.create(populationSize);
		T[] newPopulation = populationCreator.createEmpty(populationSize);
		
		for(T solution : population) {
			evaluate(solution);
		}
		
		for(int generation = 1; generation <= maxGenerations; generation++) {
			System.out.println(generation + ". generation, best value: " + best.value);
						
			newPopulation[0] = populationCreator.createCopy(best);
			 
			for(int i = 1; i < populationSize; i++) {
				T r1 = population[selection.select(population)];
				T r1copy = populationCreator.createCopy(r1);

				double operation = rand.nextDouble();
				
				if(operation < pm) {
					newPopulation[i] = r1copy;
					
					mutation.mutate(newPopulation[i]);
					
					if(!populationCreator.isLegal(newPopulation[i])) {
						newPopulation[i] = populationCreator.createCopy(r1);
					}
				} else if(operation < pm + pc) {
					T r2 = population[selection.select(population)];
					T r2copy = populationCreator.createCopy(r2);
					
					CrossoverResult<T> children = crossover.cross(r1copy, r2copy);

					if(!populationCreator.isLegal(children.first)) {
						children.first = populationCreator.createCopy(r1);
					}
					
					newPopulation[i] = children.first;
					evaluate(population[i]);
					i++;
					
					if(i < populationSize) {
						if(!populationCreator.isLegal(children.second)) {
							children.second = populationCreator.createCopy(r2);
						}
						
						newPopulation[i] = children.second;
					}
				} else {
					newPopulation[i] = r1copy;
				}
				
				if(i < populationSize) {
					evaluate(population[i]);
				}
			}
			
			T[] tmp = population;
			population = newPopulation;
			newPopulation = tmp;

			if(best.fitness >= minAllowedFitness) {
				break;
			}			
		}
		
		return best;
	}

	/**
	 * Evaluiraj zadano rjesenje
	 * @param solution rjesenje
	 */
	private void evaluate(T solution) {
		solution.value = function.valueAt(solution);
		solution.fitness = (minimize ? -solution.value : solution.value);
		
		populationCreator.correctFitness(solution);
		
		if(best == null || solution.compareTo(best) > 0) {
			best = solution;
		}
	}
	
}
