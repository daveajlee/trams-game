package de.davelee.trams.api.request;

import lombok.*;

/**
 * This class represents a request to generate stop times automatically.
 * @author Dave Lee
 */
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@ToString
public class GenerateStopTimesRequest {

    /**
     * The company that should run these stop times.
     */
    private String company;

    /**
     * Information about the stops that should be served.
     */
    private StopPatternRequest stopPatternRequest;

    /**
     * The route number serving these stops.
     */
    private String routeNumber;

    /**
     * The start time from when stop times should be generated in the format HH:mm.
     */
    private String startTime;

    /**
     * The end time until when stop times should be generated in the format HH:mm.
     */
    private String endTime;

    /**
     * The frequency in which stops times should be generated in minutes. Minimum value is 1.
     */
    private int frequency;

    /**
     * The valid from date with the date from which stop times are valid. The date is inclusive.
     */
    private String validFromDate;

    /**
     * The valid to date until when stop times are valid. The date is inclusive.
     */
    private String validToDate;

    /**
     * The days when these stop times run.
     */
    private String operatingDays;

}
