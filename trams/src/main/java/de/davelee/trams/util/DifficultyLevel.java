package de.davelee.trams.util;

public enum DifficultyLevel {
	
	EASY {
		public String getName() {
			return "Easy";
		}
		public String getDescription() {
			return "Minimal delays requiring little intervention.";
		}
	},
	INTERMEDIATE {
		public String getName() {
			return "Intermediate";
		}
		public String getDescription() {
			return "Minimal delays requiring intervention.";
		}
	},
	MEDIUM {
		public String getName() {
			return "Medium";
		}
		public String getDescription() {
			return "Frequent delays requiring occasional intervention.";
		}
	},
	HARD {
		public String getName() {
			return "Hard";
		}
		public String getDescription() {
			return "Regular substantial delays.";
		}
	};
	
	public abstract String getName();
	
	public abstract String getDescription();
	
}
