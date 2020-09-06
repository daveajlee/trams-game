package de.davelee.trams.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.davelee.trams.beans.TramsFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.IOException;

@Service
public class FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileService.class);

    public boolean saveFile (File file, TramsFile tramsFile ) {
        //Output json.
        try {
            if ( file.exists() ) {
                file.delete();
            }
            if ( file.createNewFile() ) {
                final ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule());
                mapper.writeValue(file, tramsFile);
                return true;
            }
            return false;
        } catch ( IOException ioException) {
            logger.error("Failure converting to json: " + ioException);
            return false;
        }
    }

	public TramsFile loadFile ( File file ) {
        //Define json importer.
        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file.getAbsolutePath())));
            StringBuilder out = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);   // add everything to StringBuilder
            }
            return mapper.readValue(out.toString(), TramsFile.class);
        } catch ( Exception exception ) {
            logger.error("exception whilst loading file", exception);
            return null;
        }
    }

}
