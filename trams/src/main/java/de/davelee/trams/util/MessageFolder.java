package de.davelee.trams.util;

/**
 * This enum represents the names of folders for messages which can be displayed to the user.
 * @author Dave Lee
 */
public enum MessageFolder {

	/**
	 * Folder called Inbox.
	 */
	INBOX {

		/**
		 * Display the name of the folder which should be seen by the user.
		 * @return a <code>String</code> containing the name of the folder.
		 */
		public String getDisplayName() {
			return "Inbox";
		}
	},

	/**
	 * Folder called Outbox.
	 */
	OUTBOX {

		/**
		 * Display the name of the folder which should be seen by the user.
		 * @return a <code>String</code> containing the name of the folder.
		 */
		public String getDisplayName() {
			return "Outbox";
		}
	},

	/**
	 * Folder called Sent Items.
	 */
	SENT {

		/**
		 * Display the name of the folder which should be seen by the user.
		 * @return a <code>String</code> containing the name of the folder.
		 */
		public String getDisplayName() {
			return "Sent Items";
		}
	},

	/**
	 * Folder called Trash.
	 */
	TRASH {

		/**
		 * Display the name of the folder which should be seen by the user.
		 * @return a <code>String</code> containing the name of the folder.
		 */
		public String getDisplayName() {
			return "Trash";
		}
	};

	/**
	 * Display the name of the folder which should be seen by the user.
	 * @return a <code>String</code> containing the name of the folder.
	 */
	public abstract String getDisplayName();

}


