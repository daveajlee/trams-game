package de.davelee.trams.controllers;

import de.davelee.trams.TramsGameApplication;
import de.davelee.trams.model.ScenarioModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes= TramsGameApplication.class)
public class ScenarioControllerTest {

    @Autowired
    private ScenarioController scenarioController;

    @Test
    public void testChooseScenario ( ) {
        assertNotNull(scenarioController);
        assertEquals(3, scenarioController.getScenarioList().size());
        assertEquals("Landuff Town (EASY)", scenarioController.getScenarioList().get(0));
    }

    @Test
    public void testLanduffScenario ( ) {
        ScenarioModel landuffScenarioModel = scenarioController.getScenario("Landuff Town (EASY)");
        assertNotNull(landuffScenarioModel);
        assertEquals("Landuff Transport Company", landuffScenarioModel.getName());
        assertEquals(70, landuffScenarioModel.getMinimumSatisfaction());
    }

}
