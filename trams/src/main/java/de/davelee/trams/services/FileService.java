package de.davelee.trams.services;

import de.davelee.trams.beans.TramsFile;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;

public class FileService {

    //TODO: Log exceptions.
    public boolean saveFile ( File file, TramsFile tramsFile ) {
        try {
            file.createNewFile();
            OutputStream os = new FileOutputStream(file.getName());
            // Save tramsfile to XML
            JAXBContext jc = JAXBContext.newInstance(TramsFile.class);
            Marshaller marshaller = jc.createMarshaller();
            marshaller.marshal(tramsFile, os);
            return true;
        } catch ( Exception exception ) {
            exception.printStackTrace();
            return false;
        }
    }

    //TODO: Log exceptions.
	public TramsFile loadFile ( File file ) {
        try {
            InputStream is = new FileInputStream(file.getName());
            JAXBContext jaxbContext = JAXBContext.newInstance(TramsFile.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            return (TramsFile) jaxbUnmarshaller.unmarshal(is);
        } catch ( Exception exception ) {
            exception.printStackTrace();
            return null;
        }
    }

}
