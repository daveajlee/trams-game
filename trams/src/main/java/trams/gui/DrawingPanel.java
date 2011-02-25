package trams.gui;

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
	private int theXPosition;
    private int theDirection;
    private boolean theHasDelay;
    
    public final static int LEFTTORIGHT = 1;
    public final static int RIGHTTOLEFT = 2; 
    
    /**
     * Create a new drawing panel.
     * @param xPos a <code>int</code> with the x position.
     * @param direction a <code>int</code> with the vehicle direction.
     * @param hasDelay a <code>boolean</code> which is true iff the vehicle has a delay.
     */
    public DrawingPanel ( int xPos, int direction, boolean hasDelay ) {
        super();
        theXPosition = xPos;
        theDirection = direction;
        theHasDelay = hasDelay;
    }
    
    /**
     * Paint this panel.
     * @param g a <code>Graphics</code> object.
     */
    public void paint ( Graphics g ) {
        g.setColor(Color.BLACK);
        g.drawLine(0, 25, 800, 25);
        if ( theHasDelay ) { g.setColor(Color.RED); 
        } else { g.setColor(Color.GREEN); }
        int[] xPoints;
        if ( theDirection == DrawingPanel.RIGHTTOLEFT ) {
            xPoints = new int[] {theXPosition+0,theXPosition+50,theXPosition+50};
        } else {
            xPoints = new int[] {theXPosition+100,theXPosition+50,theXPosition+50};
        }
        int[] yPoints = {25,0,50};
        g.fillPolygon(xPoints, yPoints, 3);
        //g.fill3DRect(50, 50, 25, 25, true);
    }
    
}
