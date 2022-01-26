package de.davelee.trams.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the MessageFolder class and ensures that its works correctly.
 * @author Dave Lee
 */
public class MessageFolderTest {

    @Test
    public void testMessageFolder() {
        //Test inbox.
        assertEquals("Inbox", MessageFolder.INBOX.getDisplayName());
        //Test outbox.
        assertEquals("Outbox", MessageFolder.OUTBOX.getDisplayName());
        //Test sent.
        assertEquals("Sent Items", MessageFolder.SENT.getDisplayName());
        //Test trash.
        assertEquals("Trash", MessageFolder.TRASH.getDisplayName());
    }

}
