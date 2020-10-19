package BaInteraction;

import java.awt.event.MouseEvent;
import BaRenderer.*;

public class BaUIMouse
{
	// Variables
	protected BaTransformation t;
	public enum MatMultMode {INTRINSIC, EXTRINSIC};
	MatMultMode mode;

	// Constructor
	BaUIMouse () {
		t = null;	
	} // UIMouse

	// Getters and Setters
	public void setPose (BaTransformation t) { this.t = t; }
	public void setMode (MatMultMode mode) { this.mode = mode; }

	// Dummy Callbacks
	public void mouseMoved (MouseEvent e) {};
	public void mouseDragged (MouseEvent e) {}; 
	public void mousePressed (MouseEvent e) {};
	public void mouseReleased (MouseEvent e) {};
	public void mouseClicked (MouseEvent e) {};
	public void mouseEntered (MouseEvent e) {};
	public void mouseExited (MouseEvent e) {};

} // BaUIMouse
