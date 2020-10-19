package BaGeoWorlds.BaRobotWorld;

import BaMain.BAG;
import BaMath.BaVertexH3D;
import BaRenderer.*;
import BaGeoSceneGraph.*;
import BaGeoObjects.*;
import BaAnimation.*;

public class BaRobot
	extends BaSceneGraphNode 
{
	// Variables
	private BaCuboid trunk;
	
	private BaCuboid neck;
	private BaCuboid head;

	private BaCuboid uLegR; // upper leg, right
	private BaCuboid lLegR; // lower leg, right
	private BaCuboid footR; // foot, right	
	
	private BaCuboid uLegL; // upper leg, left
	private BaCuboid lLegL; // lower leg, left
	private BaCuboid footL; // foot, left

	private BaCuboid uArmR; // upper arm, right
	private BaCuboid lArmR; // lower arm, right
	private BaCuboid handR; // hand, right

	private BaCuboid uArmL; // upper arm, left	
	private BaCuboid lArmL; // lower arm, left
	private BaCuboid handL; // hand, left
	
	private BaAnimationClock timeStep;

	// Constructor
	public BaRobot (String str) {
		super( str );
		create (str, true);
	}
	public BaRobot (String str, boolean asTriangles) {
		super( str );
		create (str, asTriangles);
	}

	private void create (String str, boolean asTriangles) {
//		coordSys = new BaCoordSys( str );	
//		coordSys.scale( 2.0, 2.0, 2.0 );		
		timeStep = new BaAnimationClock( 400 );
		
		// Trunk
		trunk = new BaCuboid( "trunk", 3.0,5.0,1.0, asTriangles );
		
		// Neck
		neck = new BaCuboid( "neck", 0.7,0.5,0.7, asTriangles );
		neck.translate( 
				0.0, yEdgeOut(trunk,neck), 0.0 );
		// Head
		head = new BaCuboid( "head", 2.0,2.0,1.0, asTriangles );
		head.translate( 
				0.0, yEdgeOut(neck,head), 0.0 );

		// Right Leg and Foot
		//   right upper leg
		uLegR = new BaCuboid( "right upper leg", 1.0,3.0,1.0, asTriangles );
		uLegR.translate( 
				-xEdgeIn(trunk,uLegR), -yEdgeOut(trunk,uLegR), 0.0 ); 
		//   right lower leg
		lLegR = new BaCuboid( "right lower leg", 1.0,3.0,1.0, asTriangles );
		lLegR.translate( 
				0.0, -yEdgeOut(uLegR,lLegR), 0.0 ); 
		//   right foot
		footR = new BaCuboid( "right foot", 1.0,0.5,2.0, asTriangles );
		footR.translate( 
				0.0, -yEdgeOut(lLegR,footR), -zEdgeIn(lLegR,footR) ); 

		// Left Leg and Foot
		//   left upper leg
		uLegL  = new BaCuboid( "left upper leg", 1.0,3.0,1.0, asTriangles );
		uLegL.translate( 
				xEdgeIn(trunk,uLegL), -yEdgeOut(trunk,uLegL), 0.0 );
		//   left lower leg
		lLegL = new BaCuboid( "left lower leg", 1.0,3.0,1.0, asTriangles );
		lLegL.translate( 
				0.0, -yEdgeOut(uLegL, lLegL), 0.0 ); 
		//   left foot
		footL = new BaCuboid( "left foot", 1.0,0.5,2.0, asTriangles );
		footL.translate( 
				0.0, -yEdgeOut(lLegL,footL), -zEdgeIn(lLegL,footL) ); 
		
		// Right Arm and Hand
		//   right upper arm
		uArmR = new BaCuboid( "right upper arm", 0.5,3.0,0.5, asTriangles );
		uArmR.translate( 
				-xEdgeOut(trunk,uArmR), yEdgeIn(trunk,uArmR), 0.0 ); 
		//   right lower arm
		lArmR = new BaCuboid( "right lower arm", 0.5,2.0,0.5, asTriangles );
		lArmR.translate( 
				-xEdgeIn(uArmR,lArmR), -yEdgeOut(uArmR,lArmR), 0.0 ); 
		//   right hand
		handR = new BaCuboid( "right hand", 0.4,0.8,0.2, asTriangles );
		handR.translate( 
				0.0, -yEdgeOut(lArmR,handR), 0.0 ); 
		
		// Left Arm and Hand
		//   left upper arm
		uArmL = new BaCuboid( "left upper arm", 0.5,3.0,0.5, asTriangles );
		uArmL.translate( 
				xEdgeOut(trunk,uArmL), yEdgeIn(trunk,uArmL), 0.0 ); 
		//   left lower arm
		lArmL = new BaCuboid( "left lower arm", 0.5,2.0,0.5, asTriangles );
		lArmL.translate ( 
				-xEdgeIn(uArmL,lArmL), -yEdgeOut(uArmL,lArmL), 0.0 ); 
		//   left hand
		handL = new BaCuboid( "left hand", 0.4,0.8,0.2, asTriangles );
		handL.translate( 
				0.0, -yEdgeOut(lArmL,handL), 0.0 ); 

		// Build the scene graph
		this.addChildNode(trunk); // root of the robot
			trunk.addChildNode(neck);		
				neck.addChildNode (head);
			trunk.addChildNode(uLegR);
				uLegR.addChildNode(lLegR);
					lLegR.addChildNode(footR);
			trunk.addChildNode(uLegL);
				uLegL.addChildNode(lLegL);
					lLegL.addChildNode(footL);
			trunk.addChildNode(uArmR);
				uArmR.addChildNode(lArmR);
					lArmR.addChildNode(handR);
			trunk.addChildNode(uArmL);
				uArmL.addChildNode(lArmL);
					lArmL.addChildNode(handL);
	} // BaRobotWorld

	// Methods
	
	public BaSceneGraphNode selectObject (String name) {
		return null;
	}
	public void simulateNextStep () {	

		timeStep.nextStep();
		
		// animate Head
		if (timeStep.between(0,99))
			head.rotate (Math.toRadians(-0.2), 0.0,1.0,0.0);
		if (timeStep.between(100, 199))
			head.rotate (Math.toRadians(0.2), 0.0,1.0,0.0);
		if (timeStep.between(200, 299))
			head.rotate (Math.toRadians(0.2), 0.0,1.0,0.0);
		if (timeStep.between(300, 399))
			head.rotate (Math.toRadians(-0.2), 0.0,1.0,0.0);	

		// animate right upper leg
		if (timeStep.between(0, 199))
			rotateWithOffset (
					uLegR, -0.2, 1.0,0.0,0.5, 0.0,uLegR.h()/2.0,0.0);
		if (timeStep.between(200, 399))
			rotateWithOffset (
					uLegR, 0.2, 1.0,0.0,0.5, 0.0,uLegR.h()/2.0,0.0);
		
		// Animate right lower leg
		if (timeStep.between(0, 199))
			rotateWithOffset (
					lLegR, 0.2, 1.0,0.0,0.0, 0.0,lLegR.h()/2.0,0.0);
		if (timeStep.between(200, 399))
			rotateWithOffset (
					lLegR, -0.2, 1.0,0.0,0.0, 0.0,lLegR.h()/2.0,0.0);

		// Animate right foot
		if (timeStep.between(0, 199))
			rotateWithOffset (
					footR, 0.2, 1.0,0.0,0.0, 0.0,footR.h()/2.0,0.0);
		if (timeStep.between(200, 399))
			rotateWithOffset (
					footR, -0.2, 1.0,0.0,0.0, 0.0,footR.h()/2.0,0.0);

		// Animate right upper arm
		if (timeStep.between(0, 199))
			rotateWithOffset (
					uArmR, -0.4, 0.0,0.0,1.0, 0.0,uArmR.h()/2.0,0.0);
		if (timeStep.between(200, 399))
			rotateWithOffset (
					uArmR, 0.4, 0.0,0.0,1.0, 0.0,uArmR.h()/2.0,0.0);

		// Animate right lower arm
		if (timeStep.between(0, 199))
			rotateWithOffset (
					lArmR, -0.5, 1.0,0.0,0.0, 0.0,lArmR.h()/2.0,0.0);
		if (timeStep.between(200, 399))
			rotateWithOffset (
					lArmR, 0.5, 1.0,0.0,0.0, 0.0,lArmR.h()/2.0,0.0);
		
		// Animate right hand
		if (timeStep.between(0, 199))
			rotateWithOffset (
					handR, -0.2, 1.0,1.0,1.0, 0.0,handR.h()/2.0,0.0);
		if (timeStep.between(200, 399))
			rotateWithOffset (
					handR, 0.2, 1.0,1.0,1.0, 0.0,handR.h()/2.0,0.0);
				
		// Animate left upper arm
		if (timeStep.between(0, 199))
			rotateWithOffset (
					uArmL, -0.2, 1.0,0.0,0.0, 0.0,uArmL.h()/2.0,0.0);
		if (timeStep.between(200, 399))
			rotateWithOffset (
					uArmL,  0.2, 1.0,0.0,0.0, 0.0,uArmL.h()/2.0,0.0);			
				
	} // simulateNextStep
		
	// helper functions
	private void rotateWithOffset (
			BaCuboid p, 
			double angle, double rx, double ry, 
			double rz, double offx, double offy, double offz) {
		p.translate (offx,offy,offz);
		p.rotate (Math.toRadians(angle), rx,ry,rz);
		p.translate (-offx,-offy,-offz);			
	}
		
	private double xEdgeOut (BaCuboid p1, BaCuboid p2) {
		return p1.w()/2.0 + p2.w()/2.0; // width in x-direction
	}
	private double yEdgeOut (BaCuboid p1, BaCuboid p2) {
		return p1.h()/2.0 + p2.h()/2.0; // height in y-direction
	}
	private double zEdgeOut (BaCuboid p1, BaCuboid p2) {
		return p1.d()/2.0 + p2.d()/2.0; // depth in z-direction
	}
	
	private double xEdgeIn (BaCuboid p1, BaCuboid p2) {
		return p1.w()/2.0 - p2.w()/2.0; // width in x-direction
	}
	private double yEdgeIn (BaCuboid p1, BaCuboid p2) {
		return p1.h()/2.0 - p2.h()/2.0; // height in y-direction
	}
	private double zEdgeIn (BaCuboid p1, BaCuboid p2) {
		return p1.d()/2.0 - p2.d()/2.0; // depth in z-direction
	}
} // class BaRobotWorld

