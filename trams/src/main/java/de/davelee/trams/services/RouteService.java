package de.davelee.trams.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.davelee.trams.dao.RouteDao;
import de.davelee.trams.data.*;
import de.davelee.trams.factory.ScenarioFactory;

import de.davelee.trams.util.SortedRoutes;

public class RouteService {

    private ScenarioFactory scenarioFactory;

    private RouteDao routeDao;
    
    public static int OUTWARD_DIRECTION = 0;
    public static int RETURN_DIRECTION = 1;
	
	public RouteService() {
	}

    public RouteDao getRouteDao() {
        return routeDao;
    }

    public void setRouteDao(RouteDao routeDao) {
        this.routeDao = routeDao;
    }

    public ScenarioFactory getScenarioFactory() {
        return scenarioFactory;
    }

    public void setScenarioFactory(ScenarioFactory scenarioFactory) {
        this.scenarioFactory = scenarioFactory;
    }

    /**
     * Get the distance between two stops.
     * @param stop1 a <code>String</code> with the name of the first stop.
     * @param stop2 a <code>String</code> with the name of the second stop.
     * @return a <code>int</code> with the distance between two stops.
     */
    public int getDistance ( String scenarioName, String stop1, String stop2 ) {
        int stop1Pos = -1; int stop2Pos = -1; int count = 0;
        List<String> stopDistanceList = scenarioFactory.createScenarioByName(scenarioName).getStopDistances();
        for ( String stopDistance : stopDistanceList ) {
            String stopName = stopDistance.split(":")[0];
            if ( stopName.equalsIgnoreCase(stop1) ) { stop1Pos = count; }
            else if ( stopName.equalsIgnoreCase(stop2) ) { stop2Pos = count; }
            count++;
        }
        return Integer.parseInt(stopDistanceList.get(stop1Pos).split(":")[1].split(",")[stop2Pos]);
    }
    
    /**
     * Move stops in the ordering list for a route.
     * @param stopName a <code>String</code> with the name of the stop. 
     * @param moveup a <code>boolean</code> which is true iff the stop should be moved up.
     * @return a <code>boolean</code> which is true iff the stop was moved successfully.
     */
    public boolean moveStops ( Route route, String stopName, boolean moveup ) {
    	//Search for stop in linked list.
        List<Stop> stops = route.getStops();
        for ( int i = 0; i < stops.size(); i++ ) {
        	if ( stops.get(i).getStopName().equalsIgnoreCase(stopName) ) {
        		//Here is the swap.
                if ( moveup && i != 0 ) {
                	Stop currentStop = stops.get(i);
                    Stop prevStop = stops.get(i-1);
                    stops.remove(prevStop); stops.remove(currentStop);
                    stops.add(i-1, currentStop);
                    stops.add(i, prevStop);
                }
                else if ( !moveup && i != stops.size()-1 ) {
                	Stop nextStop = stops.get(i+1);
                    Stop currentStop = stops.get(i);
                    stops.remove(nextStop); stops.remove(currentStop);
                    try {
                    	stops.add(i+1, currentStop);
                        stops.add(i, nextStop);
                    }
                    catch ( IndexOutOfBoundsException ioe ) {
                    	stops.add(nextStop);
                        stops.add(currentStop);
                    }
                }
            }
            route.setStops(stops);
        } 
        return true;
    }
    
    /**
     * Delete stop.
     * @param stopName a <code>String</code> with the stop name.
     * @return a <code>boolean</code> which is true iff the stop was deleted successfully.
     */
    public boolean deleteStop ( Route route, String stopName ) {
        List<Stop> stops = route.getStops(); boolean result = false;
        for ( int i = 0; i < stops.size(); i++ ) {
        	if ( stops.get(i).getStopName().equalsIgnoreCase(stopName) ) {
        		stops.remove(i); result = true;
        	}
        }
        if ( result == true ) {
        	route.setStops(stops);
        }
        return result;
    }
    
    /**
     * Add stop.
     * @param stopName a <code>String</code> with the stop name.
     * @return a <code>boolean</code> which is true iff the stop was added successfully.
     */
    public boolean addStop ( Route route, String stopName ) {
        Stop stop = new Stop();
        stop.setStopName(stopName);
        List<Stop> stops = route.getStops();
        boolean result = stops.add(stop);
        if ( result ) {
        	route.setStops(stops);
            }
        return result;
    }

    /**
     * This method returns the stops between two supplied stops in the route including the two stops.
     * @param startStop a <code>String</code> with the start stop name.
     * @param endStop a <code>String</code> with the end stop name.
     * @param direction a <code>int</code> with the direction.
     * @return a <code>LinkedList</code> of stops.
     */
    public List<Stop> getStopsBetween ( Route route, String startStop, String endStop, int direction ) {
        //Create blank list to add things to.
        List<Stop> myStops = new ArrayList<Stop>();
        //Control whether to add or not.
        boolean shouldAddStop = false;
        //Now go through route stops in the direction and add those as appropriate.
        List<Stop> relevantStops = route.getStops();
        if ( direction == OUTWARD_DIRECTION ) { 
        	for ( int i = 0; i < relevantStops.size(); i++ ) {
                if ( relevantStops.get(i).getStopName().equalsIgnoreCase(startStop) ) { myStops.add(relevantStops.get(i)); shouldAddStop = true; }
                else if ( relevantStops.get(i).getStopName().equalsIgnoreCase(endStop) ) { myStops.add(relevantStops.get(i)); shouldAddStop = false; }
                else if ( shouldAddStop ) { myStops.add(relevantStops.get(i)); }
            }
        }
        else { 
        	for ( int i = relevantStops.size()-1; i >= 0; i-- ) {
                if ( relevantStops.get(i).getStopName().equalsIgnoreCase(startStop) ) { myStops.add(relevantStops.get(i)); shouldAddStop = true; }
                else if ( relevantStops.get(i).getStopName().equalsIgnoreCase(endStop) ) { myStops.add(relevantStops.get(i)); shouldAddStop = false; }
                else if ( shouldAddStop ) { myStops.add(relevantStops.get(i)); }
            }
        }
        return myStops;
    }
    
    public Route getRouteById(long id) {
    	return routeDao.getRouteById(id);
    }
    
    public Route createRoute ( String routeNumber, String[] stopNames ) {
    	Route route = new Route();
    	route.setRouteNumber(routeNumber);
    	List<Stop> stops = new ArrayList<Stop>();
    	for ( int i = 0; i < stopNames.length; i++ ) {
    		Stop stop = new Stop();
    		stop.setStopName(stopNames[i]);
    		stops.add(stop);
    	}
    	route.setStops(stops);
    	return route;
    }
    
    /**
     * Get the route object based on the route number,
     * @param routeNumber a <code>String</code> with the route number,
     * @return a <code>Route</code> object.
     */
    public Route getRoute ( String routeNumber ) {
        for ( Route myRoute : getAllRoutes() ) {
            if ( myRoute.getRouteNumber().equalsIgnoreCase(routeNumber) ) {
                return myRoute;
            }
        }
        return null;
    }
    
    /**
     * Sort routes into alphabetical order by route number,
     */
    public void sortRoutes ( List<Route> routes ) {
        Collections.sort(routes, new SortedRoutes());
    }
    
    public List<Route> getAllRoutes ( ) {
    	return routeDao.getAllRoutes();
    }
    
    public void saveRoute ( final Route route ) {
    	routeDao.createAndStoreRoute(route);
    }

    public void removeRoute ( final Route route ) {
    	routeDao.removeRoute(route);
    }

}
