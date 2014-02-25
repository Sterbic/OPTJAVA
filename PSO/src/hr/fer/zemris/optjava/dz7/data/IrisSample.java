package hr.fer.zemris.optjava.dz7.data;

import java.util.Arrays;

/**
 * Klasa koja modelira jedan zapis u skupu podataka o irisu
 * @author Luka Sterbic
 * @version 0.2
 */
public class IrisSample {

	private double[] data;
	private double[] classification;
	
	/**
	 * Konstruktor za IrisSample
	 * @param data polje podataka, mjerenja
	 * @param classification polje klasifikacije
	 */
	public IrisSample(double[] data, double[] classification) {
		this.data = data;
		this.classification = classification;
	}
	
	/**
	 * @return polje podataka
	 */
	public double[] getData() {
		return data;
	}
	
	/**
	 * @return polje klasifikacije
	 */
	public double[] getClassification() {
		return classification;
	}
	
	/**
	 * @return klasifikacijski string
	 */
	public String getStringClassification() {
		StringBuilder sb = new StringBuilder();
		
		for(double cl : classification) {
			if(cl < 0.5) {
				sb.append("0");
			} else {
				sb.append("1");
			}
		}
		
		return sb.toString();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(Arrays.toString(data));
		sb.append(" : ");
		sb.append(Arrays.toString(classification));
		return sb.toString();
	}
	
}
