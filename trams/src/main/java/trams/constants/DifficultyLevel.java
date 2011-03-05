package trams.constants;

public enum DifficultyLevel {

	EASY,
	
	INTERMEDIATE,
	
	MEDIUM,
	
	HARD;
	
	public static DifficultyLevel getDifficultyLevel(String level) {
		if ( level.contentEquals("Intermediate") ) {
			return DifficultyLevel.INTERMEDIATE;
		}
		else if ( level.contentEquals("Medium") ) {
			return DifficultyLevel.MEDIUM;
		}
		else if ( level.contentEquals("Hard") ) {
			return DifficultyLevel.HARD;
		}
		//Default is Easy,
		return DifficultyLevel.EASY;
	}
	
}

