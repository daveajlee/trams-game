package trams.scenarios;

import java.text.*;
import java.util.*;

import trams.data.Distances;

/**
 * This class represents the Landuff Scenario in the TraMS program.
 * @author Dave Lee.
 */
public class LanduffScenario {
    
	public static void main ( String[] args ) {
		LanduffScenario ls = new LanduffScenario();
		ls.populateStops();
	}
	
    /**
     * Method to populate the stops list according to the scenario.
     */
    public void populateStops ( ) {
        //Initialise the distancesInt list.
        HashMap<String, Distances> distances = new HashMap<String, Distances>();
        //Now create the distancesInt.
        String[] stopNames;
        int[] distancesInt;
        HashMap<String, Integer> distTable = new HashMap<String, Integer>();
        distances.put("Airport", null);
        distances.put("Cargo Terminal", new Distances("Cargo Terminal", null));
        //Stop 3.
        distances.put("Airport Parking", new Distances("Airport Parking",null));
        //Stop 4.
        distances.put("Cathedral", new Distances("Cathedral", distTable));
        //Stop 5.
        distances.put("Town Centre", new Distances("Town Centre", distTable));
        //Stop 6.
        distances.put("Town Park", new Distances("Town Park", distTable));
        //Stop 7.
        distances.put("Park West", new Distances("Park West", distTable));
        //Stop 8.
        distances.put("Park North", new Distances("Park North", distTable));
        //Stop 9.
        distances.put("Park South", new Distances("Park South", distTable));
        //Stop 10.
        distances.put("Hospital", new Distances("Hospital", distTable));
        //Stop 11.
        distances.put("Bank", new Distances("Bank", distTable));
        //Stop 12.
        distances.put("Stadium", new Distances("Stadium", distTable));
        //Stop 13.
        distances.put("Arena", new Distances("Arena", distTable));
        //Stop 14.
        distances.put("Town House", new Distances("Town House", distTable));
        //Stop 15. 
        distances.put("Town Hall", new Distances("Town Hall", distTable));
        //Stop 16.
        distances.put("Ballroom", new Distances("Ballroom", distTable));
        //Stop 17.
        distances.put("Beach", new Distances("Beach", distTable));
        //Stop 18.
        distances.put("Promenade", new Distances("Promenade", distTable));
        //Stop 19.
        distances.put("Pool", new Distances("Pool", distTable));
        //Stop 20.
        distances.put("Race Trax", new Distances("Race Trax", distTable));
        //Stop 21.
        distances.put("Central Station", new Distances("Central Station", distTable));
        //Stop 22.
        distances.put("West Station", new Distances("West Station", distTable));
        //Stop 23.
        distances.put("West Church", new Distances("West Church", distTable));
        //Stop 24.
        distances.put("Medical Clinic", new Distances("Medical Clinic", distTable));
        //Stop 25.
        distances.put("Sea Village", new Distances("Sea Village", distTable));
        //Stop 26.
        distances.put("North Station", new Distances("North Station", distTable));
        //Stop 27.
        distances.put("East Station", new Distances("East Station", distTable));
        //Stop 28.
        stopNames = new String [] { "Airport","Cargo Terminal","Airport Parking","Cathedral","Town Centre","Town Park","Park West","Park North","Park South","Hospital","Bank","Stadium","Arena","Town House","Town Hall","Ballroom","Beach","Promenade","Pool","Race Trax","Central Station","West Station","West Church","Medical Clinic","Sea Village","North Station","East Station","South Station","Uni Campus","College Campus","Straight Field","Straight Avenue","Straight Street","Straight Circle","Office Avenue","Office Circle","Office Street","Industrial Estate","Greenfield","Bus Station","Post Office","Journals","T Junction","Tourist Info","Mile Inn" };
        distancesInt = new int[] { 7,13,6,12,10,8,11,1,11,21,22,10,16,7,7,19,21,20,15,18,8,14,15,6,18,9,3,11,13,15,9,12,12,17,10,8,6,8,4,14,12,12,12,12,13 };
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        System.out.println("<property name='eastChurchMap'>");
        System.out.println("<map>");
        Iterator<String> distKeys = distTable.keySet().iterator();
        while ( distKeys.hasNext() ) {
        	String key = distKeys.next();
        	System.out.println("<entry key='" + key + "' value='" + distTable.get(key) + "'/>");
        }
        System.out.println("</map>");
        System.out.println("</property>");
        distances.put("East Church", new Distances("East Church", distTable));
        //Stop 29.
        stopNames = new String [] { "Airport","Cargo Terminal","Airport Parking","Cathedral","Town Centre","Town Park","Park West","Park North","Park South","Hospital","Bank","Stadium","Arena","Town House","Town Hall","Ballroom","Beach","Promenade","Pool","Race Trax","Central Station","West Station","West Church","Medical Clinic","Sea Village","North Station","East Station","East Church","Uni Campus","College Campus","Straight Field","Straight Avenue","Straight Street","Straight Circle","Office Avenue","Office Circle","Office Street","Industrial Estate","Greenfield","Bus Station","Post Office","Journals","T Junction","Tourist Info","Mile Inn" };
        distancesInt = new int[] { 12,15,10,10,9,12,5,12,6,9,8,16,17,6,5,14,9,11,14,18,9,14,15,10,8,15,13,11,5,15,3,6,9,7,17,15,13,15,12,15,8,5,5,8,11 };
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("South Station", new Distances("South Station", distTable));
        //Stop 30.
        stopNames = new String [] { "Airport","Cargo Terminal","Airport Parking","Cathedral","Town Centre","Town Park","Park West","Park North","Park South","Hospital","Bank","Stadium","Arena","Town House","Town Hall","Ballroom","Beach","Promenade","Pool","Race Trax","Central Station","West Station","West Church","Medical Clinic","Sea Village","North Station","East Station","East Church","South Station","College Campus","Straight Field","Straight Avenue","Straight Street","Straight Circle","Office Avenue","Office Circle","Office Street","Industrial Estate","Greenfield","Bus Station","Post Office","Journals","T Junction","Tourist Info","Mile Inn" };
        distancesInt = new int[] { 14,15,11,9,8,15,8,15,9,7,6,16,17,5,4,13,8,10,13,16,9,12,13,10,6,15,15,13,5,13,3,3,6,8,17,15,13,15,12,13,4,4,4,4,10 };
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("Uni Campus", new Distances("Uni Campus", distTable));
        //Stop 31.
        stopNames = new String [] { "Airport","Cargo Terminal","Airport Parking","Cathedral","Town Centre","Town Park","Park West","Park North","Park South","Hospital","Bank","Stadium","Arena","Town House","Town Hall","Ballroom","Beach","Promenade","Pool","Race Trax","Central Station","West Station","West Church","Medical Clinic","Sea Village","North Station","East Station","East Church","South Station","Uni Campus","Straight Field","Straight Avenue","Straight Street","Straight Circle","Office Avenue","Office Circle","Office Street","Industrial Estate","Greenfield","Bus Station","Post Office","Journals","T Junction","Tourist Info","Mile Inn" };
        distancesInt = new int[] {17,14,15,6,6,23,11,17,19,12,13,8,3,8,9,4,13,11,8,5,10,2,3,6,8,7,17,15,15,13,11,14,12,11,30,28,26,28,25,8,15,16,16,15,6};
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("College Campus", new Distances("College Campus", distTable));
        //Stop 32.
        stopNames = new String [] { "Airport","Cargo Terminal","Airport Parking","Cathedral","Town Centre","Town Park","Park West","Park North","Park South","Hospital","Bank","Stadium","Arena","Town House","Town Hall","Ballroom","Beach","Promenade","Pool","Race Trax","Central Station","West Station","West Church","Medical Clinic","Sea Village","North Station","East Station","East Church","South Station","Uni Campus","College Campus","Straight Avenue","Straight Street","Straight Circle","Office Avenue","Office Circle","Office Street","Industrial Estate","Greenfield","Bus Station","Post Office","Journals","T Junction","Tourist Info","Mile Inn" };
        distancesInt = new int[] {9,13,7,8,7,14,2,8,8,9,8,11,13,5,3,16,14,13,12,16,7,12,13,10,8,12,11,9,3,3,11,5,7,9,15,13,11,13,10,15,6,7,7,6,8};
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("Straight Field", new Distances("Straight Field", distTable));
        //Stop 33.
        stopNames = new String [] { "Airport","Cargo Terminal","Airport Parking","Cathedral","Town Centre","Town Park","Park West","Park North","Park South","Hospital","Bank","Stadium","Arena","Town House","Town Hall","Ballroom","Beach","Promenade","Pool","Race Trax","Central Station","West Station","West Church","Medical Clinic","Sea Village","North Station","East Station","East Church","South Station","Uni Campus","College Campus","Straight Field","Straight Street","Straight Circle","Office Avenue","Office Circle","Office Street","Industrial Estate","Greenfield","Bus Station","Post Office","Journals","T Junction","Tourist Info","Mile Inn" };
        distancesInt = new int[] {14,16,12,7,8,19,7,13,13,5,6,12,14,7,6,14,12,11,10,14,7,10,11,11,6,13,14,12,6,3,14,5,5,7,34,32,30,32,29,7,3,3,3,3,9};
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("Straight Avenue", new Distances("Straight Avenue", distTable));
        //Stop 34.
        stopNames = new String [] { "Airport","Cargo Terminal","Airport Parking","Cathedral","Town Centre","Town Park","Park West","Park North","Park South","Hospital","Bank","Stadium","Arena","Town House","Town Hall","Ballroom","Beach","Promenade","Pool","Race Trax","Central Station","West Station","West Church","Medical Clinic","Sea Village","North Station","East Station","East Church","South Station","Uni Campus","College Campus","Straight Field","Straight Avenue","Straight Circle","Office Avenue","Office Circle","Office Street","Industrial Estate","Greenfield","Bus Station","Post Office","Journals","T Junction","Tourist Info","Mile Inn" };
        distancesInt = new int[] {13,15,11,2,2,19,7,11,15,8,9,10,12,7,6,12,14,13,12,17,4,7,8,6,5,11,14,12,9,6,12,7,5,9,22,20,18,20,17,3,6,8,8,6,8};
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("Straight Street", new Distances("Straight Street", distTable));
        //Stop 35.
        stopNames = new String [] { "Airport","Cargo Terminal","Airport Parking","Cathedral","Town Centre","Town Park","Park West","Park North","Park South","Hospital","Bank","Stadium","Arena","Town House","Town Hall","Ballroom","Beach","Promenade","Pool","Race Trax","Central Station","West Station","West Church","Medical Clinic","Sea Village","North Station","East Station","East Church","South Station","Uni Campus","College Campus","Straight Field","Straight Avenue","Straight Street","Office Avenue","Office Circle","Office Street","Industrial Estate","Greenfield","Bus Station","Post Office","Journals","T Junction","Tourist Info","Mile Inn" };
        distancesInt = new int[] {23,25,20,10,11,21,11,16,14,2,1,15,17,11,10,16,2,3,7,19,13,13,12,14,4,17,19,17,7,8,11,9,7,9,23,21,19,21,18,9,8,2,2,8,13};
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("Straight Circle", new Distances("Straight Circle", distTable));
        //Stop 36.
        stopNames = new String [] { "Airport","Cargo Terminal","Airport Parking","Cathedral","Town Centre","Town Park","Park West","Park North","Park South","Hospital","Bank","Stadium","Arena","Town House","Town Hall","Ballroom","Beach","Promenade","Pool","Race Trax","Central Station","West Station","West Church","Medical Clinic","Sea Village","North Station","East Station","East Church","South Station","Uni Campus","College Campus","Straight Field","Straight Avenue","Straight Street","Straight Circle","Office Circle","Office Street","Industrial Estate","Greenfield","Bus Station","Post Office","Journals","T Junction","Tourist Info","Mile Inn" };
        distancesInt = new int[] {16,20,14,25,23,12,16,12,17,35,34,23,28,14,15,25,37,35,31,30,18,26,27,19,28,25,8,10,17,17,30,15,34,22,23,2,4,2,8,23,18,20,20,18,15};
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("Office Avenue", new Distances("Office Avenue", distTable));
        //Stop 37.
        stopNames = new String [] { "Airport","Cargo Terminal","Airport Parking","Cathedral","Town Centre","Town Park","Park West","Park North","Park South","Hospital","Bank","Stadium","Arena","Town House","Town Hall","Ballroom","Beach","Promenade","Pool","Race Trax","Central Station","West Station","West Church","Medical Clinic","Sea Village","North Station","East Station","East Church","South Station","Uni Campus","College Campus","Straight Field","Straight Avenue","Straight Street","Straight Circle","Office Avenue","Office Street","Industrial Estate","Greenfield","Bus Station","Post Office","Journals","T Junction","Tourist Info","Mile Inn" };
        distancesInt = new int[] {14,18,12,23,21,10,14,10,15,33,32,21,26,12,13,23,35,33,29,28,16,24,25,17,26,23,6,8,15,15,28,13,32,20,21,2,2,2,6,21,16,18,18,16,13};
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("Office Circle", new Distances("Office Circle", distTable));
        //Stop 38.
        stopNames = new String [] { "Airport","Cargo Terminal","Airport Parking","Cathedral","Town Centre","Town Park","Park West","Park North","Park South","Hospital","Bank","Stadium","Arena","Town House","Town Hall","Ballroom","Beach","Promenade","Pool","Race Trax","Central Station","West Station","West Church","Medical Clinic","Sea Village","North Station","East Station","East Church","South Station","Uni Campus","College Campus","Straight Field","Straight Avenue","Straight Street","Straight Circle","Office Avenue","Office Circle","Industrial Estate","Greenfield","Bus Station","Post Office","Journals","T Junction","Tourist Info","Mile Inn" };
        distancesInt = new int[] {12,16,10,21,19,8,12,8,13,31,30,19,24,10,11,21,33,31,27,26,14,22,23,15,24,21,4,6,13,13,26,11,30,18,19,4,2,2,4,19,14,16,16,14,11};
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("Office Street", new Distances("Office Street", distTable));
        //Stop 39.
        stopNames = new String [] { "Airport","Cargo Terminal","Airport Parking","Cathedral","Town Centre","Town Park","Park West","Park North","Park South","Hospital","Bank","Stadium","Arena","Town House","Town Hall","Ballroom","Beach","Promenade","Pool","Race Trax","Central Station","West Station","West Church","Medical Clinic","Sea Village","North Station","East Station","East Church","South Station","Uni Campus","College Campus","Straight Field","Straight Avenue","Straight Street","Straight Circle","Office Avenue","Office Circle","Office Street","Greenfield","Bus Station","Post Office","Journals","T Junction","Tourist Info","Mile Inn" };
        distancesInt = new int[] {14,18,12,23,21,10,14,10,15,33,32,21,26,12,13,23,35,33,29,28,16,24,25,17,26,23,6,8,15,15,28,13,32,20,21,2,2,2,4,21,16,18,18,16,13};
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("Industrial Estate", new Distances("Industrial Estate", distTable));
        //Stop 40.
        stopNames = new String [] { "Airport","Cargo Terminal","Airport Parking","Cathedral","Town Centre","Town Park","Park West","Park North","Park South","Hospital","Bank","Stadium","Arena","Town House","Town Hall","Ballroom","Beach","Promenade","Pool","Race Trax","Central Station","West Station","West Church","Medical Clinic","Sea Village","North Station","East Station","East Church","South Station","Uni Campus","College Campus","Straight Field","Straight Avenue","Straight Street","Straight Circle","Office Avenue","Office Circle","Office Street","Industrial Estate","Bus Station","Post Office","Journals","T Junction","Tourist Info","Mile Inn" };
        distancesInt = new int[] {11,15,9,20,18,9,11,7,14,30,29,18,23,9,10,20,32,30,26,25,13,21,22,14,23,20,4,4,12,12,25,10,29,17,18,8,6,4,4,18,13,15,15,13,10};
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("Greenfield", new Distances("Greenfield", distTable));
        //Stop 41.
        stopNames = new String [] { "Airport","Cargo Terminal","Airport Parking","Cathedral","Town Centre","Town Park","Park West","Park North","Park South","Hospital","Bank","Stadium","Arena","Town House","Town Hall","Ballroom","Beach","Promenade","Pool","Race Trax","Central Station","West Station","West Church","Medical Clinic","Sea Village","North Station","East Station","East Church","South Station","Uni Campus","College Campus","Straight Field","Straight Avenue","Straight Street","Straight Circle","Office Avenue","Office Circle","Office Street","Industrial Estate","Greenfield","Post Office","Journals","T Junction","Tourist Info","Mile Inn" };
        distancesInt = new int[] {18,20,16,3,4,23,15,19,19,6,7,17,12,8,9,6,8,7,5,11,8,5,6,10,2,14,17,14,15,13,8,15,7,3,9,23,21,19,21,18,7,8,8,7,7};
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("Bus Station", new Distances("Bus Station", distTable));
        //Stop 42.
        stopNames = new String [] { "Airport","Cargo Terminal","Airport Parking","Cathedral","Town Centre","Town Park","Park West","Park North","Park South","Hospital","Bank","Stadium","Arena","Town House","Town Hall","Ballroom","Beach","Promenade","Pool","Race Trax","Central Station","West Station","West Church","Medical Clinic","Sea Village","North Station","East Station","East Church","South Station","Uni Campus","College Campus","Straight Field","Straight Avenue","Straight Street","Straight Circle","Office Avenue","Office Circle","Office Street","Industrial Estate","Greenfield","Bus Station","Journals","T Junction","Tourist Info","Mile Inn" };
        distancesInt = new int[] {13,15,11,4,5,18,7,13,13,5,6,13,8,2,3,11,9,10,12,15,5,8,9,8,6,13,15,12,8,4,15,6,3,6,8,18,16,14,16,13,7,5,5,3,6};
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("Post Office", new Distances("Post Office", distTable));
        //Stop 43.
        stopNames = new String [] { "Airport","Cargo Terminal","Airport Parking","Cathedral","Town Centre","Town Park","Park West","Park North","Park South","Hospital","Bank","Stadium","Arena","Town House","Town Hall","Ballroom","Beach","Promenade","Pool","Race Trax","Central Station","West Station","West Church","Medical Clinic","Sea Village","North Station","East Station","East Church","South Station","Uni Campus","College Campus","Straight Field","Straight Avenue","Straight Street","Straight Circle","Office Avenue","Office Circle","Office Street","Industrial Estate","Greenfield","Bus Station","Post Office","T Junction","Tourist Info","Mile Inn" };
        distancesInt = new int[] {18,21,16,10,11,16,9,12,12,5,3,15,13,7,8,16,5,6,11,19,8,12,13,11,7,14,15,12,5,4,16,7,3,8,2,20,18,16,18,15,8,5,1,7,8};
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("Journals", new Distances("Journals", distTable));
        //Stop 44.
        stopNames = new String [] { "Airport","Cargo Terminal","Airport Parking","Cathedral","Town Centre","Town Park","Park West","Park North","Park South","Hospital","Bank","Stadium","Arena","Town House","Town Hall","Ballroom","Beach","Promenade","Pool","Race Trax","Central Station","West Station","West Church","Medical Clinic","Sea Village","North Station","East Station","East Church","South Station","Uni Campus","College Campus","Straight Field","Straight Avenue","Straight Street","Straight Circle","Office Avenue","Office Circle","Office Street","Industrial Estate","Greenfield","Bus Station","Post Office","Journals","Tourist Info","Mile Inn" };
        distancesInt = new int[] {17,21,16,10,11,16,9,12,12,5,3,15,13,7,8,16,5,6,11,19,8,12,13,11,7,14,15,12,5,4,16,7,3,8,2,20,18,16,18,15,8,5,1,6,7};
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("T Junction", new Distances("T Junction", distTable));
        //Stop 45.
        stopNames = new String [] { "Airport","Cargo Terminal","Airport Parking","Cathedral","Town Centre","Town Park","Park West","Park North","Park South","Hospital","Bank","Stadium","Arena","Town House","Town Hall","Ballroom","Beach","Promenade","Pool","Race Trax","Central Station","West Station","West Church","Medical Clinic","Sea Village","North Station","East Station","East Church","South Station","Uni Campus","College Campus","Straight Field","Straight Avenue","Straight Street","Straight Circle","Office Avenue","Office Circle","Office Street","Industrial Estate","Greenfield","Bus Station","Post Office","Journals","T Junction","Mile Inn" };
        distancesInt = new int[] {10,13,8,4,5,18,7,13,13,5,6,13,8,2,3,11,9,10,12,15,5,8,9,8,6,13,15,12,8,4,15,6,3,6,8,18,16,14,16,13,7,3,7,6,4};
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("Tourist Info", new Distances("Tourist Info", distTable));
        //Stop 46.
        stopNames = new String [] { "Airport","Cargo Terminal","Airport Parking","Cathedral","Town Centre","Town Park","Park West","Park North","Park South","Hospital","Bank","Stadium","Arena","Town House","Town Hall","Ballroom","Beach","Promenade","Pool","Race Trax","Central Station","West Station","West Church","Medical Clinic","Sea Village","North Station","East Station","East Church","South Station","Uni Campus","College Campus","Straight Field","Straight Avenue","Straight Street","Straight Circle","Office Avenue","Office Circle","Office Street","Industrial Estate","Greenfield","Bus Station","Post Office","Journals","T Junction","Tourist Info" };
        distancesInt = new int[] {11,8,7,7,6,17,11,14,19,18,19,3,8,6,7,10,20,19,15,12,3,8,9,1,11,3,15,13,11,10,6,8,9,8,13,15,13,11,13,10,7,6,8,7,4};
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("Mile Inn", new Distances("Mile Inn", distTable));
    }
    
}
