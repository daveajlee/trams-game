package de.davelee.trams.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.davelee.trams.data.Game;
import de.davelee.trams.data.JourneyPattern;
import de.davelee.trams.data.Message;
import de.davelee.trams.data.Route;
import de.davelee.trams.data.Scenario;
import de.davelee.trams.data.Simulator;
import de.davelee.trams.data.Stop;
import de.davelee.trams.data.Timetable;
import de.davelee.trams.data.Vehicle;
import de.davelee.trams.util.DifficultyLevel;
import de.davelee.trams.util.MessageFolder;

public class FileService {

	private SimulationService simulationService;
	private GameService gameService;
	private FactoryService springService;
	private RouteService routeService;
	private MessageService messageService;
	private VehicleService vehicleService;
	
	public FileService() {
		routeService = new RouteService();
        gameService = new GameService();
        simulationService = new SimulationService();
        springService = new FactoryService();
        messageService = new MessageService();
        vehicleService = new VehicleService();
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
        Calendar currentTime = simulationService.getSimulator().getCurrentTime();
        int hourNum = currentTime.get(Calendar.HOUR); String hour = "" + hourNum; if ( hourNum < 10 ) { hour = "0" + hourNum; } if ( currentTime.get(Calendar.AM_PM) == Calendar.PM ) { hourNum += 12; hour = "" + hourNum; } 
        int minNum = currentTime.get(Calendar.MINUTE); String minute = "" + minNum; if ( minNum < 10 ) { minute = "0" + minNum; }
        int monthNum = currentTime.get(Calendar.MONTH)+1; String month = "" + monthNum; if ( monthNum < 10 ) { month = "0" + monthNum; }
        int dateNum = currentTime.get(Calendar.DATE); String date = "" + dateNum; if ( dateNum < 10 ) { date = "0" + dateNum; }
        String time = currentTime.get(Calendar.YEAR) + "-" + month + "-" + date + "-" + hour + ":" + minute;
        game.setAttribute("time", time);
        game.setAttribute("increment", "" + simulationService.getSimulator().getTimeIncrement());
        Hashtable<String, String> gameTable = gameService.getGameAsString();
        game.setAttribute("difficulty", "" + gameTable.get("DifficultyLevel"));
        doc.appendChild(game);
        //Create scenario name element.
        Element scenario = doc.createElement("scenario");
        scenario.setAttribute("name", gameTable.get("ScenarioName"));
        scenario.setAttribute("pName", gameTable.get("PlayerName"));
        scenario.setAttribute("balance", "" + gameTable.get("Balance"));
        scenario.setAttribute("satisfaction", "" + gameTable.get("PassengerSatisfaction"));
        game.appendChild(scenario);
        //Create message elements.
        for ( int h = (messageService.getAllMessages().size()-1); h >= 0; h-- ) {
            //Store message element - it will be quicker.
            Message myMessage = messageService.getAllMessages().get(h);
            //First of all, create message element.
            Element message = doc.createElement("message");
            //Now set attributes.
            message.setAttribute("subject", myMessage.getSubject());
            message.setAttribute("text", myMessage.getText());
            message.setAttribute("sender", myMessage.getSender());
            message.setAttribute("folder", myMessage.getFolder().name());
            //TODO: toString methode oder was anderes?
            message.setAttribute("date", myMessage.getDate().toString());
            //Finally add message to scenario.
            scenario.appendChild(message);
        }
        //Create route elements.
        for ( int i = 0; i < routeService.getAllRoutes().size(); i++ ) {
            //Store route element - it will be a lot quicker.
            Route myRoute = routeService.getAllRoutes().get(i);
            //First of all, create route element.
            Element route = doc.createElement("route");
            route.setAttribute("number", myRoute.getRouteNumber());
            //Now do outward stops.
            Element outstops = doc.createElement("stops");
            for ( int j = 0; j < myRoute.getStops().size(); j++ ) {
                Element outstop = doc.createElement("stop");
                outstop.setAttribute("name", myRoute.getStops().get(j).getStopName());
                outstops.appendChild(outstop);
            }
            route.appendChild(outstops);
            //Now do timetables for this route.
            Iterator<String> timetableNames = myRoute.getTimetableNames();
            while ( timetableNames.hasNext() ) {
                //Create a timetable element with attributes name, validFrom and validTo dates.
                Timetable myTimetable = myRoute.getTimetable(timetableNames.next());
                Element timetable = doc.createElement("timetable");
                timetable.setAttribute("name", myTimetable.getName());
                //Do valid from date.
                Calendar validFromDate = myTimetable.getValidFromDate();
                int vfMonthNum = validFromDate.get(Calendar.MONTH)+1; String vfMonth = "" + vfMonthNum; if ( vfMonthNum < 10 ) { vfMonth = "0" + vfMonthNum; }
                int vfDateNum = validFromDate.get(Calendar.DATE); String vfDate = "" + vfDateNum; if ( vfDateNum < 10 ) { vfDate = "0" + vfDateNum; }
                timetable.setAttribute("validFrom", validFromDate.get(Calendar.YEAR) + "-" + vfMonth + "-" + vfDate );
                //Do valid to date.
                Calendar validToDate = myTimetable.getValidToDate();
                int vtMonthNum = validToDate.get(Calendar.MONTH)+1; String vtMonth = "" + vtMonthNum; if ( vtMonthNum < 10 ) { vtMonth = "0" + vtMonthNum; }
                int vtDateNum = validToDate.get(Calendar.DATE); String vtDate = "" + vtDateNum; if ( vtDateNum < 10 ) { vtDate = "0" + vtDateNum; }
                timetable.setAttribute("validTo", validToDate.get(Calendar.YEAR) + "-" + vtMonth + "-" + vtDate );
                //Now for all journey patterns.
                Iterator<String> journeyPatternNames = myTimetable.getJourneyPatternNames().iterator();
                while ( journeyPatternNames.hasNext() ) {
                    JourneyPattern myJourneyPattern = myTimetable.getJourneyPattern(journeyPatternNames.next());
                    //Create element with appropriate attributes.
                    Element journeyPattern = doc.createElement("journeyPattern");
                    journeyPattern.setAttribute("name", myJourneyPattern.getName());
                    journeyPattern.setAttribute("days", createStringFromList(myJourneyPattern.getDaysOfOperation()));
                    journeyPattern.setAttribute("returnTerminus", myJourneyPattern.getReturnTerminus());
                    journeyPattern.setAttribute("outgoingTerminus", myJourneyPattern.getOutgoingTerminus());
                    journeyPattern.setAttribute("startTime", myJourneyPattern.getStartTime().toString());
                    journeyPattern.setAttribute("endTime", myJourneyPattern.getEndTime().toString());
                    journeyPattern.setAttribute("frequency", "" + myJourneyPattern.getFrequency());
                    journeyPattern.setAttribute("duration", "" + myJourneyPattern.getRouteDuration());
                    timetable.appendChild(journeyPattern);
                }
                route.appendChild(timetable);
            }
            //Finally, add route to scenario.
            scenario.appendChild(route);
        }
        //Create vehicle elements.
        Element vehicles = doc.createElement("vehicles");
        for ( int i = 0; i < vehicleService.getAllVehicles().size(); i++ ) {
            Element vehicle = doc.createElement("vehicle");
            vehicle.setAttribute("id", vehicleService.getAllVehicles().get(i).getRegistrationNumber());
            vehicle.setAttribute("type", vehicleService.getAllVehicles().get(i).getModel());
            vehicle.setAttribute("deliveryDate", "" + vehicleService.getAllVehicles().get(i).getDeliveryDate());
            //TODO: If appropriate reimplement route schedule id with route.
            /*String[] currentRouteAndId = theSimulator.getScenario().getVehicle(i).getRouteScheduleId().split("/");
            if ( currentRouteAndId.length == 2 ) {
                vehicle.setAttribute("route", currentRouteAndId[0]);
                vehicle.setAttribute("schedId", currentRouteAndId[1]);
            }
            else {*/
                //Vehicle was not assigned so make it blank for compatibility.
                vehicle.setAttribute("route", "");
                vehicle.setAttribute("schedId", "");
            //}
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
            objout.writeObject(simulationService.getSimulator());
            objout.close();
            return true;
        } catch ( Exception e ) {
            e.printStackTrace();
            return false;
        }
    }
    
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
            Scenario myScenario = springService.createScenarioObject(scenarioName);
            Game game = new Game();
            game.setPassengerSatisfaction(psgSatisfaction);
            game.setBalance(balance);
            game.setPlayerName(playerName);
            game.setScenarioName(scenarioName);
            game.setDifficultyLevel(DifficultyLevel.valueOf(document.getDocumentElement().getAttribute("difficulty")));
            //Seventhly, create the simulation object.
            Simulator simulator = new Simulator();
            String[] dateTimes = document.getDocumentElement().getAttribute("time").split("-");
            String[] times = dateTimes[3].split(":");
            Calendar theCalendar = new GregorianCalendar(Integer.parseInt(dateTimes[0]), Integer.parseInt(dateTimes[1])-1, Integer.parseInt(dateTimes[2]), Integer.parseInt(times[0]), Integer.parseInt(times[1]), 0); 
            simulator.setCurrentTime(theCalendar);
            simulator.setTimeIncrement(Integer.parseInt(document.getDocumentElement().getAttribute("increment")));
            simulator.setPreviousTime((Calendar) simulator.getCurrentTime().clone());
            //Now add the messages!!!!
            NodeList messageNode = (NodeList) xpath.evaluate("//scenario/message", document.getDocumentElement(), XPathConstants.NODESET);
            for ( int i = 0; i < messageNode.getLength(); i++ ) {
                Element messageElement = (Element) messageNode.item(i);
                //Create message element.
                //TODO: reimplement message date.
                messageService.saveMessage(messageService.createMessage(messageElement.getAttribute("subject"), messageElement.getAttribute("text"), messageElement.getAttribute("sender"), MessageFolder.valueOf(messageElement.getAttribute("folder")), Calendar.getInstance()));
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
                NodeList stopNodes = routeElement.getElementsByTagName("stop");
                List<Stop> stops = new ArrayList<Stop>();
                for ( int j = 0; j < stopNodes.getLength(); j++ ) {
                    //Add each stop to the route object.
                    Element stopElement = (Element) stopNodes.item(j);
                    Stop myStop = new Stop();
                    myStop.setStopName(stopElement.getAttribute("name"));
                    stops.add(myStop);
                }
                route.setStops(stops);
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
                    Timetable myTimetable = new Timetable();
                    myTimetable.setName(timetableElement.getAttribute("name"));
                    myTimetable.setValidFromDate(validFrom);
                    myTimetable.setValidToDate(validTo);
                    //Now add all journey patterns.
                    NodeList journeyNodes = timetableElement.getElementsByTagName("journeyPattern");
                    for ( int k = 0; k < journeyNodes.getLength(); k++ ) {
                        Element journeyElement = (Element) journeyNodes.item(k);
                        //Get service information.
                        String daysOfOperation = journeyElement.getAttribute("days");
                        String[] timeFrom = journeyElement.getAttribute("startTime").split(":");
                        Calendar startTime = new GregorianCalendar(2009,Calendar.AUGUST,5,Integer.parseInt(timeFrom[0]),Integer.parseInt(timeFrom[1]));
                        String[] timeTo = journeyElement.getAttribute("endTime").split(":");
                        Calendar endTime = new GregorianCalendar(2009,Calendar.AUGUST,5,Integer.parseInt(timeTo[0]),Integer.parseInt(timeTo[1]));
                        //Create journey object.
                        JourneyPattern myJourney = new JourneyPattern();
                        myJourney.setName(journeyElement.getAttribute("name"));
                        myJourney.setDaysOfOperation(createListFromString(daysOfOperation));
                        myJourney.setReturnTerminus(journeyElement.getAttribute("returnTerminus"));
                        myJourney.setOutgoingTerminus(journeyElement.getAttribute("outgoingTerminus"));
                        myJourney.setStartTime(startTime);
                        myJourney.setEndTime(endTime);
                        myJourney.setFrequency(Integer.parseInt(journeyElement.getAttribute("frequency")));
                        myJourney.setRouteDuration( Integer.parseInt(journeyElement.getAttribute("duration")));
                        
                        //Add to timetable.
                        myTimetable.addJourneyPattern(journeyElement.getAttribute("name"), myJourney);
                    }
                    //Finally add this timetable to the route.
                    route.addTimetable(timetableElement.getAttribute("name"), myTimetable);
                }
                //Generate timetables.
                routeService.generateRouteSchedules(route.getId(), simulator.getCurrentTime(), myScenario.getScenarioName());
                //Add route.
                routeService.saveRoute(route);
            }
            //Sixthly, get the vehicles and create relevant vehicle objects.
            NodeList vehicleList = (NodeList) xpath.evaluate("//scenario/vehicles/vehicle", document.getDocumentElement(), XPathConstants.NODESET);
            for ( int i = 0; i < vehicleList.getLength(); i++ ) {
                Element thisElem = (Element) vehicleList.item(i);
                String[] deliveryDates = thisElem.getAttribute("deliveryDate").split("-");
                Calendar deliveryDate = new GregorianCalendar(Integer.parseInt(deliveryDates[0]), Integer.parseInt(deliveryDates[1])-1, Integer.parseInt(deliveryDates[2]));
                Vehicle myVeh = springService.createVehicleObject(thisElem.getAttribute("type"), thisElem.getAttribute("id"), deliveryDate);
                myVeh.setRouteScheduleId(0);
                vehicleService.saveVehicle(myVeh);
                //Add allocation to route.
                String schedId = thisElem.getAttribute("route") + "/" + thisElem.getAttribute("schedId");
                for ( int j = 0; j < routeService.getAllRoutes().size(); j++ ) {
                    if ( routeService.getAllRoutes().get(j).getRouteNumber().equalsIgnoreCase(thisElem.getAttribute("route")) ) {
                        String[] dates = document.getDocumentElement().getAttribute("time").split("-");
                        String day = dates[2];
                        if ( day.substring(0,1).equalsIgnoreCase("0") ) { day = day.substring(1,2); }
                        routeService.getAllRoutes().get(j).addAllocation(schedId, myVeh);
                        break;
                    }
                }
                //logger.debug("Adding vehicle with id " + v.getLast().getVehicleID() + " type " + v.getLast().getVehicleType() + " age " + v.getLast().getVehicleAge());
            }
            simulationService.setSimulator(simulator);
            gameService.createGame(game.getPlayerName(), game.getScenarioName(), game.getBalance(), game.getPassengerSatisfaction(), game.getDifficultyLevel());
        }
        catch (Exception e) {
            //logger.debug("Exception!");
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    private String createStringFromList ( List<Integer> list ) {
    	String returnString = "";
    	for ( Integer myInt : list ) {
    		returnString += myInt + ",";
    	}
    	return returnString;
    }
    
    private List<Integer> createListFromString ( String string ) {
    	String[] splitString = string.split(",");
    	List<Integer> list = new ArrayList<Integer>();
    	for ( String stringer : splitString ) {
    		list.add(Integer.parseInt(stringer));
    	}
    	return list;
    }

}
