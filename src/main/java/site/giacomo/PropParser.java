package site.giacomo;

import java.io.*;
import java.util.*;

public class PropParser {

    protected String authToken;
    protected String AfkCheckChannel;
    protected String adminChannel;
    protected String verifyChannel;
    protected String voidIcon;
    protected String cultIcon;
    protected String startIcon;
    protected String stopIcon;
    protected String startName;
    protected String stopName;
    protected String voidName;
    protected String cultName;


    protected List<String> icons = new ArrayList<>();
    public void generateProperties() {
        // Creating the properties object.
        Properties properties = new Properties();
        // This is the location of the properties file. It should be in the "root" directory.
        try {
            FileInputStream in = new FileInputStream("C:\\Users\\Giacomo\\Dropbox\\Programming\\JDAbot\\build\\libs\\config.properties");
            // We will now load the properties in.
            properties.load(in);
        } catch (IOException e) {
            System.out.println("Properties failed on startup! Please check config.properties and ensure it is in the correct location!");
            e.printStackTrace();
        }
        /*
        The way in which icons are added to the icon list could easily be upgraded in future to allow for user-specified icon expansion through the
        configuration file but for the use of the bot as it stands this is fairly unnecessary and also resource intensive.
         */

        authToken = properties.getProperty("authToken");
        AfkCheckChannel = properties.getProperty("afkCheckChannel");
        adminChannel = properties.getProperty("adminChannel");
        verifyChannel = properties.getProperty("verifyChannel");
        voidIcon = properties.getProperty("voidIcon");
        icons.add(voidIcon);
        cultIcon = properties.getProperty("cultIcon");
        icons.add(cultIcon);
        startIcon = properties.getProperty("startIcon");
        icons.add(startIcon);
        stopIcon = properties.getProperty("stopIcon");
        icons.add(stopIcon);
        startName = properties.getProperty("startName");
        stopName = properties.getProperty("stopName");
        cultName = properties.getProperty("cultName");
        voidName = properties.getProperty("voidName");


    }

    public void setAdminChannel(String newChannel) throws IOException{
        FileInputStream in = new FileInputStream("filepathhere");
        Properties properties = new Properties();
        properties.load(in);
        in.close();

        FileOutputStream out = new FileOutputStream("filepathhere");
        properties.setProperty("adminChannel", newChannel);
        properties.store(out, null);
        out.close();
    }

    public void setAfkCheckChannel(String newChannel) throws IOException {
        FileInputStream in = new FileInputStream("filepathhere");
        Properties properties = new Properties();
        properties.load(in);
        in.close();

        FileOutputStream out = new FileOutputStream("filepathhere");
        properties.setProperty("afkCheckChannel", newChannel);
        properties.store(out, null);
        out.close();
    }

    public void setVerifyChannel (String newChannel) throws IOException {
        FileInputStream in = new FileInputStream("filepathhere");
        Properties properties = new Properties();
        properties.load(in);
        in.close();

        FileOutputStream out = new FileOutputStream("filepathhere");
        properties.setProperty("verifyChannel", newChannel);
        properties.store(out, null);
        out.close();
    }

    public String getAuthToken(){
        return authToken;
    }

    public String getAfkCheckChannel(){ return AfkCheckChannel; }

    public String getVerifyChannel() { return verifyChannel; }

    public String getAdminChannel(){
        return adminChannel;
    }

    public String getVoidIcon(){ return voidIcon; }

    public String getCultIcon(){ return cultIcon; }

    public String getStartIcon(){ return startIcon; }

    public String getStopIcon(){ return stopIcon; }

    public String getStopName() { return stopName; }

    public String getStartName() { return startName; }

    public String getVoidName() { return voidName; }

    public String getCultName() { return cultName; }

    public List<String> getIcons(){ return icons; }

}
