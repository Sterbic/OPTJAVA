package hr.fer.zemris.optjava.genetic.programming;

import hr.fer.zemris.optjava.ant.Ant;
import hr.fer.zemris.optjava.ant.WorldMap;

/**
 * Implementacija akcije koja izvrsava lijevu podstablo gp stabla
 * ako se hrana nalazi ispred mrava, a desno podstablo inace.
 * @author Luka Sterbic
 * @version 0.3
 */
public class AntIfFoodAhead extends AbstractGPNode {

	private static final int N_CHILDREN = 2; 
	
	@Override
	public int doAction(WorldMap map, Ant ant) {
		if(map.isFoodAhead(ant)) {
			return children.get(0).doAction(map, ant);
		} else {
			return children.get(1).doAction(map, ant);
		}
	}

	@Override
	public int nChildren() {
		return N_CHILDREN;
	}

	@Override
	public AbstractGPNode newLikeThis() {
		return new AntIfFoodAhead();
	}
	
	@Override
	public String toString() {
		return "IfFoodAhead";
	}

}
