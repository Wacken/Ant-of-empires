package BaRenderer;

import BaMath.*;

/* **************************************************************************
 * Basic transformation facilities for 3D rendering
 * (similar to concepts of OpenGL)
 *    Superclass BaMatrix defines the core matrix manipulation facilities, 
 *    such as 
 *    - identity ()
 *    - copy (mat)
 *    - gets (mat)            (assignment)
 *    - MyMatrix times (mat)  (matrix-matrix multiplication)
 *    - MyVector times (vec)  (matrix-vector multiplication)
 *    
 *    Transformations are performed by
 *    1)constructing a temporary matrix tmp representing a basic transformation
 *      (see any graphics book) and
 *    2)multiplying it from the right onto a permanent matrix M to accumulate
 *      it with all previously applied transformations (stored in M):
 *      acc = M * tmp
 *    3) M = acc
 *    
 *    thus: M = M * tmp
 *    
 *    Thus, new transformations are always appended ON THE RIGHT SIDE 
 *    (right-multiplied)
 * *************************************************************************/

public class BaTransformation
	extends BaMatrix
{
	// Variables
	// use array M of the superclass BaMatrix to accumulate transformations
	
	// Constructors
	public BaTransformation() {
		super (4,4);
		identity();
	} // BaTransformation
	
	public BaTransformation (BaTransformation T) {
		super (4,4);
		copy (T);
	} // BaTransformation

	// Methods
			
	public void translate (double tx, double ty, double tz) {
		BaMatrix tmp = new BaMatrix (4,4);	// temporary basic matrix for translation	
		tmp.setRow (0,   1, 0, 0, tx);
		tmp.setRow (1,   0, 1, 0, ty);
		tmp.setRow (2,   0, 0, 1, tz);
		tmp.setRow (3,   0, 0, 0,  1);
		this.gets (this.times (tmp));			// M = M x tmp
	} // translate
	
	public void scale (double sx, double sy, double sz) {
		BaMatrix tmp = new BaMatrix (4,4);   // temporary basic matrix for scaling
		tmp.setRow (0,   sx,  0,  0, 0);
		tmp.setRow (1,    0, sy,  0, 0);
		tmp.setRow (2,    0,  0, sz, 0);
		tmp.setRow (3,    0,  0,  0, 1);	
		this.gets (this.times (tmp));			// M = M x tmp
	} // scale

	public void rotate (char axis, double angle) {
		// angle given in radians		
		BaMatrix tmp = new BaMatrix (4,4);   // temporary matrix for basic rotation				
		double cosA = Math.cos (angle);
		double sinA = Math.sin (angle);
		switch (axis) {
		case 'x':
			tmp.setRow (0,   1,  0,     0,   0);
			tmp.setRow (1,   0, cosA, -sinA, 0);
			tmp.setRow (2,   0, sinA,  cosA, 0);
			tmp.setRow (3,   0,  0,     0,   1);
			break;
		case 'y':
			tmp.setRow (0,   cosA, 0, sinA, 0);
			tmp.setRow (1,    0,   1,  0,   0);
			tmp.setRow (2,  -sinA, 0, cosA, 0);
			tmp.setRow (3,    0,   0,  0,   1);
			break;
		case 'z':
			tmp.setRow (0,   cosA, -sinA, 0, 0);
			tmp.setRow (1,   sinA,  cosA, 0, 0);
			tmp.setRow (2,    0,     0,   1, 0);
			tmp.setRow (3,    0,     0,   0, 1);
			break;
		}
		this.gets (this.times (tmp));			// M = M x tmp
	} // rotate
	
	public void rotate (double angle, double x, double y, double z) {
		// angle given in radians
		// see web site www.talisman.org/opengl-1.1/Reference/glRotate.html
		// or http://en.wikipedia.org/wiki/Rotation_matrix

		BaVector v = new BaVector (3);
		v.set3 (x, y, z);
		v.normalize ();
		x = v.val(0);
		y = v.val(1);
		z = v.val(2);		
		
		double c = Math.cos (angle);
		double s = Math.sin (angle);
		
		double xx = x*x;
		double xy = x*y;
		double xz = x*z;
		double yy = y*y;
		double yz = y*z;
		double zz = z*z;
		
		double xs = x*s;
		double ys = y*s;
		double zs = z*s;
	
		BaMatrix tmp = new BaMatrix (4,4);   // temporary matrix for basic rotation		
		tmp.setRow (0,   xx*(1-c)+c,  xy*(1-c)-zs, xz*(1-c)+ys, 0);
		tmp.setRow (1,   xy*(1-c)+zs, yy*(1-c)+c,  yz*(1-c)-xs, 0);
		tmp.setRow (2,   xz*(1-c)-ys, yz*(1-c)+xs, zz*(1-c)+c,  0);
		tmp.setRow (3,   0,           0,           0,           1);

		this.gets (this.times (tmp));			// M = M x tmp
	} // rotate
		
	public void perspective_projection (double fov, double aspect, double n, double f) {
		double t = Math.tan (fov/2.0) * n;
		double b = -t;
		double l = aspect*b;
		double r = aspect*t;
		frustum (l, r, b, t, n, f);
	}  // perspective_projection

	public void frustum (double l, double r, double b, double t, double n, double f) {
	// note: inverted z-axis - on purpose, due to negative components 
	// in the 3. column (inverting the z-axis; resulting in a left-hand coordsys)
		BaMatrix tmp = new BaMatrix (4,4); // temporary projection matrix		
		tmp.setRow (0,   2*n/(r-l),    0,       (r+l)/(r-l),         0     );
		tmp.setRow (1,      0,      2*n/(t-b),  (t+b)/(t-b),         0     );
		tmp.setRow (2,      0,         0,      -(f+n)/(f-n), -(2*f*n)/(f-n));
		tmp.setRow (3,      0,         0,           -1,              0     );
		this.gets (this.times (tmp));			// M = M x tmp
	} // frustum

	public void orthographic_projection 
	    (double l, double r, double b, double t, double n, double f) {
		BaMatrix tmp = new BaMatrix (4,4); // temporary projection matrix		
		tmp.setRow (0,   2/(r-l),    0,       0,    -(r+l)/(r-l));
		tmp.setRow (1,      0,    2/(t-b),    0,    -(t+b)/(t-b));
		tmp.setRow (2,      0,       0,    2/(f-n), -(f+n)/(f-n));
		tmp.setRow (3,      0,       0,       0,            1   );		
		this.gets (this.times (tmp));			// M = M x tmp
	} // orthographic_projection

	public void viewport (int x, int y, int w, int h) {
		BaMatrix tmp = new BaMatrix (4,4);  	
		tmp.setRow(0,    w/2, 0,  0, w/2+x);   // only x and y components needed; 	
		tmp.setRow(1,     0, h/2, 0, h/2+y);   // z component is not needed, and
		tmp.setRow(2,     0,  0,  1,    0 );   // thus 0 0 1 0 in 3. row and 3. column
		tmp.setRow(3,     0,  0,  0,    1 );
		this.gets (this.times (tmp));			// M = M x tmp
	} // viewport

	public void lookAt (BaVertexH3D eyePosition3D, BaVertexH3D center3D, BaVectorH3D upVector3D) {	
		// calculate the vector "forward" from the coordinate "center" to the "eye position"
		BaVectorH3D forward;
		forward = center3D.minus (eyePosition3D);
		forward.normalize (3);
		
		// calculate a horizontal vector "side" (perpendicular to "up")
		BaVectorH3D side;
		side = forward.cross (upVector3D);
		side.normalize (3);
	
		// calculate new "up"-vector
		// rotate initial "upVector" around "side"-axis (to become perpendicular to "forward")
		BaVectorH3D up;
		up = side.cross (forward);
	
		// create a new rotated coordinate system that is rotated such that
		// local x-axis = side
		// local y-axis = up
		// local z-axis = forward
		BaTransformation R = new BaTransformation();
		R.identity();
		for (int r=0; r<3; r++) {
			R.M[r][0] = side.val(r);
			R.M[r][1] = up.val(r);
			R.M[r][2] = -forward.val(r);
		}

		// translate new coordinate system at eye center
		// note: translation must be performed before rotation
		// thus _M = I x T x R
		this.identity();
		// translate to eye position
		this.translate (eyePosition3D.val(0), eyePosition3D.val(1), eyePosition3D.val(2));
		// rotate to align new z-axis with forward vector, while keeping x-axis horizontal
		this.gets (this.times (R));
	} // lookAt

} // class BaTransformation
