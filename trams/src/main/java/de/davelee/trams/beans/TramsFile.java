package de.davelee.trams.beans;

import de.davelee.trams.api.response.*;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * This class represents the content of a file which can be saved or loaded and contains all of the data for a single TraMS game.
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TramsFile {

    private UserResponse[] driverModels;
    private CompanyResponse[] gameModel;
    private MessageResponse[] messageModels;
    private RouteResponse[] routeModels;
	private String[] stops;
    private VehicleResponse[] vehicleModels;

}