package uk.ac.soton.comp1206;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.stage.Stage;
import uk.ac.soton.comp1206.Network.Communicator;
import uk.ac.soton.comp1206.UI.ChatWindow;
import uk.ac.soton.comp1206.UI.DrawWindow;
import uk.ac.soton.comp1206.UI.LoginWindow;
import uk.ac.soton.comp1206.UI.TileClickerWindow;
import uk.ac.soton.comp1206.Utility.Utility;

/**
 * JavaFX App
 */
public class App extends Application {

    private static final Logger logger = LogManager.getLogger(App.class);
    private Communicator communicator;

    private boolean drawWindowOpen = false;
    private boolean tileWindowOpen = false;

    final private String defaultServer = "Echo";

    private SimpleStringProperty username = new SimpleStringProperty("");
    private SimpleStringProperty server = new SimpleStringProperty(Utility.getServer(defaultServer));

    //List of users
    private HashMap<String, User> users = new HashMap<>();

    @Override
    public void start(Stage stage) {
        this.openLogin();
    }

    public static void main(String[] args) {
        logger.info("Starting client");
        launch();
    }

    /**
     * Opens the login window
     */
    public void openLogin() {
        logger.info("Opening Login Window");
        new LoginWindow(this);
    }

    /**
     * Opens the chat window
     */
    public void openChat() {
        logger.info("Opening chat window.");
        this.communicator = new Communicator(server.get());
        new ChatWindow(this, this.communicator);
    }

    public void shutdown() {
        logger.info("Shutting down");
        System.exit(0);
    }

    //Open draw window

    public void openDrawWindow() {
        logger.info("Opening Draw Window");
        new DrawWindow(this, this.communicator);
        this.drawWindowOpen = true;
    }


    //Open tile window

    public void openTileWindow() {
        logger.info("Opening Tile Window");
        new TileClickerWindow(this, this.communicator);
        this.tileWindowOpen = true;
    }

    //Changing server

    public void changeServer(String newServer) {
        logger.info("Changing server to: {}", newServer);
        this.communicator.connect(newServer);
    }

    //Users

    public void addUser(User user) {
        var name = user.getName();
        if (this.users.containsKey(name)) {
            logger.error("Username {} already exists. Overwriting user.", name);
        }

        this.users.put(name, user);
    }

    /**
     * Creates a new user by name
     * @param name
     */
    public void addUser(String name) {
        this.addUser(new User(name));
    }

    public User getUser(String name) {
        return this.users.get(name);
    }

    //Username property

    public void setUsername(String newUsername) {
        this.username.set(newUsername);
    }

    public String getUsername() {
        return this.username.get();
    }

    public SimpleStringProperty usernameProperty() {
        return this.username;
    }

    //Server property

    public void setServer(String newServer) {
        this.server.set(newServer);
    }

    public String getServer() {
        return server.get();
    }

    public SimpleStringProperty serverProperty() {
        return server;
    }

    //Whiteboard
    public boolean getDrawWindowStatus() {
        return this.drawWindowOpen;
    }

    public void setDrawWindowStatus(boolean status) {
        this.drawWindowOpen = status;
    }

    //Tile game window

    public boolean getTileWindowStatus() {
        return this.tileWindowOpen;
    }

    public void setTileWindowStatus(boolean status) {
        this.tileWindowOpen = status;
    }

}