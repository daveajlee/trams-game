package de.davelee.trams.data;

import java.util.Calendar;
import javax.persistence.*;

/**
 * Class to represent a time for a journey to arrive at a stop.
 * @author Dave Lee
 */
@Entity
@Table(name="STOP_TIME")
public class StopTime {

    @Id
	@GeneratedValue
	@Column
	private long id;

    @Column
	private int journeyNumber;

    @Column
	private int routeScheduleNumber;

    @Column
	private String routeNumber;

    @Column
	private String stopName;

    @Column
	private Calendar time;

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

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

    public String getStopName() {
        return stopName;
    }

    public void setStopName(final String stopName) {
        this.stopName = stopName;
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(final Calendar time) {
        this.time = time;
    }
}