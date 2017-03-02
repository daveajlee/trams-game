package de.davelee.trams.services;

import java.util.Calendar;

import de.davelee.trams.data.Stop;
import de.davelee.trams.util.DateFormats;

public class StopService {
	
	public StopService() {
		
	}
	
	/**
     * Get the stop time as hh:mm.
     * @return a <code>String</code> with the time as hh:mm.
     */
    public String getDisplayStopTime( Calendar stopTime ) {
    	return DateFormats.HOUR_MINUTE_FORMAT.getFormat().format(stopTime.getTime());
    }
    
    public Stop createStop ( final String stopName, final Calendar stopTime ) {
    	Stop stop = new Stop();
    	stop.setStopName(stopName);
    	stop.setStopTime(stopTime);
    	return stop;
    }

}
