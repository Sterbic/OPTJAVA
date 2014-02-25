package hr.fer.zemris.optjava.genetic.programming;

import hr.fer.zemris.optjava.opt.SingleObjectiveSolution;

/**
 * Klasa koja modelira program koji ce mrav koristiti
 * za kretanje u svijetu i trazenje hrane.
 * @author Luka Sterbic
 * @version 0.4
 */
public class AntGPSolution extends SingleObjectiveSolution {
	
	public AbstractGPNode root;
	public double parentFitness;
	
	/**
	 * Konstruktor za AntGPSolution
	 * @param root korijen stabla rjesenja
	 */
	public AntGPSolution(AbstractGPNode root) {
		this.root = root;
	}
	
	/**
	 * Vrati ukupan broj cvorova u rjesenju
	 * @return ukupan broj cvorova u rjesenju
	 */
	public int nNodes() {
		return root.totalChilds;
	}
	
	/**
	 * Dohvati dubinu rjesenja, linearna slozenost
	 * @return dubina rjesenja
	 */
	public int getDepth() {
		return recursiveDepth(root);
	}
	
	/**
	 * Rekurzivna metoda za izracun dubine rjesenja
	 * @param root korijen rjsenja
	 * @return dubina rjesenja
	 */
	private int recursiveDepth(AbstractGPNode root) {
		if(root.totalChilds == 0) {
			return 1;
		}
		
		int maxDepth = 0;
		
		for(AbstractGPNode child : root.children) {
			int newDepth = recursiveDepth(child);
			
			if(newDepth > maxDepth) {
				maxDepth = newDepth;
			}
		}
		
		return 1 + maxDepth;
	}
	
	/**
	 * Stvori kopiju ovog rjesenja
	 * @return kopija rjesenja
	 */
	public AntGPSolution copy() {
		AntGPSolution copy = new AntGPSolution(root.deepCopy());
		
		copy.fitness = fitness;
		copy.value = value;
		
		return copy;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		root.createString(sb, 0);
		return sb.toString();
	}

}
