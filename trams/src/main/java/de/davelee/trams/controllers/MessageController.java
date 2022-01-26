package de.davelee.trams.controllers;

import de.davelee.trams.api.request.MessageRequest;
import de.davelee.trams.api.response.MessageResponse;
import de.davelee.trams.api.response.MessagesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

/**
 * This class enables access to Message data via REST endpoints to the TraMS CRM microservice in the TraMS Platform.
 * @author Dave Lee
 */
@Controller
public class MessageController {

    private RestTemplate restTemplate;

    @Value("${server.crm.url}")
    private String crmServerUrl;
    
    /**
     * Get a linked list of messages which are relevant for the specified folder, date and sender.
     * @param company a <code>String</code> with the name of the company.
     * @param folder a <code>String</code> with the name of the folder.
     * @param date a <code>String</code> with the date.
     * @param sender a <code>String</code> with the sender.
     * @return a <code>MessageResponse</code> array with messages.
     */
    public MessageResponse[] getMessagesByFolderDateSender ( final String company, final String folder, final String date, final String sender ) {
        if ( date.equalsIgnoreCase("All Dates")) {
            return getMessagesByFolder(company, folder);
        }
        //Return a message list.
        MessagesResponse messagesResponse = restTemplate.getForObject(crmServerUrl + "messages/?company=" + company + "&folder=" + folder + "&sender=" + sender + "&date=" + date, MessagesResponse.class);
        if ( messagesResponse != null && messagesResponse.getMessageResponses() != null ) {
            return messagesResponse.getMessageResponses();
        }
        return null;
    }

    /**
     * Get a linked list of messages which are relevant for the specified folder.
     * @param company a <code>String</code> with the name of the company.
     * @param folder a <code>String</code> with the name of the folder.
     * @return a <code>MessageResponse</code> array with messages.
     */
    public MessageResponse[] getMessagesByFolder ( final String company, final String folder ) {
        //Return a message list.
        MessagesResponse messagesResponse = restTemplate.getForObject(crmServerUrl + "messages/?company=" + company + "&folder=" + folder, MessagesResponse.class);
        if ( messagesResponse != null && messagesResponse.getMessageResponses() != null ) {
            return messagesResponse.getMessageResponses();
        }
        return null;
    }
    
    /**
     * Add a message to the message queue.
     * @param company a <code>String</code> with the name of the company.
     * @param subject a <code>String</code> with the subject of the message.
     * @param text a <code>String</code> with the text of the message.
     * @param sender a <code>String</code> with the name of the sender.
     * @param folder a <code>String</code> with the name of the folder to save the message to.
     * @param date a <code>LocalDate</code> object representing the date the message was sent.
     */
    public void addMessage ( final String company, final String subject, final String text, final String sender, final String folder, final String date) {
        restTemplate.postForObject(crmServerUrl + "message/", MessageRequest.builder()
                .company(company)
                .subject(subject)
                .text(text)
                .sender(sender)
                .folder(folder)
                .dateTime(date)
                .build(), Void.class);
    }

    /**
     * Return all messages for a particular company.
     * @param company a <code>String</code> with the name of the company.
     * @return a <code>MessageResponse</code> array containing all the messages for this company.
     */
    public MessageResponse[] getAllMessages (final String company ) {
        MessagesResponse messagesResponse = restTemplate.getForObject(crmServerUrl + "messages/?company=" + company, MessagesResponse.class);
        if ( messagesResponse != null && messagesResponse.getMessageResponses() != null ) {
            return messagesResponse.getMessageResponses();
        }
        return null;
    }

    /**
     * Load Messages.
     * @param messageResponses an array of <code>MessageResponse</code> objects with messages to store and delete all other messages.
     * @param company a <code>String</code> with the name of the company.
     */
    public void loadMessages ( final MessageResponse[] messageResponses, final String company ) {
        restTemplate.delete(crmServerUrl + "messages/?company=" + company);
        for ( MessageResponse messageResponse : messageResponses ) {
            restTemplate.postForObject(crmServerUrl + "message/", MessageRequest.builder()
                    .company(company)
                    .subject(messageResponse.getSubject())
                    .text(messageResponse.getText())
                    .sender(messageResponse.getSender())
                    .folder(messageResponse.getFolder())
                    .dateTime(messageResponse.getDateTime())
                    .build(), Void.class);
        }
    }

    /**
     * Set the rest template object via Spring.
     * @param restTemplate a <code>RestTemplate</code> object.
     */
    @Autowired
    public void setRestTemplate(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

}
