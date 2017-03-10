package de.davelee.trams.data;

import java.util.Calendar;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Class to represent a time for a journey to arrive at a stop.
 * @author Dave Lee
 */
@Entity
@Table(name="STOP_TIME")
public class StopTime {

    @Id
	@GeneratedValue
	@Column(name="STOP_TIME_ID")
	private long id;

    @Column(name="JOURNEY_ID")
	private long journeyId;

    @Column(name="STOP_ID")
	private long stopId;

    @Column(name="TIME")
	private Calendar time;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getJourneyId() {
        return journeyId;
    }

    public void setJourneyId(long journeyId) {
        this.journeyId = journeyId;
    }

    public long getStopId() {
        return stopId;
    }

    public void setStopId(long stopId) {
        this.stopId = stopId;
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }
}