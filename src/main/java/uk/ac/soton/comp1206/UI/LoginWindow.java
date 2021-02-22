package uk.ac.soton.comp1206.UI;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import uk.ac.soton.comp1206.App;
import uk.ac.soton.comp1206.Utility.Utility;

public class LoginWindow  {
    private static final Logger logger = LogManager.getLogger(LoginWindow.class);
    private final App app;

    private Scene scene;
    private ImageView img;

    private BorderPane root;

    final private double defWidth = 600;
    final private double defHeight = 300;

    public LoginWindow(App app) {
        this.app = app;
        this.scene = createWindow();
        this.getStyle();
        logger.info("Login screen created");

        //this.doFade(this.img);
        this.imgAnimation();

    }

    private Scene createWindow() {
        root = new BorderPane();

        var scene = new Scene(root, this.defWidth, this.defHeight);

        var hbox = new HBox();

        //Server selection

        var serverList = Utility.getServers();

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
        img.setPreserveRatio(true);
        img.setOpacity(0);

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

        root.setCenter(hbox);

        //Window
        var windowOptions = new WindowOptions(scene, "ECS Chat Login", () -> {
            logger.info("Closing window");
            System.exit(0);
        });

        this.root.setTop(windowOptions);

        return scene;
    }

    private void handleLogin() {
        if (this.app.getUsername().isBlank()) return;

        app.openChat();
    }

    public void doFade(Node element) {
        FadeTransition fader = new FadeTransition(new Duration(5000), element);
        fader.setFromValue(0);
        fader.setToValue(1);
        fader.play();
    }

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



    private void getStyle() {
        var mainCss = this.getClass().getResource("/style/LoginWindow.css").toExternalForm();
        var common = this.getClass().getResource("/style/Common.css").toExternalForm();

        this.scene.getStylesheets().addAll(mainCss, common);
    }

    @SuppressWarnings("all")
    public Scene getScene() {
        return this.scene;
    }

    public Node getRoot() {
        return this.root;
    }
}
