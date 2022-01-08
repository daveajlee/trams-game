package de.davelee.trams;

import javax.swing.UIManager;

import de.davelee.trams.controllers.ControllerHandler;
import de.davelee.trams.gui.SplashScreen;
import de.davelee.trams.gui.WelcomeScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * This is the main SpringBoot class to start the application.
 * @author Dave Lee
 */
@SpringBootApplication
@Configuration
@EnableConfigurationProperties
public class TramsGameApplication {

    private static final Logger logger = LoggerFactory.getLogger(TramsGameApplication.class);

    /**
     * Create a RestTemplate which can be autowired into other components.
     * @return a <code>RestTemplate</code> object which can be used by other components.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * Run the program through the main method.
     * @param args a <code>String</code> array of arguments which is currently ignored.
     */
	public static void main ( String[] args ) {
		try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch ( Exception e ) {
            logger.error("Error whilst setting look and feel", e);
        }
        //Display splash screen to the user.
        SpringApplicationBuilder builder = new SpringApplicationBuilder(TramsGameApplication.class);

        builder.headless(false);

        ConfigurableApplicationContext context = builder.run(args);
        ControllerHandler controllerHandler = context.getBean(ControllerHandler.class);
		SplashScreen ss = new SplashScreen(controllerHandler);
        ss.displayScreen(false);
        try {
            Thread.sleep(2000);
        } catch ( InterruptedException ie ) {
            logger.error("Error whilst sleeping thread", ie);
        }
        ss.dispose();
        WelcomeScreen welcomeScreen = new WelcomeScreen(controllerHandler);
        welcomeScreen.displayScreen();
	}
	
}
