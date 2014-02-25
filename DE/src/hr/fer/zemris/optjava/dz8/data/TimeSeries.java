package hr.fer.zemris.optjava.dz8.data;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Klasa koja sadrzi podatke o nekoj vremenskoj seriji
 * @author Luka Sterbic
 * @version 0.2
 */
public class TimeSeries {
	
	private double[] series;
	
	/**
	 * Konstruktor za TimeSeries
	 * @param path staza do datoteke s vremenskom serijom
	 * @throws IOException u slucaju greske prilikom ucitavanja datoteke
	 */
	public TimeSeries(String path) throws IOException {
		List<Double> values = new ArrayList<>();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new BufferedInputStream(new FileInputStream(new File(path)))));
		
		while(true) {
			String line = br.readLine();
			
			if(line == null) {
				break;
			}
			
			line = line.trim();
			
			if(line.isEmpty()) {
				continue;
			}
			
			values.add(Double.parseDouble(line));
		}
		
		br.close();
		
		normalize(values);
	}

	/**
	 * Normaliziraj vremensku seriju
	 * @param values vrijednosti vremenske serije
	 */
	private void normalize(List<Double> values) {
		series = new double[values.size()];
		
		double min = Double.POSITIVE_INFINITY;
		double max = Double.NEGATIVE_INFINITY;
		
		for(int i = 0; i < series.length; i++) {
			series[i] = values.get(i);
			
			min = Math.min(min, series[i]);
			max = Math.max(max, series[i]);
		}
		
		for(int i = 0; i < series.length; i++) {
			series[i] = 2.0 * (series[i] - min) / (max - min) - 1.0;
		}
	}
	
	/**
	 * Kreiraj skup uzorka za TDNN tip mreze
	 * @param l duljina vremenskog prozora
	 * @param n broj vremenskih uzoraka
	 * @return skup podataka za TDNN mrezu
	 */
	public Dataset createTDNNDataset(int l, int n) {
		List<DataSample> samples = new ArrayList<>();
		
		if(n == -1) {
			n = series.length;
		}
		
		for(int i = l; i < n; i++) {
			samples.add(new DataSample(
					Arrays.copyOfRange(series, i - l, i),
					new double[]{series[i]}
					));
		}
		
		return new Dataset(l, 1, samples);
	}
	
	/**
	 * Kreiraj skup uzorka za Elmanov tip mreze
	 * @param n broj vremenskih uzoraka
	 * @return skup podataka za Elmanovu mrezu
	 */
	public Dataset createElmanDataset(int n) {
		List<DataSample> samples = new ArrayList<>();
		
		if(n == -1) {
			n = series.length;
		}
		
		for(int i = 1; i < n; i++) {
			samples.add(new DataSample(
					new double[]{series[i - 1]},
					new double[]{series[i]}
					));
		}
		
		return new Dataset(1, 1, samples);
	}
	
}
