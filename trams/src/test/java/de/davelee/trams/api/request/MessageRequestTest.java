package de.davelee.trams.api.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the MessageRequest class and ensures that its works correctly.
 * @author Dave Lee
 */
public class MessageRequestTest {

    /**
     * Ensure that a MessageRequest class can be correctly instantiated.
     */
    @Test
    public void testCreateRequest() {
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setCompany("Lee Buses");
        messageRequest.setFolder("INBOX");
        messageRequest.setDateTime("27-01-2020 21:50");
        messageRequest.setSender("Council");
        messageRequest.setSubject("Test Message");
        messageRequest.setText("This is a test message");
        assertEquals("Lee Buses", messageRequest.getCompany());
        assertEquals("INBOX", messageRequest.getFolder());
        assertEquals("27-01-2020 21:50", messageRequest.getDateTime());
        assertEquals("Council", messageRequest.getSender());
        assertEquals("Test Message", messageRequest.getSubject());
        assertEquals("This is a test message", messageRequest.getText());
        assertEquals("MessageRequest(company=Lee Buses, subject=Test Message, text=This is a test message, sender=Council, folder=INBOX, dateTime=27-01-2020 21:50)", messageRequest.toString());
    }

}
