package de.davelee.trams.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JourneyModel {

	private int journeyNumber;
	private int routeScheduleNumber;
	private String routeNumber;
	private List<StopTimeModel> stopTimeModelList = new ArrayList<StopTimeModel>();

	public void removeStopTimeInList(final String stopName) {
		for ( StopTimeModel stopTimeModel : stopTimeModelList ) {
			if ( stopTimeModel.getStopName().equalsIgnoreCase(stopName) ) {
				stopTimeModelList.remove(stopTimeModel);
				return;
			}
		}
	}

	public void addStopTimeToList ( final StopTimeModel stopTimeModel ) {
		if ( stopTimeModelList == null ) {
			stopTimeModelList = new ArrayList<>();
		}
		stopTimeModelList.add(stopTimeModel);
	}

}
