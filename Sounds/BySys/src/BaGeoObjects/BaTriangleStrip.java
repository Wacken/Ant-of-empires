package BaGeoObjects;

import java.awt.Color;

import BaMath.*;
import BaMath.BaVertexH3D.refCoordSys;
import BaRenderer.*;
import BaGeoSceneGraph.*;
import BaMain.*;

public class BaTriangleStrip
	extends BaSceneGraphNode {

	// Variables
	private BaVertexH3D [] v;
	private Color		[] c;

	// Constructor
	public BaTriangleStrip (String name) {
		super (name);
		v = null;
		c = null;
	}

	// Methods

	public void setVertices (BaVertexH3D[] v) {
		int n  = v.length;
		
		this.v = new BaVertexH3D[n];
		for (int i=0; i<n; i++) {
			this.v[i] = new BaVertexH3D (v[i]);
		}		
	}
	
	public void setVertices (double [][] v) {
		int n = v.length;

		this.v = new BaVertexH3D[n];		
		for (int i=0; i<n; i++) {
			this.v[i] = new BaVertexH3D (v[i][0], v[i][1], v[i][2]);
		}
	}

	public void setColors (Color[] c) {
		int n = c.length;
		this.c = new Color[n];

		for (int i=0; i<n; i++) {
			this.c[i] = c[i];
		}		
	}

	public void renderYourself (BaRenderingEngine RE) {
		// render the polygon defined by the TriangleStrip 
		// (programming model similar to OpenGL)	
		RE.begin (BaGraphicsCard.DrawMode.TRIANGLE_STRIP);
		for (int i=0; i<v.length; i++) {
			if ((c!= null) && BAG.insideRange (i-2, 0, c.length-1)) {
				RE.setColor(c[i-2]);
			}
			RE.vertex (v[i]);
		}
		RE.end();		
	}

	public void printYourself (BaRenderingEngine RE, 
			String leadstr, BaVertexH3D.refCoordSys inRefSys) {
		// some general information in the title line
		System.out.println (
				leadstr + name + ": triangle strip " + 
				" (" + BaVertexH3D.refStr (inRefSys) + " coordinate system): ");

		// print the triangle strip
		printVecVals (RE, v, leadstr, inRefSys, 4);
	}
} // class BaTriangleStrip
