package de.davelee.trams.controllers;

import java.util.Calendar;

import de.davelee.trams.model.MessageModel;
import org.springframework.beans.factory.annotation.Autowired;

import de.davelee.trams.services.MessageService;
import de.davelee.trams.util.MessageFolder;

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
    public MessageModel[] getMessagesByFolderDateSender ( final String folder, final String date, final String sender ) {
        //Return a message list.
        //TODO: Implement date conversion - string to calendar.
        return messageService.getMessagesByFolderSenderDate(MessageFolder.valueOf(folder), Calendar.getInstance(), sender);
    }
    
    /**
     * Get the number of messages.
     * @return a <code>int</code> with the number of messages.
     */
    public int getNumberMessages ( ) {
        return messageService.getAllMessages().length;
    }
    
    /**
     * Add a message to the message queue.
     * @param msg a <code>Message</code> object!
     */
    public void addMessage ( final String subject, final String text, final String sender, final String folder, final Calendar date) {
        MessageModel messageModel = new MessageModel();
        messageModel.setSubject(subject);
        messageModel.setText(text);
        messageModel.setSender(sender);
        messageModel.setMessageFolder(MessageFolder.valueOf(folder));
        messageModel.setDate(date);
        messageService.saveMessage(messageModel);
    }

    public MessageModel[] getAllMessages ( ) { return messageService.getAllMessages();
    }

}
