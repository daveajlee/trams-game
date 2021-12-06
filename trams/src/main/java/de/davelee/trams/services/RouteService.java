package de.davelee.trams.services;

import de.davelee.trams.api.request.AddRouteRequest;
import de.davelee.trams.api.response.RouteResponse;
import de.davelee.trams.api.response.RoutesResponse;
import de.davelee.trams.model.RouteModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RouteService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${server.operations.url}")
    private String operationsServerUrl;

    public void saveRoute ( final RouteModel routeModel ) {
        restTemplate.postForObject(operationsServerUrl + "route/",
                AddRouteRequest.builder()
                        .company(routeModel.getCompany())
                        .routeNumber(routeModel.getRouteNumber())
                        .build(),
                Void.class);
    }
    
    /**
     * Get the route object based on the route number and company.
     * @param routeNumber a <code>String</code> with the route number.
     * @param company a <code>String</code> with the name of the company.
     * @return a <code>RouteModel</code> object.
     */
    public RouteModel getRoute ( final String routeNumber, final String company ) {
        RouteResponse routeResponse = restTemplate.getForObject(operationsServerUrl + "route/?company=" + company + "&routeNumber=" + routeNumber, RouteResponse.class);
        if ( routeResponse != null ) {
            RouteModel.builder()
                    .routeNumber(routeNumber)
                    .build();
        }
        return null;
    }

    public RouteModel[] getAllRoutes ( final String company ) {
        RoutesResponse routesResponse = restTemplate.getForObject(operationsServerUrl + "routes/?company=" + company, RoutesResponse.class);
        if ( routesResponse != null && routesResponse.getRouteResponses() != null ) {
            RouteModel[] routeModels = new RouteModel[routesResponse.getRouteResponses().length];
            for (int i = 0; i < routesResponse.getRouteResponses().length; i++) {
                routeModels[i] = RouteModel.builder()
                        .routeNumber(routesResponse.getRouteResponses()[i].getRouteNumber())
                        .build();
            }
            return routeModels;
        }
        return null;
    }

    public void removeRoute ( final RouteModel routeModel ) {
        restTemplate.delete(operationsServerUrl + "route/?company=" + routeModel.getCompany() + "&routeNumber=" + routeModel.getRouteNumber());
    }

    /**
     * Delete all routes for a company (only used as part of load function)
     * @param company a <code>String</code> with the name of the company.
     */
    public void deleteAllRoutes(final String company) {
        restTemplate.delete(operationsServerUrl + "routes/?company=" + company);
    }

}
