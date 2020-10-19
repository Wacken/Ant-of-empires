package BaGeoObjects;

import java.awt.Color;

import BaMath.*;
import BaRenderer.*;
import BaGeoSceneGraph.*;

public class BaQuadStrip
	extends BaSceneGraphNode {

	// Variables
	private BaVertexH3D [] v;
	private Color       [] c;

	// Constructor
	public BaQuadStrip (String name) {
		super (name);
		v = null;
		c = null;
	} // BaQuadStrip

	// Methods

	public void setVertices (BaVertexH3D[] v) {
		int n = v.length;
		this.v = new BaVertexH3D[n];

		for (int i=0; i<n; i++) {
			this.v[i] = new BaVertexH3D (v[i]);
		}		
	} // setVertices

	public void setVertices (double [][] v) {
		int n = v.length;
		this.v = new BaVertexH3D[n];
		
		for (int i=0; i<n; i++) {
			this.v[i] = new BaVertexH3D (v[i][0], v[i][1], v[i][2]);
		}
	} // setVertices

	public void setColors (Color[] c) {
		int n = c.length;
		this.c = new Color[n];

		for (int i=0; i<n; i++) {
			this.c[i] = c[i];
		}		
	} // setColors
		
	public void renderYourself (BaRenderingEngine RE) {
		// render the square (programming model similar to OpenGL)	
		RE.begin (BaGraphicsCard.DrawMode.QUAD_STRIP);
		for (int i=0; i<v.length; i++) {
			int ci = i/2 -1;
			if ((ci>=0) && (c!= null) && (c.length > ci) ) {
				RE.setColor(c[ci]);
			}
			RE.vertex(v[i]);
		}
		RE.end();
	}

	public void printYourself (BaRenderingEngine RE, 
		String leadstr, BaVertexH3D.refCoordSys inRefSys) {

		// some general information in the title line
		System.out.println (
			leadstr + name + 
			": quad strip " + 
			" (" + BaVertexH3D.refStr (inRefSys) + " coordinate system): ");
		
		// print the quad strip
		printVecVals (RE, v, leadstr, inRefSys, 4);
	}	
} // class BaQuadStrip
