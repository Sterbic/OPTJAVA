package hr.fer.zemris.optjava.genetic.operators;

import java.util.Random;

import hr.fer.zemris.optjava.opt.SingleObjectiveSolution;

/**
 * Klasa koje implementira n-turnirsku selekciju
 * @author Luka Sterbic
 * @version 0.4
 * @param <T> tip jedinke
 */
public class TournamentSelection<T extends SingleObjectiveSolution> implements ISelection<T> {

	private int nTournament;
	private boolean best;
	private Random rand;
	
	/**
	 * Konstruktor za TournamentSelection
	 * @param nTournament broj jedinki koje ulaze u turnir
	 * false ako pobjedjuje najlosija jedinka
	 */
	public TournamentSelection(int nTournament) {
		this(nTournament, true);
	}
	
	/**
	 * Konstruktor za TournamentSelection
	 * @param nTournament broj jedinki koje ulaze u turnir
	 * @param best true ako pobjedjuje najbolja jedinka,
	 * false ako pobjedjuje najlosija jedinka
	 */
	public TournamentSelection(int nTournament, boolean best) {
		if(nTournament < 2) {
			throw new IllegalArgumentException("Turnirska selekcija mora biti barem 2-turnirska!");
		}
		
		this.nTournament = nTournament;
		this.best = best;
		this.rand = new Random();
	}
	
	@Override
	public int select(T[] population) {
		int len = 0;
		for(int i = 0; i < population.length; i++) {
			if(population[i] == null) {
				break;
			}
			
			len = i;
		}
		
		if(len < 1) {
			System.err.println("neg len");
		}
		
		int selected = rand.nextInt(len);
		
		for(int i = 1; i < nTournament; i++) {
			int next = rand.nextInt(len);
			T t = population[next];
			
			if(best && t.compareTo(population[selected]) > 0) {
				selected = next;
			}
			
			if(!best && t.compareTo(population[selected]) < 0) {
				selected = next;
			}
		}
		
		return selected;
	}

}
