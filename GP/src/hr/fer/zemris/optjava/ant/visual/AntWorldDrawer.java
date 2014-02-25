package hr.fer.zemris.optjava.ant.visual;

import hr.fer.zemris.optjava.ant.Ant;
import hr.fer.zemris.optjava.ant.AntSimulator;
import hr.fer.zemris.optjava.ant.Direction;
import hr.fer.zemris.optjava.ant.WorldMap;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;

import javax.swing.JComponent;

/**
 * Klasa koja je zaduzena za iscrtavanja svijeta
 * @author Luka Sterbic
 * @version 0.4
 */
public class AntWorldDrawer extends JComponent implements IAntSimulatorListener {

	private static final long serialVersionUID = -1340544296908286631L;
	
	private static final int PIXEL_PER_TILE = 20;
	private static final int MARGIN = PIXEL_PER_TILE / 8;

	private static final Color EMPTY_TILE_COLOR = Color.BLUE;
	private static final Color FOOD_TILE_COLOR = Color.YELLOW;
	private static final Color ANT_COLOR = Color.RED;
	
	private WorldMap map;
	private Ant ant;
	
	/**
	 * Konstruktor za AntWorldDrawer
	 * @param simulator simulator kretanja mrava
	 */
	public AntWorldDrawer(AntSimulator simulator) {
		this.map = simulator.getActiveMap().copy();
		this.ant = simulator.getAnt().copy();
		
		simulator.reset();
		simulator.addListener(this);
	}

	@Override
	public Dimension getPreferredSize() {
		int width = map.width * (PIXEL_PER_TILE + MARGIN) + MARGIN;
		int height = map.heigth * (PIXEL_PER_TILE + MARGIN) + MARGIN;
		return new Dimension(width, height);
	}
	
	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		
		RenderingHints qualityHints = new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON 
				);
		qualityHints.put(
				  RenderingHints.KEY_RENDERING,
				  RenderingHints.VALUE_RENDER_QUALITY
				  );
		g2.setRenderingHints(qualityHints);
		
		int x = MARGIN;
		int y = MARGIN;
		
		for(int i = 0; i < map.heigth; i++) {
			for(int j = 0; j < map.width; j++) {
				g2.setColor(EMPTY_TILE_COLOR);
				
				if(map.food[i][j]) {
					g2.setColor(FOOD_TILE_COLOR);
				}
				
				g2.fillRect(x, y, PIXEL_PER_TILE, PIXEL_PER_TILE);
				
				if(i == ant.getX() && j == ant.getY()) {
					g2.setColor(ANT_COLOR);
					
					Polygon dirTriangle = getDirectionTriangle(ant.getDirection());
					dirTriangle.translate(x, y);
					
					g2.fillPolygon(dirTriangle);
				}
				
				x += PIXEL_PER_TILE + MARGIN;
			}
			
			x = MARGIN;
			y += PIXEL_PER_TILE + MARGIN;
		}
	}
	
	/**
	 * Stvori trokut s obzirom na zadani smjer
	 * @param direction smjer mrava
	 * @return trokut smjera
	 */
	private Polygon getDirectionTriangle(Direction direction) {
		Polygon triangle = new Polygon();
		
		switch(direction) {
		case NORTH:
			triangle.addPoint(0, 0);
			triangle.addPoint(PIXEL_PER_TILE, 0);
			triangle.addPoint(PIXEL_PER_TILE / 2, PIXEL_PER_TILE);
			break;
			
		case EAST:
			triangle.addPoint(0, 0);
			triangle.addPoint(0, PIXEL_PER_TILE);
			triangle.addPoint(PIXEL_PER_TILE, PIXEL_PER_TILE / 2);
			break;
			
		case WEST:
			triangle.addPoint(0, PIXEL_PER_TILE / 2);
			triangle.addPoint(PIXEL_PER_TILE, 0);
			triangle.addPoint(PIXEL_PER_TILE, PIXEL_PER_TILE);
			break;
			
		case SOUTH:
			triangle.addPoint(PIXEL_PER_TILE / 2, 0);
			triangle.addPoint(0, PIXEL_PER_TILE);
			triangle.addPoint(PIXEL_PER_TILE, PIXEL_PER_TILE);
			break;

		default:
			break;
		}
		
		return triangle;
	}

	@Override
	public void update(AntSimulator simulator) {
		map = simulator.getActiveMap().copy();
		ant = simulator.getAnt().copy();
		this.repaint();
	}

}
