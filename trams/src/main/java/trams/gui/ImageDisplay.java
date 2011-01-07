package trams.gui;

import java.awt.*;
import org.apache.log4j.Logger;

/**
 * Class to display an image in the TraMS program.
 * @author Dave Lee
 */
public class ImageDisplay extends Canvas {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Image theImage;
    private int theLeftBorder;
    private int theTopBorder;
    
    private Logger logger = Logger.getLogger(ImageDisplay.class);
    
    /**
     * Create a new image display.
     * @param fileName a <code>String</code> with the location of the file.
     * @param leftBorder a <code>int</code> with the position of left border.
     * @param topBorder a <code>int</code> with the position of top border.
     */
    public ImageDisplay ( String fileName, int leftBorder, int topBorder ) {
        //Initialise variables.
        theLeftBorder = leftBorder;
        theTopBorder = topBorder;
        //Construct and display image.
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        //theImage = toolkit.getImage(ImageDisplay.class.getResource("/" + fileName)); 
        logger.debug("Attempting to load image... " + fileName);
        theImage = toolkit.getImage("src/main/resources/trams/images/" + fileName);
        logger.debug("Image loaded!");
        //theImage = toolkit.getImage(fileName);
        MediaTracker mediaTracker = new MediaTracker(this);
	mediaTracker.addImage(theImage, 0);
        try
	{
            mediaTracker.waitForID(0);
        }
	catch (InterruptedException ie)
	{
            System.err.println(ie);
            System.exit(1);
        }
    }
    
    /**
     * Paint the graphics part of this screen.
     * @param g a <code>Graphics</code> object.
     */
    public void paint ( Graphics g ) {
        g.drawImage(theImage,theLeftBorder,theTopBorder,null);
    }
    
    /**
     * Repaint the graphics part of this screen.
     */
    public void repaint () {
        Graphics g = getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(-1000,-1000,2000,2000);
        paint ( g );
    }
    
    /**
     * Move the image on the screen.
     * @param leftBorder a <code>int</code> with the left part of the border.
     * @param topBorder a <code>int</code> with the top part of the border.
     */
    public void moveImage ( int leftBorder, int topBorder ) {
        theLeftBorder = leftBorder;
        theTopBorder = topBorder;
        repaint();
    }
    
    
}
