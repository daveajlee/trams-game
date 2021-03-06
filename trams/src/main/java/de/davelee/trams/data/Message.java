package de.davelee.trams.data;

import de.davelee.trams.util.MessageFolder;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.Lob;

/**
 * Class representing a message in TraMS.
 * @author Dave Lee
 */
@Entity
@Table(name="MESSAGE", uniqueConstraints=@UniqueConstraint(columnNames = {"subject", "sender", "date"}))
public class Message {
	
	@Id
	@GeneratedValue
	@Column(nullable=false)
	private int id;

	@Column
    private String subject;

    @Lob
	@Column(name="text", length=1024)
    private String text;
	
	@Column
    private String sender;
	
	@Column
    private String date;
	
	@Column
	private MessageFolder folder;

    public int getId() {
		return id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	/**
     * Get the subject of this message.
     * @return a <code>String</code> with the subject.
     */
    public String getSubject ( ) {
        return subject;
    }
    
    public void setSubject ( final String subject ) {
    	this.subject = subject;
    }

    /**
     * Get the text of this message.
     * @return a <code>String</code> with the text of the message.
     */
    public String getText ( ) {
        return text;
    }
    
    public void setText ( final String text ) {
    	this.text = text;
    }

    /**
     * Get the sender of this message.
     * @return a <code>String</code> with the sender.
     */
    public String getSender ( ) {
        return sender;
    }
    
    public void setSender ( final String sender ) {
    	this.sender = sender;
    }
    
    /**
     * Get the date of this message.
     * @return a <code>String</code> with the date.
     */
    public String getDate ( ) {
        return date;
    }
    
    public void setDate(final String date) {
    	this.date = date;
    }

    /**
     * Get the folder of this message.
     * @return a <code>MessageFolder</code> Enum with the type.
     */
    public MessageFolder getFolder ( ) {
    	return folder;
    }
    
    public void setFolder ( final MessageFolder folder ) {
    	this.folder = folder;
    }

}
