package de.davelee.trams.main;

import javax.swing.*;
import javax.swing.filechooser.*;

import de.davelee.trams.controllers.*;
import de.davelee.trams.data.*;
import de.davelee.trams.db.TramsFile;
import de.davelee.trams.model.RouteModel;
import de.davelee.trams.services.*;
import de.davelee.trams.util.SortedJourneys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import de.davelee.trams.gui.ControlScreen;
import de.davelee.trams.gui.SplashScreen;
import de.davelee.trams.gui.WelcomeScreen;
import de.davelee.trams.util.DateFormats;
import de.davelee.trams.util.DifficultyLevel;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This class controls the user interface of the TraMS program. 
 * @author Dave Lee.
 */
public class UserInterface {
	
	private static final Logger logger = LoggerFactory.getLogger(UserInterface.class);

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
    private GameService gameService;
    private ScenarioService scenarioService;
    private FileService fileService;
    private RouteService routeService;
    private JourneyPatternService journeyPatternService;
    private TimetableService timetableService;

    @Autowired
    private DriverController driverController;

    @Autowired
    private GameController gameController;

    @Autowired
    private MessageController messageController;

    @Autowired
    private RouteController routeController;

    @Autowired
    private RouteScheduleController routeScheduleController;
    
    /**
     * Create a new user interface - default constructor.
     */
    public UserInterface ( ) {
        //logger.debug("We are in ui constructor");
        //Temporalily create list here.
        routeDetailPos = new LinkedList<Integer>();
        //Add tip messages here.
        tipMessages.add("TIP: Watch your balance! You can't buy new vehicles or run more routes if you don't have money!");
        tipMessages.add("TIP: Earn money by improving your passenger satisfaction through running vehicles on time!");
        tipMessages.add("TIP: If your passenger satisfaction falls too low, you may be forced to resign!");
    
        routeScheduleService = new RouteScheduleService();
        vehicleService = new VehicleService();
        gameService = new GameService();
        scenarioService = new ScenarioService();
        fileService = new FileService();
        routeService = new RouteService();
        journeyService = new JourneyService();
        journeyPatternService = new JourneyPatternService();
        timetableService = new TimetableService();
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
    public void exit ( final JFrame currentFrame ) {
        //Confirm user did wish to exit.
        boolean wasSimulationRunning = gameController.pauseSimulation();
        int result = JOptionPane.showOptionDialog(currentFrame, "Are you sure you wish to exit TraMS?", "Please Confirm Exit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[] { "Yes", "No" }, "No");
        if ( result == JOptionPane.YES_OPTION ) {
            System.exit(0);
        }
        if (wasSimulationRunning) { gameController.resumeSimulation(); }
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
     * Get the number of route display vehicles.
     * @param routeNumber a <code>String</code> with the route number.
     * @return a <code>int</code> with the number of route display vehicles.
     */
    public int getNumRouteDisplayVehicles ( String routeNumber ) {
        if ( routeNumber.equalsIgnoreCase("<No Routes Currently Registered>") ) { return 0; }
        return routeScheduleService.getRouteSchedulesByRouteId(routeService.getRoute(routeNumber).getId()).size();
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
        Calendar currentTime = gameService.getCurrentTime();
        //Determine the route ids we will display using these parameters.
        logger.debug("Route number is " + routeNumber);
        logger.debug(routeService.getRoute(routeNumber).toString());
        logger.debug("Number of possible display schedules: " +  routeScheduleService.getRouteSchedulesByRouteId(routeService.getRoute(routeNumber).getId()).size());
        if ( routeScheduleService.getRouteSchedulesByRouteId(routeService.getRoute(routeNumber).getId()).size() < max ) { max = routeScheduleService.getRouteSchedulesByRouteId(routeService.getRoute(routeNumber).getId()).size(); }
        //logger.debug("Max vehicles starts at " + max + " - routeDetails size is " + routeDetails.size());
        logger.debug("Min is " + min + " & Max is " + max);
        if ( min == max ) {
            if ( vehicleService.getVehicleByRouteScheduleId(routeScheduleService.getRouteSchedulesByRouteId(routeService.getRoute(routeNumber).getId()).get(min).getId()) == null ) {
                logger.debug("A schedule was null");
            }
            if ( routeScheduleController.getCurrentStopName(routeScheduleService.getRouteSchedulesByRouteId(routeService.getRoute(routeNumber).getId()).get(min).getId(), currentTime, getDifficultyLevel()).equalsIgnoreCase("Depot") ) {
                logger.debug("Vehicle in depot!");
            }
            if ( vehicleService.getVehicleByRouteScheduleId(routeScheduleService.getRouteSchedulesByRouteId(routeService.getRoute(routeNumber).getId()).get(min).getId()) != null && !routeScheduleController.getCurrentStopName( routeScheduleService.getRouteSchedulesByRouteId(routeService.getRoute(routeNumber).getId()).get(min).getId(), currentTime, getDifficultyLevel()).equalsIgnoreCase("Depot") ) {
                //logger.debug("Adding Route Detail " + routeDetails.get(i).getId());
                routeDetailPos.add(0);
            }
            else {
                max++;
                //logger.debug("Max is now " + max + " - routeDetails size is: " + routeDetails.size());
                if ( routeScheduleService.getRouteSchedulesByRouteId(routeService.getRoute(routeNumber).getId()).size() < max ) { max = routeScheduleService.getRouteSchedulesByRouteId(routeService.getRoute(routeNumber).getId()).size(); }
                //logger.debug("Route Detail " + routeDetails.get(i).getId() + " was null - maxVehicles is now " + max);
            }
        }
        for ( int i = min; i < max; i++ ) { //Changed from i = 0; i < routeDetails.size().
            if ( vehicleService.getVehicleByRouteScheduleId(routeScheduleService.getRouteSchedulesByRouteId(routeService.getRoute(routeNumber).getId()).get(i).getId()) == null ) {
                logger.debug("A schedule was null");
            }
            if ( routeScheduleController.getCurrentStopName(routeScheduleService.getRouteSchedulesByRouteId(routeService.getRoute(routeNumber).getId()).get(i).getId(), currentTime, getDifficultyLevel()).equalsIgnoreCase("Depot") ) {
                logger.debug("Vehicle in depot!");
            }
            if ( vehicleService.getVehicleByRouteScheduleId(routeScheduleService.getRouteSchedulesByRouteId(routeService.getRoute(routeNumber).getId()).get(i).getId()) != null && !routeScheduleController.getCurrentStopName(routeScheduleService.getRouteSchedulesByRouteId(routeService.getRoute(routeNumber).getId()).get(i).getId(), currentTime, getDifficultyLevel()).equalsIgnoreCase("Depot") ) {                //logger.debug("Adding Route Detail " + routeDetails.get(i).getId());
                routeDetailPos.add(i);
            }
            else {
                max++;
                //logger.debug("Max is now " + max + " - routeDetails size is: " + routeDetails.size());
                if ( routeScheduleService.getRouteSchedulesByRouteId(routeService.getRoute(routeNumber).getId()).size() < max ) { max = routeScheduleService.getRouteSchedulesByRouteId(routeService.getRoute(routeNumber).getId()).size(); }
                //logger.debug("Route Detail " + routeDetails.get(i).getId() + " was null - maxVehicles is now " + max);
            }
        }
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
        String oldEnd = journeyService.getStopNameByStopId(journeyService.getStopTime(journeyService.getCurrentJourney(journeyService.getJourneysByRouteScheduleId(routeScheduleId), currentTime).getId(),
                journeyService.getNumStops(journeyService.getCurrentJourney(journeyService.getJourneysByRouteScheduleId(routeScheduleId), currentTime))-1).getStopId());
        //Now we need to remove the stops in beteen!
        long timeDiff = journeyService.removeStopsBetween(journeyService.getCurrentJourney(journeyService.getJourneysByRouteScheduleId(routeScheduleId), currentTime), stop, oldEnd, false, true);
        //Now for the next service we need to remove stops between first stop and stop.
        long timeDiff2 = journeyService.removeStopsBetween(journeyService.getNextJourney(journeyService.getJourneysByRouteScheduleId(routeScheduleId), currentTime), journeyService.getStartTerminus(journeyService.getNextJourney(journeyService.getJourneysByRouteScheduleId(routeScheduleId), currentTime)), stop, false, true);
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
        long timeDiff = journeyService.getStopTimeDifference(journeyService.getCurrentJourney(journeyService.getJourneysByRouteScheduleId(routeScheduleId), currentTime), currentStop, newStop);
        routeScheduleService.reduceDelay(schedule, (int) (timeDiff/2));
        //logger.debug("Vehicle delay reduced from " + oldDelay + " mins to " + getVehicleDelay() + " mins.");
     }

    /**
     * Check if any vehicles are presently running based on the current time.
     * @param currentTime a <code>Calendar</code> object with the current time.
     * @return a <code>boolean</code> which is true iff at least one vehicle is running.
     */
    public boolean areAnyVehiclesRunning (Calendar currentTime, List<Vehicle> vehicles, DifficultyLevel difficultyLevel) {
        //Check if any vehicles are running....
        for ( Vehicle myVehicle : vehicles ) {
            //First one that is not in depot indicates that vehicles are running.
            if ( !routeScheduleController.getCurrentStopName(myVehicle.getRouteScheduleId(), currentTime, difficultyLevel).equalsIgnoreCase("Depot") ) {
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

    public void generateRouteSchedules (final RouteModel routeModel, Calendar currentTime, String scenarioName ) {
        //Initialise parameters.
        long[] outgoingJourneyIds = generateJourneyTimetables(routeModel, currentTime, scenarioName, RouteService.OUTWARD_DIRECTION);
        long[] returnJourneyIds = generateJourneyTimetables(routeModel, currentTime, scenarioName, RouteService.RETURN_DIRECTION);
        List<Journey> outgoingJourneys = new ArrayList<Journey>();
        for ( int i = 0; i < outgoingJourneyIds.length; i++ ) {
            outgoingJourneys.add(journeyService.getJourneyById(outgoingJourneyIds[i]));
        }
        List<Journey> returnJourneys = new ArrayList<Journey>();
        for ( int i = 0; i < returnJourneyIds.length; i++ ) {
            returnJourneys.add(journeyService.getJourneyById(returnJourneyIds[i]));
        }
        //We need to repeat this loop until both outgoingJourneys and returnJourneys are empty!
        int counter = 1;
        while ( outgoingJourneys.size() > 0 || returnJourneys.size() > 0 ) {
            //Control what journey we want - initially we don't care.
            boolean wantOutgoing = true; boolean wantReturn = true;
            //Create a new route schedule.
            RouteSchedule mySchedule = new RouteSchedule ( );
            mySchedule.setScheduleNumber(counter);
            //Create our calendar object and set it to midnight.
            Calendar myCal = new GregorianCalendar(2009,7,7,0,0);
            //Find whether the first outgoing journey time is before the first return journey time.
            if ( returnJourneys.size() > 0 && outgoingJourneys.size() > 0 && journeyService.getStopTimesByJourneyId(outgoingJourneys.get(0).getId()).get(0).getTime().after(journeyService.getStopTimesByJourneyId(returnJourneys.get(0).getId()).get(0).getTime()) ) {
                myCal = (Calendar) journeyService.getStopTimesByJourneyId(returnJourneys.get(0).getId()).get(0).getTime().clone();
            }
            else if ( outgoingJourneys.size() == 0 ) {
                myCal = (Calendar) journeyService.getStopTimesByJourneyId(returnJourneys.get(0).getId()).get(0).getTime().clone();
            }
            else {
                myCal = (Calendar) journeyService.getStopTimesByJourneyId(outgoingJourneys.get(0).getId()).get(0).getTime().clone();
            }
            //Here's the loop.
            while ( true ) {
                //logger.debug("Schedule " + counter + " Time is now " + myCal.get(Calendar.HOUR_OF_DAY) + ":" + myCal.get(Calendar.MINUTE));
                if ( outgoingJourneys.size() > 0 && returnJourneys.size() > 0 ) {
                    if ( myCal.after(journeyService.getStopTimesByJourneyId(outgoingJourneys.get(outgoingJourneys.size()-1).getId()).get(0).getTime()) && myCal.after(journeyService.getStopTimesByJourneyId(returnJourneys.get(returnJourneys.size()-1).getId()).get(0).getTime())) {
                        break;
                    }
                } else if ( outgoingJourneys.size() > 0 ) {
                    if ( myCal.after(journeyService.getStopTimesByJourneyId(outgoingJourneys.get(outgoingJourneys.size()-1).getId()).get(0).getTime()) ) {
                        break;
                    }
                } else if ( returnJourneys.size() > 0 ) {
                    if ( myCal.after(journeyService.getStopTimesByJourneyId(returnJourneys.get(outgoingJourneys.size()-1).getId()).get(0).getTime()) ) {
                        break;
                    }
                } else {
                    break; //Both outgoing journeys and return journeys were 0 so finished.
                }
                int loopPos = 0;
                while ( true ) {
                    if ( loopPos >= outgoingJourneys.size() && loopPos >= returnJourneys.size() ) { break; }
                    if ( loopPos < outgoingJourneys.size() ) {
                        //if ( wantOutgoing ) { logger.debug("I want an outgoing service so trying: " + returnServices.get(loopPos).getAllDisplayStops() + " to route schedule " + counter); }
                        if ( wantOutgoing && journeyService.getStopTimesByJourneyId(outgoingJourneys.get(loopPos).getId()).get(0).getTime().get(Calendar.HOUR_OF_DAY) == myCal.get(Calendar.HOUR_OF_DAY) && journeyService.getStopTimesByJourneyId(outgoingJourneys.get(loopPos).getId()).get(0).getTime().get(Calendar.MINUTE) == myCal.get(Calendar.MINUTE)) {
                            //logger.debug("Adding service " + outgoingServices.get(loopPos).getAllDisplayStops() + " to route schedule " + counter);
                            //We have found our journey - its an outgoing one!!!
                            outgoingJourneys.get(loopPos).setRouteScheduleId(mySchedule.getId());
                            //Set calendar equal to last stop time.
                            myCal = (Calendar) journeyService.getStopTimesByJourneyId(outgoingJourneys.get(loopPos).getId()).get(journeyService.getStopTimesByJourneyId(outgoingJourneys.get(loopPos).getId()).size()-1).getTime().clone();
                            //myCal.add(Calendar.MINUTE, -1); //This prevents bad effect of adding one later!
                            //Remove this journey from the list.
                            outgoingJourneys.remove(loopPos);
                            //Note that we next want a return one.
                            wantOutgoing = false; wantReturn = true;
                            //Continue loop.
                            continue;
                        }
                    }
                    if ( loopPos < returnJourneys.size() ) {
                        //if ( wantReturn ) { logger.debug("I want a return service so trying: " + returnServices.get(loopPos).getAllDisplayStops() + " to route schedule " + counter); }
                        if ( wantReturn && journeyService.getStopTimesByJourneyId(returnJourneys.get(loopPos).getId()).get(0).getTime().get(Calendar.HOUR_OF_DAY) == myCal.get(Calendar.HOUR_OF_DAY) && journeyService.getStopTimesByJourneyId(returnJourneys.get(loopPos).getId()).get(0).getTime().get(Calendar.MINUTE) == myCal.get(Calendar.MINUTE)) {
                            //logger.debug("Adding service " + returnServices.get(loopPos).getAllDisplayStops() + " to route schedule " + counter);
                            //We have found our journey - its a return one!!!
                            returnJourneys.get(loopPos).setRouteScheduleId(mySchedule.getId());
                            //Set calendar equal to last stop time.
                            myCal = (Calendar) journeyService.getStopTimesByJourneyId(returnJourneys.get(loopPos).getId()).get(journeyService.getStopTimesByJourneyId(returnJourneys.get(loopPos).getId()).size()-1).getTime().clone();
                            //myCal.add(Calendar.MINUTE, -1); //This prevents bad effect of adding one later!
                            //Remove this journey from the list.
                            returnJourneys.remove(loopPos);
                            //Note that we next want an outgoing one.
                            wantReturn = false; wantOutgoing = true;
                            //Continue loop.
                            continue;
                        }
                    }
                    //Increment loopPos.
                    loopPos++;
                }
                //Increment calendar and loopPos.
                myCal.add(Calendar.MINUTE, 1);
            }
            //Add route schedule to database.
            routeScheduleService.saveRouteSchedule(mySchedule);
            //Increment counter.
            counter++;
        }
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
        return routeScheduleService.getRouteSchedulesByRouteId(routeService.getRoute(routeNumber).getId()).get(routeDetailPos.get(pos)).getId();
    }
    
    /**
     * Load a scenario.
     * @param scenarioName a <code>String</code> with the scenario's name.
     * @param playerName a <code>String</code> with the player's name.
     * @return a <code>boolean</code> which is true iff the scenario has been created successfully.
     */
    public void loadScenario ( String scenarioName, String playerName ) {
        List<Vehicle> vehicles = createSuppliedVehicles(scenarioName, gameService.getCurrentTime());
        for ( int i = 0; i < vehicles.size(); i++ ) {
    		vehicleService.saveVehicle(vehicles.get(i));
    	}
        //Create welcome message.
        messageController.addMessage("Welcome Message", "Congratulations on your appointment as Managing Director of the " + getScenarioName() + "! \n\n Your targets for the coming days and months are: " + scenarioService.retrieveScenarioObject(getScenarioName()).getTargets(),"Council","INBOX",gameService.getCurrentTime());
    }
    
    /**
     * Run simulation!
     */
    public void runSimulation (final JFrame currentFrame) {
        currentFrame.dispose();
        ControlScreen cs = new ControlScreen(this, "", 0, 4, false);
        cs.drawVehicles(true);
        currentFrame.setVisible(true);
        //Set control screen.
        cs.setVisible(true);
    }
    
    /**
     * Change the selected route.
     * @param routeNumber a <code>String</code> with the new route number.
     */
    public void changeRoute ( String routeNumber, final JFrame currentFrame ) {
        //Now create new control screen.
        ControlScreen cs = new ControlScreen(this, routeNumber, 0, 4, false);
        cs.drawVehicles(true);
        currentFrame.setVisible(true);
        //Set control screen.
        cs.setVisible(true);
        //Resume simulation.
        gameController.resumeSimulation();
    }
    
    /**
     * Change the display to show other vehicles.
     * @param routeNumber a <code>String</code> with the route number.
     * @param min a <code>int</code> with the new min vehicle id.
     * @param max a <code>int</code> with the new max vehicle id.
     * @param allocations a <code>boolean</code> which is true iff allocations have been performed.
     */
    public void changeDisplay ( String routeNumber, int min, int max, boolean allocations, final JFrame currentFrame ) {
        //Now create new control screen.
        ControlScreen cs = new ControlScreen(this, routeNumber, min, max, allocations);
        //cs.drawVehicles(true);
        currentFrame.setVisible(true);
        //Set control screen.
        cs.setVisible(true);
        //Resume simulation.
        gameController.resumeSimulation();
    }
    
    /**
     * Save file.
     * @return a <code>boolean</code> which is true iff the file was saved successfully.
     */
    public boolean saveFile ( final JFrame currentFrame ) {
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
            if ( fileService.saveFile(fileDialog.getSelectedFile(), prepareTramsFile()) ) {
                String fileName = fileDialog.getSelectedFile().getPath();
                if ( !fileName.endsWith(".tms") ) { fileName += ".tms"; }
                JOptionPane.showMessageDialog(currentFrame, "The current simulation has been successfully saved to " + fileName, "File Saved Successfully", JOptionPane.INFORMATION_MESSAGE);
                return true;
            }
            JOptionPane.showMessageDialog(currentFrame, "The file could not be saved. Please try again later.", "ERROR: File Could Not Be Saved", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    public TramsFile prepareTramsFile ( ) {
        return new TramsFile(driverController.getAllDrivers(), gameService.getGame(), journeyService.getAllJourneys(),
                journeyPatternService.getAllJourneyPatterns(), messageController.getAllMessages(), routeService.getAllRoutes(),
                routeScheduleService.getAllRouteSchedules(), journeyService.getAllStops(), journeyService.getAllStopTimes(),
                timetableService.getAllTimetables(), vehicleService.getAllVehicles());
    }

    public void reloadDatabaseWithFile ( TramsFile myFile ) {
        //TODO: Load file into database!
    }
    
    /**
     * Load file. 
     * @return a <code>boolean</code> which is true iff the file was loaded successfully.
     */
    public boolean loadFile ( final JFrame currentFrame ) {
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
            TramsFile myFile = fileService.loadFile(fileDialog.getSelectedFile());
            if ( myFile != null ) {
                reloadDatabaseWithFile(myFile);
                JFrame oldFrame = currentFrame;
                setManagementScreen(true);
                ControlScreen cs = new ControlScreen(this, "", 0, 4, false);
                //cs.drawVehicles(false);
                //WelcomeScreen ws = new WelcomeScreen(this);
                //cs.setVisible(true);
                currentFrame.setVisible(true);
                oldFrame.dispose();
                //Set control screen.
                cs.setVisible(true);
                //Finally, run simulation
                /*isEnd = false;
                theRunningThread = new Thread(this, "simThread");
                theRunningThread.start();*/
                gameController.pauseSimulation();
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
            for ( int i = 0; i < (routeScheduleService.getRouteSchedulesByRouteId(routeService.getRoute(routeService.getAllRoutes().get(h).getRouteNumber()).getId()).size()); i++ ) {
                runningIds.add(routeScheduleService.getRouteSchedulesByRouteId(routeService.getRoute(routeService.getAllRoutes().get(h).getRouteNumber()).getId()).get(i).toString());
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
     * Get the balance.
     * @return a <code>double</code> with the balance amount.
     */
    public double getBalance ( ) {
        return gameService.getCurrentBalance();
    }
    
    /**
     * Get the number of available vehicle types.
     * @return a <code>int</code> with the number of available vehicle types.
     */
    public int getNumVehicleTypes ( ) {
        return vehicleService.getNumberAvailableVehicles();
    }
    
    /**
     * Edit route - replace the two routes. 
     * @param oldRoute a <code>Route</code> object with the old route.
     * @param newRoute a <code>Route</code> object with the new route.
     */
    public void editRoute ( long routeId, String routeNumber, String[] stopNames ) {
        //Delete old route.
        routeController.deleteRoute(routeId);
        //Add new route.
        routeController.addNewRoute(routeNumber, stopNames);
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
    
    public String formatDateString ( Calendar currentTime, DateFormats dateFormat ) {
    	return gameService.formatDateString(currentTime, dateFormat);
    }
    
    public void setTimeIncrement ( int timeIncrement ) {
    	gameService.setTimeIncrement(timeIncrement);
    }
    
    public int getTimeIncrement ( ) {
    	return gameService.getTimeIncrement();
    }
    
    public Calendar getPreviousSimTime ( ) {
    	return gameService.getPreviousTime();
    }
    
    public int getNumberScenarios ( ) {
        return scenarioService.getNumberAvailableScenarios();
    }
    
    public String getScenarioNameByPosition ( int position ) {
        return scenarioService.getAvailableScenarioNames()[position];
    }
    
    public String getScenarioCityDescriptionByPosition ( int position ) {
        return scenarioService.getAvailableScenarioCityDescriptions()[position];
    }
    
    public String getPlayerName ( ) {
    	return gameService.getPlayerName();
    }
    
    public String getScenarioDescriptionByName ( String name ) {
        return scenarioService.retrieveScenarioObject(name).getDescription();
    }
    
    public String getAllocatedRegistrationNumber ( long routeScheduleId ) {
    	return vehicleService.getVehicleByRouteScheduleId(routeScheduleId).getRegistrationNumber();
    }
    
    public String getAllocatedVehicleImage ( long routeScheduleId ) {
    	return vehicleService.getVehicleByRouteScheduleId(routeScheduleId).getImagePath();
    }
    
    public void generateRouteSchedules ( final RouteModel routeModel ) {
        generateRouteSchedules(routeModel, gameController.getCurrentSimTime(), getScenarioName());
    }
    
    public long[] generateOutwardJourneyTimetables ( final RouteModel routeModel, Calendar cal ) {
        return generateJourneyTimetables(routeModel, cal, getScenarioName(), RouteService.OUTWARD_DIRECTION);
    }
    
    public long[] generateReturnJourneyTimetables ( final RouteModel routeModel, Calendar cal ) {
        return generateJourneyTimetables(routeModel, cal, getScenarioName(), RouteService.RETURN_DIRECTION);
    }
    
    public String getScenarioTargets ( ) {
        return scenarioService.retrieveScenarioObject(getScenarioName()).getTargets();
    }
    
    public String getScenarioLocationMap ( ) {
        return scenarioService.retrieveScenarioObject(getScenarioName()).getLocationMapFileName();
    }

    public int computeAndReturnPassengerSatisfaction ( ) {
        //Essentially satisfaction is determined by the route schedules that are running on time.
        //Now count number of route schedules into three groups: 1 - 5 minutes late, 6 - 15 minutes late, 16+ minutes late.
        int numSmallLateSchedules = 0; int numMediumLateSchedules = 0; int numLargeLateSchedules = 0;
        //Now go through all routes.
        for ( Route myRoute : routeService.getAllRoutes() ) {
             for ( RouteSchedule mySchedule : routeScheduleService.getRouteSchedulesByRouteId(myRoute.getId())) {
                //Running... 1 - 5 minutes late.
                if ( mySchedule.getDelayInMins() > 0 && mySchedule.getDelayInMins() < 6 ) {
                    numSmallLateSchedules++;
                }
                //Running... 6 - 15 minutes late.
                else if ( mySchedule.getDelayInMins() > 5 && mySchedule.getDelayInMins() < 16 ) {
                    numMediumLateSchedules++;
                }
                //Running... 16+ minutes late.
                else if ( mySchedule.getDelayInMins() > 15 ) {
                    numLargeLateSchedules++;
                }
             }
        }
        return gameService.computeAndReturnPassengerSatisfaction(numSmallLateSchedules, numMediumLateSchedules, numLargeLateSchedules);
    }
    
    public int getMinimumSatisfaction ( ) {
        return scenarioService.retrieveScenarioObject(getScenarioName()).getMinimumSatisfaction();
    }

    public List<Vehicle> createSuppliedVehicles( final String scenarioName, Calendar currentTime) {
        List<Vehicle> vehicles = new ArrayList<Vehicle>();
        Iterator<String> vehicleModels = scenarioService.retrieveScenarioObject(scenarioName).getSuppliedVehicles().keySet().iterator();
        while (vehicleModels.hasNext()) {
            String vehicleModel = vehicleModels.next();
            for ( int i = 0; i < scenarioService.retrieveScenarioObject(scenarioName).getSuppliedVehicles().get(vehicleModel); i++ )  {
                vehicles.add(vehicleService.createVehicleObject(vehicleModel, vehicleService.generateRandomReg(
                        currentTime.get(Calendar.YEAR)), currentTime));
            }
        }
        return vehicles;
    }

    public VehicleService getVehicleService() {
        return vehicleService;
    }

    public void setVehicleService(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    public ScenarioService getScenarioService() {
        return scenarioService;
    }

    public void setScenarioService(ScenarioService scenarioService) {
        this.scenarioService = scenarioService;
    }

    /**
     * Return a formatted String array of schedule dates from today.
     * @param today a <code>Calendar</code> object with the current date.
     * @return a <code>String</code> array of possible schedule dates.
     */
    public String[] getPossibleSchedulesDates ( final RouteModel routeModel, Calendar today ) {
        //Create the list.
        List<Calendar> myCalendar = new ArrayList<Calendar>();
        //Go through all of the timetables and add them if they are not already in.
        List<Timetable> timetables = timetableService.getTimetablesByRouteId(routeController.getRouteId(routeModel.getRouteNumber()));
        Calendar thisDate;
        for (Timetable timeT : timetables) {
            thisDate = (Calendar) today.clone();
            //Now check if we have passed the valid to date.
            while ( !thisDate.after(timeT.getValidToDate()) ) {
                //Check if we have added this date before...
                if ( !myCalendar.contains(thisDate) ) {
                    //Finally check that at least one of the journey patterns has an operating service on this day.
                    List<JourneyPattern> journeyPatterns = journeyPatternService.getJourneyPatternsByTimetableId(timeT.getId());
                    for ( JourneyPattern jp : journeyPatterns ) {
                        if ( jp.getDaysOfOperation().contains(thisDate.get(Calendar.DAY_OF_WEEK)) ) {
                            myCalendar.add(thisDate);
                            break;
                        }
                    }
                }
                thisDate = ((Calendar) (thisDate.clone()));
                thisDate.add(Calendar.HOUR, 24);
            }
        }
        Collections.sort(myCalendar);
        String[] myCalDates = new String[myCalendar.size()];
        logger.debug("MyCalDates length is " + myCalDates.length);
        for ( int i = 0; i < myCalDates.length; i++ ) {
            myCalDates[i] = DateFormats.FULL_FORMAT.getFormat().format(myCalendar);
        }
        return myCalDates;
    }

    /**
     * This method generates the route timetables for a particular day - it is a very important method.
     * @param today a <code>Calendar</code> object with today's date.
     */
    public long[] generateJourneyTimetables ( final RouteModel routeModel, Calendar today, String scenarioName, int direction ) {
        logger.debug("I'm generating timetable for routeId " + routeController.getRouteId(routeModel.getRouteNumber()) + " for " + DateFormats.DAY_MONTH_YEAR_FORMAT.getFormat().format(today.getTime()));
        //First of all, get the current timetable.
        Timetable currentTimetable = timetableService.getCurrentTimetable(routeController.getRouteId(routeModel.getRouteNumber()), today);
        //Create a list to store journeys.
        List<Journey> allJourneys = new ArrayList<Journey>();
        //Now we need to go through the journey patterns.
        List<JourneyPattern> journeyPatterns = journeyPatternService.getJourneyPatternsByTimetableId(currentTimetable.getId());
        for ( JourneyPattern myJourneyPattern : journeyPatterns ) {
            //Clone the time so that we can add to it but keep it the same for next iteration.
            Calendar myTime = (Calendar) today.clone();
            //If this service pattern is not valid for this date then don't bother.
            if ( myJourneyPattern.getDaysOfOperation().contains(myTime.get(Calendar.DAY_OF_WEEK))) { continue; }
            //Set myTime hour and minute to the start time of the service pattern.
            myTime.set(Calendar.HOUR_OF_DAY, myJourneyPattern.getStartTime().get(Calendar.HOUR_OF_DAY));
            myTime.set(Calendar.MINUTE, myJourneyPattern.getStartTime().get(Calendar.MINUTE));
            int diffDurationFreq = myJourneyPattern.getRouteDuration() % myJourneyPattern.getFrequency();
            if ( direction == RouteService.RETURN_DIRECTION && diffDurationFreq <= (myJourneyPattern.getFrequency()/2)) {
                myTime.add(Calendar.MINUTE, myJourneyPattern.getFrequency()/2);
            }
            //logger.debug("End time is " + myJourneyPattern.getEndTime().get(Calendar.HOUR_OF_DAY) + ":" + myJourneyPattern.getEndTime().get(Calendar.MINUTE) );
            //Now repeat this loop until myTime is after the journey pattern end time.
            while ( true ) {
                if ( (myTime.get(Calendar.HOUR_OF_DAY) > myJourneyPattern.getEndTime().get(Calendar.HOUR_OF_DAY)) ) { break; }
                else if ( (myTime.get(Calendar.HOUR_OF_DAY) == myJourneyPattern.getEndTime().get(Calendar.HOUR_OF_DAY)) && (myTime.get(Calendar.MINUTE) > myJourneyPattern.getEndTime().get(Calendar.MINUTE)) ) { break; }
                else {
                    //logger.debug("I want a journey starting from both terminuses at " + myTime.get(Calendar.HOUR_OF_DAY) + ":" + myTime.get(Calendar.MINUTE));
                    //Create an outgoing service.
                    Journey newJourney = new Journey();
                    //Add stops - we also need to create a separate calendar to ensure we don't advance more than we want!!!!
                    Calendar journeyTime = (Calendar) myTime.clone();
                    List<Stop> journeyStops = new ArrayList<Stop>();
                    if ( direction == RouteService.OUTWARD_DIRECTION ) {
                        journeyStops = routeService.getStopsBetween(routeService.getRouteById(routeController.getRouteId(routeModel.getRouteNumber())), myJourneyPattern.getReturnTerminus(), myJourneyPattern.getOutgoingTerminus(), direction);
                    }
                    else {
                        journeyStops = routeService.getStopsBetween(routeService.getRouteById(routeController.getRouteId(routeModel.getRouteNumber())), myJourneyPattern.getOutgoingTerminus(), myJourneyPattern.getReturnTerminus(), direction);
                    }
                    long stopId = journeyService.getStopByStopName(journeyStops.get(0).getStopName()).getId();
                    StopTime newStopTime = new StopTime();
                    newStopTime.setJourneyId(newJourney.getId());
                    newStopTime.setStopId(stopId);
                    newStopTime.setTime( (Calendar) journeyTime.clone());
                    for ( int i = 1; i < journeyStops.size(); i++ ) {
                        //Now add to journey time the difference between the two stops.
                        journeyTime.add(Calendar.MINUTE, routeService.getDistance(scenarioName, journeyStops.get(i-1).getStopName(), journeyStops.get(i).getStopName()));
                        //Create stop.
                        long stop2Id = journeyService.getStopByStopName(journeyStops.get(i).getStopName()).getId();
                        StopTime newStopTime2 = new StopTime();
                        newStopTime2.setJourneyId(newJourney.getId());
                        newStopTime2.setStopId(stop2Id);
                        newStopTime.setTime( (Calendar) journeyTime.clone());
                    }
                    //logger.debug("Service #" + serviceId + ": " + newService.getAllDisplayStops());{
                    allJourneys.add(newJourney);
                    //Increment calendar.
                    myTime.add(Calendar.MINUTE, myJourneyPattern.getFrequency());
                }
            }
        }
        //Sort all journeys.
        Collections.sort(allJourneys, new SortedJourneys());
        //Return the journeys.
        long[] journeyIds = new long[allJourneys.size()];
        for ( int i = 0; i < allJourneys.size(); i++ ) {
            journeyIds[i] = allJourneys.get(i).getId();
        }
        return journeyIds;
    }

    public int getDelay ( long routeScheduleId ) {
        return routeScheduleService.getDelay(routeScheduleId);
    }

}
