package de.davelee.trams.services;

import java.util.Calendar;

import de.davelee.trams.data.Simulator;
import de.davelee.trams.util.DateFormats;

public class SimulationService {
	
	private Simulator simulator;
	
	public Simulator getSimulator() {
		return simulator;
	}

	public void setSimulator(Simulator simulator) {
		this.simulator = simulator;
	}
	
	public void createSimulator() {
		setSimulator(new Simulator());
	}

    /**
     * Increment the current time.
     */
    public void incrementTime ( ) {
        //Copy previous time first.
        simulator.setPreviousTime((Calendar) simulator.getCurrentTime().clone());
        //Increment time.
        simulator.getCurrentTime().add(Calendar.MINUTE, simulator.getTimeIncrement());
    }
    
    /**
     * Return the supplied calendar object as a formatted string.
     * @param calDate a <code>Calendar</code> object to format.
     * @return a <code>String</code> with the formatted string.
     */
    public String formatDateString ( Calendar calDate, DateFormats dateFormat ) {
    	return dateFormat.getFormat().format(calDate.getTime());
    }
    
}
