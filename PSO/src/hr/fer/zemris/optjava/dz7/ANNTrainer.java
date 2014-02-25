package hr.fer.zemris.optjava.dz7;

import hr.fer.zemris.optjava.dz7.ais.ClonAlg;
import hr.fer.zemris.optjava.dz7.data.IrisData;
import hr.fer.zemris.optjava.dz7.data.IrisSample;
import hr.fer.zemris.optjava.dz7.function.IFunction;
import hr.fer.zemris.optjava.dz7.function.NetworkErrorFunction;
import hr.fer.zemris.optjava.dz7.neuro.NeuralNetwork;
import hr.fer.zemris.optjava.dz7.opt.DoubleArraySolution;
import hr.fer.zemris.optjava.dz7.opt.IOptAlgorithm;
import hr.fer.zemris.optjava.dz7.pso.PSO;

import java.util.Arrays;

/**
 * Progam koji trenira umjetnu neuronsku mrezu za klasifikaciju irisa.
 * Program prima 5 argumenta iz komandne linije:
 * 1. staza do datoteke sa skupom uzoraka o irisima
 * 2. identifikator optimizacijskog algoritma [pso-a, pso-b-N, clonalg], N > 0
 * 3. velicina populacije
 * 4. prihvatljivo srednje kvadratno odstupanje
 * 5. makismalni broj iteracija
 * Napomena: preporucena velicina populacije za ClonAlg je 50,
 * brzina jako opada s velikim populacijama.
 * @author Luka Sterbic
 * @version 0.4
 */
public class ANNTrainer {
	
	private static final double BETA = 5.0;
	private static final double V_MIN = -1.0;
	private static final double V_MAX = 1.0;

	/**
	 * Main funkcija programa
	 * @param args argumenti komandne linije
	 */
	public static void main(String[] args) {
		if(args.length != 5) {
			exitWithMsg("Krivi broj argumenata!");
		}
		
		IrisData irisData = null;
		try {
			irisData = new IrisData(args[0]);
		} catch(Exception ex) {
			exitWithMsg("Dogodila se greska prilikom citanja datoteke s uzorcima!");
		}
		
		String algorithm = args[1];
		int populationSize = -1;
		double allowedError = -1.0;
		int maxIterations = -1;
		int distance = -1;
		
		try {
			populationSize = Integer.parseInt(args[2]);
			allowedError = Double.parseDouble(args[3]);
			maxIterations = Integer.parseInt(args[4]);
			
			if(algorithm.startsWith("pso-b-")) {
				distance = Integer.parseInt(algorithm.substring(6));
				algorithm = algorithm.substring(0, 5);
			}
		} catch(Exception ignorable) {}
		
		NeuralNetwork network = new NeuralNetwork(irisData, 4, 5, 3, 3);
		IFunction<DoubleArraySolution> function = new NetworkErrorFunction(network);
		
		double[] vMins = new double[function.dimensions()];
		double[] vMaxs = new double[function.dimensions()];
		
		Arrays.fill(vMins, V_MIN);
		Arrays.fill(vMaxs, V_MAX);
		
		IOptAlgorithm<DoubleArraySolution> optAlgorithm = null;
		
		switch(algorithm) {
		case "pso-a":
			optAlgorithm = new PSO(
					populationSize,
					allowedError,
					maxIterations,
					function,
					vMins,
					vMaxs
					);
			break;
			
		case "pso-b":
			optAlgorithm = new PSO(
					populationSize,
					allowedError,
					maxIterations,
					function,
					vMins,
					vMaxs,
					distance
					);
			break;
			
		case "clonalg":
			optAlgorithm = new ClonAlg(
					populationSize,
					populationSize / 10,
					BETA,
					allowedError,
					maxIterations,
					function
					);
			break;

		default:
			exitWithMsg("Nepoznati identifikator algoritma!");
		}
		
		DoubleArraySolution solution = optAlgorithm.run();
		network.setParams(solution.values);

		int correct = 0;
		int samples = 0;
		
		for(IrisSample sample : irisData) {
			String nClass = network.getClassification(sample.getData());
			String dClass = sample.getStringClassification();
			
			if(nClass.equals(dClass)) {
				correct++;
			}
			
			StringBuilder sb = new StringBuilder();
			
			sb.append(++samples).append(". sample: ");
			sb.append(Arrays.toString(sample.getData()));
			sb.append(", Network classification: [").append(nClass);
			sb.append("], Real classification: [").append(dClass).append("]");
			
			System.out.println(sb.toString());
		}
		
		double p = correct / (double) samples;
		String cPercentage = String.format("[%5.2f%%]", p * 100);
		String nPercentage = String.format("[%5.2f%%]", (1.0 - p) * 100);

		System.out.println("Right classified samples: " +
				String.format("%3d", correct) + " " + cPercentage);
		System.out.println("Wrong classified samples: " +
				String.format("%3d",samples - correct) + " " + nPercentage);
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
