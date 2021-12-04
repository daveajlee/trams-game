package de.davelee.trams.api.request;

import lombok.*;

/**
 * This class represents a request to add a message.
 * @author Dave Lee
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MessageRequest {

    /**
     * The company receiving this message.
     */
    private String company;

    /**
     * The subject of the message.
     */
    private String subject;

    /**
     * The content of the message.
     */
    private String text;

    /**
     * The sender of this message.
     */
    private String sender;

    /**
     * The folder where this message is located.
     */
    private String folder;

    /**
     * The date and time that this message was sent in format dd-MM-yyyy HH:mm.
     */
    private String dateTime;

}