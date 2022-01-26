package de.davelee.trams.controllers;

import de.davelee.trams.TramsGameApplication;
import de.davelee.trams.api.response.MessageResponse;
import de.davelee.trams.api.response.MessagesResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes= TramsGameApplication.class)
public class MessageControllerTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private MessageController messageController;

    @Test
    public void testGetMessagesbyFolderSenderDate() {
        Mockito.when(restTemplate.getForObject(anyString(), eq(MessagesResponse.class))).thenReturn(
                MessagesResponse.builder()
                        .messageResponses(new MessageResponse[0])
                        .count(0L)
                        .build()
        );
        assertNotNull(messageController.getMessagesByFolderDateSender("Mustermann GmbH", "INBOX", "26-01-2020", "Council"));
        assertNotNull(messageController.getMessagesByFolderDateSender("Mustermann GmbH", "INBOX", "All Dates", "Council"));
        Mockito.when(restTemplate.getForObject(anyString(), eq(MessagesResponse.class))).thenReturn(null);
        assertNull(messageController.getMessagesByFolderDateSender("Mustermann GmbH", "INBOX", "26-01-2020", "Council"));
        assertNull(messageController.getMessagesByFolderDateSender("Mustermann GmbH", "INBOX", "All Dates", "Council"));
    }

    @Test
    public void testAddMessage() {
        Mockito.when(restTemplate.postForObject(anyString(), any(), eq(Void.class))).thenReturn(null);
        messageController.addMessage("Mustermann GmbH", "Test Message", "This is a test message", "Council", "INBOX", "26-01-2020");
    }

    @Test
    public void testGetAllMessages() {
        Mockito.when(restTemplate.getForObject(anyString(), eq(MessagesResponse.class))).thenReturn(
                MessagesResponse.builder()
                        .messageResponses(new MessageResponse[0])
                        .count(0L)
                        .build()
        );
        assertNotNull(messageController.getAllMessages("Mustermann GmbH"));
        Mockito.when(restTemplate.getForObject(anyString(), eq(MessagesResponse.class))).thenReturn(null);
        assertNull(messageController.getAllMessages("Mustermann GmbH"));
    }

    @Test
    public void testLoadMessages() {
        Mockito.doNothing().when(restTemplate).delete(anyString());
        Mockito.when(restTemplate.postForObject(anyString(), any(), eq(Void.class))).thenReturn(null);
        messageController.loadMessages(new MessageResponse[] {
                MessageResponse.builder()
                        .company("Mustermann GmbH")
                        .folder("INBOX")
                        .sender("Council")
                        .subject("Test Message")
                        .text("This is a test message")
                        .dateTime("01-01-2020 02:00").build()
        }, "Mustermann GmbH");
    }

}
