package hr.fer.zemris.optjava.functions;

import hr.fer.zemris.art.GrayScaleImage;
import hr.fer.zemris.art.ImageBufferProvider;
import hr.fer.zemris.optjava.opt.IntegerArraySolution;

/**
 * Klasa koja implementira funkciju kazne za aproksimaciju grayscale
 * slike pozadinom odredjene boje i skupom pravokutnika.
 * Implementacija je visedretveno sigurna.
 * @author Luka Sterbic
 * @version 0.2
 */
public class ImageDistanceFunction implements ITSFunction<IntegerArraySolution>{

	private final GrayScaleImage template;
	private final int dimension;
	
	/**
	 * Konstruktor za ImageDistanceFunction
	 * @param template izvorna slika
	 * @param dimension broj pravokutnika s kojim ce se aproksimirati slika
	 */
	public ImageDistanceFunction(GrayScaleImage template, int dimension) {
		this.template = template;
		this.dimension = dimension;
	}
	
	@Override
	public int dimensions() {
		return 1 + 5 * dimension;
	}

	@Override
	public double valueAt(IntegerArraySolution point) {
		byte[] tData = template.getData();
		byte[] sData = drawSolution(point).getData();

		int size = template.getHeight() * template.getWidth();
		int index = 0;
		double error = 0.0;
		
		while(index < size) {
			error += Math.abs(((int) sData[index] & 0xFF) - ((int) tData[index] & 0xFF));
			index++;
		}
		
		return error;
	}
	
	/**
	 * Nacrtaj zadano rjesenje na slikovni objekt
	 * @param solution trenutno rjesenje
	 * @return rjesenje u obliku grayscale slike
	 */
	public GrayScaleImage drawSolution(IntegerArraySolution solution) {
		GrayScaleImage image = ImageBufferProvider.getImageBuffer(
				template.getWidth(),
				template.getHeight()
				);
		
		int[] data = solution.values;
		int index = 1;
		int n = (data.length - 1) / 5; 
		
		byte bgColor = (byte) data[0];
		image.clear(bgColor);
		
		for(int i = 0; i < n; i++) {
			image.rectangle(
					data[index],
					data[index + 1],
					data[index + 2],
					data[index + 3],
					(byte) data[index + 3]		
					);
			index += 4;
		}
		
		return image;
	}

}
