package de.davelee.trams.controllers;

import de.davelee.trams.TramsGameApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes= TramsGameApplication.class)
public class TipControllerTest {

    @Autowired
    private TipController tipController;

    @Test
    public void testRandomTipMessage() {
        assertNotNull(tipController.getRandomTipMessage());
    }
}
