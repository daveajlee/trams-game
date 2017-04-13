package de.davelee.trams.util;

public enum MessageFolder {

	INBOX {
		public String getDisplayName() {
			return "Inbox";
		}
	},
	OUTBOX {
		public String getDisplayName() {
			return "Outbox";
		}
	},
	SENT {
		public String getDisplayName() {
			return "Sent Items";
		}
	},
	TRASH {
		public String getDisplayName() {
			return "Trash";
		}
	};

	public abstract String getDisplayName();

	public static MessageFolder getFolderEnum ( final String displayName ) {
		if ( displayName.equalsIgnoreCase("Inbox") ) {
			return INBOX;
		} else if ( displayName.equalsIgnoreCase("Outbox")) {
			return OUTBOX;
		} else if ( displayName.equalsIgnoreCase("Sent Items")) {
			return SENT;
		} else if ( displayName.equalsIgnoreCase("Trash")) {
			return TRASH;
		} else {
			return null;
		}
	}

}


