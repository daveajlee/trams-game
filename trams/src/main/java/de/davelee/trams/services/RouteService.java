package de.davelee.trams.services;

import java.util.ArrayList;
import java.util.List;

import de.davelee.trams.data.Route;
import de.davelee.trams.data.Stop;
import de.davelee.trams.model.RouteModel;
import de.davelee.trams.repository.RouteRepository;
import de.davelee.trams.util.TramsConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RouteService {

    @Autowired
    private RouteRepository routeRepository;
    
    /**
     * Move stops in the ordering list for a route.
     * @param routeModel a <code>RouteModel</code> representing the route to move stops for.
     * @param stopName a <code>String</code> with the name of the stop. 
     * @param moveup a <code>boolean</code> which is true iff the stop should be moved up.
     * @return a <code>boolean</code> which is true iff the stop was moved successfully.
     */
    public boolean moveStops (final RouteModel routeModel, final String stopName, final boolean moveup ) {
    	//Search for stop in linked list.
        List<Stop> stops = new ArrayList<Stop>();
        //List<Stop> stops = routeRepository.findRouteByNumber(routeModel.getRouteNumber()).getStops();
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
            //routeRepository.findRouteByNumber(routeModel.getRouteNumber()).setStops(stops);
            routeModel.setStopNames(convertStops(stops));
        } 
        return true;
    }

    private List<String> convertStops ( final List<Stop> stops ) {
        List<String> stopNames = new ArrayList<String>();
        for (int i = 0; i < stops.size(); i++) {
            stopNames.add(stops.get(i).getStopName());
        }
        return stopNames;
    }
    
    /**
     * Delete stop.
     * @param routeModel a <code>RouteModel</code> representing the route to delete stop for.
     * @param stopName a <code>String</code> with the stop name.
     * @return a <code>boolean</code> which is true iff the stop was deleted successfully.
     */
    public boolean deleteStop ( final RouteModel routeModel, final String stopName ) {
        List<Stop> stops = new ArrayList<Stop>();
        //List<Stop> stops = routeRepository.findRouteByNumber(routeModel.getRouteNumber()).getStops();
        boolean result = false;
        for ( int i = 0; i < stops.size(); i++ ) {
        	if ( stops.get(i).getStopName().equalsIgnoreCase(stopName) ) {
        		stops.remove(i); result = true;
        	}
        }
        return result;
    }
    
    /**
     * Add stop.
     * @param routeModel a <code>RouteModel</code> representing the route to add stop for.
     * @param stopName a <code>String</code> with the stop name.
     * @return a <code>boolean</code> which is true iff the stop was added successfully.
     */
    public boolean addStop ( final RouteModel routeModel, final String stopName ) {
        Stop stop = new Stop();
        stop.setStopName(stopName);
        List<Stop> stops = new ArrayList<Stop>();
        //List<Stop> stops = routeRepository.findRouteByNumber(routeModel.getRouteNumber()).getStops();
        boolean result = stops.add(stop);
        if ( result ) {
            //routeRepository.findRouteByNumber(routeModel.getRouteNumber()).setStops(stops);
        }
        return result;
    }

    /**
     * This method returns the stops between two supplied stops in the route including the two stops.
     * @param routeModel a <code>RouteModel</code> representing the route to get the stops between.
     * @param startStop a <code>String</code> with the start stop name.
     * @param endStop a <code>String</code> with the end stop name.
     * @param direction a <code>int</code> with the direction.
     * @return a <code>LinkedList</code> of stops.
     */
    public List<String> getStopsBetween ( final RouteModel routeModel, final String startStop, final String endStop, final int direction ) {
        //Create blank list to add things to.
        List<String> myStops = new ArrayList<String>();
        //Control whether to add or not.
        boolean shouldAddStop = false;
        //Now go through route stops in the direction and add those as appropriate.
        List<String> relevantStops = routeModel.getStopNames();
        if ( direction == TramsConstants.OUTWARD_DIRECTION ) {
        	for ( int i = 0; i < relevantStops.size(); i++ ) {
                if ( relevantStops.get(i).equalsIgnoreCase(startStop) ) { myStops.add(relevantStops.get(i)); shouldAddStop = true; }
                else if ( relevantStops.get(i).equalsIgnoreCase(endStop) ) { myStops.add(relevantStops.get(i)); shouldAddStop = false; }
                else if ( shouldAddStop ) { myStops.add(relevantStops.get(i)); }
            }
        }
        else { 
        	for ( int i = relevantStops.size()-1; i >= 0; i-- ) {
                if ( relevantStops.get(i).equalsIgnoreCase(startStop) ) { myStops.add(relevantStops.get(i)); shouldAddStop = true; }
                else if ( relevantStops.get(i).equalsIgnoreCase(endStop) ) { myStops.add(relevantStops.get(i)); shouldAddStop = false; }
                else if ( shouldAddStop ) { myStops.add(relevantStops.get(i)); }
            }
        }
        return myStops;
    }

    public void saveRoute ( final RouteModel routeModel ) {
        routeRepository.saveAndFlush(convertToRoute(routeModel));
    }

    private Route convertToRoute (final RouteModel routeModel ) {
    	Route route = new Route();
        route.setNumber(routeModel.getRouteNumber());
    	List<Stop> stops = new ArrayList<Stop>();
        for ( int i = 0; i < routeModel.getStopNames().size(); i++ ) {
    		Stop stop = new Stop();
            stop.setStopName(routeModel.getStopNames().get(i));
    		stops.add(stop);
    	}
        route.setStops(stops);
    	return route;
    }

    private RouteModel convertToRouteModel ( final Route route ) {
        RouteModel routeModel = new RouteModel();
        routeModel.setRouteNumber(route.getNumber());
        List<Stop> stops = route.getStops();
        List<String> stopNames = new ArrayList<String>();
        for ( int i = 0; i < stops.size(); i++ ) {
            stopNames.add(stops.get(i).getStopName());
        }
        routeModel.setStopNames(stopNames);
        return routeModel;
    }
    
    /**
     * Get the route object based on the route number,
     * @param routeNumber a <code>String</code> with the route number,
     * @return a <code>RouteModel</code> object.
     */
    public RouteModel getRoute ( String routeNumber ) {
        Route route = routeRepository.findRouteByNumber(routeNumber);
        if ( route != null ) {
            return convertToRouteModel(route);
        }
        return null;
    }

    public RouteModel[] getAllRoutes ( ) {
        List<Route> routes = routeRepository.findAll();
        RouteModel[] routeModels = new RouteModel[routes.size()];
        for ( int i = 0; i < routes.size(); i++ ) {
            routeModels[i] = convertToRouteModel(routes.get(i));
        }
        return routeModels;
    }

    public void removeRoute ( final RouteModel routeModel ) {
        routeRepository.delete(routeRepository.findRouteByNumber(routeModel.getRouteNumber()));
    }

    /**
     * Delete all routes (only used as part of load function)
     */
    public void deleteAllRoutes() {
        routeRepository.deleteAll();
    }

}
