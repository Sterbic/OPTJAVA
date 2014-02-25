package hr.fer.zemris.optjava.dz11;

import hr.fer.zemris.art.GrayScaleImage;
import hr.fer.zemris.optjava.functions.ImageDistanceFunction;
import hr.fer.zemris.optjava.genetic.ParallelGA;
import hr.fer.zemris.optjava.genetic.operators.GaussAddMutation;
import hr.fer.zemris.optjava.genetic.operators.IASPopulationCreator;
import hr.fer.zemris.optjava.genetic.operators.ICrossover;
import hr.fer.zemris.optjava.genetic.operators.IMutation;
import hr.fer.zemris.optjava.genetic.operators.OnePointCrossover;
import hr.fer.zemris.optjava.genetic.operators.TournamentSelection;
import hr.fer.zemris.optjava.genetic.operators.UniformCrossover;
import hr.fer.zemris.optjava.genetic.operators.UniformSwapMutation;
import hr.fer.zemris.optjava.opt.IOptAlgorithm;
import hr.fer.zemris.optjava.opt.IntegerArraySolution;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Progam koji pokrece genetski algoritam s paralelnim stvaranjem jedinki
 * koji rjesava problem aproksimacije grayscale slike pozadinskom bojom i
 * skupom pravokutnika. Program prima 7 argumenta iz komandne linije:
 * 1. putanja do originalne PNG slike
 * 2. broj kvadrata kojima se aproksimira slika
 * 3. velicina populacije
 * 4. makismalni broj generacija
 * 5. minimalni fitness uz koji algoritam moze stati
 * 6. staza to .txt datoteke u koju ce se ispisati optimalni parametri
 * 7. staza do PNG slike koju ce algoritam generirati kao rjesenj
 * @author Luka Sterbic
 * @version 0.1
 */
public class Pokretac2 {
	
	private static final int GRAYSCALE_MIN_VALUE = 0;	
	private static final int GRAYSCALE_MAX_VALUE = 256;
	private static final int K = 3;
	private static final double SIGMA = 10.0;
	private static final double P = 0.01;
	//private static final float ALPHA = 0.25f;

	/**
	 * Main funkcija programa
	 * @param args argumenti komandne linije
	 */
	public static void main(String[] args) {
		if(args.length != 7) {
			exitWithMsg("Krivi broj argumenata!");
		}
		
		GrayScaleImage original = null;
		
		try {
			original = GrayScaleImage.load(new File(args[0]));
		} catch (IOException e) {
			exitWithMsg("Doslo je do greske prilikom ucitavanja originalne slike!");
		}
		
		int dimension = -1;
		int populationSize = -1;
		int maxGenerations = -1;
		double minAllowedFitness = -1.0;
		
		try {
			dimension = Integer.parseInt(args[1]);
			populationSize = Integer.parseInt(args[2]);
			maxGenerations = Integer.parseInt(args[3]);
			minAllowedFitness = Double.parseDouble(args[4]);
		} catch(NumberFormatException ex) {
			exitWithMsg("Doslo je do greske prilikom parsiranja argumenata iz komandne linije!");
		}
		
		ImageDistanceFunction function = new ImageDistanceFunction(original, dimension);
		
		int[] mins = new int[function.dimensions()];
		int[] maxs = new int[function.dimensions()];
		int max = Math.max(GRAYSCALE_MAX_VALUE,
				Math.max(original.getHeight(), original.getWidth()));
		
		Arrays.fill(mins, GRAYSCALE_MIN_VALUE);
		Arrays.fill(maxs, max);
		
		List<ICrossover<IntegerArraySolution>> crossovers = new ArrayList<>();
		crossovers.add(new UniformCrossover());
		crossovers.add(new OnePointCrossover());
		//crossovers.add(new ArithmeticCrossover());
		//crossovers.add(new BLXAlphaCrossover(ALPHA));
		
		List<IMutation<IntegerArraySolution>> mutations = new ArrayList<>();
		mutations.add(new GaussAddMutation(SIGMA, P));
		mutations.add(new UniformSwapMutation(GRAYSCALE_MIN_VALUE, max, P));
		
		IOptAlgorithm<IntegerArraySolution> parallelEvalGA = new ParallelGA<>(
				function,
				populationSize,
				new IASPopulationCreator(function.dimensions(), mins, maxs),
				new TournamentSelection<IntegerArraySolution>(K),
				crossovers,
				mutations,
				minAllowedFitness,
				maxGenerations
				);
		
		IntegerArraySolution solution = parallelEvalGA.run();
		
		System.out.println("Solution found! Fitness: " + solution.fitness);
		
		try(PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(args[5])))) {
			for(int value : solution.values) {
				writer.write(value + "\n");
			}
			
			writer.flush();
		} catch(IOException ex) {
			exitWithMsg("Dogodila se greska prilikom zapisivanja optimalnih parametara u datoteku!");
		}
		
		try {
			function.drawSolution(solution).save(new File(args[6]));
		} catch (IOException e) {
			exitWithMsg("Dogodila se greska prilikom zapisivanja PNG slike u datoteku!");
		}
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
