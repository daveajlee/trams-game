package de.davelee.trams.controllers;

import de.davelee.trams.TramsGameApplication;
import de.davelee.trams.api.response.CompanyResponse;
import de.davelee.trams.services.GameService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes= TramsGameApplication.class)
public class FileControllerTest {

    @Autowired
    private FileController fileController;

    @InjectMocks
    private GameController gameController;

    @Mock
    private GameService gameService;

    @Autowired
    private ScenarioController scenarioController;

    @Test
    public void testSaveFile() {
        assertNotNull(scenarioController);
        Mockito.doNothing().when(gameService).saveGame(any());
        gameController.createGameModel("Dave Lee", "Landuff Transport Company", "Mustermann GmbH");
        Mockito.when(gameController.getGameModel(any(), any())).thenReturn(CompanyResponse.builder()
                        .playerName("Dave Lee")
                .build());
        assertEquals("Dave Lee", gameController.getGameModel("Mustermann GmbH", "Dave Lee").getPlayerName());
        fileController.saveFile(new File("test-game.json"));
        gameController.createGameModel("Bob Smith", "Landuff Transport Company", "Mustermann GmbH");
        Mockito.when(gameController.getGameModel(any(), any())).thenReturn(CompanyResponse.builder()
                .playerName("Bob Smith")
                .build());
        assertEquals("Bob Smith", gameController.getGameModel("Mustermann GmbH", "Bob Smith").getPlayerName());
        //TODO: test load file after implementation of save file is completed.
        /*fileController.loadFile(new File("test-game.json"));
        assertEquals("Dave Lee", gameController.getCurrentPlayerName());*/
    }

    private void assertEquals ( final String expected, final String actual ) {
        Assertions.assertEquals(expected, actual);
    }

}
