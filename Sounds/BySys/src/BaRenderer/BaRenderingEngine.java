package BaRenderer;

import java.awt.Graphics;
import java.awt.Color;

import BaMath.*;
import BaMain.*;

/* **************************************************************************
 * The rendering engine receives commands from the scene graph (when it is
 * traversed for (re)draw all scene objects, using the renderYourself methods
 * of BaSceneGraphNodes).
 * The essential commands are
 * - begin    starting a new list of vertices to form a polygon of some shape
 *            (drawMode)
 * - vertex   sending vertex data from the scene graph to the rendering 
 *            engine. The rendering has to react to each new vertex depending
 *            on the shape that is being drawn (drawMode)
 * - end      signaling that the current shape is finished.
 *            For some shapes (drawModes), the rendering has to react e.g. 
 *            by closing the current polygon (drawing a line from the last 
 *            vertex to the first one)
 *            
 * drawModes: NONE, POINTS, STRINGS, LINES, LINE_STRIP, LINE_LOOP, POLYGON,
 *            TRIANGLES, TRIANGLE_STRIP, QUADS, QUAD_STRIP, TRIANGLE_FAN
 * 
 * The rendering engine coordinates what lines need to be drawn from where
 * to where (according to the selected drawMode), but it does not execute the
 * drawing process. Rather, it forwards it to the graphics card.
 * 
 * In a real rendering engine, the graphics card would be implemented and
 * handled on a real hardware. In this exemplary rendering engine, the
 * graphics card is implemented in software in class BaGraphicsCard.
 * 
 * (See also explanations in class "BaFrameBuffer".)
 * *************************************************************************/

public class BaRenderingEngine
{
	// Variables
	private int			   cnt;
	private double []	   xvec, yvec, zvec;
	private int			   maxVertex;
	private String []	   strvec;
	private int			   strcnt;

	// graphics card
	private BaGraphicsCard gCard;
		
	// Constructor
	public BaRenderingEngine(int w, int h) {
		gCard = new BaGraphicsCard(w,h);		
		// List of vertices and text strings to draw polygons and strings
		maxVertex = 100;
		xvec	  = new double[maxVertex];
		yvec	  = new double[maxVertex];
		zvec	  = new double[maxVertex];
		strvec	  = new String[maxVertex];
		cnt		  = 0;
		strcnt	  = 0;	
		
		init (w,h);
	}
	
	// Methods
		
	public void init(int w, int h) {
		// handle on the drawing window in the BasicFrame
		gCard.init(w,h);
						
		// internal list of vertices and text strings to draw polygons and strings
		cnt = 0;
	}
		
	/*
	 * Methods to draw polygons (similar to programming model of openGL)
	 */
	/*
	 * typical pseudocode to draw a rectangle looks like this:
	 *   begin (QUADS);
	 *     vertex (v0);
	 *     vertex (v1);
	 *     vertex (v2);
	 *     vertex (v3);
	 *   end ();
	 */
	
	public void begin(BaGraphicsCard.DrawMode d) {
		gCard.setDrawMode(d);
		cnt = 0;
		strcnt = 0;		
		gCard.startFill();
	}

	public void vertex(BaVectorH3D v) {
		// transform the vertex position from the local 3D coordinate system to 
		// the 3D camera coordinate system

		// vf = projectionMatrix * modelviewMatrix * v
		BaVectorH3D vf = (BaVectorH3D) 
				 gCard.ProjectionMatrix().times 
				(gCard.ModelviewMatrix()).times 
				(v);
		// un-homogenize the vector (4D -> 3D)
		vf.project(); 
		double x = vf.val(0);
		double y = vf.val(1);
		double z = vf.val(2);
		
		// viewport adaptation
		int w = BaMain.sWin.getWidth();
		int h = BaMain.sWin.getHeight();
		double s = Math.min(w, h)/2;
		double xw = (x+1)*s;   
		double yw = h - (y+1)*s;
		// flip the y-axis to switch from AWT coordsys (upper left) 
		// to openGL coordsys (lower left)

		// store the current pixel position 'curr' in the vertex list
		int curr = cnt++;
		xvec[curr] = xw;
		yvec[curr] = yw; 
		zvec[curr] = z; // unimportant - unless we perform hidden surface removal
		
		// draw lines between adjacent vertices of the vertex list,
		// draw text strings at the current vertex
			
		// draw text
		if ((gCard.drawMode() == BaGraphicsCard.DrawMode.STRINGS) && (strcnt > 0)) {
			gCard.drawString (strvec[strcnt-1], xvec[curr], yvec[curr]);
		}
		
		// if this is the first vertex, we can not yet draw lines;
		// don't do anything; just wait for the next vertex
		if (curr == 0) 
			return;

		// if we are beyond the first vertex, draw lines from 'prev' to 'curr'
		// -- except when a closed primitive (line, triangle or quad) has just 
		// -- been "finished" and we need to skip to the next point to start 
		// -- drawing a new primitive 
		
		int prev = curr-1;
		// decide whether a line should be drawn
		switch (gCard.drawMode()) {
			case NONE:
			case POINTS:
			case STRINGS:
				// do nothing
				break;
			case LINES: // draw every other line: 0-1, 2-3, 4-5, ...
				if (curr % 2 != 0) drawLine (prev,curr);
				break;
			case LINE_STRIP: 
			case LINE_LOOP: 
			case POLYGON: 
				drawLine (prev,curr); 
				break;
			case TRIANGLES: 
				// draw lines 0-1-2,  3-4-5,  6-7-8, ...
				if (curr % 3 != 0) drawLine (prev,curr);
				// draw lines back from third point to first point to close triangles: 
				// 2-0,  5-3, 8-6, ...
				if (curr % 3 == 2) {
					drawLine (curr,curr-2);
					gCard.fillLines();
					gCard.startFill();
				}
				break;
			case TRIANGLE_STRIP:
				// arrangement of vertices in trianglestrips:
				//
				//         curr    *     *
				//    v0 --*v2*-- v4 -- v6   ...
				//     |  /  |  /  |  /  |   ...
				//     | /   | /   | /   |   ...
				//    v1 -- v3 -- v5 -- v7   ...
				//           *     *     *
				//
				// start when at least three vertices exist
				// then, draw a new triangle whenever a new vertex comes in
				// i.e., for v2, v3, v4, ...
				if (curr >=2) {
					gCard.startFill();
					drawLine (curr-2,curr-1);
					drawLine (curr-1,curr);
					drawLine (curr,curr-2);
					gCard.fillLines();					
				}
				break;
			case QUADS: 
				// draw lines 0-1-2-3,  4-5-6-7,  8-9-10-11, ...
				if (curr % 4 != 0) drawLine (prev,curr);
				// draw lines back from fourth point to first point to close quads: 
				// 3-0,  7-4, 11-8, ...
				if (curr % 4 == 3) {
					drawLine (curr,curr-3);
					gCard.fillLines();
					gCard.startFill();
				}
				break;
			case QUAD_STRIP:
				// arrangement of vertices in quadstrips:
				//
				//    v0 ---- v2 ---- v4 ---- v6   ...
				//     |       |       |       |   ...
				//     |       |       |       |   ...
				//    v1 ----*v3*---- v5 ---- v7   ...
				//           curr      *       *   ...
				//
				// start when at least four vertices exist
				// then, draw a new quad whenever two more vertices have come in
				// i.e., for v3, v5, v7, ...

				if ((curr >=3) && (curr %2 == 1)) {
					gCard.startFill();
					drawLine (curr-3,curr-2);
					drawLine (curr-1,curr-0);
					drawLine (curr-1,curr-3);
					drawLine (curr,curr-2);
					gCard.fillLines();					
				}
				break;
			case TRIANGLE_FAN:
				// not yet implemented
				break;
		} // switch (_drawMode)
	} // vertex
	
	public void end() {
		// close loops and polygons (draw line from last vertex to first vertex)
		int curr = cnt;
		if (curr > 0) {	
			int last = curr-1; // last element
			switch (gCard.drawMode()) {
				case NONE:
				case POINTS:
				case STRINGS:
				case LINES:
				case LINE_STRIP: 
				case TRIANGLES:
				case TRIANGLE_STRIP:
				case TRIANGLE_FAN:
				case QUADS:
				case QUAD_STRIP:
					// do nothing
					break;
					
				// For closed polygons:
				// draw final line from last point back to first point
				case LINE_LOOP:
				case POLYGON:   // not exactly according openGL-definition
					gCard.drawLine (xvec[last], yvec[last], zvec[last],   xvec[0], yvec[0], zvec[0]);
					gCard.fillLines();						
			} // switch
		}
		gCard.setDrawMode(BaGraphicsCard.DrawMode.NONE);
		curr = 0;
		cnt = 0;
	} // end

	public void text(String str) {
		strvec[strcnt] = str;
		strcnt++;
	}

	/*
	 * Routines to manipulate the primitives (and stacks) of the graphics context 
	 * on the graphics card.
	 */

	// color
	public void setColor(Color c) 						{ gCard.setColor(c); }
	public Color color()								{ return gCard.color(); }
	public void pushColor()								{ gCard.pushColor(); } 
	public void popColor()								{ gCard.popColor(); } 

	// linewidth
	public void setLinewidth(int l)						{ gCard.setLinewidth(l); }
	public int  lineWidth()								{ return gCard.lineWidth(); }
	public void pushLinewidth()							{ gCard.pushLinewidth(); }	
	public void popLinewidth()							{ gCard.popLinewidth(); } 
	
	// drawMode
	public void setDrawMode(BaGraphicsCard.DrawMode d)	{ gCard.setDrawMode(d); }
	public BaGraphicsCard.DrawMode drawMode()			{ return gCard.drawMode(); }
	public void pushDrawMode()							{ gCard.pushDrawMode(); } 	
	public void popDrawMode()							{ gCard.popDrawMode(); } 
		
	// filled
	public void setFilled(boolean f)					{ gCard.setFilled(f); }
	public boolean filled()								{ return gCard.filled(); }
	public void pushFilled()							{ gCard.pushFilled(); } 	
	public void popFilled()								{ gCard.popFilled(); }

	// modelviewMatrix
	public BaTransformation ModelviewMatrix()			{ return gCard.ModelviewMatrix(); }	
	public void pushModelviewMatrix()		 			{ gCard.pushModelviewMatrix(); }
	public void popModelviewMatrix()		 			{ gCard.popModelviewMatrix(); }

	// projectionMatrix
	public BaTransformation ProjectionMatrix()			{ return gCard.ProjectionMatrix(); }
	public void pushProjectionMatrix()	 				{ gCard.popProjectionMatrix(); }
	public void popProjectionMatrix()					{ gCard.popProjectionMatrix();}
	
	// viewportMatrix
	public BaTransformation ViewportMatrix()	 		{ return gCard.ViewportMatrix(); }
	public void pushViewportMatrix()		 			{ gCard.pushViewportMatrix(); }
	public void popViewportMatrix()		 				{ gCard.popViewportMatrix(); }

	// worldMatrix
	public BaTransformation WorldMatrix()				{ return gCard.WorldMatrix(); }	
	public void pushWorldMatrix()		 				{ gCard.pushWorldMatrix(); }
	public void popWorldMatrix()		 				{ gCard.popWorldMatrix(); }
	
	/*
	 * Methods to receive messages from the windowFrame
	 */

	public void notifyResize(int w, int h)				{ gCard.notifyResize(w,h); }
	public void setGraphics(Graphics g)					{ gCard.setGraphics(g); }
	public void mypaint(Graphics g) 					{ gCard.mypaint(g);	}
	
	/*
	 * Helper methods
	 */
	
	public BaVertexH3D transformToWindow(BaVertexH3D vf) {
		BaVertexH3D tmp = new BaVertexH3D ();
		tmp.copy(vf);
		
		tmp.project();
		double x = tmp.val(0);
		double y = tmp.val(1);
//		double z = tmp.val(2);
		
		// viewport adaptation
		int w = BaMain.sWin.getWidth();
		int h = BaMain.sWin.getHeight();
		double s = Math.min(w, h)/2;
		double xw = (x+1)*s;   
		double yw = h - (y+1)*s;
			// flip the y-axis to switch from AWT coordsys (upper left) 
			// to openGL coordsys (lower left)
		
		tmp.setVal(0, xw);
		tmp.setVal(1, yw);
		return tmp;
	} // transformToWindow
	
	public void drawLine(int s, int e) {
		gCard.drawLine (xvec[s], yvec[s], zvec[s],   xvec[e], yvec[e], zvec[e]);
	}

} // class BaRenderingEngine
