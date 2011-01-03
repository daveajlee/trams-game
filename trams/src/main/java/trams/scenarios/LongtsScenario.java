package trams.scenarios;

import java.text.*;
import java.util.*;

import trams.data.Distances;

/**
 * This class represents the Longts Scenario in the TraMS program.
 * @author Dave Lee.
 */
public class LongtsScenario extends trams.data.Scenario {

    private String theScenarioName;
    
    /**
     * Create a new LongtsScenario - for load function.
     * @param playerName a <code>String</code> with the name of the player.
     * @param balance a <code>double</code> with the available balance.
     * @param psgSatisfaction a <code>int</code> with the passenger satisfaction.
     */
    public LongtsScenario ( String playerName, double balance, int psgSatisfaction ) {
        super(playerName, balance, psgSatisfaction);
        theScenarioName = "Longts Transport Company";
    }
    
    /**
     * Method to populate the stops list according to the scenario.
     */
    public void populateStops ( ) {
        //Initialise the stop list.
        stops = new LinkedList<trams.data.Stop>();
        //Now create the stops.
        stops.add(new trams.data.Stop("City Centre"));
        stops.add(new trams.data.Stop("Centre Square"));
        stops.add(new trams.data.Stop("Airport Junction"));
        stops.add(new trams.data.Stop("Airport Road"));
        stops.add(new trams.data.Stop("Airport"));
        stops.add(new trams.data.Stop("Courier Tower"));
        stops.add(new trams.data.Stop("Diamond Junction"));
        stops.add(new trams.data.Stop("City Centre West"));
        stops.add(new trams.data.Stop("West Road"));
        stops.add(new trams.data.Stop("Crescent Avenue"));
        stops.add(new trams.data.Stop("South Street"));
        stops.add(new trams.data.Stop("South Street (Ring)"));
        stops.add(new trams.data.Stop("Ring Road (Airport)"));
        stops.add(new trams.data.Stop("Crescent Junction"));
        stops.add(new trams.data.Stop("Crescent Avenue East"));
        stops.add(new trams.data.Stop("East Avenue"));
        stops.add(new trams.data.Stop("Ring Road (South)"));
        stops.add(new trams.data.Stop("Crescent South"));
        stops.add(new trams.data.Stop("South Road"));
        stops.add(new trams.data.Stop("Ring Link"));
        stops.add(new trams.data.Stop("Ring Road"));
        stops.add(new trams.data.Stop("Stadium"));
        stops.add(new trams.data.Stop("X Junction"));
        stops.add(new trams.data.Stop("Post Office"));
        stops.add(new trams.data.Stop("East Road"));
        stops.add(new trams.data.Stop("Crescent Road"));
        stops.add(new trams.data.Stop("South Square"));
        stops.add(new trams.data.Stop("Ring Road (East)"));
        stops.add(new trams.data.Stop("Arrow Junction"));
        stops.add(new trams.data.Stop("Lake Junction"));
        stops.add(new trams.data.Stop("East Way"));
        stops.add(new trams.data.Stop("East Junction"));
        stops.add(new trams.data.Stop("East Avenue"));
        stops.add(new trams.data.Stop("Park North"));
        stops.add(new trams.data.Stop("Ring Park"));
        stops.add(new trams.data.Stop("Park Road"));
        stops.add(new trams.data.Stop("North Street"));
        stops.add(new trams.data.Stop("East Street"));
        stops.add(new trams.data.Stop("North Road"));
        stops.add(new trams.data.Stop("Expressway"));
        stops.add(new trams.data.Stop("Express West"));
        stops.add(new trams.data.Stop("North Cross"));
        stops.add(new trams.data.Stop("North Inn"));
        stops.add(new trams.data.Stop("North Link"));
        stops.add(new trams.data.Stop("West Link"));
        stops.add(new trams.data.Stop("West Street"));
        stops.add(new trams.data.Stop("Cinema"));
        stops.add(new trams.data.Stop("Bus Station"));
        stops.add(new trams.data.Stop("South Avenue"));
        stops.add(new trams.data.Stop("Park South"));
        //Initialise the distancesInt list.
        distances = new HashMap<String, Distances>();
        //Now create the distancesInt.
        String[] stopNames = new String [] { "Centre Square","Airport Junction","Airport Road","Airport","Courier Tower","Diamond Junction","City Centre West","West Road","Crescent Avenue","South Street","South Street (Ring)","Ring Road (Airport)","Crescent Junction","Crescent Avenue East","East Avenue","Ring Road (South)","Crescent South","South Road","Ring Link","Ring Road","Stadium","X Junction","Post Office","East Road","Crescent Road","South Square","Ring Road (East)","Arrow Junction","Lake Junction","East Way","East Junction","East Avenue","Park North","Ring Park","Park Road","North Street","East Street","North Road","Expressway","Express West","North Cross","North Inn","North Link","West Link","West Street","Cinema","Bus Station","South Avenue","Park South" };
        int[] distancesInt = new int[] { 7,3,14,12,12,10,7,18,23,24,10,15,9,11,21,25,22,18,21,9,17,18,11,19,13,10,7,12,14,17,9,14,13,23,16,14,12,14,11,18,13,18,17,10,11,2,4,5,7 };
        HashMap<String, Integer> distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("City Centre", new Distances("City Centre", distTable));
        //Stop 2.
        stopNames = new String [] { "City Centre","Airport Junction","Airport Road","Airport","Courier Tower","Diamond Junction","City Centre West","West Road","Crescent Avenue","South Street","South Street (Ring)","Ring Road (Airport)","Crescent Junction","Crescent Avenue East","East Avenue","Ring Road (South)","Crescent South","South Road","Ring Link","Ring Road","Stadium","X Junction","Post Office","East Road","Crescent Road","South Square","Ring Road (East)","Arrow Junction","Lake Junction","East Way","East Junction","East Avenue","Park North","Ring Park","Park Road","North Street","East Street","North Road","Expressway","Express West","North Cross","North Inn","North Link","West Link","West Street","Cinema","Bus Station","South Avenue","Park South" };
        distancesInt = new int[] { 7,4,11,9,15,8,9,16,24,23,4,10,13,15,16,26,25,19,18,13,18,19,8,6,8,16,13,15,15,14,13,16,15,25,20,18,16,18,15,20,15,21,21,13,8,4,6,7,9 };
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("Centre Square", new Distances("Centre Square", distTable));
        //Stop 3.
        stopNames = new String [] { "City Centre","Centre Square","Airport Road","Airport","Courier Tower","Diamond Junction","City Centre West","West Road","Crescent Avenue","South Street","South Street (Ring)","Ring Road (Airport)","Crescent Junction","Crescent Avenue East","East Avenue","Ring Road (South)","Crescent South","South Road","Ring Link","Ring Road","Stadium","X Junction","Post Office","East Road","Crescent Road","South Square","Ring Road (East)","Arrow Junction","Lake Junction","East Way","East Junction","East Avenue","Park North","Ring Park","Park Road","North Street","East Street","North Road","Expressway","Express West","North Cross","North Inn","North Link","West Link","West Street","Cinema","Bus Station","South Avenue","Park South" };
        distancesInt = new int[] { 3,4,9,7,10,6,5,14,21,20,7,13,6,8,19,23,22,16,14,7,15,16,8,21,11,9,6,10,11,15,7,12,11,20,14,12,10,12,9,16,11,16,16,8,7,6,8,9,11 };
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("Airport Junction", new Distances("Airport Junction", distTable));
        //Stop 4.
        stopNames = new String [] { "City Centre","Centre Square","Airport Junction","Airport","Courier Tower","Diamond Junction","City Centre West","West Road","Crescent Avenue","South Street","South Street (Ring)","Ring Road (Airport)","Crescent Junction","Crescent Avenue East","East Avenue","Ring Road (South)","Crescent South","South Road","Ring Link","Ring Road","Stadium","X Junction","Post Office","East Road","Crescent Road","South Square","Ring Road (East)","Arrow Junction","Lake Junction","East Way","East Junction","East Avenue","Park North","Ring Park","Park Road","North Street","East Street","North Road","Expressway","Express West","North Cross","North Inn","North Link","West Link","West Street","Cinema","Bus Station","South Avenue","Park South" };
        distancesInt = new int[] { 14,11,9,2,14,10,8,13,9,8,9,10,6,8,9,12,11,7,12,7,6,7,6,7,10,15,12,10,9,6,8,7,2,10,25,23,21,23,20,3,4,10,10,4,7,8,10,11,13 };
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("Airport Road", new Distances("Airport Road", distTable));
        //Stop 5.
        stopNames = new String [] { "City Centre","Centre Square","Airport Junction","Airport Road","Courier Tower","Diamond Junction","City Centre West","West Road","Crescent Avenue","South Street","South Street (Ring)","Ring Road (Airport)","Crescent Junction","Crescent Avenue East","East Avenue","Ring Road (South)","Crescent South","South Road","Ring Link","Ring Road","Stadium","X Junction","Post Office","East Road","Crescent Road","South Square","Ring Road (East)","Arrow Junction","Lake Junction","East Way","East Junction","East Avenue","Park North","Ring Park","Park Road","North Street","East Street","North Road","Expressway","Express West","North Cross","North Inn","North Link","West Link","West Street","Cinema","Bus Station","South Avenue","Park South" };
        distancesInt = new int[] { 12,9,7,2,13,9,7,12,8,7,8,9,4,6,10,14,13,8,15,5,7,9,5,8,9,13,10,8,8,6,7,8,2,11,23,21,19,21,18,4,5,11,11,5,6,10,12,13,15 };
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("Airport", new Distances("Airport", distTable));
        //Stop 6.
        stopNames = new String [] { "City Centre","Centre Square","Airport Junction","Airport Road","Airport","Diamond Junction","City Centre West","West Road","Crescent Avenue","South Street","South Street (Ring)","Ring Road (Airport)","Crescent Junction","Crescent Avenue East","East Avenue","Ring Road (South)","Crescent South","South Road","Ring Link","Ring Road","Stadium","X Junction","Post Office","East Road","Crescent Road","South Square","Ring Road (East)","Arrow Junction","Lake Junction","East Way","East Junction","East Avenue","Park North","Ring Park","Park Road","North Street","East Street","North Road","Expressway","Express West","North Cross","North Inn","North Link","West Link","West Street","Cinema","Bus Station","South Avenue","Park South" };
        distancesInt = new int[] { 12,15,10,14,13,8,4,4,24,23,15,23,13,12,25,30,30,21,27,13,27,28,19,20,23,6,8,12,15,23,14,19,19,21,12,10,8,10,9,23,18,16,16,18,17,12,14,15,14 };
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("Courier Tower", new Distances("Courier Tower", distTable));
        //Stop 7.
        stopNames = new String [] { "City Centre","Centre Square","Airport Junction","Airport Road","Airport","Courier Tower","City Centre West","West Road","Crescent Avenue","South Street","South Street (Ring)","Ring Road (Airport)","Crescent Junction","Crescent Avenue East","East Avenue","Ring Road (South)","Crescent South","South Road","Ring Link","Ring Road","Stadium","X Junction","Post Office","East Road","Crescent Road","South Square","Ring Road (East)","Arrow Junction","Lake Junction","East Way","East Junction","East Avenue","Park North","Ring Park","Park Road","North Street","East Street","North Road","Expressway","Express West","North Cross","North Inn","North Link","West Link","West Street","Cinema","Bus Station","South Avenue","Park South" };
        distancesInt = new int[] { 10,8,6,10,9,8,5,4,12,11,9,17,4,3,17,22,22,13,19,9,18,19,11,12,16,14,11,5,8,11,2,7,7,11,16,14,12,14,11,15,7,9,9,7,11,14,16,17,16 };
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("Diamond Junction", new Distances("Diamond Junction", distTable));
        //Stop 8.
        stopNames = new String [] { "City Centre","Centre Square","Airport Junction","Airport Road","Airport","Courier Tower","Diamond Junction","West Road","Crescent Avenue","South Street","South Street (Ring)","Ring Road (Airport)","Crescent Junction","Crescent Avenue East","East Avenue","Ring Road (South)","Crescent South","South Road","Ring Link","Ring Road","Stadium","X Junction","Post Office","East Road","Crescent Road","South Square","Ring Road (East)","Arrow Junction","Lake Junction","East Way","East Junction","East Avenue","Park North","Ring Park","Park Road","North Street","East Street","North Road","Expressway","Express West","North Cross","North Inn","North Link","West Link","West Street","Cinema","Bus Station","South Avenue","Park South" };
        distancesInt = new int[] { 7,9,5,8,7,4,5,8,15,14,12,20,17,16,21,26,25,17,21,7,22,23,15,16,17,4,1,12,15,17,8,13,11,16,12,10,8,10,7,19,13,12,12,13,14,10,12,13,12 };
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("City Centre West", new Distances("City Centre West", distTable));
        //Stop 9.
        stopNames = new String [] { "City Centre","Centre Square","Airport Junction","Airport Road","Airport","Courier Tower","Diamond Junction","City Centre West","Crescent Avenue","South Street","South Street (Ring)","Ring Road (Airport)","Crescent Junction","Crescent Avenue East","East Avenue","Ring Road (South)","Crescent South","South Road","Ring Link","Ring Road","Stadium","X Junction","Post Office","East Road","Crescent Road","South Square","Ring Road (East)","Arrow Junction","Lake Junction","East Way","East Junction","East Avenue","Park North","Ring Park","Park Road","North Street","East Street","North Road","Expressway","Express West","North Cross","North Inn","North Link","West Link","West Street","Cinema","Bus Station","South Avenue","Park South" };
        distancesInt = new int[] { 18,16,14,13,12,4,4,8,16,15,18,26,17,16,21,26,26,17,23,11,22,23,15,16,21,10,11,6,9,19,8,13,15,14,17,15,13,15,14,19,13,12,12,13,19,11,13,14,10 };
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("West Road", new Distances("West Road", distTable));
        //Stop 10.
        stopNames = new String [] { "City Centre","Centre Square","Airport Junction","Airport Road","Airport","Courier Tower","Diamond Junction","City Centre West","West Road","South Street","South Street (Ring)","Ring Road (Airport)","Crescent Junction","Crescent Avenue East","East Avenue","Ring Road (South)","Crescent South","South Road","Ring Link","Ring Road","Stadium","X Junction","Post Office","East Road","Crescent Road","South Square","Ring Road (East)","Arrow Junction","Lake Junction","East Way","East Junction","East Avenue","Park North","Ring Park","Park Road","North Street","East Street","North Road","Expressway","Express West","North Cross","North Inn","North Link","West Link","West Street","Cinema","Bus Station","South Avenue","Park South" };
        distancesInt = new int[] { 23,24,21,9,8,24,12,15,16,2,25,30,15,14,22,3,2,8,24,15,7,6,18,5,18,24,21,9,7,12,9,5,8,2,35,33,31,33,30,6,5,5,5,5,18,11,13,14,10 };
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("Crescent Avenue", new Distances("Crescent Avenue", distTable));
        //Stop 11.
        stopNames = new String [] { "City Centre","Centre Square","Airport Junction","Airport Road","Airport","Courier Tower","Diamond Junction","City Centre West","West Road","Crescent Avenue","South Street (Ring)","Ring Road (Airport)","Crescent Junction","Crescent Avenue East","East Avenue","Ring Road (South)","Crescent South","South Road","Ring Link","Ring Road","Stadium","X Junction","Post Office","East Road","Crescent Road","South Square","Ring Road (East)","Arrow Junction","Lake Junction","East Way","East Junction","East Avenue","Park North","Ring Park","Park Road","North Street","East Street","North Road","Expressway","Express West","North Cross","North Inn","North Link","West Link","West Street","Cinema","Bus Station","South Avenue","Park South" };
        distancesInt = new int[] { 24,23,20,8,7,23,11,14,15,2,28,32,16,15,24,4,4,9,26,15,8,7,19,6,19,25,22,8,6,13,8,6,9,1,34,32,30,32,29,7,6,3,3,6,19,12,14,15,11 };
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("South Street", new Distances("South Street", distTable));
        //Stop 12.
        stopNames = new String [] { "City Centre","Centre Square","Airport Junction","Airport Road","Airport","Courier Tower","Diamond Junction","City Centre West","West Road","Crescent Avenue","South Street","Ring Road (Airport)","Crescent Junction","Crescent Avenue East","East Avenue","Ring Road (South)","Crescent South","South Road","Ring Link","Ring Road","Stadium","X Junction","Post Office","East Road","Crescent Road","South Square","Ring Road (East)","Arrow Junction","Lake Junction","East Way","East Junction","East Avenue","Park North","Ring Park","Park Road","North Street","East Street","North Road","Expressway","Express West","North Cross","North Inn","North Link","West Link","West Street","Cinema","Bus Station","South Avenue","Park South" };
        distancesInt = new int[] { 10,4,7,9,8,15,9,12,18,25,28,8,9,10,16,21,20,15,6,7,15,16,4,15,4,13,10,16,16,8,11,12,10,15,23,21,19,21,18,17,13,15,15,13,3,13,15,16,13 };
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("South Street (Ring)", new Distances("South Street (Ring)", distTable));
        //Stop 13.
        stopNames = new String [] { "City Centre","Centre Square","Airport Junction","Airport Road","Airport","Courier Tower","Diamond Junction","City Centre West","West Road","Crescent Avenue","South Street","South Street (Ring)","Crescent Junction","Crescent Avenue East","East Avenue","Ring Road (South)","Crescent South","South Road","Ring Link","Ring Road","Stadium","X Junction","Post Office","East Road","Crescent Road","South Square","Ring Road (East)","Arrow Junction","Lake Junction","East Way","East Junction","East Avenue","Park North","Ring Park","Park Road","North Street","East Street","North Road","Expressway","Express West","North Cross","North Inn","North Link","West Link","West Street","Cinema","Bus Station","South Avenue","Park South" };
        distancesInt = new int[] { 15,10,13,10,9,23,17,20,26,30,32,8,11,13,9,19,18,6,14,9,7,8,6,14,4,19,16,17,17,3,13,14,12,17,28,26,24,26,23,12,8,13,13,8,8,15,17,18,16 };
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("Ring Road (Airport)", new Distances("Ring Road (Airport)", distTable));
        //Stop 14.
        stopNames = new String [] { "City Centre","Centre Square","Airport Junction","Airport Road","Airport","Courier Tower","Diamond Junction","City Centre West","West Road","Crescent Avenue","South Street","South Street (Ring)","Ring Road (Airport)","Crescent Avenue East","East Avenue","Ring Road (South)","Crescent South","South Road","Ring Link","Ring Road","Stadium","X Junction","Post Office","East Road","Crescent Road","South Square","Ring Road (East)","Arrow Junction","Lake Junction","East Way","East Junction","East Avenue","Park North","Ring Park","Park Road","North Street","East Street","North Road","Expressway","Express West","North Cross","North Inn","North Link","West Link","West Street","Cinema","Bus Station","South Avenue","Park South" };
        distancesInt = new int[] { 9,13,6,6,4,13,4,17,17,15,16,8,11,2,14,19,18,10,16,1,12,13,7,10,9,10,7,6,5,8,5,7,7,11,14,12,10,12,9,8,2,7,7,2,6,16,18,19,14 };
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("Crescent Junction", new Distances("Crescent Junction", distTable));
        //Stop 15.
        stopNames = new String [] { "City Centre","Centre Square","Airport Junction","Airport Road","Airport","Courier Tower","Diamond Junction","City Centre West","West Road","Crescent Avenue","South Street","South Street (Ring)","Ring Road (Airport)","Crescent Junction","East Avenue","Ring Road (South)","Crescent South","South Road","Ring Link","Ring Road","Stadium","X Junction","Post Office","East Road","Crescent Road","South Square","Ring Road (East)","Arrow Junction","Lake Junction","East Way","East Junction","East Avenue","Park North","Ring Park","Park Road","North Street","East Street","North Road","Expressway","Express West","North Cross","North Inn","North Link","West Link","West Street","Cinema","Bus Station","South Avenue","Park South" };
        distancesInt = new int[] { 11,15,8,8,6,12,3,16,16,14,15,10,13,2,16,17,16,11,18,3,13,14,8,10,10,10,7,5,4,9,3,6,6,10,15,13,11,13,10,9,3,8,8,3,7,14,16,17,16 };
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("Crescent Avenue East", new Distances("Crescent Avenue East", distTable));
        //Stop 16.
        stopNames = new String [] { "City Centre","Centre Square","Airport Junction","Airport Road","Airport","Courier Tower","Diamond Junction","City Centre West","West Road","Crescent Avenue","South Street","South Street (Ring)","Ring Road (Airport)","Crescent Junction","Crescent Avenue East","Ring Road (South)","Crescent South","South Road","Ring Link","Ring Road","Stadium","X Junction","Post Office","East Road","Crescent Road","South Square","Ring Road (East)","Arrow Junction","Lake Junction","East Way","East Junction","East Avenue","Park North","Ring Park","Park Road","North Street","East Street","North Road","Expressway","Express West","North Cross","North Inn","North Link","West Link","West Street","Cinema","Bus Station","South Avenue","Park South" };
        distancesInt = new int[] { 21,16,19,9,10,25,17,21,21,22,24,16,9,14,16,10,8,4,3,13,3,2,7,8,12,22,19,14,13,4,16,14,12,16,25,23,21,23,20,6,11,16,16,11,10,18,20,21,17 };
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("East Avenue", new Distances("East Avenue", distTable));
        //Stop 17.
        stopNames = new String [] { "City Centre","Centre Square","Airport Junction","Airport Road","Airport","Courier Tower","Diamond Junction","City Centre West","West Road","Crescent Avenue","South Street","South Street (Ring)","Ring Road (Airport)","Crescent Junction","Crescent Avenue East","East Avenue","Crescent South","South Road","Ring Link","Ring Road","Stadium","X Junction","Post Office","East Road","Crescent Road","South Square","Ring Road (East)","Arrow Junction","Lake Junction","East Way","East Junction","East Avenue","Park North","Ring Park","Park Road","North Street","East Street","North Road","Expressway","Express West","North Cross","North Inn","North Link","West Link","West Street","Cinema","Bus Station","South Avenue","Park South" };
        distancesInt = new int[] { 25,26,23,12,14,30,22,26,26,3,4,21,19,19,17,10,1,9,13,16,8,7,20,6,18,24,21,9,8,13,14,12,14,2,37,35,33,35,32,8,9,5,5,9,20,16,18,19,18 };
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("Ring Road (South)", new Distances("Ring Road (South)", distTable));
        //Stop 18.
        stopNames = new String [] { "City Centre","Centre Square","Airport Junction","Airport Road","Airport","Courier Tower","Diamond Junction","City Centre West","West Road","Crescent Avenue","South Street","South Street (Ring)","Ring Road (Airport)","Crescent Junction","Crescent Avenue East","East Avenue","Ring Road (South)","South Road","Ring Link","Ring Road","Stadium","X Junction","Post Office","East Road","Crescent Road","South Square","Ring Road (East)","Arrow Junction","Lake Junction","East Way","East Junction","East Avenue","Park North","Ring Park","Park Road","North Street","East Street","North Road","Expressway","Express West","North Cross","North Inn","North Link","West Link","West Street","Cinema","Bus Station","South Avenue","Park South" };
        distancesInt = new int[] { 22,25,22,11,13,30,22,25,26,2,4,20,18,18,16,8,1,7,11,15,7,6,19,4,16,23,20,11,10,11,13,11,13,3,35,33,31,33,30,7,10,6,6,10,19,14,16,17,16 };
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("Crescent South", new Distances("Crescent South", distTable));
        //Stop 19.
        stopNames = new String [] { "City Centre","Centre Square","Airport Junction","Airport Road","Airport","Courier Tower","Diamond Junction","City Centre West","West Road","Crescent Avenue","South Street","South Street (Ring)","Ring Road (Airport)","Crescent Junction","Crescent Avenue East","East Avenue","Ring Road (South)","Crescent South","Ring Link","Ring Road","Stadium","X Junction","Post Office","East Road","Crescent Road","South Square","Ring Road (East)","Arrow Junction","Lake Junction","East Way","East Junction","East Avenue","Park North","Ring Park","Park Road","North Street","East Street","North Road","Expressway","Express West","North Cross","North Inn","North Link","West Link","West Street","Cinema","Bus Station","South Avenue","Park South" };
        distancesInt = new int[] { 18,19,16,7,8,21,13,17,17,8,9,15,6,10,11,4,9,7,6,12,4,3,9,4,11,18,15,14,13,8,12,10,12,7,31,29,27,29,26,5,12,11,11,12,15,11,13,14,12 };
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("South Road", new Distances("South Road", distTable));
        //Stop 20.
        stopNames = new String [] { "City Centre","Centre Square","Airport Junction","Airport Road","Airport","Courier Tower","Diamond Junction","City Centre West","West Road","Crescent Avenue","South Street","South Street (Ring)","Ring Road (Airport)","Crescent Junction","Crescent Avenue East","East Avenue","Ring Road (South)","Crescent South","South Road","Ring Road","Stadium","X Junction","Post Office","East Road","Crescent Road","South Square","Ring Road (East)","Arrow Junction","Lake Junction","East Way","East Junction","East Avenue","Park North","Ring Park","Park Road","North Street","East Street","North Road","Expressway","Express West","North Cross","North Inn","North Link","West Link","West Street","Cinema","Bus Station","South Avenue","Park South" };
        distancesInt = new int[] { 21,18,14,12,15,27,19,21,23,24,26,6,14,16,18,3,13,11,6,16,6,5,10,10,9,21,18,18,16,5,16,14,17,19,30,28,26,28,25,11,15,19,19,15,12,9,11,12,13 };
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("Ring Link", new Distances("Ring Link", distTable));
        //Stop 21.
        stopNames = new String [] { "City Centre","Centre Square","Airport Junction","Airport Road","Airport","Courier Tower","Diamond Junction","City Centre West","West Road","Crescent Avenue","South Street","South Street (Ring)","Ring Road (Airport)","Crescent Junction","Crescent Avenue East","East Avenue","Ring Road (South)","Crescent South","South Road","Ring Link","Stadium","X Junction","Post Office","East Road","Crescent Road","South Square","Ring Road (East)","Arrow Junction","Lake Junction","East Way","East Junction","East Avenue","Park North","Ring Park","Park Road","North Street","East Street","North Road","Expressway","Express West","North Cross","North Inn","North Link","West Link","West Street","Cinema","Bus Station","South Avenue","Park South" };
        distancesInt = new int[] { 9,13,7,7,5,13,9,7,11,15,15,7,9,1,3,13,16,15,12,16,12,13,5,9,8,11,8,9,9,10,7,7,4,13,18,16,14,16,13,8,5,8,8,5,3,11,13,14,15 };
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("Ring Road", new Distances("Ring Road", distTable));
        //Stop 22.
        stopNames = new String [] { "City Centre","Centre Square","Airport Junction","Airport Road","Airport","Courier Tower","Diamond Junction","City Centre West","West Road","Crescent Avenue","South Street","South Street (Ring)","Ring Road (Airport)","Crescent Junction","Crescent Avenue East","East Avenue","Ring Road (South)","Crescent South","South Road","Ring Link","Ring Road","X Junction","Post Office","East Road","Crescent Road","South Square","Ring Road (East)","Arrow Junction","Lake Junction","East Way","East Junction","East Avenue","Park North","Ring Park","Park Road","North Street","East Street","North Road","Expressway","Express West","North Cross","North Inn","North Link","West Link","West Street","Cinema","Bus Station","South Avenue","Park South" };
        distancesInt = new int[] { 17,18,15,6,8,27,18,22,22,7,8,15,7,12,13,3,8,7,4,6,12,1,7,5,9,17,14,14,12,2,12,10,7,13,26,24,22,24,21,5,8,12,12,8,8,13,15,16,17 };
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("Stadium", new Distances("Stadium", distTable));
        //Stop 23.
        stopNames = new String [] { "City Centre","Centre Square","Airport Junction","Airport Road","Airport","Courier Tower","Diamond Junction","City Centre West","West Road","Crescent Avenue","South Street","South Street (Ring)","Ring Road (Airport)","Crescent Junction","Crescent Avenue East","East Avenue","Ring Road (South)","Crescent South","South Road","Ring Link","Ring Road","Stadium","Post Office","East Road","Crescent Road","South Square","Ring Road (East)","Arrow Junction","Lake Junction","East Way","East Junction","East Avenue","Park North","Ring Park","Park Road","North Street","East Street","North Road","Expressway","Express West","North Cross","North Inn","North Link","West Link","West Street","Cinema","Bus Station","South Avenue","Park South" };
        distancesInt = new int[] { 18,19,16,7,9,28,19,23,23,6,7,16,8,13,14,2,7,6,3,5,13,1,8,4,10,18,15,15,13,3,13,11,8,12,27,25,23,25,22,6,9,13,13,9,9,7,9,10,11 };
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("X Junction", new Distances("X Junction", distTable));
        //Stop 24.
        stopNames = new String [] { "City Centre","Centre Square","Airport Junction","Airport Road","Airport","Courier Tower","Diamond Junction","City Centre West","West Road","Crescent Avenue","South Street","South Street (Ring)","Ring Road (Airport)","Crescent Junction","Crescent Avenue East","East Avenue","Ring Road (South)","Crescent South","South Road","Ring Link","Ring Road","Stadium","X Junction","East Road","Crescent Road","South Square","Ring Road (East)","Arrow Junction","Lake Junction","East Way","East Junction","East Avenue","Park North","Ring Park","Park Road","North Street","East Street","North Road","Expressway","Express West","North Cross","North Inn","North Link","West Link","West Street","Cinema","Bus Station","South Avenue","Park South" };
        distancesInt = new int[] { 11,8,8,6,5,19,11,15,15,18,19,4,6,7,8,7,20,19,9,10,5,7,8,9,3,9,6,10,10,6,10,11,6,14,19,17,15,17,14,10,8,11,11,8,1,5,7,8,9 };
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("Post Office", new Distances("Post Office", distTable));
        //Stop 25.
        stopNames = new String [] { "City Centre","Centre Square","Airport Junction","Airport Road","Airport","Courier Tower","Diamond Junction","City Centre West","West Road","Crescent Avenue","South Street","South Street (Ring)","Ring Road (Airport)","Crescent Junction","Crescent Avenue East","East Avenue","Ring Road (South)","Crescent South","South Road","Ring Link","Ring Road","Stadium","X Junction","Post Office","Crescent Road","South Square","Ring Road (East)","Arrow Junction","Lake Junction","East Way","East Junction","East Avenue","Park North","Ring Park","Park Road","North Street","East Street","North Road","Expressway","Express West","North Cross","North Inn","North Link","West Link","West Street","Cinema","Bus Station","South Avenue","Park South" };
        distancesInt = new int[] { 19,16,21,7,8,20,12,16,16,5,6,15,14,10,10,8,6,4,4,10,9,5,4,9,15,21,18,8,6,8,8,6,5,4,28,26,24,26,23,2,6,7,7,6,11,6,8,9,10 };
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("East Road", new Distances("East Road", distTable));
        //Stop 26.
        stopNames = new String [] { "City Centre","Centre Square","Airport Junction","Airport Road","Airport","Courier Tower","Diamond Junction","City Centre West","West Road","Crescent Avenue","South Street","South Street (Ring)","Ring Road (Airport)","Crescent Junction","Crescent Avenue East","East Avenue","Ring Road (South)","Crescent South","South Road","Ring Link","Ring Road","Stadium","X Junction","Post Office","East Road","South Square","Ring Road (East)","Arrow Junction","Lake Junction","East Way","East Junction","East Avenue","Park North","Ring Park","Park Road","North Street","East Street","North Road","Expressway","Express West","North Cross","North Inn","North Link","West Link","West Street","Cinema","Bus Station","South Avenue","Park South" };
        distancesInt = new int[] { 13,8,11,10,9,23,16,17,21,18,19,4,4,9,10,12,18,16,11,9,8,9,10,3,15,12,9,15,15,7,12,13,11,17,25,23,21,23,20,14,13,14,14,13,3,5,7,8,10 };
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("Crescent Road", new Distances("Crescent Road", distTable));
        //Stop 27.
        stopNames = new String [] { "City Centre","Centre Square","Airport Junction","Airport Road","Airport","Courier Tower","Diamond Junction","City Centre West","West Road","Crescent Avenue","South Street","South Street (Ring)","Ring Road (Airport)","Crescent Junction","Crescent Avenue East","East Avenue","Ring Road (South)","Crescent South","South Road","Ring Link","Ring Road","Stadium","X Junction","Post Office","East Road","Crescent Road","Ring Road (East)","Arrow Junction","Lake Junction","East Way","East Junction","East Avenue","Park North","Ring Park","Park Road","North Street","East Street","North Road","Expressway","Express West","North Cross","North Inn","North Link","West Link","West Street","Cinema","Bus Station","South Avenue","Park South" };
        distancesInt = new int[] { 10,16,9,15,13,6,14,4,10,24,25,13,19,10,10,22,24,23,18,21,11,17,18,9,21,12,3,13,15,17,11,14,14,19,8,6,4,6,4,17,15,15,15,15,15,7,9,10,11 };
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("South Square", new Distances("South Square", distTable));
        //Stop 28.
        stopNames = new String [] { "City Centre","Centre Square","Airport Junction","Airport Road","Airport","Courier Tower","Diamond Junction","City Centre West","West Road","Crescent Avenue","South Street","South Street (Ring)","Ring Road (Airport)","Crescent Junction","Crescent Avenue East","East Avenue","Ring Road (South)","Crescent South","South Road","Ring Link","Ring Road","Stadium","X Junction","Post Office","East Road","Crescent Road","South Square","Arrow Junction","Lake Junction","East Way","East Junction","East Avenue","Park North","Ring Park","Park Road","North Street","East Street","North Road","Expressway","Express West","North Cross","North Inn","North Link","West Link","West Street","Cinema","Bus Station","South Avenue","Park South" };
        distancesInt = new int[] { 7,13,6,12,10,8,11,1,11,21,22,10,16,7,7,19,21,20,15,18,8,14,15,6,18,9,3,11,13,15,9,12,12,17,10,8,6,8,4,14,12,12,12,12,13,9,11,12,13 };
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("Ring Road (East)", new Distances("Ring Road (East)", distTable));
        //Stop 29.
        stopNames = new String [] { "City Centre","Centre Square","Airport Junction","Airport Road","Airport","Courier Tower","Diamond Junction","City Centre West","West Road","Crescent Avenue","South Street","South Street (Ring)","Ring Road (Airport)","Crescent Junction","Crescent Avenue East","East Avenue","Ring Road (South)","Crescent South","South Road","Ring Link","Ring Road","Stadium","X Junction","Post Office","East Road","Crescent Road","South Square","Ring Road (East)","Lake Junction","East Way","East Junction","East Avenue","Park North","Ring Park","Park Road","North Street","East Street","North Road","Expressway","Express West","North Cross","North Inn","North Link","West Link","West Street","Cinema","Bus Station","South Avenue","Park South" };
        distancesInt = new int[] { 12,15,10,10,9,12,5,12,6,9,8,16,17,6,5,14,9,11,14,18,9,14,15,10,8,15,13,11,5,15,3,6,9,7,17,15,13,15,12,15,8,5,5,8,11,8,10,11,14 };
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("Arrow Junction", new Distances("Arrow Junction", distTable));
        //Stop 30.
        stopNames = new String [] { "City Centre","Centre Square","Airport Junction","Airport Road","Airport","Courier Tower","Diamond Junction","City Centre West","West Road","Crescent Avenue","South Street","South Street (Ring)","Ring Road (Airport)","Crescent Junction","Crescent Avenue East","East Avenue","Ring Road (South)","Crescent South","South Road","Ring Link","Ring Road","Stadium","X Junction","Post Office","East Road","Crescent Road","South Square","Ring Road (East)","Arrow Junction","East Way","East Junction","East Avenue","Park North","Ring Park","Park Road","North Street","East Street","North Road","Expressway","Express West","North Cross","North Inn","North Link","West Link","West Street","Cinema","Bus Station","South Avenue","Park South" };
        distancesInt = new int[] { 14,15,11,9,8,15,8,15,9,7,6,16,17,5,4,13,8,10,13,16,9,12,13,10,6,15,15,13,5,13,3,3,6,8,17,15,13,15,12,13,4,4,4,4,10,7,9,10,13 };
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("Lake Junction", new Distances("Lake Junction", distTable));
        //Stop 31.
        stopNames = new String [] { "City Centre","Centre Square","Airport Junction","Airport Road","Airport","Courier Tower","Diamond Junction","City Centre West","West Road","Crescent Avenue","South Street","South Street (Ring)","Ring Road (Airport)","Crescent Junction","Crescent Avenue East","East Avenue","Ring Road (South)","Crescent South","South Road","Ring Link","Ring Road","Stadium","X Junction","Post Office","East Road","Crescent Road","South Square","Ring Road (East)","Arrow Junction","Lake Junction","East Junction","East Avenue","Park North","Ring Park","Park Road","North Street","East Street","North Road","Expressway","Express West","North Cross","North Inn","North Link","West Link","West Street","Cinema","Bus Station","South Avenue","Park South" };
        distancesInt = new int[] { 17,14,15,6,6,23,11,17,19,12,13,8,3,8,9,4,13,11,8,5,10,2,3,6,8,7,17,15,15,13,11,14,12,11,30,28,26,28,25,8,15,16,16,15,6,4,6,7,9 };
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("East Way", new Distances("East Way", distTable));
        //Stop 32.
        stopNames = new String [] { "City Centre","Centre Square","Airport Junction","Airport Road","Airport","Courier Tower","Diamond Junction","City Centre West","West Road","Crescent Avenue","South Street","South Street (Ring)","Ring Road (Airport)","Crescent Junction","Crescent Avenue East","East Avenue","Ring Road (South)","Crescent South","South Road","Ring Link","Ring Road","Stadium","X Junction","Post Office","East Road","Crescent Road","South Square","Ring Road (East)","Arrow Junction","Lake Junction","East Way","East Avenue","Park North","Ring Park","Park Road","North Street","East Street","North Road","Expressway","Express West","North Cross","North Inn","North Link","West Link","West Street","Cinema","Bus Station","South Avenue","Park South" };
        distancesInt = new int[] { 9,13,7,8,7,14,2,8,8,9,8,11,13,5,3,16,14,13,12,16,7,12,13,10,8,12,11,9,3,3,11,5,7,9,15,13,11,13,10,15,6,7,7,6,8,12,14,15,17 };
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("East Junction", new Distances("East Junction", distTable));
        //Stop 33.
        stopNames = new String [] { "City Centre","Centre Square","Airport Junction","Airport Road","Airport","Courier Tower","Diamond Junction","City Centre West","West Road","Crescent Avenue","South Street","South Street (Ring)","Ring Road (Airport)","Crescent Junction","Crescent Avenue East","East Avenue","Ring Road (South)","Crescent South","South Road","Ring Link","Ring Road","Stadium","X Junction","Post Office","East Road","Crescent Road","South Square","Ring Road (East)","Arrow Junction","Lake Junction","East Way","East Junction","Park North","Ring Park","Park Road","North Street","East Street","North Road","Expressway","Express West","North Cross","North Inn","North Link","West Link","West Street","Cinema","Bus Station","South Avenue","Park South" };
        distancesInt = new int[] { 14,16,12,7,8,19,7,13,13,5,6,12,14,7,6,14,12,11,10,14,7,10,11,11,6,13,14,12,6,3,14,5,5,7,34,32,30,32,29,7,3,3,3,3,9,14,16,17,18 };
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("East Avenue", new Distances("East Avenue", distTable));
        //Stop 34.
        stopNames = new String [] { "City Centre","Centre Square","Airport Junction","Airport Road","Airport","Courier Tower","Diamond Junction","City Centre West","West Road","Crescent Avenue","South Street","South Street (Ring)","Ring Road (Airport)","Crescent Junction","Crescent Avenue East","East Avenue","Ring Road (South)","Crescent South","South Road","Ring Link","Ring Road","Stadium","X Junction","Post Office","East Road","Crescent Road","South Square","Ring Road (East)","Arrow Junction","Lake Junction","East Way","East Junction","East Avenue","Ring Park","Park Road","North Street","East Street","North Road","Expressway","Express West","North Cross","North Inn","North Link","West Link","West Street","Cinema","Bus Station","South Avenue","Park South" };
        distancesInt = new int[] { 13,15,11,2,2,19,7,11,15,8,9,10,12,7,6,12,14,13,12,17,4,7,8,6,5,11,14,12,9,6,12,7,5,9,22,20,18,20,17,3,6,8,8,6,8,18,20,21,23 };
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("Park North", new Distances("Park North", distTable));
        //Stop 35.
        stopNames = new String [] { "City Centre","Centre Square","Airport Junction","Airport Road","Airport","Courier Tower","Diamond Junction","City Centre West","West Road","Crescent Avenue","South Street","South Street (Ring)","Ring Road (Airport)","Crescent Junction","Crescent Avenue East","East Avenue","Ring Road (South)","Crescent South","South Road","Ring Link","Ring Road","Stadium","X Junction","Post Office","East Road","Crescent Road","South Square","Ring Road (East)","Arrow Junction","Lake Junction","East Way","East Junction","East Avenue","Park North","Park Road","North Street","East Street","North Road","Expressway","Express West","North Cross","North Inn","North Link","West Link","West Street","Cinema","Bus Station","South Avenue","Park South" };
        distancesInt = new int[] { 23,25,20,10,11,21,11,16,14,2,1,15,17,11,10,16,2,3,7,19,13,13,12,14,4,17,19,17,7,8,11,9,7,9,23,21,19,21,18,9,8,2,2,8,13,12,14,15,17 };
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("Ring Park", new Distances("Ring Park", distTable));
        //Stop 36.
        stopNames = new String [] { "City Centre","Centre Square","Airport Junction","Airport Road","Airport","Courier Tower","Diamond Junction","City Centre West","West Road","Crescent Avenue","South Street","South Street (Ring)","Ring Road (Airport)","Crescent Junction","Crescent Avenue East","East Avenue","Ring Road (South)","Crescent South","South Road","Ring Link","Ring Road","Stadium","X Junction","Post Office","East Road","Crescent Road","South Square","Ring Road (East)","Arrow Junction","Lake Junction","East Way","East Junction","East Avenue","Park North","Ring Park","North Street","East Street","North Road","Expressway","Express West","North Cross","North Inn","North Link","West Link","West Street","Cinema","Bus Station","South Avenue","Park South" };
        distancesInt = new int[] { 16,20,14,25,23,12,16,12,17,35,34,23,28,14,15,25,37,35,31,30,18,26,27,19,28,25,8,10,17,17,30,15,34,22,23,2,4,2,8,23,18,20,20,18,15,14,16,17,19 };
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("Park Road", new Distances("Park Road", distTable));
        //Stop 37.
        stopNames = new String [] { "City Centre","Centre Square","Airport Junction","Airport Road","Airport","Courier Tower","Diamond Junction","City Centre West","West Road","Crescent Avenue","South Street","South Street (Ring)","Ring Road (Airport)","Crescent Junction","Crescent Avenue East","East Avenue","Ring Road (South)","Crescent South","South Road","Ring Link","Ring Road","Stadium","X Junction","Post Office","East Road","Crescent Road","South Square","Ring Road (East)","Arrow Junction","Lake Junction","East Way","East Junction","East Avenue","Park North","Ring Park","Park Road","East Street","North Road","Expressway","Express West","North Cross","North Inn","North Link","West Link","West Street","Cinema","Bus Station","South Avenue","Park South" };
        distancesInt = new int[] { 14,18,12,23,21,10,14,10,15,33,32,21,26,12,13,23,35,33,29,28,16,24,25,17,26,23,6,8,15,15,28,13,32,20,21,2,2,2,6,21,16,18,18,16,13,16,18,19,21 };
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("North Street", new Distances("North Street", distTable));
        //Stop 38.
        stopNames = new String [] { "City Centre","Centre Square","Airport Junction","Airport Road","Airport","Courier Tower","Diamond Junction","City Centre West","West Road","Crescent Avenue","South Street","South Street (Ring)","Ring Road (Airport)","Crescent Junction","Crescent Avenue East","East Avenue","Ring Road (South)","Crescent South","South Road","Ring Link","Ring Road","Stadium","X Junction","Post Office","East Road","Crescent Road","South Square","Ring Road (East)","Arrow Junction","Lake Junction","East Way","East Junction","East Avenue","Park North","Ring Park","Park Road","North Street","North Road","Expressway","Express West","North Cross","North Inn","North Link","West Link","West Street","Cinema","Bus Station","South Avenue","Park South" };
        distancesInt = new int[] { 12,16,10,21,19,8,12,8,13,31,30,19,24,10,11,21,33,31,27,26,14,22,23,15,24,21,4,6,13,13,26,11,30,18,19,4,2,2,4,19,14,16,16,14,11,18,20,21,23 };
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("East Street", new Distances("East Street", distTable));
        //Stop 39.
        stopNames = new String [] { "City Centre","Centre Square","Airport Junction","Airport Road","Airport","Courier Tower","Diamond Junction","City Centre West","West Road","Crescent Avenue","South Street","South Street (Ring)","Ring Road (Airport)","Crescent Junction","Crescent Avenue East","East Avenue","Ring Road (South)","Crescent South","South Road","Ring Link","Ring Road","Stadium","X Junction","Post Office","East Road","Crescent Road","South Square","Ring Road (East)","Arrow Junction","Lake Junction","East Way","East Junction","East Avenue","Park North","Ring Park","Park Road","North Street","East Street","Expressway","Express West","North Cross","North Inn","North Link","West Link","West Street","Cinema","Bus Station","South Avenue","Park South" };
        distancesInt = new int[] { 14,18,12,23,21,10,14,10,15,33,32,21,26,12,13,23,35,33,29,28,16,24,25,17,26,23,6,8,15,15,28,13,32,20,21,2,2,2,4,21,16,18,18,16,13,18,20,21,20 };
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("North Road", new Distances("North Road", distTable));
        //Stop 40.
        stopNames = new String [] { "City Centre","Centre Square","Airport Junction","Airport Road","Airport","Courier Tower","Diamond Junction","City Centre West","West Road","Crescent Avenue","South Street","South Street (Ring)","Ring Road (Airport)","Crescent Junction","Crescent Avenue East","East Avenue","Ring Road (South)","Crescent South","South Road","Ring Link","Ring Road","Stadium","X Junction","Post Office","East Road","Crescent Road","South Square","Ring Road (East)","Arrow Junction","Lake Junction","East Way","East Junction","East Avenue","Park North","Ring Park","Park Road","North Street","East Street","North Road","Express West","North Cross","North Inn","North Link","West Link","West Street","Cinema","Bus Station","South Avenue","Park South" };
        distancesInt = new int[] { 11,15,9,20,18,9,11,7,14,30,29,18,23,9,10,20,32,30,26,25,13,21,22,14,23,20,4,4,12,12,25,10,29,17,18,8,6,4,4,18,13,15,15,13,10,14,16,17,19 };
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("Expressway", new Distances("Expressway", distTable));
        //Stop 41.
        stopNames = new String [] { "City Centre","Centre Square","Airport Junction","Airport Road","Airport","Courier Tower","Diamond Junction","City Centre West","West Road","Crescent Avenue","South Street","South Street (Ring)","Ring Road (Airport)","Crescent Junction","Crescent Avenue East","East Avenue","Ring Road (South)","Crescent South","South Road","Ring Link","Ring Road","Stadium","X Junction","Post Office","East Road","Crescent Road","South Square","Ring Road (East)","Arrow Junction","Lake Junction","East Way","East Junction","East Avenue","Park North","Ring Park","Park Road","North Street","East Street","North Road","Expressway","North Cross","North Inn","North Link","West Link","West Street","Cinema","Bus Station","South Avenue","Park South" };
        distancesInt = new int[] { 18,20,16,3,4,23,15,19,19,6,7,17,12,8,9,6,8,7,5,11,8,5,6,10,2,14,17,14,15,13,8,15,7,3,9,23,21,19,21,18,7,8,8,7,7,3,5,6,8 };
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("Express West", new Distances("Express West", distTable));
        //Stop 42.
        stopNames = new String [] { "City Centre","Centre Square","Airport Junction","Airport Road","Airport","Courier Tower","Diamond Junction","City Centre West","West Road","Crescent Avenue","South Street","South Street (Ring)","Ring Road (Airport)","Crescent Junction","Crescent Avenue East","East Avenue","Ring Road (South)","Crescent South","South Road","Ring Link","Ring Road","Stadium","X Junction","Post Office","East Road","Crescent Road","South Square","Ring Road (East)","Arrow Junction","Lake Junction","East Way","East Junction","East Avenue","Park North","Ring Park","Park Road","North Street","East Street","North Road","Expressway","Express West","North Inn","North Link","West Link","West Street","Cinema","Bus Station","South Avenue","Park South" };
        distancesInt = new int[] { 13,15,11,4,5,18,7,13,13,5,6,13,8,2,3,11,9,10,12,15,5,8,9,8,6,13,15,12,8,4,15,6,3,6,8,18,16,14,16,13,7,5,5,3,6,5,7,8,9 };
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("North Cross", new Distances("North Cross", distTable));
        //Stop 43.
        stopNames = new String [] { "City Centre","Centre Square","Airport Junction","Airport Road","Airport","Courier Tower","Diamond Junction","City Centre West","West Road","Crescent Avenue","South Street","South Street (Ring)","Ring Road (Airport)","Crescent Junction","Crescent Avenue East","East Avenue","Ring Road (South)","Crescent South","South Road","Ring Link","Ring Road","Stadium","X Junction","Post Office","East Road","Crescent Road","South Square","Ring Road (East)","Arrow Junction","Lake Junction","East Way","East Junction","East Avenue","Park North","Ring Park","Park Road","North Street","East Street","North Road","Expressway","Express West","North Cross","North Link","West Link","West Street","Cinema","Bus Station","South Avenue","Park South" };
        distancesInt = new int[] { 18,21,16,10,11,16,9,12,12,5,3,15,13,7,8,16,5,6,11,19,8,12,13,11,7,14,15,12,5,4,16,7,3,8,2,20,18,16,18,15,8,5,6,7,8,3,5,6,7 };
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("North Inn", new Distances("North Inn", distTable));
        //Stop 44.
        stopNames = new String [] { "City Centre","Centre Square","Airport Junction","Airport Road","Airport","Courier Tower","Diamond Junction","City Centre West","West Road","Crescent Avenue","South Street","South Street (Ring)","Ring Road (Airport)","Crescent Junction","Crescent Avenue East","East Avenue","Ring Road (South)","Crescent South","South Road","Ring Link","Ring Road","Stadium","X Junction","Post Office","East Road","Crescent Road","South Square","Ring Road (East)","Arrow Junction","Lake Junction","East Way","East Junction","East Avenue","Park North","Ring Park","Park Road","North Street","East Street","North Road","Expressway","Express West","North Cross","North Inn","West Link","West Street","Cinema","Bus Station","South Avenue","Park South" };
        distancesInt = new int[] { 17,21,16,10,11,16,9,12,12,5,3,15,13,7,8,16,5,6,11,19,8,12,13,11,7,14,15,12,5,4,16,7,3,8,2,20,18,16,18,15,8,5,6,6,7,7,6,7,8 };
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("North Link", new Distances("North Link", distTable));
        //Stop 45.
        stopNames = new String [] { "City Centre","Centre Square","Airport Junction","Airport Road","Airport","Courier Tower","Diamond Junction","City Centre West","West Road","Crescent Avenue","South Street","South Street (Ring)","Ring Road (Airport)","Crescent Junction","Crescent Avenue East","East Avenue","Ring Road (South)","Crescent South","South Road","Ring Link","Ring Road","Stadium","X Junction","Post Office","East Road","Crescent Road","South Square","Ring Road (East)","Arrow Junction","Lake Junction","East Way","East Junction","East Avenue","Park North","Ring Park","Park Road","North Street","East Street","North Road","Expressway","Express West","North Cross","North Inn","North Link","West Street","Cinema","Bus Station","South Avenue","Park South" };
        distancesInt = new int[] { 10,13,8,4,5,18,7,13,13,5,6,13,8,2,3,11,9,10,12,15,5,8,9,8,6,13,15,12,8,4,15,6,3,6,8,18,16,14,16,13,7,3,7,6,4,15,14,15,17 };
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("West Link", new Distances("West Link", distTable));
        //Stop 46.
        stopNames = new String [] { "City Centre","Centre Square","Airport Junction","Airport Road","Airport","Courier Tower","Diamond Junction","City Centre West","West Road","Crescent Avenue","South Street","South Street (Ring)","Ring Road (Airport)","Crescent Junction","Crescent Avenue East","East Avenue","Ring Road (South)","Crescent South","South Road","Ring Link","Ring Road","Stadium","X Junction","Post Office","East Road","Crescent Road","South Square","Ring Road (East)","Arrow Junction","Lake Junction","East Way","East Junction","East Avenue","Park North","Ring Park","Park Road","North Street","East Street","North Road","Expressway","Express West","North Cross","North Inn","North Link","West Link","Cinema","Bus Station","South Avenue","Park South" };
        distancesInt = new int[] { 11,8,7,7,6,17,11,14,19,18,19,3,8,6,7,10,20,19,15,12,3,8,9,1,11,3,15,13,11,10,6,8,9,8,13,15,13,11,13,10,7,6,8,7,4,6,5,6,8 };
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("West Street", new Distances("West Street", distTable));
        //Stop 47.
        stopNames = new String [] { "City Centre","Centre Square","Airport Junction","Airport Road","Airport","Courier Tower","Diamond Junction","City Centre West","West Road","Crescent Avenue","South Street","South Street (Ring)","Ring Road (Airport)","Crescent Junction","Crescent Avenue East","East Avenue","Ring Road (South)","Crescent South","South Road","Ring Link","Ring Road","Stadium","X Junction","Post Office","East Road","Crescent Road","South Square","Ring Road (East)","Arrow Junction","Lake Junction","East Way","East Junction","East Avenue","Park North","Ring Park","Park Road","North Street","East Street","North Road","Expressway","Express West","North Cross","North Inn","North Link","West Link","West Street","Bus Station","South Avenue","Park South" };
        distancesInt = new int[] { 2,4,6,8,10,12,14,10,11,11,12,13,15,16,14,18,16,14,11,9,11,13,7,5,6,5,7,9,8,7,4,12,14,18,12,14,16,18,18,14,3,5,3,7,15,6,4,6,15 };
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("Cinema", new Distances("Cinema", distTable));
        //Stop 48.
        stopNames = new String [] { "City Centre","Centre Square","Airport Junction","Airport Road","Airport","Courier Tower","Diamond Junction","City Centre West","West Road","Crescent Avenue","South Street","South Street (Ring)","Ring Road (Airport)","Crescent Junction","Crescent Avenue East","East Avenue","Ring Road (South)","Crescent South","South Road","Ring Link","Ring Road","Stadium","X Junction","Post Office","East Road","Crescent Road","South Square","Ring Road (East)","Arrow Junction","Lake Junction","East Way","East Junction","East Avenue","Park North","Ring Park","Park Road","North Street","East Street","North Road","Expressway","Express West","North Cross","North Inn","North Link","West Link","West Street","Cinema","South Avenue","Park South" };
        distancesInt = new int[] { 4,6,8,10,12,14,16,12,13,13,14,15,17,18,16,20,18,16,13,11,13,15,9,7,8,7,9,11,10,9,6,14,16,20,14,16,18,20,20,16,5,7,5,6,14,5,4,5,14 };
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("Bus Station", new Distances("Bus Station", distTable));
        //Stop 49.
        stopNames = new String [] { "City Centre","Centre Square","Airport Junction","Airport Road","Airport","Courier Tower","Diamond Junction","City Centre West","West Road","Crescent Avenue","South Street","South Street (Ring)","Ring Road (Airport)","Crescent Junction","Crescent Avenue East","East Avenue","Ring Road (South)","Crescent South","South Road","Ring Link","Ring Road","Stadium","X Junction","Post Office","East Road","Crescent Road","South Square","Ring Road (East)","Arrow Junction","Lake Junction","East Way","East Junction","East Avenue","Park North","Ring Park","Park Road","North Street","East Street","North Road","Expressway","Express West","North Cross","North Inn","North Link","West Link","West Street","Cinema","Bus Station","Park South" };
        distancesInt = new int[] { 5,7,9,11,13,15,17,13,14,14,15,16,18,19,17,21,19,17,14,12,14,16,10,8,9,8,10,12,11,10,7,15,17,21,15,17,19,21,21,17,6,8,6,7,15,6,6,5,15 };
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("South Avenue", new Distances("South Avenue", distTable));
        //Stop 50.
        stopNames = new String [] { "City Centre","Centre Square","Airport Junction","Airport Road","Airport","Courier Tower","Diamond Junction","City Centre West","West Road","Crescent Avenue","South Street","South Street (Ring)","Ring Road (Airport)","Crescent Junction","Crescent Avenue East","East Avenue","Ring Road (South)","Crescent South","South Road","Ring Link","Ring Road","Stadium","X Junction","Post Office","East Road","Crescent Road","South Square","Ring Road (East)","Arrow Junction","Lake Junction","East Way","East Junction","East Avenue","Park North","Ring Park","Park Road","North Street","East Street","North Road","Expressway","Express West","North Cross","North Inn","North Link","West Link","West Street","Cinema","Bus Station","South Avenue" };
        distancesInt = new int[] { 7,9,11,13,15,14,16,12,10,10,11,13,16,14,16,17,18,16,12,13,15,17,11,9,10,10,11,13,14,13,9,17,18,23,17,19,21,23,20,19,8,9,7,8,17,8,15,14,15 };
        distTable = new HashMap<String, Integer>();
        for ( int i = 0; i < stopNames.length; i++ ) {
            distTable.put(stopNames[i], distancesInt[i]);
        }
        distances.put("Park South", new Distances("Park South", distTable));
    }
    
    /**
     * Get the scenario name.
     * @return a <code>String</code> with the scenario name.
     */
    public String getName () {
        return theScenarioName;
    }

    /**
     * Get the minimum satisfaction.
     * @return a <code>int</code> with the minimum satisfaction.
     */
    public int getMinimumSatisfaction () {
        return 50;
    }
    
    /**
     * Get the description i.e. details of the mission.
     * @return a <code>String</code> with the description.
     */
    public String getDescription ( ) {
        DecimalFormat format = new DecimalFormat("0.00");
        return super.getPlayerName() + " has been appointed Managing Director of the Longts Transport Company saying " +
            "'I am delighted to be taking on this role and look forward to delivering a transport system" +
            " worthy of the fantastic city of Longts. \n\nLongts City Council will provide access to 50 " +
            "bus stops around the city. In addition, the council has supplied £" + format.format(super.getBalance()) + " of initial funding and 2 new single decker buses."  +
            " The council expects the Longts Transport Company to offer a very frequent service to all bus stops without subsidy and with " +
            "a very high level of passenger satisfaction. If the passenger satisfaction drops below 50% then the contract " +
            "will be immediately revoked. The council looks forward to seeing if the Longts Transport Company can meet these ambitious targets.";
    }

    /**
     * Get the targets of this mission.
     * @return a <code>String</code> with the mission targets.
     */
    public String getTargets ( ) {
        return "1. Serve all bus stops in Longts. \n\n2. Ensure a very frequent service on all routes. \n\n3. Ensure that passenger satisfaction remains above 50% at all times. \n\n\n"
                + "The contract to run public transport services in Longts will be terminated if these targets are not met. Good Luck!";
    }
    
    /**
     * Get the location map file name to display the location map.
     * @return a <code>String</code> with the location map file name.
     */
    public String getLocationMapFileName ( ) {
        return "longtsmappic.jpg";
    }
    
    /**
     * Get the number of supplied vehicles at the start.
     * @return a <code>int</code> with the number of supplied vehicles.
     */
    public int getNumberSuppliedVehicles ( ) {
        return 3;
    }
    
}
