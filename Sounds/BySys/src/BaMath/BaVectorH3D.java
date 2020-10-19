package BaMath;
	
////////////////////////
public class BaVectorH3D
////////////////////////
	extends BaVector 
{
	// Variables

	// Constructors
	public BaVectorH3D () {
			super (4);
	} // BaHVector3D

	public BaVectorH3D (double x, double y, double z) {
		super (4);
		v[0] = x;
		v[1] = y;
		v[2] = z;
		v[3] = 1.0;
	} // BaHVector

	// Getters and Setters
	public double x () { return v[0]; }
	public double y () { return v[1]; }
	public double z () { return v[2]; }

	public void set (double x, double y, double z) {
		v[0] = x;
		v[1] = y;
		v[2] = z;
		v[3] = 1.0;
	} // set

	public void copy (BaVector v) {
		for (int i=0; i<3; i++) {
			this.v[i] = v.v[i];
		}
		this.v[3] = 1.0;
	} // copy

	// Methods

	public void project () {
		double w = v[3];

		if (w == 0.0) {
			// error message
			return;
		}
		if (w != 1.0) {
			v[0] /= w;
			v[1] /= w;
			v[2] /= w;
			v[3] = 1.0;
		}
	} // project

	public void normalize () {
		project ();
		super.normalize (3);
	} // normalize

	public BaVectorH3D plus (BaVectorH3D v) {
		project();
		v.project();
		
		BaVectorH3D result = (BaVectorH3D) super.plus(v,3);
		result.v[3] = 1.0;
		return result;
	} // plus

	public BaVectorH3D minus (BaVectorH3D v) {
		project();
		v.project();

		BaVector result = super.minus(v,3);
		BaVectorH3D hResult3D = new BaVectorH3D();
		hResult3D.copy (result);
		hResult3D.v[3] = 1.0;
		return hResult3D;
	} // minus

	public double dot (BaVectorH3D v) {
		double val = 0;

		project ();
		v.project ();
		for (int i=0; i<3; i++) {
			val += this.v[i] * v.v[i];
		}
		return val;
	} // dot

	public BaVectorH3D cross (BaVectorH3D v) {
		int n = elems;
		if (n != v.elems) {
			// ...error message
		}

		project();
		v.project();

		double x1 = this.v[0],   x2 = v.v[0];
		double y1 = this.v[1],   y2 = v.v[1];
		double z1 = this.v[2],   z2 = v.v[2];

		BaVectorH3D rvec = new BaVectorH3D ();
		rvec.v[0] = y1*z2 - z1*y2;
		rvec.v[1] = z1*x2 - x1*z2;
		rvec.v[2] = x1*y2 - y1*x2;
		rvec.v[3] = 1.0;

		return rvec;
	} // cross
	
	public int print () {	
		return print (3);
	} // print

} // class BaHVector3D

