package de.davelee.trams.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.davelee.trams.beans.Scenario;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

/**
 * This class enables access to the available Scenarios shipped with TraMS.
 * @author Dave Lee
 */
@Controller
@ConfigurationProperties(prefix="scenarios")
@PropertySource("classpath:scenarios.properties")
@Getter
@Setter
public class ScenarioController {

	private List<String> scenarioList;

	private static final Logger logger = LoggerFactory.getLogger(ScenarioController.class);

	/**
	 * Return the available scenario names that ship with TraMS.
	 * @return a <code>List</code> of <code>String</code> containing the scenario names which are currently available.
	 */
	public List<String> getAvailableScenarios ( ) {
		return scenarioList;
	}

	/**
	 * Return the Scenario data including stops, drivers and vehicles which match the supplied Scenario name. Basically
	 * load the scenario with the matching name!
	 * @param scenarioName a <code>String</code> containing the name of the Scenario to return/load.
	 * @return a <code>Scenario</code> which returns the data for the matching Scenario which can be null if no scenario with this name is found.
	 */
	public Scenario getScenario (final String scenarioName ) {
		//Define json importer.
		final ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		try {
			String filePath = "scenarios/" + scenarioName.split(" ")[0].toLowerCase() + ".json";
			BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(filePath)));
			StringBuilder out = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				out.append(line);   // add everything to StringBuilder
			}
			return mapper.readValue(out.toString(), Scenario.class);
		} catch ( Exception exception ) {
			logger.error("exception whilst loading file", exception);
			return null;
		}
	}

}
