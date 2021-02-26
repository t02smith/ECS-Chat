package uk.ac.soton.comp1206.UI.Components.chatComponents;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import uk.ac.soton.comp1206.Utility.Utility;
import uk.ac.soton.comp1206.Utility.Listeners.ChangeServerListener;
import uk.ac.soton.comp1206.Utility.Listeners.UsernameListener;

public class Settings extends VBox {
    private static final Logger logger = LogManager.getLogger(Settings.class);

    private ImageView iv;
    private UsernameListener ul;
    private ChangeServerListener cs;

    public Settings(double width) {
        this.setPrefWidth(width);
        this.getStyleClass().add("sidebar");
        this.setSpacing(5);

        this.iv = new ImageView(Utility.getImage("ECS.png"));
        this.iv.setFitWidth(width);
        this.iv.setPreserveRatio(true);

        //Mute audio
        var mute = new CheckBox("Enable Audio");
        mute.setId("settings-checkbox");
        mute.selectedProperty().bindBidirectional(Utility.audioEnabledProperty());
        mute.setOnAction(event -> {
            logger.info("audioEnabled set to: " + Utility.getAudioEnabled());
        });
        
        //Option to save messages
        var saveMsg = new CheckBox("Save Messages");
        saveMsg.setId("settings-checkbox");
        saveMsg.selectedProperty().bindBidirectional(Utility.saveMessageProperty());
        saveMsg.setOnAction(event -> {
            Utility.setSaveLocation();
            logger.info("saveMessages set to " + Utility.getSaveMessages());
        });

        //Change the save location of the chat log
        var changeLocation = new Button("Change location");
        changeLocation.setId("change-location-btn");
        changeLocation.setOnAction(event -> {
            Utility.setSaveLocation();
        });
        
        //Change the current username
        //var usernameLbl = new Label("Change username:");
        var username = new TextField();
        username.setPromptText("Change username");
        username.setOnKeyTyped(event -> {
            this.ul.changeUsername(username.getText());
        });

        //Change server
        var serverList = Utility.getServers();

        var serverSelect = new ComboBox<String>();
        serverSelect.getItems().addAll(serverList.keySet());
        serverSelect.setPromptText("Change Server");
        serverSelect.setOnAction(event -> {
            this.cs.changeServer(serverList.get(serverSelect.getValue()));
        });

        this.getChildren().addAll(this.iv, mute, saveMsg, changeLocation, username, serverSelect);

    }

    public void addUsernameListener(UsernameListener ul) {
        this.ul = ul;
    }

    public void addChangeServerListener(ChangeServerListener cs) {
        this.cs = cs;
    }
}
