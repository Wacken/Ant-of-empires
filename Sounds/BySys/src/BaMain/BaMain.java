package BaMain;

import BaMath.*;
import BaRenderer.*;
import BaWindow.*;
import BaGeoSceneGraph.*;
import BaGeoObjects.*;
import BaGeoWorlds.*;
import BaGeoWorlds.BaRobotWorld.*;

/* **************************************************************************
 * Preamble:
 * This code is intended to provide first-year computer science students 
 * (specialty games engineering) in their very first months of learning how 
 * to program (in Java) with a basic understanding of how a game engine 
 * works.
 *   
 * The code is intentionally written completely in Java - without using 
 * commonly available graphics libraries (such as openGL). It is not intended 
 * to be efficient. Rather, it tries to convey the programming model that 
 * openGL programmers need to be aware of when writing openGL programs 
 * (providing a view "under the hood" of the basic flow of information in 
 * such programs).
 *   
 * Particular emphasis lies on the construction of a simple hierarchical 
 * "scene graph" and on the accumulative handling of transformations to 
 * transform vertices from their own, local coordinate space into a global, 
 * world-wide coordinate space (based globally on a "geometry pipeline" and 
 * locally on "matrix stacks".)
 *   
 * A further aspect is the management of control back and forth between the 
 * main loop of the game engine and the management of a window (AWT) via 
 * "callbacks"
 * *************************************************************************/

/* **************************************************************************
 * BaSys:  The Basic System Components of a Game Engine
 * *************************************************************************/

public class BaMain 
{		
	// static Gobal Variables	
	public static BaSceneGraphNode	vWorld;
	public static BaCamera 			vCam;	
	public static BaRenderingEngine	rEng;
	public static BaWindowFrame 	sWin;

	// Constructor	
	public BaMain () {};
	
	// Main program
	//=====================================
	public static void main (String[] args) 
	//=====================================
			throws InterruptedException 
	{
		//--------------------------------------------
		// STEP 1:
		// Create and initialize the essential objects
		//--------------------------------------------
		
//		 The virtual WORLD, represented as a scene graph
		vWorld = new BaRobot( "Robot" ); 
//		vWorld = new BaCubeWorld ( "Cube World", BAG.cubes_per_row);
//		vWorld = new BaTestWorld ( "Test World" );
//		vWorld = new BaRobotWorld ("Robot World");

		// The 2D WINDOW, as shown the display SCREEN
		int x=250, y=50, w=1000, h=800;
		sWin = new BaWindowFrame( "BaSys", x,y,w,h );

		// the virtual CAMERA placed inside the world
		double aspect = (double)w / (double)h;
		double angle = 80.0;	
		double near = 1.0;
		double far = 1e6;
		BaVertexH3D eyePos = new BaVertexH3D( -0.25, 0.75, 20.00 );
		BaVertexH3D wldPos = new BaVertexH3D( 0.0, 0.0, 0.0 );
		BaVectorH3D	upDir = new BaVectorH3D( 0.0, 1.0, 0.0 );
		vCam = new BaCamera( "virtualCamera", 
				angle, aspect, near, far,   
				eyePos, wldPos, upDir );

		// The RENDERER
		rEng = new BaRenderingEngine( sWin.getWidth(),sWin.getHeight() );

		BAG.initialized = true;				
	
		//--------------------------------------------------------
		// STEP 2:
		// Present and animate the virtual world (render or print)
		//--------------------------------------------------------		

		vWorld.print( rEng,"",BaVertexH3D.refCoordSys.inGLOBAL );
		
		// MAIN LOOP of the renderer/game engine
		while (true) { // infinite loop
			// Capture/react to user input
				// done asynchronously via RenderFrame and PanelButtons

			// Compute entity reactions
				// already done (asynchronously) in the previous step

			// Perform next simulation/animation step for the world
			vWorld.simulateNextStep();
			
			// Render and display result
			rEng.init( sWin.getWidth(), sWin.getHeight() );
			sWin.repaint_synchronized();			
			
			// Check high-level game logic
				// do nothing
			
			// Manage control variables
				// do nothing
		} // MAIN LOOP of the render / game engine		
		
	} // main program
}   // class BaMain
