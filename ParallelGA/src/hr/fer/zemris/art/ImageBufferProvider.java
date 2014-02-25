package hr.fer.zemris.art;

/**
 * Klasa koja svakoj dretvi nudi jedan slikovni buffer.
 * U svakoj aplikaciji postoji samo jedna instanca ove klase.
 * @author Luka Sterbic
 * @version 0.2
 */
public class ImageBufferProvider {
	
	private static ImageBufferProvider provider;
	
	private ThreadLocal<GrayScaleImage> threadLocal;
	
	/**
	 * Privatni konstruktor za ImageBufferProvider
	 */
	private ImageBufferProvider() {
		threadLocal = new ThreadLocal<>();
	}
	
	/**
	 * Dohvati buffer
	 * @param width sirina slike
	 * @param height visina slike
	 * @return slikovni buffer
	 */
	private GrayScaleImage getBuffer(int width, int height) {
		GrayScaleImage image = threadLocal.get();
		
		if(image == null) {
			image = new GrayScaleImage(width, height);
			threadLocal.set(image);
		}
		
		return image;
	}
	
	/**
	 * Dohvati provider
	 * @return provider
	 */
	private static ImageBufferProvider getProvider() {
		if(provider == null) {
			synchronized(ImageBufferProvider.class) {
				if(provider == null) {
					provider = new ImageBufferProvider();
				}
			}
		}
		
		return provider;
	}
	
	/**
	 * Dohvati slikovni buffer
	 * @param width sirina slike
	 * @param height visina slike
	 * @return slikovni buffer
	 */
	public static GrayScaleImage getImageBuffer(int width, int height) {
		return getProvider().getBuffer(width, height);
	}

}
