package BaGeoObjects;

import java.awt.Color;

import BaMath.*;
import BaRenderer.*;
import BaGeoSceneGraph.*;

public class BaCoordSys
	extends BaSceneGraphNode 
{
	/*
	 * Draw three lines (red, green, blue) to illustrate the position
	 * and orientation of some coordinate system.
	 * (Very helpful for visualizing the current state of a scene graph 
	 * when a program needs to be debugged.)
	 */

	// Variables
	private BaVertexH3D o, x1, y1, z1;
	private boolean     showLabels = false;

	// Constructor
	public BaCoordSys(String name) {
		super(name);

		// create the origin and 3 points on the three axes
		o  = new BaVertexH3D("O", 0.0, 0.0, 0.0);
		x1 = new BaVertexH3D("x", 1.0, 0.0, 0.0);
		y1 = new BaVertexH3D("y", 0.0, 1.0, 0.0);
		z1 = new BaVertexH3D("z", 0.0, 0.0, 1.0);
	} // BaCoordSys

	// getters and setters
	public void setShowLabels (boolean b) {
		showLabels = b;
	}
	
	// Methods

	public void renderYourself (BaRenderingEngine RE) {
		RE.pushColor();
		RE.pushLinewidth();

		RE.setLinewidth(2);

		// render coordinate axes
		RE.begin(BaGraphicsCard.DrawMode.LINES);
		RE.setColor(Color.red);
		RE.vertex(o);
		RE.vertex(x1);

		RE.setColor(Color.green);
		RE.vertex(o);
		RE.vertex(y1);

		RE.setColor(Color.blue);
		RE.vertex(o);
		RE.vertex(z1);
		RE.end();

		// render text labels 
		// (this solution does not correspond to OpenGL concepts)
		if (name.length() > 0) {
			RE.begin(BaGraphicsCard.DrawMode.STRINGS);
			RE.setColor(Color.black);
			RE.text(name);
			RE.vertex(o);
			RE.end();
		}
		if (showLabels) {
			RE.begin(BaGraphicsCard.DrawMode.STRINGS);
			RE.setColor(Color.red);
			RE.text("x");
			RE.vertex(x1);

			RE.setColor(Color.green);
			RE.text("y");
			RE.vertex(y1);

			RE.setColor(Color.blue);
			RE.text("z");
			RE.vertex(z1);
			RE.end();
		}

		// restore previous state of the graphics context: 
		RE.popLinewidth();
		RE.popColor();
	}
	
	public void printYourself (BaRenderingEngine RE, 
			String leadstr, BaVertexH3D.refCoordSys inRefSys) {
		// print the coordinate axes
		System.out.print(name + " (global): ");
		o.print();
		System.out.print(" - ");
		x1.print();
		System.out.print(" - ");
		y1.print();
		System.out.print(" - ");
		z1.print();
		System.out.println("");
	} //
} // class BaCoordSys
