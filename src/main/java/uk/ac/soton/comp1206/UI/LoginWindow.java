package uk.ac.soton.comp1206.UI;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import uk.ac.soton.comp1206.App;
import uk.ac.soton.comp1206.Utility.Utility;

public class LoginWindow extends Window {
    private final App app;

    private ImageView img;  //Menu image

    public LoginWindow(App app) {
        super("ECS Chat Login", 600, 300);
        this.app = app;

        this.scene.getStylesheets().addAll(Utility.getCSSFile("LoginWindow.css"));
        this.createWindow();
        this.imgAnimation();

    }

    private void createWindow() {
        var hbox = new HBox();

        //Server selection
        var serverList = Utility.getServers();

        //Server drop down menu
        var serverSelect = new ComboBox<String>();
        serverSelect.getItems().addAll(serverList.keySet());
        serverSelect.getSelectionModel().select("ECS: 9500");
        serverSelect.setOnAction(event -> {
            this.app.setServer(serverList.get(serverSelect.getValue()));
        });

        //Login screen image
        this.img = new ImageView(new Image(
            this.getClass().getResource("/images/ECS-Menu.png").toExternalForm(),
             200, 200, false, false
        ));
        this.img.setPreserveRatio(true);
        this.img.setOpacity(0);

        //Welcome message
        var welcomeMsg = new Label("Welcome to ECS Chat!");
        welcomeMsg.setId("welcome-msg");

        //Enter the username
        var username = new TextField();
        username.setId("name-input");

        username.setPromptText("Enter a username");
        username.setFocusTraversable(false);
        username.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                this.handleLogin();
            }
        });

        //Binds username to App class
        username.textProperty().bindBidirectional(this.app.usernameProperty());

        //Login button
        var loginBtn = new Button("Login!");
        loginBtn.setId("login-btn");
        loginBtn.setOnAction(event -> {
            this.handleLogin();
        });

        //Add these to the center
        var vbox = new VBox(welcomeMsg, username, serverSelect, loginBtn);
        vbox.setId("login-page");

        hbox.getChildren().addAll(img, vbox);
        hbox.setAlignment(Pos.CENTER);

        this.root.setCenter(hbox);

    }

    private void handleLogin() {
        if (this.app.getUsername().isBlank()) return;
        app.openChat();
        this.stage.close();
        logger.info("Closing login window.");
    }

    /**
     * Main menu image animation
     */
    private void imgAnimation() {
        var translate = new TranslateTransition(Duration.millis(2500), this.img);
        translate.setFromY(-200);
        translate.setByY(200);
        translate.setFromX(-200);
        translate.setByX(200);

        var fade = new FadeTransition(Duration.millis(2500), this.img);
        fade.setFromValue(0);
        fade.setToValue(1);

        fade.play();
        translate.play();
    }

}
