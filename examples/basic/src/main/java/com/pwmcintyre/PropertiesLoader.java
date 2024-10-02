package com.pwmcintyre;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {

    public static Properties loadProperties() throws IOException {
        Properties props = new Properties();
        InputStream inputStream = PropertiesLoader.class
            .getClassLoader()
            .getResourceAsStream("application.properties");
        if (inputStream == null) {
            throw new RuntimeException("no application.properties found");
        }
        props.load(inputStream);
        return props;
    }
}