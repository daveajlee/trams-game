package de.davelee.trams.model;

import de.davelee.trams.util.MessageFolder;

public class MessageModel {
	
	private String subject;
	private String text;
	private String sender;
	private String date;
	private MessageFolder messageFolder;
	
	public String getSubject() {
		return subject;
	}
	
	public void setSubject(final String subject) {
		this.subject = subject;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(final String text) {
		this.text = text;
	}
	
	public String getSender() {
		return sender;
	}
	
	public void setSender(final String sender) {
		this.sender = sender;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setDate(final String date) {
		this.date = date;
	}
	
	public MessageFolder getMessageFolder() {
		return messageFolder;
	}
	
	public void setMessageFolder(final MessageFolder messageFolder) {
		this.messageFolder = messageFolder;
	}

}
