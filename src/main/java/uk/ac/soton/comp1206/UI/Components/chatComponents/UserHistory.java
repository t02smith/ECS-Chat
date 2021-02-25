package uk.ac.soton.comp1206.UI.Components.chatComponents;

import java.util.Iterator;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import uk.ac.soton.comp1206.User;
import uk.ac.soton.comp1206.UI.ChatWindow;

public class UserHistory extends ScrollPane {
    private ChatWindow cw;
    private User user;

    private HBox title;
    private Label name;
    private Button close;
    
    private VBox msgList;

    public UserHistory(ChatWindow cw, User user) {
        this.cw = cw;
        this.user = user;

        this.msgList = new VBox();
        this.setContent(msgList);
        this.msgList.setId("user-history-message-shell");

        //User's name
        this.name = new Label(user.getName());
        this.name.setId("user-board-name");

        var empty = new Region();
        HBox.setHgrow(empty, Priority.ALWAYS);

        //Close the user history board button
        this.close = new Button("X");
        this.close.setId("close-btn");
        this.close.setId("close-user-history");
        this.close.setOnAction(event -> {
            this.cw.closeUserBoard();
        });

        this.title = new HBox(this.name, empty, this.close);
        this.msgList.getChildren().add(this.title);

        this.setMaxWidth(200);
        this.setFitToWidth(true);

        this.addMessages();

        this.setId("active-users-pane");

    }

    private void addMessages() {
        String time = "";
        Iterator<String[]> msgIt = user.getMessages();

        //msg content = msgIt[0]

        while (msgIt.hasNext()) {
            var message = msgIt.next();

            var content = new Message(user.getName(), message[0], false);
            var display = content.getMessageContent();
            display.setId("user-history-message");

            if (!time.equals(message[1])) {
                var timeStamp = new Label(message[1]);
                timeStamp.setId("time");

                time = message[1];
                this.msgList.getChildren().addAll(timeStamp, display);
            } else {
                this.msgList.getChildren().add(display);
            }
        }
    }
}
