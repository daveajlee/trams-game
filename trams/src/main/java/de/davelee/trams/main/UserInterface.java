package de.davelee.trams.main;

import javax.swing.*;

import de.davelee.trams.controllers.*;
import de.davelee.trams.data.*;
import de.davelee.trams.model.RouteModel;
import de.davelee.trams.services.*;
import de.davelee.trams.util.SortedJourneys;
import de.davelee.trams.util.TramsConstants;
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
    
    //This is to decide which screen to show when we refresh.
    private boolean showingMessages = false;
    private boolean showingManagement = false;

    //This is where we keep the tip messages.
    LinkedList<String> tipMessages = new LinkedList<String>();
    
    private RouteScheduleService routeScheduleService;
    private JourneyService journeyService;
    private VehicleService vehicleService;
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
        //Add tip messages here.
        tipMessages.add("TIP: Watch your balance! You can't buy new vehicles or run more routes if you don't have money!");
        tipMessages.add("TIP: Earn money by improving your passenger satisfaction through running vehicles on time!");
        tipMessages.add("TIP: If your passenger satisfaction falls too low, you may be forced to resign!");
    
        routeScheduleService = new RouteScheduleService();
        vehicleService = new VehicleService();
        routeService = new RouteService();
        journeyService = new JourneyService();
        journeyPatternService = new JourneyPatternService();
        timetableService = new TimetableService();
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

    public void generateRouteSchedules (final RouteModel routeModel, Calendar currentTime, String scenarioName ) {
        //Initialise parameters.
        long[] outgoingJourneyIds = generateJourneyTimetables(routeModel, currentTime, scenarioName, TramsConstants.OUTWARD_DIRECTION);
        long[] returnJourneyIds = generateJourneyTimetables(routeModel, currentTime, scenarioName, TramsConstants.RETURN_DIRECTION);
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
     * Run simulation!
     */
    public void runSimulation (final JFrame currentFrame) {
        currentFrame.dispose();
        ControlScreen cs = new ControlScreen("", 0, 4, false);
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
        ControlScreen cs = new ControlScreen(routeNumber, 0, 4, false);
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
        ControlScreen cs = new ControlScreen(routeNumber, min, max, allocations);
        //cs.drawVehicles(true);
        currentFrame.setVisible(true);
        //Set control screen.
        cs.setVisible(true);
        //Resume simulation.
        gameController.resumeSimulation();
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
     * Get the number of available vehicle types.
     * @return a <code>int</code> with the number of available vehicle types.
     */
    public int getNumVehicleTypes ( ) {
        return vehicleService.getNumberAvailableVehicles();
    }
    
    /**
     * Sort vehicles by alphabetical order.
     */
    public void sortVehicles ( ) {
        vehicleService.sortVehicles(vehicleService.getAllVehicles());
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
        SplashScreen ss = new SplashScreen(false);
        for ( int i = 12; i > -5; i-- ) {
            try {
                Thread.sleep(200);
                ss.moveImage(10*(i+1),0);
            }
            catch ( InterruptedException ie ) { }
        }
        ss.dispose();
        new WelcomeScreen();
        //LoadingScreen ls = new LoadingScreen();
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
            if ( direction == TramsConstants.RETURN_DIRECTION && diffDurationFreq <= (myJourneyPattern.getFrequency()/2)) {
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
                    if ( direction == TramsConstants.OUTWARD_DIRECTION ) {
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

}
