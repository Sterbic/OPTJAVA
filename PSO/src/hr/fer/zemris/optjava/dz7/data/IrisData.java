package hr.fer.zemris.optjava.dz7.data;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Klasa koja sakuplja vise iris uzorka
 * @author Luka Sterbic
 * @version 0.1
 */
public class IrisData implements Iterable<IrisSample> {
	
	private List<IrisSample> samples;;
	
	/**
	 * Konstruktor za IrisData
	 * @param path staza do datoteke s podacima
	 * @throws IOException u slucaju I/O greske
	 */
	public IrisData(String path) throws IOException {
		this.samples = new ArrayList<>();
		
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
			
			String[] parts = line.split(":");
			String[] dataParts = parts[0].substring(1, parts[0].length() - 1).split(",");
			String[] classParts = parts[1].substring(1, parts[1].length() - 1).split(",");
			
			double[] data = doubleArrayFromStrings(dataParts);
			double[] classification = doubleArrayFromStrings(classParts);
			
			this.samples.add(new IrisSample(data, classification));
		}
		
		br.close();
	}
	
	/**
	 * Vrati broj spremljenih uzorka
	 * @return broj uzorka
	 */
	public int size() {
		return samples.size();
	}
	
	@Override
	public Iterator<IrisSample> iterator() {
		return samples.iterator();
	}
	
	/**
	 * Stvori polje doubleova iz polja stringova
	 * @param strings polje stringova
	 * @return polje doubleova
	 */
	private double[] doubleArrayFromStrings(String[] strings) {
		double[] array = new double[strings.length];
		
		for(int i = 0; i < strings.length; i++) {
			array[i] = Double.parseDouble(strings[i]);
		}
		
		return array;
	}
	
}
