package BaMain;

/*
 * Collection of ALL GLOBAL VARIBALES that are available
 * to all packages, classes and methods in this program.
 * (Status and control variables, system parameters)
 */

public class BAG {
	public static boolean 		doubleBuffered = true; 
	public static boolean 		zBuffering = true; 
	public static boolean 		synchronize = true;
	public static boolean		shortSleep = false;
	public static int 			shortSleepingTime = 5;
	public static int 			longSleepingTime = 50;
	public static int 			cubes_per_row = 5;
	public static boolean 		filled = true;
	public static boolean		showCoordSys = false;
	public static boolean		drawTriangles = true;

	public static boolean		initialized = false;
	public static int 			cnt = 0;
	public static int 			debugLevel = 0;

	// constructor
	BAG () { }
	
	// some generally useful helper routines	
	// geometry
	public static boolean insideRange (int val, int lb, int ub) {
		return ((lb <= val) && (val <= ub));
	}
	public static boolean insideRange (double val, int lb, int ub) {
		return ((lb <= val) && (val <= ub));
	}
	public static boolean insideRange (double val, double lb, double ub) {
		return ((lb <= val) && (val <= ub));
	}
	
} // class BAG
