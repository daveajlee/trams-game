package de.davelee.trams.services;

import de.davelee.trams.beans.TramsFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;

@Service
public class FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileService.class);

    public boolean saveFile ( File file, TramsFile tramsFile ) {
        try {
            file.createNewFile();
            // Save tramsfile to XML
            JAXBContext jc = JAXBContext.newInstance(TramsFile.class);
            Marshaller marshaller = jc.createMarshaller();
            marshaller.marshal(tramsFile, file);
            return true;
        } catch ( Exception exception ) {
            logger.error("exception whilst saving file", exception);
            return false;
        }
    }

	public TramsFile loadFile ( File file ) {
        try {
            InputStream is = new FileInputStream(file.getAbsolutePath());
            JAXBContext jaxbContext = JAXBContext.newInstance(TramsFile.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            return (TramsFile) jaxbUnmarshaller.unmarshal(is);
        } catch ( Exception exception ) {
            logger.error("exception whilst loading file", exception);
            return null;
        }
    }

}
