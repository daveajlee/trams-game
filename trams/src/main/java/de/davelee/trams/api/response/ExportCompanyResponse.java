package de.davelee.trams.api.response;

import lombok.*;

/**
 * This class represents a response containing the export of all company information including routes, drivers, vehicles and messages.
 * @author Dave Lee
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExportCompanyResponse {

    /**
     * The name of this company.
     */
    private String name;

    /**
     * The balance of this company.
     */
    private double balance;

    /**
     * The player name for the company.
     */
    private String playerName;

    /**
     * The satisfaction rate for this company.
     */
    private double satisfactionRate;

    /**
     * The current simulated time for this company.
     */
    private String time;

    /**
     * The scenario which this company was generated for (can be empty).
     */
    private String scenarioName;

    /**
     * The difficulty level which this company should be run at (can be EASY, MEDIUM or HARD)
     */
    private String difficultyLevel;

    /**
     * The route information that exist for this company.
     */
    private String routes;

    /**
     * The driver information that exist for this company.
     */
    private String drivers;

    /**
     * The vehicle information that exist for this company.
     */
    private String vehicles;

    /**
     * The message information that exist for this company.
     */
    private String messages;

}
