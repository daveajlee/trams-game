package de.davelee.trams.services;

import de.davelee.trams.api.request.AddStopRequest;
import de.davelee.trams.api.response.StopsResponse;
import de.davelee.trams.model.JourneyModel;
import de.davelee.trams.model.StopTimeModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class JourneyService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${server.operations.url}")
    private String operationsServerUrl;
    
    /**
     * Get stop object based on stop name.
     * @param journeyModel a <code>JourneyModel</code> with the journey details.
     * @param name a <code>String</code> with the stop name.
     * @return a <code>Stop</code> object.
     */
    public StopTimeModel getStopTime ( final JourneyModel journeyModel, final String name ) {
        for ( StopTimeModel stopTimeModel : journeyModel.getStopTimeModelList()) {
            if ( stopTimeModel.getStopName().equalsIgnoreCase(name) ) {
                return stopTimeModel;
            }
        }
        return null;
    }

    public void saveStop ( final String stopName, final String company ) {
        restTemplate.postForObject(operationsServerUrl + "stop/",
                AddStopRequest.builder()
                        .company(company)
                        .name(stopName)
                        .build(),
                Void.class);
    }

    public String[] getAllStops(final String company) {
        StopsResponse stopsResponse = restTemplate.getForObject(operationsServerUrl + "stops/?company=" + company, StopsResponse.class);
        String[] stopNames = new String[stopsResponse.getStopResponses().length];
        for ( int i = 0; i < stopNames.length; i++ ) {
            stopNames[i] = stopsResponse.getStopResponses()[i].getName();
        }
        return stopNames;
    }

    /**
     * Get the distance between two stops.
     * @param scenarioStopDistances a <code>List</code> of <code>String</code> objects with the distances between stops.
     * @param stop1 a <code>String</code> with the name of the first stop.
     * @param stop2 a <code>String</code> with the name of the second stop.
     * @return a <code>int</code> with the distance between two stops.
     */
    public int getDistance ( final List<String> scenarioStopDistances, final String stop1, final String stop2 ) {
        int stop1Pos = -1; int stop2Pos = -1; int count = 0;
        for ( String stopDistance : scenarioStopDistances ) {
            String stopName = stopDistance.split(":")[0];
            if ( stopName.equalsIgnoreCase(stop1) ) { stop1Pos = count; }
            else if ( stopName.equalsIgnoreCase(stop2) ) { stop2Pos = count; }
            count++;
        }
        return Integer.parseInt(scenarioStopDistances.get(stop1Pos).split(":")[1].split(",")[stop2Pos]);
    }

    /**
     * Remove all existing stops (used only for the load function)
     * @param company a <code>String</code> containing the name of the company to delete all stops for.
     */
    public void deleteAllStops ( final String company ) {
        restTemplate.delete(operationsServerUrl + "stops/?company=" + company);
    }

}
