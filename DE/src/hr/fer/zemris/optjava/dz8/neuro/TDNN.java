package hr.fer.zemris.optjava.dz8.neuro;

import hr.fer.zemris.optjava.dz8.data.DataSample;
import hr.fer.zemris.optjava.dz8.data.Dataset;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 * Klasa koja modelira TDNN tip mreze
 * @author Luka Sterbic
 * @version 0.2
 */
public class TDNN implements INeuralNetwork {
	
	private Dataset dataset;
	private int layers;
	private int totalParams;
	private RealMatrix[] thetas;
	private UnivariateFunction tanh;
	
	/**
	 * Konstruktor za NeuralNetwork
	 * @param dataset podaci za ucenje
	 * @param architecture arhitektura mreze (npr. 4,5,3,3)
	 */
	public TDNN(Dataset dataset, int... architecture) {
		this.dataset = dataset;
		this.layers = architecture.length;
		this.thetas = new RealMatrix[this.layers - 1];
		
		for(int i = 0; i < this.thetas.length; i++) {
			int rows = architecture[i + 1];
			int cols = architecture[i] + 1;
			
			this.thetas[i] = new Array2DRowRealMatrix(rows, cols).transpose();
			this.totalParams += rows * cols;
		}
		
		this.tanh = new UnivariateFunction() {
			
			@Override
			public double value(double x) {
				return 2.0 / (1.0 + Math.exp(-x)) - 1.0;
			}
			
		};
	}
	
	@Override
	public int getTotalParams() {
		return totalParams;
	}
	
	@Override
	public RealVector propagateForward(double[] inputs) {
		RealVector one = new ArrayRealVector(new double[]{1});
		RealVector a = one.append(new ArrayRealVector(inputs));
		
		for(int i = 0; i < thetas.length; i++) {
			RealVector z = thetas[i].preMultiply(a);
			z.mapToSelf(tanh);
			
			if(i != thetas.length - 1) {
				a = one.append(z);
			} else {
				a = z;
			}
		}
		
		return a;
	}
	
	@Override
	public double getSquaredError() {
		double error = 0.0;
		
		for(DataSample sample : dataset) {
			RealVector prediction = propagateForward(sample.getData());
			RealVector output = new ArrayRealVector(sample.getOutput());
			RealVector errorVector = prediction.subtract(output);
			error += errorVector.dotProduct(errorVector);
		}
		
		return error / dataset.size();
	}
	
	@Override
	public void setParams(double[] params) {
		if(params.length != totalParams) {
			throw new IllegalArgumentException("Ilegalna velicina parametara!");
		}
		
		int offset = 0;
		
		for(RealMatrix theta : thetas) {
			for(int i = 0; i < theta.getRowDimension(); i++) {
				for(int j = 0; j < theta.getColumnDimension(); j++) {
					theta.setEntry(i, j, params[offset++]);
				}
			}
		}
	}

	@Override
	public void setDataset(Dataset dataset) {
		this.dataset = dataset;
	}

}
