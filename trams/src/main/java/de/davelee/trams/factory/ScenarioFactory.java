package de.davelee.trams.factory;

import java.util.List;
import java.util.NoSuchElementException;

import de.davelee.trams.beans.Scenario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

@Controller
public class ScenarioFactory {

	@Autowired
	@Resource(name="availableScenariosList")
	private List<Scenario> availableScenarios;
	
	public ScenarioFactory() {
		
	}

	public List<Scenario> getAvailableScenarios() {
		return availableScenarios;
	}

	public void setAvailableScenarios(List<Scenario> availableScenarios) {
		this.availableScenarios = availableScenarios;
	}

	/**
	 * Find the scenario matching the specified name and return the scenario information.
	 * Throws an exception if the scenario is not found.
	 * @param name a <code>String</code> with the scenario to find.
	 * @return a <code>Scenario</code> object containing the scenario information.
	 */
	public Scenario createScenarioByName(final String name) {
		for ( Scenario availableScenario : availableScenarios ) {
			if ( availableScenario.getScenarioName().equalsIgnoreCase(name) ) {
				return (Scenario) availableScenario.clone();
			}
		}
		throw new NoSuchElementException();
	}

}
