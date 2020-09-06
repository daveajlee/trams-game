package de.davelee.trams.beans;

import de.davelee.trams.model.DriverModel;
import de.davelee.trams.model.GameModel;
import de.davelee.trams.model.JourneyModel;
import de.davelee.trams.model.JourneyPatternModel;
import de.davelee.trams.model.MessageModel;
import de.davelee.trams.model.RouteModel;
import de.davelee.trams.model.RouteScheduleModel;
import de.davelee.trams.model.TimetableModel;
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
    private JourneyModel[] journeyModels;
    private JourneyPatternModel[] journeyPatternModels;
    private MessageModel[] messageModels;
    private RouteModel[] routeModels;
    private RouteScheduleModel[] routeScheduleModels;
	private String[] stops;
    private TimetableModel[] timetableModels;
    private VehicleModel[] vehicleModels;

}