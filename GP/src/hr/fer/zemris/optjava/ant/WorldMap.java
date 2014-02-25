package hr.fer.zemris.optjava.ant;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Klasa koja predstavlja mapu svijete u kojemu se mrav krece
 * @author Luka Sterbic
 * @version 0.1
 */
public class WorldMap {
	
	private static final char FOOD_CHAR = '1';
	
	public int width;
	public int heigth;
	public boolean[][] food;
	
	/**
	 * Konstruktor za WorldMap
	 * @param width sirina svijeta
	 * @param heigth visina svijeta
	 * @param food polje pozicija, true ako je prisutna hrana
	 */
	public WorldMap(int width, int heigth, boolean[][] food) {
		this.width = width;
		this.heigth = heigth;
		this.food = food;
	}
	
	/**
	 * Dojava mapi da se mrav pomaknuo na trenutnu poziciju
	 * @param ant mrav
	 */
	public void antMoved(Ant ant) {
		ant.x = clipWidth(ant.x);
		ant.y = clipHeight(ant.y);
		
		if(food[ant.x][ant.y]) {
			food[ant.x][ant.y] = false;
			ant.score++;
		}
	}
	
	/**
	 * Provjeri da li je hrana ispred mrava
	 * @param ant mrav
	 * @return true ako je hrana ispred mrava, false inace
	 */
	public boolean isFoodAhead(Ant ant) {
		Ant copy = ant.copy();
		copy.forward();
		return food[clipWidth(copy.x)][clipHeight(copy.y)];
	}
	
	/**
	 * Stvori kopoiju svijeta
	 * @return kopija svijeta
	 */
	public WorldMap copy() {
		boolean[][] copyFood = new boolean[heigth][width];
		
		for(int i = 0; i < heigth; i++) {
			System.arraycopy(food[i], 0, copyFood[i], 0, width);
		}
		
		return new WorldMap(width, heigth, copyFood);
	}
	
	/**
	 * Korigiraj zadanu x-koordinatu s obzirom na sirinu mape
	 * @param x x-koordinata
	 * @return korigirana vrijednost x-koordinate
	 */
	private int clipWidth(int x) {
		if(x < 0) {
			return width - 1;
		} else if(x >= width) {
			return 0;
		} else {
			return x;
		}
	}
	
	/**
	 * Korigiraj zadanu y-koordinatu s obzirom na visinu mape
	 * @param y y-koordinata
	 * @return korigirana vrijednost y-koordinate
	 */
	private int clipHeight(int y) {
		if(y < 0) {
			return heigth - 1;
		} else if(y >= heigth) {
			return 0;
		} else {
			return y;
		}
	}

	/**
	 * Parsiraj konfiguracijsku datoteku i stvori mapu
	 * @param path staza do konfiguracijske datoteke
	 * @return mapa svijeta
	 * @throws IOException u slucaju greske prilikom citanja datoteke
	 */
	public static WorldMap fromFile(String path) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get(path), Charset.defaultCharset());
		
		String[] sizes = lines.get(0).split("x");

		int heigth = Integer.parseInt(sizes[0]);
		int width = Integer.parseInt(sizes[1]);
		boolean[][] food = new boolean[heigth][width];
		
		for(int i = 0; i < heigth; i++) {
			char[] chars = lines.get(i + 1).toCharArray();
			
			for(int j = 0; j < width; j++) {
				if(chars[j] == FOOD_CHAR) {
					food[i][j] = true;
				}
			}
		}
		
		return new WorldMap(width, heigth, food);
	}

}
