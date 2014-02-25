package hr.fer.zemris.optjava.dz8.neuro;

import hr.fer.zemris.optjava.dz8.data.Dataset;

import org.apache.commons.math3.linear.RealVector;

/**
 * Sucelje koje predstavlja umjetnu neuronsku mrezu
 * @author Luka Sterbic
 * @version 0.1
 */
public interface INeuralNetwork {

	/**
	 * @return ukupni broj parametara mreze
	 */
	public int getTotalParams();
	
	/**
	 * Propagiraj zadani ulaz kroz mrezu
	 * @param inputs ulazne vrijednosti
	 * @return vrijednosti na izlaznom sloju mreze
	 */
	public RealVector propagateForward(double[] inputs);
	
	/**
	 * Izracunaj kvadratnu pogresku na podacima za ucenje
	 * @return kvadratna pogreska
	 */
	public double getSquaredError();
	
	/**
	 * Postavi tezine mreze iz zadanog polja
	 * @param params polje parametara
	 */
	public void setParams(double[] params);
	
	/**
	 * Postavi skup podataka
	 * @param dataset skup podataka
	 */
	public void setDataset(Dataset dataset);
	
}
