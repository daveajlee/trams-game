package trams.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class FileUtils {

	public static ArrayList<String> readFile ( String path ) {
		ArrayList<String> fileContents = new ArrayList<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			String nextLine;
			while ( (nextLine = reader.readLine()) != null ) {
				fileContents.add(nextLine);
			}
		} catch ( Exception e ) {
			e.printStackTrace();
			return null;
		}
		return fileContents;
	}
	
}
