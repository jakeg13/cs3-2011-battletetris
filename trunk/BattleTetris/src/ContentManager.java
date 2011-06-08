import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Static content manager for lazy loading of game assets (currently images only).
 * @author Alexandre Boulgakov
 */
public class ContentManager {
	/** Image cache */
	static private Image[] images;
	
	/** This class cannot be instantiated. */
	private ContentManager() { }
	
	/** Initializes caches */
	static
	{
		// Initialize image cache
		images = new Image[PowerUp.NUM_POWERUPS];
	}
	
	/**
	 * Returns the image for powerup i.
	 * This might cause the image to be read from disk if this is the first time it was requested.
	 */
	static public Image getImage(int i)
	{
		// If the image isn't in the cache, try to load it
		if (images[i - 1] == null)
		{
			try
			{
				File file = new File(PowerUp.getImageName(i));
				Image image = ImageIO.read(file);
				images[i - 1] = image;
			}
			catch (IOException _)
			{
				// Do nothing here. The image is already null, and there's nothing else we can do.
			}
		}
		return images[i - 1];
	}
}