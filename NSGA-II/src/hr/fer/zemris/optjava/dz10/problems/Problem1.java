package hr.fer.zemris.optjava.dz10.problems;

import java.util.Arrays;
import java.util.Random;

/**
 * Implementacija prvog problema visekriterijske optimizacije
 * @author Luka Sterbic
 * @version 0.3
 */
public class Problem1 implements MOOPProblem {
	
	private int dimension;
	private double min;
	private double max;
	private Random rand;

	/**
	 * Konstruktor za Problem1
	 * @param dimension dimenzija ulazne varijable
	 * @param min eksplicitno ogranicenje, minimum
	 * @param max eksplicitno ogranicenje, maksimum
	 */
	public Problem1(int dimension, double min, double max) {
		this.dimension = dimension;
		this.min = min;
		this.max = max;
		this.rand = new Random();
	}

	@Override
	public int dimension() {
		return dimension;
	}

	@Override
	public int nObjectives() {
		return dimension;
	}

	@Override
	public double[] evaluate(double[] solution) {
		double[] result = new double[dimension];
		
		for(int i = 0; i < dimension; i++) {
			result[i] = Math.pow(solution[i], 2);
		}
		
		return result;
	}

	@Override
	public void checkConstraints(double[] solution) {
		for(int i = 0; i < dimension; i++) {
			if(solution[i] > max) {
				solution[i] = max;
			} else if(solution[i] < min) {
				solution[i] = min;
			}
		}
	}

	@Override
	public double[] randPoint() {
		double[] point = new double[dimension];
		
		for(int i = 0; i < dimension; i++) {
			point[i] = (max - min) * rand.nextDouble() + min;
		}
		
		return point;
	}

	@Override
	public double[] getRanges() {
		double[] ranges = new double[dimension];
		Arrays.fill(ranges, max - min);
		return ranges;
	}

	@Override
	public double[] getObjectiveRanges() {
		double[] ranges = new double[dimension];
		Arrays.fill(ranges, Math.max(min * min, max * max));
		return ranges;
	}

}
