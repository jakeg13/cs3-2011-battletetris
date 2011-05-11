/** ArrowListener.java
 * 
 * An interface for classes that responds to keyboard presses.
 * A KeyListener (like BlockDisplay) preprocesses KeyEvents, asking any
 * ArrowListener (like Tetris and TetrisControl) to actuate them.
 * 
 * The most relevant methods are for WASD, UpDownLeftRight, and P (pause)
 * Comma, Period, Q, E, space, and enter are not relevant to Tetris/TetrisControl. (removable)
 * 
 * downStart, downEnd. sStart, and sEnd are used to determine if a key is being
 * 	held down. This is useful for when pieces should continuously move when a key is held.
 * To add more such keys, one would add XStart, XEnd, for the relevant key.
 * 	While also having any relevant KeyListener (like BlockDisplay) handle such KeyEvents.
 * 
 * @author AlexFandrianto
 *
 */

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