package de.davelee.trams.api.request;

import lombok.*;

/**
 * This class represents a request to add the following company to the server
 * containing name, playerName and starting balance and time.
 * @author Dave Lee
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CompanyRequest {

    /**
     * The name of the company to request.
     */
    private String name;

    /**
     * The starting balance of this company.
     */
    private double startingBalance;

    /**
     * The player name for the company.
     */
    private String playerName;

    /**
     * The starting time for this company.
     */
    private String startingTime;

    /**
     * The scenario which this company was generated for (can be empty).
     */
    private String scenarioName;

    /**
     * The difficulty level which this company should be run at (can be EASY, MEDIUM or HARD)
     */
    private String difficultyLevel;

}
