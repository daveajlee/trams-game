package trams.main;

import java.util.Calendar;

public class MyCalendarUtils {
	
	private static final int NUM_MONTHS = 12;

	/**
	 * Return the difference between Calendar c1 and c2. Is greater than 0 iff c1 is after c2.
	 * @param c1
	 * @param c2
	 * @return
	 */
	public static int getDiff ( Calendar c1, Calendar c2 ) {
		int yearDiff = Math.abs(c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR));
        int monthDiff = Math.abs(c1.get(Calendar.MONTH) - c2.get(Calendar.MONTH));
        int totalDiff = (yearDiff * NUM_MONTHS) + monthDiff;
        return totalDiff;
	}
	
}
