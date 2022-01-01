package de.davelee.trams.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.davelee.trams.beans.Scenario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ScenarioService {

	private static final Logger logger = LoggerFactory.getLogger(ScenarioService.class);

	/**
     * Get the stop names as a String array plus a - and return it.
     * @return a <code>String</code> array with the stop names plus a -. 
     */
	private String[] getStopNames ( final List<String> stopDistances ) {
		String[] possStops = new String[stopDistances.size() + 1];
        //Add all stops.
		for ( int i = 0; i < stopDistances.size(); i++ ) {
			possStops[i] = stopDistances.get(i).split(":")[0];
		}
        //Return stop names.
        return possStops;
    }

	public Scenario retrieveScenarioFile ( String name ) {
		//Define json importer.
		final ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		try {
			String filePath = "scenarios/" + name.split(" ")[0].toLowerCase() + ".json";
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
