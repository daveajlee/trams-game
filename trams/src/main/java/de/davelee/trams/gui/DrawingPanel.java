package de.davelee.trams.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Class to represent the drawing part of the control screen.
 * @author Dave
 */
public class DrawingPanel extends JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int xPosition;
    private int direction;
    private boolean hasDelay;
    
    public final static int LEFT_TO_RIGHT = 1;
    public final static int RIGHT_TO_LEFT = 2; 
    
    /**
     * Create a new drawing panel.
     * @param xPos a <code>int</code> with the x position.
     * @param direction a <code>int</code> with the vehicle direction.
     * @param hasDelay a <code>boolean</code> which is true iff the vehicle has a delay.
     */
    public DrawingPanel ( int xPos, int direction, boolean hasDelay ) {
        super();
        xPosition = xPos;
        this.direction = direction;
        this.hasDelay = hasDelay;
    }
    
    /**
     * Paint this panel.
     * @param g a <code>Graphics</code> object.
     */
    public void paint ( Graphics g ) {
        g.setColor(Color.BLACK);
        g.drawLine(0, 25, 800, 25);
        if ( hasDelay ) { g.setColor(Color.RED); }
        else { g.setColor(Color.GREEN); }
        int[] xPoints;
        if ( direction == DrawingPanel.RIGHT_TO_LEFT ) {
            xPoints = new int[] {xPosition+0,xPosition+50,xPosition+50};
        }
        else {
            xPoints = new int[] {xPosition+100,xPosition+50,xPosition+50};
        }
        int[] yPoints = {25,0,50};
        g.fillPolygon(xPoints, yPoints, 3);
        //g.fill3DRect(50, 50, 25, 25, true);
    }
    
}
