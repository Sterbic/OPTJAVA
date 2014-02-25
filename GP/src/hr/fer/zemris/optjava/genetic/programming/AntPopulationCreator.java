package hr.fer.zemris.optjava.genetic.programming;

import hr.fer.zemris.optjava.genetic.operators.IPopulationCreator;

import java.util.Random;

/**
 * Implementacija tvornice rjesenja za problem genetskog programiranja mrava
 * @author Luka Sterbic
 * @version 0.3
 */
public class AntPopulationCreator implements IPopulationCreator<AntGPSolution> {
	
	private static final double FITNESS_CORRECTION = 0.9;
	
	private int maxCreationDepth;
	private int maxLegalDepth;
	private int maxLegalNodes;
	private AbstractGPNode[] functions;
	private AbstractGPNode[] terminals;
	private AbstractGPNode[] allNodes;
	private Random rand;
	
	/**
	 * Konstruktor za AntPopulationCreator
	 * @param maxCreationDepth najveca dopustena
	 * dubina prilikom konstrukcije stabla
	 * @param maxLegalDepth najveca dopustena dubina
	 * @param maxLegalNodes najveci dopusteni broj cvorova
	 * @param functions polje funkcija
	 * @param terminals polje primitiva
	 */
	public AntPopulationCreator(int maxCreationDepth,
			int maxLegalDepth, int maxLegalNodes,
			AbstractGPNode[] functions, AbstractGPNode[] terminals) {
		this.maxCreationDepth = maxCreationDepth;
		this.maxLegalDepth = maxLegalDepth;
		this.maxLegalNodes = maxLegalNodes;
		this.functions = functions;
		this.terminals = terminals;
		this.allNodes = new AbstractGPNode[functions.length + terminals.length];
		this.rand = new Random();
		
		System.arraycopy(functions, 0, allNodes, 0, functions.length);
		System.arraycopy(terminals, 0, allNodes, functions.length, terminals.length);
	}

	@Override
	public AntGPSolution[] create(int populationSize) {
		AntGPSolution[] population = createEmpty(populationSize);
		
		int depths = maxCreationDepth - 1;
		boolean grow = false;
		
		for(int i = 0; i < populationSize; i++) {
			int depth = 2 + (i % depths);
			
			if(depth == 2) {
				grow = !grow;
			}
			
			population[i] = create(depth, grow);
		}
		
		return population;
	}

	@Override
	public AntGPSolution[] createEmpty(int populationSize) {
		return new AntGPSolution[populationSize];
	}

	@Override
	public AntGPSolution createCopy(AntGPSolution solution) {
		return solution.copy();
	}
	
	@Override
	public boolean isLegal(AntGPSolution solution) {
		return (solution.getDepth() <= maxLegalDepth) 
				&& (solution.nNodes() <= maxLegalNodes);
	}
	
	@Override
	public void correctFitness(AntGPSolution solution) {
		if(Math.abs(solution.fitness - solution.parentFitness) < 1E-6) {
			solution.fitness *= FITNESS_CORRECTION;
		}
	}
	
	/**
	 * Stvori slucajno generirano podstablo
	 * @return podstablo
	 */
	public AbstractGPNode createSubtree() {
		AbstractGPNode root = getAny();
		createRecursive(root, 2, rand.nextInt(maxCreationDepth - 1) + 2, rand.nextBoolean());
		return root;
	}
	
	/**
	 * Stvori novo rjesenje
	 * @param maxDepth maksimalna dubina rjesenja
	 * @param grow true ako se koristi grow metoda,
	 * false ako se koristi full metoda
	 * @return novo rjesenje
	 */
	private AntGPSolution create(int maxDepth, boolean grow) {
		AbstractGPNode root = getFunction();
		createRecursive(root, 2, maxDepth, grow);
		return new AntGPSolution(root);
	}
	
	/**
	 * Rekurzivno stvori stablo rjesenja
	 * @param root trenutni korijen stabla
	 * @param depth trenutna dubina
	 * @param maxDepth maksimalna dubina stabla
	 * @param grow true ako se koristi grow metoda,
	 * false ako se koristi full metoda
	 * @return dubina stvorenog rjesenja
	 */
	private int createRecursive(AbstractGPNode root, int depth, int maxDepth, boolean grow) {
		int ret = depth;
		
		for(int i = 0; i < root.nChildren(); i++) {
			if(depth == maxDepth) {
				root.addChildren(getTerminal());
			} else {
				AbstractGPNode child = null;
				
				if(grow) {
					child = getAny();
				} else {
					child = getFunction();
				}
				
				root.addChildren(child);
				int newDepth = createRecursive(child, depth + 1, maxDepth, grow);
				
				if(newDepth > ret) {
					ret = newDepth;
				}
			}
		}
		
		return ret;		
	}
	
	/**
	 * Vrati slucajno odabranu funkciju
	 * @return funkcija
	 */
	private AbstractGPNode getFunction() {
		return functions[rand.nextInt(functions.length)].newLikeThis();
	}
	
	/**
	 * Vrati slucajno odabrani primitiv
	 * @return primitiv
	 */
	private AbstractGPNode getTerminal() {
		return terminals[rand.nextInt(terminals.length)].newLikeThis();
	}
	
	/**
	 * Vrati slucajno odabrani cvor
	 * @return cvor
	 */
	private AbstractGPNode getAny() {
		return allNodes[rand.nextInt(allNodes.length)].newLikeThis();
	}
	
}
