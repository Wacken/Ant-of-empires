package BaWindow;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import BaCSTools.BaSemaphore;
import BaGeoSceneGraph.*;
import BaMain.BAG;

/* **************************************************************************
 * This class is based on the Java Advanced Window Toolkit (AWT). It
 * maintains a window on the screen, defined by the AWT class JFrame. Inside 
 * this frame exist two subareas: a BaButtonsPanel (derived from the AWT 
 * class JPanel) and a BaCanvas (derived from the AWT class JComponent).
 * 
 * The virtual world is rendered in the BaCanvas - which also records and
 * forwards mouse and keyboard interaction (for details see BaCanvas).
 * 
 * The BaButtonsPanel presents several buttons with which users can control
 * a few general steering parameters (for details see BaButtonsPanel).
 * 
 * The windowFrame defined in class BaWindowFrame does not do much by itself;
 * it is mainly a container for the panel and the canvas. Yet, it does use
 * a WindowListener to handle that the program exits properly when the 
 * window is closed.
 * 
 * SYNCHRONIZATION OF THREADS:
 * BaWindowFrame is also in charge of maintaining proper synchronization
 * between the main thread of the GameEngine (main loop) and the separate 
 * AWT window thread (from the AWT windowing part) 
 * (see also explanations in BaFrameBuffer).
 * 
 * Here is the problematic issue that requires synchronization: 
 *  - When the Main program requests sWin (class BaWindowFrame) to
 *    repaint itself ("sWin.repaint_synchronized()"; inside the main loop).
 *  - BaWindowFrame forwards this request to the BaCanvas
 *    ("renderingCanvas.repaint()"), and
 *  - the underlying JComponent puts the window onto its "dirty list" -
 *    to be repainted as soon as possible in the future (within the AWT 
 *    thread).
 *  - When this happens (from the AWT thread), it issues a callback to
 *    the JComponent method "paintComponent (Graphics g)" (see BaCanvas).
 *  - BaCanvas forwards the request to the rendering engine
 *    ("BaMain.rEng.mypaint(g)").
 *    The rendering is then executed (as part of the AWT-thread).
 * Depending on the complexity of the virtual world, this rendering may take 
 * some time.
 * 
 * In parallel, the main thread of the Game Engine keeps iterating through
 * its main loop and issues further requests to sWin to repaint itself.
 * This may cause the BaMain.rEng renderer to restart before it has finished
 * its previous rendering traversal through the scene graph. The result are
 * unfinished images of the virtual world.
 * 
 * To avoid this, the two threads need to be synchronized. To this end, class
 * BaWindowFrame maintains a semaphore "windowBusy". 
 * Whenever a new paintComponent request is issued (as a callback from the 
 * AWT), the method raises the "windowBusy" semaphore before issuing the 
 * repaint request to the Canvas (and henceforth to the renderer) 
 * (as described above). It then waits until the semaphore is dropped.
 * 
 * The "windowBusy" semaphore is dropped in BaCampus.paintComponent(g), when
 * the renderer has finished rendering (see BaCampus).
 * 
 * (Further explanations are in class "BaFrameBuffer".)
 * *************************************************************************/

public class BaWindowFrame
{
	// Variables
	private BaCanvas 	   renderingCanvas;
	private BaButtonsPanel uiPanel;
	private JFrame		   windowFrame;

	// for thread synchronization between the main program and window
	public static BaSemaphore	windowBusy; 

	// Constructor
	public BaWindowFrame (String message, int x, int y, int w, int h) {
		renderingCanvas = new BaCanvas(w,h);
		uiPanel = new BaButtonsPanel();

		windowFrame = new JFrame (message);
		windowFrame.addWindowListener (new WindowAdapter() {
			public void windowClosing (WindowEvent we) { System.exit(0); }
		});		
		windowFrame.setLocation(x, y);
		windowFrame.setSize(w, h);	
		windowFrame.setLayout(new BorderLayout());
		windowFrame.add(uiPanel, BorderLayout.NORTH);
	    windowFrame.add(renderingCanvas);
		windowFrame.setVisible (true);

		// Create a SEMAPHORE to synchronize the threads of
		// - the main loop (SIMPLE GAME ENGINE) and
		// - the display window (WINDOWFRAME)
		windowBusy = new BaSemaphore ();

	} // BaWindowFrame
	
	// Getters and Setters	
	public int getWidth () { return renderingCanvas.getWidth (); }	
	public int getHeight () { return renderingCanvas.getHeight (); }	
	public void setUIMouseObject (BaSceneGraphNode o) { 
		renderingCanvas.setUIMouseObject(o); 
	}
	
	
	// Methods
	public void init () {
		renderingCanvas.init();
	}
	
	public void repaint_synchronized () {
		if (BAG.synchronize)	repaint_using_semaphore ();
		else					repaint_using_fixed_sleeping_time ();		
	}
	
	public void repaint_using_semaphore () {
		windowBusy.raise ();
		renderingCanvas.repaint();
		try { windowBusy.waitUntilDropped (); }
		catch (InterruptedException e) {}
	}
	public void repaint_using_fixed_sleeping_time () {
		renderingCanvas.repaint();
		int t;
		if (BAG.shortSleep) t = BAG.shortSleepingTime; 
		else t = BAG.longSleepingTime;
		try { Thread.sleep (t); } 
		catch (InterruptedException e) { }
	}

} // class BaWindowFrame
