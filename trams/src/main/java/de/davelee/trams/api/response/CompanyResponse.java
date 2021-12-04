package de.davelee.trams.api.response;

import lombok.*;

/**
 * This class represents a response containing all company information.
 * @author Dave Lee
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CompanyResponse {

    /**
     * The name of the company to request.
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
     * The current time for this company.
     */
    private String time;

    /**
     * The satisfaction rate for this company.
     */
    private double satisfactionRate;

}
