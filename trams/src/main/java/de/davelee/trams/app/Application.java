package de.davelee.trams.app;

import javax.swing.UIManager;

import de.davelee.trams.gui.NewGameScreen;
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
        /*for ( int i = 12; i > -5; i-- ) {
            try {
                Thread.sleep(200);
                ss.moveImage(10*(i+1),0);
            }
            catch ( InterruptedException ie ) { }
        }
        ss.dispose();
        WelcomeScreen welcomeScreen = context.getBean(WelcomeScreen.class);
        welcomeScreen.displayScreen();*/

        /*GameService gameService = context.getBean(GameService.class);
        GameModel[] gameModels = gameService.getAllGames();
        System.out.println("Got all games!");
        Arrays.toString(gameModels);

        GameModel[] gameModels2 = gameService.getAllGames();
        System.out.println("Got all games 2!");
        Arrays.toString(gameModels2);

        GameController gameController = context.getBean(GameController.class);
        GameModel gameModel = gameController.getGameModel();
        System.out.println("Test gameController method!");*/

		/*GameController gameController = context.getBean(GameController.class);
        GameModel gameModel2 = gameController.createGameModel("Dave Lee", "Landuff Transport Company");
        System.out.println("Balance: " + gameModel2.getBalance());*/

        NewGameScreen newGameScreen = context.getBean(NewGameScreen.class);
        newGameScreen.doInit();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        newGameScreen.saveNewGame(0);

        context.close();
	}
	
}
