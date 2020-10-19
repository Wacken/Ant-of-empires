package BaGeoObjects;

import java.awt.Color;

import BaMath.*;
import BaRenderer.*;
import BaGeoSceneGraph.*;

class BaSquare
	extends BaSceneGraphNode {

	// Variables
	private BaVertexH3D [] v;

	// Constructor
	BaSquare (String name) {
		super (name);
		v = new BaVertexH3D [4];

		// create the 4 corner vertices of the square
		v[0] = new BaVertexH3D ("v0",  0.0, 0.0, 0.0);
		v[1] = new BaVertexH3D ("v1",  0.0, 1.0, 0.0);
		v[2] = new BaVertexH3D ("v2",  1.0, 1.0, 0.0);
		v[3] = new BaVertexH3D ("v3",  1.0, 0.0, 0.0);
	} // BaSquare

	// Methods of Square
	
	public void renderYourself (BaRenderingEngine RE) {
		// render the square (programming model similar to OpenGL)	
		RE.begin (BaGraphicsCard.DrawMode.QUADS);
		for (int i=0; i<v.length; i++) RE.vertex (v[i]);
		RE.end();
	}

	public void printYourself (BaRenderingEngine RE, 
			String leadstr, BaVertexH3D.refCoordSys inRefSys) {

		System.out.println (
				leadstr + name + ": square " + 
				" (" + BaVertexH3D.refStr (inRefSys) + " coordinate system): ");	

		// print the square
		printVecVals (RE, v, leadstr, inRefSys, 4);
	}
} // class BaSquare
