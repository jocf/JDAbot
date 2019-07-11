package site.giacomo;

import java.io.*;
import java.util.*;

public class PropParser {

    protected String authToken;
    protected String AfkCheckChannel;
    protected String adminChannel;
    protected String voidIcon;
    protected String cultIcon;
    protected String startIcon;

    public void generateProperties() {
        // Creating the properties object.
        Properties properties = new Properties();
        // This is the location of the properties file. It should be in the "root" directory.
        try {
            FileInputStream ip = new FileInputStream("config.properties");
            // We will now load the properties in.
            properties.load(ip);
        } catch (IOException e) {
            System.out.println("Properties failed on startup! Please check config.properties and ensure it is in the correct location!");
            e.printStackTrace();
        }
        authToken = properties.getProperty("authToken");
        AfkCheckChannel = properties.getProperty("afkCheckChannel");
        adminChannel = properties.getProperty("adminChannel");
        voidIcon = properties.getProperty("voidIcon");
        cultIcon = properties.getProperty("cultIcon");
        startIcon = properties.getProperty("startIcon");

    }

    public String getAuthToken(){
        return authToken;
    }

    public String getAfkCheckChannel(){ return AfkCheckChannel; }

    public String getAdminChannel(){
        return adminChannel;
    }

    public String getVoidIcon(){ return voidIcon; }

    public String getCultIcon(){ return cultIcon; }

    public String getStartIcon(){ return startIcon; }

}
