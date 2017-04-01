package de.davelee.trams.services;

import java.util.Calendar;
import java.util.List;

import de.davelee.trams.model.MessageModel;
import de.davelee.trams.repository.MessageRepository;

import de.davelee.trams.data.Message;
import de.davelee.trams.util.MessageFolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

	@Autowired
    private MessageRepository messageRepository;
	
	public MessageService() {
		
	}
	
	/**
     * Save a new message.
     * @param messageModel a <code>MessageModel</code> object representing the message to save.
     */
    public void saveMessage ( final MessageModel messageModel ) {
        Message message = new Message();
        message.setSubject(messageModel.getSubject());
        message.setText(messageModel.getText());
        message.setSender(messageModel.getSender());
        message.setFolder(messageModel.getMessageFolder());
        message.setDate(messageModel.getDate());
        messageRepository.saveAndFlush(message);
    }

    private MessageModel convertToMessageModel ( final Message message ) {
        MessageModel messageModel = new MessageModel();
        messageModel.setDate(message.getDate());
        messageModel.setMessageFolder(message.getFolder());
        messageModel.setSender(message.getSender());
        messageModel.setSubject(message.getSubject());
        messageModel.setText(message.getText());
        return messageModel;
    }

    /**
     * Get a linked list of messages which are relevant for the specified folder, date and sender.
     * @param folder a <code>MessageFolder</code> with the name of the folder.
     * @param date a <code>String</code> with the date.
     * @param sender a <code>String</code> with the sender.
     * @return a <code>LinkedList</code> with messages.
     */
    public MessageModel[] getMessagesByFolderSenderDate ( final MessageFolder folder, final String date, final String sender ) {
        List<Message> messages = messageRepository.findByFolderAndSenderAndDate(folder, sender, date);
        MessageModel[] messageModels = new MessageModel[messages.size()];
        for ( int i = 0; i < messageModels.length; i++ ) {
            messageModels[i] = convertToMessageModel(messages.get(i));
        }
        return messageModels;
    }

    /**
     * Get a linked list of messages which are relevant for the specified folder.
     * @param folder a <code>MessageFolder</code> with the name of the folder.
     * @return a <code>LinkedList</code> with messages.
     */
    public MessageModel[] getMessagesByFolder ( final MessageFolder folder ) {
        List<Message> messages = messageRepository.findByFolder(folder);
        MessageModel[] messageModels = new MessageModel[messages.size()];
        for ( int i = 0; i < messageModels.length; i++ ) {
            messageModels[i] = convertToMessageModel(messages.get(i));
        }
        return messageModels;
    }

    public MessageModel[] getAllMessages() {
        List<Message> messages = messageRepository.findAll();
        MessageModel[] messageModels = new MessageModel[messages.size()];
        for ( int i = 0; i < messageModels.length; i++ ) {
            messageModels[i] = convertToMessageModel(messages.get(i));
        }
        return messageModels;
    }

}
