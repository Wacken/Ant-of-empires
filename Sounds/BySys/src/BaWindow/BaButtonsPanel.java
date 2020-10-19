package BaWindow;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

import BaGeoWorlds.BaRobotWorld.BaRobot;
import BaMain.*;

public class BaButtonsPanel
	extends JPanel
	implements ActionListener
{
	// Variables	
	JCheckBox B1,B2,B3,B4,B5,B6,B7;
	private static final long	serialVersionUID= 0L;

		
	// Constructor
	public BaButtonsPanel () {
		setBorder(BorderFactory.createLineBorder(Color.ORANGE));		
		setLayout(new FlowLayout(FlowLayout.LEADING));

		B1 = new JCheckBox ("zBuffering");
		B1.addActionListener(this);
		B1.setSelected(BAG.zBuffering);
		add (B1);

		B2 = new JCheckBox ("Double Buffered");
		B2.setSelected(BAG.doubleBuffered);
		B2.addActionListener(this);
		add (B2);

		B3 = new JCheckBox ("Filled Polygons");
		B3.setSelected(BAG.filled);;
		B3.addActionListener(this);
		add (B3);

		B4 = new JCheckBox ("Show Coordinate Systems");
		B4.setSelected(BAG.showCoordSys);
		B4.addActionListener(this);
		add (B4);

		B5 = new JCheckBox ("Synchronize with Semaphore");
		B5.setSelected(BAG.synchronize);
		B5.addActionListener(this);
		add (B5);
		
		B6 = new JCheckBox ("Short Sleep");
		B6.setSelected(BAG.shortSleep);
		B6.addActionListener(this);
		add (B6);

		B7 = new JCheckBox ("Draw Triangles");
		B7.setSelected(BAG.drawTriangles);
		B7.addActionListener(this);
		add (B7);

	} // BaButtonsPanel
	
	
		
	// Callback
	public void actionPerformed (ActionEvent e) {
		// Did someone push one of our buttons?
		if (e.getSource() == B1) {
			BAG.zBuffering = !BAG.zBuffering; }
		if (e.getSource() == B2) { 
			BAG.doubleBuffered = !BAG.doubleBuffered; }
		if (e.getSource() == B3) {
			BAG.filled = !BAG.filled;
			BaMain.vWorld.setFilled(BAG.filled);
		}
		if (e.getSource() == B4) { 
			BAG.showCoordSys = !BAG.showCoordSys; }
		if (e.getSource() == B5) { 
			BAG.synchronize = !BAG.synchronize; }		
		if (e.getSource() == B6) { 
			BAG.shortSleep = !BAG.shortSleep; }		
		if (e.getSource() == B7) {
			BAG.drawTriangles = !BAG.drawTriangles; 
			BaMain.vWorld = new BaRobot( "Robot", BAG.drawTriangles); 
		}		

	} // actionPerformed
} // class BaButtonPanel

