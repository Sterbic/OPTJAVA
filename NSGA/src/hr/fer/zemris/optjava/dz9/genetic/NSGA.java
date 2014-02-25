package hr.fer.zemris.optjava.dz9.genetic;

import hr.fer.zemris.optjava.dz9.opt.MultipleObjectiveSolution;
import hr.fer.zemris.optjava.dz9.problems.MOOPProblem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Implementacija algoritma Non-Dominated Sorting Genetic Algorithm
 * @author Luka Sterbic
 * @version 0.1
 */
public class NSGA {

	private MOOPProblem problem;
	private int populationSize;
	private ISelection<MultipleObjectiveSolution> selection;
	private List<ICrossover<MultipleObjectiveSolution>> crossovers;
	private IMutation<MultipleObjectiveSolution> mutation;
	private int maxGenerations;
	private boolean decisionSpaceFS;
	private double alpha;
	private double sigmaShare;
	
	/**
	 * Konstruktor za NSGA
	 * @param problem problem visekriterijske optimizacije
	 * @param populationSize velicina populacije 
	 * @param crossovers lista objekata koji vrse krizanje
	 * @param mutation objekt koji vrsi mutaciju
	 * @param maxIterations maksimalni broj iteracija
	 * @param decisionSpaceFS true ako se fitness sharing provodi
	 * nad decision space-om, false za objective space
	 * @param alpha parametar alfa
	 * @param sigmaShare parametar sigmaShare
	 */
	public NSGA(MOOPProblem problem, int populationSize,
			List<ICrossover<MultipleObjectiveSolution>> crossovers,
			IMutation<MultipleObjectiveSolution> mutation, int maxIterations,
			boolean decisionSpaceFS, double alpha, double sigmaShare) {
		this.problem = problem;
		this.populationSize = populationSize;
		this.selection = new RouletteSelection();
		this.crossovers = crossovers;
		this.mutation = mutation;
		this.maxGenerations = maxIterations / populationSize;
		this.decisionSpaceFS = decisionSpaceFS;
		this.alpha = alpha;
		this.sigmaShare = sigmaShare;
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
		}
		
		for(int generation = 1; generation <= maxGenerations; generation++) {
			List<List<MultipleObjectiveSolution>> fronts = createFronts(population);
			
			double fitness = populationSize;
			for(List<MultipleObjectiveSolution> front : fronts) {
				double min = Double.POSITIVE_INFINITY;
				
				for(MultipleObjectiveSolution solution : front) {
					solution.fitness = fitness;
					correctFitness(solution, population);
					min = Math.min(min, solution.fitness);
				}
				
				fitness = 0.95 * min;
			}
			
			MultipleObjectiveSolution[] newPopulation = 
					new MultipleObjectiveSolution[populationSize];
			
			for(int i = 0; i < populationSize; i++) {
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
				
				newPopulation[i] = child;
			}
			
			population = newPopulation;
		}

		return createFronts(population);
	}
	
	/**
	 * Korigiraj dobrotu jedinke po fitness sharing principu
	 * @param solution jedinka
	 * @param population trenutna populacija
	 */
	private void correctFitness(MultipleObjectiveSolution solution,
			MultipleObjectiveSolution[] population) {
		double nc = 0.0;
		
		for(MultipleObjectiveSolution other : population) {
			double d = distance(solution, other);
			
			if(d <= sigmaShare) {
				nc += 1 - Math.pow(d / sigmaShare, alpha);
			}
		}
		
		solution.fitness /= nc;		
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
			if(entry.level == 0) {
				nonDominated.add(entry);
			}
		}
		
		while(!nonDominated.isEmpty()) {
			List<FrontEntry> newNonDominated = new ArrayList<>();
			List<MultipleObjectiveSolution> front = new ArrayList<>();
			
			for(FrontEntry entry : nonDominated) {
				front.add(entry.solution);
				
				for(FrontEntry dominated : entry.dominates) {
					dominated.level--;
					
					if(dominated.level == 0) {
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
	 * Izracuna udaljenost izmedju dvije jedinke
	 * @param first prva jedinka
	 * @param second druga jedinka
	 * @return udaljenost medju jedinkama
	 */
	private double distance(MultipleObjectiveSolution first,
			MultipleObjectiveSolution second) {
		double[] firstVals = null;
		double[] secondVals = null;
		double[] ranges = null;
		
		if(decisionSpaceFS) {
			firstVals = first.values;
			secondVals = second.values;
			ranges = problem.getRanges();
		} else {
			firstVals = first.objectives;
			secondVals = second.objectives;
			ranges = problem.getObjectiveRanges();
		}
		
		double distance = 0.0;
		
		for(int i = 0; i < firstVals.length; i++) {
			double diff = (firstVals[i] - secondVals[i]) / ranges[i];
			distance += Math.pow(diff, 2);
		}
		
		return Math.sqrt(distance);
	}
	
	/**
	 * Privatna klasa koja modelira rjesenje na fronti
	 * @author Luka Sterbic
	 * @version 0.1
	 */
	private class FrontEntry {
		
		private MultipleObjectiveSolution solution;
		private int level;
		private List<FrontEntry> dominates;
		
		/**
		 * Konstruktor za FrontEntry
		 * @param solution rjesenje
		 */
		public FrontEntry(MultipleObjectiveSolution solution) {
			this.solution = solution;
			this.level = 0;
			this.dominates = new ArrayList<>();
		}
		
		/**
		 * Provjeri relaciju dominacije
		 * @param other drugo rjesenje
		 */
		public void checkDominance(FrontEntry other) {
			if(solution.dominates(other.solution)) {
				other.level++;
				dominates.add(other);
			}
		}
		
	}
	
}
