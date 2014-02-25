package hr.fer.zemris.optjava.genetic.programming;

import hr.fer.zemris.optjava.ant.Ant;
import hr.fer.zemris.optjava.ant.WorldMap;

/**
 * Implementacija akcije koja okrece mrava za 90 stupnjeva u lijevo
 * @author Luka Sterbic
 * @version 0.3
 */
public class AntTurnLeft extends AbstractGPNode {

	private static final int N_CHILDREN = 0; 

	@Override
	public int doAction(WorldMap map, Ant ant) {
		ant.turn(false);
		return 1;
	}

	@Override
	public int nChildren() {
		return N_CHILDREN;
	}

	@Override
	public AbstractGPNode newLikeThis() {
		return new AntTurnLeft();
	}
	
	@Override
	public String toString() {
		return "LEFT";
	}

}
