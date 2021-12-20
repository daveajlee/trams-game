package de.davelee.trams.beans;

import de.davelee.trams.model.DriverModel;
import de.davelee.trams.model.GameModel;
import de.davelee.trams.model.MessageModel;
import de.davelee.trams.model.RouteModel;
import de.davelee.trams.model.VehicleModel;
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

    private DriverModel[] driverModels;
    private GameModel[] gameModel;
    private MessageModel[] messageModels;
    private RouteModel[] routeModels;
	private String[] stops;
    private VehicleModel[] vehicleModels;

}