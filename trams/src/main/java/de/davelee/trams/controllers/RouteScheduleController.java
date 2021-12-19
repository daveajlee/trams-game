package de.davelee.trams.controllers;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class RouteScheduleController {

	 @Autowired
	 private JourneyController journeyController;

	 @Autowired
	 private VehicleController vehicleController;

	 private List<Integer> routeDetailPos = new ArrayList<Integer>();

	/**
	 * Get the current minimum schedule.
	 * @return a <code>int</code> with the id of the first schedule.
	 */
	public int getCurrentMinSchedule ( ) {
		return routeDetailPos.get(0);
	}

	/**
	 * Get the current maximum schedule.
	 * @return a <code>int</code> with the id of the second schedule.
	 */
	public int getCurrentMaxSchedule ( ) {
		return routeDetailPos.get(routeDetailPos.size()-1);
	}

	/**
	 * Get list of allocations for the specified company.
	 * @param company a <code>String</code> with the name of the company.
	 * @return a <code>LinkedList</code> of allocations.
	 */
	public List<String> getAllocations ( final String company ) {
		return vehicleController.getAllocations(company);
	}
	 
}
