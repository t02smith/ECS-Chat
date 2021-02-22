package uk.ac.soton.comp1206.UI;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import javafx.application.Platform;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import uk.ac.soton.comp1206.App;
import uk.ac.soton.comp1206.User;
import uk.ac.soton.comp1206.Network.Communicator;
import uk.ac.soton.comp1206.UI.Components.ActiveUsers;
import uk.ac.soton.comp1206.UI.Components.Message;
import uk.ac.soton.comp1206.UI.Components.Settings;
import uk.ac.soton.comp1206.UI.Components.Toolbar;
import uk.ac.soton.comp1206.UI.Components.TopBar;
import uk.ac.soton.comp1206.UI.Components.UserHistory;
import uk.ac.soton.comp1206.Utility.Utility;


public class ChatWindow extends Window {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    private final App app;
    private final Communicator communicator;

    private VBox messages;
    private ScrollPane sp;

    private HashMap<String, User> users = new HashMap<>();
    private ActiveUsers activeUsers = new ActiveUsers();
    private Settings settings = new Settings(150);

    private boolean settingsOpen = false;
    private boolean activeUsersOpen = false;

    public ChatWindow (App app, Communicator communicator) {
        super("ECS Chat", 700, 485, () -> {
            Utility.saveMessages();
            logger.info("Closing chat window.");
            System.exit(0);
        });
        this.app = app;
        this.communicator = communicator;

        this.settings.addUsernameListener(name -> {
            this.app.setUsername(name);
        });

        this.settings.addChangeServerListener(server -> {
            this.app.changeServer(server);
        });

        this.communicator.addListener(message -> {
            Platform.runLater(() -> this.receiveMessage(message));
        });

        this.scene.getStylesheets().addAll(
            Utility.getCSSFile("ChatWindow.css"),
            Utility.getCSSFile("MessageStyle.css")
        );

        var topBar = new TopBar();

        topBar.addListener("settings", () -> {
            if (this.settingsOpen) {
                this.settings.slideOut(event -> {
                    this.root.setRight(null);
                });
                //Message window slide right
            } else {
                this.root.setRight(this.settings);
                //message window slide left
                this.settings.slideIn();
            }

            this.settingsOpen = !this.settingsOpen;
        });

        topBar.addListener("users", () -> {
            if (this.activeUsersOpen) this.root.setRight(null);
            else this.root.setRight(this.activeUsers);

            this.activeUsersOpen = !this.activeUsersOpen;
        });

        topBar.addListener("whiteboard", () -> {
            if (!this.app.getDrawWindowStatus()) {
                this.app.openDrawWindow();
            }
        });

        var top = new VBox();
        top.getChildren().addAll(
            this.root.getTop(),
            topBar
        );

        Utility.playAudio("sounds/connected.mp3");
        this.root.setTop(top);
        this.root.setBottom(new Toolbar(this));
        this.root.setCenter(this.createMsgWindow());

    }

    /**
     * Creates the main area for messages to be displayed
     * @return The message window
     */
    private ScrollPane createMsgWindow() {
        this.sp = new ScrollPane();
        this.sp.setId("message-window");

        this.messages = new VBox(5);
        this.sp.setFitToWidth(true);

        this.sp.setContent(this.messages);     
        
        return this.sp;
    }

    /**
     * Displays any message received
     * @param author Who sent the message
     * @param message The content of the message
     * @param isUser Was it the user who sent the message
     */
    private void displayMessage(String author, String message, boolean isUser) {
        var msg = new Message(author, message, isUser);
        msg.addListener(name -> {
            this.displayUserBoard(name);
        });

        this.messages.getChildren().add(msg);
        msg.slideIn();

        this.sp.applyCss();
        this.sp.layout();
        this.sp.setVvalue(1.0);
    }

    /**
     * Overloaded version if it definitely is not the user who sent it
     * @param author
     * @param message
     */
    private void displayMessage(String author, String message) {
        this.displayMessage(author, message, false);
    }

    /**
     * Displays a user's message history
     * @param name The name of the user
     */
    public void displayUserBoard(String name) {
        this.root.setRight(new UserHistory(this, this.users.get(name)));
    }

    /**
     * Close the user history board
     */
    public void closeUserBoard() {
        this.root.setRight(null);
    }
   
    /**
     * Sends a message
     * @param message The content of the message
     */
    public void sendCurrentMessage(String message) {
        if (message.trim().length() == 0) return;
        this.communicator.send(this.app.getUsername() + ":" + message);
    }

    /**
     * Called when a message is received
     * @param message The received message
     */
    public void receiveMessage(String message) {
        var time = formatter.format(LocalDateTime.now());

        //Splits message into author and content
        String[] msgSplit = message.split(":", 2);
        if (msgSplit.length != 2) return;

        //If the user has already sent a message
        if (this.users.containsKey(msgSplit[0])) {
            this.users.get(msgSplit[0]).addMessage(msgSplit[1], time);
        } else { //If it is a new user
            var user = new User(msgSplit[0]);
            user.addMessage(msgSplit[1], time);
            this.users.put(msgSplit[0], user);
            this.activeUsers.addUser(user);
            logger.info("User: '" + msgSplit[0] + "' created.");
        }

        //If the user sent the message
        if (msgSplit[0].equals(this.app.getUsername())) {
            this.displayMessage(msgSplit[0], msgSplit[1], true);
        } else {
            Utility.playAudio("sounds/message.mp3");
            this.displayMessage(msgSplit[0], msgSplit[1]);

            if (msgSplit[1].contains("@"+this.app.getUsername())) {
                System.out.println("test");
            }
        }

        Utility.addMessage(time, msgSplit[0], msgSplit[1]);
        
        this.activeUsers.updateUsers();
    }

}
