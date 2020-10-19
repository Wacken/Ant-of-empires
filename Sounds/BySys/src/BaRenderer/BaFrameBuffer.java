package BaRenderer;

import java.awt.Graphics;
import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.RepaintManager;

import BaMain.*;

/* **************************************************************************
 * This class "BaFrameBuffer" provides pixel-wise drawing operations in a 
 * frame buffer. The main task is to draw a line from a start vertex 
 * (x0,y0,z0) to an end vertex (x1,y1,z1) while considering 
 * - z-Buffering
 * - polygon filling and
 * - double buffering
 *
 * Unlike a real frame buffer, this SW-emulation adapts to the window size.
 * I.e: it does not use a fixed, very large rectangular array of pixels
 * like an HW-framebuffer, implemented on a real graphics card.
 * Instead, it adapts to the size of the associated window of the AWT 
 * toolkit.
 * 
 * The method "drawLine" draws a straight line from point (x0,y0,z0) to
 * point (x1,y1,z1). The drawing itself (in 2D) is delegated to the AWT 
 * toolkit.
 * 
 * DRAWING IN 3D, BASED ON A Z-BUFFER:
 * This framebuffer deals with 3D occlusions when one objects is partially
 * or completely in front of another object. To this end, the framebuffer 
 * contains a zBuffer. The zBuffer has the same size as the window. For 
 * every pixel (x,y), it stores the z-value of the 3D object point that 
 * is rendered at this position. If, during the rendering process,
 * a point of another object is to be drawn at the same pixel position,
 * the z-value of the new object point is compared to the stored one. 
 * Only if the new object point is closer to the camera will the pixel 
 * get the color of the new object point, thereby overwriting the 
 * previously drawn object. Thus, at the end of the rendering process, 
 * only the closest objects are visible. Objects further in the background 
 * are occluded. (See also class BaZBuffer).
 * 
 * DRAWING FILLED POLYGONS:
 * In order to fill the inside of a polygon, all pixels inside the polygon
 * need to be drawn. To this end, the frameBuffer uses the class 
 * "BaFilledPolygon" to store all points that lie on the boundary of the
 * polygon, grouped in points on the left and on the right side of the
 * polygon. It then draws horizontal lines from the left boundary side
 * to the right side of the boundary, checking in the process for
 * zBuffering.
 * NOTE: this method assumes that polygons are convex!
 * 
 * DOUBLE BUFFERING:
 * If a renderer works with a single framebuffer, the images blink!
 * This is because, whenever the rendered image is finished, the renderer
 * automatically restarts. For this, it re-initializes the image - and
 * typically starts by clearing the screen (drawing it in some background
 * color) - with the result that users hardly ever see the full image.
 * To avoid this, framebuffers provide doublebuffering: They work with a
 * forebuffer and a backbuffer. The current image is shown in the forebuffer
 * while the next image is rendered in the backbuffer. When the rendering
 * process is finished, the backbuffer becomes the forebuffer - and is
 * shown on the screen, while the next image is rendered in the back.
 * In principle, this is done using a second Image variable, "bufImage".
 * In the AWT Toolkit and SWING, doublebuffering is provided via the 
 * RepaintManager. It only needs to be turned on (see method "mypaint").
 * 
 * LINK TO THE EXTERNAL java.awt.graphics PACKAGE:
 * This class uses the external Java Advanced Window Toolkit (AWT) to
 * handle window management on the computer display screen.
 * (See also package BaWindow and, in particular, class BaWindowFrame).
 * The BaSys system uses the AWT to create a window frame (with a canvas
 * inside, within which the virtual world is rendered). The AWT window
 * frame provides a graphics context gContext which, in turn, is delivered
 * to the BaFrameBuffer.
 * BaFrameBuffer sends the following commands to the AWT window frame
 * to draw lines on the display screen: 
 *  - gContext.setColor(c)
 *  - gContext.fillRect(0, 0, width, height);	
 *  - gContext.drawString (str, x, y);
 *  - gContext.drawLine(x0,y0, x1,y1);	
 *  
 * HANDLING CALLBACKS:
 * The framebuffer BaFrameBuffer, in combination with the window 
 * (BaWindowFrame and BaCanvas based on the AWT package) is the linkage point 
 * between the main thread of the Game Engine (main loop) and a second thread
 * (within the AWT package).
 * 
 * On the one hand, the main loop runs the virtual simulation part; on the
 * other hand, user input via mouse and keyboard must be collected and integrated
 * into the system. Since user input can occur anytime, and since it should
 * be handled without delay (with minimal lag), it is generally handled in
 * a separate thread. This thread continuously watches out for user input.
 * 
 * When new input is available, it provides it to the rendering/game engine
 * via well-defined communication channels, called "callbacks". Most 
 * importantly, the BaWindow input thread issues a callback whenever the 
 * window needs to be redrawn. 
 * To this end, the BaFrameBuffer provides the callback method "mypaint".
 * Calling sequence:
 *   AWT calls
 *     -> BaCanvas.paintComponent(Graphics g) calls
 *        -> BaRenderingEngine.mypaint(Graphics g) calls
 *           -> BaGraphicsCard.mypaint (Graphics g) calls
 *              -> BaFrameBuffer.mypaint (Graphics g)
 *                    -> switches buffers (double buffering)
 *                    -> reinitializes the frame buffer (orange background)
 *                    -> restarts the rendering process:
 *                          BaMain.vWorld.render (BaMain.rEng);
 *
 * *************************************************************************/

public class BaFrameBuffer
	extends JComponent
{
	// Variables
	private BaZBuffer					zBuffer;	
	private BaFilledPolygon				filledPolygon; 
	
	// double buffering
	private RepaintManager				myRepaintManager;
	private Graphics					gContext;
//	private Image						bufImage;
//	private Graphics					bufG;		
	
	private static final long	serialVersionUID= 0L;

	// Constructor
	public BaFrameBuffer(int w, int h) {
		super ();				
		zBuffer							= new BaZBuffer (w,h);
		filledPolygon					= new BaFilledPolygon(w,h);
		
		// for doublebuffering (this is done automatically by swing JFRAME)
		myRepaintManager				= new RepaintManager();
		RepaintManager.setCurrentManager(myRepaintManager);
		gContext						= null;
//		bufImage						= null;
//		bufG							= null;		
	} // BaFrameBuffer

	
	void init (int w, int h) {
		zBuffer.setSize(w,h);
		filledPolygon.setSize(w,h);

		zBuffer.initZ();
		filledPolygon.init();
	}

	// Getters and Setters
		
	private Graphics g() {
//		if (glob.u.doubleBuffered)
//			return bufG;
//		// else
		return gContext;
	} // method g

	void setColor(Color c) { gContext.setColor(c); }
	void setGraphics(Graphics g) { gContext = g; }
	
	// callbacks
	
	void mypaint(Graphics g) {
		if (BAG.initialized) {			
			myRepaintManager.setDoubleBufferingEnabled(BAG.doubleBuffered);

			// clear the screen
			g().setColor(Color.orange);
			g().fillRect(0, 0, zBuffer.width(), zBuffer.height());		
			BaMain.vWorld.render (BaMain.rEng);		

//			if (global.u.doubleBuffered) {
//				g.drawImage(bufImage, 0, 0, this);		
//			}
		}
	} // mypaint
			
	public synchronized void notifyResize(int w, int h) 
	{
		// do nothing
	}

	// line drawing routines
	void drawString(String str, double x, double y) {
		g().drawString (str, (int)x, (int) y);		
	}
	
	void drawLine(double x0, double y0, double z0, double x1, double y1, double z1, boolean filled) {
		if (!BAG.zBuffering) {
			// draw in 2D
			g().drawLine((int)x0, (int)y0, (int)x1, (int)y1);
		}
		else { // draw in 3D (using zBuffering)
			double dx = x1-x0;
			double dy = y1-y0;
			double dz = z1-z0;

			double xs=x0, ys=y0, zs=z0;
			if (Math.abs(dx) > Math.abs(dy)) {
				// line with small slope: flat line
				if (x0 > x1) 
				{xs = x1; ys = y1; zs = z1;}
			
				for (double x=xs; x<xs+Math.abs(dx); x++) {				
					double y = BaFilledPolygon.pointOnLineAt (x, xs, ys, dy/dx);	
					double z = BaFilledPolygon.pointOnLineAt (x, xs, zs, dz/dx);
					if (filled)
						filledPolygon.updateBoundary(x, y, z);
					filledPolygon.drawPixel_ifCloser ((int) x, (int) y, z, zBuffer, g());			
				} // for x
			} // flat line
			else {
				// line with large slope: steep line
				if (y0 > y1) 
					{xs = x1; ys = y1; zs = z1;}

				if (dy != 0) {
					for (double y=ys; y<ys+Math.abs(dy); y++) {
						double x = BaFilledPolygon.pointOnLineAt (y, ys, xs, dx/dy); 
						double z = BaFilledPolygon.pointOnLineAt (y, ys, zs, dz/dy);
						if (filled)
							filledPolygon.updateBoundary(x, y, z);
						filledPolygon.drawPixel_ifCloser ((int)x, (int)y, z, zBuffer, g());							
					} // for y
				} // if dy != 0
				else { // both dx and dy are 0; start and end point are identical
					double x = xs;
					double y = ys;
					double z = zs;
					if (filled) 
						filledPolygon.updateBoundary(x, y, z);
					filledPolygon.drawPixel_ifCloser ((int)x, (int)y, z, zBuffer, g());				
				}	
			} // steep line	
		} // drawLine3D
	} // drawLine

	// access to polygon filling routines (implemented in BaFilledPolygon)
	void startFill() { 
		filledPolygon.init(); 
	}	
	
	void fillLines() {
		filledPolygon.fillLines(zBuffer, g()); 
	}	
	
	void updateBoundary(double x, double y, double z) { 
		filledPolygon.updateBoundary(x, y, z); 
	}
} // class BaFrameBuffer
