package BaGeoObjects;

import java.awt.Color;

import BaRenderer.*;
import BaGeoSceneGraph.*;
import BaMath.*;
import BaMain.*;

public class BaCuboid
	extends BaSceneGraphNode 
{
	/* The cuboid consists of 8 corners (vertices)
	 * 
	 *            v011----------v111
	 *           /  |          /  |
	 *          /   |         /   |
	 *       v010----------v110   |
	 *         |    |        |    |
	 *         |    |        |    |
	 *         |  v001-------|--v101
	 *         |  /          |  /
	 *         | /           | /
	 *       v000----------v100
	 * 
	 *    y
	 *    |   z
	 *    |  /
	 *    | /
	 *    o---- x
	 */
	
	// Variables
	private double  w;
	private double  h;
	private double  d;
	private boolean filled = true;

	// corner vertices
	BaVertexH3D 	
			v000, v100,   v010, v110, // front face (z=0)		
			v001, v101,   v011, v111; // back face  (z=1)
	
	// Constructor	
	public BaCuboid (String name, double w, double h, double d) {
		super( name );
		createCuboid (name, w, h, d, true);
	}

	public BaCuboid (String name, double w, double h, double d, boolean drawTriangles) {
		super( name );
		createCuboid (name, w, h, d, drawTriangles);
	}
	
	private void createCuboid (String name, double w, double h, double d, boolean drawTriangles) {
		this.w = w;
		this.h = h;
		this.d = d;
		
		double o = 0.0;
		v000 = new BaVertexH3D("v000", o,o,o );
		v100 = new BaVertexH3D("v100", w,o,o );
		v010 = new BaVertexH3D("v010", o,h,o );
		v110 = new BaVertexH3D("v110", w,h,o );

		v001 = new BaVertexH3D("v001", o,o,d );
		v101 = new BaVertexH3D("v101", w,o,d );
		v011 = new BaVertexH3D("v011", o,h,d );
		v111 = new BaVertexH3D("v111", w,h,d );

		// create the 6 surfaces of the cube and describe them 
		// as 3 triangle strips
		children = 3;
		child = new BaSceneGraphNode[children];
		coordSys = new BaCoordSys( name );
		
		/* strip going across the 4 side surfaces
		 * (the 4 side surfaces are "unrolled"
		 *            . . . . . . . .
		 *           .  .         . .
		 *          .   .        .  .
		 *       v010---------v110---------v111---------v011---------v010
		 *         # #  .       # # .        # #          # #          #
		 *         #   #.       #   #        #   #        #   #        #
		 *         #   . # . . .#   . #      #     #      #     #      #
		 *         #  .    #    #  .    #    #       #    #       #    #   
		 *         # .       #  # .       #  #         #  #         #  #
		 *       v000---------v100---------v101---------v001---------v000
		 *           front        right        back         left
		 */
		BaVertexH3D [] sideVertices = {
			v000, v010, v100, v110, 
			v101, v111, v001, v011,
			v000, v010
		};
		if (drawTriangles) {
			Color [] sideColors = {
					Color.gray, Color.gray,
					Color.darkGray, Color.darkGray,
					Color.gray, Color.gray,
					Color.darkGray, Color.darkGray
				};
			child[0] = new BaTriangleStrip ("- sides");
			((BaTriangleStrip)child[0]).setVertices(sideVertices);
			((BaTriangleStrip)child[0]).setColors(sideColors);
		}
		else {
			Color [] sideColors = {
					Color.gray,
					Color.darkGray,
					Color.gray,
					Color.darkGray
				};
			child[0] = new BaQuadStrip ("- sides");
			((BaQuadStrip)child[0]).setVertices(sideVertices);
			((BaQuadStrip)child[0]).setColors(sideColors);
			
		}
		
		/* top surface
		 * 
		 *            v011 ------- v111
		 *           #  .##       #  .
		 *          #   .  ##    #   .
		 *       v010 ------- #v110  .
		 *         .    .       .    .
		 *         .    .       .    .
		 *         .    . . . . . . ..
		 *         .  .         .  .
		 *         . .          . .
		 *         . . . . . . ..
		 */
		BaVertexH3D [] topVertices = {
				v010, v011, v110, v111
			};
		if (drawTriangles) {
			Color [] topColors = {
					Color.white, Color.white
					};
			child[1] = new BaTriangleStrip ("- top");
			((BaTriangleStrip)child[1]).setVertices(topVertices);
			((BaTriangleStrip)child[1]).setColors(topColors);
		}
		else {
			Color [] topColors = {
					Color.white
					};
			child[1] = new BaQuadStrip ("- top");
			((BaQuadStrip)child[1]).setVertices(topVertices);
			((BaQuadStrip)child[1]).setColors(topColors);
			
		}
			

		/* bottom surface
		 *              .. . . . . . ..
		 *            . .          . .
		 *           .  .         .  .
		 *          .   .        .   .
		 *         . . . . . .  .    .
		 *         .    .       .    .
		 *         .    .       .    .
		 *         .  v001 ------- v101
		 *         .  #  ##     .  #
		 *         . #     ##   . #
		 *       v000 -------#v100
		 */
		BaVertexH3D [] bottomVertices = {
				v000, v001, v100, v101
			};
		if (drawTriangles) {		
			Color [] bottomColors = {
					Color.black, Color.black
					};
			child[2] = new BaTriangleStrip ("- bottom");
			((BaTriangleStrip)child[2]).setVertices(bottomVertices);
			((BaTriangleStrip)child[2]).setColors(bottomColors);
		}
		else {
			Color [] bottomColors = {
					Color.black
					};
			child[2] = new BaQuadStrip ("- bottom");
			((BaQuadStrip)child[2]).setVertices(bottomVertices);
			((BaQuadStrip)child[2]).setColors(bottomColors);
		}
		// position the cuboid centered around the coordinate system
		for (int i=0; i<children;i++) {
			child[i].translate(-w/2.0, -h/2.0, -d/2.0);
		}
	} // BaCuboid

	// Getters and Setters
	public void setFilled (boolean f) { filled = f; }
	public double w() { return this.w; }
	public double h() { return this.h; }
	public double d() { return this.d; }
	
	// Methods	
	// ... taken from superclass BaSceneGraphNode

} // class BaCuboid
