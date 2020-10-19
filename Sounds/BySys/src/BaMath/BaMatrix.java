package BaMath;

/* **************************************************************************
 * Basic math: linear algebra (4x4 matrices and 4-element vectors
 *       intentionally left simple
 *       
 * (instead of this home-grown implementation,
 * see  the JAMA package http://math.nist.gov/javanumerics/jama/
 * for a more professional solution)
 * *************************************************************************/

public class BaMatrix 
{
	// Variables
	public double [][] M;
	private int        m,n;
		
	// Constructor
	public BaMatrix (int rows, int cols) {
		m = rows;
		n = cols;
		M = new double [m][n];
		identity();		
	} // BaMatrix
		
	// Getters and Setters
	public double[][] M () { return this.M; }	
	public int rows () { return this.m; }	
	public int cols () { return this.n; }	
	public double val (int r, int c) { return M[r][c]; }
	public void setVal (int r, int c, double val) { M[r][c] = val; }	
	public void setRow (int r, double v0, double v1, double v2, double v3) {
		M[r][0] = v0;
		M[r][1] = v1;
		M[r][2] = v2;
		M[r][3] = v3;
	} // setRow

	public void gets (BaMatrix mat) {
		copy (mat);
	} // equals
	
	public void copy (BaMatrix mat) {
		for (int r=0; r<m; r++) {
			for (int c=0; c<n; c++) {
				this.M[r][c] = mat.M[r][c];
			}
		}
	} // copy
	
	public void identity () {
		for (int r=0; r<m; r++) {
			for (int c=0; c<n; c++) {
				this.M[r][c] = 0.0;
			}
		}	
		int rank;
		if (m<=n) rank=m; else rank=n;
		for (int r=0; r<rank; r++) {
			this.M[r][r] = 1.0;
		}
	} // identity
			
	public BaMatrix times (BaMatrix mat) { // result = this * mat		
		BaMatrix tmp = new BaMatrix (m,mat.n);			
		// _n must be equal to mat._m !!
		if (n != mat.m) {
			// error handling
			System.out.format
				("matrix-matrix multiply: incompatible matrix sizes: %d, %d",
				n, mat.m);
			return (tmp);
		}
		for (int r=0; r<m; r++) {
			for (int c=0; c<mat.n; c++) {
				// multiply elements or this.row r with elements of mat.column c
				tmp.M[r][c] = 0.0;
				for (int i=0; i<m; i++) {
					tmp.M[r][c] += this.M[r][i] * mat.M[i][c];
				}
			}
		}	
		return tmp;
	} // times		
	
	public BaVector times (BaVector v) { // result = this * v
		BaVector tmpv = new BaVector (4);	
		// this.n must be equal to v.elems!!
		if (n != v.elements ()) {
			// error handling
			System.out.format 
				("matrix-vector multiply: incompatible sizes: %d, %d\n", 
				n, v.elements ());
			return (tmpv);
		}
		for (int e=0; e<v.elements (); e++) {
			tmpv.setVal (e,0.0);
			for (int c=0; c<n; c++) {
				tmpv.setVal (e, tmpv.val (e) + this.M[e][c] * v.val (c));
			}
		}
		return tmpv;		
	} // times

	public BaVectorH3D times (BaVectorH3D v) { // result = this * v
		BaVectorH3D tmpv = new BaVectorH3D ();	
		// this.n must be equal to v.elems!!
		if (n != v.elements ()) {
			// error handling
			System.out.format 
				("matrix-vector multiply: incompatible sizes: %d, %d\n", 
				n, v.elements ());
			return (tmpv);
		}
		for (int e=0; e<v.elements (); e++) {
			tmpv.setVal (e,0.0);
			for (int c=0; c<n; c++) {
				tmpv.setVal (e, tmpv.val (e) + this.M[e][c] * v.val (c));
			}
		}
		return tmpv;		
	} // times

	public BaVertexH3D times (BaVertexH3D v) { // result = this * v
		BaVertexH3D tmpv = new BaVertexH3D ();		
		// this.n must be equal to v.elems!!
		if (n != v.elements ()) {
			// error handling
			System.out.format 
				("matrix-vector multiply: incompatible sizes: %d, %d\n", 
				n, v.elements ());
			return (tmpv);
		}
		for (int e=0; e<v.elements (); e++) {
			tmpv.setVal (e,0.0);
			for (int c=0; c<n; c++) {
				tmpv.setVal (e, tmpv.val (e) + this.M[e][c] * v.val (c));
			}
		}
		return tmpv;		
	} // times

	public BaMatrix inverse () {
		// must be a square matrix
		if (m != n) {
			// error message
			//return ;
		}
		
		// Gaussian elimination process
		BaMatrix tmp = new BaMatrix (m,n);
		tmp.copy(this);
		BaMatrix inv = new BaMatrix (m,n);
		inv.identity();
		double val;
		
		// check diagonal elements: must not be 0! (otherwise: div by 0)
		for (int r=0; r<m; r++) {
			if (tmp.M[r][r] == 0) {
				// if the diagonal element is 0, add another row with a 
				// non-zero element in this column
				for (int rr=0; rr<m; rr++) {
					if (tmp.M[rr][r] != 0) {
						// add row rr to row r --- s.t. the diagonal element 
						// becomes non-zero
						addScaledRow1ToRow2 (tmp,rr,r,1.0);
						addScaledRow1ToRow2 (inv,rr,r,1.0);
						break;
					} // if tmp rr... != 0
				} // for rr
			} // if tmp... == 0
		} // for r
		
		// turn lower left triangular matrix elements into 0 and diagonal elements into 1
		for (int r=0; r<m; r++) {
			for (int c=0; c<r; c++) {
				// for rows 1.._m  (not for row 0)
				// turn element [r][c] to 0 by subtracting a multiple 
				// of the row above that has the diagonal element 1
				val = tmp.M[r][c]/tmp.M[c][c];  
				addScaledRow1ToRow2 (tmp,c,r,-val);
				addScaledRow1ToRow2 (inv,c,r,-val);
			} // for c
			// turn diagonal element into 1
			val = tmp.M[r][r];
			divideRowBy (tmp,r,val);
			divideRowBy (inv,r,val);
		} // for r
		
		// turn upper right triangular matrix elements into 0
		for (int r=m-2; r>=0; r--) {
			for (int c=n-1; c>r; c--) {
				val = tmp.M[r][c]/tmp.M[c][c];  
				addScaledRow1ToRow2 (tmp,c,r,-val);
				addScaledRow1ToRow2 (inv,c,r,-val);
			} // for c			
		} // for r
		
		return inv;
	} // inverse
	
	public void print () {
		print ("");
	} // print
	
	public void print (String name) {
		System.out.println ("matrix " + name + ":");
		
		for (int r=0; r<m; r++) {
			System.out.print("  ");
			for (int c=0; c<n; c++) {
				//System.out.format("%7.2f",M[r][c]);
				System.out.format("%11.6f",M[r][c]);
				if (c < n-1) System.out.print (" ");
			}
			System.out.println ("");
		}
	} // print
	
	// Helper methods
	
	private void divideRowBy (BaMatrix mat, int row, double val) {
		for (int c=0; c<n; c++) {
			mat.M[row][c] = mat.M[row][c] / val;
		}
	} // divideRowBy
	
	private void addScaledRow1ToRow2 (BaMatrix mat, int row1, int row2, double val) {
		for (int c=0; c<n; c++) {
			mat.M[row2][c] = mat.M[row2][c] + val*mat.M[row1][c];
		}
	} // addScaledRow1ToRow2
} // class BaMatrix
