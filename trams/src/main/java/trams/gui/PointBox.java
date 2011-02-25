package trams.gui;

import trams.data.*;

/**
 * Class to represent a point (box) in the control screen display in the TraMS program.
 * @author Dave Lee.
 */
public class PointBox {
    
    private int theMinHeight;
    private int theMaxHeight;
    private int theMinWidth;
    private int theMaxWidth;
    private RouteSchedule theMessage;
    
    /**
     * Create a new PointBox object.
     * @param minWidth a <code>int</code> with the minimum width.
     * @param minHeight a <code>int</code> with the minimum height.
     * @param maxWidth a <code>int</code> with the maximum width.
     * @param maxHeight a <code>int</code> with the maximum height.
     * @param message a <code>RouteSchedule</code> object with the schedule id represented by this box.
     */
    public PointBox ( int minWidth, int minHeight, int maxWidth, int maxHeight, RouteSchedule message ) {
        theMinHeight = (minHeight+180);
        theMaxHeight = (minHeight+180)+maxHeight;
        theMinWidth = (minWidth+215);
        theMaxWidth = (minWidth+215)+maxWidth;
        theMessage = message;
    }
    
    /**
     * Check if the supplied width and height occur within the bounds of this box.
     * @param width a <code>int</code> with the width (x).
     * @param height a <code>int</code> with the height (y).
     * @return a <code>boolean</code> which is true iff the supplied x and y occurs in the box.
     */
    public boolean isThisBox ( int width, int height ) {
        if ( width >= theMinWidth && width <= theMaxWidth && height >= theMinHeight && height <= theMaxHeight ) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Get the message to be represented by this box.
     * @return a <code>String</code> with the message.
     */
    public String getMessage ( ) {
        return theMessage.toString();
    }
    
    /**
     * Get the minimum width of this box.
     * @return a <code>int</code> with the minimum width.
     */
    public int getMinWidth ( ) {
        return theMinWidth;
    }
    
    /**
     * Get the maximum width of this box.
     * @return a <code>int</code> with the maximum width.
     */
    public int getMaxWidth ( ) {
        return theMaxWidth;
    }
    
    /**
     * Get the minimum height of this box.
     * @return a <code>int</code> with the minimum height.
     */
    public int getMinHeight ( ) {
        return theMinHeight;
    }
    
    /**
     * Get the maximum height of this box.
     * @return a <code>int</code> with the maximum height.
     */
    public int getMaxHeight ( ) {
        return theMaxHeight;
    }
    
}
