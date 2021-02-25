package uk.ac.soton.comp1206.Utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class Utility {
    private static final Logger logger = LogManager.getLogger(Utility.class);

    private static SimpleBooleanProperty audioEnabledProperty = new SimpleBooleanProperty(true);

    //Stores all sent/received messages and can produce a file containing them
    private static ArrayList<String> messages = new ArrayList<>();
    private static SimpleBooleanProperty saveMessages = new SimpleBooleanProperty(false);
    private static String absolutePath;

    //Server list
    private static TreeMap<String, String> servers = new TreeMap<>();

    @SuppressWarnings("all")
    public static Image getImage(String url) {
        return new Image(Utility.class.getResource("/images/" + url).toExternalForm());
    }

    //Get style files

    public static String getCSSFile(String name) {
        return Utility.class.getResource("/style/"+name).toExternalForm();
    }

    //Get buffered reader for file

    public static ArrayList<String> getCSVFile(String path) {
        try {
            var reader = new BufferedReader(new FileReader(new File(Utility.class.getResource(path).toURI())));
            var lines = new ArrayList<String>();

            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }

            return lines;
        } catch (IOException | URISyntaxException e) {
            logger.error("Error finding file");
            return null;
        }
    }

    //Get list of available servers

    public static TreeMap<String, String> getServers() {
        if (servers.size() == 0) fillServerList();
        return servers;
    }
    
    public static String getServer(String key) {
        return servers.get(key);
    }

    private static void fillServerList() {
        try {
            var serverList = new BufferedReader(new FileReader(new File(Utility.class.getResource("/config/servers.csv").toURI())));

            String line;
            String[] server;
            while ((line = serverList.readLine()) != null) {
                server = line.split(",");
                servers.put(server[0], server[1]);
            }
        } catch (FileNotFoundException e) {
            logger.error("No servers found");
        } catch (IOException | URISyntaxException e) {
            logger.error("Error reading file.");
        }
        
        //This program will alwyas add the echo web socket
        servers.put("Echo", "ws://echo.websocket.org");
    }

    //Audio

    public static void playAudio(String file) {
        if (audioEnabledProperty.get()) {
            String toPlay = Utility.class.getResource("/" + file).toExternalForm();
            logger.info("Playing audio: " + toPlay);
    
            var play = new Media(toPlay);
            var mediaPlayer = new MediaPlayer(play);
            mediaPlayer.play();
        } else {
            logger.info("Audio is disabled.");
        }
        
    }

    public static void setAudioEnabled(boolean set) {
        audioEnabledProperty.set(set);
    }

    public static SimpleBooleanProperty audioEnabledProperty() {
        return audioEnabledProperty;
    }

    public static boolean getAudioEnabled() {
        return audioEnabledProperty.get();
    }

    //Save messages

    public static void addMessage(String time, String author, String content) {
        var message = String.format("%-5s %-12s %s", time, author, content);
        messages.add(message);
    }

    public static void setSaveLocation() {
        var chooser = new DirectoryChooser();
        chooser.setTitle("Select save location");

        absolutePath = chooser.showDialog(new Stage()).getAbsolutePath();
        logger.info("Messages will be saved in: " + absolutePath);
    }

    public static void saveMessages() {
        if (!saveMessages.get()) return;

        var title = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

        if (absolutePath.isEmpty()) setSaveLocation();

        try {
            var file = new FileWriter(absolutePath + "/" + title + ".txt");
            file.write(String.format("%-5s %-12s %s\n\n", "Time", "User", "Message"));

            for (String msg: messages) {
                file.write(msg + "\n");
            }
            file.close();
            logger.info("Messages saved.");
        } catch (IOException e) {
            logger.error(e);
        }

    }

    public static boolean getSaveMessages() {
        return saveMessages.get();
    }

    public static void setSaveMessagesProperty(boolean set) {
        saveMessages.set(true);
    }

    public static SimpleBooleanProperty saveMessageProperty() {
        return saveMessages;
    }

}
