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

@Controller
@ConfigurationProperties(prefix="scenarios")
@PropertySource("classpath:scenarios.properties")
@Getter
@Setter
public class ScenarioController {

	private List<String> scenarioList;

	private static final Logger logger = LoggerFactory.getLogger(ScenarioController.class);

	public List<String> getAvailableScenarios ( ) {
		return scenarioList;
	}

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
