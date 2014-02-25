package hr.fer.zemris.optjava.dz7.neuro;

import hr.fer.zemris.optjava.dz7.data.IrisData;
import hr.fer.zemris.optjava.dz7.data.IrisSample;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 * Klasa koja modelira umjetnu neuronsku mrezu
 * @author Luka Sterbic
 * @version 0.3
 */
public class NeuralNetwork {
	
	private IrisData irisData;
	private int layers;
	private int totalParams;
	private RealMatrix[] thetas;
	private UnivariateFunction sigmoid;
	
	/**
	 * Konstruktor za NeuralNetwork
	 * @param irisData podaci za ucenje
	 * @param architecture arhitektura mreze (npr. 4,5,3,3)
	 */
	public NeuralNetwork(IrisData irisData, int... architecture) {
		this.irisData = irisData;
		this.layers = architecture.length;
		this.thetas = new RealMatrix[this.layers - 1];
		
		for(int i = 0; i < this.thetas.length; i++) {
			int rows = architecture[i + 1];
			int cols = architecture[i] + 1;
			
			this.thetas[i] = new Array2DRowRealMatrix(rows, cols).transpose();
			this.totalParams += rows * cols;
		}
		
		this.sigmoid = new UnivariateFunction() {
			
			@Override
			public double value(double x) {
				return 1.0 / (1.0 + Math.exp(-x));
			}
			
		};
	}
	
	/**
	 * @return ukupni broj parametara mreze
	 */
	public int getTotalParams() {
		return totalParams;
	}
	
	public RealVector propagateForward(double[] inputs) {
		RealVector one = new ArrayRealVector(new double[]{1});
		RealVector a = one.append(new ArrayRealVector(inputs));
		
		for(int i = 0; i < thetas.length; i++) {
			RealVector z = thetas[i].preMultiply(a);
			z.mapToSelf(sigmoid);
			
			if(i != thetas.length - 1) {
				a = one.append(z);
			} else {
				a = z;
			}
		}
		
		return a;
	}
	
	/**
	 * Vrati string klasifikacije za dana mjerenja
	 * @param input mjerenja irisa
	 * @return klasifikacijski string
	 */
	public String getClassification(double[] input) {
		RealVector output = propagateForward(input);
		StringBuilder sb = new StringBuilder();
		
		for(int i = 0; i < output.getDimension(); i++) {
			if(output.getEntry(i) < 0.5) {
				sb.append("0");
			} else {
				sb.append("1");
			}
		}
		
		return sb.toString();
	}
	
	/**
	 * Izracunaj kvadratnu pogresku na podacima za ucenje
	 * @return kvadratna pogreska
	 */
	public double getQuadraticError() {
		double error = 0.0;
		
		for(IrisSample sample : irisData) {
			RealVector output = propagateForward(sample.getData());
			RealVector classification = new ArrayRealVector(sample.getClassification());
			RealVector errorVector = output.subtract(classification);
			error += errorVector.dotProduct(errorVector);
		}
		
		return error / irisData.size();
	}
	
	/**
	 * Postavi tezine mreze iz zadanog polja
	 * @param params polje parametara
	 */
	public void setParams(double[] params) {
		if(params.length != totalParams) {
			throw new IllegalArgumentException("Illegal parameters size!");
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

}
