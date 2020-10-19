package BaInteraction;

import java.awt.event.MouseEvent;
import BaRenderer.*;

public class BaUIMouseObjTranslator
extends BaUIMouse
{
	// Variables
	private double x0, y0;
	private double scalefactor;

	// Constructor
	BaUIMouseObjTranslator (MatMultMode mode) {
	//==================-------------------
		super();
		x0 = 0;
		y0 = 0;
		scalefactor = 50.0;
		this.mode = mode;
	} // BaUIMouseController



	//--------------------------------------------------------------------------------
	//
	// In interactive (mouse-based) object manipulation, right-multiplication 
	// corresponds to 
	// - INTRINSIC steering: each new mouse command is interpreted relative to the local 
	// coordinate system.
	// - To allow EXTRINSIC mouse control, we need a left-multiplication
	// thus: _M = m* _M
	//
	// note: this is not available in OpenGL (instead, programmers need to retrieve the
	// accumulating matrix _M from the renderer, pre-multiply it with the new transformation
	// themselves, and then send the result back to the renderer
	//--------------------------------------------------------------------------------

	// Callbacks
	
	public void mousePressed (MouseEvent e) {
		x0 = e.getX();
		y0 = e.getY();
	}; // mousePressed

	public void mouseDragged (MouseEvent e) {		
		double dx = (e.getX() - x0)/scalefactor;
		double dy = (e.getY() - y0)/scalefactor;

		if (t != null) {
			switch (mode) {
				case INTRINSIC:
					t.translate (dx,-dy,0.0);
					break;
				case EXTRINSIC:
					// get _M from the _obj
					//Transformation objtransf = _obj.pose();

					// create new transformation m with given translation parameters
					BaTransformation newtransf = new BaTransformation(); 
					newtransf.translate (dx,-dy,0.0);

					// pre-multiply: result = m * _M   (EXTRINSIC mode)
					BaTransformation resulttransf = new BaTransformation();
					//resulttransf.equals (newtransf.times (objtransf));
					resulttransf.gets (newtransf.times (t));

					// apply the result to _obj
					//_obj.setPose(resulttransf); 
					t.copy(resulttransf); 
					break;
			}
		}
		x0 = e.getX();
		y0 = e.getY();
	}; // mouseDragged
} // class BaUIMouseObjTranslator

