package de.davelee.trams.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public enum DateFormats {
	
	FULL_FORMAT {
		public DateFormat getFormat() {
			return DateFormat.getDateInstance(DateFormat.FULL, Locale.UK);
		}
	},
	FULL_TIME_FORMAT {
		public DateFormat getFormat() {
			return DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.LONG, Locale.UK);
		}
	},
	TIME_FORMAT {
		public DateFormat getFormat() {
			return new SimpleDateFormat("h:ma");
		}
	},
	HOUR_MINUTE_FORMAT {
		public DateFormat getFormat() {
			return new SimpleDateFormat("HH:mm");
		}
	},
	SHORT_YEAR_FORMAT {
		public DateFormat getFormat() {
			return new SimpleDateFormat("yy");
		}
	},
	SHORT_FORMAT {
		public DateFormat getFormat() {
			return DateFormat.getDateInstance(DateFormat.SHORT, Locale.UK);
		}
	},
	DAY_MONTH_YEAR_FORMAT {
		public DateFormat getFormat() {
			return new SimpleDateFormat("d MMMM yyyy");
		}
	},
	MONTH_YEAR_FORMAT {
		public DateFormat getFormat() {
			return new SimpleDateFormat("MMMM yyyy");
		}
	};
	
	public abstract DateFormat getFormat();

}
