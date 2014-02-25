package hr.fer.zemris.optjava.genetic.programming;

import hr.fer.zemris.optjava.ant.Ant;
import hr.fer.zemris.optjava.ant.WorldMap;

/**
 * Implementacija funkcije koja izvodi n podstabla u gp stablu
 * @author Luka Sterbic
 * @version 0.3
 */
public class AntNProg extends AbstractGPNode {
	
	private int n;
	
	/**
	 * Konstruktor za AntNProg
	 * @param n broj podstable koji ce se izvrsiti
	 */
	public AntNProg(int n) {
		this.n = n;
	}
	
	@Override
	public int doAction(WorldMap map, Ant ant) {
		int actions = 0;
		
		for(int i = 0; i < n; i++) {
			actions += children.get(i).doAction(map, ant);
		}
		
		return actions;
	}

	@Override
	public int nChildren() {
		return n;
	}

	@Override
	public AbstractGPNode newLikeThis() {
		return new AntNProg(n);
	}
	
	@Override
	public String toString() {
		return "Prog" + n;
	}

}
