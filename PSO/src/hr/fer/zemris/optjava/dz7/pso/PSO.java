package hr.fer.zemris.optjava.dz7.pso;

import hr.fer.zemris.optjava.dz7.function.IFunction;
import hr.fer.zemris.optjava.dz7.opt.DoubleArraySolution;
import hr.fer.zemris.optjava.dz7.opt.IOptAlgorithm;

import java.util.Random;

/**
 * Implementacija algoritma roja cestica
 * @author Luka Sterbic
 * @version 0.1
 */
public class PSO implements IOptAlgorithm<DoubleArraySolution> {

	private static final double C1 = 2.0;
	private static final double C2 = 2.0;
	private static final double DELTA = 1.0;

	private int populationSize;
	private double allowedMaxValue;
	private int maxIterations;
	private IFunction<DoubleArraySolution> function;
	private int distance;
	
	private boolean global;
	private double[] vMins;
	private double[] vMaxs;
	private int dimension;
	private DoubleArraySolution globalBest;
	private double inertion;
	private double inertionStep;
	
	/**
	 * Konstruktor za PSO. globalna verzija
	 * @param populationSize velicina populacije
	 * @param allowedMaxValue maksimalna dopustena vrijednost
	 * @param maxIterations maksimalni broj iteracija
	 * @param function funkcija koja se optimira
	 * @param vMins polje minimalnih brzina
	 * @param vMaxs polje maksimalnih brzina
	 */
	public PSO(int populationSize, double allowedMaxValue,
			int maxIterations, IFunction<DoubleArraySolution> function,
			double[] vMins, double[] vMaxs) {
		this.populationSize = populationSize;
		this.allowedMaxValue = allowedMaxValue;
		this.maxIterations = maxIterations;
		this.function = function;
		this.vMins = vMins;
		this.vMaxs = vMaxs;
		
		this.distance = -1;
		this.global = true;
		this.dimension = function.dimensions();
		this.globalBest = null;
		this.inertion = 1.0;
		this.inertionStep = 1.0 / (maxIterations - 1.0);
	}
	
	/**
	 * Konstruktor za PSO, lokalna verzija
	 * @param populationSize velicina populacije
	 * @param allowedMaxValue maksimalna dopustena vrijednost
	 * @param maxIterations maksimalni broj iteracija
	 * @param function funkcija koja se optimira
	 * @param vMins polje minimalnih brzina
	 * @param vMaxs polje maksimalnih brzina
	 */
	public PSO(int populationSize, double allowedMaxValue,
			int maxIterations, IFunction<DoubleArraySolution> function,
			double[] vMins, double[] vMaxs, int distance) {
		this(populationSize, allowedMaxValue, maxIterations, function, vMins, vMaxs);
		
		this.global = false;
		this.distance = distance;
	}

	@Override
	public DoubleArraySolution run() {
		Random rand = new Random();
		
		Particle[] population = new Particle[populationSize];
		for(int i = 0; i < populationSize; i++) {
			double[] velocity = new double[dimension];
			
			for(int j = 0; j < velocity.length; j++) {
				velocity[j] = rand.nextDouble() * (vMaxs[j] - vMins[j]) + vMins[j];
			}
			
			population[i] = new Particle(
					new DoubleArraySolution(dimension, rand, DELTA),
					velocity
					);
		}
		
		for(int iter = 1; iter <= maxIterations; iter++) {
			evaluate(population);
			
			if(globalBest.value < allowedMaxValue) {
				break;
			}
			
			for(int p = 0; p < populationSize; p++) {
				double[] velocity = population[p].velocity;
				double[] position = population[p].position.values;
				double[] personalBest = population[p].personalBest.values;
				double[] lgBest = getBestPosition(p, population);
				
				for(int i = 0; i < dimension; i++) {
					velocity[i] *= inertion;
					velocity[i] += C1 * rand.nextDouble() * (personalBest[i] - position[i]);
					velocity[i] += C2 * rand.nextDouble() * (lgBest[i] - position[i]);
					
					if(velocity[i] > vMaxs[i]) {
						velocity[i] = vMaxs[i];
					} else if(velocity[i] < vMins[i]) {
						velocity[i] = vMins[i];
					}
					
					position[i] += velocity[i];
				}
			}
			
			inertion = Math.max(0.0, inertion - inertionStep);
		}
		
		return globalBest;
	}

	/**
	 * Vrati najbolju poziciju ovisno o varijanti algoritma koja se koristi
	 * @param index indeks trenutno promatrane cestice
	 * @param population populacija cestica
	 * @return polje najbolje pozicije
	 */
	private double[] getBestPosition(int index, Particle[] population) {
		if(global) {
			return globalBest.values;
		} else {
			DoubleArraySolution best = population[index].position;
			
			for(int i = 0; i < distance; i++) {
				int forward = (index + i + 1) % populationSize;
				
				if(population[forward].position.compareTo(best) > 0) {
					best = population[forward].position;
				}
				
				int backward = index - i - 1;
				if(backward < 0) {
					backward += populationSize;
				}
				
				if(population[backward].position.compareTo(best) > 0) {
					best = population[backward].position;
				}
			}
			
			return best.values;
		}
	}

	/**
	 * Evaluiraj zadanu populaciju i osvjezi najbolju poziciju ako je potrebno
	 * @param population populacija cestica
	 */
	private void evaluate(Particle[] population) {
		for(Particle particle : population) {
			particle.evaluate(function);
			
			if(globalBest == null || particle.position.compareTo(globalBest) > 0) {
				globalBest = particle.position.duplicate();
			}
		}
	}

	/**
	 * Privatna klasa koja predstavlja jednu cesticu
	 * @author Luka Sterbic
	 * @version 0.1
	 */
	private class Particle {
	
		private DoubleArraySolution position;
		private double[] velocity;
		private DoubleArraySolution personalBest;
		
		/**
		 * Konstruktor za Particle
		 * @param position trenutna pozicija
		 * @param velocity trenutna brzina
		 */
		public Particle(DoubleArraySolution position, double[] velocity) {
			this.position = position;
			this.velocity = velocity;
			this.personalBest = position;
		}

		/**
		 * Odredi dobrotu cestice s obzirom na zadanu fnkciju
		 * @param function funkcija koja se optimira
		 */
		public void evaluate(IFunction<DoubleArraySolution> function) {
			position.value = function.valueAt(position);
			position.fitness = -position.value;
			
			if(position.compareTo(personalBest) > 0) {
				personalBest = position;
			}
		}
		
	}
	
}
