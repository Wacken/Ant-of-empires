package BaGeoWorlds;

import java.awt.Color;

import BaRenderer.*;
import BaGeoSceneGraph.*;
import BaGeoObjects.*;
import BaMain.*;

public class BaTestWorld
	extends BaSceneGraphNode 
{
	// Variables
	private BaCube cube;
	
	private BaTriangleStrip tstrip;
	double [][] tpnts = {
			{0.0, 0.0, 0.0},
			{1.0, 0.0, 0.0},
			{1.0, 1.0, 0.0},
			{2.0, 1.0, 1.0},
			{3.0, 1.0, 1.0},
			{3.0, 2.0, 1.0}
	};
	private Color [] tcolors = {
			Color.red,
			Color.green,
			Color.blue,
			Color.white
		};
	
	private BaQuadStrip qstrip;
	double [][] qpnts = { 
			{0.0, 0.0,  1.0}, {1.0, 0.0,  1.0},
			{0.0, 1.0,  0.0}, {1.0, 1.0,  0.0},
			{0.0, 2.0, -2.0}, {1.0, 2.0, -2.0},
			{0.0, 3.0, -1.0}, {1.0, 3.0,  0.0},
			{0.0, 4.0,  0.0}, {1.0, 4.0,  0.0},
			{0.0, 5.0,  1.0}, {1.0, 5.0,  1.0}
	};
	private Color [] qcolors = {
		Color.yellow,
		Color.magenta,
		Color.pink,
		new Color(128,128,128),
		new Color(0,128,200)
	};
	
	private BaHeightMap hmap;
	double [][] hh = {
			{0.0,2.0,1.0,2.0,0.0},
			{2.0,0.5,0.0,0.5,2.0},
			{1.0,0.0,0.0,0.0,1.0},
			{2.0,0.5,0.0,0.5,2.0},
			{0.0,2.0,1.0,2.0,0.0}
	};
	
	// Constructor
	public BaTestWorld (String name) {
		super(name);
		coordSys = new BaCoordSys(name);	
		
		cube = new BaCube("Cube");
		cube.translate(1.0, 0.0, 0.0);
		
		tstrip = new BaTriangleStrip ("TriangleStrip");
		tstrip.translate(0.0, 2.0, 0.0);
		tstrip.setVertices(tpnts);
		tstrip.setColors(tcolors);
		
		qstrip = new BaQuadStrip ("QuadStrip");
		qstrip.translate(-1.0,-1.0,0.0);
		qstrip.setVertices(qpnts);
		qstrip.setColors(qcolors);
		
		hmap = new BaHeightMap ("HeightMap");
		hmap.translate(3.0,1.0,-2.0);
		hmap.setHeights(hh, 5, 5);
		
		// Build the scene graph
		this.addChildNode(cube);
		this.addChildNode(tstrip);
		this.addChildNode(qstrip);
		this.addChildNode(hmap);

	} // BaTestWorld

	// Methods

	public void setFilled (boolean f) {
		cube.setFilled(f);
	}

    public void renderYourself (BaRenderingEngine RE) {
		// render the three axes of the coordinate system
		coordSys.render(RE);
    }

	public void renderChildren (BaRenderingEngine RE) {
		cube.render(RE);

		tstrip.render(RE, Color.green, BAG.filled);
		tstrip.render(RE, Color.black, false);
	
		qstrip.render(RE, Color.cyan, BAG.filled);
		qstrip.render(RE, Color.black, false);
	
		hmap.render(RE, Color.red, BAG.filled);
		hmap.render(RE, Color.black, false);
	}

	public void simulateNextStep () {	
	}

	public BaSceneGraphNode selectObject (String name) {
		return null;
	}

} // class BaTestWorld
