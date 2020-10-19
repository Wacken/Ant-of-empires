package BaInteraction;

import java.awt.event.MouseEvent;
import BaMath.*;
import BaRenderer.*;
import BaMain.*;

public class BaUIMouseObjRotatorGauss
	extends BaUIMouse
{
	// Variables
	private double      ctrX, ctrY;
	private int         winHeight, winWidth;
	private double      radius;
	private BaVectorH3D v0;

	// Constructor	
	BaUIMouseObjRotatorGauss (MatMultMode mode, int w, int h) {
		super();
		v0 = new BaVectorH3D ();
		// define a circle around window center of "reasonable size"
		// ...going nearly to the edges of the window, but not quite...
		ctrX      = w / 2.0;
		ctrY      = h / 2.0;
		winHeight = h;
		winWidth  = w;
		
		double winsquare = Math.min (ctrX,ctrY);
		radius = winsquare * 0.75;  
		
		this.mode = mode;
	} // BaUIMouseController

	
	
	//--------------------------------------------------------------------------------
	//
	// In interactive (mouse-based) object manipulation, right-multiplication 
	// corresponds to 
	// - INTRINSIC steering: each new mouse command is interpreted relative to the 
	// local coordinate system.
	// - To allow EXTRINSIC mouse control, we need a left-multiplication
	// thus: _M = m* _M
	//
	// This is not available in OpenGL (instead, programmers need to retrieve the
	// accumulating matrix _M from the renderer, pre-multiply it with the new 
	// transformation themselves, and then send the result back to the renderer
	//--------------------------------------------------------------------------------

	// Callbacks
	
	public void mouseDragged (MouseEvent e) {
		BaVectorH3D v1 = new BaVectorH3D();
		calculate3DVectorFromGaussianImage (e.getX(), e.getY(), v1);
		
		double rotangle = v0.dot(v1);
		BaVectorH3D rotaxis = v0.cross(v1);
		rotaxis.normalize();
		
		if (t != null) {
			switch (mode) {
				case INTRINSIC:
					t.rotate (rotangle/90.0, rotaxis.x(),rotaxis.y(),rotaxis.z());
					break;
				case EXTRINSIC:
					// create new transformation m with given rotation parameters
					BaTransformation newtransf = new BaTransformation(); 
					newtransf.rotate (rotangle/90.0, rotaxis.x(),rotaxis.y(),rotaxis.z());

					// pre-multiply: result = m * _M   (EXTRINSIC mode)
					BaTransformation resulttransf = new BaTransformation();
					resulttransf.gets (newtransf.times (t));

					// apply the result to _obj
					t.copy(resulttransf);	
					break;
			}
		}
		v0.copy(v1);
	}; 
	
	public void mousePressed (MouseEvent e) {
		calculate3DVectorFromGaussianImage (e.getX(), e.getY(), v0);
	};
	
	private void calculate3DVectorFromGaussianImage 
	    (int mouseX, int mouseY, BaVectorH3D vec) {
		// Interpret mouse position (x,y) inside a circle (around (_ctrX,_ctrY) size _radius)
		// as the projection of a 3D unit vector onto a plane.
		// This means: the top half of a virtual unit sphere exists above the plane,
		// viewed downwards along the z-axis.
		//
		// The mouse position describes a unit vector from the center (_ctrX,_ctrY,0) 
		// to position (x,y,z) on the unit sphere, projected down onto the plane.
		
		// The heading of the unit vector (Def: orientation within the xy-plane, rotated around z)
		// can be calculated from the orientation of the projected 2D line on the plane
		double dx = mouseX - ctrX;
		double dy = winHeight - 1 - mouseY - ctrY;
		double len = Math.min(radius, Math.sqrt (dx*dx + dy*dy)); 
		double heading = Math.atan2(dy, dx);
		
		// The pitch of the unit vector (Def: orientation within the xz-plane, rotated around y)
		// is the downward projection of the unit vector along z.
		// It can be calculated from the length of the 2D line on the plane (down-projected along z)
		double pitch = Math.acos(len/radius);
		
		// Since the 3D unit vector has length 1 (hypotenuse of a right-angled triangle),
		// z is the sine of the pitch angle (opposite leg, Gegenkathete)
		double z = Math.sin(pitch);
		// The adjacent leg of the triangle (Ankathete) (cosine of the pitch angle) determines
		// a scale factor by which the down-projected 2D vector on the plane has to be enlarged
		// to become a unit vector within the plane
		double scalefactor = Math.cos(pitch);
		
		// Within the plane, the heading angle of the down-projected vector 
		// (scaled up to unit length) determines the x and y coordinates of the vector.
		double x = Math.cos(heading)*scalefactor; 
		double y = Math.sin(heading)*scalefactor;
	
		vec.set(x,y,z);
		vec.normalize();
	}

} // class UIMouseController
