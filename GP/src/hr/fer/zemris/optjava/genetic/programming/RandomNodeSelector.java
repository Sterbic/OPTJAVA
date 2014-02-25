package hr.fer.zemris.optjava.genetic.programming;

import java.util.Random;

/**
 * Klasa koja sadrzi staticku metodu za odabir slucajnog cvora u stablu
 * @author Luka Sterbic
 * @version 0.1
 */
public class RandomNodeSelector {
	
	/**
	 * Izaberi slucajni cvor u stablu
	 * @param root cvor stabla
	 * @param rand random objekt
	 * @return slucajni cvor
	 */
	public static AbstractGPNode select(AbstractGPNode root, Random rand) {
		if(root.totalChilds == 0) {
			return root;
		}
		
		double choice = rand.nextDouble();
		double p = 1.0 / root.totalChilds;
		
		if(choice < p) {
			return root;
		}
		
		for(AbstractGPNode child : root.children) {
			p += (child.totalChilds + 1.0) / root.totalChilds;
			
			if(choice < p) {
				return select(child, rand);
			}
		}
		
		return null;
	}

}
