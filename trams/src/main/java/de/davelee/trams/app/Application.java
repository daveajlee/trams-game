package de.davelee.trams.app;

import javax.swing.UIManager;

import de.davelee.trams.gui.SplashScreen;
import de.davelee.trams.gui.WelcomeScreen;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Application {
	
	public static void main ( String[] args ) {
		try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        }
        catch ( Exception e ) { }
        //Display splash screen to the user.
        SplashScreen ss = new SplashScreen(false);
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("de/davelee/trams/spring/trams-context.xml");
        for ( int i = 12; i > -5; i-- ) {
            try {
                Thread.sleep(200);
                ss.moveImage(10*(i+1),0);
            }
            catch ( InterruptedException ie ) { }
        }
        ss.dispose();
        WelcomeScreen welcomeScreen = context.getBean(WelcomeScreen.class);
        welcomeScreen.displayScreen();
	}
	
}
