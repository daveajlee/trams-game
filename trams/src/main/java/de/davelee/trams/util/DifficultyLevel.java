package de.davelee.trams.util;

/**
 * This enum represents the difficulty of the game which can be either EASY, INTERMEDIATE, MEDIUM or HARD.
 * @author Dave Lee
 */
public enum DifficultyLevel {

	/**
	 * Easy level - very few delays.
	 */
	EASY {
		/**
		 * Return the value of this enum.
		 * @return a <code>String</code> with the exact name of this value of the enum.
		 */
		public String getName() {
			return "Easy";
		}

		/**
		 * Return a text description of this enum which can be manually set.
		 * @return a <code>String</code> with the text description of this enum.
		 */
		public String getDescription() {
			return "Minimal delays requiring little intervention.";
		}
	},

	/**
	 * Intermediate level - very few delays but need intervention to avoid more delays.
	 */
	INTERMEDIATE {
		/**
		 * Return the value of this enum.
		 * @return a <code>String</code> with the exact name of this value of the enum.
		 */
		public String getName() {
			return "Intermediate";
		}

		/**
		 * Return a text description of this enum which can be manually set.
		 * @return a <code>String</code> with the text description of this enum.
		 */
		public String getDescription() {
			return "Minimal delays requiring intervention.";
		}
	},

	/**
	 * Medium level - frequent delays but minimal intervention needed.
	 */
	MEDIUM {
		/**
		 * Return the value of this enum.
		 * @return a <code>String</code> with the exact name of this value of the enum.
		 */
		public String getName() {
			return "Medium";
		}

		/**
		 * Return a text description of this enum which can be manually set.
		 * @return a <code>String</code> with the text description of this enum.
		 */
		public String getDescription() {
			return "Frequent delays requiring occasional intervention.";
		}
	},

	/**
	 * Hard level - frequent delays and regular intervention needed.
	 */
	HARD {
		/**
		 * Return the value of this enum.
		 * @return a <code>String</code> with the exact name of this value of the enum.
		 */
		public String getName() {
			return "Hard";
		}

		/**
		 * Return a text description of this enum which can be manually set.
		 * @return a <code>String</code> with the text description of this enum.
		 */
		public String getDescription() {
			return "Regular substantial delays.";
		}
	};

	/**
	 * Return the value of this enum.
	 * @return a <code>String</code> with the exact name of this value of the enum.
	 */
	public abstract String getName();

	/**
	 * Return a text description of this enum which can be manually set.
	 * @return a <code>String</code> with the text description of this enum.
	 */
	public abstract String getDescription();
	
}
