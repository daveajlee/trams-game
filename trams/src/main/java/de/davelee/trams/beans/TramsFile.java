package de.davelee.trams.beans;

import de.davelee.trams.api.response.*;
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
    private CompanyResponse[] gameModel;
    private MessageResponse[] messageModels;
    private RouteResponse[] routeModels;
	private String[] stops;
    private VehicleResponse[] vehicleModels;

}