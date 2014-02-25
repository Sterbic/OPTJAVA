package hr.fer.zemris.optjava.genetic.programming;

import java.util.Random;

import hr.fer.zemris.optjava.genetic.operators.ICrossover;

/**
 * Implementacija operator krizanja za algoritam genetskog
 * programiranja koji zamijenjuje dva podstabla u roditeljima.
 * @author Luka Sterbic
 * @version 0.2
 */
public class SubtreeSwapCrossover implements ICrossover<AntGPSolution> {

	private Random rand;
	
	/**
	 * Konstruktor za SubtreeSwapCrossover
	 */
	public SubtreeSwapCrossover() {
		this.rand = new Random();
	}
	
	@Override
	public CrossoverResult<AntGPSolution> cross(
			AntGPSolution firstParent, AntGPSolution secondParent) {
		AbstractGPNode first = RandomNodeSelector.select(firstParent.root, rand);
		AbstractGPNode second = RandomNodeSelector.select(secondParent.root, rand);
		
		int firstNodeBalnce = second.totalChilds - first.totalChilds;
		
		AbstractGPNode fpp = first.parent;
		AbstractGPNode spp = second.parent;
		
		if(fpp != null) {
			fpp.children.remove(first);
			fpp.children.add(second);
			firstParent.parentFitness = firstParent.fitness;
		} else {
			firstParent.root = second;
			firstParent.parentFitness = secondParent.fitness;
		}
		
		if(spp != null) {
			spp.children.remove(second);
			spp.children.add(first);
			secondParent.parentFitness = secondParent.fitness;
		} else {
			secondParent.root = first;
			secondParent.parentFitness = firstParent.fitness;
		}
								
		first.parent = spp;
		second.parent = fpp;
		
		while(fpp != null) {
			fpp.totalChilds += firstNodeBalnce;
			fpp = fpp.parent;
		}
		
		while(spp != null) {
			spp.totalChilds -= firstNodeBalnce;
			spp = spp.parent;
		}
		
		return new CrossoverResult<AntGPSolution>(firstParent, secondParent);
	}

}
