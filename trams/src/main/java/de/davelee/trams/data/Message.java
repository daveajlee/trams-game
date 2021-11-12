package de.davelee.trams.data;

import de.davelee.trams.util.MessageFolder;
import lombok.Getter;
import lombok.Setter;

/**
 * Class representing a message in TraMS.
 * @author Dave Lee
 */
@Getter
@Setter
public class Message {

	private int id;
    private String subject;
    private String text;
    private String sender;
    private String date;
	private MessageFolder folder;

}
