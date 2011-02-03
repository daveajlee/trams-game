package trams.main;

import javax.swing.*;
import javax.swing.filechooser.*;

import trams.data.*;
import trams.gui.ControlScreen;
import trams.gui.SplashScreen;
import trams.gui.WelcomeScreen;
import trams.simulation.Simulator;

import java.util.*;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

/**
 * This class controls the user interface of the TraMS program. 
 * @author Dave Lee.
 */
public class UserInterface implements Runnable {
    
    private ProgramOperations theOperations;
    private JFrame theCurrentFrame;
    private ControlScreen theControlScreen;
    private boolean isEnd;
    private Thread theRunningThread;
    private boolean isSimulationRunning;
    private LinkedList<Integer> theRouteDetailPos;
    
    private int theSimulationSpeed = 2000;
    
    private final String theVersion = "0.3.1 (Open Source Edition)";
    
    private Logger logger = Logger.getLogger(UserInterface.class);
    
    //This is to decide which screen to show when we refresh.
    private boolean showingMessages = false;
    private boolean showingManagement = false;

    //This is where we keep the tip messages.
    LinkedList<String> theTipMessages = new LinkedList<String>();
    
    /**
     * Create a new user interface - default constructor.
     */
    public UserInterface ( ) {
        //logger.debug("We are in ui constructor");
        isEnd = false;
        isSimulationRunning = false;
        theOperations = new ProgramOperations();
        //Temporalily create list here.
        theRouteDetailPos = new LinkedList<Integer>();
        //Add tip messages here.
        theTipMessages.add("TIP: Watch your balance! You can't buy new vehicles or run more routes if you don't have money!");
        theTipMessages.add("TIP: Earn money by improving your passenger satisfaction through running vehicles on time!");
        theTipMessages.add("TIP: If your passenger satisfaction falls too low, you may be forced to resign!");
    }
    
    /**
     * Set the current frame displayed to the user.
     * @param currentFrame a <code>JFrame</code> with the current frame.
     */
    public void setFrame ( JFrame currentFrame ) {
        theCurrentFrame = currentFrame;
    }
    
    /**
     * Get the current frame displayed to the user.
     * @return a <code>JFrame</code> with the current frame.
     */
    public JFrame getFrame ( ) {
        return theCurrentFrame;
    }
    
    /**
     * Add a message to the message queue.
     * @param msg a <code>Message</code> object!
     */
    public void addMessage ( Message msg ) {
        getSimulator().addMessage(msg);
    }
    
    /**
     * Get the current version number.
     * @return a <code>String</code> with the current version number.
     */
    public String getVersion ( ) {
        return theVersion;
    }
    
    /**
     * Set the current simulator.
     * @param s a <code>Simulator</code> object.
     */
    public void setSimulator ( Simulator s ) {
        theOperations.setSimulator(s);
    }
    
    /**
     * Confirm and exit the TraMS program.
     */
    public void exit ( ) {
        //Confirm user did wish to exit.
        boolean wasSimulationRunning = false;
        if (isSimulationRunning) { pauseSimulation(); wasSimulationRunning = true; }
        int result = JOptionPane.showOptionDialog(theCurrentFrame, "Are you sure you wish to exit TraMS?", "Please Confirm Exit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[] { "Yes", "No" }, "No");
        if ( result == JOptionPane.YES_OPTION ) {
            isEnd = true;
            System.exit(0);
        }
        if (wasSimulationRunning) { resumeSimulation(); }
    }
    
    /**
     * Set the message screen we are using.
     */
    public void setMessageScreen ( boolean flag ) {
        showingMessages = flag;
    }
    
    /**
     * Check if we are using the message screen.
     */
    public boolean getMessageScreen ( ) {
        return showingMessages;
    }
    
    /**
     * Set the management screen we are using.
     */
    public void setManagementScreen ( boolean flag ) {
        showingManagement = flag;
    }
    
    /**
     * Check if we are using the message screen.
     */
    public boolean getManagementScreen ( ) {
        return showingManagement;
    }
    
    /**
     * This is a method to randomly pick a tip from the tip messages list and return it!
     * @return a <code>String</code> with a random tip message.
     */
    public String getRandomTipMessage ( ) {
        Random r = new Random();
        return theTipMessages.get(r.nextInt(theTipMessages.size()));
    }
    
    /**
     * Run simulation until pause is called.
     */
    @SuppressWarnings("static-access")
    public void run() {
        isSimulationRunning = true;
        //First of all, sleep for theSimulationSpeed seconds.
        try { theRunningThread.sleep(theSimulationSpeed); } catch (InterruptedException ie) {}
        //Keep running this until pause.
        while ( !isEnd ) {
            //Increment time.
            theOperations.incrementSimTime();
            theControlScreen.drawVehicles(true);
            //Now sleep!
            try { theRunningThread.sleep(theSimulationSpeed); } catch (InterruptedException ie) {}
        }
    }
    
    /**
     * Speed up the simulation!
     */
    public void speedUpSimulation() {
        if ( theSimulationSpeed > 1000 ) {
            theSimulationSpeed -= 250;
        }
    }
    
    /**
     * Slow down the simulation!
     */
    public void slowSimulation() {
        if ( theSimulationSpeed < 4000 ) {
            theSimulationSpeed += 250;
        }
    }
    
    /**
     * Get the simulation speed!
     * @return a <code>int</code> with the simulation speed.
     */
    public int getSimulationSpeed() {
        return theSimulationSpeed;
    }
    
    /**
     * Set the control screen.
     * @param cs a <code>ControlScreen</code> object.
     */
    public void setControlScreen( ControlScreen cs ) {
        theControlScreen = cs;
        logger.debug("Making control screen visible...");
        theControlScreen.setVisible(true);
    }
    
    /**
     * Get the number of route display vehicles.
     * @param routeNumber a <code>String</code> with the route number.
     * @return a <code>int</code> with the number of route display vehicles.
     */
    public int getNumRouteDisplayVehicles ( String routeNumber ) {
        if ( routeNumber.equalsIgnoreCase("<No Routes Currently Registered>") ) { return 0; }
        return theOperations.getSimulator().getScenario().getRoute(routeNumber).getNumRouteSchedules();
    }
    
    /**
     * Get the difficulty level.
     * @return a <code>String</code> with the difficulty level.
     */
    public String getDifficultyLevel ( ) {
        return theOperations.getSimulator().getDifficultyLevel();
    }
    
    /**
     * Set the difficulty level.
     * @param diffLevel a <code>String</code> with the new difficulty level.
     */
    public void setDifficultyLevel ( String diffLevel ) {
        theOperations.getSimulator().setDifficultyLevel(diffLevel);
    }
    
    /**
     * Get a linked list of messages which are relevant for the specified folder, date and type.
     * @param folder a <code>String</code> with the name of the folder.
     * @param date a <code>String</code> with the date.
     * @param type a <code>String</code> with the message type.
     * @return a <code>LinkedList</code> with messages.
     */
    public LinkedList<Message> getMessages ( String folder, String date, String type ) {
        //Return a message list.
        return getSimulator().getMessages(folder, date, type);
    }
    
    /**
     * Get the number of messages.
     * @return a <code>int</code> with the number of messages.
     */
    public int getNumberMessages ( ) {
        return getSimulator().getNumberMessages();
    }
    
    /**
     * Get the message at the supplied position.
     * @param pos a <code>int</code> with the position.
     * @return a <code>Message</code> object which is at the supplied position.
     */
    public Message getMessage ( int pos ) {
        return getSimulator().getMessage(pos);
    }

    /**
     * Get the number of stops for a particular route.
     * @param routeNumber a <code>String</code> with the route number.
     * @return a <code>int</code> with the number of stops.
     */
    public int getNumStops ( String routeNumber ) {
        return theOperations.getSimulator().getScenario().getRoute(routeNumber).getNumStops(Route.OUTWARDSTOPS);
    }
    
    /**
     * Get the stop name for a particular route and particular stop number.
     * @param routeNumber a <code>String</code> with the route number.
     * @param pos a <code>int</code> with the stop position for that route.
     * @return a <code>String</code> with the stop name.
     */
    public String getStopName ( String routeNumber, int pos ) {
        return theOperations.getSimulator().getScenario().getRoute(routeNumber).getStop(Route.OUTWARDSTOPS, pos).getStopName();
    }
    
    /**
     * Set the minimum and maximum display of vehicles.
     * @param min a <code>int</code> with the minimum.
     * @param max a <code>int</code> with the maximum.
     * @param routeNumber a <code>String</code> with the route number.
     */
    public void setCurrentDisplayMinMax ( int min, int max, String routeNumber ) {
        //Clear the original matrix for which routes to display.
        theRouteDetailPos = new LinkedList<Integer>();
        //Store the currentDate - we will need it for display schedules.
        Calendar currentTime = theOperations.getSimulator().getCurrentSimTime();
        //Determine the route ids we will display using these parameters.
        logger.debug("Route number is " + routeNumber);
        logger.debug(theOperations.getSimulator().getScenario().getRoute(routeNumber));
        logger.debug("Number of possible display schedules: " +  theOperations.getSimulator().getScenario().getRoute(routeNumber).getNumRouteSchedules());
        if ( theOperations.getSimulator().getScenario().getRoute(routeNumber).getNumRouteSchedules() < max ) { max = theOperations.getSimulator().getScenario().getRoute(routeNumber).getNumRouteSchedules(); }
        //logger.debug("Max vehicles starts at " + max + " - routeDetails size is " + routeDetails.size());
        logger.debug("Min is " + min + " & Max is " + max);
        if ( min == max ) {
            RouteSchedule rs = theOperations.getSimulator().getScenario().getRoute(routeNumber).getRouteSchedule(min);
            logger.debug("Getting route schedule " + rs.toString() + " from loop being equal!");
            if ( theOperations.getSimulator().getScenario().getRoute(routeNumber).getAssignedVehicle(rs.toString()) == null ) {
                logger.debug("A schedule was null");
            }
            if ( rs.getCurrentStop(currentTime, theOperations.getSimulator())[0].equalsIgnoreCase("Depot") ) {
                logger.debug("Vehicle in depot!");
            }
            if ( theOperations.getSimulator().getScenario().getRoute(routeNumber).getAssignedVehicle(rs.toString()) != null && !rs.getCurrentStop(currentTime, theOperations.getSimulator())[0].equalsIgnoreCase("Depot") ) {
                //logger.debug("Adding Route Detail " + routeDetails.get(i).getId());
                theRouteDetailPos.add(0);
            }
            else {
                max++;
                //logger.debug("Max is now " + max + " - routeDetails size is: " + routeDetails.size());
                if ( theOperations.getSimulator().getScenario().getRoute(routeNumber).getNumRouteSchedules() < max ) { max = theOperations.getSimulator().getScenario().getRoute(routeNumber).getNumRouteSchedules(); }
                //logger.debug("Route Detail " + routeDetails.get(i).getId() + " was null - maxVehicles is now " + max);
            }
        }
        for ( int i = min; i < max; i++ ) { //Changed from i = 0; i < routeDetails.size().
            RouteSchedule rs = theOperations.getSimulator().getScenario().getRoute(routeNumber).getRouteSchedule(i);
            logger.debug("Getting route schedule " + rs.toString() + " from for loop!");
            if ( theOperations.getSimulator().getScenario().getRoute(routeNumber).getAssignedVehicle(rs.toString()) == null ) {
                logger.debug("A schedule was null");
            }
            if ( rs.getCurrentStop(currentTime, theOperations.getSimulator())[0].equalsIgnoreCase("Depot") ) {
                logger.debug("Vehicle in depot!");
            }
            if ( theOperations.getSimulator().getScenario().getRoute(routeNumber).getAssignedVehicle(rs.toString()) != null && !rs.getCurrentStop(currentTime, theOperations.getSimulator())[0].equalsIgnoreCase("Depot") ) {
                //logger.debug("Adding Route Detail " + routeDetails.get(i).getId());
                theRouteDetailPos.add(i);
            }
            else {
                max++;
                //logger.debug("Max is now " + max + " - routeDetails size is: " + routeDetails.size());
                if ( theOperations.getSimulator().getScenario().getRoute(routeNumber).getNumRouteSchedules() < max ) { max = theOperations.getSimulator().getScenario().getRoute(routeNumber).getNumRouteSchedules(); }
                //logger.debug("Route Detail " + routeDetails.get(i).getId() + " was null - maxVehicles is now " + max);
            }
        }
    }
    
    /**
     * Get the current minimum vehicle.
     * @return a <code>int</code> with the id of the first vehicle.
     */
    public int getCurrentMinVehicle ( ) {
        return theRouteDetailPos.getFirst();
    }
    
    /**
     * Get the current maximum vehicle.
     * @return a <code>int</code> with the id of the second vehicle.
     */
    public int getCurrentMaxVehicle ( ) {
        return theRouteDetailPos.getLast();
    }
    
    /**
     * Get the current number of display schedules.
     * @return a <code>int</code> with the current number of display schedules.
     */
    public int getNumCurrentDisplaySchedules ( ) {
        return theRouteDetailPos.size();
    }
    
    /**
     * Get the display schedule based on route number and position.
     * @param routeNumber a <code>String</code> with the route number.
     * @param pos a <code>int</code> with the position.
     * @return a <code>RouteSchedule</code> object.
     */
    public RouteSchedule getDisplaySchedule ( String routeNumber, int pos ) {
        //Store the currentDate - we will need it for display schedules.
        return theOperations.getSimulator().getScenario().getRoute(routeNumber).getRouteSchedule(theRouteDetailPos.get(pos));
    }
    
    /**
     * Get the current simulator. 
     * @return a <code>Simulator</code> object.
     */
    public Simulator getSimulator ( ) {
        return theOperations.getSimulator();
    }
    
    /**
     * Pause the simulation!
     */
    public void pauseSimulation ( ) {
        isSimulationRunning = false;
        //logger.debug("Pausing - Setting isEnd to true in " + this.toString());
        isEnd = true;
    }
    
    /**
     * Resume the simulation!
     */
    public void resumeSimulation ( ) {
        isSimulationRunning = true;
        //logger.debug("Resuming - Setting isEnd to false");
        isEnd = false;
        theRunningThread = new Thread(this, "SimThread");
        theRunningThread.start();
        //runSimulation(theControlScreen, theOperations.getSimulator());
    }
    
    /**
     * Load a scenario.
     * @param scenarioName a <code>String</code> with the scenario's name.
     * @param playerName a <code>String</code> with the player's name.
     * @return a <code>boolean</code> which is true iff the scenario has been created successfully.
     */
    public boolean loadScenario ( String scenarioName, String playerName ) {
        //Process through program operations.
        if ( theOperations.createSimulator(scenarioName, playerName) ) {
            //Create welcome message.
        	Message message = (Message) getContext().getBean("CouncilMessage");
        	message.setSubject("Welcome Message");
        	message.setText("Congratulations on your appointment as Managing Director of the " + getScenario().getScenarioName() + "! \n\n Your targets for the coming days and months are: " + getScenario().getTargets());
        	message.setSender("Council");
        	message.setFolder("Inbox");
        	message.setDate(getSimulator().getCurrentDisplaySimTime());
            addMessage(message);
            return true;
        }
        return false;
    }
    
    public ApplicationContext getContext ( ) {
    	return theOperations.getContext();
    }
    
    /**
     * Run simulation!
     */
    public void runSimulation ( ) {
        theCurrentFrame.dispose();
        ControlScreen cs = new ControlScreen(this, theOperations.getSimulator(), "", 0, 4, false);
        cs.drawVehicles(true);
        theCurrentFrame.setVisible(true);
        //Set control screen.
        setControlScreen(cs);
        //Finally, run simulation
        isEnd = false;
        theRunningThread = new Thread(this, "simThread");
        theRunningThread.start();
        //runSimulation(cs, theOperations.getSimulator());
    }
    
    /**
     * Change the selected route.
     * @param routeNumber a <code>String</code> with the new route number.
     */
    public void changeRoute ( String routeNumber ) {
        //Now create new control screen.
        ControlScreen cs = new ControlScreen(this, theOperations.getSimulator(), routeNumber, 0, 4, false);
        cs.drawVehicles(true);
        theCurrentFrame.setVisible(true);
        //Set control screen.
        setControlScreen(cs);
        //Resume simulation.
        resumeSimulation();
    }
    
    /**
     * Change the display to show other vehicles.
     * @param routeNumber a <code>String</code> with the route number.
     * @param min a <code>int</code> with the new min vehicle id.
     * @param max a <code>int</code> with the new max vehicle id.
     * @param allocations a <code>boolean</code> which is true iff allocations have been performed.
     */
    public void changeDisplay ( String routeNumber, int min, int max, boolean allocations ) {
        //Now create new control screen.
        ControlScreen cs = new ControlScreen(this, theOperations.getSimulator(), routeNumber, min, max, allocations);
        //cs.drawVehicles(true);
        theCurrentFrame.setVisible(true);
        //Set control screen.
        setControlScreen(cs);
        //Resume simulation.
        resumeSimulation();
    }
    
    /**
     * Save file.
     * @return a <code>boolean</code> which is true iff the file was saved successfully.
     */
    public boolean saveFile ( ) {
        //Create file dialog box.
        JFileChooser fileDialog = new JFileChooser();
        fileDialog.setDialogTitle("Save Game");
        //Only display files with tra extension.
        FileNameExtensionFilter filter = new FileNameExtensionFilter("TraMS Saved Games", "tms");
        fileDialog.setFileFilter(filter);
        //Display file dialog.
        int returnVal = fileDialog.showSaveDialog(theCurrentFrame);
        //Check if user submitted file.
        if ( returnVal == JFileChooser.APPROVE_OPTION ) {
            if ( theOperations.saveFile(fileDialog.getSelectedFile()) ) {
                String fileName = fileDialog.getSelectedFile().getPath();
                if ( !fileName.endsWith(".tms") ) { fileName += ".tms"; }
                JOptionPane.showMessageDialog(theCurrentFrame, "The current simulation has been successfully saved to " + fileName, "File Saved Successfully", JOptionPane.INFORMATION_MESSAGE);
                return true;
            }
            JOptionPane.showMessageDialog(theCurrentFrame, "The file could not be saved. Please try again later.", "ERROR: File Could Not Be Saved", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
    
    /**
     * Load file. 
     * @return a <code>boolean</code> which is true iff the file was loaded successfully.
     */
    public boolean loadFile ( ) {
        //Create file dialog box.
        JFileChooser fileDialog = new JFileChooser();
        fileDialog.setDialogTitle("Load Game");
        //Only display files with tra extension.
        FileNameExtensionFilter filter = new FileNameExtensionFilter("TraMS Saved Games", "tms");
        fileDialog.setFileFilter(filter);
        //Display file dialog.
        int returnVal = fileDialog.showOpenDialog(theCurrentFrame);
        //Check if user submitted file and print coming soon.
        boolean validFile = true;
        if ( returnVal == JFileChooser.APPROVE_OPTION) {
            if ( theOperations.loadFile(fileDialog.getSelectedFile()) ) {
                JFrame oldFrame = theCurrentFrame;
                setManagementScreen(true);
                ControlScreen cs = new ControlScreen(this, theOperations.getSimulator(), "", 0, 4, false);
                //cs.drawVehicles(false);
                //WelcomeScreen ws = new WelcomeScreen(this);
                //cs.setVisible(true);
                theCurrentFrame.setVisible(true);
                oldFrame.dispose();
                //Set control screen.
                setControlScreen(cs);
                //Finally, run simulation
                /*isEnd = false;
                theRunningThread = new Thread(this, "simThread");
                theRunningThread.start();*/
                pauseSimulation();
                //runSimulation(cs, theOperations.getSimulator());
                return true;
            }
            //theScreen.dispose();
            validFile = false;
        }
        if ( !validFile ) {
            JOptionPane.showMessageDialog(theCurrentFrame,"The selected file is not compatible with this version of TraMS.\nYou may want to check the TraMS website for a convertor at http://trams.davelee.me.uk\nPlease either choose another file or create a new game.", "ERROR: Saved Game Could Not Be Loaded", JOptionPane.ERROR_MESSAGE);
        }
        //If we reach here then return false.
        return false;
    }
    
    /**
     * Get current simulated time. 
     * @return a <code>Calendar</code> representing the current simulated time.
     */
    public Calendar getCurrentSimTime ( ) {
        return theOperations.getSimulator().getCurrentSimTime();
    }
    
    /**
     * Add a new route.
     * @param r a <code>Route</code> object.
     * @return a <code>boolean</code> which is true iff the route was added successfully.
     */
    public boolean addNewRoute ( Route r ) {
        //Check for duplicate id.
        for ( int i = 0; i < theOperations.getSimulator().getScenario().getNumberRoutes(); i++ ) {
            if ( theOperations.getSimulator().getScenario().getRoute(i).getRouteNumber().equalsIgnoreCase(r.getRouteNumber() )) {
                return false;
            }
        }
        //Return result of ProgramOperations method.
        return theOperations.addRoute(r);
    }
    
    /**
     * Delete route.
     * @param r a <code>Route</code> object to delete.
     * @return a <code>boolean</code> which is true iff the route has been deleted successfully.
     */
    public boolean deleteRoute ( Route r ) {
        return theOperations.deleteRoute(r);
    }
    
    /**
     * Find a route with the matching string representation as the one supplied.
     * @param routeData a <code>String</code> with the route data.
     * @return a <code>Route</code> object.
     */
    public Route findRoute ( String routeData ) {
        //Find route with matching toString to the one supplied.
        for ( int i = 0; i < theOperations.getSimulator().getScenario().getNumberRoutes(); i++ ) {
            if ( theOperations.getSimulator().getScenario().getRoute(i).toString().equalsIgnoreCase(routeData) ) {
                return theOperations.getSimulator().getScenario().getRoute(i);
            }
        }
        //Return null if no route found.
        return null;
    }
    
    /**
     * Get the number of routes which this scenario currently has.
     * @return a <code>int</code> with the number of routes.
     */
    public int getNumberRoutes ( ) {
        return theOperations.getSimulator().getScenario().getNumberRoutes();
    }
    
    /**
     * Get the route based on the position.
     * @param pos a <code>int</code> with the position.
     * @return a <code>Route</code> object at that position.
     */
    public Route getRoute ( int pos ) {
        return theOperations.getSimulator().getScenario().getRoute(pos);
    }
    
    /**
     * Get the route based on comparing the toString method with the supplied text.
     * @param routeStr a <code>String</code> with the string representation of the route.
     * @return a <code>Route</code> object matching the string representation.
     */
    public Route getRoute ( String routeStr ) {
        String routeNumber = routeStr.split(":")[0];
        return theOperations.getSimulator().getScenario().getRoute(routeNumber);
    }
    
    /**
     * Get the route which has the supplied number.
     * @param routeNumber a <code>String</code> with the route number.
     * @return a <code>Route</code> object which has that route number.
     */
    public Route getRouteByNumber ( String routeNumber ) {
        return theOperations.getSimulator().getScenario().getRoute(routeNumber);
    }

    /**
     * Sort routes by route number alphabetically.
     */
    public void sortRoutes ( ) {
        theOperations.getSimulator().getScenario().sortRoutes();
    }
    
    /**
     * Get the list of allocations.
     * @return a <code>LinkedList</code> of allocations.
     */
    public ArrayList<String> getAllocations ( ) {
        return theOperations.getAllocations();
    }
    
    /**
     * Get list of today's allocations.
     * @param currentDate a <code>String</code> with the current date.
     * @return a <code>LinkedList</code> of allocations.
     */
    public ArrayList<String> getTodayAllocations ( String currentDate ) {
        ArrayList<String> allAllocations = theOperations.getAllocations();
        ArrayList<String> runningIds = new ArrayList<String>();
        for ( int h = 0; h < theOperations.getSimulator().getScenario().getNumberRoutes(); h++ ) {
            for ( int i = 0; i < (theOperations.getSimulator().getScenario().getRoute(theOperations.getSimulator().getScenario().getRoute(h).getRouteNumber()).getNumRouteSchedules()); i++ ) {
                runningIds.add(theOperations.getSimulator().getScenario().getRoute(theOperations.getSimulator().getScenario().getRoute(h).getRouteNumber()).getRouteSchedule(i).toString());
            }
        }
        for ( int i = 0; i < allAllocations.size(); i++ ) {
            boolean keep = false;
            for ( int j = 0; j < runningIds.size(); j++ ) {
                logger.debug("This is " + allAllocations.get(i).split("&")[0].trim() + " against " + runningIds.get(j));
                if ( allAllocations.get(i).split("&")[0].trim().equalsIgnoreCase(runningIds.get(j)) ) {
                    keep = true;
                }
            }
            if ( keep == false ) {
                allAllocations.remove(i); i--;
            }
        }
        logger.debug("All allocations are: " + allAllocations.toString());
        logger.debug("Running Ids are: " + runningIds.toString());
        return allAllocations;
    }
    
    /**
     * Get the allocated vehicle for a particular schedule id.
     * @param routeSchedId a <code>String</code> with the schedule id.
     * @return a <code>Vehicle</code> object.
     */
    public Vehicle getAllocatedVehicle ( String routeSchedId ) {
        return theOperations.getRoute(routeSchedId.split("/")[0]).getAssignedVehicle(routeSchedId);
    }
    
    /**
     * Employ a new driver.
     * @param name a <code>String</code> with the driver's name.
     * @param hours a <code>int</code> with the contracted hours.
     * @param startDate a <code>Calendar</code> with the start date.
     * @return a <code>boolean</code> which is true iff the driver has been successfully employed.
     */
    public boolean employDriver ( String name, int hours, int rate, Calendar startDate ) {
        return theOperations.addDriver(name, hours, rate, startDate);
    }
    
    /**
     * Sack a driver.
     * @param d a <code>Driver</code> to sack.
     * @return a <code>boolean</code> which is true iff the driver was sacked.
     */
    public boolean sackDriver ( Driver d ) {
        return theOperations.sackDriver(d);
    }
    
    /**
     * Purchase a new vehicle.
     * @param type a <code>String</code> with the vehicle type.
     * @param deliveryDate a <code>Calendar</code> with the delivery date.
     * @return a <code>boolean</code> which is true iff the vehicle has been purchased successfully.
     */
    public boolean purchaseVehicle ( String type, Calendar deliveryDate ) {
        //Return result of ProgramOperations method.
        return theOperations.purchaseVehicle(type, deliveryDate);
    }
    
    /**
     * Get the balance.
     * @return a <code>double</code> with the balance amount.
     */
    public double getBalance ( ) {
        return getScenario().getBalance();
    }
    
    /**
     * Create vehicle object based on type.
     * This method needs to be updated in order to new vehicle types to TraMS.
     * @param pos a <code>int</code> with the supplied vehicle type position in the array.
     * @return a <code>Vehicle</code> object.
     */
    public Vehicle createVehicleObject ( int pos ) {
        return theOperations.createVehicleObject ( "AA", theOperations.getVehicleType(pos), getCurrentSimTime() );
    }
    
    /**
     * Get the number of available vehicle types.
     * @return a <code>int</code> with the number of available vehicle types.
     */
    public int getNumVehicleTypes ( ) {
        return theOperations.getNumVehicleTypes();
    }
    
    /**
     * Edit route - replace the two routes. 
     * @param oldRoute a <code>Route</code> object with the old route.
     * @param newRoute a <code>Route</code> object with the new route.
     * @return a <code>boolean</code> which is true iff the route was edited successfully.
     */
    public boolean editRoute ( Route oldRoute, Route newRoute ) {
        boolean success = false;
        //Delete old route.
        if ( deleteRoute(oldRoute) ) {
            //Add new vehicle.
            if ( addNewRoute(newRoute) ) {
                success = true;
            }
        }
        else {
            addNewRoute(newRoute);
        }
        //If fail to delete old, return false.
        return success;
    }
    
    /**
     * Sell a vehicle.
     * @param v a <code>Vehicle</code> to sell.
     * @return a <code>boolean</code> which is true iff the vehicle was sold.
     */
    public boolean sellVehicle ( Vehicle v ) {
        return theOperations.sellVehicle(v);
    }
    
    /**
     * Find the vehicle with the matching String representation.
     * @param vehicleData a <code>String</code> with the string representation.
     * @return a <code>Vehicle</code> object.
     */
    public Vehicle findVehicle ( String vehicleData ) {
        //Find vehicle with matching toString to the one supplied.
        for ( int i = 0; i < theOperations.getSimulator().getScenario().getNumberVehicles(); i++ ) {
            if ( theOperations.getSimulator().getScenario().getVehicle(i).toString().equalsIgnoreCase(vehicleData) ) {
                return theOperations.getSimulator().getScenario().getVehicle(i);
            }
        }
        //Return null if no vehicle found.
        return null;
    }
    
    /**
     * Get number of vehicles owned by this scenario.
     * @return a <code>int</code> with the number of vehicles.
     */
    public int getNumberVehicles ( ) {
        return theOperations.getSimulator().getScenario().getNumberVehicles();
    }

    public int getNumberDrivers ( ) {
        return theOperations.getSimulator().getScenario().getNumberDrivers();
    }
    
    /**
     * This method checks if any vehicles have been delivered to the company yet!
     * @return a <code>boolean</code> which is true iff some vehicles have been delivered!
     */
    public boolean hasSomeVehiclesBeenDelivered ( ) {
        if ( getNumberVehicles() == 0 ) { return false; }
        for ( int i = 0; i < getNumberVehicles(); i++ ) {
            if ( getVehicle(i).hasBeenDelivered(getCurrentSimTime()) ) { return true; }
        }
        return false;
    }

    /**
     * This method checks if any employees have started working for the company!
     * @return a <code>boolean</code> which is true iff some drivers have started working.
     */
    public boolean hasSomeDriversBeenEmployed ( ) {
        if ( getNumberDrivers() == 0 ) { return false; }
        for ( int i = 0; i < getNumberDrivers(); i++ ) {
        	if ( getDriver(i).hasStartedWork(getCurrentSimTime()) ) { return true; }
        }
        return false;
    }

    /**
     * Get a driver based on its position.
     * @param pos a <code>int</code> with the position.
     * @return a <code>Driver</code> object.
     */
    public Driver getDriver ( int pos ) {
        return theOperations.getSimulator().getScenario().getDriver(pos);
    }
    
    /**
     * Get a driver based on its id.
     * @param pos a <code>int</code> with the id.
     * @return a <code>Driver</code> object.
     */
    public Driver getDriverById ( int id ) {
        return theOperations.getSimulator().getScenario().getDriverById(id);
    }

    /**
     * Get a vehicle based on its position.
     * @param pos a <code>int</code> with the position.
     * @return a <code>Vehicle</code> object.
     */
    public Vehicle getVehicle ( int pos ) {
        return theOperations.getSimulator().getScenario().getVehicle(pos);
    }
    
    /**
     * Get a vehicle based on its id.
     * @param id a <code>String</code> with the id.
     * @return a <code>Vehicle</code> object.
     */
    public Vehicle getVehicle ( String id ) {
        return theOperations.getSimulator().getScenario().getVehicle(id);
    }
    
    /**
     * Sort vehicles by alphabetical order.
     */
    public void sortVehicles ( ) {
        theOperations.getSimulator().getScenario().sortVehicles();
    }
    
    /**
     * Sort drivers in ascending order.
     */
    public void sortDrivers ( ) {
    	theOperations.getSimulator().getScenario().sortDrivers();
    }
    
    /**
     * Set a new scenario.
     * @param newScenario a <code>Scenario</code> with the new scenario.
     */
    public void setScenario ( Scenario newScenario ) {
        theOperations.getSimulator().setScenario(newScenario);
    }
    
    /**
     * Get the current scenario.
     * @return a <code>Scenario</code> object.
     */
    public Scenario getScenario ( ) {
        return theOperations.getSimulator().getScenario();
    }
    
    /**
     * Main method to run the TraMS program.
     * @param args a <code>String</code> array which is not presently being used.
     */
    public static void main ( String[] args ) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        }
        catch ( Exception e ) { }
        //Display splash screen to the user.
        SplashScreen ss = new SplashScreen(false, null);
        for ( int i = 12; i > -5; i-- ) {
            try {
                Thread.sleep(200);
                ss.moveImage(10*(i+1),0);
            }
            catch ( InterruptedException ie ) { }
        }
        ss.dispose();
        UserInterface ui = new UserInterface();
        new WelcomeScreen(ui);
        //LoadingScreen ls = new LoadingScreen();
    }
    
}
