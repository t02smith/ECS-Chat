package uk.ac.soton.comp1206.UI.Components;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import uk.ac.soton.comp1206.UI.ChatWindow;
import uk.ac.soton.comp1206.Utility.Utility;

public class Toolbar extends HBox {
    private ChatWindow cw;

    private TextField input;
    private Button send;

    public Toolbar(ChatWindow cw) {
        this.cw = cw;
        this.input = this.createMsgInput();
        this.send = this.createSendBtn();

        this.setId("toolbar");
        this.setAlignment(Pos.CENTER_LEFT);
        this.getChildren().addAll(this.input, this.send);

    }

    /**
     * Creates the input field for a message
     * @return message input field
     */
    private TextField createMsgInput() {
        var input = new TextField();
        input.setId("message-input");
        input.setPrefHeight(40);
        input.setPromptText("Enter message");

        input.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                this.cw.sendCurrentMessage(input.getText());
                input.clear();
            }
        });

        HBox.setHgrow(input, Priority.ALWAYS);

        return input;
    }

    /**
     * @return Creates send button
     */
    private Button createSendBtn() {
        var send = new Button();
        send.setPrefSize(80, 50);

        ImageView sendIcon = new ImageView(Utility.getImage("send.png"));
        send.setGraphic(sendIcon);
        
        send.setOnAction(event -> {
            this.cw.sendCurrentMessage(this.input.getText());
            this.input.clear();
        });

        return send;
    }
}
