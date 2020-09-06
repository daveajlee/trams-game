package de.davelee.trams.app;

import javax.swing.UIManager;

import de.davelee.trams.controllers.ControllerHandler;
import de.davelee.trams.gui.SplashScreen;
import de.davelee.trams.gui.WelcomeScreen;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@SpringBootApplication
public class Application {
	
	public static void main ( String[] args ) {
		try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        }
        catch ( Exception e ) { }
        //Display splash screen to the user.
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("de/davelee/trams/spring/trams-context.xml");
        ControllerHandler controllerHandler = context.getBean(ControllerHandler.class);
		SplashScreen ss = new SplashScreen(controllerHandler);
        ss.displayScreen(false);
        for ( int i = 12; i > -5; i-- ) {
            try {
                Thread.sleep(200);
                ss.moveImage(10*(i+1),0);
            }
            catch ( InterruptedException ie ) { }
        }
        ss.dispose();
        WelcomeScreen welcomeScreen = new WelcomeScreen(controllerHandler);
        welcomeScreen.displayScreen();
	}
	
}
