package de.davelee.trams.controllers;

import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.util.Random;

/**
 * This class controls the loading and saving of tips in TraMS.
 * @author Dave Lee
 */
@Controller
public class TipController {

	//This is where we keep the tip messages.
	private String[] tipMessages;

	/**
	 * Initialise the tip messages that should be showed to the user.
	 */
	@PostConstruct
	public void init() {
		tipMessages = new String[] {
			"TIP: Watch your balance! You can't buy new vehicles or run more routes if you don't have money!",
			"TIP: Earn money by improving your passenger satisfaction through running vehicles on time!",
			"TIP: If your passenger satisfaction falls too low, you may be forced to resign!"
		};

	}

	/**
	 * This is a method to randomly pick a tip from the tip messages list and return it!
	 * @return a <code>String</code> with a random tip message.
	 */
	public String getRandomTipMessage ( ) {
		Random r = new Random();
		return tipMessages[r.nextInt(tipMessages.length)];
	}

}
