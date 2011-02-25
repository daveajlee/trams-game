package trams.simulation;

import java.util.*;

import trams.data.*;
import org.apache.log4j.Logger;

/**
 * This class represents the simulator in the TraMS program.
 * @author Dave Lee.
 */
public class Simulator implements java.io.Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SimTime theSimTime;
    private Calendar thePreviousTime;
    private Scenario theCurrentScenario;
    private String theDifficultyLevel = "Easy"; //Default to easy.
    
    private Logger logger = Logger.getLogger(Simulator.class);

    //This represents the message to be displayed on screen.
    private LinkedList<Message> theMessageQueue;
    
    /**
     * Create a new simulator - for a scenario.
     * @param newScenario a <code>Scenario</code> with the current scenario.
     */
    public Simulator ( Scenario newScenario ) {
        theSimTime = new SimTime(15);
        thePreviousTime = (Calendar) theSimTime.getCurrentTime().clone();
        theCurrentScenario = newScenario;
        //Initialise queue here.
        theMessageQueue = new LinkedList<Message>();
    }
    
    /**
     * Create a new simulator - for scenario, start time and time increment.
     * @param newScenario a <code>Scenario</code> with the current scenario.
     * @param startTime a <code>String</code> with the start time.
     * @param timeIncrement a <code>int</code> with the time increment.
     */
    public Simulator ( Scenario newScenario, String startTime, int timeIncrement ) {
        theCurrentScenario = newScenario;
        String[] dateTimes = startTime.split("-");
        String[] times = dateTimes[3].split(":");
        Calendar theCalendar = new GregorianCalendar(Integer.parseInt(dateTimes[0]), Integer.parseInt(dateTimes[1])-1, Integer.parseInt(dateTimes[2]), Integer.parseInt(times[0]), Integer.parseInt(times[1]), 0); 
        theSimTime = new SimTime(theCalendar, timeIncrement);
        thePreviousTime = (Calendar) theSimTime.getCurrentTime().clone();
        //Initialise queue here.
        theMessageQueue = new LinkedList<Message>();
    }
    
    /**
     * Set the current scenario.
     * @param newScenario a <code>Scenario</code> with the new scenario.
     */
    public void setScenario ( Scenario newScenario ) {
        theCurrentScenario = newScenario;
    }
    
    /**
     * Get the current scenario.
     * @return a <code>Scenario</code> with the current scenario.
     */
    public Scenario getScenario ( ) {
        return theCurrentScenario;
    }

    /**
     * Add a message to the message queue.
     * @param msg a <code>Message</code> object!
     */
    public void addMessage ( Message msg ) {
        theMessageQueue.addFirst(msg);
    }

    /**
     * Get a linked list of messages which are relevant for the specified folder, date and type.
     * @param folder a <code>String</code> with the name of the folder.
     * @param date a <code>String</code> with the date.
     * @param type a <code>String</code> with the message type.
     * @return a <code>LinkedList</code> with messages.
     */
    public LinkedList<Message> getMessages ( String folder, String date, String type ) {
        //Create a blank linked list first.
        LinkedList<Message> messages = new LinkedList<Message>();
        logger.debug("There are " + theMessageQueue.size() + " messages!");
        //Go through all of the messages and see if they are applicable.
        for ( int i = 0; i < theMessageQueue.size(); i++ ) {
            logger.debug("This is message " + theMessageQueue.get(i).getSubject());
            logger.debug("Date is " + date);
            if ( date.equalsIgnoreCase("All Dates") ) {
                logger.debug("Date was ok!");
                if ( folder.equalsIgnoreCase(theMessageQueue.get(i).getFolder()) && type.equalsIgnoreCase(theMessageQueue.get(i).getType())) {
                    messages.add(theMessageQueue.get(i));
                }
            } else if ( date.equalsIgnoreCase(theMessageQueue.get(i).getDate().split(" at")[0]) && folder.equalsIgnoreCase(theMessageQueue.get(i).getFolder()) && type.equalsIgnoreCase(theMessageQueue.get(i).getType()) ) {
                messages.add(theMessageQueue.get(i));
            }
        }
        //Return a message list.
        return messages;
    }

    /**
     * Get the number of messages.
     * @return a <code>int</code> with the number of messages.
     */
    public int getNumberMessages ( ) {
        return theMessageQueue.size();
    }

    /**
     * Get the message at the supplied position.
     * @param pos a <code>int</code> with the position.
     * @return a <code>Message</code> object which is at the supplied position.
     */
    public Message getMessage ( int pos ) {
        return theMessageQueue.get(pos);
    }
    
    /**
     * Increment the current time.
     */
    public void incrementTime ( ) {
        //Copy previous time first.
        thePreviousTime = (Calendar) theSimTime.getCurrentTime().clone();
        //Increment time.
        theSimTime.incrementTime();
    }
    
    /**
     * Get the current time increment.
     * @return a <code>int</code> with the current time increment.
     */
    public int getIncrement ( ) {
        return theSimTime.getIncrement();
    }
    
    /**
     * Return the supplied calendar object as a formatted string.
     * @param calDate a <code>Calendar</code> object to format.
     * @return a <code>String</code> with the formatted string.
     */
    public String formatDateString ( Calendar calDate ) {
        return theSimTime.formatDateString(calDate);
    }
    
    /**
     * Set the current time increment.
     * @param newIncrement a <code>int</code> with the new time increment.
     */
    public void setIncrement ( int newIncrement ) {
        theSimTime.setIncrement(newIncrement);
    }
    
    /**
     * Get the short year in the form yy.
     * @return a <code>String</code> with the short date info.
     */
    public String getShortYear ( ) {
        return theSimTime.getShortYear();
    }
    
    /**
     * Get the current difficulty level.
     * @return a <code>String</code> with the difficulty level.
     */
    public String getDifficultyLevel ( ) {
        return theDifficultyLevel;
    }
    
    /**
     * Set the current difficulty level.
     * @param diffLevel a <code>String</code> with the difficulty level.
     */
    public void setDifficultyLevel ( String diffLevel ) {
        theDifficultyLevel = diffLevel;
    }
    
    /**
     * Get current simulated time. 
     * @return a <code>Calendar</code> representing the current simulated time.
     */
    public Calendar getCurrentSimTime ( ) {
        return (Calendar) theSimTime.getCurrentTime().clone();
    }

    /**
     * Get previous simulated time.
     * @return a <code>Calendar</code> representing the previous simulated time.
     */
    public Calendar getPreviousSimTime ( ) {
        return thePreviousTime;
    }
    
    /**
     * Get the current display date as a String.
     * @return a <code>String</code> with the current display date.
     */
    public String getCurrentDisplaySimDay ( ) {
        return theSimTime.getDateInfo();
    }

    /**
     * Get the previous display date as a String.
     * @return a <code>String</code> with the previous display date.
     */
    public String getPreviousDisplaySimDay ( ) {
        return theSimTime.getDay(thePreviousTime.get(Calendar.DAY_OF_WEEK)) + " " + thePreviousTime.get(Calendar.DATE) + theSimTime.getDateExt(thePreviousTime.get(Calendar.DATE)) + " " + theSimTime.getMonth(thePreviousTime.get(Calendar.MONTH)) + " " + thePreviousTime.get(Calendar.YEAR);
    }
    
    /**
     * Get the current display time as a String.
     * @return a <code>String</code> with the current display date and time.
     */
    public String getCurrentDisplaySimTime ( ) {
        return theSimTime.getDateInfo() + " at " + theSimTime.getTimeInfo();
    }
    
    /**
     * Get the shorthand version of the date and time as a String for stamping messages.
     * @return a <code>String</code> with the current date and time for stamping messages.
     */
    public String getMessageDisplaySimTime ( ) {
        return theSimTime.getShortDate() + " " + theSimTime.getTimeInfo();
    }
    
}
