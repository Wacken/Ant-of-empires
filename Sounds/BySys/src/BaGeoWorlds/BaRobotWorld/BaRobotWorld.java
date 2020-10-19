package BaGeoWorlds.BaRobotWorld;

import BaGeoObjects.*;
import BaGeoSceneGraph.*;
import BaMain.BAG;
import BaRenderer.BaRenderingEngine;

public class BaRobotWorld 
	extends BaSceneGraphNode 
{
	// Variables
	BaCuboid floor;
	BaRobot  robbie;
	BaRobot  claire;

	public BaRobotWorld (String str) {
		super(str);
		coordSys = new BaCoordSys(str);	
		coordSys.scale(2.0, 2.0, 2.0);		

		floor = new BaCuboid ("floor",20.0,0.1,8.0);
		floor.translate(0.0, -9.0, 0.0);

		robbie = new BaRobot ("robbie", true);
//		robbie.translate(-4.0, 0.0, 0.0);
		robbie.translate(2.0, 0.0, 0.0);
//		robbie.rotate(Math.toRadians(90.0), 0.0,1.0,0.0);
		
		claire = new BaRobot ("claire", false);
//		claire.translate(4.0, -1.8, 0.0);
		claire.translate(-2.0, -1.8, 0.0);
//		claire.rotate(Math.toRadians(-90.0), 0.0,1.0,0.0);
		claire.scale(0.8, 0.8, 0.8);

		this.addChildNode(robbie);
		this.addChildNode(claire);
	}
	
	public void simulateNextStep () {	
		for (int i=0; i<children; i++)
			child[i].simulateNextStep ();
	}
}
