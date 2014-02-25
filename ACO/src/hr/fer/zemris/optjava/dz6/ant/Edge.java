package hr.fer.zemris.optjava.dz6.ant;

public class Edge implements Comparable<Edge> {
	
	protected int firstCity;
	protected int secondCity;
	protected double distance;
	protected double pheromones;
	protected double fitness;
	
	public Edge(int firstCity, int secondCity, double distance, double pheromones) {
		this.firstCity = firstCity;
		this.secondCity = secondCity;
		this.distance = distance;
		this.pheromones = pheromones;
		this.fitness = 0.0;
	}
	
	public int getDst(int src) {
		if(src == firstCity) {
			return secondCity;
		} else if(src == secondCity) {
			return firstCity;
		} else {
			throw new IllegalArgumentException("Illegal source city!");
		}
	}
	
	public boolean hasFirstCity() {
		return (firstCity == 0 || secondCity == 0);
	}

	@Override
	public int compareTo(Edge o) {
		double diff = distance - o.distance;
		
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
		sb.append("[").append(firstCity + 1).append(" -> ");
		sb.append(secondCity + 1).append("] = ").append(distance);
		return sb.toString();
	}
	
}
