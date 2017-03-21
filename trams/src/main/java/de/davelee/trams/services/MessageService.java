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

    public MessageModel[] getMessagesByFolderSenderDate ( final MessageFolder folder, final Calendar date, final String sender ) {
        List<Message> messages = messageRepository.findByFolderAndSenderAndDate(folder, sender, date);
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
