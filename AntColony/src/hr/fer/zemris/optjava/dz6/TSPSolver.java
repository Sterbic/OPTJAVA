package hr.fer.zemris.optjava.dz6;

import hr.fer.zemris.optjava.dz6.algebra.Matrix;
import hr.fer.zemris.optjava.dz6.ant.Ant;
import hr.fer.zemris.optjava.dz6.ant.MinMaxAntSystem;
import hr.fer.zemris.optjava.dz6.tsp.TSPParser;

/**
 * Klasa koja rjesava problem trgovackog putnika (TSP).
 * Program prima 4 argumenta iz komandne linije:
 * 1. datoteka s definicijom TSP problema
 * 2. veličina liste kandidata
 * 3. broj mrava u koloniji
 * 4. maksimlani broj iteracija
 * @author Luka Sterbic
 * @version 0.1
 */
public class TSPSolver {
	
	private static final double alpha = 1.5;
	private static final double beta = 3.5;
	private static final double a = 20.0;
	private static final double rho = 0.02;

	/**
	 * Main funkcija programa
	 * @param args argumenti komandne linije
	 */
	public static void main(String[] args) {
		if(args.length != 4) {
			exitWithMsg("Krivi broj argumenata!");
		}
		
		int candidates = -1;
		int ants = -1;
		int maxIterations = -1;
		
		try {
			candidates = Integer.parseInt(args[1]);
			ants = Integer.parseInt(args[2]);
			maxIterations = Integer.parseInt(args[3]);
		} catch(NumberFormatException ignorable) {}
		
		Matrix distances = null;
		
		try {
			distances = TSPParser.parse(args[0]);
		} catch(Exception ex) {
			exitWithMsg("Dogodila se greska prilikom ucitavanja datoteke TSP problema!");
		}
		
		MinMaxAntSystem mmas = new MinMaxAntSystem(
				candidates,
				ants,
				maxIterations,
				alpha,
				beta,
				a,
				rho,
				distances
				);
		
		Ant solution = mmas.run();
		
		System.out.println("Tour: " + solution.toString());
		System.out.println("Total distance: " + solution.getTotalDistance());
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
