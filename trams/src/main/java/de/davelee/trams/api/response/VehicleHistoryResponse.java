package de.davelee.trams.api.response;

import lombok.*;

/**
 * This class represents a response from the server for a particular vehicle
 * history entry containing date, reason and comment.
 * @author Dave Lee
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class VehicleHistoryResponse {

    /**
     * The date that this history entry took place in format dd-MM-yyyy.
     */
    private String date;

    /**
     * The reason for this history entry.
     */
    private String vehicleHistoryReason;

    /**
     * A comment about this history - this could be the reason it was given.
     */
    private String comment;

}