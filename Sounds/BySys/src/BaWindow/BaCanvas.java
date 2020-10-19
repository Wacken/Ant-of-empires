package BaWindow;

import java.awt.*;
import java.awt.event.*;

import javax.swing.JComponent;
import javax.swing.event.MouseInputListener;

import BaGeoSceneGraph.*;
import BaInteraction.*;
import BaMain.*;

/* **************************************************************************
 * This class is based on the Java Advanced Window Toolkit (AWT). It is based
 * on the AWT "JComponent" and maintains a drawing area ("canvas") for the 
 * renderer to render the virtual world. 
 * 
 * See BaWindowFrame and BaFramebuffer for detailed explanations on how the
 * rendering works (callbacks from BaWindowFrame, drawing in BaFrameBuffer). 
 * 
 * BaCanvas also records and forwards mouse and keyboard interaction.
 * To this end, it implements a number of "Listeners" that are provided by
 * the AWT package. BaCanvas receives the input and then forwards it to the
 * "uiController" (see BaUIController in package "BaInteraction" for further
 * explanations).
 * *************************************************************************/

public class BaCanvas
	extends JComponent
    implements MouseInputListener, KeyListener, ComponentListener, MouseWheelListener
{				
	// Variables
	private BaUIController 	uiController;
	static final long 		serialVersionUID = 0L;

	
	
	// Constructor
	public BaCanvas (int w,int h) {
		super ();		
		uiController = new BaUIController (w,h);

		addMouseListener(this);
		addMouseMotionListener(this);
		
		addMouseWheelListener(this);
		
		addComponentListener(this); 

		addKeyListener(this);
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(new MyDispatcher());

	} // BaRenderingCanvas


	
	// Methods
	
	void init() { }
	
	public void setUIMouseObject (BaSceneGraphNode o) {
		uiController.setObject(o);
	}

	public void update (Graphics g) {
		paint (g);
	}

	public void paintComponent (Graphics g) {
		if (BAG.initialized) {
			BaMain.rEng.setGraphics(g);
			BaMain.rEng.mypaint(g);
			BaWindowFrame.windowBusy.drop();			
		}
	} // paintComponent

	
	// JComponent: callback to repaint itself
//	public void repaint () {
//	   this sets the current window onto the "dirty list" of components
//     such that it is repainted as soon as possible
//     handled by the superclass; must not be changed.
//}
	
	 // UI control: callbacks for Mouse Input

	public void mouseMoved (MouseEvent e) {
		uiController.mouseMoved(e);
	};
	public void mouseDragged (MouseEvent e) { 
		uiController.mouseDragged(e);
	}; 
	public void mousePressed (MouseEvent e) { 
		uiController.mousePressed(e);
	};
	public void mouseReleased (MouseEvent e) { 
		uiController.mouseReleased(e);
	};
	public void mouseClicked (MouseEvent e) { 
		uiController.mouseClicked(e);
	};
	public void mouseEntered (MouseEvent e) { 
		uiController.mouseEntered(e);
	};
	public void mouseExited (MouseEvent e) { 
		uiController.mouseExited(e);
	};
	
	
	
	
	// UI control: callbacks for MouseWheel Input
	
	public void mouseWheelMoved(MouseWheelEvent e) {
		uiController.mouseWheelMoved(e);
	}
	     

	
	// UI control: callbacks for Key Input
	
   private class MyDispatcher implements KeyEventDispatcher {
        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            if (e.getID() == KeyEvent.KEY_PRESSED) { keyPressed(e); } 
            else if (e.getID() == KeyEvent.KEY_RELEASED) { keyReleased(e); } 
            else if (e.getID() == KeyEvent.KEY_TYPED) { keyTyped(e); }
            return false;
        }
    }
	public void keyPressed(KeyEvent e) {
		uiController.keyPressed(e);
        repaint();
	}
	public void keyReleased(KeyEvent e) {
		uiController.keyReleased(e);
		repaint();
	}
	public void keyTyped(KeyEvent e) {
		uiController.keyTyped(e);
        repaint();
	}

	
	// UI control: callbacks for Window changes
	public void componentHidden (ComponentEvent e) { }
	public void componentShown (ComponentEvent e) { }
	public void componentMoved (ComponentEvent e) { }
	
	public void componentResized (ComponentEvent e) {
		if (BAG.initialized) {
			BaMain.rEng.notifyResize(getWidth(),getHeight());
		}
        repaint();
    } // componentResized
	

} // class BaCanvas

