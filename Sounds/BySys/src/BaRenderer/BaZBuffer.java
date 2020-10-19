package BaRenderer;

/* **************************************************************************
 * See the explanations in class BaFrameBuffer
 * *************************************************************************/

public class BaZBuffer {
	private double [][]	z;
	private double		initVal = 1.0;
	private int			width = 0;
	private int			height = 0;
	
	// constructor
	BaZBuffer (int w, int h) {
		setSize(w,h);
		initZ();
	}
	
	void initZ () {
		for (int row=0; row<height; row++) {
			for (int col=0; col<width; col++) {
				z[row][col] = initVal;
			}
		}
	}
	
	void setSize (int w, int h) {
		width = w;
		height = h;
		z = new double[height][width];
	}
	
	double z(int xi, int yi) {
		return z[yi][xi];
	}
	
	void setz(int xi, int yi, double zval) {
		z[yi][xi] = zval;	
	}
	
	int width() {
		return width;
	}
	int height() {
		return height;
	}

}; // class BaZBuffer
