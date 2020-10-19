package BaMath;

import BaRenderer.*;

public class BaVertexH3D
	extends BaVectorH3D 
{
	//Superclass hVector3D defines routines for 3D vector manipulations
	//- project ()           // turns a homogeneous 3D vector (with 4 components) 
	//                       // an inhomogeneous "normal" 3D vector (4. component w = 1)
	//- normalize ()         // normalize the vector s.t. it has length 1
	//- plus (hVector3D v)   // vector addition             this.plus  (v)
	//- minus (hVector3D v)  // vector subtraction          this.minus (v)
	//- dot (hVector3D v)    // scalar (dot) product        this.dot   (v)
	//- cross (hVector3D v)  // cross  (vector)             this.cross (v)
	//Superclass VVector defines
	//- init ()
	//- Vector times (mat)    // (vector-matrix multiplication)   this.times (mat)
	//- print ()              // present the (original) vector values in the local coord sys

	// Variables
	private String name;	
	// use array v of the (super)superclass Vector
	
	public enum refCoordSys {
		inLOCAL, inGLOBAL, inCAMERA, inWINDOW
	}

	// Constructors
	public BaVertexH3D () {
		super (0.0, 0.0, 0.0);
		name = "";
	} // BaVertex

	public BaVertexH3D (double x, double y, double z) {
		super (x, y, z);
		name = "";
	} // BaVertex

	public BaVertexH3D (String name, double x, double y, double z) {
		super (x, y, z);
		this.name = name;
	} // BaVertex

	public BaVertexH3D (BaVertexH3D v) {
		super (v.x(),v.y(),v.z());
		this.name = v.name();
	}
	// Methods

	public String name () {
		return this.name;
	}

	public void print (BaRenderingEngine RE, refCoordSys refSys) {
		switch (refSys) {
		case inLOCAL:
			printInLocalCSys (RE);
			break;
		case inGLOBAL:
			printInGlobalCSys (RE);
			break;
		case inCAMERA:
			printInCameraCSys (RE);
			break;
		case inWINDOW:
			printInWindowCSys (RE);
			break;
		}
	}

	public void printInLocalCSys (BaRenderingEngine Re) {
		print();
	}

	public void printInGlobalCSys (BaRenderingEngine RE) {
		// apply the currently set transformations to the (local) vector
		// ooordinates s.t. the vertex is presented with respect to the 
		// global (world) coordinate system
		BaVector tmpv = RE.WorldMatrix ().times (this);
		tmpv.print();
	} // printInGlobalCSys

	public void printInCameraCSys (BaRenderingEngine RE) {
		// apply the currently set transformations to the (camera) vector
		// coordinates s.t. the vertex is presented with respect to the 
		// camera coordinate system
		BaVector tmpv = RE.ModelviewMatrix ().times (this);
		tmpv.print();
	} // printInCameraCSys

	public void printInWindowCSys (BaRenderingEngine RE) {
		// apply the currently set transformations to the 2D window vector
		// coordinates s.t. the vertex is presented with respect to the 
		// window coordinate system
		BaVertexH3D tmpv = RE.ProjectionMatrix().times (RE.ModelviewMatrix ().times (this));
		BaVertexH3D tmpvv = RE.transformToWindow (tmpv);
		tmpvv.print(2);
	} // printInWindowCSys	
	
	static public String refStr (refCoordSys inRefSys) {
		switch (inRefSys) {
			case inLOCAL:  return "local";
			case inGLOBAL: return "global";
			case inCAMERA: return "camera";
			case inWINDOW: return "window";
			default:       return "";
		}
	}

} // class Vertex
