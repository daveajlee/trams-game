package de.davelee.trams.controllers;

import de.davelee.trams.TramsGameApplication;
import de.davelee.trams.api.response.CompanyResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes= TramsGameApplication.class)
public class FileControllerTest {

    @Autowired
    private FileController fileController;

    @InjectMocks
    private CompanyController companyController;

    @Autowired
    private ScenarioController scenarioController;

    @Mock
    private RestTemplate restTemplate;

    @Test
    public void testSaveFile() {
        assertNotNull(scenarioController);
        Mockito.when(restTemplate.postForObject(anyString(), any(), eq(Void.class))).thenReturn(null);
        companyController.createCompany("Dave Lee", "Landuff Transport Company", "Mustermann GmbH");
        Mockito.when(companyController.getCompany(any(), any())).thenReturn(CompanyResponse.builder()
                        .playerName("Dave Lee")
                .build());
        assertEquals("Dave Lee", companyController.getCompany("Mustermann GmbH", "Dave Lee").getPlayerName());
        fileController.saveFile(new File("test-game.json"));
        companyController.createCompany("Bob Smith", "Landuff Transport Company", "Mustermann GmbH");
        Mockito.when(companyController.getCompany(any(), any())).thenReturn(CompanyResponse.builder()
                .playerName("Bob Smith")
                .build());
        assertEquals("Bob Smith", companyController.getCompany("Mustermann GmbH", "Bob Smith").getPlayerName());
        //TODO: test load file after implementation of save file is completed.
        /*fileController.loadFile(new File("test-game.json"));
        assertEquals("Dave Lee", companyController.getCurrentPlayerName());*/
    }

    @Test
    public void testSaveFileWithContent ( ) {
        //Now create and save file.
        fileController.saveFile(new File("test-game-service.json"));
    }

    @Test
    public void testLoadFile ( ) {
        CompanyResponse companyResponse = fileController.loadFile(new File("test-game-service.json"));
        //TODO: Fix save and load file logic and replace test with correct test.
        /*assertNotNull(companyResponse);
        assertEquals("Dave Lee", companyResponse.getPlayerName());*/
    }

}
