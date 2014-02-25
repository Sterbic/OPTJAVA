package hr.fer.zemris.optjava.genetic.programming;

import hr.fer.zemris.optjava.genetic.operators.IMutation;

import java.util.Random;

/**
 * Implementacija operator mutacije za algoritam genetskog
 * programiranja koji zamijenjuje slucajno dobarno podstablo.
 * @author Luka Sterbic
 * @version 0.1
 */
public class SubtreeSwapMutation implements IMutation<AntGPSolution> {

	private AntPopulationCreator antCreator;
	private Random rand;
	
	/**
	 * Konstruktor za SubtreeSwapMutation
	 * @param antCreator objekt koji stvara slucajno podstabla
	 */
	public SubtreeSwapMutation(AntPopulationCreator antCreator) {
		this.antCreator = antCreator;
		this.rand = new Random();		
	}
	
	@Override
	public void mutate(AntGPSolution chromosome) {
		chromosome.parentFitness = chromosome.fitness;
		
		AbstractGPNode swapOut = RandomNodeSelector.select(chromosome.root, rand);
		AbstractGPNode swapIn = antCreator.createSubtree();
		
		int swapBalance = swapIn.totalChilds - swapOut.totalChilds;	
		AbstractGPNode sop = swapOut.parent;
		
		if(sop != null) {
			sop.children.remove(swapOut);
			sop.children.add(swapIn);
		} else {
			chromosome.root = swapIn;
		}
		
		swapIn.parent = sop;
		
		while(sop != null) {
			sop.totalChilds += swapBalance;
			sop = sop.parent;
		}
	}

}
