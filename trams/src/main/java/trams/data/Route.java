package trams.data;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;

import trams.util.SortedServices;

import org.apache.log4j.Logger;


/**
 * This represents a Route object in the TraMS program.
 * @author Dave Lee
 */
public class Route {
    
    /**
	 * 
	 */
	private int id;
	private String routeNumber;
	private String routeType;
    private List<Stop> outwardStops;
    private List<Stop> returnStops;
    private List<RouteSchedule> routeSchedules;
    
    private Map<String, Vehicle> assignedSchedules; 
    
    //Fixed value representing outward stops.
    public static final int OUTWARDSTOPS = 1;
    //Fixed value representing inward stops.
    public static final int RETURNSTOPS = 2;
    
    private Map<String, Timetable> timetables;
    
    private Logger logger;
    
    /**
     * Create a new route.
     * @param routeNo a <code>String</code> with the route number,
     */
    public Route ( ) {
        routeNumber = "";
        assignedSchedules = new HashMap<String, Vehicle>();
        outwardStops = new ArrayList<Stop>();
        returnStops = new ArrayList<Stop>();
        //theValidFromDate = validFrom;
        //theValidToDate = validTo;
        //Initialise the hash table.
        routeSchedules = new ArrayList<RouteSchedule>();
        timetables = new HashMap<String, Timetable>();
        
        logger = Logger.getLogger("Route");
    }
    
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Stop> getOutwardStops() {
		return outwardStops;
	}

	public void setOutwardStops(List<Stop> outwardStops) {
		this.outwardStops = outwardStops;
	}

	public List<Stop> getReturnStops() {
		return returnStops;
	}

	public void setReturnStops(List<Stop> returnStops) {
		this.returnStops = returnStops;
	}

	public List<RouteSchedule> getRouteSchedules() {
		return routeSchedules;
	}

	public void setRouteSchedules(List<RouteSchedule> routeSchedules) {
		this.routeSchedules = routeSchedules;
	}

	public Map<String, Vehicle> getAssignedSchedules() {
		return assignedSchedules;
	}

	public void setAssignedSchedules(Map<String, Vehicle> assignedSchedules) {
		this.assignedSchedules = assignedSchedules;
	}

	public Map<String, Timetable> getTimetables() {
		return timetables;
	}

	public void setTimetables(Map<String, Timetable> timetables) {
		this.timetables = timetables;
	}

	/**
     * Add a timetable to this route - this will be used to generate route schedules.
     * @param name a <code>String</code> with the timetable name.
     * @param timetable a <code>Timetable</code> object with the timetable details.
     */
    public void addTimetable ( String name, Timetable timetable ) {
        timetables.put(name, timetable);
    }
    
    /**
     * Get a timetable for this route based on the supplied name.
     * @param name a <code>String</code> with the required timetable name.
     * @return a <code>Timetable</code> object with the timetable name.
     */
    public Timetable getTimetable ( String name ) {
        return timetables.get(name);
    }
    
    /**
     * Get all of the current timetable names.
     * @return a <code>Enumeration</code> with all timetable names for iteration.
     */
    public Iterator<String> getTimetableNames ( ) {
        return timetables.keySet().iterator();
    }
    
    /**
     * Delete the timetable with the specified name.
     * @param timeName a <code>String</code> with the timetable name.
     */
    public void deleteTimetable ( String timeName ) {
        timetables.remove(timeName);
    }

    /**
     * This method gets the current timetable which is valid for day.
     * It is specifically used for getting the days which this timetable is valid for.
     * @param today a <code>Calendar</code> object with today's date.
     * @return a <code>Timetable</code> object.
     */
    public Timetable getCurrentTimetable ( Calendar today ) {
        Iterator<String> timetableNames = getTimetableNames();
        while ( timetableNames.hasNext() ) {
            Timetable myTimetable = getTimetable(timetableNames.next());
            if ( (myTimetable.getValidFrom().before(today) || myTimetable.getValidFrom().equals(today)) && (myTimetable.getValidTo().after(today) || myTimetable.getValidTo().equals(today))  ) {
                return myTimetable;
            }
        }
        return null; //If can't find timetable.
    }

    /**
     * This method returns the stops between two supplied stops in the route including the two stops.
     * @param startStop a <code>String</code> with the start stop name.
     * @param endStop a <code>String</code> with the end stop name.
     * @param direction a <code>int</code> with the direction.
     * @return a <code>LinkedList</code> of stops.
     */
    public List<Stop> getStopsBetween ( String startStop, String endStop, int direction ) {
        //Create blank list to add things to.
        List<Stop> myStops = new ArrayList<Stop>();
        //Control whether to add or not.
        boolean shouldAddStop = false;
        //Now go through route stops in the direction and add those as appropriate.
        for ( int i = 0; i < getNumStops(direction); i++ ) {
            if ( getStop(direction, i).getStopName().equalsIgnoreCase(startStop) ) { myStops.add(getStop(direction, i)); shouldAddStop = true; 
            } else if ( getStop(direction, i).getStopName().equalsIgnoreCase(endStop) ) { myStops.add(getStop(direction, i)); shouldAddStop = false; 
            } else if ( shouldAddStop ) { myStops.add(getStop(direction, i)); }
        }
        return myStops;
    }
    
    /**
     * This method generates the route timetables for a particular day - it is a very important method.
     * @param today a <code>Calendar</code> object with today's date.
     */
    public List<Service> generateServiceTimetables ( Calendar today, Scenario scene, int direction ) {
        logger.debug("I'm generating timetable for route " + routeNumber + " for " + today.get(Calendar.DAY_OF_MONTH) + " " + getMonth(today.get(Calendar.MONTH)) + " " + today.get(Calendar.YEAR) );
        //First of all, get the current timetable.
        Timetable currentTimetable = getCurrentTimetable(today);
        //Create a list to store services.
        List<Service> allServices = new ArrayList<Service>();
        //Now we need to go through the service patterns.
        Iterator<String> patternNames = currentTimetable.getServicePatternNames().iterator();
        while ( patternNames.hasNext() ) {
            //Store the service patttern.
            ServicePattern myServicePattern = currentTimetable.getServicePattern(patternNames.next());
            //Clone the time so that we can add to it but keep it the same for next iteration.
            Calendar myTime = (Calendar) today.clone();
            //If this service pattern is not valid for this date then don't bother.
            if ( !myServicePattern.isDayOfOperation(myTime.get(Calendar.DAY_OF_WEEK)) ) { continue; }
            //Set myTime hour and minute to the start time of the service pattern.
            myTime.set(Calendar.HOUR_OF_DAY, myServicePattern.getStartTime().get(Calendar.HOUR_OF_DAY));
            myTime.set(Calendar.MINUTE, myServicePattern.getStartTime().get(Calendar.MINUTE));
            int diffDurationFreq = myServicePattern.getDuration() % myServicePattern.getFrequency();
            if ( direction == Route.RETURNSTOPS && diffDurationFreq <= (myServicePattern.getFrequency()/2)) {
                myTime.add(Calendar.MINUTE, myServicePattern.getFrequency()/2);
            }
            //logger.debug("End time is " + myServicePattern.getEndTime().get(Calendar.HOUR_OF_DAY) + ":" + myServicePattern.getEndTime().get(Calendar.MINUTE) );
            //Maintain a counter for the service id.
            int serviceId = 0;
            //Now repeat this loop until myTime is after the service pattern end time.
            while ( true ) {
                if ( (myTime.get(Calendar.HOUR_OF_DAY) > myServicePattern.getEndTime().get(Calendar.HOUR_OF_DAY)) ) { break; 
                } else if ( (myTime.get(Calendar.HOUR_OF_DAY) == myServicePattern.getEndTime().get(Calendar.HOUR_OF_DAY)) && (myTime.get(Calendar.MINUTE) > myServicePattern.getEndTime().get(Calendar.MINUTE)) ) { break; 
                } else {
                    //logger.debug("I want a service starting from both terminuses at " + myTime.get(Calendar.HOUR_OF_DAY) + ":" + myTime.get(Calendar.MINUTE));
                    //Create an outgoing service.
                    Service newService = new Service();
                    //Add stops - we also need to create a separate calendar to ensure we don't advance more than we want!!!!
                    Calendar serviceTime = (Calendar) myTime.clone();
                    List<Stop> serviceStops = new ArrayList<Stop>();
                    if ( direction == Route.OUTWARDSTOPS ) {
                        serviceStops = getStopsBetween(myServicePattern.getReturnTerminus(), myServicePattern.getOutgoingTerminus(), direction);
                    } else {
                        serviceStops = getStopsBetween(myServicePattern.getOutgoingTerminus(), myServicePattern.getReturnTerminus(), direction);
                    }
                    newService.addStop(new Stop(serviceStops.get(0).getStopName(), (Calendar) serviceTime.clone()));
                    for ( int i = 1; i < serviceStops.size(); i++ ) {
                        //Now add to service time the difference between the two stops.
                        serviceTime.add(Calendar.MINUTE, scene.getDistance(serviceStops.get(i-1).getStopName(), serviceStops.get(i).getStopName()));
                        //Create stop.
                        newService.addStop(new Stop(serviceStops.get(i).getStopName(), (Calendar) serviceTime.clone()));
                    }
                    //logger.debug("Service #" + serviceId + ": " + newService.getAllDisplayStops());
                    if ( !isDuplicateService(allServices, newService) ) {
                        allServices.add(newService);
                    }
                    //Increment calendar.
                    myTime.add(Calendar.MINUTE, myServicePattern.getFrequency());
                    //Increment service id.
                    serviceId++;
                }
            }
        }
        //Sort all services.
        Collections.sort(allServices, new SortedServices());
        //Return the services.
        return allServices;
    }

    /**
     * Check for duplicate services.
     * @param allServices a <code>LinkedList</code> with all services.
     * @param newService a <code>Service</code> with the new service.
     */
    public boolean isDuplicateService ( List<Service> allServices, Service newService ) {
        //Go through all services and see if one equals it.
        for ( int i = 0; i < allServices.size(); i++ ) {
            if ( allServices.get(i).getAllDisplayStops().equalsIgnoreCase(newService.getAllDisplayStops()) ) {
                return true;
            }
        }
        //Otherwise return false.
        return false;
    }

    /**
     * Generate route schedules.
     * @param outgoingServices a <code>LinkedList</code> with all outgoing services.
     * @param returnServices a <code>LinkedList</code> with all return services.
     * @param sim a <code>Simulator</code> object for reference.
     */
    public void generateRouteSchedules ( List<Service> outgoingServices, List<Service> returnServices ) {
        logger.debug("In generate route schedules...");
    	//Clear any old route schedules.
        routeSchedules.clear();
        //We need to repeat this loop until both outgoingServices and returnServices are empty!
        int counter = 1;
        logger.debug("Outgoing Services = " + outgoingServices.size() + " Return Services = " + returnServices.size());
        while ( outgoingServices.size() > 0 || returnServices.size() > 0 ) {
            //Control what service we want - initially we don't care.
            boolean wantOutgoing = true; boolean wantReturn = true;
            //Create a new route schedule.
            RouteSchedule mySchedule = new RouteSchedule ( routeNumber, counter );
            //Create our calendar object and set it to midnight.
            Calendar myCal = new GregorianCalendar(2009,7,7,0,0);
            //Find whether the first outgoing service time is before the first return service time.
            logger.debug("Outgoing services has size: " + outgoingServices.size());
            logger.debug("Return services has size: " + returnServices.size());
            if ( returnServices.size() > 0 && outgoingServices.size() > 0 && outgoingServices.get(0).getStop(0).getStopTime().after(returnServices.get(0).getStop(0).getStopTime()) ) {
                myCal = (Calendar) returnServices.get(0).getStop(0).getStopTime().clone();
            } else if ( outgoingServices.size() == 0 ) {
                myCal = (Calendar) returnServices.get(0).getStop(0).getStopTime().clone();
            } else {
                myCal = (Calendar) outgoingServices.get(0).getStop(0).getStopTime().clone();
            }
            //Here's the loop.
            while ( true ) {
                //logger.debug("Schedule " + counter + " Time is now " + myCal.get(Calendar.HOUR_OF_DAY) + ":" + myCal.get(Calendar.MINUTE));
                if ( outgoingServices.size() > 0 && returnServices.size() > 0 ) {
                    if ( myCal.after(outgoingServices.get(outgoingServices.size()-1).getStop(0).getStopTime()) && myCal.after(returnServices.get(returnServices.size()-1).getStop(0).getStopTime())) {
                        break;
                    }
                } else if ( outgoingServices.size() > 0 ) {
                    if ( myCal.after(outgoingServices.get(outgoingServices.size()-1).getStop(0).getStopTime()) ) {
                        break;
                    }
                } else if ( returnServices.size() > 0 ) {
                    if ( myCal.after(returnServices.get(returnServices.size()-1).getStop(0).getStopTime()) ) {
                        break;
                    }
                } else {
                    break; //Both outgoing services and return services were 0 so finished.
                }
                int loopPos = 0;
                while ( true ) {
                    if ( loopPos >= outgoingServices.size() && loopPos >= returnServices.size() ) { break; }
                    if ( loopPos < outgoingServices.size() ) {
                        //if ( wantOutgoing ) { logger.debug("I want an outgoing service so trying: " + returnServices.get(loopPos).getAllDisplayStops() + " to route schedule " + counter); }
                        if ( wantOutgoing && outgoingServices.get(loopPos).getStop(0).getStopTime().get(Calendar.HOUR_OF_DAY) == myCal.get(Calendar.HOUR_OF_DAY) && outgoingServices.get(loopPos).getStop(0).getStopTime().get(Calendar.MINUTE) == myCal.get(Calendar.MINUTE)) {
                            //logger.debug("Adding service " + outgoingServices.get(loopPos).getAllDisplayStops() + " to route schedule " + counter);
                            //We have found our service - its an outgoing one!!!
                            mySchedule.addService(outgoingServices.get(loopPos));
                            //Set calendar equal to last stop time.
                            myCal = (Calendar) outgoingServices.get(loopPos).getLastStop().getStopTime().clone();
                            //myCal.add(Calendar.MINUTE, -1); //This prevents bad effect of adding one later!
                            //Remove this service from the list.
                            outgoingServices.remove(loopPos);
                            //Note that we next want a return one.
                            wantOutgoing = false; wantReturn = true;
                            //Continue loop.
                            continue;
                        }
                    }
                    if ( loopPos < returnServices.size() ) {
                        //if ( wantReturn ) { logger.debug("I want a return service so trying: " + returnServices.get(loopPos).getAllDisplayStops() + " to route schedule " + counter); }
                        if ( wantReturn && returnServices.get(loopPos).getStop(0).getStopTime().get(Calendar.HOUR_OF_DAY) == myCal.get(Calendar.HOUR_OF_DAY) && returnServices.get(loopPos).getStop(0).getStopTime().get(Calendar.MINUTE) == myCal.get(Calendar.MINUTE)) {
                            //logger.debug("Adding service " + returnServices.get(loopPos).getAllDisplayStops() + " to route schedule " + counter);
                            //We have found our service - its a return one!!!
                            mySchedule.addService(returnServices.get(loopPos));
                            //Set calendar equal to last stop time.
                            myCal = (Calendar) returnServices.get(loopPos).getLastStop().getStopTime().clone();
                            //myCal.add(Calendar.MINUTE, -1); //This prevents bad effect of adding one later!
                            //Remove this service from the list.
                            returnServices.remove(loopPos);
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
            //Add route schedule to route.
            routeSchedules.add(mySchedule);
            //Debug route schedule.
            logger.debug("This is route schedule " + mySchedule.toString() + " with services: ");
            for ( int i = 0; i < mySchedule.getNumServices(); i++ ) {
                logger.debug(mySchedule.getService(i).getAllDisplayStops() + " |****| ");
            }
            logger.debug("");
            //Increment counter.
            counter++;
        }
    }
    
    /**
     * Set route number.
     * @param routeNumber a <code>String</code> with the route number.
     */
    public void setRouteNumber ( String routeNumber ) {
        this.routeNumber = routeNumber;
    } 
    
    public String getRouteType() {
		return routeType;
	}

	public void setRouteType(String routeType) {
		this.routeType = routeType;
	}

	/**
     * Return a formatted String array of schedule dates from today.
     * @param today a <code>Calendar</code> object with the current date.
     * @return a <code>String</code> array of possible schedule dates.
     */
    public String[] getPossibleSchedulesDates ( Calendar today ) {
        //Create the list.
        List<Calendar> myCalendar = new ArrayList<Calendar>();
        //Go through all of the timetables and add them if they are not already in.
        Iterator<String> timeNames = getTimetableNames();
        Calendar thisDate;
        while (timeNames.hasNext()) {
            Timetable timeT = getTimetable(timeNames.next());
            logger.debug("Timetable valid to date is: " + timeT.getValidToDateInfo());
            thisDate = (Calendar) today.clone();
            //Now check if we have passed the valid to date.
            while ( !thisDate.after(timeT.getValidTo()) ) {
                //Check if we have added this date before...
                if ( !myCalendar.contains(thisDate) ) {
                    //Finally check that at least one of the service patterns has an operating service on this day.
                    Iterator<String> patternNames = timeT.getServicePatternNames().iterator();
                    while ( patternNames.hasNext() ) {
                        ServicePattern sp = timeT.getServicePattern(patternNames.next());
                        logger.debug("Processing service pattern: " + sp.getName());
                        logger.debug("Calendar day: " + thisDate.get(Calendar.DAY_OF_WEEK) + " days of operation: " + sp.getDaysOfOperationAsString());
                        if ( sp.isDayOfOperation(thisDate.get(Calendar.DAY_OF_WEEK)) ) {
                        	logger.debug("Adding this date to calendar: " + thisDate.get(Calendar.DAY_OF_MONTH) + "-" + thisDate.get(Calendar.MONTH) + "-" + thisDate.get(Calendar.YEAR));
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
            myCalDates[i] = getDay(myCalendar.get(i).get(Calendar.DAY_OF_WEEK)) + " " + myCalendar.get(i).get(Calendar.DAY_OF_MONTH) + getDateExt(myCalendar.get(i).get(Calendar.DAY_OF_MONTH)) + " " + getMonth(myCalendar.get(i).get(Calendar.MONTH)) + " " + myCalendar.get(i).get(Calendar.YEAR); 
        }
        return myCalDates;
    }
    
    /**
     * Return all outgoing services for a particular day.
     */
    public List<Service> getAllOutgoingServices ( String day ) {
        //Re-translate the date so that we know what schedules to look at!
        String[] dayParts = day.split(" ");
        String dayStr = "";
        for ( int i = 0; i < dayParts[1].length(); i++ ) {
            if (Character.isDigit(dayParts[1].charAt(i)) ) {
                dayStr += dayParts[1].charAt(i);
            }
        }
        //Initialise list to store the services.
        List<Service> outgoingServices = new ArrayList<Service>();
        //Get the route schedules for that day!
        logger.debug(routeSchedules.toString());
        for ( int h = 0;  h < routeSchedules.size(); h++ ) {
            for ( int i = 0; i < routeSchedules.get(h).getNumServices(); i++ ) {
                Service myService = routeSchedules.get(h).getService(i);
                if ( isOutwardService(myService.getStop(0).getStopName(), myService.getStop(1).getStopName()) ) {
                    outgoingServices.add(myService);
                }
            }
        }
        Collections.sort(outgoingServices, new SortedServices());
        return outgoingServices;
    }

    /**
     * Translate a formal day string into a calendar object.
     * @param day a <code>String</code> with the formal day.
     * @return a <code>Calendar</code> object representing the formal day.
     */
    public Calendar translateDate ( String day ) {
        String[] dayParts = day.split(" ");
        String month = getMonth(dayParts[2]);
        String day2 = ""; String dayStr = "";
        for ( int i = 0; i < dayParts[1].length(); i++ ) {
            if (Character.isDigit(dayParts[1].charAt(i)) ) {
                dayStr += dayParts[1].charAt(i);
            }
        }
        day2 = dayStr;
        return new GregorianCalendar(Integer.parseInt(dayParts[3]), Integer.parseInt(month), Integer.parseInt(day2));
    }

    /**
     * Return all return services for a particular day.
     */
    public List<Service> getAllReturnServices ( String day ) {
        //Re-translate the date so that we know what schedules to look at!
        String[] dayParts = day.split(" ");
        String dayStr = "";
        for ( int i = 0; i < dayParts[1].length(); i++ ) {
            if (Character.isDigit(dayParts[1].charAt(i)) ) {
                dayStr += dayParts[1].charAt(i);
            }
        }
        //Initialise list to store the services.
        List<Service> returnServices = new ArrayList<Service>();
        //Get the route schedules for that day!
        logger.debug(routeSchedules.toString());
        for ( int h = 0; h < routeSchedules.size(); h++ ) {
            for ( int i = 0; i < routeSchedules.get(h).getNumServices(); i++ ) {
                Service myService = routeSchedules.get(h).getService(i);
                if ( !isOutwardService(myService.getStop(0).getStopName(), myService.getStop(1).getStopName()) ) {
                    returnServices.add(myService);
                }
            }
        }
        Collections.sort(returnServices, new SortedServices());
        return returnServices;
    }
    
    /**
     * This method checks using two stops if the service is an outward or inward service.
     * @return a <code>boolean</code> which is true iff the service is an outward service.
     */
    public boolean isOutwardService ( String stop1, String stop2 ) {
        //Go through the stops - if we find the 1st one before the 2nd one - it is outward.
        //Otherwise it is inward.
        for ( int i = 0; i < outwardStops.size(); i++ ) {
            if ( outwardStops.get(i).getStopName().equalsIgnoreCase(stop1) ) {
                return true;
            } else if ( outwardStops.get(i).getStopName().equalsIgnoreCase(stop2) ) {
                return false;
            }
        }
        return false;
    }
    
    
    /**
     * Get the day of the week as a String based on the number.
     * @param day a <code>int</code> with the day number.
     * @return a <code>String</code> with the string representation of the day.
     */
    private String getDay ( int day ) {
        if ( day == Calendar.SUNDAY ) { return "Sunday";
    	} else if ( day == Calendar.MONDAY ) { return "Monday";
		} else if ( day == Calendar.TUESDAY ) { return "Tuesday";
		} else if ( day == Calendar.WEDNESDAY ) { return "Wednesday";
		} else if ( day == Calendar.THURSDAY ) { return "Thursday";
		} else if ( day == Calendar.FRIDAY ) { return "Friday";
		} else if ( day == Calendar.SATURDAY ) { return "Saturday";
		} else { return ""; }
    }
    
    /**
     * Get the month as a String based on the number.
     * @param day a <code>int</code> with the month number.
     * @return a <code>String</code> with the string representation of the month.
     */
    private String getMonth ( int month ) {
        if ( month == Calendar.JANUARY ) { return "January";
    	} else if ( month == Calendar.FEBRUARY ) { return "February";
		} else if ( month == Calendar.MARCH ) { return "March";
		} else if ( month == Calendar.APRIL ) { return "April";
		} else if ( month == Calendar.MAY ) { return "May";
		} else if ( month == Calendar.JUNE ) { return "June";
		} else if ( month == Calendar.JULY ) { return "July";
		} else if ( month == Calendar.AUGUST ) { return "August";
		} else if ( month == Calendar.SEPTEMBER ) { return "September";
		} else if ( month == Calendar.OCTOBER ) { return "October";
		} else if ( month == Calendar.NOVEMBER ) { return "November";
		} else if ( month == Calendar.DECEMBER ) { return "December";
		} else { return ""; }
    }
    
    /**
     * Get the month as a number based on the String.
     * @param day a <code>String</code> with the month name.
     * @return a <code>String</code> with the String representation of the month in number form.
     */
    private String getMonth ( String month ) {
        if ( month.equalsIgnoreCase("January") ) { return "0";
    	} else if ( month.equalsIgnoreCase("February") ) { return "1";
		} else if ( month.equalsIgnoreCase("March") ) { return "2";
		} else if ( month.equalsIgnoreCase("April") ) { return "3";
		} else if ( month.equalsIgnoreCase("May") ) { return "4";
		} else if ( month.equalsIgnoreCase("June") ) { return "5";
		} else if ( month.equalsIgnoreCase("July") ) { return "6";
		} else if ( month.equalsIgnoreCase("August") ) { return "7";
		} else if ( month.equalsIgnoreCase("September") ) { return "8";
		} else if ( month.equalsIgnoreCase("October") ) { return "9";
		} else if ( month.equalsIgnoreCase("November") ) { return "10";
		} else if ( month.equalsIgnoreCase("December") ) { return "11";
		} else { return ""; }
    }
    
    /**
     * Get the day extension for a particular day number.
     * @param dayDate a <code>int</code> with the day number.
     * @return a <code>String</code> with the day extension.
     */
    private String getDateExt ( int dayDate ) {
        if ( dayDate == 1 || dayDate == 21 || dayDate == 31 ) { return "st";
    	} else if ( dayDate == 2 || dayDate == 22 ) { return "nd";
		} else if ( dayDate == 3 || dayDate == 23 ) { return "rd";
		} else { return "th"; }
    }
    
    /**
     * Add an allocation to this route.
     * @param schedId a <code>String</code> with the schedule id.
     * @param v a <code>Vehicle</code> object with the vehicle running the schedule.
     */
    public void addAllocation ( String schedId, Vehicle v ) {
        assignedSchedules.put(schedId, v);
        for ( int i = 0; i < routeSchedules.size(); i++ ) {
            if ( routeSchedules.get(i).toString().equalsIgnoreCase(schedId) ) {
                v.setAssignedSchedule(routeSchedules.get(i));
            }
        }
    }
    
    /**
     * Get the assigned vehicle for a schedule id.
     * @param schedId a <code>String</code> with the schedule id.
     * @return a <code>Vehicle</code> object.
     */
    public Vehicle getAssignedVehicle ( String schedId ) {
        return assignedSchedules.get(schedId);
    }
    
    /**
     * Get route number.
     * @return a <code>String</code> with the route number.
     */
    public String getRouteNumber ( ) {
        return routeNumber;
    }
    
    /**
     * Add stop.
     * @param stopName a <code>String</code> with the stop name.
     * @param outinward a <code>int</code> with the direction of the stop.
     * @return a <code>boolean</code> which is true iff the stop was added successfully.
     */
    public boolean addStop ( String stopName, int outinward ) {
        //Outward stops.
        if ( outinward == Route.OUTWARDSTOPS ) {
            return outwardStops.add(new Stop(stopName));
        } else if ( outinward == Route.RETURNSTOPS ) {
            return returnStops.add(new Stop(stopName));
        }
        return false;
    }
    
    /**
     * Move stops in the ordering list for a route.
     * @param stopName a <code>String</code> with the name of the stop. 
     * @param outinward a <code>int</code> with the direction of the stop.
     * @param moveup a <code>boolean</code> which is true iff the stop should be moved up.
     * @return a <code>boolean</code> which is true iff the stop was moved successfully.
     */
    public boolean moveStops ( String stopName, boolean outinward, boolean moveup ) {
        //Outward stops.
        if ( outinward ) {
            //Search for stop in linked list.
            for ( int i = 0; i < outwardStops.size(); i++ ) {
                if ( outwardStops.get(i).getStopName().equalsIgnoreCase(stopName) ) {
                    //Here is the swap.
                    if ( moveup && i != 0 ) {
                        Stop currentStop = outwardStops.get(i);
                        Stop prevStop = outwardStops.get(i-1);
                        outwardStops.remove(prevStop); outwardStops.remove(currentStop);
                        outwardStops.add(i-1, currentStop);
                        outwardStops.add(i, prevStop);
                    } else if ( !moveup && i != outwardStops.size()-1 ) {
                        Stop nextStop = outwardStops.get(i+1);
                        Stop currentStop = outwardStops.get(i);
                        outwardStops.remove(nextStop); outwardStops.remove(currentStop);
                        try {
                            outwardStops.add(i+1, currentStop);
                            outwardStops.add(i, nextStop);
                        } catch ( IndexOutOfBoundsException ioe ) {
                            outwardStops.add(nextStop);
                            outwardStops.add(currentStop);
                        }
                        
                    }
                }
            }
        }  else {
            //Search for stop in linked list.
            for ( int i = 0; i < returnStops.size(); i++ ) {
                if ( returnStops.get(i).getStopName().equalsIgnoreCase(stopName) ) {
                    //Here is the swap.
                    if ( moveup && i != 0 ) {
                        Stop prevStop = returnStops.get(i-1);
                        Stop currentStop = returnStops.get(i);
                        returnStops.remove(prevStop); returnStops.remove(currentStop);
                        returnStops.add(i-1, currentStop);
                        returnStops.add(i, prevStop);
                    } else if ( !moveup && i != returnStops.size()-1 ) {
                        Stop nextStop = returnStops.get(i+1);
                        Stop currentStop = returnStops.get(i);
                        returnStops.remove(nextStop); returnStops.remove(currentStop);
                        try {
                            returnStops.add(i+1, currentStop);
                            returnStops.add(i, nextStop);
                        } catch ( IndexOutOfBoundsException ioe ) {
                            returnStops.add(nextStop);
                            returnStops.add(currentStop);
                        }
                    }
                }
            }
        }
        return true;
    }
    
    /**
     * Delete stop.
     * @param stopName a <code>String</code> with the stop name.
     * @param stopDirection a <code>int</code> with the direction of the stop.
     * @return a <code>boolean</code> which is true iff the stop was deleted successfully.
     */
    public boolean deleteStop ( String stopName, int stopDirection ) {
        if ( stopDirection == Route.OUTWARDSTOPS ) {
            for ( int i = 0; i < outwardStops.size(); i++ ) {
                if ( outwardStops.get(i).getStopName().equalsIgnoreCase(stopName) ) {
                    outwardStops.remove(i); return true;
                }
            }
            return false;
        } else {
            for ( int i = 0; i < returnStops.size(); i++ ) {
                if ( returnStops.get(i).getStopName().equalsIgnoreCase(stopName) ) {
                    returnStops.remove(i); return true;
                }
            }
            return false;
        }
    }
    
    /**
     * Get the number of route schedules.
     * @return a <code>int</code> with the number of route schedules.
     */
    public int getNumRouteSchedules ( ) {
        try {
            return routeSchedules.size();
        } catch ( NullPointerException npe ) {
            return 0;
        }
    }
    
    /**
     * Get route schedule based on number.
     * @param num a <code>int</code> with the number.
     * @return a <code>RouteSchedule</code> object.
     */
    public RouteSchedule getRouteSchedule ( int num ) {
        return routeSchedules.get(num);
    }
    
    /**
     * Get number of stops of a particular direction.
     * @param stopType a <code>int</code> with the stop direction.
     * @return a <code>int</code> with the number of stops.
     */
    public int getNumStops ( int stopType ) {
        if ( stopType == OUTWARDSTOPS ) {
            return outwardStops.size();
        } else if ( stopType == RETURNSTOPS ) {
            return returnStops.size();
        }
        return 0;
    }
    
    /** 
     * Get stop based on direction and number.
     * @param stopType a <code>int</code> with the stop direction.
     * @param num a <code>int</code> with the position in list.
     * @return a <code>Stop</code> object.
     */
    public Stop getStop ( int stopType, int num ) {
        if ( stopType == OUTWARDSTOPS ) {
            return outwardStops.get(num);
        } else if ( stopType == RETURNSTOPS ) {
            return returnStops.get(num);
        }
        return null;
    }
      
    /**
     * Return a String representation of this object.
     * @return a <code>String</code> object.
     */
    public String toString ( ) {
        return routeNumber + ": " + outwardStops.get(0).getStopName() + " - " + returnStops.get(0).getStopName();
    }
    
}
