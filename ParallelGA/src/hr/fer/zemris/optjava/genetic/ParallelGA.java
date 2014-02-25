package hr.fer.zemris.optjava.genetic;

import hr.fer.zemris.optjava.functions.ITSFunction;
import hr.fer.zemris.optjava.genetic.operators.ICrossover;
import hr.fer.zemris.optjava.genetic.operators.IMutation;
import hr.fer.zemris.optjava.genetic.operators.IPopulationCreator;
import hr.fer.zemris.optjava.genetic.operators.ISelection;
import hr.fer.zemris.optjava.opt.IOptAlgorithm;
import hr.fer.zemris.optjava.opt.SingleObjectiveSolution;
import hr.fer.zemris.optjava.rng.EVOThread;
import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.RNG;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Klasa koja implementira generacijski genetski
 * algoritam s paralelnom evalucijom jedinki.
 * @author Luka Sterbic
 * @version 0.9
 * @param <T> tip jedinke
 */
public class ParallelGA<T extends SingleObjectiveSolution>
	implements IOptAlgorithm<T> {
	
	protected static final long SLEEP_INTERVAL = 1;
	
	private final ITSFunction<T> function_;
	private final int populationSize_;
	private IPopulationCreator<T> populationCreator;
	private final ISelection<T> selection_;
	private final List<ICrossover<T>> crossovers_;
	private final List<IMutation<T>> mutations_;
	private double minAllowedFitness;
	private int maxGenerations;
	private T best;
	
	private EVOThread[] threads;
	private GeneratorJob poison;
	private Queue<GeneratorJob> in;
	private Queue<T> out;
	
	/**
	 * Konstruktor za ParallelGA
	 * @param function funkcija koja se optimira
	 * @param populationSize velicina populacije
	 * @param populationCreator objekt koji ce stvoriti pocetnu populaciju
	 * @param selection objekt koji vrsi selekciju roditalja
	 * @param crossovers lista objekata koji vrse krizanje
	 * @param mutations lista objekata koji vrse mutaciju
	 * @param minAllowedFitness minimalna dopustena dobrota
	 * @param maxGenerations maksimalni broj generacija
	 */
	public ParallelGA(ITSFunction<T> function, int populationSize,
			IPopulationCreator<T> populationCreator, ISelection<T> selection,
			List<ICrossover<T>> crossovers,	List<IMutation<T>> mutations,
			double minAllowedFitness, int maxGenerations) {
		this.function_ = function;
		this.populationSize_ = populationSize;
		this.populationCreator = populationCreator;
		this.selection_ = selection;
		this.crossovers_ = crossovers;
		this.mutations_ = mutations;
		this.minAllowedFitness = minAllowedFitness;
		this.maxGenerations = maxGenerations;
		this.best = null;
		
		this.threads = new EVOThread[Runtime.getRuntime().availableProcessors()];
		this.poison = new GeneratorJob(0, null);
		this.in = new ConcurrentLinkedQueue<>();
		this.out = new ConcurrentLinkedQueue<>();
		
		for(int i = 0; i < threads.length; i++) {
			this.threads[i] = new EVOThread(new Runnable() {
				
				@Override
				public void run() {
					while(true) {
						GeneratorJob job = in.poll();
						
						if(job == null) {
							Thread.yield();
						} else {
							if(job == poison) {
								return;
							}
							
							IRNG rand = RNG.getRNG();
							
							for(int i = 0; i < job.n; i++) {
								T r1 = job.population[selection_.select(job.population)];
								T r2 = job.population[selection_.select(job.population)];
								
								int limit = 0;
								while(r1 == r2) {
									if(limit > populationSize_) {
										break;
									}
									
									limit++;
									r2 = job.population[selection_.select(job.population)];
								}
								
								int crossover = rand.nextInt(0, crossovers_.size());
								T child = crossovers_.get(crossover).cross(r1, r2);
								
								int mutation = rand.nextInt(0, mutations_.size());
								mutations_.get(mutation).mutate(child);
							
								child.value = function_.valueAt(child);
								child.fitness = -child.value;
								
								out.add(child);
							}
						}
					}
				}
				
			});
		}
		
		for(EVOThread thread : this.threads) {
			thread.start();
		}
	}
	
	@Override
	public T run() {
		T[] population = populationCreator.create(populationSize_);
		T[] newPopulation = populationCreator.createEmpty(populationSize_);
		
		int jobsPerThread = (populationSize_ - 1) / threads.length;
		int additionalJobs = (populationSize_ - 1) % threads.length;
		
		for(T solution : population) {
			solution.value = function_.valueAt(solution);
			solution.fitness = -solution.value;
			
			if(best == null || solution.compareTo(best) > 0) {
				best = solution;
			}
		}
		
		for(int generation = 1; generation <= maxGenerations; generation++) {
			if(generation % 500 == 0) {
				System.out.println(generation + ". generation, best value: " + best.value);
			}
			
			for(int i = 0; i < threads.length; i++) {
				in.add(new GeneratorJob(jobsPerThread + (i < additionalJobs ? 1 : 0), population));
			}
			
			newPopulation[0] = best;
			
			for(int i = 1; i < populationSize_; i++) {
				newPopulation[i] = pull();
				
				if(best == null || newPopulation[i].compareTo(best) > 0) {
					best = newPopulation[i];
				}
			}
			
			T[] tmp = population;
			population = newPopulation;
			newPopulation = tmp;

			if(best.fitness >= minAllowedFitness) {
				break;
			}			
		}

		shutdownThreads();
		
		return best;
	}
	
	/**
	 * Dohvati evaluiranu jedinku, dretva zapinje
	 * u metodi dok jedinka ne postane dostupna.
	 * @return evaluirana jedinka
	 */
	public T pull() {
		while(true) {
			T t = out.poll();
			
			if(t == null) {
				Thread.yield();
			} else {
				return t;
			}
		}
	}
	
	/**
	 * Ugasi sve dretve osim glavne
	 */
	private void shutdownThreads() {
		for(int i = 0; i < threads.length; i++) {
			in.add(poison);
		}
		
		for(Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {}
		}
	}
	
	/**
	 * Privatna klasa koja predstavlja posao generiranja jedinki
	 * @author Luka Sterbic
	 * @version 0.1
	 */
	private class GeneratorJob {
		
		private int n;
		private T[] population;
		
		/**
		 * Konstruktor za GeneratorJob
		 * @param n broj jedinki koje treba generirati
		 * @param population populacija jedinki
		 */
		public GeneratorJob(int n, T[] population) {
			this.n = n;
			this.population = population;
		}
		
	}

}
