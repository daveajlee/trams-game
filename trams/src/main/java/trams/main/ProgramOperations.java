package trams.main;

import java.util.*;
import javax.xml.parsers.*;
import javax.xml.xpath.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.xml.sax.*;
import java.io.*;
import org.w3c.dom.*;

import org.apache.log4j.Logger;

import trams.data.*;
import trams.simulation.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * This class does all the hard computation - in terms of creating, loading & saving for the TraMS program.
 * @author Dave Lee.
 */
public class ProgramOperations {
    
    private Simulator theSimulator;
    private Hashtable<String, LinkedList<Integer>> theSchedIds;
    private Logger logger = Logger.getLogger(ProgramOperations.class);
    private ApplicationContext theContext = new ClassPathXmlApplicationContext("trams/spring/context.xml");
    //private LinkedList<Route> theRoutes;
    
    //Update this list plus createVehicleObject to add new vehicle types to TraMS!
    private String[] availableVehicles = new String[] { "MyBus Single Decker", "MyBus Double Decker", "MyBus Bendy", "MyTram Tram1" };
     
    /**
     * Create a new ProgramOperations object - default constructor.
     */
    public ProgramOperations ( ) {
        theSimulator = null;
        theSchedIds = new Hashtable<String, LinkedList<Integer>>();
    }
    
    /**
     * Set the simulator object.
     * @param s a <code>Simulator</code> object.
     */
    public void setSimulator ( Simulator s ) {
        theSimulator = s;
    }
    
    /**
     * Get the current simulation.
     * @return a <code>Simulator</code> object.
     */
    public Simulator getSimulator ( ) {
        return theSimulator;
    }
    
    /**
     * Create the simulator.
     * @param scenarioName a <code>String</code> with the scenario's name.
     * @param playerName a <code>String</code> with the player's name.
     * @return a <code>boolean</code> which is true iff the simulation was created successfully.
     */
    public boolean createSimulator ( String scenarioName, String playerName ) {
        Scenario scen = createScenarioObject(scenarioName, playerName, 800000.00, 100);
        theSimulator = new Simulator(scen);
        //Now for the scenario - create supplied vehicles.
        logger.debug("Creating " + scen.getNumberSuppliedVehicles() + " vehicles!");
        for ( int i = 0; i < scen.getNumberSuppliedVehicles(); i++ ) {
        	Calendar deliveryDate = theSimulator.getCurrentSimTime();
        	deliveryDate.add(Calendar.DAY_OF_MONTH, -1);
            scen.addVehicle(createVehicleObject(generateRandomReg(theSimulator.getShortYear()), "MyBus Single Decker", deliveryDate ));
        }
        //Also create supplied drivers.
        logger.debug("Creating " + scen.getNumberSuppliedDrivers() + " drivers!");
        for ( int i = 0; i < scen.getNumberSuppliedDrivers(); i++ ) {
        	Calendar startDate = theSimulator.getCurrentSimTime();
        	startDate.add(Calendar.DAY_OF_MONTH, -1);
        	addDriver(generateRandomName(), 40, 5, startDate);
        }
        return true;
    }
    
    /**
     * Increment the simulator's time.
     */
    public void incrementSimTime ( ) {
        theSimulator.incrementTime();
    }
    
    /**
     * Check if a particular schedule id exists for a particular route.
     * @param schedId a <code>int</code> with the schedule id.
     * @param routeNumber a <code>String</code> with the route number.
     * @return a <code>boolean</code> which is true iff the schedule does exist.
     */
    public boolean doesSchedIdExist ( int schedId, String routeNumber ) {
        LinkedList<Integer> scheds = theSchedIds.get(routeNumber);
        if ( scheds != null ) {
            for ( int i = 0; i < scheds.size(); i++ ) {
                if ( scheds.get(i) == schedId ) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Get the integer value of a month based on its three letter abbreviation.
     * @param name a <code>String</code> with the month name.
     * @return a <code>int</code> with the value id.
     */
    public int getMonth ( String name ) {
        if ( name.equalsIgnoreCase("Jan") ) {
            return Calendar.JANUARY;
        }
        else if ( name.equalsIgnoreCase("Feb") ) {
            return Calendar.FEBRUARY;
        }
        else if ( name.equalsIgnoreCase("Mar") ) {
            return Calendar.MARCH;
        }
        else if ( name.equalsIgnoreCase("Apr") ) {
            return Calendar.APRIL;
        }
        else if ( name.equalsIgnoreCase("May") ) {
            return Calendar.MAY;
        }
        else if ( name.equalsIgnoreCase("Jun") ) {
            return Calendar.JUNE;
        }
        else if ( name.equalsIgnoreCase("Jul") ) {
            return Calendar.JULY;
        }
        else if ( name.equalsIgnoreCase("Aug") ) {
            return Calendar.AUGUST;
        }
        else if ( name.equalsIgnoreCase("Sep") ) {
            return Calendar.SEPTEMBER;
        }
        else if ( name.equalsIgnoreCase("Oct") ) {
            return Calendar.OCTOBER;
        }
        else if ( name.equalsIgnoreCase("Nov") ) {
            return Calendar.NOVEMBER;
        }
        else if ( name.equalsIgnoreCase("Dec") ) {
            return Calendar.DECEMBER;
        }
        else {
            return -1;
        }
    }
    
    /**
     * Check if the integer value for the day of the week and the string representation matches.
     * @param dayOfWeek a <code>int</code> with the day of the week.
     * @param dayName a <code>String</code> with the day of the week.
     * @return a <code>boolean</code> which is true iff the string representation and integer match.
     */
    public boolean isSameDay ( int dayOfWeek, String dayName ) {
        if ( dayName.equalsIgnoreCase("Monday") && dayOfWeek == Calendar.MONDAY ) { return true; }
        if ( dayName.equalsIgnoreCase("Tuesday") && dayOfWeek == Calendar.TUESDAY ) { return true; }
        if ( dayName.equalsIgnoreCase("Wednesday") && dayOfWeek == Calendar.WEDNESDAY ) { return true; }
        if ( dayName.equalsIgnoreCase("Thursday") && dayOfWeek == Calendar.THURSDAY ) { return true; }
        if ( dayName.equalsIgnoreCase("Friday") && dayOfWeek == Calendar.FRIDAY ) { return true; }
        if ( dayName.equalsIgnoreCase("Saturday") && dayOfWeek == Calendar.SATURDAY ) { return true; }
        if ( dayName.equalsIgnoreCase("Sunday") && dayOfWeek == Calendar.SUNDAY ) { return true; }
        return false;
    }
    
    /**
     * Load the specified file.
     * @param file a <code>File</code> to load.
     * @return a <code>boolean</code> which is true iff the file was loaded successfully.
     */
    public boolean loadFile ( File file ) {
        //Load XML document using XML classes.
        Document document; //This is the important variable - the DOM!
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(file);
        } 
        //Exception handling from http://java.sun.com/webservices/jaxp/dist/1.1/docs/tutorial/dom/1_read.html
        catch (SAXException sxe) {
            // Error generated during parsing
            Exception  x = sxe;
            if (sxe.getException() != null)
                x = sxe.getException();
            x.printStackTrace();
            return false;
        } 
        catch (ParserConfigurationException pce) {
            // Parser with specified options can't be built
            pce.printStackTrace();
            return false;
        } 
        catch (IOException ioe) {
            // I/O error
            ioe.printStackTrace();
            return false;
        }
        //Run helper method to process XML document.
        return createSimulationFromFile(document);
    }
    
    /**
     * Save the simulation as a file.
     * @param f a <code>File</code> with the name of the file to save the simulation to.
     * @return a <code>boolean</code> which is true iff the simulation has been saved successfully.
     */
    public boolean saveFile ( File f ) {
        //Check it has correct extension!
        if ( !f.getName().endsWith(".tms") ) { f = new File(f.getName() + ".tms"); }
        //Create DOM instance.
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            //Create document builder.
            DocumentBuilder builder = factory.newDocumentBuilder();
            //Create new DOM document.
            Document document = builder.newDocument();
            createXMLDocument(document);
            //Save file.
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new FileOutputStream(f));
            transformer.transform(source, result);
            return true;
        } catch ( Exception e ) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Create an XML document for saving to file.
     * @param doc a <code>Document</code> object.
     * @return a <code>Document</code> object which has been modified.
     */
    public Document createXMLDocument ( Document doc ) {
        //Create root element.
        Element game = doc.createElement("game");
        Calendar currentTime = theSimulator.getCurrentSimTime();
        int hourNum = currentTime.get(Calendar.HOUR); String hour = "" + hourNum; if ( hourNum < 10 ) { hour = "0" + hourNum; } if ( currentTime.get(Calendar.AM_PM) == Calendar.PM ) { hourNum += 12; hour = "" + hourNum; } 
        int minNum = currentTime.get(Calendar.MINUTE); String minute = "" + minNum; if ( minNum < 10 ) { minute = "0" + minNum; }
        int monthNum = currentTime.get(Calendar.MONTH)+1; String month = "" + monthNum; if ( monthNum < 10 ) { month = "0" + monthNum; }
        int dateNum = currentTime.get(Calendar.DATE); String date = "" + dateNum; if ( dateNum < 10 ) { date = "0" + dateNum; }
        String time = currentTime.get(Calendar.YEAR) + "-" + month + "-" + date + "-" + hour + ":" + minute;
        game.setAttribute("time", time);
        game.setAttribute("increment", "" + theSimulator.getIncrement());
        game.setAttribute("difficulty", "" + theSimulator.getDifficultyLevel());
        doc.appendChild(game);
        //Create scenario name element.
        Element scenario = doc.createElement("scenario");
        scenario.setAttribute("name", theSimulator.getScenario().getScenarioName());
        scenario.setAttribute("pName", theSimulator.getScenario().getPlayerName());
        scenario.setAttribute("balance", "" + theSimulator.getScenario().getBalance());
        scenario.setAttribute("satisfaction", "" + theSimulator.getScenario().getPassengerSatisfaction());
        game.appendChild(scenario);
        //Create message elements.
        for ( int h = (theSimulator.getNumberMessages()-1); h >= 0; h-- ) {
            //Store message element - it will be quicker.
            Message myMessage = theSimulator.getMessage(h);
            //First of all, create message element.
            Element message = doc.createElement("message");
            //Now set attributes.
            message.setAttribute("subject", myMessage.getSubject());
            message.setAttribute("text", myMessage.getText());
            message.setAttribute("sender", myMessage.getSender());
            message.setAttribute("folder", myMessage.getFolder());
            message.setAttribute("date", myMessage.getDate());
            message.setAttribute("type", myMessage.getType());
            //Finally add message to scenario.
            scenario.appendChild(message);
        }
        //Create route elements.
        for ( int i = 0; i < theSimulator.getScenario().getNumberRoutes(); i++ ) {
            //Store route element - it will be a lot quicker.
            Route myRoute = theSimulator.getScenario().getRoute(i);
            //First of all, create route element.
            Element route = doc.createElement("route");
            route.setAttribute("number", myRoute.getRouteNumber());
            //Now do outward stops.
            Element outstops = doc.createElement("outstops");
            for ( int j = 0; j < myRoute.getNumStops(Route.OUTWARDSTOPS); j++ ) {
                Element outstop = doc.createElement("outstop");
                outstop.setAttribute("name", myRoute.getStop(Route.OUTWARDSTOPS, j).getStopName());
                outstops.appendChild(outstop);
            }
            route.appendChild(outstops);
            //Now do inward stops.
            Element instops = doc.createElement("instops");
            for ( int j = 0; j < myRoute.getNumStops(Route.RETURNSTOPS); j++ ) {
                Element instop = doc.createElement("instop");
                instop.setAttribute("name", myRoute.getStop(Route.RETURNSTOPS, j).getStopName());
                instops.appendChild(instop);
            }
            route.appendChild(instops);
            //Now do timetables for this route.
            Iterator<String> timetableNames = myRoute.getTimetableNames();
            while ( timetableNames.hasNext() ) {
                //Create a timetable element with attributes name, validFrom and validTo dates.
                Timetable myTimetable = myRoute.getTimetable(timetableNames.next());
                Element timetable = doc.createElement("timetable");
                timetable.setAttribute("name", myTimetable.getName());
                //Do valid from date.
                Calendar validFromDate = myTimetable.getValidFrom();
                int vfMonthNum = validFromDate.get(Calendar.MONTH)+1; String vfMonth = "" + vfMonthNum; if ( vfMonthNum < 10 ) { vfMonth = "0" + vfMonthNum; }
                int vfDateNum = validFromDate.get(Calendar.DATE); String vfDate = "" + vfDateNum; if ( vfDateNum < 10 ) { vfDate = "0" + vfDateNum; }
                timetable.setAttribute("validFrom", validFromDate.get(Calendar.YEAR) + "-" + vfMonth + "-" + vfDate );
                //Do valid to date.
                Calendar validToDate = myTimetable.getValidTo();
                int vtMonthNum = validToDate.get(Calendar.MONTH)+1; String vtMonth = "" + vtMonthNum; if ( vtMonthNum < 10 ) { vtMonth = "0" + vtMonthNum; }
                int vtDateNum = validToDate.get(Calendar.DATE); String vtDate = "" + vtDateNum; if ( vtDateNum < 10 ) { vtDate = "0" + vtDateNum; }
                timetable.setAttribute("validTo", validToDate.get(Calendar.YEAR) + "-" + vtMonth + "-" + vtDate );
                //Now for all service patterns.
                Iterator<String> servicePatternNames = myTimetable.getServicePatternNames().iterator();
                while ( servicePatternNames.hasNext() ) {
                    ServicePattern myServicePattern = myTimetable.getServicePattern(servicePatternNames.next());
                    //Create element with appropriate attributes.
                    Element servicePattern = doc.createElement("servicePattern");
                    servicePattern.setAttribute("name", myServicePattern.getName());
                    servicePattern.setAttribute("days", myServicePattern.getDaysOfOperationAsString());
                    servicePattern.setAttribute("returnTerminus", myServicePattern.getReturnTerminus());
                    servicePattern.setAttribute("outgoingTerminus", myServicePattern.getOutgoingTerminus());
                    servicePattern.setAttribute("startTime", myServicePattern.getStartTimeInfo());
                    servicePattern.setAttribute("endTime", myServicePattern.getEndTimeInfo());
                    servicePattern.setAttribute("frequency", "" + myServicePattern.getFrequency());
                    servicePattern.setAttribute("duration", "" + myServicePattern.getDuration());
                    timetable.appendChild(servicePattern);
                }
                route.appendChild(timetable);
            }
            //Finally, add route to scenario.
            scenario.appendChild(route);
        }
        //Create vehicle elements.
        Element vehicles = doc.createElement("vehicles");
        for ( int i = 0; i < theSimulator.getScenario().getNumberVehicles(); i++ ) {
            Element vehicle = doc.createElement("vehicle");
            vehicle.setAttribute("id", theSimulator.getScenario().getVehicle(i).getRegistrationNumber());
            vehicle.setAttribute("type", theSimulator.getScenario().getVehicle(i).getModel());
            vehicle.setAttribute("deliveryDate", "" + theSimulator.getScenario().getVehicle(i).getDeliveryDate());
            String[] currentRouteAndId = theSimulator.getScenario().getVehicle(i).getAssignedScheduleId().split("/");
            if ( currentRouteAndId.length == 2 ) {
                vehicle.setAttribute("route", currentRouteAndId[0]);
                vehicle.setAttribute("schedId", currentRouteAndId[1]);
            }
            else {
                //Vehicle was not assigned so make it blank for compatibility.
                vehicle.setAttribute("route", "");
                vehicle.setAttribute("schedId", "");
            }
            vehicles.appendChild(vehicle);
        }
        scenario.appendChild(vehicles);
        //Finally, return document.
        return doc;
    }
    
    public boolean saveObjFile ( File f ) {
        //Check it has correct extension!
        if ( !f.getName().endsWith(".tra") ) { f = new File(f.getName() + ".tra"); }
        //Save file using object stream.
        try {
            FileOutputStream fout = new FileOutputStream(f.getPath());
            ObjectOutputStream objout = new ObjectOutputStream(fout);
            objout.writeObject(getSimulator());
            return true;
        } catch ( Exception e ) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get the route object which corresponds to the supplied route number.
     * @param routeNumber a <code>String</code> with the route number.
     * @return a <code>Route</code> object.
     */
    public Route getRoute ( String routeNumber ) {
        return theSimulator.getScenario().getRoute(routeNumber);
        /*//Go through all routes,
        for ( int i = 0; i < theRoutes.size(); i++ ) {
            if ( theRoutes.get(i).getRouteNumber().equalsIgnoreCase(routeNumber) ) {
                return theRoutes.get(i);
            }
        }
        return null;*/
    }
    
    /**
     * Get the position of the route in the route list for the supplied route number.
     * @param routeNumber a <code>String</code> with the route number.
     * @return a <code>int</code> with the position.
     */
    /*public int getRoutePos ( String routeNumber ) {
        return theSimulator.getScenario().getR
        //Go through all routes,
        for ( int i = 0; i < theRoutes.size(); i++ ) {
            if ( theRoutes.get(i).getRouteNumber().equalsIgnoreCase(routeNumber) ) {
                return i;
            }
        }
        return -1;
    }*/
    
    /**
     * Create simulation from file - for load.
     * @param document a <code>Document</code> object.
     * @return a <code>boolean</code> which is true iff the simulation was created successfully.
     */
    private boolean createSimulationFromFile ( Document document ) {
        //First of all create all of the data we need to create firstly.
        try {
            XPath xpath = XPathFactory.newInstance().newXPath();
            //First get scenario name!
            Element scenario = (Element) ((NodeList) xpath.evaluate("//scenario", document.getDocumentElement(), XPathConstants.NODESET)).item(0);
            String scenarioName = scenario.getAttribute("name");
            String playerName = scenario.getAttribute("pName");
            double balance = Double.parseDouble(scenario.getAttribute("balance"));
            int psgSatisfaction = Integer.parseInt(scenario.getAttribute("satisfaction"));
            //Now create the scenario object.
            Scenario myScenario = createScenarioObject(scenarioName, playerName, balance, psgSatisfaction);
            //Seventhly, create the simulation object.
            theSimulator = new Simulator(myScenario, document.getDocumentElement().getAttribute("time"), Integer.parseInt(document.getDocumentElement().getAttribute("increment")));
            //Set the difficulty level.
            theSimulator.setDifficultyLevel(document.getDocumentElement().getAttribute("difficulty"));
            //Now add the messages!!!!
            NodeList messageNode = (NodeList) xpath.evaluate("//scenario/message", document.getDocumentElement(), XPathConstants.NODESET);
            for ( int i = 0; i < messageNode.getLength(); i++ ) {
                Element messageElement = (Element) messageNode.item(i);
                //Create message element.
                theSimulator.addMessage(createMessageObject(messageElement.getAttribute("subject"), messageElement.getAttribute("text"), messageElement.getAttribute("sender"), messageElement.getAttribute("folder"), messageElement.getAttribute("date"), messageElement.getAttribute("type")));
            }
            //Fourthly, get the route details and create the route object.
            //theRoutes = new LinkedList<Route>();
            NodeList routeNode = (NodeList) xpath.evaluate("//scenario/route", document.getDocumentElement(), XPathConstants.NODESET);
            for ( int i = 0; i < routeNode.getLength(); i++ ) {
                Element routeElement = (Element) routeNode.item(i);
                //Create the route by creating the route number and valid from and valid to dates.
                String routeNumber = routeElement.getAttribute("number");
                //Create route object.
                Route route = new Route();
                route.setRouteNumber(routeNumber);
                //Now get the outward stops.
                NodeList outwardStopNodes = routeElement.getElementsByTagName("outstop");
                for ( int j = 0; j < outwardStopNodes.getLength(); j++ ) {
                    //Add each stop to the route object.
                    Element stopElement = (Element) outwardStopNodes.item(j);
                    route.addStop(stopElement.getAttribute("name"), Route.OUTWARDSTOPS);
                }
                //Now get the inward stops.
                NodeList inwardStopNodes = routeElement.getElementsByTagName("instop");
                for ( int j = 0; j < inwardStopNodes.getLength(); j++ ) {
                    //Add each stop to the route object.
                    Element stopElement = (Element) inwardStopNodes.item(j);
                    route.addStop(stopElement.getAttribute("name"), Route.RETURNSTOPS);
                }
                //Now go through and get the timetable elements.
                NodeList timetableNodes = routeElement.getElementsByTagName("timetable");
                for ( int j = 0; j < timetableNodes.getLength(); j++ ) {
                    Element timetableElement = (Element) timetableNodes.item(j);
                    //Get timetable information.
                    String[] validFromDates = timetableElement.getAttribute("validFrom").split("-");
                    Calendar validFrom = new GregorianCalendar(Integer.parseInt(validFromDates[0]), Integer.parseInt(validFromDates[1])-1, Integer.parseInt(validFromDates[2]));
                    String[] validToDates = timetableElement.getAttribute("validTo").split("-");
                    Calendar validTo = new GregorianCalendar(Integer.parseInt(validToDates[0]), Integer.parseInt(validToDates[1])-1, Integer.parseInt(validToDates[2]));
                    //Create timetable object.
                    Timetable myTimetable = new Timetable(timetableElement.getAttribute("name"), validFrom, validTo);
                    //Now add all service patterns.
                    NodeList serviceNodes = timetableElement.getElementsByTagName("servicePattern");
                    for ( int k = 0; k < serviceNodes.getLength(); k++ ) {
                        Element serviceElement = (Element) serviceNodes.item(k);
                        //Get service information.
                        String daysOfOperation = serviceElement.getAttribute("days");
                        String[] timeFrom = serviceElement.getAttribute("startTime").split(":");
                        Calendar startTime = new GregorianCalendar(2009,Calendar.AUGUST,5,Integer.parseInt(timeFrom[0]),Integer.parseInt(timeFrom[1]));
                        String[] timeTo = serviceElement.getAttribute("endTime").split(":");
                        Calendar endTime = new GregorianCalendar(2009,Calendar.AUGUST,5,Integer.parseInt(timeTo[0]),Integer.parseInt(timeTo[1]));
                        //Create service object.
                        ServicePattern myService = new ServicePattern(serviceElement.getAttribute("name"), daysOfOperation, serviceElement.getAttribute("returnTerminus"), serviceElement.getAttribute("outgoingTerminus"), startTime, endTime, Integer.parseInt(serviceElement.getAttribute("frequency")), Integer.parseInt(serviceElement.getAttribute("duration")));
                        //Add to timetable.
                        myTimetable.addServicePattern(serviceElement.getAttribute("name"), myService);
                    }
                    //Finally add this timetable to the route.
                    logger.debug("Adding " + myTimetable.getName() + " to " + route.getRouteNumber());
                    route.addTimetable(timetableElement.getAttribute("name"), myTimetable);
                }
                //Generate timetables.
                List<Service> outgoingServices = route.generateServiceTimetables(getSimulator().getCurrentSimTime(), myScenario, Route.OUTWARDSTOPS);
                List<Service> returnServices = route.generateServiceTimetables(getSimulator().getCurrentSimTime(), myScenario, Route.RETURNSTOPS);
                route.generateRouteSchedules(outgoingServices, returnServices);
                //Add route.
                //theRoutes.add(route);
                myScenario.addRoute(route);
            }
            //Sixthly, get the vehicles and create relevant vehicle objects.
            NodeList vehicleList = (NodeList) xpath.evaluate("//scenario/vehicles/vehicle", document.getDocumentElement(), XPathConstants.NODESET);
            for ( int i = 0; i < vehicleList.getLength(); i++ ) {
                Element thisElem = (Element) vehicleList.item(i);
                String[] deliveryDates = thisElem.getAttribute("deliveryDate").split("-");
                Calendar deliveryDate = new GregorianCalendar(Integer.parseInt(deliveryDates[0]), Integer.parseInt(deliveryDates[1])-1, Integer.parseInt(deliveryDates[2]));
                Vehicle myVeh = createVehicleObject(thisElem.getAttribute("id"), thisElem.getAttribute("type"), deliveryDate);
                myVeh.setAssignedSchedule(null);
                myScenario.addVehicle(myVeh);
                //Add allocation to route.
                String schedId = thisElem.getAttribute("route") + "/" + thisElem.getAttribute("schedId");
                for ( int j = 0; j < myScenario.getNumberRoutes(); j++ ) {
                    if ( myScenario.getRoute(j).getRouteNumber().equalsIgnoreCase(thisElem.getAttribute("route")) ) {
                        String[] dates = document.getDocumentElement().getAttribute("time").split("-");
                        String day = dates[2];
                        if ( day.substring(0,1).equalsIgnoreCase("0") ) { day = day.substring(1,2); }
                        myScenario.getRoute(j).addAllocation(schedId, myVeh);
                        break;
                    }
                }
                //logger.debug("Adding vehicle with id " + v.getLast().getVehicleID() + " type " + v.getLast().getVehicleType() + " age " + v.getLast().getVehicleAge());
            }
            
        }
        catch (Exception e) {
            //logger.debug("Exception!");
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    /**
     * Create a new simulation.
     * @param scenario a <code>Scenario</code> with the scenario to simulate.
     * @return a <code>boolean</code> which is true iff the simulation was created successfully.
     */
    public boolean createSimulation ( Scenario scenario ) {
        theSimulator = new Simulator(scenario);
        return true;
    }
    
    /**
     * Add a route to the simualtion.
     * @param r a <code>Route</code> object.
     * @return a <code>boolean</code> which is true iff the route was added successfully.
     */
    public boolean addRoute ( Route r ) {
        return theSimulator.getScenario().addRoute(r);
    }
    
    /**
     * Delete a route from the simulation.
     * @param r a <code>Route</code> object.
     * @return a <code>boolean</code> which is true iff the route was deleted successfully.
     */
    public boolean deleteRoute ( Route r ) {
        return theSimulator.getScenario().deleteRoute(r);
    }

    /**
     * Add a driver in the simulation.
     * @param name a <code>String</code> with the driver's name.
     * @param hours a <code>int</code> with the contracted hours.
     * @param startDate a <code>Calendar</code> with the start date.
     * @return a <code>boolean</code> which is true iff the driver has been successfully employed.
     */
    public boolean addDriver ( String name, int hours, int rate, Calendar startDate ) {
    	//Determine the next free personal id.
    	int highestSoFar = 0;
    	List<Driver> drivers = theSimulator.getScenario().getDrivers();
    	for ( int i = 0; i < drivers.size(); i++ ) {
    		if ( highestSoFar < drivers.get(i).getIdNumber() ) {
    			highestSoFar = drivers.get(i).getIdNumber();
    		}
    	}
        return theSimulator.getScenario().employDriver(new Driver((highestSoFar+1), name, hours, rate, startDate));
    }
    
    /**
     * Sack a driver from the simulation.
     * @param d a <code>Driver</code> object to sell.
     * @return a <code>boolean</code> which is true iff the driver has been sacked successfully.
     */
    public boolean sackDriver ( Driver d ) {
        return theSimulator.getScenario().sackDriver(d, theSimulator.getCurrentSimTime());
    }

    /**
     * Purchase a vehicle to the simulation.
     * @param type a <code>String</code> with the vehicle type.
     * @param deliveryDate a <code>Calendar</code> with the delivery date.
     * @return a <code>boolean</code> which is true iff the vehicle has been purchased successfully.
     */
    public boolean purchaseVehicle ( String type, Calendar deliveryDate ) {
        return theSimulator.getScenario().purchaseVehicle(createVehicleObject(generateRandomReg(theSimulator.getShortYear()), type, deliveryDate));
    }
    
    /**
     * Helper method to generate random driver names.
     */
    private String generateRandomName ( ) {
    	//Get current list of drivers.
    	List<Driver> drivers = theSimulator.getScenario().getDrivers();
    	//List of possible names.
    	String[] firstNames = new String[] { "Bob", "Bill", "Robert", "David", "Emma", "Ruth", "Samuel", "Matthew", "Mark", "John", "Paul", "Timothy", "Peter", "Andrew" };
    	String[] surNames = new String[] { "Bloggs", "Nachname" };
    	//At the moment simply choose a firstname and surname at random.
    	boolean isDuplicate = true; Random random = new Random(); String proposedName = "";
    	while ( isDuplicate ) {
    		isDuplicate = false;
    		proposedName = firstNames[random.nextInt(firstNames.length)] + " " + surNames[random.nextInt(surNames.length)];
    		for ( int i = 0; i < drivers.size(); i++ ) {
    			if ( drivers.get(i).getName().equalsIgnoreCase(proposedName) ) {
    				isDuplicate = true; break;
    			}
    		}
    	}
    	return proposedName;
    }
    
    /**
     * Helper method to generate random vehicle registration.
     * @param year a <code>String</code> with the current simulation year.
     */
    private String generateRandomReg ( String year ) {
        //Generate random registration - in form 2 digit year - then 5 random letters.
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        //This is our loop - till we get unique reg.
        boolean isUniqueReg = true;
        String randomReg = "" + year + "-";
        do {
            //Here is random reg.
            Random r = new Random();
            for ( int i = 0; i < 5; i++ ) {
                randomReg += alphabet.charAt(r.nextInt(alphabet.length()));
            }
            //Now check that random reg not been generated before.
            for ( int i = 0; i < getSimulator().getScenario().getNumberVehicles(); i++ ) {
                if ( getSimulator().getScenario().getVehicle(i).getRegistrationNumber().equalsIgnoreCase(randomReg) ) {
                    isUniqueReg = false;
                    break;
                }
            }
            if ( !isUniqueReg ) {
                randomReg = "" + year + "-";
            }
        } while ( !isUniqueReg );
        return randomReg;
    }

    /**
     * Create message object based on type.
     * This method needs to be updated in order to add new message types to TraMS.
     * @param subject a <code>String</code> with the subject.
     * @param text a <code>String</code> with the message text.
     * @param sender a <code>String</code> with the sender.
     * @param folder a <code>String</code> with the folder.
     * @param date a <code>String</code> with the date.
     * @param type a <code>String</code> with the type.
     */
    public Message createMessageObject ( String subject, String text, String sender, String folder, String date, String type ) {
        if ( type.equalsIgnoreCase("Council") ) {
        	Message message = (Message) theContext.getBean("CouncilMessage");
        	message.setSubject(subject);
        	message.setText(text);
        	message.setSender(sender);
        	message.setFolder(folder);
        	message.setDate(date);
        	return message;
        }
        else if ( type.equalsIgnoreCase("Vehicle") ) {
            Message message = (Message) theContext.getBean("VehicleMessage");
            message.setSubject(subject);
            message.setText(text);
            message.setSender(sender);
            message.setFolder(folder);
            message.setDate(date);
            return message;
        }
        else {
            return null; //Null if can't find message.
        }
    }
    
    public ApplicationContext getContext() {
    	return theContext;
    }
    
    /**
     * Create vehicle object based on type.
     * This method needs to be updated in order to new vehicle types to TraMS.
     * @param id a <code>String</code> with the vehicle registration.
     * @param type a <code>String</code> with the vehicle type.
     * @param deliveryDate a <code>Calendar</code> with the delivery date.
     * @return a <code>Vehicle</code> object.
     */
    public Vehicle createVehicleObject ( String id, String type, Calendar deliveryDate ) {
    	Vehicle vehicle = null;
        if ( type.equalsIgnoreCase("MyBus Single Decker") ) {
        	vehicle = (Vehicle) theContext.getBean("singleDeckerBus");
        }
        else if ( type.equalsIgnoreCase("MyBus Double Decker") ) {
            vehicle = (Vehicle) theContext.getBean("doubleDeckerBus");
        }
        else if ( type.equalsIgnoreCase("MyBus Bendy") ) {
            vehicle = (Vehicle) theContext.getBean("bendyBus");
        }
        else if ( type.equalsIgnoreCase("MyTram Tram1") ) {
        	vehicle = (Vehicle) theContext.getBean("tram");
        }
        vehicle.setRegistrationNumber(id);
        System.out.println("This vehicle has registration number " + vehicle.getRegistrationNumber());
    	vehicle.setDeliveryDate(deliveryDate);
    	return (Vehicle) vehicle.clone();
    }
    
    /**
     * Create scenario object based on type.
     * This method needs to be updated in order to add new scenarios to TraMS.
     * @param scenarioName a <code>String</code> with the scenario name.
     * @param pName a <code>String</code> with the player name.
     * @param balance a <code>double</code> with the balance.
     * @param psgSatisfaction a <code>int</code> with the passenger satisfaction.
     * @return a <code>Scenario</code> object.
     */
    public Scenario createScenarioObject ( String scenarioName, String pName, double balance, int psgSatisfaction ) {
        Scenario scenario = null;
    	if ( scenarioName.equalsIgnoreCase("Landuff Transport Company") ) {
            scenario = (Scenario) theContext.getBean("LanduffScenario");
        }
        else if ( scenarioName.equalsIgnoreCase("MDorf Transport Company") ) {
            scenario = (Scenario) theContext.getBean("MDorfScenario");
        }
        else if ( scenarioName.equalsIgnoreCase("Longts Transport Company") ) {
            scenario = (Scenario) theContext.getBean("LongtsScenario");
        }
        else {
        	return null; //Null if can't find scenario.
        }
        scenario.setPlayerName(pName);
        scenario.setBalance(balance);
        scenario.setPassengerSatisfaction(psgSatisfaction);
        return scenario;
    }
    
    /**
     * Get the number of available vehicle types.
     * @return a <code>int</code> with the number of available vehicle types.
     */
    public int getNumVehicleTypes ( ) {
        return availableVehicles.length;
    }
    
    /**
     * Get the vehicle type at the supplied position in the array.
     * @param pos a <code>int</code> with the position in the array.
     * @return a <code>String</code> with the vehicle type.
     */
    public String getVehicleType ( int pos ) {
        return availableVehicles[pos];
    }

    /**
     * Sell a vehicle from the simulation.
     * @param v a <code>Vehicle</code> object to sell.
     * @return a <code>boolean</code. which is true iff the vehicle has been sold successfully.
     */
    public boolean sellVehicle ( Vehicle v ) {
        return theSimulator.getScenario().sellVehicle(v, theSimulator.getCurrentSimTime());
    }
    
    /**
     * Get current allocations for the simulation.
     * @return a <code>LinkedList</code> of allocations.
     */
    public ArrayList<String> getAllocations ( ) {
        //Allocations list.
        ArrayList<String> allocations = new ArrayList<String>();
        //Now go through and add their allocation if they already have an allocation.
        for ( int i = 0; i < theSimulator.getScenario().getNumberVehicles(); i++ ) {
            if ( !theSimulator.getScenario().getVehicle(i).getAssignedScheduleId().equalsIgnoreCase("Not Assigned") ) {
                allocations.add(theSimulator.getScenario().getVehicle(i).getAssignedScheduleId() + " & " + theSimulator.getScenario().getVehicle(i).getRegistrationNumber());
            }
        }
        //Return allocations list.
        return allocations;
    }
    
}
