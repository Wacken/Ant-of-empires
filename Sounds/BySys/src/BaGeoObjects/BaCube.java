package BaGeoObjects;

import java.awt.Color;

import BaRenderer.*;
import BaGeoSceneGraph.*;
import BaMain.*;

public class BaCube
	extends BaSceneGraphNode 
{
	// Variables
	private boolean filled = true;

	// Constructor
	public BaCube(String name) {
		super(name);
		double rad90 = Math.toRadians(90.0);

		// create the 6 surfaces of the cube and arrange them appropriately
		coordSys = new BaCoordSys (name);
		children = 6;
		child = new BaSceneGraphNode[children];
				
		child[0] = new BaSquare("front plane ");

		child[1] = new BaSquare("back plane  ");
		child[1].translate(0.0, 0.0, -1.0);

		child[2] = new BaSquare("left plane  ");
		child[2].rotate('y', rad90);

		child[3] = new BaSquare("right plane ");
		child[3].translate(1.0, 0.0, 0.0);
		child[3].rotate('y', rad90);

		child[4] = new BaSquare("bottom plane");
		child[4].rotate('x', -rad90);

		child[5] = new BaSquare("top plane   ");
		child[5].translate(0.0, 1.0, 0.0);
		child[5].rotate('x', -rad90);

	} // BaCube

	
	// Getters and Setters
	public void setFilled (boolean f) { filled = f; }
	
	// Methods

	public void renderChildren (BaRenderingEngine RE) {
		// render the cube
		Color [] childColor = {
				Color.cyan, Color.cyan,
				Color.magenta, Color.magenta,
				Color.yellow, Color.yellow
		};

		for (int i=0; i<children; i++) {
			((BaSquare) child[i]).render(RE,childColor[i],filled);
		}
	}
	
} // class BaCube
