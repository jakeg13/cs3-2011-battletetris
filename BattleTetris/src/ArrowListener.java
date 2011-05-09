import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// Used to display the contents of a game board
public interface ArrowListener
{
	public void leftPressed();
	public void rightPressed();
	public void commaPressed();
	public void upPressed();
	public void periodPressed();
	public void downStart();
	public void pPressed();
	public void spacePressed();
	public void enterPressed();
	public void qPressed();
	public void wPressed();
	public void ePressed();
	public void sStart();
	public void aPressed();
	public void dPressed();
	public void sEnd();
	public void downEnd();
}