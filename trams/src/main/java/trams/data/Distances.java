package trams.data;

import java.util.HashMap;
import java.util.Map;

public class Distances {
	
	private int id;
	private String stopName;
	private Map<String, Integer> distanceTimes;

	public Distances() {
		distanceTimes = new HashMap<String, Integer>();
	}
	
	public Distances(String stopName, Map<String, Integer> distanceTimes) {
		this.stopName = stopName;
		this.distanceTimes = distanceTimes;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStopName() {
		return stopName;
	}

	public void setStopName(String stopName) {
		this.stopName = stopName;
	}

	public Map<String, Integer> getDistanceTimes() {
		return distanceTimes;
	}

	public void setDistanceTimes(Map<String, Integer> distanceTimes) {
		this.distanceTimes = distanceTimes;
	}
	
	public int getStopDistance ( String name ) {
		return distanceTimes.get(name);
	}
	
	public void setStopDistance ( String name, int time ) {
		distanceTimes.put(name, time);
	}
	
}
