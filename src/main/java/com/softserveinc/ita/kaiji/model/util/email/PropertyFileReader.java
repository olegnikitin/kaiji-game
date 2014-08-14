package com.softserveinc.ita.kaiji.model.util.email;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Component
public class PropertyFileReader {

    private static final Logger LOG = Logger.getLogger(PropertyFileReader.class);

    public Properties readMailProperty(String fileName) {

        Properties properties = new Properties();
        try (InputStream input=new FileInputStream(fileName)){
            properties.load(input);
        }catch(IOException ex){
            LOG.error("Unable to read mail properties from file " +
                    fileName + " " + ex.getMessage());
            return null;
        }
        return  properties;
    }
}
