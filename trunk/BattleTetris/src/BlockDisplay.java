/** IBlockDisplay.java
 * @author Alexandre Boulgakov
 */
import java.awt.event.*;

import javax.swing.JPanel;


public interface BlockDisplay extends KeyListener {
	public void setArrowListener(ArrowListener listener);
	public void setLocation(int x, int y);
	public void setTitle(String title);
	public JPanel getPanel();
	public void showBlocks();
}
