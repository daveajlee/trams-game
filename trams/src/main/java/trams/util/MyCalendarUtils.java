package trams.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
	
	/**
     * Get the day extension for a particular day number.
     * @param dayDate a <code>int</code> with the day number.
     * @return a <code>String</code> with the day extension.
     */
    public static String getDateExt ( int dayDate ) {
        if ( dayDate == 1 || dayDate == 21 || dayDate == 31 ) { return "st"; 
        } else if ( dayDate == 2 || dayDate == 22 ) { return "nd"; 
    	} else if ( dayDate == 3 || dayDate == 23 ) { return "rd"; 
    	} else { return "th"; }
    }
    

    /**
     * Get the date info.
     * @return a <code>String</code> with the date info.
     */
    public static String getDateInfo ( Calendar calendar ) {
    	Date myDate = calendar.getTime();
    	SimpleDateFormat myFormatterStart = new SimpleDateFormat("EEEEEEEEE d", new Locale("ENGLISH", "UK"));
    	SimpleDateFormat myFormatterEnd = new SimpleDateFormat("MMMMMMM yyyy", new Locale("ENGLISH", "UK"));
    	myFormatterStart.format(myDate);
    	return myFormatterStart.format(myDate) + getDateExt(calendar.get(Calendar.DATE)) + " " + myFormatterEnd.format(myDate);
    }
    
    /**
     * Get the short date in the form dd/mm/yy.
     * @return a <code>String</code> with the short date info.
     */
    public static String getShortDate ( Calendar calendar ) {
    	Date myDate = calendar.getTime();
    	SimpleDateFormat myFormatter = new SimpleDateFormat("dd/mm/yy");
    	return myFormatter.format(myDate);
    }
    
    /**
     * Get the short year in the form yy.
     * @return a <code>String</code> with the short date info.
     */
    public static String getShortYear ( Calendar calendar ) {
    	Date myDate = calendar.getTime();
    	SimpleDateFormat myFormatter = new SimpleDateFormat("yy");
    	return myFormatter.format(myDate);
    }
    
    /**
     * Get the time info.
     * @return a <code>String</code> with the time info.
     */
    public static String getTimeInfo ( Calendar calendar, boolean ampm ) {
    	Date myDate = calendar.getTime();
    	if ( ampm ) {
    		SimpleDateFormat myFormatter = new SimpleDateFormat("h:mma");
    		return myFormatter.format(myDate);
    	} 
    	else {
    		SimpleDateFormat myFormatter = new SimpleDateFormat("H:mm");
    		return myFormatter.format(myDate);
    	}
    }
    	
}
