package hr.fer.zemris.optjava.dz10.genetic;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import hr.fer.zemris.optjava.dz10.opt.MultipleObjectiveSolution;

/**
 * Implementacija grupirajuce turnirske selekcije
 * @author Luka
 * @version 0.1
 */
public class CrowdedTournamentSelection implements ISelection<MultipleObjectiveSolution> {

	private int k;
	private Random rand;
	
	/**
	 * Konstruktor za CrowdedTournamentSelection
	 * @param k broj jedinki koje ulaze u turnir
	 */
	public CrowdedTournamentSelection(int k) {
		this.k = k;
		this.rand = new Random();
	}
	
	
	@Override
	public int select(MultipleObjectiveSolution[] population) {
		Set<Integer> tournament = new HashSet<>();
		
		while(tournament.size() < k) {
			tournament.add(rand.nextInt(population.length));
		}
		
		int winner = -1;
		
		for(int index : tournament) {
			if(winner == -1) {
				winner = index;
			} else {
				if(population[index].level < population[winner].level) {
					winner = index;
				} else if(population[index].level == population[winner].level) {
					if(population[index].distance > population[winner].distance) {
						winner = index;
					}
				}
			}
		}
		
		return winner;
	}

}
