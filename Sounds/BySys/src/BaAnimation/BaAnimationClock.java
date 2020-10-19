package BaAnimation;

public class BaAnimationClock {

	// Variables
	int t;
	int tstart;
	int tend;
	int tinc;	
	
	public BaAnimationClock (int tend) {
		this.tstart = 0;
		this.tend = tend;
		this.tinc = 1;
		this.t = tstart-1;
	}
	
	public void init () {
		t = tstart-1;
	}
	
	public void nextStep () {
		t+= tinc;
		if (t >= tend) t = tstart;
	}
	
	public boolean between (int tstart, int tend) {
		return ((tstart <= t) && (t <= tend));
	}
}
