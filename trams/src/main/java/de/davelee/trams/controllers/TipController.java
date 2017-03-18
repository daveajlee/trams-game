package de.davelee.trams.controllers;

import de.davelee.trams.services.TipService;

import org.springframework.beans.factory.annotation.Autowired;

public class TipController {
	
	@Autowired
	private TipService tipService;
	
	public String getRandomTipMessage ( ) {
		return tipService.getRandomTipMessage();
	}

}
