package de.davelee.trams.services;

import java.util.ArrayList;
import java.util.List;

import de.davelee.trams.data.Route;
import de.davelee.trams.data.Stop;
import de.davelee.trams.model.RouteModel;
import de.davelee.trams.util.TramsConstants;
import org.springframework.stereotype.Service;

@Service
public class RouteService {

    public void saveRoute ( final RouteModel routeModel ) {
        routeRepository.saveAndFlush(convertToRoute(routeModel));
    }

    private Route convertToRoute (final RouteModel routeModel ) {
    	Route route = new Route();
        route.setNumber(routeModel.getRouteNumber());
    	List<Stop> stops = new ArrayList<>();
        for ( int i = 0; i < routeModel.getStopNames().size(); i++ ) {
    		Stop stop = new Stop();
            stop.setStopName(routeModel.getStopNames().get(i));
    		stops.add(stop);
    	}
        route.setStops(stops);
    	return route;
    }

    private RouteModel convertToRouteModel ( final Route route ) {
        List<Stop> stops = route.getStops();
        List<String> stopNames = new ArrayList<>();
        for ( Stop stop : stops ) {
            stopNames.add(stop.getStopName());
        }
        return RouteModel.builder()
                .routeNumber(route.getNumber())
                .stopNames(stopNames)
                .build();
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
