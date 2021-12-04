package de.davelee.trams.api.response;

import lombok.*;

/**
 * This class represents a response containing a single message returned from the server.
 * @author Dave Lee
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MessageResponse {

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
     * The date and time that this message was sent.
     */
    private String dateTime;

}
