package BaGeoWorlds;

import BaRenderer.*;
import BaGeoSceneGraph.*;
import BaGeoObjects.*;
import BaMath.*;
import BaMain.*;

//***************************************************************************
//Objects (assembled in a tree structure)
//Similar to concepts in Scene Graphs, such as OpenInventor (VRML) or Java3D
//***************************************************************************

//Sketch of the scene graph 
//arranged up by the constructors of the subsequently defined classes
//World, Cube and Square:
//
//                                 _world  (global variable in main program)
//                                    |
//                                    +--------------------------------+
//                                    |                                |
//          ...     ...     ...     _cube     ...     ...          _coordsys
//                                    |
//                  +-----------------+-----------------+
//                  |                                   |
//         +--------+--------+                  +-------+--------+
//         |                 |                  |                |
//      _square    ...    _square            _square   ...    _square
//         |                 |                  |                |
//   +---+-+-+---+     +---+-+-+---+      +---+-+-+---+    +---+-+-+---+
//   |   |   |   |     |   |   |   |      |   |   |   |    |   |   |   |
//  _v0 _v1 _v2 _v3   _v0 _v1 _v2 _v3    _v0 _v1 _v2 _v3  _v0 _v2 _v3 _v4
//
//
//The 'render' and 'print' methods traverse the tree top-to-bottom and 
//left-to-right, handing the transformation matrix _T to the RenderingEngine
//(accumulating it onto the ModelviewMatrix), as well as providing the vertex 
//vectors _v0.._v3 of each _square of each _cube in the _world.
//
//During the tree traversal, the state of the graphics engine (esp. the 
//ModelviewMatrix) needs to be saved on the way down through a tree branch to 
//the bottom such that on the way back up, it can be restored (in order to 
//"undo" the transformations that were accumulated onto the ModelviewMatrix 
//further down in the tree.

public class BaCubeWorld
	extends BaSceneGraphNode 
{
	// Variables
	private BaCube[][] cube; // array of _n x _n cubes
	private int        n;	

	// Constructor
	public BaCubeWorld (String name, int n) {
		// Create a world of nxn cubes (each of size 0.5), arranged as a 
		// tableau in the x-y plane at z=0

		super(name);
		coordSys = new BaCoordSys(name);
		this.n = n;
		double offset = (double) this.n / 2.0 - 0.25;

		cube = new BaCube[this.n][this.n];
		for (int i = 0; i < this.n; i++) {
			for (int j = 0; j < this.n; j++) {
				cube[i][j] = new BaCube("cube["+i+"]["+j+"]");
				cube[i][j].translate((double)i-offset,(double)j-offset,0.0);
				cube[i][j].scale(0.5, 0.5, 0.5);
			}
		}
		
		setFilled (BAG.filled);
		
		// Build the scene graph
		for (int i = 0; i < this.n; i++) {
			for (int j = 0; j < this.n; j++) {
				this.addChildNode(cube[i][j]);
			}
		}
	} // BaCubeWorld

	
	
	// Methods
	public void setFilled (boolean f) {
		//_filled = f;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				cube[i][j].setFilled(f);
			}
		}
	}

    public void renderYourself (BaRenderingEngine RE) {
		// render the three axes of the coordinate system
		coordSys.render(RE);
    }
	
	static double epsilon = 0.01;
	static int cnt=0;
	
	public void simulateNextStep () {	
		//camera.lookAt(ctrl.eyePosition3D, ctrl.center3D, ctrl.upVector3D);

		//Simulate/animate world
		//cubeworld.rotate ('y', Math.toRadians(13.0));
		//cubeworld.rotate ('y', Math.toRadians(0.1));
		//cubeworld._cube[0][0].rotate ('x',Math.toRadians(0.3));
		int i = BAG.cubes_per_row / 2;
		cnt++;
		if (cnt == 100) {
			epsilon *= -1;
			cnt = 0;
		}
		double sx = 1+epsilon, sy=1+2*epsilon, sz = 1+epsilon;
		cube[i][i].rotate (Math.toRadians(0.5), 1.0,1.0,-1.0);
		cube[i][i].scale (sx,sy,sz);
		
		//if (cubes_per_row >= 3)
		//	cubeworld._cube[2][2].translate(0.4, 0.0, 0.0);
	}

	public BaSceneGraphNode selectObject (int n) {
		return cube[n][n];
	}

	public BaSceneGraphNode selectObject (String name) {
		return null;
	}

} // class BaCubeWorld
