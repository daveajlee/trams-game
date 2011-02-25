package trams.tramsdb;

import org.hibernate.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import trams.data.*;
import trams.util.HibernateUtil;

import java.util.*;

public class DatabaseManager {
	
	private static final double DEPRECIATION_FACTOR = 0.006;

    public static void main(String[] args) {
        DatabaseManager mgr = new DatabaseManager();

        mgr.createAndStoreDriver("Dave Lee", 40, Calendar.getInstance());
        mgr.createAndStoreStop("Rathaus Pankow", Calendar.getInstance());
        mgr.createAndStoreService();
        mgr.createAndStoreServicePattern("Mon-Fri","2,3,4,5,6","Rathaus Pankow","S + U Pankow",Calendar.getInstance(),Calendar.getInstance(),15,3);
        //ServicePattern sp = new ServicePattern("Mon-Fri","2,3,4,5,6","Rathaus Pankow","S + U Pankow",Calendar.getInstance(),Calendar.getInstance(),15,3);
        //mgr.createAndStoreTimetable("myTimetable", Calendar.getInstance(), Calendar.getInstance(), sp);
        //mgr.createAndStoreRouteSchedule("2A", 1, 5);
        //mgr.createAndStoreVehicle("CV58 2XD", Calendar.getInstance(), 0.006, "image.png", "Mercedes", 40, 60, 200.99);
        mgr.createAndStoreRoute();
        
        Map<String, Integer> distances = new HashMap<String, Integer>();
        distances.put("Pankow Kirche", new Integer(4));
        distances.put("Rathaus Pankow", new Integer(7));
        mgr.createAndStoreDistances("S+U Pankow", distances);
        
        List<Route> routes = new ArrayList<Route>();
        routes.add(mgr.createAndStoreRoute());
        List<Vehicle> vehicles = new ArrayList<Vehicle>();
        vehicles.add(mgr.createAndStoreVehicle("CV58 2XD", Calendar.getInstance(), DEPRECIATION_FACTOR, "image.png", "Mercedes", 40, 60, 200.99));
        List<Driver> drivers = new ArrayList<Driver>();
        drivers.add(mgr.createAndStoreDriver("Dave Lee", 40, Calendar.getInstance()));
        mgr.createAndStoreScenarios(routes, vehicles, drivers, 100, "Dave Lee", 20000.00);
        
        //mgr.createAndStoreService();
        mgr.createAndStoreMessage("Subject", "Text", "Sender", "Folder", "Date");
        
        HibernateUtil.getSessionFactory().close();
    }

    private Driver createAndStoreDriver(String name, int contractedHours, Calendar startDate) {
        //Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        //session.beginTransaction();

        Driver theDriver = new Driver();
        theDriver.setName(name);
        theDriver.setContractedHours(contractedHours);
        theDriver.setStartDate(startDate);
        //session.save(theDriver);

        //session.getTransaction().commit();
        
        return theDriver;
    }
    
    private void createAndStoreStop(String name, Calendar time) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        Stop theStop = new Stop();
        theStop.setStopName(name);
        theStop.setStopTime(time);
        session.save(theStop);

        session.getTransaction().commit();
    }
    
    private void createAndStoreService() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        Service theService = new Service();
        theService.addStop(new Stop("Rathaus Pankow", Calendar.getInstance()));
        theService.addStop(new Stop("Pankow Kirche", Calendar.getInstance()));
        theService.addStop(new Stop("S + U Pankow", Calendar.getInstance()));
        session.save(theService);

        session.getTransaction().commit();
    }
    
    private void createAndStoreServicePattern(String name, String daysOfOperation, String returnTerminus, String outgoingTerminus, Calendar startTime, Calendar endTime, int frequency, int routeDuration) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        ServicePattern theServicePattern = new ServicePattern();
        theServicePattern.setName(name);
        theServicePattern.setDaysOfOperation(daysOfOperation);
        theServicePattern.setReturnTerminus(returnTerminus);
        theServicePattern.setOutgoingTerminus(outgoingTerminus);
        theServicePattern.setStartTime(startTime);
        theServicePattern.setEndTime(endTime);
        theServicePattern.setFrequency(frequency);
        theServicePattern.setRouteDuration(routeDuration);
        session.save(theServicePattern);

        session.getTransaction().commit();
    }
    
    private Timetable createAndStoreTimetable(String name, Calendar validFromDate, Calendar validToDate, ServicePattern servicePattern) {
        //Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        //session.beginTransaction();

        Timetable theTimetable = new Timetable();
        theTimetable.addServicePattern(servicePattern.getName(), servicePattern);
        theTimetable.setName(name);
        theTimetable.setValidFromDate(validFromDate);
        theTimetable.setValidToDate(validToDate);
        
        return theTimetable;
        //session.save(theTimetable);

        //session.getTransaction().commit();
    }
    
    private RouteSchedule createAndStoreRouteSchedule(String routeNumber, int scheduleNumber, int delayInMins) {
        //Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        //session.beginTransaction();

        Service theService = new Service();
        theService.addStop(new Stop("Rathaus Pankow", Calendar.getInstance()));
        theService.addStop(new Stop("Pankow Kirche", Calendar.getInstance()));
        theService.addStop(new Stop("S + U Pankow", Calendar.getInstance()));
        RouteSchedule theRouteSchedule = new RouteSchedule();
        theRouteSchedule.addService(theService);
        theRouteSchedule.setRouteNumber(routeNumber);
        theRouteSchedule.setScheduleNumber(scheduleNumber);
        theRouteSchedule.setDelayInMins(delayInMins);
        return theRouteSchedule;
        //session.save(theRouteSchedule);

        //session.getTransaction().commit();
    }
    
    private Vehicle createAndStoreVehicle(String registrationNumber, Calendar deliveryDate, double depreciationFactor, String imagePath, String model, int seatingNum, int standingNum, double purchasePrice) {
        //Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        //session.beginTransaction();

        Service theService = new Service();
        theService.addStop(new Stop("Rathaus Pankow", Calendar.getInstance()));
        theService.addStop(new Stop("Pankow Kirche", Calendar.getInstance()));
        theService.addStop(new Stop("S + U Pankow", Calendar.getInstance()));
        RouteSchedule theRouteSchedule = new RouteSchedule();
        theRouteSchedule.addService(theService);
        theRouteSchedule.setRouteNumber("2A");
        theRouteSchedule.setScheduleNumber(1);
        theRouteSchedule.setDelayInMins(5);
        //session.save(theRouteSchedule);
        
        Vehicle theVehicle = new Vehicle();
        theVehicle.setRegistrationNumber(registrationNumber);
        theVehicle.setAssignedSchedule(theRouteSchedule);
        theVehicle.setDeliveryDate(deliveryDate);
        theVehicle.setDepreciationFactor(depreciationFactor);
        theVehicle.setImagePath(imagePath);
        theVehicle.setModel(model);
        theVehicle.setSeatingNum(seatingNum);
        theVehicle.setStandingNum(standingNum);
        theVehicle.setPurchasePrice(purchasePrice);
        
        return theVehicle;
        //session.save(theVehicle);

        //session.getTransaction().commit();
    }
    
    private Route createAndStoreRoute ( ) {
    	//Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        //session.beginTransaction();
        
        Route theRoute = new Route();
        theRoute.setRouteNumber("155");
        theRoute.addStop("Rathaus Pankow", Route.OUTWARDSTOPS);
        theRoute.addStop("Pankow Kirche", Route.OUTWARDSTOPS);
        theRoute.addStop("S + U Pankow", Route.OUTWARDSTOPS);
        theRoute.addStop("S + U Pankow", Route.RETURNSTOPS);
        theRoute.addStop("Pankow Kirche", Route.RETURNSTOPS);
        theRoute.addStop("Rathaus Pankow", Route.RETURNSTOPS);
        List<RouteSchedule> theRouteSchedulesList = new ArrayList<RouteSchedule>();
        theRouteSchedulesList.add(createAndStoreRouteSchedule("155", 1, 0));
        theRoute.setRouteSchedules(theRouteSchedulesList);
        theRoute.addAllocation(theRouteSchedulesList.get(0).toString(), createAndStoreVehicle("CV58 2XD", Calendar.getInstance(), DEPRECIATION_FACTOR, "image.png", "Mercedes", 40, 60, 200.99));
        ServicePattern sp = new ServicePattern("Mon-Fri","2,3,4,5,6","Rathaus Pankow","S + U Pankow",Calendar.getInstance(),Calendar.getInstance(),15,3);
        theRoute.addTimetable("myTimetable", createAndStoreTimetable("myTimetable", Calendar.getInstance(), Calendar.getInstance(), sp));
        
        //session.save(theRoute);
        //session.getTransaction().commit();
        return theRoute;
    }
    
    private void createAndStoreDistances ( String name, Map<String, Integer> distances ) {
    	Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        Distances theDistances = new Distances();
        theDistances.setStopName(name);
        theDistances.setDistanceTimes(distances);
        
        session.save(theDistances);
        session.getTransaction().commit();
    }
    
    private void createAndStoreScenarios ( List<Route> routes, List<Vehicle> vehicles, List<Driver> drivers, int passengerSatisfaction, String playerName, double balance ) {
    	Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        ApplicationContext theContext = new ClassPathXmlApplicationContext("trams/data/context.xml");
        Scenario theScenario = (Scenario) theContext.getBean("LanduffScenario");
        theScenario.setPlayerName(playerName);
        theScenario.setBalance(balance);
        theScenario.setPassengerSatisfaction(passengerSatisfaction);
        theScenario.setRoutes(routes);
        theScenario.setVehicles(vehicles);
        theScenario.setDrivers(drivers);
        
        session.save(theScenario);
        session.getTransaction().commit();
    }
    
    public void createAndStoreMessage ( String subject, String text, String sender, String folder, String date ) {
    	Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        ApplicationContext theContext = new ClassPathXmlApplicationContext("trams/spring/context.xml");
        Message theMessage = (Message) theContext.getBean("CouncilMessage");
        theMessage.setSubject(subject);
        theMessage.setText(text);
        theMessage.setSender(sender);
        theMessage.setFolder(folder);
        theMessage.setDate(date);
        System.out.println(theMessage.getType());
        
        session.save(theMessage);
        session.getTransaction().commit();
    }
    
}