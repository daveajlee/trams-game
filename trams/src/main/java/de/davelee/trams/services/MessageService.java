package de.davelee.trams.services;

import de.davelee.trams.api.request.MessageRequest;
import de.davelee.trams.api.response.MessageResponse;
import de.davelee.trams.api.response.MessagesResponse;
import de.davelee.trams.model.MessageModel;

import de.davelee.trams.util.MessageFolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MessageService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${server.crm.url}")
    private String crmServerUrl;
	
	/**
     * Save a new message.
     * @param messageModel a <code>MessageModel</code> object representing the message to save.
     * @param company a <code>String</code> containing the name of the company to save the message for.
     */
    public void saveMessage ( final MessageModel messageModel, final String company ) {
        restTemplate.postForObject(crmServerUrl + "message/",
                MessageRequest.builder()
                        .company(company)
                        .subject(messageModel.getSubject())
                        .text(messageModel.getText())
                        .sender(messageModel.getSender())
                        .folder(messageModel.getMessageFolder().getDisplayName())
                        .dateTime(messageModel.getDate())
                        .build(),
                Void.class);
    }

    private MessageModel convertToMessageModel ( final MessageResponse messageResponse ) {
        return MessageModel.builder()
                .date(messageResponse.getDateTime())
                .messageFolder(MessageFolder.valueOf(messageResponse.getFolder()))
                .sender(messageResponse.getSender())
                .subject(messageResponse.getSubject())
                .text(messageResponse.getText())
                .build();
    }

    /**
     * Get a linked list of messages which are relevant for the specified folder, date and sender.
     * @param company a <code>String</code> containing the name of the company to save the message for.
     * @param folder a <code>MessageFolder</code> with the name of the folder.
     * @param date a <code>String</code> with the date.
     * @param sender a <code>String</code> with the sender.
     * @return a <code>LinkedList</code> with messages.
     */
    public MessageModel[] getMessagesByFolderSenderDate ( final String company, final MessageFolder folder, final String date, final String sender ) {
        MessagesResponse messagesResponse = restTemplate.getForObject(crmServerUrl + "messages/?company=" + company + "&folder=" + folder + "&sender=" + sender + "&date=" + date, MessagesResponse.class);
        MessageModel[] messageModels = new MessageModel[messagesResponse.getMessageResponses().length];
        for ( int i = 0; i < messageModels.length; i++ ) {
            messageModels[i] = convertToMessageModel(messagesResponse.getMessageResponses()[i]);
        }
        return messageModels;
    }

    /**
     * Get a linked list of messages which are relevant for the specified folder.
     * @param company a <code>String</code> containing the name of the company to save the message for.
     * @param folder a <code>MessageFolder</code> with the name of the folder.
     * @return a <code>LinkedList</code> with messages.
     */
    public MessageModel[] getMessagesByFolder ( final String company, final MessageFolder folder ) {
        MessagesResponse messagesResponse = restTemplate.getForObject(crmServerUrl + "messages/?company=" + company + "&folder=" + folder, MessagesResponse.class);
        MessageModel[] messageModels = new MessageModel[messagesResponse.getMessageResponses().length];
        for ( int i = 0; i < messageModels.length; i++ ) {
            messageModels[i] = convertToMessageModel(messagesResponse.getMessageResponses()[i]);
        }
        return messageModels;
    }

    public MessageModel[] getAllMessages(final String company) {
        MessagesResponse messagesResponse = restTemplate.getForObject(crmServerUrl + "messages/?company=" + company, MessagesResponse.class);
        MessageModel[] messageModels = new MessageModel[messagesResponse.getMessageResponses().length];
        for ( int i = 0; i < messageModels.length; i++ ) {
            messageModels[i] = convertToMessageModel(messagesResponse.getMessageResponses()[i]);
        }
        return messageModels;
    }

    /**
     * Delete all messages (only used as part of load function)
     * @param company a <code>String</code> containing the name of the company to delete all messages for.
     */
    public void deleteAllMessages(final String company) {
        restTemplate.delete(crmServerUrl + "messages/?company=" + company);
    }

}
