package de.davelee.trams.controllers;

import de.davelee.trams.api.request.MessageRequest;
import de.davelee.trams.api.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;

import de.davelee.trams.services.MessageService;
import de.davelee.trams.util.MessageFolder;
import org.springframework.stereotype.Controller;

@Controller
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
    public MessageResponse[] getMessagesByFolderDateSender ( final String company, final String folder, final String date, final String sender ) {
        if ( date.equalsIgnoreCase("All Dates")) {
            return getMessagesByFolder(company, folder);
        }
        //Return a message list.
        return messageService.getMessagesByFolderSenderDate(company, MessageFolder.getFolderEnum(folder), date, sender);
    }

    /**
     * Get a linked list of messages which are relevant for the specified folder.
     * @param folder a <code>String</code> with the name of the folder.
     * @return a <code>LinkedList</code> with messages.
     */
    public MessageResponse[] getMessagesByFolder ( final String company, final String folder ) {
        //Return a message list.
        return messageService.getMessagesByFolder(company, MessageFolder.getFolderEnum(folder));
    }
    
    /**
     * Add a message to the message queue.
     * @param subject a <code>String</code> with the subject of the message.
     * @param text a <code>String</code> with the text of the message.
     * @param sender a <code>String</code> with the name of the sender.
     * @param folder a <code>String</code> with the name of the folder to save the message to.
     * @param date a <code>LocalDate</code> object representing the date the message was sent.
     */
    public void addMessage ( final String company, final String subject, final String text, final String sender, final String folder, final String date) {
        messageService.saveMessage(MessageRequest.builder()
                .company(company)
                .subject(subject)
                .text(text)
                .sender(sender)
                .folder(folder)
                .dateTime(date)
                .build());
    }

    public MessageResponse[] getAllMessages (final String company ) { return messageService.getAllMessages(company);
    }

    /**
     * Load Messages.
     * @param messageModels an array of <code>MessageResponse</code> objects with messages to store and delete all other messages.
     */
    public void loadMessages ( final MessageResponse[] messageModels, final String company ) {
        messageService.deleteAllMessages(company);
        for ( MessageResponse messageModel : messageModels ) {
            messageService.saveMessage(MessageRequest.builder()
                    .company(company)
                    .subject(messageModel.getSubject())
                    .text(messageModel.getText())
                    .sender(messageModel.getSender())
                    .folder(messageModel.getFolder())
                    .dateTime(messageModel.getDateTime())
                    .build());
        }
    }

}
