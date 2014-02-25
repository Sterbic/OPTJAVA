package hr.fer.zemris.art;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Klasa koja predstavlja grayscale sliku. Svaki element slike opisan je
 * nijansom sive od 0 do 255.
 * @author Luka Sterbic
 * @version 0.1
 */
public class GrayScaleImage {

	private int width;
	private int height;
	private byte[] data;

	/**
	 * Konstruktor za GrayScaleImage
	 * @param width sirina slike
	 * @param height visina slike
	 */
	public GrayScaleImage(int width, int height) {
		this.width = width;
		this.height = height;
		this.data = new byte[height * width];
	}
	
	/**
	 * @return visina slike
	 */
	public int getHeight() {
		return height; 
	}
	
	/**
	 * @return sirina slike
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * @return podaci slike
	 */
	public byte[] getData() {
		return data;
	}
	
	/**
	 * Postavi cijelu sliku na zadanu boju
	 * @param color boja slike
	 */
	public void clear(byte color) {
		int index = 0;

		for(int h = 0; h < height; h++) {
			for(int w = 0; w < width; w++) {
				data[index++] = color;
			}
		}
	}
	
	/**
	 * Nacrtaj pravokutnih zadanih dimenzija i boje
	 * @param x x-koordinata gornje lijeve tocke pravokutnika
	 * @param y y-koordinata gornje lijeve tocke pravokutnika
	 * @param w sirina pravokutnika
	 * @param h visina pravokutnika
	 * @param color boja pravokutnika
	 */
	public void rectangle(int x, int y, int w, int h, byte color) {
		int xs = x;
		int xe = x + w - 1;
		int ys = y;
		int ye = y + h - 1;

		if(width <= xs || height <= ys || xe < 0 || ye < 0) {
			return;
		}
		
		if(xs < 0) {
			xs = 0;
		}
		
		if(ys < 0) {
			ys = 0;
		}
		
		if(xe >= width) {
			xe = width - 1;
		}
		
		if(ye >= height) {
			ye = height - 1;
		}
		
		for(int yl = ys; yl <= ye; yl++) {
			int index = yl * width + xs;
			
			for(int xl = xs; xl <= xe; xl++) { 
				data[index++] = color;
			}
		}
	}
	
	/**
	 * Spremi sliku na disk
	 * @param file datoteka u kojoj ce se zapisati slika
	 * @throws IOException u slucaju greske tijekom pisanja u datoteku
	 */
	public void save(File file) throws IOException {
		BufferedImage bim = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster r = bim.getRaster();
		
		int[] buf = new int[1];
		int index = 0;
		
		
		for(int h = 0; h < height; h++) {
			for(int w = 0; w < width; w++) {
				buf[0] = (int) data[index++] & 0xFF;
				r.setPixel(w, h, buf);
			}
		}
		
		try {
			ImageIO.write(bim, "png", file);
		} catch(IOException ex) {
			throw ex;
		} catch(Exception ex) {
			throw new IOException(ex);
		}
	}
	
	/**
	 * Ucitaj sliku sa diska
	 * @param file datoteka iz koje ce se ucitati slika
	 * @return grayscale slika
	 * @throws IOException u slucaju greske tijekom citanja datoteke
	 */
	public static GrayScaleImage load(File file) throws IOException {
		BufferedImage bim = ImageIO.read(file);
		
		if(bim.getType() != BufferedImage.TYPE_BYTE_GRAY) {
			throw new IOException("Slika nije grayscale.");
		}
		
		GrayScaleImage im = new GrayScaleImage(bim.getWidth(), bim.getHeight());
		
		try {
			int[] buf = new int[1];
			int index = 0;
			
			WritableRaster r = bim.getRaster();
			
			for(int h = 0; h < im.height; h++) {
				for(int w = 0; w < im.width; w++) {
					r.getPixel(w, h, buf);
					im.data[index++] = (byte) buf[0];
				}
			}
		} catch(Exception ex) {
			throw new IOException("Slika nije grayscale.");
		}
		
		return im;
	}

}
