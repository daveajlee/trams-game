package de.davelee.trams.beans;

import de.davelee.trams.api.response.MessageResponse;
import de.davelee.trams.api.response.UserResponse;
import de.davelee.trams.api.response.VehicleResponse;
import de.davelee.trams.model.GameModel;
import de.davelee.trams.model.RouteModel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TramsFile {

    private UserResponse[] driverModels;
    private GameModel[] gameModel;
    private MessageResponse[] messageModels;
    private RouteModel[] routeModels;
	private String[] stops;
    private VehicleResponse[] vehicleModels;

}