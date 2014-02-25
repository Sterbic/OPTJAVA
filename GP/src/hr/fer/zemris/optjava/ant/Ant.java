package hr.fer.zemris.optjava.ant;

/**
 * Klasa koja modelira mrava
 * @author Luka Sterbic
 * @version 0.3
 */
public class Ant {
	
	protected int x;
	protected int y;
	protected Direction direction;
	protected int score;
	
	/**
	 * Konstruktor za Ant
	 * @param x x-koordinata mrava
	 * @param y y-koordinata mrava
	 * @param direction smjer u kojemu mrav gleda
	 */
	public Ant(int x, int y, Direction direction) {
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.score = 0;
	}
	
	/**
	 * @return x-koordinata mrava
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * @return y-koordinata mrava
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * @return smjer u kojemu mrav gleda
	 */
	public Direction getDirection() {
		return direction;
	}
	
	/**
	 * @return kolicina hrane koju je mrav pojeo
	 */
	public int getScore() {
		return score;
	}
	
	/**
	 * Stvori kopiju mrava
	 * @return kopija mrava
	 */
	public Ant copy() {
		return new Ant(x, y, direction);
	}

	/**
	 * Pomakni mrava naprijed u smjeru u kojem gleda
	 */
	public void forward() {
		switch(direction) {
		case NORTH:
			y--;
			break;

		case EAST:
			x++;
			break;

		case WEST:
			x--;
			break;

		case SOUTH:
			y++;
			break;
			
		default:
			break;
		}
	}

	/**
	 * Okreni mrava za 90 stupnjeva
	 * @param right true za desno, false za lijevo
	 */
	public void turn(boolean right) {
		switch(direction) {
		case NORTH:
			direction = (right ? Direction.EAST : Direction.WEST);
			break;

		case EAST:
			direction = (right ? Direction.SOUTH : Direction.NORTH);
			break;

		case WEST:
			direction = (right ? Direction.NORTH : Direction.SOUTH);
			break;

		case SOUTH:
			direction = (right ? Direction.WEST : Direction.EAST);
			break;
			
		default:
			break;
		}
	}
	
}
