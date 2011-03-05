package trams.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import trams.simulation.Simulator;

/**
 * Class represents a route schedule (i.e. a particular timetable instance of a route) in the TraMS program.
 * @author Dave Lee.
 */
public class RouteSchedule {
    
	private int id;
	private String routeNumber;
	private int scheduleNumber;
    private List<Service> serviceList;
    private int delayInMins;
    
    private static final double PERCENT_20 = 0.20;
    private static final double PERCENT_25 = 0.25;
    private static final double PERCENT_30 = 0.30;
    private static final double PERCENT_60 = 0.60;
    private static final double PERCENT_75 = 0.75;
    private static final double PERCENT_85 = 0.85;
    private static final double PERCENT_90 = 0.90;
    private static final double PERCENT_95 = 0.95;

    public RouteSchedule() {
    	serviceList = new ArrayList<Service>();
    }
    
    public String getRouteNumber() {
		return routeNumber;
	}

	public void setRouteNumber(String routeNumber) {
		this.routeNumber = routeNumber;
	}

	public List<Service> getServiceList() {
		return serviceList;
	}

	public void setServiceList(List<Service> serviceList) {
		this.serviceList = serviceList;
	}

	public int getDelayInMins() {
		return delayInMins;
	}

	public void setDelayInMins(int delayInMins) {
		this.delayInMins = delayInMins;
	}

	public int getScheduleNumber() {
		return scheduleNumber;
	}

	public void setScheduleNumber(int scheduleNumber) {
		this.scheduleNumber = scheduleNumber;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
     * Create a new route schedule.
     * @param routeNumber a <code>String</code> with the route number.
     * @param routeSchedId a <code>int</code> with the route schedule id.
     */
    public RouteSchedule ( String routeNumber, int scheduleNumber ) {
        //Initialise variables.
        this.routeNumber = routeNumber;
        this.scheduleNumber = scheduleNumber;
        serviceList = new ArrayList<Service>();
    }

    /**
     * Add a service to this route schedule.
     * @param serviceId a <code>int</code> with the service id.
     * @param stops a <code>LinkedList</code> with the stops served by this service.
     */
    /*public void addService ( int serviceId, LinkedList<Stop> stops ) {
        theServiceList.add(new Service(serviceId, stops));
    }*/
    
    /**
     * Add a pre-made service to this route schedule.
     * @param newService a <code>Service</code> object with the service to add.
     */
    public void addService ( Service newService ) {
        serviceList.add(newService);
    }
    
    /**
     * Remove the specified service. 
     * @param oldService a <code>Service</code> object to remove.
     */
    public void removeService(Service oldService) {
        serviceList.remove(oldService);
    }
    
    /**
     * Get the current stop which this route schedule is on based on the current date.
     * @param currentDate a <code>Calendar</code> object.
     * @return a <code>String</code> array with the stop details.
     */
    public String[] getCurrentStop ( Calendar currentDate, Simulator simulator ) {
        //Copy current Date to current Time and then use delay to determine position.
        Calendar currentTime = (Calendar) currentDate.clone();
        currentTime.add(Calendar.MINUTE, -delayInMins);
        for (Service myService : serviceList) {
            if (myService.hasServiceStarted(currentTime) && !myService.hasServiceEnded(currentTime)) {
                //Now fiddle delay!
                calculateNewDelay(simulator);
                return myService.getCurrentStop(currentTime);
            } else if (!myService.hasServiceStarted(currentTime) && myService.getServiceId() != 1) {
                return new String[] { myService.getStartTerminus(currentTime), "" + 0 };
            } else if (!myService.hasServiceStarted(currentTime) && myService.getServiceId() == 1) {
                return new String[] { "Depot", "" + 0 };
            }
        }
        delayInMins = 0; //Finished for the day.
        return new String[] { "Depot", "" + 0 };
    }
    
    /**
     * Get the current service running on this schedule based on the current date.
     * @param currentTime a <code>Calendar</code> object with current time.
     * @return a <code>Service</code> object.
     */
    public Service getCurrentService ( Calendar currentTime ) {
        for ( int i = 0; i < serviceList.size(); i++ ) {
            if ( serviceList.get(i).hasServiceStarted(currentTime) && !serviceList.get(i).hasServiceEnded(currentTime)) {
                return serviceList.get(i);
            } else if ( serviceList.get(i).hasServiceStarted(currentTime) && i != (serviceList.size()-1) && !serviceList.get(i+1).hasServiceStarted(currentTime) ) {
                return serviceList.get(i);
            }
        }
        return null;
    }
    
    public Service getNextService ( Calendar currentTime ) {
        boolean returnNextService = false;
        for ( Service myService : serviceList ) {
            if ( returnNextService ) {
                return myService;
            }
            if ( myService.hasServiceStarted(currentTime) && !myService.hasServiceEnded(currentTime)) {
                returnNextService = true;
            }
        }
        return null;
    }
    
    /**
     * Return a String representation of this RouteSchedule object.
     * @return a <code>String</code> object.
     */
    public String toString() {
        return routeNumber + "/" + scheduleNumber;
    }

    /**
     * Calculate a new random delay for this route schedule.
     */
    public void calculateNewDelay ( Simulator simulator ) {

        //Generate a random number between 0 and 1.
        Random randNumGen = new Random();
        double prob = randNumGen.nextDouble();
        //Create probability array.
        double[] ratioArray = new double[0];
        switch ( simulator.getDifficultyLevel() ) {
        	case EASY:
        		ratioArray = new double[] { PERCENT_25, PERCENT_85, PERCENT_95 };
        		break;
        	case INTERMEDIATE:
        		ratioArray = new double[] { PERCENT_20, PERCENT_85, PERCENT_95 };
        		break;
        	case MEDIUM:
        		ratioArray = new double[] { PERCENT_20, PERCENT_75, PERCENT_90 }; 
        		break;
        	case HARD:
        		ratioArray = new double[] { PERCENT_30, PERCENT_60, PERCENT_85 };
        		break;
        }
        //With ratioArray[0] probability no delay change.
        if ( prob < ratioArray[0] ) { return; }
        //With ratioArray[1] probability - reduce delay by 1-5 mins.
        if ( prob >= ratioArray[0] && prob < ratioArray[1] ) {
            int delayReduction = randNumGen.nextInt(5) + 1;
            reduceDelay(delayReduction);
            return;
        }
        //With 10% probability - increase delay by 1-5 mins.
        if ( prob >= ratioArray[1] && prob < ratioArray[2] ) {
            int delayIncrease = randNumGen.nextInt(5) + 1;
            increaseDelay(delayIncrease);
            return;
        }
        //Remaining probability - generate delay between 5 and 20 mins.
        int delayIncrease = randNumGen.nextInt(15) + 6;
        increaseDelay(delayIncrease);
    }
    
    /**
     * Get the current delay of this route schedule in minutes.
     * @return a <code>int</code> with the delay in minutes.
     */
    public int getDelay() {
        return delayInMins;
    }
    
    /**
     * Check if this vehicle has a delay.
     * @return a <code>boolean>/code> with the delay of this vehicle.
     */
    public boolean hasDelay() {
        if ( delayInMins != 0 ) {
            return true;
        }
        return false;
    }

    /**
     * Reduces the current delay by a certain number of minutes.
     * @param mins a <code>int</code> with the number of minutes.
     */
    public void reduceDelay(int mins) {
        //If no delay, then can't reduce it so just return.
        if (delayInMins == 0) { return; 
        } else {
            delayInMins -= mins;
            //Now check if delay falls below 0, if it does then delay is 0.
            if (delayInMins < 0) { delayInMins = 0; }
        }    
    }

    /**
     * Increases the vehicles current delay by a certain number of minutes.
     * @param mins a <code>int</code> with the number of minutes.
     */
    public void increaseDelay(int mins) {
        //This is easy because increasing delay has no special processing!!!!
        delayInMins += mins;
    }
    
    /**
     * Get the number of services in this route schedule.
     * @return a <code>int</code> with the number of services.
     */
    public int getNumServices ( ) {
        return serviceList.size();
    }
    
    /**
     * Get a service based on its location in the list.
     * @param pos a <code>int</code> with the position.
     * @return a <code>Service</code> object.
     */
    public Service getService ( int pos ) {
        return serviceList.get(pos);
    }
    
    public int getId () {
    	return id;
    }
    
    /**
     * Shorten schedule to the specific stop stated and reduce the delay accordingly.
     * @param stop a <code>String</code> with the stop to terminate at.
     * @param currentTime a <code>Calendar</code> with the current time.
     */
    public void shortenSchedule ( String stop, Calendar currentTime ) {
        //Shorten schedule to the specific stop stated and reduce the delay accordingly - for current service remove stops after the specified stop.
        //logger.debug("Service was ending at: " + theAssignedSchedule.getCurrentService().getEndDestination());
        String oldEnd = getCurrentService(currentTime).getEndDestination();
        //Now we need to remove the stops in beteen!
        long timeDiff = getCurrentService(currentTime).removeStopsBetween(stop, oldEnd, false, true);
        //Now for the next service we need to remove stops between first stop and stop.
        long timeDiff2 = getNextService(currentTime).removeStopsBetween(getNextService(currentTime).getStartTerminus(), stop, false, true);
        //Divide both timeDiff's by 60 to convert to minutes and then use that to reduce vehicle delay.
        long delayReduction = (timeDiff/60) + (timeDiff2/60);
        //Reduce delay!
        reduceDelay((int) delayReduction);
    }
    
    /**
     * Put this vehicle out of service from the current stop until the new stop.
     * @param currentStop a <code>String</code> with the stop to go out of service from.
     * @param newStop a <code>String</code> with the stop to resume service from.
     * @param currentTime a <code>Calendar</code> object with the current time.
     */
    public void outOfService ( String currentStop, String newStop, Calendar currentTime ) {
        //Get the time difference between current stop and new stop.
        long timeDiff = getCurrentService(currentTime).getStopTimeDifference(currentStop, newStop);
        reduceDelay((int) (timeDiff/2));
        //logger.debug("Vehicle delay reduced from " + oldDelay + " mins to " + getVehicleDelay() + " mins.");
    }
    
}
