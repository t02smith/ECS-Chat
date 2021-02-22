package uk.ac.soton.comp1206;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import uk.ac.soton.comp1206.Network.Communicator;
import uk.ac.soton.comp1206.UI.ChatWindow;
import uk.ac.soton.comp1206.UI.DrawWindow;
import uk.ac.soton.comp1206.UI.LoginWindow;
import uk.ac.soton.comp1206.UI.Components.wbComponents.Whiteboard;
import uk.ac.soton.comp1206.Utility.Utility;

/**
 * JavaFX App
 */
public class App extends Application {

    private static final Logger logger = LogManager.getLogger(App.class);
    private Communicator communicator;
    private Stage stage;

    private Stage wbWindow;
    private boolean whiteboardOpen = false;

    private SimpleStringProperty username = new SimpleStringProperty();
    private SimpleStringProperty server = new SimpleStringProperty("ws://discord.ecs.soton.ac.uk:9500");

    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    @SuppressWarnings("all")
    public void start(Stage stage) {
        this.stage = stage;

        stage.setTitle("ECS Chat");
        stage.setOnCloseRequest(event -> {
            Utility.saveMessages(stage);
            this.shutdown();
        });
        stage.getIcons().add(Utility.getImage("ECS.png"));

        this.stage.initStyle(StageStyle.UNDECORATED);

        this.openLogin();
    }

    public static void main(String[] args) {
        logger.info("Starting client");
        launch();
    }

    public void openWhiteboard() {
        logger.info("Starting whiteboard");

        this.wbWindow = new Stage();
        this.wbWindow.setTitle("ECS Draw");
        this.wbWindow.setOnCloseRequest(event -> {
            this.setWhiteboardStatus(false);
            this.wbWindow.close();
            logger.info("Whiteboard closing");
        });

        this.wbWindow.getIcons().add(Utility.getImage("ECS.png"));
        this.wbWindow.initStyle(StageStyle.UNDECORATED);

        var whiteboard = new DrawWindow(this, this.communicator);
        this.wbWindow.setScene(whiteboard.getScene());

        var root = whiteboard.getRoot();
        root.setOnMousePressed(event -> {
            if (!(event.getTarget() instanceof Whiteboard)) {
                whiteboard.setXOffset(event.getSceneX());
                whiteboard.setYOffset(event.getSceneY());
            }
        });

        root.setOnMouseDragged(event -> {
            if (!(event.getTarget() instanceof Whiteboard)) {
                this.wbWindow.setX(event.getScreenX() - whiteboard.getXOffset());
                this.wbWindow.setY(event.getScreenY() - whiteboard.getYOffset());
            }
            
        });

        this.wbWindow.centerOnScreen();
        this.wbWindow.show();
    }

    public void closeWhiteboard() {
        this.wbWindow.close();
    }

    public void openLogin() {
        logger.info("Opening Login Window");
        var window = new LoginWindow(this);

        this.stage.setScene(window.getScene());

        var root = window.getRoot();
        root.setOnMousePressed(event -> {
            this.xOffset = event.getSceneX();
            this.yOffset = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            this.stage.setX(event.getScreenX() - this.xOffset);
            this.stage.setY(event.getScreenY() - this.yOffset);
        });

        this.stage.show();
        this.stage.centerOnScreen();
    }

    public void openChat() {
        logger.info("Opening chat window");
        this.communicator = new Communicator(server.get());
        var window = new ChatWindow(this, this.communicator);

        this.stage.setScene(window.getScene());

        var root = window.getRoot();
        root.setOnMousePressed(event -> {
            this.xOffset = event.getSceneX();
            this.yOffset = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            this.stage.setX(event.getScreenX() - this.xOffset);
            this.stage.setY(event.getScreenY() - this.yOffset);
        });

        this.stage.show();
        this.stage.centerOnScreen();
    }

    public void shutdown() {
        logger.info("Shutting down");
        System.exit(0);
    }

    //Changing server

    public void changeServer(String newServer) {
        logger.info("Changing server to: {}", newServer);
        this.communicator.connect(newServer);
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
    public boolean getWhiteboardOpen() {
        return this.whiteboardOpen;
    }

    public void setWhiteboardStatus(boolean status) {
        this.whiteboardOpen = status;
    }

}