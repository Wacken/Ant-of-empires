package BaRenderer;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Stack;

import BaGeoObjects.*;
import BaMain.*;

/* **************************************************************************
 * Usually, the GraphicsCard is a piece of hardware.
 * Here, the GraphicsCard is emulated by software to show its principal
 * functioning.
 * 
 * A graphics card possesses various pieces of memory to store a number
 * of rendering attributes, called the "graphics context". Among these
 * are the current drawing color, linewidth and drawmode. We also include
 * the control parameter "filled" to indicate whether a polygon is filled
 * or only shown as a wireframe.
 * 
 * Furthermore, the graphics card stores the homogeneous (4x4) matrices
 * "modelviewMatrix" and "projectionMatrix" to compute transformations
 * between the local coordinate system of a vertex in a scene graph and
 * its global position with respect to the camera, as well as its 
 * window coordinates (projected from 3D down to 2D).
 * To support printing (debugging) we also include a "worldMatrix" such
 * that the routines "printYourself" of SceneGraphNodes can also print
 * vertex coordinate in the global world coordinate system (in addition
 * to presenting them with respect to the camera).
 * 
 * Graphics cards are capable of handling stacks of graphics contexts.
 * We emulate this, using the java.util.Stack package.
 * Each parameter owns its own stack. When traversing a scene graph (tree), 
 * the current parameters can be pushed onto the respective stacks while 
 * traversing downwards along a branch of the tree. The parameters can
 * then be popped back up when the traversal is reversed, going upward to
 * a higher node to then start traversing down a sibling branch.
 * 
 * One of the most important parts of a graphics card is the frameBuffer.
 * It stores an number of informations for each pixel, such as its 
 * color as well as its depth. This is handled separately in class
 * "BaZBuffer".
 * 
 * A frameBuffer consists of large arrays as big as the current window
 * on the display screen. When users resize such windows, the windowing
 * system BaWindowFrame issues callbacks that notify the frameBuffer
 * to adjust its size accordingly. Furthermore, the windowing system
 * also issues callbacks to the frameBuffer to redraw itself (e.g
 * 60 times per second, and/or whenever some window event occurs: mouse
 * motion, partial occlusion, etc): method "mypaint".
 * (See also explanations in class "BaFrameBuffer".)
 * *************************************************************************/

public class BaGraphicsCard {

	// Variables
	
	// Graphics Context: frame buffer
	private BaFrameBuffer			frameBuffer;

	// Graphics Context: geometry pipeline
	private BaTransformation        modelviewMatrix;
	private BaTransformation        projectionMatrix;
	private BaTransformation        viewportMatrix;
	private BaTransformation        worldMatrix; // for debugging: vertices printed in world coordSys
	private Stack<BaTransformation> modelviewMatrixStack;
	private Stack<BaTransformation> projectionMatrixStack;
	private Stack<BaTransformation> viewportMatrixStack;
	private Stack<BaTransformation> worldMatrixStack;

	// Graphics Context: color, line width, filled
	private Color                   color;
	private int	                    linewidth;
	private boolean                 filled;
	private Stack<Color>            colorStack;
	private Stack<Integer>          linewidthStack;
	private Stack<Boolean>          filledStack;

	// Drawmode
	public enum DrawMode 
	{NONE,POINTS,LINES,LINE_STRIP,LINE_LOOP,TRIANGLES,TRIANGLE_STRIP,TRIANGLE_FAN,QUADS,QUAD_STRIP,POLYGON,STRINGS,};		
	private DrawMode                drawmode;
	private Stack<DrawMode>         drawmodeStack;

	// Constructor
	BaGraphicsCard(int w, int h) {
		frameBuffer 			= new BaFrameBuffer(w,h);

		// Graphics Context: geometry pipeline
		modelviewMatrix			= new BaTransformation ();
		projectionMatrix		= new BaTransformation ();
		viewportMatrix 			= new BaTransformation ();
		worldMatrix				= new BaTransformation (); // for debugging
		modelviewMatrixStack	= new Stack<BaTransformation> ();		
		projectionMatrixStack	= new Stack<BaTransformation> ();
		viewportMatrixStack  	= new Stack<BaTransformation> ();
		worldMatrixStack		= new Stack<BaTransformation> ();		

		// Graphics Context: color, line width, draw mode
		colorStack				= new Stack<Color> ();
		linewidthStack 			= new Stack<Integer> ();
		drawmodeStack			= new Stack<DrawMode> ();
		filledStack				= new Stack<Boolean> ();
	} // BaGraphicsCard

	void init(int w, int h) {

		// Graphics Context: geometry pipeline		

		// camera placement:
		// the transformation is defined to describe the camera pose within the 
		// world, i.e.: the camPos relative to the world coordinate system
		//
		// yet, we here need to set the _modelviewMatrix through the camera's eyes
		// i.e.: the worldPos relative to the camera coordinate system
		// we thus need to invert the pose		
		worldMatrix.identity();
		modelviewMatrix.identity();
		modelviewMatrix.gets (modelviewMatrix.times (BaMain.vCam.inversePose()));

		// Viewport
		viewportMatrix.identity();
		viewportMatrix.viewport (0, 0, w, h);

		// Frustum (perspective projection); 
		// geometric model similar to gluperspective (fov, aspect, near, far)
		projectionMatrix.identity();
		projectionMatrix.perspective_projection (
				Math.toRadians (BaMain.vCam.viewingAngle()), 
				BaMain.vCam.aspect(), BaMain.vCam.near(), BaMain.vCam.far());

		// Graphics Context: color, line width, draw mode
		color = Color.black;
		linewidth = 1;
		drawmode = DrawMode.NONE;
		filled = BAG.filled;
		
		// Frame buffer
		frameBuffer.init(w,h);
	} // init
	
    /*
     * Methods to manipulate the geometry pipeline of the graphics
     * context (including stacks)
     * 
     * NOTE for the subsequent matrix push operations: 
     * it is important to produce a local copy of the matrix
     * due to Java's treatment of arrays.
     * Only the address of the array (matrix) is pushed onto the
     * stack. If no local copy is made, any subsequent modifications
     * of the (original!) _modelviewMatrix also affect the variants
     * that have been pushed onto the stack - i.e. the state of the
     * matrix that existed when it was pushed onto the stack is not
     * kept untouched. IT IS CHANGED!
     */
	
	// _modelviewMatrix methods
	BaTransformation ModelviewMatrix() { return modelviewMatrix; }	
	void pushModelviewMatrix()		 { BaTransformation t = new BaTransformation (modelviewMatrix); 
									   modelviewMatrixStack.push(t); }
	void popModelviewMatrix()		 { modelviewMatrix.copy (modelviewMatrixStack.pop());}

	// _projectionMatrix methods
	BaTransformation ProjectionMatrix(){ return projectionMatrix; }
	void pushProjectionMatrix()		 { BaTransformation t = new BaTransformation (projectionMatrix);
									   projectionMatrixStack.push (t); }
	void popProjectionMatrix()		 { projectionMatrix.copy(projectionMatrixStack.pop());}
	
	// _viewportMatrix methods
	BaTransformation ViewportMatrix()	 { return viewportMatrix; }
	void pushViewportMatrix()		 { BaTransformation t = new BaTransformation (viewportMatrix);
									   viewportMatrixStack.push (t); }
	void popViewportMatrix()		 { viewportMatrix.copy(viewportMatrixStack.pop()); }

	// _worldMatrix methods
	BaTransformation WorldMatrix()	 { return worldMatrix; }	
	void pushWorldMatrix()			 { BaTransformation t = new BaTransformation (worldMatrix); 
									   worldMatrixStack.push(t); }
	void popWorldMatrix()		 	 { worldMatrix.copy (worldMatrixStack.pop());}

	/*
	 * Methods to manipulate the graphics primitives of the graphics
	 * context (and their stacks)
	 */

	// _color methods
	void setColor(Color c)				{ color = c;                  
										  frameBuffer.setColor(color); }

	Color color()						{ return color;}
	void pushColor()					{ colorStack.push (color); } 
	void popColor()						{ color = colorStack.pop (); 
										  frameBuffer.setColor(color); } 

	// _linewidth methods
	void setLinewidth(int l)			{ linewidth = l; }
	int  lineWidth()					{ return linewidth; }
	void pushLinewidth()				{ linewidthStack.push (linewidth); }	
	void popLinewidth()					{ linewidth = linewidthStack.pop (); } 
	
	// _drawMode methods
	void setDrawMode(DrawMode d)		{ drawmode = d; }
	DrawMode drawMode()					{ return drawmode; }
	void pushDrawMode()					{ drawmodeStack.push (drawmode); } 	
	void popDrawMode()					{ drawmode = drawmodeStack.pop (); } 
		
	// _filled methods
	void setFilled(boolean f)			{ filled = f; }
	boolean filled()					{ return filled; }
	void pushFilled()					{ filledStack.push (filled); } 	
	void popFilled()					{ filled = filledStack.pop (); }

	/*
	 * methods for tasks handled by the framebuffer
	 */
	
	void notifyResize(int w, int h)		{ frameBuffer.notifyResize(w,h); }
	void setGraphics(Graphics g)		{ frameBuffer.setGraphics(g); }
	void mypaint(Graphics g)			{ frameBuffer.mypaint(g); }

	void startFill()					{ frameBuffer.startFill(); }	
	void fillLines()					{ if (filled) frameBuffer.fillLines(); }
	
	void drawLine(double x0, double y0, double z0, double x1, double y1, double z1) {
		frameBuffer.drawLine(x0, y0, z0, x1, y1, z1, filled);
	}

	void drawString(String str, double x, double y) {
		frameBuffer.drawString (str, (int)x, (int) y);		
	}
} // BaGraphicsCard
