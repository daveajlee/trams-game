package de.davelee.trams.api.request;

import lombok.*;

/**
 * This class represents a request to export all the company information to a JSON file including the supplied JSON information.
 * @author Dave Lee
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExportCompanyRequest {

    /**
     * The name of the company to export.
     */
    private String company;

    /**
     * The player name of the company to export.
     */
    private String playerName;

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
