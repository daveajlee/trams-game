package de.davelee.trams.controllers;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import de.davelee.trams.services.MessageService;
import de.davelee.trams.util.MessageFolder;
import de.davelee.trams.data.Message;

public class MessageController {
	
	@Autowired
	private MessageService messageService;
    
    /**
     * Get a linked list of messages which are relevant for the specified folder, date and sender.
     * @param folder a <code>String</code> with the name of the folder.
     * @param date a <code>String</code> with the date.
     * @param sender a <code>String</code> with the sender.
     * @return a <code>LinkedList</code> with messages.
     */
    public long[] getMessageIds ( final String folder, final String date, final String sender ) {
        //Return a message list.
        return messageService.getMessageIds(messageService.getAllMessages(), MessageFolder.valueOf(folder), date, sender);
    }
    
    public String[] getMessageSubjects ( final String folder, final String date, final String sender ) {
    	long[] messageIds = messageService.getMessageIds(messageService.getAllMessages(), MessageFolder.valueOf(folder), date, sender);
    	String[] subjects = new String[messageIds.length];
    	for ( int i = 0; i < subjects.length; i++ ) {
    		subjects[i] = messageService.getMessageById(messageIds[i]).getSubject();
    	}
    	return subjects;
    }
    
    public String getMessageText ( final String folder, final String date, final String sender, final int pos ) {
    	return messageService.getMessageById(messageService.getMessageIds(messageService.getAllMessages(), MessageFolder.valueOf(folder), date, sender)
    			[pos]).getText();
    }
    
    /**
     * Get the number of messages.
     * @return a <code>int</code> with the number of messages.
     */
    public int getNumberMessages ( ) {
        return messageService.getAllMessages().size();
    }
    
    /**
     * Get the message at the supplied position.
     * @param pos a <code>int</code> with the position.
     * @return a <code>Message</code> object which is at the supplied position.
     */
    public long getMessageId ( final int pos ) {
        return messageService.getAllMessages().get(pos).getId();
    }
    
    public Calendar getMessageDateByPosition ( final int position ) {
    	return messageService.getMessageById(position).getDate();
    }
    
    /**
     * Add a message to the message queue.
     * @param msg a <code>Message</code> object!
     */
    public void addMessage ( final String subject, final String text, final String sender, final String folder, final Calendar date) {
    	messageService.saveMessage(messageService.createMessage(subject, text, sender, MessageFolder.valueOf(folder), date));
    }
    
    public List<Message> getAllMessages ( ) {
    	return messageService.getAllMessages();
    }

}
