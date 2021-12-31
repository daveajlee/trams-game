package de.davelee.trams.services;

import de.davelee.trams.api.request.MessageRequest;
import de.davelee.trams.api.response.MessageResponse;
import de.davelee.trams.api.response.MessagesResponse;
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
     * @param messageRequest a <code>MessageRequest</code> object representing the message to save.
     */
    public void saveMessage ( final MessageRequest messageRequest ) {
        restTemplate.postForObject(crmServerUrl + "message/", messageRequest, Void.class);
    }

    /**
     * Get a linked list of messages which are relevant for the specified folder, date and sender.
     * @param company a <code>String</code> containing the name of the company to save the message for.
     * @param folder a <code>MessageFolder</code> with the name of the folder.
     * @param date a <code>String</code> with the date.
     * @param sender a <code>String</code> with the sender.
     * @return a <code>LinkedList</code> with messages.
     */
    public MessageResponse[] getMessagesByFolderSenderDate ( final String company, final MessageFolder folder, final String date, final String sender ) {
        MessagesResponse messagesResponse = restTemplate.getForObject(crmServerUrl + "messages/?company=" + company + "&folder=" + folder + "&sender=" + sender + "&date=" + date, MessagesResponse.class);
        if ( messagesResponse != null && messagesResponse.getMessageResponses() != null ) {
            return messagesResponse.getMessageResponses();
        }
        return null;
    }

    /**
     * Get a linked list of messages which are relevant for the specified folder.
     * @param company a <code>String</code> containing the name of the company to save the message for.
     * @param folder a <code>MessageFolder</code> with the name of the folder.
     * @return a <code>LinkedList</code> with messages.
     */
    public MessageResponse[] getMessagesByFolder ( final String company, final MessageFolder folder ) {
        MessagesResponse messagesResponse = restTemplate.getForObject(crmServerUrl + "messages/?company=" + company + "&folder=" + folder, MessagesResponse.class);
        if ( messagesResponse != null && messagesResponse.getMessageResponses() != null ) {
            return messagesResponse.getMessageResponses();
        }
        return null;
    }

    public MessageResponse[] getAllMessages(final String company) {
        MessagesResponse messagesResponse = restTemplate.getForObject(crmServerUrl + "messages/?company=" + company, MessagesResponse.class);
        if ( messagesResponse != null && messagesResponse.getMessageResponses() != null ) {
            return messagesResponse.getMessageResponses();
        }
        return null;
    }

    /**
     * Delete all messages (only used as part of load function)
     * @param company a <code>String</code> containing the name of the company to delete all messages for.
     */
    public void deleteAllMessages(final String company) {
        restTemplate.delete(crmServerUrl + "messages/?company=" + company);
    }

}
