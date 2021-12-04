package de.davelee.trams.api.request;

import lombok.*;

/**
 * This class represents a request to adjust the balance
 * by either crediting or withdrawing money for a particular company.
 * @author Dave Lee
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AdjustBalanceRequest {

    /**
     * The name of the company to adjust the balance.
     */
    private String company;

    /**
     * The value to either subtract (minus) or credit (plus) to the balance.
     */
    private double value;

}