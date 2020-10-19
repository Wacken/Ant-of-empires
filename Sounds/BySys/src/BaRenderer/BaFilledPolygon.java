package BaRenderer;

import java.awt.Graphics;

import BaMain.*;

/* **************************************************************************
 * See the explanations in class BaFrameBuffer
 * 
 * This class uses the java.awt.Graphics (Java Advanced Window 
 * Toolkit (AWT) to perform the real drawing (in 2D). This
 * is based on:  
 *   - class "Graphics" (parameter g)  
 *       defining the graphics context that is used in display
 *       windows that are maintained by the AWT, and into which
 *       the rendering engine (graphics card, frame buffer) 
 *       draws the virtual world
 *         HERE: the filled polygons 
 *       using routines
 *       - g.fillRect (...)
 * *************************************************************************/


public class BaFilledPolygon {
	
	// list of filled rows; for a filled polygon 
	// Variables: start and endpoints of horizontal lines (rows)
	private double [] xs, xe;
	private double [] zs, ze;
	private int frameWidth;
	private int frameHeight;

	// Constructor
	BaFilledPolygon (int w, int h) {
		frameWidth = w;
		frameHeight = h;
	}

	// Methods

	void setSize (int w, int h) {
		frameWidth = w;
		frameHeight = h;

		xs = new double[frameHeight];
		xe = new double[frameHeight];
		zs = new double[frameHeight];
		ze = new double[frameHeight];
		
	} // setSize

	void init () {
		for (int row=0; row<frameHeight; row++) {
			xs[row] = frameWidth;
			xe[row] = -1;
		}
	} // init

	void updateBoundary (double x, double y, double z) {
		if ((y >= 0) && (y < frameHeight)) {
			if (x < xs[(int)y]) {
				xs[(int)y] = x;
				zs[(int)y] = z;
			}
			if (xe[(int)y] < x) {
				xe[(int)y] = x;
				ze[(int)y] = z;
			}
		}
	} // updateBoundary

	void fillLines (BaZBuffer zbuf, Graphics g) {
		for (int yi = 0; yi < frameHeight; yi++) {
			double dz = ze[yi] - zs[yi];
			double dx = xe[yi] - xs[yi];
			double slope = dz/dx;
			for (int xi = Math.max(0,(int)xs[yi]); xi < Math.min(xe[yi],frameWidth); xi++) {
				// horizontal line (yi = constant)
				// compute z as a function of xi
				double z = pointOnLineAt ((double)xi, xs[yi], zs[yi], slope);
				drawPixel_ifCloser (xi,yi,z, zbuf, g);
			} // for xi
		} // for yi
	} // fillLines
	
	public void drawPixel_ifCloser(int xi, int yi, double z, BaZBuffer zbuf, Graphics g) {
		if(!BAG.insideRange(xi,0,zbuf.width()-1) || !BAG.insideRange(yi,0,zbuf.height()-1))
			return;
			
		if (z <= zbuf.z(xi,yi)) {
			// new z-value is closer to viewer; replace pixel in frame and in zBuffer
			g.fillRect (xi, yi, 1, 1);
			zbuf.setz(xi,yi,z);
		}
		// else:  new z-value is further away; do nothing
	} // drawPixel_ifCloser

	public static double pointOnLineAt(double x, double xs, double ys, double m) {
		double y = ys + m * (x-xs);
		return y;
	} // pointOnLineAt
} // class FilledPolygon	
