package com.ebay.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertyManager {

    protected Properties property;

    public PropertyManager(String path) throws IOException {

        property = new Properties();
        FileInputStream configFile = new FileInputStream(path);
        property.load(configFile);

    }

    public String getUrl(){

        return property.getProperty("url");
    }

    public String getBrowser(){

        return property.getProperty("browser");
    }


}
