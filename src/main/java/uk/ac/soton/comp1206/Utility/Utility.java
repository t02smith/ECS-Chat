package uk.ac.soton.comp1206.Utility;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

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
    private static HashMap<String, String> servers = new HashMap<>();

    @SuppressWarnings("all")
    public static Image getImage(String url) {
        return new Image(Utility.class.getResource("/images/" + url).toExternalForm());
    }

    //Get list of available servers

    public static HashMap<String, String> getServers() {
        if (servers.size() == 0) fillServerList();
        return servers;
    }

    private static void fillServerList() {
        servers.put("Echo", "ws://echo.websocket.org");
        servers.put("ECS: 9500", "ws://discord.ecs.soton.ac.uk:9500");
        servers.put("ECS: 9501", "ws://discord.ecs.soton.ac.uk:9501");
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

    public static void setSaveLocation(Stage stage) {
        var chooser = new DirectoryChooser();
        chooser.setTitle("Select save location");

        absolutePath = chooser.showDialog(stage).getAbsolutePath();
        logger.info("Messages will be saved in: " + absolutePath);
    }

    public static void saveMessages(Stage stage) {
        if (!saveMessages.get()) return;

        var title = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

        if (absolutePath.isEmpty()) setSaveLocation(stage);

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
