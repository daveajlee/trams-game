package de.davelee.trams.api.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * This class represents a response from the server containing details of a single stop time according to specified criteria.
 * @author Dave Lee
 */
@Builder
@Getter
@Setter
@ToString
public class StopTimeResponse {

    /**
     * The name of the stop where the journey will arrive or depart.
     */
    private String stopName;

    /**
     * The name of the company operating this journey.
     */
    private String company;

    /**
     * The arrival time when the journey will arrive which may be null if journey starts here.
     */
    private String arrivalTime;

    /**
     * The departure time when the journey will depart which may be null if journey ends here.
     */
    private String departureTime;

    /**
     * The destination of this journey which may be equal to the stop name if the journey ends here.
     */
    private String destination;

    /**
     * The number of the route which this journey is a part of.
     */
    private String routeNumber;

    /**
     * The date from which this stop occurs (inclusive).
     */
    private String validFromDate;

    /**
     * The date until which this stop occurs (inclusive).
     */
    private String validToDate;

    /**
     * The days on which this stop takes place.
     */
    private List<String> operatingDays;

    /**
     * The number of the journey which can contain both alphanumeric and alphabetical characters.
     */
    private String journeyNumber;

}
