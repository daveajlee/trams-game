package trams.tasks;

import java.util.TimerTask;

import trams.gui.SplashScreen;

public class SplashScreenImageTask extends TimerTask {
	
	private SplashScreen splashScreen;
	
	public SplashScreen getSplashScreen() {
		return splashScreen;
	}

	public void setSplashScreen(SplashScreen splashScreen) {
		this.splashScreen = splashScreen;
	}

	private int position;
	
	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	private int[] positions = new int[] { 130, 120, 110, 100, 90, 80, 70, 60, 50, 40, 30, 20, 10, 0, -10, -20, -30, -40 };
	
	public void run() {
		if ( !splashScreen.getStarted() ) { splashScreen.dispose(); return; }
		splashScreen.moveImage(positions[position], 0);
		if ( position == (positions.length-1) ) {
			splashScreen.setFinished();
			splashScreen.dispose();
			this.cancel();
		}
		position++;
		
	}

}
