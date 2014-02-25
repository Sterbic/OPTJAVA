package hr.fer.zemris.optjava.dz8;

import hr.fer.zemris.optjava.dz8.data.Dataset;
import hr.fer.zemris.optjava.dz8.data.TimeSeries;
import hr.fer.zemris.optjava.dz8.de.DEUniCrossover;
import hr.fer.zemris.optjava.dz8.de.DifferentialEvolution;
import hr.fer.zemris.optjava.dz8.de.LinearMutantCreator;
import hr.fer.zemris.optjava.dz8.de.RandBaseSelection;
import hr.fer.zemris.optjava.dz8.function.NetworkErrorFunction;
import hr.fer.zemris.optjava.dz8.neuro.ElmanNetwork;
import hr.fer.zemris.optjava.dz8.neuro.INeuralNetwork;
import hr.fer.zemris.optjava.dz8.neuro.TDNN;
import hr.fer.zemris.optjava.dz8.opt.DoubleArraySolution;
import hr.fer.zemris.optjava.dz8.opt.IOptAlgorithm;

/**
 * Progam koji trenira umjetnu neuronsku mrezu za predvidjanje 
 * vremenske serije. Program prima 4 argumenta iz komandne linije:
 * 1. staza do datoteke s podacima vremenske serije
 * 2. oznaka neuronske mreze [tdnn-arh, elman-arh], npr. arh = 8x10x1
 * 3. velicina populacije
 * 4. prihvatljivo srednje kvadratno odstupanje
 * 5. makismalni broj iteracija
 * @author Luka Sterbic
 * @version 0.3
 */
public class ANNTrainer {
	
	private static final int N = 600;
	private static final double CR = 0.01;
	private static final int LVL = 2;
	
	/**
	 * Main funkcija programa
	 * @param args argumenti komandne linije
	 */
	public static void main(String[] args) {
		if(args.length != 5) {
			exitWithMsg("Krivi broj argumenata!");
		}
		
		TimeSeries timeSeries = null;
		try {
			timeSeries = new TimeSeries(args[0]);
		} catch(Exception ex) {
			exitWithMsg("Dogodila se greska pilikom ucitavanja vremenske serije!");
		}
		
		int populationSize = -1;
		double allowedError = -1.0;
		int maxIterations = -1;
		
		try {
			populationSize = Integer.parseInt(args[2]);
			allowedError = Double.parseDouble(args[3]);
			maxIterations = Integer.parseInt(args[4]);
		} catch(NumberFormatException ignorable) {}
		
		INeuralNetwork network = null;
		Dataset dataset = null;
		
		String[] networkType = args[1].split("-");
		int[] architecture = getArchitecture(networkType[1]);
		
		switch(networkType[0]) {
		case "tdnn":
			network = new TDNN(
					timeSeries.createTDNNDataset(architecture[0], N),
					architecture
					);
			dataset = timeSeries.createTDNNDataset(architecture[0], -1);
			break;
			
		case "elman":
			network = new ElmanNetwork(
					timeSeries.createElmanDataset(N),
					architecture
					);
			dataset = timeSeries.createElmanDataset(-1);
			break;

		default:
			exitWithMsg("Nepoznati tip mreze!");
		}
		
		IOptAlgorithm<DoubleArraySolution> de = new DifferentialEvolution(
				populationSize,
				allowedError,
				maxIterations,
				new NetworkErrorFunction(network),
				new RandBaseSelection(),
				new DEUniCrossover(CR),
				new LinearMutantCreator(LVL)
				);
		
		DoubleArraySolution solution = de.run();
		
		network.setParams(solution.values);
		System.out.println("Squared error [train]: " + network.getSquaredError());
		
		network.setDataset(dataset);
		System.out.println("Squared error [all]:" + network.getSquaredError());
	}
	
	/**
	 * Kreiraj polje arhitekture mreze na bazi zadanog stringa
	 * @param architecture string reprezentacija arhitekture mreze
	 * @return polje intova koje predstavlja arhitekturu mreze
	 */
	private static int[] getArchitecture(String architecture) {
		String[] parts = architecture.split("x");
		int[] arch = new int[parts.length];
		
		for(int i = 0; i < arch.length; i++) {
			arch[i] = Integer.parseInt(parts[i]);
		}
		
		return arch;
	}

	/**
	 * Ispisi zadanu poruku na standardni izlaz i izadji iz programa
	 * @param message poruka greske
	 */
	private static void exitWithMsg(String message) {
		System.out.println(message);
		System.exit(-1);
	}
	
}
