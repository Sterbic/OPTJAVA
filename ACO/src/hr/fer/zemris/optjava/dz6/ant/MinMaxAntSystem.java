package hr.fer.zemris.optjava.dz6.ant;

import hr.fer.zemris.optjava.dz6.algebra.Matrix;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Klasa koja implementira mravlji algoritam MMAS
 * @author Luka Sterbic
 * @version 0.1
 */
public class MinMaxAntSystem {
	
	private int candidatesSize;
	private int ants;
	private int maxIterations;
	private double alpha;
	private double beta;
	private double a;
	private double rho;
	
	private int cities;
	private Random rand;
	private List<List<Edge>> edges;
	private List<Edge> allEdges;
	private Matrix heuristics;
	
	private Ant bestSoFarAnt;
	private double tauMin;
	private double tauMax;
	
	/**
	 * Konstruk za MMAS
	 * @param candidatesSize broj kandidata po cvoru
	 * @param ants broj mrava u koloniji
	 * @param maxIterations maksimlani broj iteracija
	 * @param alpha parametar alfa
	 * @param beta parametar beta
	 * @param a parametar a
	 * @param rho stop isparavanja
	 * @param distances matrica udaljenosti
	 */
	public MinMaxAntSystem(int candidatesSize, int ants, int maxIterations,
			double alpha, double beta, double a, double rho, Matrix distances) {
		this.candidatesSize = candidatesSize;
		this.ants = ants;
		this.maxIterations = maxIterations;
		this.alpha = alpha;
		this.beta = beta;
		this.a = a;
		this.rho = rho;
		this.cities = distances.getCols();
		this.rand = new Random();
		
		initHeuristics(distances);
		initEdges(distances);
		initTaus();
	}

	/**
	 * Inicijaliziraj tauMax i tauMin
	 */
	private void initTaus() {
		Ant ant = new Ant();
		
		int start = rand.nextInt(cities);
		int current = start;
		
		for(int step = 0; step < cities - 1; step++) {
			ant.visitedCities.add(current);
			
			for(Edge edge : edges.get(current)) {
				if(!ant.visitedCities.contains(edge.getDst(current))) {
					ant.addEdge(edge);
					break;
				}
			}
		}
		
		for(Edge edge : edges.get(current)) {
			if(edge.getDst(current) == start) {
				ant.addEdge(edge);
				break;
			}
		}
		
		tauMax = 1.0 / (rho * ant.totalDistance);
		tauMin = tauMax / a;
		
		for(Edge edge : allEdges) {
			edge.pheromones = tauMax;
		}
	}

	/**
	 * Inicijaliziraj matricu heuristika
	 * @param distances matrica udaljenosti
	 */
	private void initHeuristics(Matrix distances) {
		heuristics = new Matrix(cities, cities);
		
		for(int i = 0; i < cities - 1; i++) {
			for(int j = i + 1; j < cities; j++) {
				double hValue = Math.pow(1.0 / distances.get(i, j), beta);

				heuristics.set(i, j, hValue);
				heuristics.set(j, i, hValue);
			}
		}
	}
	
	/**
	 * Inicijaliziraj bridove
	 * @param distances matrica udaljenosti
	 */
	private void initEdges(Matrix distances) {
		allEdges = new ArrayList<>();
		edges = new ArrayList<>();

		for(int c = 0; c < cities; c++) {
			edges.add(new ArrayList<Edge>());
		}
		
		for(int i = 0; i < cities - 1; i++) {
			for(int j = i + 1; j < cities; j++) {
				Edge edge = new Edge(i, j, distances.get(i, j), tauMax);
				
				allEdges.add(edge);
				edges.get(i).add(edge);
				edges.get(j).add(edge);
			}
		}
		
		for(int c = 0; c < cities; c++) {
			Collections.sort(edges.get(c));
		}
	}
	
	/**
	 * Pokreni algoritam
	 * @return najbolji pronadjeni mrav
	 */
	public Ant run() {
		int lastBestUpdate = 0;
		
		for(int iter = 1; iter <= maxIterations; iter++) {
			Ant iterBestAnt = null;
			lastBestUpdate++;
			
			for(int i = 0; i < ants; i++) {
				Ant ant = new Ant();
				
				int start = rand.nextInt(cities);
				int current = start;
				
				for(int step = 0; step < cities - 1; step++) {
					ant.visitedCities.add(current);
					Edge nextEdge = selectFromCandidates(current, ant);
					
					if(nextEdge == null) {
						nextEdge = selectFromAll(current, ant);
					}
					
					ant.addEdge(nextEdge);
					current = nextEdge.getDst(current);
				}
				
				for(Edge edge : edges.get(current)) {
					if(edge.getDst(current) == start) {
						ant.addEdge(edge);
						break;
					}
				}
				
				if(iterBestAnt == null || ant.compareTo(iterBestAnt) > 0) {
					iterBestAnt = ant;
					
					if(bestSoFarAnt == null || iterBestAnt.compareTo(bestSoFarAnt) > 0) {
						bestSoFarAnt = iterBestAnt;
						lastBestUpdate = 0;
						tauMax = 1.0 / (rho * bestSoFarAnt.totalDistance);
						tauMin = tauMax / a;
					}
				}
			}
			
			if(lastBestUpdate > 500) {
				for(Edge edge : allEdges) {
					edge.pheromones = tauMax;
				}
				
				lastBestUpdate = 0;
				continue;
			}
			
			evaporatePheromones();
			
			updatePheromones(iterBestAnt);
			if(iter % 10 == 0) {
				updatePheromones(bestSoFarAnt);
			}
		}
		
		return bestSoFarAnt;
	}
	
	/**
	 * Osvjezi feromonske tragove za zadanog mrava
	 * @param ant mrav
	 */
	private void updatePheromones(Ant ant) {
		for(Edge edge : ant.path) {
			edge.pheromones += (1.0 / ant.totalDistance);
			
			if(edge.pheromones > tauMax) {
				edge.pheromones = tauMax;
			}
		}
	}

	/**
	 * Izaberi iduci brid izmedju svih dostupnih bridova
	 * @param currentCity trenutni grad
	 * @param ant mrav
	 * @return sljedeci brid na putu
	 */
	private Edge selectFromAll(int currentCity, Ant ant) {
		List<Edge> possibleJumps = new ArrayList<>();
		
		for(Edge edge : edges.get(currentCity).subList(candidatesSize, cities - 1)) {
			if(!ant.visitedCities.contains(edge.getDst(currentCity))) {
				possibleJumps.add(edge);
			}
		}
		
		return select(possibleJumps);
	}

	/**
	 * Izaberi iduci brid izmedju bridova u listi kandidata
	 * @param currentCity trenutni grad
	 * @param ant mrav
	 * @return sljedeci brid na putu, null ako takav ne postoji
	 */
	private Edge selectFromCandidates(int currentCity, Ant ant) {
		List<Edge> possibleJumps = new ArrayList<>();
		
		for(Edge edge : edges.get(currentCity).subList(0, candidatesSize)) {
			if(!ant.visitedCities.contains(edge.getDst(currentCity))) {
				possibleJumps.add(edge);
			}
		}
		
		if(possibleJumps.isEmpty()) {
			return null;
		} else {
			return select(possibleJumps);
		}
	}
	
	/**
	 * Ispari feromonske tragove
	 */
	private void evaporatePheromones() {
		for(Edge edge : allEdges) {
			edge.pheromones *= (1.0 - rho);
			
			if(edge.pheromones < tauMin) {
				edge.pheromones = tauMin;
			}
		}
	}
	
	/**
	 * Izaberi brid proporcijonalnom slucajnom selekcijom
	 * @param possibleEdges lista mogucih bridova
	 * @return odabrani brid
	 */
	private Edge select(List<Edge> possibleEdges) {
		double sum = 0.0;
		
		for(Edge edge : possibleEdges) {
			edge.fitness = heuristics.get(edge.firstCity, edge.secondCity);
			edge.fitness = Math.pow(edge.pheromones, alpha);
			sum += edge.fitness;
		}
		
		double probability = rand.nextDouble();
		double offset = 0.0;
		
		for(Edge edge : possibleEdges) {
			double fitness = edge.fitness / sum;
			offset += fitness;
			
			if(offset >= probability) {
				return edge;
			}
		}
		
		return null;
	}

}
