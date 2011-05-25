/** IBlockDisplay.java
 * @author Alexandre Boulgakov
 */
import java.awt.event.*;

public interface IBlockDisplay extends KeyListener {
	public void setArrowListener(ArrowListener listener);
	public void setLocation(int x, int y);
	public void setTitle(String title);
	public void showBlocks();
}
