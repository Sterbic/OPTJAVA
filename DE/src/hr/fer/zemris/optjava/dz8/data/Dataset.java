package hr.fer.zemris.optjava.dz8.data;

import java.util.Iterator;
import java.util.List;

/**
 * Klasa koja sadrzi formatirane uzorke za TDNN mrezu
 * @author Luka Sterbic
 * @version 0.2
 */
public class Dataset implements Iterable<DataSample> {
	
	private int inputSize;
	private int outputSize;
	private List<DataSample> samples;
	
	/**
	 * Konstruktor za Dataset
	 * @param inputSize dimenzija ulaza
	 * @param outputSize dimenzija ilzaza
	 * @param samples lista uzorka
	 */
	public Dataset(int inputSize, int outputSize, List<DataSample> samples) {
		this.inputSize = inputSize;
		this.outputSize = outputSize;
		this.samples = samples;
		
		for(DataSample sample : samples) {
			if(sample.getData().length != inputSize) {
				throw new IllegalArgumentException("Ilegalna velicina ulaza!");
			} else if(sample.getOutput().length != outputSize) {
				throw new IllegalArgumentException("Ilegalna velicina izlaza!");
			}
		}
	}

	/**
	 * Vrati broj spremljenih uzorka
	 * @return broj uzorka
	 */
	public int size() {
		return samples.size();
	}
	
	/**
	 * @return vrati dimenziju ulaza
	 */
	public int getInputSize() {
		return inputSize;
	}
	
	/**
	 * @return vrati dimenziju izlaza
	 */
	public int getOutputSize() {
		return outputSize;
	}

	@Override
	public Iterator<DataSample> iterator() {
		return samples.iterator();
	}

}
