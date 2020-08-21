package de.davelee.trams.controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("/de/davelee/trams/spring/test-context.xml")
public class FileControllerTest {

    @Autowired
    private FileController fileController;

    @Autowired
    private GameController gameController;

    @Test
    public void testSaveFile() {
        gameController.createGameModel("Dave Lee", "Landuff Transport Company");
        assertEquals("Dave Lee", gameController.getCurrentPlayerName());
        fileController.saveFile(new File("test-trams-controller.xml"));
        gameController.createGameModel("Bob Smith", "Landuff Transport Company");
        assertEquals("Bob Smith", gameController.getCurrentPlayerName());
        fileController.loadFile(new File("test-trams-controller.xml"));
        assertEquals("Dave Lee", gameController.getCurrentPlayerName());
    }

    private void assertEquals ( final String expected, final String actual ) {
        Assertions.assertEquals(expected, actual);
    }

}
