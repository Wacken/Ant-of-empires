package BaGeoObjects;

import BaMath.*;
import BaGeoSceneGraph.*;
import BaMain.BAG;

public class BaCamera 
	extends BaSceneGraphNode 
{
	/* BaCamera describes the virtual camera with respect to which the
	 * virtual world is rendered (viewed).
	 * It is described by a pyramid stump emanating from the focal point
	 * of the camera - which is defined by the geometric pose (position 
	 * and orientation) of the camera.
	 * 
	 *                         ______________
	 *                      .°/           . /
	 *                    .° /        . °  /
	 *                  ________ . °      /
	 *                 /   /    /        /    pyramid
	 *                /   /____/________/     stump
	 *       |       /_._°____/  .   °
	 *       |/
	 *       @----- 
	 *  camera pose
	 *  (at focal point)
	 * 
	 */
	/* BaCamera is a subclass of BaSceneGraphNode. 
	 */
	/* GEOMETRY: BaCamera inherits all geometric properties of
	 * BaSceneGraphNode regarding the camera pose such that it can be 
	 * moved around (rotated, translated) in the the virtual world. Its 
	 * current pose with respect to the global world coordinate system 
	 * is represented by the local transformation matrix t (inherited 
	 * from BaSceneGraphNode).
	 * 
	 * In addition to these inherited geometric transformation 
	 * properties, BaCamera has variables to define the viewing 
	 * frustum of the camera (the shape of the pyramid stump)
	 * - viewing angle: the (vertical) angle between the top and bottom
	 *   planes of the viewing frustum (pyramid stump)
	 * - aspect: the length ratio between the horizontal (width) and 
	 *   vertical (height) sides of the pyramid stump
	 * - near: the near clipping plane (front part of the pyramid stump)
	 * - far: the far clipping plane (back part of the pyramid stump)
	 * These parameters are used to calculate the perspective projection
	 * transformation (from world coordinates to pixels in the image plane
	 * of the camera)
	 */
	/* POSITION IN THE SCENE GRAPH: the BaCamera is typically defined as
	 * a direct child of the root of the scene graph, existing as a sibling
	 * to recursive trees of nodes defining virtual objects in the scene.
	 * 
	 * Yet, there could also be several cameras, attached to various
	 * objects in the scene and each providing their own individual view
	 * of the world.
	 */
	
	// Variables
	private double viewingAngle;
	private double aspect;
	private double near;
	private double far;

	// Constructor
	public BaCamera (
			String name, 
			double viewingAngle, double aspect, double near, double far) {
		super (name);		
		this.viewingAngle = viewingAngle;
		this.aspect = aspect;
		this.near = near;
		this.far = far;
		identity();
	} // constructor BaCamera

	public BaCamera (
			String name, 
			double viewingAngle, double aspect, double near, double far,
			BaVertexH3D eyePos, BaVertexH3D ctrPos, BaVectorH3D upVec) {
		super (name);		
		this.viewingAngle = viewingAngle;
		this.aspect = aspect;
		this.near = near;
		this.far = far;
		identity();
		
		lookAt (eyePos, ctrPos, upVec);
	} // constructor BaCamera

    // Getters and Setters
	public double viewingAngle() { return this.viewingAngle; }
	public double aspect() { return this.aspect; }
	public double near() { return this.near; }
	public double far() { return this.far; }

	// Methods	
	public void lookAt (
			BaVertexH3D eyePos, BaVertexH3D ctrPos, BaVectorH3D upVec) {
		t.lookAt (eyePos, ctrPos, upVec);
	} // lookAt  
} // class BaCamera
