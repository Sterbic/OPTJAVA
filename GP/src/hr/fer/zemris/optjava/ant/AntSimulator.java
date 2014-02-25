package hr.fer.zemris.optjava.ant;

import hr.fer.zemris.optjava.ant.visual.IAntSimulatorListener;
import hr.fer.zemris.optjava.genetic.programming.AntGPSolution;
import hr.fer.zemris.optjava.opt.IFunction;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasa koja simulira kretanje mrava po svijetu
 * @author Luka Sterbic
 * @version 0.2
 */
public class AntSimulator implements IFunction<AntGPSolution> {
	
	private static final Direction START_DIRECTION = Direction.EAST;
	private static final int START_X = 0;
	private static final int START_Y = 0;
	
	private WorldMap map;
	private int maxSteps;
	private WorldMap activeMap;
	private Ant ant;
	private List<IAntSimulatorListener> listeners;
	private AntGPSolution solution;
	
	/**
	 * Konstruktor za AntSimulator
	 * @param map mapa svijeta
	 * @param maksimalni broj koraka
	 */
	public AntSimulator(WorldMap map, int maxSteps) {
		this.map = map;
		this.maxSteps = maxSteps;
		this.listeners = new ArrayList<>();
		reset();
	}
	
	/**
	 * @return aktivna mapa svijeta
	 */
	public WorldMap getActiveMap() {
		return activeMap;
	}
	
	/**
	 * @return trenutno stanje mrava
	 */
	public Ant getAnt() {
		return ant;
	}
	
	/**
	 * Postavi rjesenje za kretanje mrava
	 * @param solution rjesenje
	 */
	public void setSolution(AntGPSolution solution) {
		this.solution = solution;
		reset();
	}
	
	/**
	 * Simuliraj jedan korak mrava
	 */
	public void next() {
		solution.root.doAction(activeMap, ant);
		fire();
	}
	
	/**
	 * Resetiraj poziciju mrava
	 */
	public void reset() {
		activeMap = map.copy();
		ant = new Ant(START_X, START_Y, START_DIRECTION);
		fire();
	}
	
	/**
	 * Dodaj zadani listener u listu listenera
	 * @param listener listener
	 */
	public void addListener(IAntSimulatorListener listener) {
		if(!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}
	
	/**
	 * Javi svim listenerima da je doslo do promijene
	 */
	private void fire() {
		for(IAntSimulatorListener listener : listeners) {
			listener.update(this);
		}
	}

	@Override
	public double valueAt(AntGPSolution point) {
		reset();
		int usedSteps = 0;
		
		while(usedSteps <= maxSteps) {
			usedSteps += point.root.doAction(activeMap, ant);
		}
		
		return ant.score;
	}

}
