package de.davelee.trams.data;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import de.davelee.trams.util.MessageFolder;

/**
 * Class representing a message in TraMS.
 * @author Dave Lee
 */
@Entity
@Table(name="MESSAGE")
public class Message {
	
	@Id
	@GeneratedValue
	@Column(name="MESSAGE_ID", nullable=false)
	private int id;

	@Column(name="SUBJECT")
    private String subject;
	
	@Column(name="TEXT")
    private String text;
	
	@Column(name="SENDER")
    private String sender;
	
	@Column(name="DATE")
    private Calendar date;
	
	@Column(name="FOLDER")
	private MessageFolder folder;

    /**
     * Create a new message.
     */
    public Message ( ) {
    }

    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
     * Get the subject of this message.
     * @return a <code>String</code> with the subject.
     */
    public String getSubject ( ) {
        return subject;
    }
    
    public void setSubject ( String subject ) {
    	this.subject = subject;
    }

    /**
     * Get the text of this message.
     * @return a <code>String</code> with the text of the message.
     */
    public String getText ( ) {
        return text;
    }
    
    public void setText ( String text ) {
    	this.text = text;
    }

    /**
     * Get the sender of this message.
     * @return a <code>String</code> with the sender.
     */
    public String getSender ( ) {
        return sender;
    }
    
    public void setSender ( String sender ) {
    	this.sender = sender;
    }
    
    /**
     * Get the date of this message.
     * @return a <code>Calendar</code> with the date.
     */
    public Calendar getDate ( ) {
        return date;
    }
    
    public void setDate(Calendar date) {
    	this.date = date;
    }

    /**
     * Get the folder of this message.
     * @return a <code>MessageFolder</code> Enum with the type.
     */
    public MessageFolder getFolder ( ) {
    	return folder;
    }
    
    public void setFolder ( MessageFolder folder ) {
    	this.folder = folder;
    }

}
