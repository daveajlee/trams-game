package de.davelee.trams.controllers;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;

import de.davelee.trams.services.GameService;

public class GameController {
	
	@Autowired
	private GameService gameService;
	
	public void withdrawBalance ( double amount ) {
		gameService.withdrawBalance(amount);
	}
	
	/**
     * Get current simulated time. 
     * @return a <code>Calendar</code> representing the current simulated time.
     */
    public Calendar getCurrentSimTime ( ) {
        return gameService.getCurrentTime();
    }

	public void creditBalance ( double amount ) {
		gameService.creditBalance(amount);
	}

}
