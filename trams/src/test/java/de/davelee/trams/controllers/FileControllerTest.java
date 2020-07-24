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
        Assertions.assertEquals(gameController.getCurrentPlayerName(), "Dave Lee");
        fileController.saveFile(new File("test-trams-controller.xml"));
        gameController.createGameModel("Bob Smith", "Landuff Transport Company");
        Assertions.assertEquals(gameController.getCurrentPlayerName(), "Bob Smith");
        fileController.loadFile(new File("test-trams-controller.xml"));
        Assertions.assertEquals(gameController.getCurrentPlayerName(), "Dave Lee");
    }

}
