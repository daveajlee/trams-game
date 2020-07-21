package de.davelee.trams.controllers;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/de/davelee/trams/spring/test-context.xml")
//TODO: remove ignore test comment.
@Ignore
public class FileControllerTest {

    @Autowired
    private FileController fileController;

    @Autowired
    private GameController gameController;

    @Test
    public void testSaveFile() {
        gameController.createGameModel("Dave Lee", "Landuff Transport Company");
        assertEquals(gameController.getCurrentPlayerName(), "Dave Lee");
        fileController.saveFile(new File("test-trams-controller.xml"));
        gameController.createGameModel("Bob Smith", "Landuff Transport Company");
        assertEquals(gameController.getCurrentPlayerName(), "Bob Smith");
        fileController.loadFile(new File("test-trams-controller.xml"));
        assertEquals(gameController.getCurrentPlayerName(), "Dave Lee");
    }

}
