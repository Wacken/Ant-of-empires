package BaMath;

public class BaVector
{
	// Variables
	protected double [] v;
	protected int       elems;

	// Constructor
	public BaVector (int elems) {
		this.elems = elems;
		v =          new double [elems];
		init ();
	} // BaVector

	// Methods

	public void init () {
		for (int e=0; e<elems; e++) {
			v[e] = 0.0;
		}
	} // init

	// Getters and Setters
	public int elements () { return elems; }
	public double val (int e) {	return v[e]; }
	public void setVal (int e, double val) { v[e] = val; }
	public void set3 (double v0, double v1, double v2) {
		v[0] = v0;
		v[1] = v1;
		v[2] = v2;
	} // set3
	public void set4 (double v0, double v1, double v2, double v3) {
		v[0] = v0;
		v[1] = v1;
		v[2] = v2;
		v[3] = v3;
	} // set4

	public void normalize () {
		normalize (elems);
	} // normalize

	public void normalize (int n) {
	// normalize only the first n elements
	// this is useful for handling homogeneous vectors (hVector3D)
		if (n > elems) n = elems;
		double len=0;
		for (int i=0; i<n; i++) {
			len += v[i] * v[i];
		}
		len = Math.sqrt(len);
		for (int i=0; i<n; i++) {
			v[i] /= len;
		}		
	} // normalize

	public BaVector times (BaMatrix mat) { // result = this * mat
		BaVector tmpv = new BaVector (elems);	
		// this.elems must be equal to mat.m!!
		if (elems != mat.rows ()) {
			// error handling
			System.out.format 
			("vector-matrix multiply: incompatible sizes: %d, %d\n", 
					elems, mat.rows ());
			return (tmpv);
		}
		for (int e=0; e<elems; e++) {
			tmpv.setVal (e, 0.0);
			for (int r=0; r<mat.rows (); r++) {
				tmpv.setVal (e, tmpv.val(e) + v[r] * mat.val (r, e));
			}
		}
		return tmpv;
	} // times

	public BaVector plus (BaVector v) {
		return plus (v, elems);
	} // plus

	public BaVector plus (BaVector v, int n) {
		if (elems < n) 
			n = elems;
		if (n != v.elems) {
			// ...error message...
		}
		BaVector rvec = new BaVector (elems);
		for (int i=0; i<n; i++) {
			rvec.v[i] = this.v[i] + v.v[i];
		}
		return rvec;
	} // plus

	public BaVector minus (BaVector v) {
		return minus (v, elems);
	} // minus

	public BaVector minus (BaVector v, int n) {
		if (elems < n) 
			n = elems;
		if (n != v.elems) {
			// ...error message...
		}
		BaVector rvec = new BaVector (elems);
		for (int i=0; i<n; i++) {
			rvec.v[i] = this.v[i] - v.v[i];
		}
		return rvec;
	} // minus

	public int print () {	
		return print (elems);
	} // print
	
	public int print (int n) {	
		if (n > elems) n = elems;
		System.out.print ("("); 
		int cnt = 1;
		for (int e=0; e<n; e++) {
			System.out.format ("%4.1f", v[e]);
			cnt +=4;
			if (e<elems-1) {
				System.out.print (" ");
				cnt++;
			}
		}
		System.out.print(")");	
		cnt++;
		return cnt;
	} // print

} // class BaVector

