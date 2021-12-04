package de.davelee.trams.api.response;

import lombok.*;

/**
 * This class represents a response from the server containing details
 * of all matched messages according to specified criteria. As well as containing details about the messages in form of
 * an array of <code>MessageResponse</code> objects, the object also contains a simple count of the messages.
 * @author Dave Lee
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MessagesResponse {

    //a count of the number of messages which were found by the server.
    private Long count;

    //an array of all messages found by the server.
    private MessageResponse[] messageResponses;

}