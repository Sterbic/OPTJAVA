package hr.fer.zemris.optjava.dz8.data;

import java.util.Arrays;

/**
 * Klasa koja modelira jedan uzorak
 * @author Luka Sterbic
 * @version 0.1
 */
public class DataSample {

	private double[] data;
	private double[] output;
	
	/**
	 * Konstruktor za DataSample
	 * @param data polje podataka
	 * @param output polje izlaza
	 */
	public DataSample(double[] data, double[] output) {
		this.data = data;
		this.output = output;
	}
	
	/**
	 * @return polje podataka
	 */
	public double[] getData() {
		return data;
	}
	
	/**
	 * @return polje izlaza
	 */
	public double[] getOutput() {
		return output;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(Arrays.toString(data));
		sb.append(" : ");
		sb.append(Arrays.toString(output));
		return sb.toString();
	}
	
}
