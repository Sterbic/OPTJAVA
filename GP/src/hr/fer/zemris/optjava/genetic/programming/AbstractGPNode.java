package hr.fer.zemris.optjava.genetic.programming;

import hr.fer.zemris.optjava.ant.Ant;
import hr.fer.zemris.optjava.ant.WorldMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Apstrkatna klasa koja predstavlja cvor u gp stablu
 * @author Luka Sterbic
 * @version 0.4
 */
public abstract class AbstractGPNode {
	
	protected AbstractGPNode parent;
	protected int totalChilds;
	protected List<AbstractGPNode> children;

	/**
	 * Konstruktor za AbstractGPNode
	 */
	public AbstractGPNode() {
		this.parent = null;
		this.totalChilds = 0;
		this.children = new ArrayList<>();
	}
	
	/**
	 * Dodaj dijete ovom cvoru
	 * @param child cvor dijete
	 */
	public void addChildren(AbstractGPNode child) {
		child.parent = this;
		children.add(child);
		
		AbstractGPNode current = this;
		
		while(current != null) {
			current.totalChilds++;
			current = current.parent;
		}
	}
	
	/**
	 * Stvori duboku kopiju ovog cvora
	 * @return duboka kopija cvora
	 */
	public AbstractGPNode deepCopy() {
		AbstractGPNode copy = newLikeThis();
		copy.totalChilds = totalChilds;
		
		for(AbstractGPNode child : children) {
			AbstractGPNode childCopy = child.deepCopy();
			childCopy.parent = copy;
			copy.children.add(childCopy);
		}
		
		return copy;
	}
	
	/**
	 * Izvedi akciju koja je spremljena u ovom cvoru
	 * @param map mapa svijeta
	 * @param ant mrav
	 * @return ukupan broj akcija mrava
	 */
	public abstract int doAction(WorldMap map, Ant ant);
	
	/**
	 * Vrati broj djece koje ovaj cvor treba imat
	 * @return broj djece
	 */
	public abstract int nChildren();
	
	/**
	 * Stvori novi cvor koji je istog tipa kao ovaj cvor
	 * @return novi cvor ovog tipa
	 */
	public abstract AbstractGPNode newLikeThis();

	/**
	 * Stvori stringovni zapis stabla
	 * @param sb buffer u koji ce se string postepeno graditi
	 * @param depth trenutna dubina stabla 
	 */
	public void createString(StringBuilder sb, int depth) {
		sb.append(this.toString());
		
		if(nChildren() != 0) {
			sb.append("(");
		}		
		
		for(int i = 0; i < nChildren(); i++) {
			children.get(i).createString(sb, depth + 1);
			
			if(i != nChildren() - 1) {
				sb.append(", ");
			}
		}
				
		if(nChildren() != 0) {		
			sb.append(")");
		}
	}

}
