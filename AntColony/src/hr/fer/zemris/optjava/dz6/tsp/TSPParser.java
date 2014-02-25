package hr.fer.zemris.optjava.dz6.tsp;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import hr.fer.zemris.optjava.dz6.algebra.Matrix;

/**
 * Klasa koja sadrzi staticku metodu za persiranje
 * datoteke s konfiguracijom TSP problema
 * @author Luka Sterbic
 * @version 0.1
 */
public class TSPParser {
	
	/**
	 * Ucitaj matricu udaljenosti iz zadane konfiguracijske datoteke
	 * @param path staza do datoteke s definicijom problema
	 * @return matica udaljenosti
	 * @throws IOException u slucaju I/O greske
	 */
	public static Matrix parse(String path) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get(path), Charset.defaultCharset());
		
		int dimension = -1;
		int nodesStartIndex = -1;
		int edgeMatrixIndex = -1;
		
		for(int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);
			
			if(line.startsWith("DIMENSION")) {
				dimension = Integer.parseInt(line.split(":")[1].trim());
			} else if(line.startsWith("NODE_COORD_SECTION")) {
				nodesStartIndex = i + 1;
				break;
			} else if(line.startsWith("EDGE_WEIGHT_SECTION")) {
				edgeMatrixIndex = i + 1;
			}
		}
		
		if(dimension != -1) {
			if(edgeMatrixIndex != -1) {
				return Matrix.load(lines.subList(edgeMatrixIndex, edgeMatrixIndex + dimension));
			} else if(nodesStartIndex != -1) {
				double[][] coords = new double[dimension][2];
				
				for(int i = 0; i < dimension; i++) {
					String[] parts = lines.get(nodesStartIndex + i).split("\\s+");

					coords[i][0] = Double.parseDouble(parts[1]);
					coords[i][1] = Double.parseDouble(parts[2]);
				}
				
				Matrix distances = new Matrix(dimension, dimension);
				
				for(int i = 0; i < dimension - 1; i++) {
					for(int j = i + 1; j < dimension; j++) {
						double xDiff = Math.pow(coords[i][0] - coords[j][0], 2);
						double yDiff = Math.pow(coords[i][1] - coords[j][1], 2);
						double distance = Math.sqrt(xDiff + yDiff);
						
						distances.set(i, j, distance);
						distances.set(j, i, distance);
					}
				}
				
				return distances;
			} 
		}
		
		return null;
	}

}
