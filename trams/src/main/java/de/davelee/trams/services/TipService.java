package de.davelee.trams.services;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class TipService {
	
	//This is where we keep the tip messages.
    String[] tipMessages;

    public void init() {
    	tipMessages = new String[3];
    	//Add tip messages here.
        tipMessages[0] = "TIP: Watch your balance! You can't buy new vehicles or run more routes if you don't have money!";
        tipMessages[1] = "TIP: Earn money by improving your passenger satisfaction through running vehicles on time!";
        tipMessages[2] = "TIP: If your passenger satisfaction falls too low, you may be forced to resign!";
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
