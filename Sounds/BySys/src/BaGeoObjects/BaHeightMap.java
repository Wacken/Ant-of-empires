package BaGeoObjects;

import java.awt.Color;

import BaRenderer.*;
import BaGeoSceneGraph.*;
import BaMath.BaVertexH3D;

public class BaHeightMap
	extends BaSceneGraphNode {

	// Variables
	private double [][] h;
	private int         m, n;
	private double      maxheight;
	private double      minheight;
	private double      heightrange;

	// Constructor
	public BaHeightMap (String name) {
		super (name);
		h = null;
		m = 0;
		n = 0;
	} // BaHeightMap

	// Methods

	public void setHeights (double [][] h, int m, int n) {		
		this.h = new double[m][n];
		this.m = m;
		this.n = n;
		maxheight = minheight = h[0][0];
		for (int i=0; i<m; i++) {
			for (int j=0; j<n; j++) {
				this.h[i][j] = h[i][j];
				minheight = Math.min (minheight, h[i][j]); 
				maxheight = Math.max (maxheight, h[i][j]);
			}
		}
		heightrange = maxheight - minheight;
	} // setHeights
	
	public void renderYourself (BaRenderingEngine RE) {
		for (int i=0; i<m-1; i++) {
			// create a quadstrip for rows i and i+1
			BaQuadStrip qstrip = new BaQuadStrip("");
			double [][] qpnts = new double[n*2][3]; 
			for (int j=0; j<n; j++) {
				qpnts[j*2][0] = i;
				qpnts[j*2][1] = j;
				qpnts[j*2][2] = h[i][j];
				qpnts[j*2+1][0] = i+1;
				qpnts[j*2+1][1] = j;
				qpnts[j*2+1][2] = h[i+1][j];
			}
			Color [] qcolors = new Color[n-1];
			for (int j=0; j<n-1; j++) {
				double h0 = (h[i][j] + minheight)/heightrange;
				double h1 = (h[i][j+1] + minheight)/heightrange;
				double h2 = (h[i+1][j] + minheight)/heightrange;
				double h3 = (h[i+1][j+1] + minheight)/heightrange;
				double avg = (h0+h1+h2+h3)/4.0;
				int ival = (int) (avg * 255);
				qcolors[j] = new Color(ival,ival,ival);
			}			
			qstrip.setVertices(qpnts);
			qstrip.setColors(qcolors);
			qstrip.render(RE);	
		}
	}

	public void printYourself (BaRenderingEngine RE, 
			String leadstr, BaVertexH3D.refCoordSys inRefSys) {
		// some general information in the title line
		System.out.println (
				leadstr + name + 
				": heightmap " + 
				" (" + BaVertexH3D.refStr (inRefSys) + " coordinate system): ");
		
		int cnt = 0;
		BaVertexH3D v;		
		for (int i=0; i<m; i++) {
			System.out.print(leadstr+"  ");
			for (int j=0; j<n; j++) {
				v = new BaVertexH3D ("v",(double)i,(double)j,h[i][j]);
				v.print(RE,inRefSys);
			}
			System.out.println();
		}
		System.out.println();
	}	
	
} // class BaHeightMap
