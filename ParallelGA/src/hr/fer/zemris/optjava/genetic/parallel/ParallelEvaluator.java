package hr.fer.zemris.optjava.genetic.parallel;

import hr.fer.zemris.optjava.functions.ITSFunction;
import hr.fer.zemris.optjava.opt.SingleObjectiveSolution;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Klasa koja implementira paralelni evaluator rjesenja genetskog algoritma
 * @author Luka Sterbic
 * @version 0.2
 * @param <T> tip jedinke
 */
public class ParallelEvaluator<T extends SingleObjectiveSolution> {

	private final ITSFunction<T> function;
	private final Queue<SingleObjectiveSolution> in;
	private final Queue<SingleObjectiveSolution> out;
	private final SingleObjectiveSolution poison;
	private Thread[] threads;
	
	/**
	 * Konstruktor za ParallelEvaluator
	 * @param function funkcija kazne
	 */
	public ParallelEvaluator(ITSFunction<T> evalFunction) {
		this.function = evalFunction;
		this.in = new ConcurrentLinkedQueue<>();
		this.out = new ConcurrentLinkedQueue<>();
		this.poison = new SingleObjectiveSolution();		
		this.threads = new Thread[Runtime.getRuntime().availableProcessors()];
		
		for(int i = 0; i < this.threads.length; i++) {
			this.threads[i] = new Thread(new Runnable() {
				
				@Override
				public void run() {
					while(true) {
						@SuppressWarnings("unchecked")
						T t = (T) in.poll();
						
						if(t == null) {
							Thread.yield();
						} else {
							if(t == poison) {
								return;
							}
							
							t.value = function.valueAt(t);
							t.fitness = -t.value;
							
							out.offer(t);
						}
					}
					
				}
				
			});
		}
		
		for(Thread thread : this.threads) {
			thread.start();
		}
	}
	
	/**
	 * Ugasi dretve evaluatora
	 */
	public void shutdown() {
		for(int i = 0; i < threads.length; i++) {
			in.add(poison);
		}
		
		for(Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {}
		}
	}
	
	/**
	 * Posalji jedinku na evaluaciju
	 * @param solution jedinka koju treba evaluirati
	 */
	public void push(T solution) {
		in.add(solution);
	}
	
	/**
	 * Dohvati evaluiranu jedinku, dretva zapinje
	 * u metodi dok jedinka ne postane dostupna.
	 * @return evaluirana jedinka
	 */
	public T pull() {
		while(true) {
			@SuppressWarnings("unchecked")
			T t = (T) out.poll();
			
			if(t == null) {
				Thread.yield();
			} else {
				return t;
			}
		}
	}
	
}
