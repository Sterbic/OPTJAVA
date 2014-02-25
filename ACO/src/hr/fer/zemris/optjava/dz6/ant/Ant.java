package hr.fer.zemris.optjava.dz6.ant;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Klasa koja modelira jednog mrava u MMAS algoritmu
 * @author Luka Sterbic
 * @version 0.1
 */
public class Ant implements Comparable<Ant> {
	
	protected double totalDistance;
	protected List<Edge> path;
	protected Set<Integer> visitedCities;
	
	/**
	 * Konstruktor za Ant
	 */
	public Ant() {
		this.totalDistance = 0.0;
		this.path = new ArrayList<>();
		this.visitedCities = new LinkedHashSet<>();
	}
	
	/**
	 * Dodaj novi brid na putu mrava
	 * @param edge novi brid
	 */
	public void addEdge(Edge edge) {
		path.add(edge);
		totalDistance += edge.distance;
	}
	
	/**
	 * Vrati ukupnu udaljenost koju je mrav prosao
	 * @return ukupna udaljenost
	 */
	public double getTotalDistance() {
		return totalDistance;
	}

	@Override
	public int compareTo(Ant o) {
		double diff = o.totalDistance - totalDistance;
		
		if(diff > 0) {
			return 1;
		} else if(diff < 0) {
			return -1;
		} else {
			return 0;
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		String last = null;
		
		for(int city : visitedCities) {
			if(city == 0) {
				last = sb.toString();
				sb = new StringBuilder("[");
			}
			
			sb.append(city + 1).append(", ");
		}
		
		if(last.length() >= 2) {
			last = last.substring(0, last.length() - 2);
		}
		
		return sb.toString() + last + "]";
	}

}
