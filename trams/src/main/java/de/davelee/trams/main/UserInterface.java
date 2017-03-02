package de.davelee.trams.main;

import javax.swing.*;
import javax.swing.filechooser.*;

import de.davelee.trams.data.RouteSchedule;
import de.davelee.trams.data.Simulator;
import de.davelee.trams.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import de.davelee.trams.data.Vehicle;
import de.davelee.trams.gui.ControlScreen;
import de.davelee.trams.gui.SplashScreen;
import de.davelee.trams.gui.WelcomeScreen;
import de.davelee.trams.util.DateFormats;
import de.davelee.trams.util.DifficultyLevel;
import de.davelee.trams.util.MessageFolder;

/**
 * This class controls the user interface of the TraMS program. 
 * @author Dave Lee.
 */
public class UserInterface implements Runnable {
	
	private static final Logger logger = LoggerFactory.getLogger(UserInterface.class);
    
    private JFrame currentFrame;
    private ControlScreen controlScreen;
    private boolean end;
    private Thread runningThread;
    private boolean simulationRunning;
    private LinkedList<Integer> routeDetailPos;
    
    private int simulationSpeed = 2000;
    
    private final String version = "Final Preview Edition (Release Candidate)";
    
    //This is to decide which screen to show when we refresh.
    private boolean showingMessages = false;
    private boolean showingManagement = false;

    //This is where we keep the tip messages.
    LinkedList<String> tipMessages = new LinkedList<String>();
    
    private RouteScheduleService routeScheduleService;
    private JourneyService journeyService;
    private VehicleService vehicleService;
    private DriverService driverService;
    private GameService gameService;
    private SimulationService simulationService;
    private FactoryService springService;
    private ScenarioService scenarioService;
    private FileService fileService;
    private MessageService messageService;
    private RouteService routeService;
    
    /**
     * Create a new user interface - default constructor.
     */
    public UserInterface ( ) {
        //logger.debug("We are in ui constructor");
        end = false;
        simulationRunning = false;
        //Temporalily create list here.
        routeDetailPos = new LinkedList<Integer>();
        //Add tip messages here.
        tipMessages.add("TIP: Watch your balance! You can't buy new vehicles or run more routes if you don't have money!");
        tipMessages.add("TIP: Earn money by improving your passenger satisfaction through running vehicles on time!");
        tipMessages.add("TIP: If your passenger satisfaction falls too low, you may be forced to resign!");
    
        routeScheduleService = new RouteScheduleService();
        vehicleService = new VehicleService();
        driverService = new DriverService();
        gameService = new GameService();
        simulationService = new SimulationService();
        springService = new FactoryService();
        scenarioService = new ScenarioService();
        fileService = new FileService();
        routeService = new RouteService();
        journeyService = new JourneyService();
    }
    
    /**
     * Set the current frame displayed to the user.
     * @param currentFrame a <code>JFrame</code> with the current frame.
     */
    public void setFrame ( JFrame currentFrame ) {
        this.currentFrame = currentFrame;
    }
    
    /**
     * Get the current frame displayed to the user.
     * @return a <code>JFrame</code> with the current frame.
     */
    public JFrame getFrame ( ) {
        return currentFrame;
    }
    
    /**
     * Add a message to the message queue.
     * @param msg a <code>Message</code> object!
     */
    public void addMessage ( String subject, String text, String sender, MessageFolder folder, Calendar date ) {
        messageService.saveMessage( messageService.createMessage(subject, text, sender, 
        		folder, date));
    }
    
    /**
     * Get the current version number.
     * @return a <code>String</code> with the current version number.
     */
    public String getVersion ( ) {
        return version;
    }

    /**
     * Confirm and exit the TraMS program.
     */
    public void exit ( ) {
        //Confirm user did wish to exit.
        boolean wasSimulationRunning = false;
        if (simulationRunning) { pauseSimulation(); wasSimulationRunning = true; }
        int result = JOptionPane.showOptionDialog(currentFrame, "Are you sure you wish to exit TraMS?", "Please Confirm Exit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[] { "Yes", "No" }, "No");
        if ( result == JOptionPane.YES_OPTION ) {
            end = true;
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
        return tipMessages.get(r.nextInt(tipMessages.size()));
    }
    
    /**
     * Run simulation until pause is called.
     */
    @SuppressWarnings("static-access")
    public void run() {
        simulationRunning = true;
        //First of all, sleep for theSimulationSpeed seconds.
        try { runningThread.sleep(simulationSpeed); } catch (InterruptedException ie) {}
        //Keep running this until pause.
        while ( !end ) {
            //Increment time.
        	simulationService.incrementTime();
            controlScreen.drawVehicles(true);
            //Now sleep!
            try { runningThread.sleep(simulationSpeed); } catch (InterruptedException ie) {}
        }
    }
    
    /**
     * Speed up the simulation!
     */
    public void speedUpSimulation() {
        if ( simulationSpeed > 1000 ) {
            simulationSpeed -= 250;
        }
    }
    
    /**
     * Slow down the simulation!
     */
    public void slowSimulation() {
        if ( simulationSpeed < 4000 ) {
            simulationSpeed += 250;
        }
    }
    
    /**
     * Get the simulation speed!
     * @return a <code>int</code> with the simulation speed.
     */
    public int getSimulationSpeed() {
        return simulationSpeed;
    }
    
    /**
     * Set the control screen.
     * @param cs a <code>ControlScreen</code> object.
     */
    public void setControlScreen( ControlScreen cs ) {
        controlScreen = cs;
        logger.debug("Making control screen visible...");
        controlScreen.setVisible(true);
    }
    
    /**
     * Get the number of route display vehicles.
     * @param routeNumber a <code>String</code> with the route number.
     * @return a <code>int</code> with the number of route display vehicles.
     */
    public int getNumRouteDisplayVehicles ( String routeNumber ) {
        if ( routeNumber.equalsIgnoreCase("<No Routes Currently Registered>") ) { return 0; }
        return routeService.getRoute(routeNumber).getRouteSchedules().size();
    }
    
    /**
     * Get the difficulty level.
     * @return a <code>Enum</code> with the difficulty level.
     */
    public DifficultyLevel getDifficultyLevel ( ) {
        return gameService.getDifficultyLevel();
    }
    
    /**
     * Set the difficulty level.
     * @param diffLevel a <code>Enum</code> with the new difficulty level.
     */
    public void setDifficultyLevel ( DifficultyLevel diffLevel ) {
        gameService.setDifficultyLevel(diffLevel);
    }
    
    /**
     * Get a linked list of messages which are relevant for the specified folder, date and sender.
     * @param folder a <code>String</code> with the name of the folder.
     * @param date a <code>String</code> with the date.
     * @param sender a <code>String</code> with the sender.
     * @return a <code>LinkedList</code> with messages.
     */
    public long[] getMessageIds ( MessageFolder folder, String date, String sender ) {
        //Return a message list.
        return messageService.getMessageIds(messageService.getAllMessages(), folder, date, sender);
    }
    
    public String[] getMessageSubjects ( MessageFolder folder, String date, String sender ) {
    	long[] messageIds = messageService.getMessageIds(messageService.getAllMessages(), folder, date, sender);
    	String[] subjects = new String[messageIds.length];
    	for ( int i = 0; i < subjects.length; i++ ) {
    		subjects[i] = messageService.getMessageById(messageIds[i]).getSubject();
    	}
    	return subjects;
    }
    
    public String getMessageText ( MessageFolder folder, String date, String sender, int pos ) {
    	return messageService.getMessageById(messageService.getMessageIds(messageService.getAllMessages(), folder, date, sender)
    			[pos]).getText();
    }
    
    /**
     * Get the number of messages.
     * @return a <code>int</code> with the number of messages.
     */
    public int getNumberMessages ( ) {
        return messageService.getAllMessages().size();
    }
    
    /**
     * Get the message at the supplied position.
     * @param pos a <code>int</code> with the position.
     * @return a <code>Message</code> object which is at the supplied position.
     */
    public long getMessageId ( int pos ) {
        return messageService.getAllMessages().get(pos).getId();
    }

    /**
     * Get the number of stops for a particular route.
     * @param routeNumber a <code>String</code> with the route number.
     * @return a <code>int</code> with the number of stops.
     */
    public int getNumStops ( String routeNumber ) {
        return routeService.getRoute(routeNumber).getStops().size();
    }
    
    /**
     * Get the stop name for a particular route and particular stop number.
     * @param routeNumber a <code>String</code> with the route number.
     * @param pos a <code>int</code> with the stop position for that route.
     * @return a <code>String</code> with the stop name.
     */
    public String getStopName ( String routeNumber, int pos ) {
        return routeService.getRoute(routeNumber).getStops().get(pos).getStopName();
    }
    
    /**
     * Set the minimum and maximum display of vehicles.
     * @param min a <code>int</code> with the minimum.
     * @param max a <code>int</code> with the maximum.
     * @param routeNumber a <code>String</code> with the route number.
     */
    public void setCurrentDisplayMinMax ( int min, int max, String routeNumber ) {
        //Clear the original matrix for which routes to display.
        routeDetailPos = new LinkedList<Integer>();
        //Store the currentDate - we will need it for display schedules.
        Calendar currentTime = simulationService.getSimulator().getCurrentTime();
        //Determine the route ids we will display using these parameters.
        logger.debug("Route number is " + routeNumber);
        logger.debug(routeService.getRoute(routeNumber).toString());
        logger.debug("Number of possible display schedules: " +  routeService.getRoute(routeNumber).getRouteSchedules().size());
        if ( routeService.getRoute(routeNumber).getRouteSchedules().size() < max ) { max = routeService.getRoute(routeNumber).getRouteSchedules().size(); }
        //logger.debug("Max vehicles starts at " + max + " - routeDetails size is " + routeDetails.size());
        logger.debug("Min is " + min + " & Max is " + max);
        if ( min == max ) {
            if ( routeService.getRoute(routeNumber).getAssignedVehicle(routeService.getRoute(routeNumber).getRouteSchedules().get(min).toString()) == null ) {
                logger.debug("A schedule was null");
            }
            if ( getCurrentStopName(routeService.getRoute(routeNumber).getRouteSchedules().get(min).getId(), currentTime, getDifficultyLevel()).equalsIgnoreCase("Depot") ) {
                logger.debug("Vehicle in depot!");
            }
            if ( routeService.getRoute(routeNumber).getAssignedVehicle( routeService.getRoute(routeNumber).getRouteSchedules().get(min).toString()) != null && !getCurrentStopName( routeService.getRoute(routeNumber).getRouteSchedules().get(min).getId(), currentTime, getDifficultyLevel()).equalsIgnoreCase("Depot") ) {
                //logger.debug("Adding Route Detail " + routeDetails.get(i).getId());
                routeDetailPos.add(0);
            }
            else {
                max++;
                //logger.debug("Max is now " + max + " - routeDetails size is: " + routeDetails.size());
                if ( routeService.getRoute(routeNumber).getRouteSchedules().size() < max ) { max = routeService.getRoute(routeNumber).getRouteSchedules().size(); }
                //logger.debug("Route Detail " + routeDetails.get(i).getId() + " was null - maxVehicles is now " + max);
            }
        }
        for ( int i = min; i < max; i++ ) { //Changed from i = 0; i < routeDetails.size().
            if ( routeService.getRoute(routeNumber).getAssignedVehicle(routeService.getRoute(routeNumber).getRouteSchedules().get(i).toString()) == null ) {
                logger.debug("A schedule was null");
            }
            if ( getCurrentStopName(routeService.getRoute(routeNumber).getRouteSchedules().get(i).getId(), currentTime, getDifficultyLevel()).equalsIgnoreCase("Depot") ) {
                logger.debug("Vehicle in depot!");
            }
            if ( routeService.getRoute(routeNumber).getAssignedVehicle(routeService.getRoute(routeNumber).getRouteSchedules().get(i).toString()) != null && !getCurrentStopName(routeService.getRoute(routeNumber).getRouteSchedules().get(i).getId(), currentTime, getDifficultyLevel()).equalsIgnoreCase("Depot") ) {
                //logger.debug("Adding Route Detail " + routeDetails.get(i).getId());
                routeDetailPos.add(i);
            }
            else {
                max++;
                //logger.debug("Max is now " + max + " - routeDetails size is: " + routeDetails.size());
                if ( routeService.getRoute(routeNumber).getRouteSchedules().size() < max ) { max = routeService.getRoute(routeNumber).getRouteSchedules().size(); }
                //logger.debug("Route Detail " + routeDetails.get(i).getId() + " was null - maxVehicles is now " + max);
            }
        }
    }

    /**
     * Get the current stop name which this route schedule is on based on the current date.
     * @param currentDate a <code>Calendar</code> object.
     * @return a <code>String</code> array with the stop details.
     */
    public String getCurrentStopName ( long routeScheduleId, Calendar currentDate, DifficultyLevel difficultyLevel ) {
        //Copy current Date to current Time and then use delay to determine position.
        Calendar currentTime = (Calendar) currentDate.clone();
        currentTime.add(Calendar.MINUTE, -routeScheduleService.getRouteScheduleById(routeScheduleId).getDelayInMins());
        String stopName = journeyService.getCurrentStopName(routeScheduleService.getJourneyList(routeScheduleService.getRouteScheduleById(routeScheduleId)), currentTime);
        if ( stopName.contentEquals("Depot") ) {
            routeScheduleService.getRouteScheduleById(routeScheduleId).setDelayInMins(0); //Finished for the day or not started.
        }
        else {
            //Now fiddle delay!
            routeScheduleService.calculateNewDelay(routeScheduleService.getRouteScheduleById(routeScheduleId), difficultyLevel);
        }
        return stopName;
    }

    public String getLastStopName ( long routeScheduleId, Calendar currentDate, DifficultyLevel difficultyLevel) {
        //Copy current Date to current Time and then use delay to determine position.
        Calendar currentTime = (Calendar) currentDate.clone();
        currentTime.add(Calendar.MINUTE, -routeScheduleService.getRouteScheduleById(routeScheduleId).getDelayInMins());
        String stopName = journeyService.getLastStopName(routeScheduleService.getJourneyList(routeScheduleService.getRouteScheduleById(routeScheduleId)), currentTime);
        if ( stopName.contentEquals("Depot")) {
            routeScheduleService.getRouteScheduleById(routeScheduleId).setDelayInMins(0); //Finished for the day.
        }
        else {
            //Now fiddle delay!
            routeScheduleService.calculateNewDelay(routeScheduleService.getRouteScheduleById(routeScheduleId), difficultyLevel);
        }
        return stopName;
    }

    /**
     * Shorten schedule to the specific stop stated and reduce the delay accordingly.
     * @param stop a <code>String</code> with the stop to terminate at.
     * @param currentTime a <code>Calendar</code> with the current time.
     */
    public void shortenSchedule ( long routeScheduleId, String stop, Calendar currentTime ) {
        RouteSchedule schedule = routeScheduleService.getRouteScheduleById(routeScheduleId);
        //Shorten schedule to the specific stop stated and reduce the delay accordingly - for current service remove stops after the specified stop.
        //logger.debug("Service was ending at: " + theAssignedSchedule.getCurrentService().getEndDestination());
        String oldEnd = journeyService.getStop(journeyService.getCurrentJourney(schedule.getJourneyList(), currentTime).getId(), journeyService.getNumStops(journeyService.getCurrentJourney(schedule.getJourneyList(), currentTime))-1).getStopName();
        //Now we need to remove the stops in beteen!
        long timeDiff = journeyService.removeStopsBetween(journeyService.getCurrentJourney(schedule.getJourneyList(), currentTime), stop, oldEnd, false, true);
        //Now for the next service we need to remove stops between first stop and stop.
        long timeDiff2 = journeyService.removeStopsBetween(journeyService.getNextJourney(schedule.getJourneyList(), currentTime), journeyService.getStartTerminus(journeyService.getNextJourney(schedule.getJourneyList(), currentTime)), stop, false, true);
        //Divide both timeDiff's by 60 to convert to minutes and then use that to reduce vehicle delay.
        long delayReduction = (timeDiff/60) + (timeDiff2/60);
        //Reduce delay!
        routeScheduleService.reduceDelay(schedule, (int) delayReduction);
    }

    /**
     * Put this vehicle out of service from the current stop until the new stop.
     * @param currentStop a <code>String</code> with the stop to go out of service from.
     * @param newStop a <code>String</code> with the stop to resume service from.
     * @param currentTime a <code>Calendar</code> object with the current time.
     */
     public void outOfService ( long routeScheduleId, String currentStop, String newStop, Calendar currentTime ) {
        //Get the time difference between current stop and new stop.
        RouteSchedule schedule = routeScheduleService.getRouteScheduleById(routeScheduleId);
        long timeDiff = journeyService.getStopTimeDifference(journeyService.getCurrentJourney(schedule.getJourneyList(), currentTime), currentStop, newStop);
        routeScheduleService.reduceDelay(schedule, (int) (timeDiff/2));
        //logger.debug("Vehicle delay reduced from " + oldDelay + " mins to " + getVehicleDelay() + " mins.");
     }

    /**
     * Check if any vehicles are presently running based on the current time.
     * @param currentTime a <code>Calendar</code> object with the current time.
     * @return a <code>boolean</code> which is true iff at least one vehicle is running.
     */
    public boolean areAnyVehiclesRunning (Calendar currentTime, Simulator simulator, List<Vehicle> vehicles, DifficultyLevel difficultyLevel) {
        //Check if any vehicles are running....
        for ( Vehicle myVehicle : vehicles ) {
            //First one that is not in depot indicates that vehicles are running.
            if ( !getCurrentStopName(myVehicle.getRouteScheduleId(), currentTime, difficultyLevel).equalsIgnoreCase("Depot") ) {
                return true;
            }
        }
        //Otherwise, return false;
        return false;
    }
    
    /**
     * Get the current minimum vehicle.
     * @return a <code>int</code> with the id of the first vehicle.
     */
    public int getCurrentMinVehicle ( ) {
        return routeDetailPos.getFirst();
    }
    
    /**
     * Get the current maximum vehicle.
     * @return a <code>int</code> with the id of the second vehicle.
     */
    public int getCurrentMaxVehicle ( ) {
        return routeDetailPos.getLast();
    }
    
    /**
     * Get the current number of display schedules.
     * @return a <code>int</code> with the current number of display schedules.
     */
    public int getNumCurrentDisplaySchedules ( ) {
        return routeDetailPos.size();
    }
    
    /**
     * Get the display schedule based on route number and position.
     * @param routeNumber a <code>String</code> with the route number.
     * @param pos a <code>int</code> with the position.
     * @return a <code>long</code> object.
     */
    public long getDisplaySchedule ( String routeNumber, int pos ) {
        //Store the currentDate - we will need it for display schedules.
        return routeService.getRoute(routeNumber).getRouteSchedules().get(routeDetailPos.get(pos)).getId();
    }
    
    /**
     * Pause the simulation!
     */
    public void pauseSimulation ( ) {
        simulationRunning = false;
        //logger.debug("Pausing - Setting isEnd to true in " + this.toString());
        end = true;
    }
    
    /**
     * Resume the simulation!
     */
    public void resumeSimulation ( ) {
        simulationRunning = true;
        //logger.debug("Resuming - Setting isEnd to false");
        end = false;
        runningThread = new Thread(this, "SimThread");
        runningThread.start();
        //runSimulation(theControlScreen, theOperations.getSimulator());
    }
    
    /**
     * Load a scenario.
     * @param scenarioName a <code>String</code> with the scenario's name.
     * @param playerName a <code>String</code> with the player's name.
     * @return a <code>boolean</code> which is true iff the scenario has been created successfully.
     */
    public void loadScenario ( String scenarioName, String playerName ) {
        simulationService.createSimulator();
        List<Vehicle> vehicles = scenarioService.createSuppliedVehicles(springService.createScenarioObject(scenarioName).getSuppliedVehicles(),
    			simulationService.getSimulator().getCurrentTime(), vehicleService, springService);
        for ( int i = 0; i < vehicles.size(); i++ ) {
    		vehicleService.saveVehicle(vehicles.get(i));
    	}
       //Create welcome message.
       messageService.createMessage("Welcome Message", "Congratulations on your appointment as Managing Director of the " + getScenarioName() + "! \n\n Your targets for the coming days and months are: " + springService.createScenarioObject(getScenarioName()).getTargets(),"Council",MessageFolder.INBOX,simulationService.getSimulator().getCurrentTime());
    }
    
    /**
     * Run simulation!
     */
    public void runSimulation ( ) {
        currentFrame.dispose();
        ControlScreen cs = new ControlScreen(this, "", 0, 4, false);
        cs.drawVehicles(true);
        currentFrame.setVisible(true);
        //Set control screen.
        setControlScreen(cs);
        //Finally, run simulation
        end = false;
        runningThread = new Thread(this, "simThread");
        runningThread.start();
        //runSimulation(cs, theOperations.getSimulator());
    }
    
    /**
     * Change the selected route.
     * @param routeNumber a <code>String</code> with the new route number.
     */
    public void changeRoute ( String routeNumber ) {
        //Now create new control screen.
        ControlScreen cs = new ControlScreen(this, routeNumber, 0, 4, false);
        cs.drawVehicles(true);
        currentFrame.setVisible(true);
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
        ControlScreen cs = new ControlScreen(this, routeNumber, min, max, allocations);
        //cs.drawVehicles(true);
        currentFrame.setVisible(true);
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
        int returnVal = fileDialog.showSaveDialog(currentFrame);
        //Check if user submitted file.
        if ( returnVal == JFileChooser.APPROVE_OPTION ) {
            if ( fileService.saveFile(fileDialog.getSelectedFile()) ) {
                String fileName = fileDialog.getSelectedFile().getPath();
                if ( !fileName.endsWith(".tms") ) { fileName += ".tms"; }
                JOptionPane.showMessageDialog(currentFrame, "The current simulation has been successfully saved to " + fileName, "File Saved Successfully", JOptionPane.INFORMATION_MESSAGE);
                return true;
            }
            JOptionPane.showMessageDialog(currentFrame, "The file could not be saved. Please try again later.", "ERROR: File Could Not Be Saved", JOptionPane.ERROR_MESSAGE);
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
        int returnVal = fileDialog.showOpenDialog(currentFrame);
        //Check if user submitted file and print coming soon.
        boolean validFile = true;
        if ( returnVal == JFileChooser.APPROVE_OPTION) {
            if ( fileService.loadFile(fileDialog.getSelectedFile()) ) {
                JFrame oldFrame = currentFrame;
                setManagementScreen(true);
                ControlScreen cs = new ControlScreen(this, "", 0, 4, false);
                //cs.drawVehicles(false);
                //WelcomeScreen ws = new WelcomeScreen(this);
                //cs.setVisible(true);
                currentFrame.setVisible(true);
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
            JOptionPane.showMessageDialog(currentFrame,"The selected file is not compatible with this version of TraMS.\nYou may want to check the TraMS website for a convertor at http://trams.davelee.me.uk\nPlease either choose another file or create a new game.", "ERROR: Saved Game Could Not Be Loaded", JOptionPane.ERROR_MESSAGE);
        }
        //If we reach here then return false.
        return false;
    }
    
    /**
     * Get current simulated time. 
     * @return a <code>Calendar</code> representing the current simulated time.
     */
    public Calendar getCurrentSimTime ( ) {
        return simulationService.getSimulator().getCurrentTime();
    }
    
    /**
     * Add a new route.
     * @param r a <code>Route</code> object.
     */
    public void addNewRoute ( String routeNumber, String[] stopNames ) {
    	routeService.saveRoute(routeService.createRoute(routeNumber, stopNames));
    }
    
    /**
     * Delete route.
     * @param r a <code>Route</code> object to delete.
     */
    public void deleteRoute ( long routeId ) {
        routeService.removeRoute(routeService.getRouteById(routeId));
    }
    
    /**
     * Find a route with the matching string representation as the one supplied.
     * @param routeData a <code>String</code> with the route data.
     * @return a <code>Route</code> object.
     */
    public long findRoute ( String routeData ) {
        //Find route with matching toString to the one supplied.
        for ( int i = 0; i < routeService.getAllRoutes().size(); i++ ) {
            if ( routeService.getAllRoutes().get(i).toString().equalsIgnoreCase(routeData) ) {
                return routeService.getAllRoutes().get(i).getId();
            }
        }
        //Return null if no route found.
        return -1;
    }
    
    /**
     * Get the number of routes which this scenario currently has.
     * @return a <code>int</code> with the number of routes.
     */
    public int getNumberRoutes ( ) {
        return routeService.getAllRoutes().size();
    }
    
    /**
     * Get the route based on comparing the toString method with the supplied text.
     * @param routeStr a <code>String</code> with the string representation of the route.
     * @return a <code>Route</code> object matching the string representation.
     */
    public long getRoute ( String routeStr ) {
        String routeNumber = routeStr.split(":")[0];
        return routeService.getRoute(routeNumber).getId();
    }
    
    /**
     * Get the route which has the supplied number.
     * @param routeNumber a <code>String</code> with the route number.
     * @return a <code>Route</code> object which has that route number.
     */
    public long getRouteByNumber ( String routeNumber ) {
        return routeService.getRoute(routeNumber).getId();
    }

    public long getRoute ( int pos ) {
    	return routeService.getAllRoutes().get(pos).getId();
    }
    
    /**
     * Sort routes by route number alphabetically.
     */
    public void sortRoutes ( ) {
        routeService.sortRoutes(routeService.getAllRoutes());
    }
    
    /**
     * Get the list of allocations.
     * @return a <code>LinkedList</code> of allocations.
     */
    public ArrayList<String> getAllocations ( ) {
        return vehicleService.getAllocations();
    }
    
    /**
     * Get list of today's allocations.
     * @param currentDate a <code>String</code> with the current date.
     * @return a <code>LinkedList</code> of allocations.
     */
    public ArrayList<String> getTodayAllocations ( String currentDate ) {
        ArrayList<String> allAllocations = vehicleService.getAllocations();
        ArrayList<String> runningIds = new ArrayList<String>();
        for ( int h = 0; h < routeService.getAllRoutes().size(); h++ ) {
            for ( int i = 0; i < (routeService.getRoute(routeService.getAllRoutes().get(h).getRouteNumber()).getRouteSchedules().size()); i++ ) {
                runningIds.add(routeService.getRoute(routeService.getAllRoutes().get(h).getRouteNumber()).getRouteSchedules().get(i).toString());
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
    public long getAllocatedVehicle ( long routeScheduleId ) {
    	return vehicleService.getVehicleByRouteScheduleId(routeScheduleId).getId();
    }
    
    /**
     * Employ a new driver.
     * @param name a <code>String</code> with the driver's name.
     * @param hours a <code>int</code> with the contracted hours.
     * @param startDate a <code>Calendar</code> with the start date.
     * @return a <code>boolean</code> which is true iff the driver has been successfully employed.
     */
    public void employDriver ( String name, int hours, Calendar startDate ) {
    	//TODO: Employing drivers should cost money.
    	gameService.withdrawBalance(0);
    	driverService.saveDriver(driverService.createDriver(name, hours, startDate));
    }
    
    /**
     * Purchase a new vehicle.
     * @param type a <code>String</code> with the vehicle type.
     * @param deliveryDate a <code>Calendar</code> with the delivery date.
     * @return a <code>boolean</code> which is true iff the vehicle has been purchased successfully.
     */
    public void purchaseVehicle ( String type, Calendar deliveryDate ) {
        Vehicle vehicle = springService.createVehicleObject(type, vehicleService.generateRandomReg(
        		simulationService.getSimulator().getCurrentTime().get(Calendar.YEAR)), deliveryDate);
        gameService.withdrawBalance(vehicle.getPurchasePrice());
        vehicleService.saveVehicle(vehicle);
    }
    
    /**
     * Get the balance.
     * @return a <code>double</code> with the balance amount.
     */
    public double getBalance ( ) {
        return gameService.getCurrentBalance();
    }
    
    /**
     * Create vehicle object based on type.
     * This method needs to be updated in order to new vehicle types to TraMS.
     * @param pos a <code>int</code> with the supplied vehicle type position in the array.
     * @return a <code>Vehicle</code> object.
     */
    public long createVehicleObject ( int pos ) {
    	return springService.createVehicleObject ( springService.getVehicleModel(pos), "AA", getCurrentSimTime() ).getId();
    }
    
    /**
     * Get the number of available vehicle types.
     * @return a <code>int</code> with the number of available vehicle types.
     */
    public int getNumVehicleTypes ( ) {
        return springService.getNumberAvailableVehicles();
    }
    
    /**
     * Edit route - replace the two routes. 
     * @param oldRoute a <code>Route</code> object with the old route.
     * @param newRoute a <code>Route</code> object with the new route.
     */
    public void editRoute ( long routeId, String routeNumber, String[] stopNames ) {
        //Delete old route.
        deleteRoute(routeId);
        //Add new route.
        addNewRoute(routeNumber, stopNames);
    }
    
    /**
     * Sell a vehicle.
     * @param v a <code>Vehicle</code> to sell.
     * @return a <code>boolean</code> which is true iff the vehicle was sold.
     */
    public void sellVehicle ( long vehicleId ) {
        Vehicle vehicle = vehicleService.getVehicleById(vehicleId);
        gameService.creditBalance(vehicleService.getValue(vehicle.getPurchasePrice(), vehicle.getDepreciationFactor(), vehicle.getDeliveryDate(), simulationService.getSimulator().getCurrentTime()));
        vehicleService.removeVehicle(vehicle);
    }
    
    /**
     * Find the vehicle with the matching String representation.
     * @param vehicleData a <code>String</code> with the string representation.
     * @return a <code>Vehicle</code> object.
     */
    public long findVehicle ( String vehicleData ) {
        //Find vehicle with matching toString to the one supplied.
        for ( int i = 0; i < vehicleService.getAllVehicles().size(); i++ ) {
            if ( vehicleService.getAllVehicles().get(i).toString().equalsIgnoreCase(vehicleData) ) {
                return vehicleService.getAllVehicles().get(i).getId();
            }
        }
        //Return null if no vehicle found.
        return -1;
    }
    
    /**
     * Get number of vehicles owned by this scenario.
     * @return a <code>int</code> with the number of vehicles.
     */
    public int getNumberVehicles ( ) {
        return vehicleService.getAllVehicles().size();
    }

    public int getNumberDrivers ( ) {
        return driverService.getAllDrivers().size();
    }
    
    /**
     * This method checks if any vehicles have been delivered to the company yet!
     * @return a <code>boolean</code> which is true iff some vehicles have been delivered!
     */
    public boolean hasSomeVehiclesBeenDelivered ( ) {
        if ( getNumberVehicles() == 0 ) { return false; }
        for ( int i = 0; i < getNumberVehicles(); i++ ) {
            if ( vehicleService.hasBeenDelivered(vehicleService.getVehicleById(getVehicle(i)).getDeliveryDate(), getCurrentSimTime()) ) { return true; }
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
        	if ( driverService.hasStartedWork(driverService.getDriverById(getDriver(i)).getStartDate(), getCurrentSimTime()) ) { return true; }
        }
        return false;
    }

    /**
     * Get a driver based on its position.
     * @param pos a <code>int</code> with the position.
     * @return a <code>Driver</code> object.
     */
    public long getDriver ( int pos ) {
        return driverService.getAllDrivers().get(pos).getId();
    }

    /**
     * Get a vehicle based on its position.
     * @param pos a <code>int</code> with the position.
     * @return a <code>Vehicle</code> object.
     */
    public long getVehicle ( int pos ) {
        return vehicleService.getAllVehicles().get(pos).getId();
    }
    
    /**
     * Get a vehicle based on its id.
     * @param id a <code>String</code> with the id.
     * @return a <code>Vehicle</code> object.
     */
    public long getVehicle ( String id ) {
        return vehicleService.getVehicle(id).getId();
    }
    
    /**
     * Sort vehicles by alphabetical order.
     */
    public void sortVehicles ( ) {
        vehicleService.sortVehicles(vehicleService.getAllVehicles());
    }

    /**
     * Get the current scenario name.
     * @return a <code>String</code> object.
     */
    public String getScenarioName ( ) {
        return gameService.getScenarioName();
    }
    
    public long createScenarioObject ( String scenarioName ) {
    	return springService.createScenarioObject(scenarioName).getId();
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
    
    public void computePassengerSatisfaction ( Calendar currentTime ) {
    	gameService.computeAndReturnPassengerSatisfaction(currentTime, routeService.getAllRoutes());
    }
    
    public String formatDateString ( Calendar currentTime, DateFormats dateFormat ) {
    	return simulationService.formatDateString(currentTime, dateFormat);
    }
    
    public void setTimeIncrement ( int timeIncrement ) {
    	simulationService.getSimulator().setTimeIncrement(timeIncrement);
    }
    
    public int getTimeIncrement ( ) {
    	return simulationService.getSimulator().getTimeIncrement();
    }
    
    public Calendar getPreviousSimTime ( ) {
    	return simulationService.getSimulator().getPreviousTime();
    }
    
    public int getNumberScenarios ( ) {
    	return springService.getNumberAvailableScenarios();
    }
    
    public String getScenarioNameByPosition ( int position ) {
    	return springService.getAvailableScenarioNames()[position];
    }
    
    public String getScenarioCityDescriptionByPosition ( int position ) {
    	return springService.getAvailableScenarioCityDescriptions()[position];
    }
    
    public String getPlayerName ( ) {
    	return gameService.getPlayerName();
    }
    
    public String getScenarioDescriptionByName ( String name ) {
    	return springService.createScenarioObject(name).getDescription();
    }
    
    public String getAllocatedRegistrationNumber ( long routeScheduleId ) {
    	return vehicleService.getVehicleByRouteScheduleId(routeScheduleId).getRegistrationNumber();
    }
    
    public String getAllocatedVehicleImage ( long routeScheduleId ) {
    	return vehicleService.getVehicleByRouteScheduleId(routeScheduleId).getImagePath();
    }
    
    public void generateRouteSchedules ( long selectedRouteId ) {
    	routeService.generateRouteSchedules(selectedRouteId, getCurrentSimTime(), getScenarioName());
    }
    
    public long[] generateOutwardJourneyTimetables ( long selectedRouteId, Calendar cal ) {
    	return routeService.generateJourneyTimetables(selectedRouteId, cal, getScenarioName(), RouteService.OUTWARD_DIRECTION);
    }
    
    public long[] generateReturnJourneyTimetables ( long selectedRouteId, Calendar cal ) {
    	return routeService.generateJourneyTimetables(selectedRouteId, cal, getScenarioName(), RouteService.RETURN_DIRECTION);
    }
    
    public String getScenarioTargets ( ) {
    	return scenarioService.getScenarioById(createScenarioObject(getScenarioName())).getTargets();
    }
    
    public String getScenarioLocationMap ( ) {
    	return scenarioService.getScenarioById(createScenarioObject(getScenarioName())).getLocationMapFileName();
    }
    
    public boolean areRoutesDefined ( ) {
    	return routeService.getAllRoutes().size() > 0;
    }
    
    public int getPassengerSatisfaction ( ) {
    	return gameService.computeAndReturnPassengerSatisfaction(getCurrentSimTime(), routeService.getAllRoutes());
    }
    
    public int getMinimumSatisfaction ( ) {
    	return springService.createScenarioObject(getScenarioName()).getMinimumSatisfaction();
    }
    
    public Calendar getMessageDateByPosition ( int position ) {
    	return messageService.getMessageById(position).getDate();
    }
    
    public String getRouteNumber ( long id ) {
    	return routeService.getRouteById(id).getRouteNumber();
    }
    
}
