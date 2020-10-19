package BaInteraction;

import java.awt.event.*;

import BaRenderer.*;
import BaGeoSceneGraph.*;
import BaMain.*;

public class BaUIController
{
	// Variables
	private BaSceneGraphNode obj;
	private BaTransformation objPose;
	private BaTransformation objRotation;
	private BaTransformation objTranslation;
	private BaTransformation objScaling;
	private BaUIMouseObjRotatorGauss uimRotator;
	private BaUIMouseObjTranslator uimTranslator;
	private int currButton;
	
	// Constructor
	public BaUIController (int w, int h) {
		obj = BaMain.vWorld;		
		objPose = new BaTransformation();
		objRotation = new BaTransformation();
		objTranslation = new BaTransformation();
		objScaling = new BaTransformation();

		uimRotator = new BaUIMouseObjRotatorGauss(BaUIMouseObjRotatorGauss.MatMultMode.EXTRINSIC,w,h);
		uimTranslator = new BaUIMouseObjTranslator(BaUIMouseObjTranslator.MatMultMode.EXTRINSIC);
		uimRotator.setPose(objRotation);
		uimTranslator.setPose(objTranslation);

		currButton = 0;
	} // BaUIController 


	// Methods

	public void init () {
		objPose.identity();
		objRotation.identity();
		objTranslation.identity();
		objScaling.identity();
		currButton = 0;
	} // init

	public void setObject (BaSceneGraphNode o) {
		init();
		obj = o;	
		objPose.copy(obj.pose());
	} // setObject

	private void updateObjectPose (BaSceneGraphNode o) {
		BaTransformation resultTransf = new BaTransformation();
		resultTransf.gets(objTranslation.times(objRotation.times(objPose)));
		o.setPose(resultTransf);
	}
	
	// Callbacks
	
	public void mouseMoved (MouseEvent e) {
		BaUIMouse ui = currentMouseInterpretation(e);
		if (ui != null) ui.mouseMoved(e);
	}; // mouseMoved

	public void mouseDragged (MouseEvent e) {
		obj = BaMain.vWorld;
		BaUIMouse ui = currentMouseInterpretation(e);
		if (ui != null) ui.mouseDragged(e); 
		updateObjectPose (BaMain.vWorld);
	}; // mouseDragged

	public void mousePressed (MouseEvent e) {
		BaUIMouse ui = currentMouseInterpretation(e);
		if (ui != null) ui.mousePressed(e); 
	}; // mousePressed

	public void mouseReleased (MouseEvent e) {
		BaUIMouse ui = currentMouseInterpretation(e);
		if (ui != null) ui.mouseReleased(e); 
	}; // mouseReleased

	public void mouseClicked (MouseEvent e) {
		BaUIMouse ui = currentMouseInterpretation(e);
		if (ui != null) ui.mouseClicked(e); 
	}; // mouseClicked

	public void mouseEntered (MouseEvent e) {
		BaUIMouse ui = currentMouseInterpretation(e);
		if (ui != null) ui.mouseEntered(e);
	}; // mouseEntered

	public void mouseExited (MouseEvent e) {
		BaUIMouse ui = currentMouseInterpretation(e);
		if (ui != null) ui.mouseExited(e); 
	}; // mouseExited

	public void mouseWheelMoved(MouseWheelEvent e) {
		double notches = (double) e.getWheelRotation();	   
		objTranslation.translate(0.0,0.0,notches);
		updateObjectPose (BaMain.vWorld);
	} // MoseWheelMoved

	
	public void keyPressed (KeyEvent e) {
		interpretKeyPressed(e);
		updateObjectPose (BaMain.vWorld);
	}; // keyPressed

	public void keyReleased (KeyEvent e) {
		interpretKeyReleased(e);
		updateObjectPose (BaMain.vWorld);
	}; // keyReleased

	public void keyTyped (KeyEvent e) {
	}; // keyTyped

	
	
	public BaUIMouse currentMouseInterpretation (MouseEvent e) {
		int b = e.getButton();
		if (b != MouseEvent.NOBUTTON)
			currButton = b;

		switch (currButton) {
			case MouseEvent.BUTTON1: 
				return uimRotator;
			case MouseEvent.BUTTON2: 
				return uimTranslator;
			case MouseEvent.BUTTON3:
				return uimTranslator;
			default: 
				return uimRotator;
		}
	}

	
	
	public void interpretKeyPressed (KeyEvent e) {
		char k = e.getKeyChar();

		double stepsize = 1.0;
		switch (k) {
			case 'i':
				init();
				break;
			case 'f': // translate right (along x)
				objTranslation.translate(stepsize, 0.0, 0.0);
				break;
			case 's': // translate right (along x)
				objTranslation.translate(-stepsize, 0.0, 0.0);
				break;
			case 'e': // translate up (along y)
				objTranslation.translate(0.0,stepsize,0.0);
				break;
			case 'd': // translate down (along y)
				objTranslation.translate(0.0,-stepsize,0.0);
				break;
			case 'c': // translate backward (along -z)
				objTranslation.translate(0.0,0.0,-stepsize);
				break;
			case 'x': // translate forward (along -z)
				objTranslation.translate(0.0,0.0,stepsize);
				break;
			case 'h':
				System.out.println("help");
				break;
			default: 
				break;
		}
	}

	public void interpretKeyReleased (KeyEvent e) {
		char k = e.getKeyChar();

		switch (k) {
			case 'i':
				break;
			case 'h':
				break;
			default: 
				break;
		}
	}


} // class BaUIController
