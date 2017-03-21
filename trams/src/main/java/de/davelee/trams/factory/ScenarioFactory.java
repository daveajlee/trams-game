package de.davelee.trams.factory;

import java.util.List;

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
	
	public Scenario createScenarioByName(String name) {
		for ( Scenario availableScenario : availableScenarios ) {
			if ( availableScenario.getScenarioName().equalsIgnoreCase(name) ) {
				return (Scenario) availableScenario.clone();
			}
		}
		//TODO: throw exception
		return null;
	}

}
