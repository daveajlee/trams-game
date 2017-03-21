package de.davelee.trams.controllers;

import de.davelee.trams.services.TipService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class TipController {
	
	@Autowired
	private TipService tipService;
	
	public String getRandomTipMessage ( ) {
		return tipService.getRandomTipMessage();
	}

}
