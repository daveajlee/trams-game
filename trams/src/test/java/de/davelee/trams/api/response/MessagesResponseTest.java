package de.davelee.trams.api.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the constructor, getter and setter methods of the <code>MessagesResponse</code> class.
 */
public class MessagesResponseTest {

    /**
     * Test the setter methods and ensure variables are set together using the getter methods.
     */
    @Test
    public void testGettersAndSetters() {
        MessageResponse[] messageResponses = new MessageResponse[1];
        messageResponses[0] = MessageResponse.builder()
                .company("Mustermann GmbH")
                .folder("INBOX")
                .sender("Local Authority")
                .text("This is a test message")
                .subject("Test")
                .dateTime("28-12-2020 12:22")
                .build();
        MessagesResponse messagesResponse = new MessagesResponse();
        messagesResponse.setCount(1L);
        messagesResponse.setMessageResponses(messageResponses);
        assertEquals(1L, messagesResponse.getCount());
        assertEquals(1, messagesResponse.getMessageResponses().length);
    }

}
