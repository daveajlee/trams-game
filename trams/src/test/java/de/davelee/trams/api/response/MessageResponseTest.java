package de.davelee.trams.api.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test cases for the <class>MessageResponse</class> class which are not covered
 * by other tests.
 * @author Dave Lee
 */
public class MessageResponseTest {

    /**
     * Test case: construct an empty <code>MessageResponse</code> object
     * fill it with values through setters and return string of it.
     * Expected Result: valid values and string.
     */
    @Test
    public void testSettersToString() {
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setCompany("Mustermann GmbH");
        messageResponse.setFolder("INBOX");
        messageResponse.setSender("Local Authority");
        messageResponse.setText("This is a test message");
        messageResponse.setSubject("Test");
        messageResponse.setDateTime("28-12-2020 12:22");
        assertEquals("MessageResponse(company=Mustermann GmbH, subject=Test, text=This is a test message, sender=Local Authority, folder=INBOX, dateTime=28-12-2020 12:22)", messageResponse.toString());
    }

}
