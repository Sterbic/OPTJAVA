package hr.fer.zemris.optjava.genetic.programming;

import hr.fer.zemris.optjava.ant.Ant;
import hr.fer.zemris.optjava.ant.WorldMap;

/**
 * Implementacija akcije koja pomice mrava za jedno polje naprijed
 * @author Luka Sterbic
 * @version 0.3
 */
public class AntMove extends AbstractGPNode {
	
	private static final int N_CHILDREN = 0; 

	@Override
	public int doAction(WorldMap map, Ant ant) {
		ant.forward();
		map.antMoved(ant);
		return 1;
	}

	@Override
	public int nChildren() {
		return N_CHILDREN;
	}

	@Override
	public AbstractGPNode newLikeThis() {
		return new AntMove();
	}
	
	@Override
	public String toString() {
		return "MOVE";
	}

}
