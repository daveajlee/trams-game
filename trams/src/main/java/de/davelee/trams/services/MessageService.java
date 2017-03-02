package de.davelee.trams.services;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.davelee.trams.data.Message;
import de.davelee.trams.db.DatabaseManager;
import de.davelee.trams.util.MessageFolder;

public class MessageService {
	
	private static final Logger logger = LoggerFactory.getLogger(MessageService.class);
	
	private DatabaseManager databaseManager; 
	
	public MessageService() {
		
	}
	
	/**
     * Create a new message.
     * @param subject a <code>String</code> with the subject.
     * @param text a <code>String</code> with the message text.
     * @param sender a <code>String</code> with the sender.
     * @param folder a <code>String</code> with the folder.
     * @param date a <code>Calendar</code> with the date.
     * @param messageType a <code>MessageType</code> Enum with the message type.
     */
    public Message createMessage ( String subject, String text, String sender, MessageFolder folder, Calendar date ) {
        Message message = new Message();
    	message.setSubject(subject);
        message.setText(text);
        message.setSender(sender);
        message.setFolder(folder);
        message.setDate(date);
        return message;
    }
    
    /**
     * Get a linked list of messages which are relevant for the specified folder, date and sender.
     * @param folder a <code>String</code> with the name of the folder.
     * @param date a <code>String</code> with the date.
     * @param type a <code>String</code> with the sender.
     * @return a <code>LinkedList</code> with messages.
     */
    public long[] getMessageIds ( List<Message> messageQueue, MessageFolder folder, String date, String sender ) {
        //Create a blank linked list first.
        LinkedList<Message> messages = new LinkedList<Message>();
        logger.debug("There are " + messageQueue.size() + " messages!");
        //Go through all of the messages and see if they are applicable.
        for ( int i = 0; i < messageQueue.size(); i++ ) {
            logger.debug("This is message " + messageQueue.get(i).getSubject());
            logger.debug("Date is " + date);
            if ( date.equalsIgnoreCase("All Dates") ) {
                logger.debug("Date was ok!");
                if ( folder==messageQueue.get(i).getFolder() && sender.contentEquals(messageQueue.get(i).getSender())) {
                    messages.add(messageQueue.get(i));
                }
            }
            //TODO: reimplement date comparison
            else if ( date.equalsIgnoreCase(messageQueue.get(i).getDate().toString()) && folder==messageQueue.get(i).getFolder() && sender.contentEquals(messageQueue.get(i).getSender()) ) {
                messages.add(messageQueue.get(i));
            }
        }
        long[] messageIds = new long[messages.size()];
        for ( int i = 0; i < messageIds.length; i++ ) {
        	messageIds[i] = messages.get(i).getId();
        }
        //Return a message list.
        return messageIds;
    }
    
    public Message getMessageById(long id) {
    	return databaseManager.getMessageById(id);
    }
    
    public void saveMessage(Message message) {
    	databaseManager.saveMessage(message);
    }
    
    public List<Message> getAllMessages() {
    	return databaseManager.getAllMessages();
    }

}
