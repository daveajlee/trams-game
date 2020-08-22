package de.davelee.trams.model;

import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Builder
public class JourneyModel {

	private int journeyNumber;
	private int routeScheduleNumber;
	private String routeNumber;
	private List<StopTimeModel> stopTimeModelList = new ArrayList<StopTimeModel>();

	public int getJourneyNumber() {
		return journeyNumber;
	}

	public void setJourneyNumber(final int journeyNumber) {
		this.journeyNumber = journeyNumber;
	}

	public int getRouteScheduleNumber() {
		return routeScheduleNumber;
	}

	public void setRouteScheduleNumber(final int routeScheduleNumber) {
		this.routeScheduleNumber = routeScheduleNumber;
	}

	public String getRouteNumber() {
		return routeNumber;
	}

	public void setRouteNumber(final String routeNumber) {
		this.routeNumber = routeNumber;
	}

	public List<StopTimeModel> getStopTimeModelList() {
		return stopTimeModelList;
	}

	public void setStopTimeModelList(final List<StopTimeModel> stopTimeModelList) {
		this.stopTimeModelList = stopTimeModelList;
	}

	public void removeStopTimeInList(final String stopName) {
		for ( StopTimeModel stopTimeModel : stopTimeModelList ) {
			if ( stopTimeModel.getStopName().equalsIgnoreCase(stopName) ) {
				stopTimeModelList.remove(stopTimeModel);
				return;
			}
		}
	}

	public void addStopTimeToList ( final StopTimeModel stopTimeModel ) {
		stopTimeModelList.add(stopTimeModel);
	}

}
