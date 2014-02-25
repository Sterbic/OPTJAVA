package hr.fer.zemris.optjava.rng;

import hr.fer.zemris.optjava.rng.rngimpl.RNGRandomImpl;

/**
 * Klasa koja nasljedjuje klasu Thread i implementira 
 * suclje providera gneratora slucajnih brojeva koji
 * daje pristupu privatnom generatoru.
 * @author Luka Sterbic
 * @version 0.1
 */
public class EVOThread extends Thread implements IRNGProvider {

	private IRNG rng = new RNGRandomImpl();
	
	/**
	 * Konstruktor za EVOThread
	 */
	public EVOThread() {
	}
	
	/**
	 * Konstruktor za EVOThread
	 * @param target runnable objekt koji ce dretva pokrenuti
	 */
	public EVOThread(Runnable target) {
		super(target);
	}
	
	/**
	 * Konstruktor za EVOThread
	 * @param name ime dretve
	 */
	public EVOThread(String name) {
		super(name);
	}
	
	/**
	 * Konstruktor za EVOThread
	 * @param group grupa kojoj dretva pripada
	 * @param target runnable objekt koji ce dretva pokrenuti
	 */
	public EVOThread(ThreadGroup group, Runnable target) {
		super(group, target);
	}
	
	/**
	 * Konstruktor za EVOThread
	 * @param group grupa kojoj dretva pripada
	 * @param name ime dretve
	 */
	public EVOThread(ThreadGroup group, String name) {
		super(group, name);
	}
	
	/**
	 * Konstruktor za EVOThread
	 * @param target runnable objekt koji ce dretva pokrenuti
	 * @param name ime dretve
	 */
	public EVOThread(Runnable target, String name) {
		super(target, name);
	}
	
	/**
	 * Konstruktor za EVOThread
	 * @param group grupa kojoj dretva pripada
	 * @param target runnable objekt koji ce dretva pokrenuti
	 * @param name ime dretve
	 */
	public EVOThread(ThreadGroup group, Runnable target, String name) {
		super(group, target, name);
	}
	
	/**
	 * Konstruktor za EVOThread
	 * @param group grupa kojoj dretva pripada
	 * @param target runnable objekt koji ce dretva pokrenuti
	 * @param name ime dretve
	 * @param stackSize velicina stoga
	 */
	public EVOThread(ThreadGroup group, Runnable target, String name, long stackSize) {
		super(group, target, name, stackSize);
	}
	
	@Override
	public IRNG getRNG() {
		return rng;
	}

}
