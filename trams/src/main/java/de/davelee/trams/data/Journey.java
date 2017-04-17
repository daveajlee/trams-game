package de.davelee.trams.data;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

/**
 * Class to represent a journey (i.e. one run of a route from terminus to terminus) in the TraMS program.
 * @author Dave
 */
@Entity
@Table(name="JOURNEY", uniqueConstraints=@UniqueConstraint(columnNames = {"routeScheduleNumber", "routeNumber", "journeyNumber"}))
public class Journey {
    
    /**
	 * 
	 */
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

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    @Column(name="JOURNEY_STOPTIMES")
    private List<StopTime> stopTimes;
    
    /**
     * Create a new journey.
     */
    public Journey ( ) {
    }
    
    /**
     * Get the journey id.
     * @return a <code>long</code> with the journey id.
     */
    public long getId () {
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

    public List<StopTime> getStopTimes() {
        return stopTimes;
    }

    public void setStopTimes(List<StopTime> stopTimes) {
        this.stopTimes = stopTimes;
    }

    public void addStopTimeToList ( StopTime stopTime ) {
        stopTimes.add(stopTime);
    }

}
