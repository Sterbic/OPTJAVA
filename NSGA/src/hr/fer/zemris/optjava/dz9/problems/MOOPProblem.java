package hr.fer.zemris.optjava.dz9.problems;

/**
 * Sucelje koje predstavlja problem visekriterijske optimizacije
 * @author Luka Sterbic
 * @version 0.1
 */
public interface MOOPProblem {
	
	/**
	 * Vrati dimenziju ulazne varijable
	 * @return dimenzija ulazne varijable
	 */
	public int dimension();
	
	/**
	 * Vrati broj ciljnih funkcija
	 * @return broj ciljnih funkcija
	 */
	public int nObjectives();
	
	/**
	 * Evaluiraj zadano rjesenje
	 * @param solution rjesenje
	 * @return vrijednosti funkcija cilja
	 */
	public double[] evaluate(double[] solution);
	
	/**
	 * Provjeri da li rjesenje zadovoljava eksplicitna
	 * ogranicenja i obavi izmjene ako je potrebno.
	 * @param solution rjesenje
	 */
	public void checkConstraints(double[] solution);
	
	/**
	 * Generiraj slucajnu tocku koja zadovoljava eksplicitna ogranicenja
	 * @return slucjana tocka
	 */
	public double[] randPoint();
	
	/**
	 * Vrati raspon varijabli u ovom problemu
	 * @return raspon varijabli
	 */
	public double[] getRanges();
	
	/**
	 * Vrati raspon funkcija cilja u ovom problemu
	 * @return raspon varijabli
	 */
	public double[] getObjectiveRanges();

}
