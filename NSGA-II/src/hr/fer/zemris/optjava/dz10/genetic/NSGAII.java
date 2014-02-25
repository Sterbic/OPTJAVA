package hr.fer.zemris.optjava.dz10.genetic;

import hr.fer.zemris.optjava.dz10.opt.MultipleObjectiveSolution;
import hr.fer.zemris.optjava.dz10.problems.MOOPProblem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Implementacija algoritma Non-Dominated Sorting Genetic Algorithm
 * @author Luka Sterbic
 * @version 0.1
 */
public class NSGAII {
	
	private static final double BORDER_VALUE = 1E6;

	private MOOPProblem problem;
	private int populationSize;
	private ISelection<MultipleObjectiveSolution> selection;
	private List<ICrossover<MultipleObjectiveSolution>> crossovers;
	private IMutation<MultipleObjectiveSolution> mutation;
	private int maxGenerations;
	
	private double[] mins;
	private double[] maxs;
	
	/**
	 * Konstruktor za NSGA
	 * @param problem problem visekriterijske optimizacije
	 * @param populationSize velicina populacije 
	 * @param k velicina turnirske selekcije
	 * @param crossovers lista objekata koji vrse krizanje
	 * @param mutation objekt koji vrsi mutaciju
	 * @param maxIterations maksimalni broj iteracija
	 * nad decision space-om, false za objective space
	 */
	public NSGAII(MOOPProblem problem, int populationSize, int k,
			List<ICrossover<MultipleObjectiveSolution>> crossovers,
			IMutation<MultipleObjectiveSolution> mutation, int maxIterations) {
		this.problem = problem;
		this.populationSize = populationSize;
		this.selection = new CrowdedTournamentSelection(k);
		this.crossovers = crossovers;
		this.mutation = mutation;
		this.maxGenerations = maxIterations / populationSize;
		
		this.mins = new double[problem.nObjectives()];
		this.maxs = new double[problem.nObjectives()];
	}

	/**
	 * Pokreni algoritam
	 * @return konacna populacija podijeljena u fronte
	 */
	public List<List<MultipleObjectiveSolution>> run() {
		Random rand = new Random();
		MultipleObjectiveSolution[] population = new MultipleObjectiveSolution[populationSize];
		
		for(int i = 0; i < populationSize; i++) {
			population[i] = new MultipleObjectiveSolution(problem.randPoint());
			population[i].objectives = problem.evaluate(population[i].values);
			checkObjRanges(population[i]);
		}
		
		for(int generation = 1; generation <= maxGenerations; generation++) {
			initLevels(population);
			initDistances(population);
			
			MultipleObjectiveSolution[] R = new MultipleObjectiveSolution[2 * populationSize];
			System.arraycopy(population, 0, R, 0, populationSize);
			
			for(int i = populationSize; i < R.length; i++) {
				MultipleObjectiveSolution r1 = population[selection.select(population)];
				MultipleObjectiveSolution r2 = population[selection.select(population)];
				
				while(r1 == r2) {
					r2 = population[selection.select(population)];
				}
				
				int crossover = rand.nextInt(crossovers.size());
				MultipleObjectiveSolution child = crossovers.get(crossover).cross(r1, r2);
				
				mutation.mutate(child);

				problem.checkConstraints(child.values);
				child.objectives = problem.evaluate(child.values);
				
				R[i] = child;
			}
			
			List<List<MultipleObjectiveSolution>> fronts = createFronts(R);
			
			List<MultipleObjectiveSolution> lastFront = null;
			int offset = 0;
			
			for(List<MultipleObjectiveSolution> front : fronts) {
				if(offset + front.size() <= populationSize) {
					for(MultipleObjectiveSolution solution : front) {
						population[offset++] = solution;
					}
				} else {
					lastFront = front;
					break;
				}
			}
			
			if(offset == populationSize) {
				continue;
			}
			
			initDistances(R);
			
			Collections.sort(lastFront, new Comparator<MultipleObjectiveSolution>() {

				@Override
				public int compare(MultipleObjectiveSolution o1,
						MultipleObjectiveSolution o2) {
					double diff = -(o1.distance - o2.distance);
					
					if(diff > 0) {
						return 1;
					} if(diff < 0) {
						return -1;
					} else {
						return 0;
					}
				}
				
			});
			
			for(MultipleObjectiveSolution solution : lastFront) {
				population[offset++] = solution;
				
				if(offset == populationSize) {
					break;
				}
			}
		}

		return createFronts(population);
	}
	
	/**
	 * Osvjezi minimalne i maksimalne vrijednosti ciljnih
	 * funkcija s obzirom na zadano rjesenje.
	 * @param multipleObjectiveSolution rjesenje
	 */
	private void checkObjRanges(MultipleObjectiveSolution solution) {
		double[] objectives = solution.objectives;
		
		for(int i = 0; i < problem.nObjectives(); i++) {
			if(objectives[i] > maxs[i]) {
				maxs[i] = objectives[i];
			} else if(objectives[i] < mins[i]) {
				mins[i] = objectives[i];
			}
		}
		
	}
	
	/**
	 * Inicijaliziraj razinu nedominiranosti za zadanu populaciju
	 * @param population populacija rjesenja
	 */
	private void initLevels(MultipleObjectiveSolution[] population) {
		for(int i = 0; i < population.length; i++) {
			for(int j = 0; j < population.length; j++) {
				if(i != j && population[i].dominates(population[j])) {
					population[j].level++;
				}
			}
		}
	}

	/**
	 * Inicijaliziraj udaljenosti grupiranja za zadanu populaciju
	 * @param population populacija rjesenja
	 */
	private void initDistances(MultipleObjectiveSolution[] population) {
		Arrays.fill(mins, Double.POSITIVE_INFINITY);
		Arrays.fill(maxs, Double.NEGATIVE_INFINITY);
		
		for(MultipleObjectiveSolution solution : population) {
			checkObjRanges(solution);
		}
		
		for(int i = 0; i < problem.nObjectives(); i++) {
			Arrays.sort(population, new DistanceComparator(i));

			population[0].distance += BORDER_VALUE;
			population[population.length - 1].distance += BORDER_VALUE;
			
			for(int j = 1; j < population.length - 1; j++) {
				double lower = population[j - 1].objectives[i];
				double upper = population[j + 1].objectives[i];
				
				population[j].distance += (upper - lower) / (maxs[i] - mins[i]);
			}
		}
	}

	/**
	 * Stvori fronte iz populacije nedominirajucim sortiranjem
	 * @param population trenutna populacija rjesenja
	 * @return lista fronti
	 */
	public List<List<MultipleObjectiveSolution>> createFronts(
			MultipleObjectiveSolution[] population) {
		List<List<MultipleObjectiveSolution>> fronts = new ArrayList<>();
		
		FrontEntry[] entries = new FrontEntry[populationSize];
		for(int i = 0; i < populationSize; i++) {
			entries[i] = new FrontEntry(population[i]);
			population[i].level = 0;
		}
		
		for(int i = 0; i < populationSize; i++) {
			for(int j = 0; j < populationSize; j++) {
				if(i != j) {
					entries[i].checkDominance(entries[j]);
				}
			}
		}
		
		List<FrontEntry> nonDominated = new ArrayList<>();
		for(FrontEntry entry : entries) {
			if(entry.solution.level == 0) {
				nonDominated.add(entry);
			}
		}
		
		while(!nonDominated.isEmpty()) {
			List<FrontEntry> newNonDominated = new ArrayList<>();
			List<MultipleObjectiveSolution> front = new ArrayList<>();
			
			for(FrontEntry entry : nonDominated) {
				front.add(entry.solution);
				
				for(FrontEntry dominated : entry.dominates) {
					dominated.solution.level--;
					
					if(dominated.solution.level == 0) {
						newNonDominated.add(dominated);
					}
				}
			}
			
			nonDominated = newNonDominated;
			fronts.add(front);
		}
		
		return fronts;
	}
	
	/**
	 * Privatna klasa koja modelira rjesenje na fronti
	 * @author Luka Sterbic
	 * @version 0.2
	 */
	private class FrontEntry {
		
		private MultipleObjectiveSolution solution;
		private List<FrontEntry> dominates;
		
		/**
		 * Konstruktor za FrontEntry
		 * @param solution rjesenje
		 */
		public FrontEntry(MultipleObjectiveSolution solution) {
			this.solution = solution;
			this.solution.level = 0;
			this.dominates = new ArrayList<>();
		}
		
		/**
		 * Provjeri relaciju dominacije
		 * @param other drugo rjesenje
		 */
		public void checkDominance(FrontEntry other) {
			if(solution.dominates(other.solution)) {
				other.solution.level++;
				dominates.add(other);
			}
		}

	}
	
	/**
	 * Comparator rjesenja po udaljenosti ciljnih funkcija
	 * @author Luka
	 * @version 0.1
	 */
	private class DistanceComparator implements Comparator<MultipleObjectiveSolution> {
		
		private int index;
		
		/**
		 * Konstruktor za DistanceComparator
		 * @param index indeks po kojemu ce se obaviti usporedba
		 */
		public DistanceComparator(int index) {
			this.index = index;
		}
		
		@Override
		public int compare(MultipleObjectiveSolution o1, MultipleObjectiveSolution o2) {
			double diff = o1.objectives[index] - o2.objectives[index];
			
			if(diff > 0) {
				return 1;
			} else if(diff < 0) {
				return -1;
			} else {
				return 0;
			}
		}
	}
	
}
