package BaGeoSceneGraph;
import java.awt.Color;

import  BaGeoObjects.*;
import  BaMain.*;
import  BaMath.*;
import  BaRenderer.*;

public class BaSceneGraphNode
{
	/* GEOMETRY: This class provides the facilities to place geometric 
	 * objects into a world (scene graph). It defines a geometric object as 
	 * well as the operations to translate, scale and rotate it, accumulating 
	 * all applied transformations in the local transformation matrix t. 
	 * Upon request, it returns the current pose, as well as the inversePose. 
	 */
	/* TREE-BASED RECURSIVE DATA STRUCTURE: This class defines the root node
	 * of a tree of further geometric objects which depend on the pose of the
	 * object described in this node. To this end, this class is described as 
	 * a "BaSceneGraphNode" that contains a list of children which are also 
	 * of type "BaSceneGraphNode" and can thus generate a recursive data 
	 * structure (a tree).
	 */
	/* CLASS HIERARCHY: This class is meant to be used as a superclass
	 * (describing the properties of a scene graph node that are common to 
	 * all geometric objects). It does NOT describe the specific shape of the 
	 * object.
	 * Subclasses will extend this "BaSceneGraphNode" to describe specific
	 * geometric properties of objects in the scene graph. They inherit t 
	 * (as well as a list of children) and the methods to manipulate the 
	 * geometric object.
	 * Examples: see packages BaGeoObjects, BaGeoWorlds, and 
	 * BaGeoWorlds.BaRobotWorld
	 */

	// Variables
	protected String				name;
	protected BaTransformation		t;
	protected BaSceneGraphNode[]	child;
	protected int					children;
	protected BaCoordSys			coordSys;		

	// Constructor
	public BaSceneGraphNode (String name) {
		this.name	= name;
		t			= new BaTransformation();
		child		= null;
		children	= 0;
		coordSys	= null;
	}

	// Getters and Setters
	public BaTransformation pose ()				{ return t; }	
	public BaMatrix inversePose ()				{ return t.inverse(); } 
	public void setPose (BaTransformation t)	{ this.t.copy(t); }

	// Methods
	public void addChildNode (BaSceneGraphNode node) {		
		// create a new, temporary list of children
		// with one element more than the old list
		BaSceneGraphNode [] tmp = new BaSceneGraphNode[children+1];
		
		// copy old list of children into the temporary list
		for (int i=0; i<children; i++) 
			tmp[i] = child[i];
		
		// copy new element at the end of the list
		tmp[children] = node;
		
		// copy the temporary list into the children's list of BaSceneGraphNode
		child = tmp;
		children++;
	}
	
	public void identity () { 
		t.identity(); 
	}
	public void translate (double tx, double ty, double tz) { 
		t.translate( tx, ty, tz ); 
	}
	public void scale (double sx, double sy, double sz) { 
		t.scale( sx, sy, sz );
	}
	public void rotate (char axis, double angle) { 
		t.rotate( axis, angle ); 
	}
	public void rotate (double angle, double x, double y, double z)	{ 
		t.rotate( angle, x,y,z ); 
	}
	
	public void render (BaRenderingEngine RE) {	
		// save current state of the graphics context: ModelviewMatrix
		RE.pushModelviewMatrix(); 
		RE.pushWorldMatrix(); 
		
		// apply current transformation to ModelviewMatrix of RenderingEngine
		RE.ModelviewMatrix().gets( RE.ModelviewMatrix().times(t) ); 
		RE.WorldMatrix().gets( RE.WorldMatrix().times(t) ); 

			// render polygons of the current node
			renderYourself (RE);
		
			// recursively render all children
			renderChildren (RE);
		
			// render the local coordinate system (if so requested)
			if (BAG.showCoordSys && coordSys != null) 
				coordSys.render( RE );

		// restore previous state graphics context: ModelviewMatrix
		RE.popModelviewMatrix(); 
		RE.popWorldMatrix(); 
	}	
	public void renderYourself (BaRenderingEngine RE) {
		// to be overwritten and fleshed out by subclasses
	}
	public void renderChildren (BaRenderingEngine RE) {
		// to be overwritten and fleshed out by subclasses
		for (int i=0; i<children; i++) { 
			child[i].render( RE ); 
		}
	}

	// a more complex version of the renderer, 
	// saving, setting and restoring a more elaborate graphics context
	// (color, filled)
	public void render (BaRenderingEngine RE, Color color, Boolean filled) {
		RE.pushColor();
		RE.pushFilled();

		RE.setColor(color);
		RE.setFilled(filled);
		render(RE);

		RE.pushFilled();
		RE.popColor();
	} // render

	
	
	public void print (BaRenderingEngine RE, 
			String leadstr, BaVertexH3D.refCoordSys inRefSys) {
		// save current state of the graphics context: ModelviewMatrix
		RE.pushModelviewMatrix();
		RE.pushWorldMatrix();
		
		// apply current transformation to ModelviewMatrix of RenderingEngine
		RE.ModelviewMatrix().gets( RE.ModelviewMatrix().times(t) );
		RE.WorldMatrix().gets( RE.WorldMatrix().times(t) );

		// print data of the current node
		printYourself (RE, leadstr, inRefSys);
			
		// recursively print all children
		printChildren (RE, leadstr, inRefSys);

		// restore previous state of the graphics context: ModelviewMatrix
		RE.popModelviewMatrix();		
		RE.popWorldMatrix();		
	}
	public void printYourself (BaRenderingEngine RE, 
			String leadstr, BaVertexH3D.refCoordSys inRefSys) {
		// as a default, print only the node name
		System.out.println( leadstr + name );
		// to be overwritten and fleshed out by subclasses
	}
	public void printChildren (BaRenderingEngine RE,
			String leadstr, BaVertexH3D.refCoordSys inRefSys) {
		// to be overwritten and fleshed out by subclasses
		for (int i=0; i<children; i++) { child[i].print( RE, leadstr+"  " , inRefSys); }
	}
	public void printVecVals (BaRenderingEngine RE, BaVertexH3D [] v,
			String leadstr, BaVertexH3D.refCoordSys inRefSys,
			int elemsPerRow) {
		int cnt = 0;
		for (int i=0; i<v.length; i++) {
			if (cnt==0) System.out.print(leadstr + "  ");
			v[i].print(RE,inRefSys);
			cnt++;
			if (cnt >= elemsPerRow) {
				System.out.println();
				cnt = 0;
			}
		}
		if (cnt >0) System.out.println();
	}
	
	
	public void setFilled (boolean f) {
		for (int i=0; i<children; i++) { child[i].setFilled(f); }	
	}	

	// functions to be fleshed out by subclasses 
	public void simulateNextStep () {}
	public BaSceneGraphNode selectObject (int n) { return null; }
	public BaSceneGraphNode selectObject (String name) { return null; }
} // class BaSceneGraphNode
