package hr.fer.zemris.optjava.dz9;

import hr.fer.zemris.optjava.dz9.genetic.BLXAlphaCrossover;
import hr.fer.zemris.optjava.dz9.genetic.GaussAddMutation;
import hr.fer.zemris.optjava.dz9.genetic.ICrossover;
import hr.fer.zemris.optjava.dz9.genetic.NSGA;
import hr.fer.zemris.optjava.dz9.opt.MultipleObjectiveSolution;
import hr.fer.zemris.optjava.dz9.problems.MOOPProblem;
import hr.fer.zemris.optjava.dz9.problems.Problem1;
import hr.fer.zemris.optjava.dz9.problems.Problem2;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Progam koji vrsi visekriterijsku optimizaciju uporabom algoritma NSGA.
 * 1. identifikator problema {1, 2}
 * 2. velicina populacije
 * 3. prostor u kojemu se racuna funkcija djeljenja
 * {decision-space, objective-space}
 * 4. makismalni broj iteracija
 */
public class MOOP {

	private static final int P1_DIM = 4;
	private static final double P1_MIN = -5.0;
	private static final double P1_MAX = 5.0;
	private static final double blxAlpha = 0.25;
	private static final double sigma = 0.3;
	private static final double pm = 0.05;
	private static final double alpha = 2.0;
	private static final double sigmaShare = 0.05;
	
	/**
	 * Main funkcija programa
	 * @param args argumenti komandne linije
	 */
	public static void main(String[] args) {
		if(args.length != 4) {
			exitWithMsg("Krivi broj argumenata!");
		}
		
		int populationSize = Integer.parseInt(args[1]);
		int	maxIterations = Integer.parseInt(args[3]);
		
		MOOPProblem problem = null;
		
		switch(args[0]) {
		case "1":
			problem = new Problem1(P1_DIM, P1_MIN, P1_MAX);
			break;
			
		case "2":
			problem = new Problem2();
			break;

		default:
			exitWithMsg("Nepoznati optimizacijski problem!");
		}
		
		boolean decisionSpaceFS = false;
		
		switch(args[2]) {
		case "decision-space":
			decisionSpaceFS = true;
			break;
			
		case "objective-space":
			decisionSpaceFS = false;
			break;

		default:
			exitWithMsg("Nepoznata vrsta fitness sharinga!");
		}
		
		List<ICrossover<MultipleObjectiveSolution>> crossovers = new ArrayList<>();
		crossovers.add(new BLXAlphaCrossover(blxAlpha));
		
		NSGA nsga = new NSGA(
				problem,
				populationSize,
				crossovers,
				new GaussAddMutation(sigma, pm),
				maxIterations,
				decisionSpaceFS,
				alpha,
				sigmaShare
				);
		
		List<List<MultipleObjectiveSolution>> fronts = nsga.run();
		
		System.out.println("Total fronts: " + fronts.size());
		for(int i = 0; i < fronts.size(); i++) {
			System.out.println("Front[" + i + "] = " + fronts.get(i).size());
		}
		
		try {
			PrintWriter dsWriter = new PrintWriter(
					new BufferedWriter(new FileWriter("izlaz-dec.txt")));
			PrintWriter osWriter = new PrintWriter(
					new BufferedWriter(new FileWriter("izlaz-obj.txt")));
			
			for(List<MultipleObjectiveSolution> front : fronts) {
				for(MultipleObjectiveSolution solution : front) {
					solution.objectives = problem.evaluate(solution.values);
					
					dsWriter.write(Arrays.toString(solution.values));
					dsWriter.write("\n");
					
					osWriter.write(Arrays.toString(solution.objectives));
					osWriter.write("\n");
				}
			}
			
			dsWriter.flush();
			dsWriter.close();
			
			osWriter.flush();
			osWriter.close();
		} catch(IOException ex) {
			exitWithMsg("Dogodila se greska prilikom zapisivanja rezultata!");
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
