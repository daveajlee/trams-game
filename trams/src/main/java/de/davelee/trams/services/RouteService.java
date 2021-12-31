package de.davelee.trams.services;

import de.davelee.trams.api.request.AddRouteRequest;
import de.davelee.trams.api.response.RouteResponse;
import de.davelee.trams.api.response.RoutesResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RouteService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${server.operations.url}")
    private String operationsServerUrl;

    public void saveRoute ( final AddRouteRequest addRouteRequest ) {
        restTemplate.postForObject(operationsServerUrl + "route/", addRouteRequest, Void.class);
    }
    
    /**
     * Get the route object based on the route number and company.
     * @param routeNumber a <code>String</code> with the route number.
     * @param company a <code>String</code> with the name of the company.
     * @return a <code>RouteModel</code> object.
     */
    public RouteResponse getRoute ( final String routeNumber, final String company ) {
        return restTemplate.getForObject(operationsServerUrl + "route/?company=" + company + "&routeNumber=" + routeNumber, RouteResponse.class);
    }

    public RouteResponse[] getAllRoutes ( final String company ) {
        RoutesResponse routesResponse = restTemplate.getForObject(operationsServerUrl + "routes/?company=" + company, RoutesResponse.class);
        if ( routesResponse != null && routesResponse.getRouteResponses() != null ) {
            return routesResponse.getRouteResponses();
        }
        return null;
    }

    public void removeRoute ( final String company, final String routeNumber ) {
        restTemplate.delete(operationsServerUrl + "route/?company=" + company + "&routeNumber=" + routeNumber);
    }

    /**
     * Delete all routes for a company (only used as part of load function)
     * @param company a <code>String</code> with the name of the company.
     */
    public void deleteAllRoutes(final String company) {
        restTemplate.delete(operationsServerUrl + "routes/?company=" + company);
    }

}
