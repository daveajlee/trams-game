package trams.data;

import java.util.Calendar;

/**
 * Class representing a message in TraMS.
 * @author Dave Lee
 */
public class Message {

    private String subject;
    private String text;
    private String sender;
    private String folder;
    private String date;
    private String type;

    public Message ( ) { }

    public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
     * Get the subject of this message.
     * @return a <code>String</code> with the subject.
     */
    public String getSubject ( ) {
        return subject;
    }

    /**
     * Get the text of this message.
     * @return a <code>String</code> with the text of the message.
     */
    public String getText ( ) {
        return text;
    }

    /**
     * Get the sender of this message.
     * @return a <code>String</code> with the sender.
     */
    public String getSender ( ) {
        return sender;
    }

    /**
     * Get the folder which this message belongs in.
     * @return a <code>String</code> with the folder.
     */
    public String getFolder ( ) {
        return folder;
    }

    /**
     * Get the date of this message.
     * @return a <code>String</code> with the date.
     */
    public String getDate ( ) {
        return date;
    }

    /**
     * Get the type of this message.
     * @return a <code>String</code> with the type.
     */
    public String getType ( ) {
    	return type;
    }

}
