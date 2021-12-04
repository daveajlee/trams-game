package de.davelee.trams.api.response;

import lombok.*;

/**
 * This class represents a response containing the company
 * and its current satisfaction rate.
 * @author Dave Lee
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SatisfactionRateResponse {

    /**
     * The name of the company.
     */
    private String company;

    /**
     * The satisfaction rate of the company.
     */
    private double satisfactionRate;


}
