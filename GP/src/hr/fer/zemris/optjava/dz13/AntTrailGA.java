package hr.fer.zemris.optjava.dz13;

import hr.fer.zemris.optjava.ant.AntSimulator;
import hr.fer.zemris.optjava.ant.WorldMap;
import hr.fer.zemris.optjava.ant.visual.AntWorldFrame;
import hr.fer.zemris.optjava.genetic.GP;
import hr.fer.zemris.optjava.genetic.operators.TournamentSelection;
import hr.fer.zemris.optjava.genetic.programming.AbstractGPNode;
import hr.fer.zemris.optjava.genetic.programming.AntGPSolution;
import hr.fer.zemris.optjava.genetic.programming.AntIfFoodAhead;
import hr.fer.zemris.optjava.genetic.programming.AntMove;
import hr.fer.zemris.optjava.genetic.programming.AntNProg;
import hr.fer.zemris.optjava.genetic.programming.AntPopulationCreator;
import hr.fer.zemris.optjava.genetic.programming.AntTurnLeft;
import hr.fer.zemris.optjava.genetic.programming.AntTurnRight;
import hr.fer.zemris.optjava.genetic.programming.SubtreeSwapCrossover;
import hr.fer.zemris.optjava.genetic.programming.SubtreeSwapMutation;
import hr.fer.zemris.optjava.opt.IOptAlgorithm;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.SwingUtilities;

/**
 * Progam koji pokrece  algoritam genetskog programiranja za ucenje mrava.
 * Nakon sto je ucenje gotovo pokrece se interkativni vizualizator.
 * Program prima 5 argumenta iz komandne linije:
 * 1. putanja do datoteke s mapom
 * 2. makismalni broj generacija
 * 3. velicina populacije
 * 4. minimalni fitness uz koji algoritam moze stati
 * 5. putanja do datoteke u kojoj ce se zapisati konacno rjesenje
 * @author Luka Sterbic
 * @version 0.3
 */
public class AntTrailGA {

	private static final int TREE_CREATION_MAX_DEPTH = 6;
	private static final int TREE_MAX_DEPTH = 20;
	private static final int MAX_NODES = 200;
	private static final int K_TOURNAMENT = 7;
	private static final double PM = 0.14;
	private static final double PC = 0.85;
	private static final int MAX_STEPS = 600;
	
	/**
	 * Main metoda programa
	 * @param args argumenti komandne linije
	 */
	public static void main(String[] args) {
		if(args.length != 5) {
			exitWithMsg("Krivi broj argumenata!");
		}
		
		WorldMap map = null;
		
		try {
			map = WorldMap.fromFile(args[0]);
		} catch (IOException e) {
			exitWithMsg("Dogodila se greska prilikom parsiranja mape svijeta!");
		}

		int maxGenerations = -1;
		int populationSize = -1;
		double minAllowedFitness = -1.0;
		
		try {
			maxGenerations = Integer.parseInt(args[1]);
			populationSize = Integer.parseInt(args[2]);
			minAllowedFitness = Double.parseDouble(args[3]);
		} catch(NumberFormatException ex) {
			exitWithMsg("Doslo je do greske prilikom parsiranja argumenata iz komandne linije!");
		}
		

		AbstractGPNode[] functions = new AbstractGPNode[]{
				new AntIfFoodAhead(), new AntNProg(2), new AntNProg(3)
				};
		
		AbstractGPNode[] terminals = new AbstractGPNode[]{
				new AntMove(), new AntTurnLeft(), new AntTurnRight()
				};
		
		AntPopulationCreator antCreator = new AntPopulationCreator(
				TREE_CREATION_MAX_DEPTH,
				TREE_MAX_DEPTH,
				MAX_NODES,
				functions,
				terminals
				);
		
		final AntSimulator simulator = new AntSimulator(map, MAX_STEPS);

		IOptAlgorithm<AntGPSolution> gp = new GP<>(
				simulator,
				populationSize,
				antCreator,
				new TournamentSelection<AntGPSolution>(K_TOURNAMENT),
				new SubtreeSwapCrossover(),
				new SubtreeSwapMutation(antCreator),
				minAllowedFitness,
				maxGenerations,
				PM,
				PC,
				false
				);
		
		AntGPSolution solution = gp.run();
		System.out.println("Solution found! Value: " + solution.value);
		
		simulator.setSolution(solution);
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				new AntWorldFrame(simulator).setVisible(true);
			}
			
		});
		
		try(PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(args[4])))) {	
			writer.write(solution.toString());
			writer.flush();
			writer.close();
		} catch (IOException e) {
			exitWithMsg("Dogodila se greska prilikom zapisivanja u izlaznu datoteku!");
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
