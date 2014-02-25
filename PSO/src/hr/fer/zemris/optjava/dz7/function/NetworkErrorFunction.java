package hr.fer.zemris.optjava.dz7.function;

import hr.fer.zemris.optjava.dz7.neuro.NeuralNetwork;
import hr.fer.zemris.optjava.dz7.opt.DoubleArraySolution;

/**
 * Implementacija optimizacijske funkcije kao
 * kvadratnu pogresku umjetne neuronske mreze.
 * @author Luka Sterbic
 * @version 0.1
 */
public class NetworkErrorFunction implements IFunction<DoubleArraySolution> {

	private NeuralNetwork network;
	
	/**
	 * Konstruktor za NetworkErrorFunction
	 * @param network neuronska mreza
	 */
	public NetworkErrorFunction(NeuralNetwork network) {
		this.network = network;
	}
	
	@Override
	public int dimensions() {
		return network.getTotalParams();
	}

	@Override
	public double valueAt(DoubleArraySolution point) {
		network.setParams(point.values);
		return network.getQuadraticError();
	}

}
