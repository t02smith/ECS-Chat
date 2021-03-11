package uk.ac.soton.comp1206.UI.Components.chatComponents;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import uk.ac.soton.comp1206.Utility.Listeners.UserHistoryListener;

public class Message extends HBox {
    private VBox msgShell;
    private Label msgAuthor;
    private TextFlow msgContent;
    private Label msgTime;

    private UserHistoryListener uhl;

    private boolean styleClose = false;
    private boolean isUser = false;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    private static final Logger logger = LogManager.getLogger(Message.class);

    public Message(String author, String content, boolean isUser) {
        this.msgShell = new VBox(1.0);

        this.msgAuthor = this.createMsgAuthor(author);
        this.msgContent = this.createMsgContent(content);
        this.msgTime = this.createMsgTime();

        this.msgShell.getChildren().addAll(this.msgAuthor, this.msgContent);
        this.setOpacity(0);

        if (isUser) {
            this.isUser = true;
            this.msgShell.setId("message-content-user");
            this.setAlignment(Pos.TOP_RIGHT);
            this.getChildren().addAll(this.msgShell, this.msgTime);
        } else {
            this.msgShell.setId("message-content");
            this.getChildren().addAll(this.msgTime, this.msgShell);
        }
    }

    public void slideIn() {
        var duration = new Duration(350);

        var fade = new FadeTransition(duration, this);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();

        var translate = new TranslateTransition(duration, this);

        if (this.isUser) {
            translate.setFromX(25);
            translate.setByX(-25);
        } else {
            translate.setFromX(-25);
            translate.setByX(25);
        }

        translate.play();

    }

    private Label createMsgAuthor(String author) {
        var title = new Label(author + ":");
        title.setId("message-author");

        title.setOnMouseClicked(event -> {
            this.uhl.showHistory(author);
        });

        return title;
    }

    public void addListener(UserHistoryListener uhl) {
        this.uhl = uhl;
    }
    
    private TextFlow createMsgContent(String content) {
        var msgContent = new TextFlow();
        var words = content.split(" ");

        for (String word: words) {
            var text = this.formatWord(new Text(), word);

            if (this.styleClose) {
                var space = new Text(" ");
                msgContent.getChildren().addAll(text, space);
            } else {
                msgContent.getChildren().add(text);
            }
        }

        MsgStyle.resetActive();
        return msgContent;
    }

    private Text formatWord(Text text, String word) {


        for (String key: MsgStyle.keyset) {
            if (word.startsWith(key)) {
                MsgStyle.getStyle(key).swapActive();
                return this.formatWord(text, word.substring(2));
            }
        }

        for (String key: MsgStyle.keyset) {
            var style = MsgStyle.getStyle(key);
            if (style.active) text.getStyleClass().add(style.toString());
        }

        for (String key: MsgStyle.keyset) {
            if (word.endsWith(key)) {

                MsgStyle.getStyle(key).swapActive();
                return this.formatWord(text, word.substring(0, word.length()-2));
            }
        }

        text.setText(this.styleClose ? word: word + " ");
        return text;
    }
    

    private Label createMsgTime() {
        var time = new Label(formatter.format(LocalDateTime.now()));
        time.setMinWidth(40);
        time.setId("time");

        return time;
    }

    @Deprecated
    private Text createLink(Text text, String word) {
        text.getStyleClass().addAll("link", "italics");
        text.setFill(Color.DARKGREEN);
        text.setOnMouseClicked(event -> {
            try {
                var uri = new URI(word);
                java.awt.Desktop.getDesktop().browse(uri);
                logger.info("Opening url: " + word);
            } catch(Exception e) {
                logger.error(e);
            }
        });
        

        text.setText(word);
        return text;
    }

    public TextFlow getMessageContent() {
        return this.msgContent;
    }

    public enum MsgStyle {
        BOLD("**"),
        ITALIC("++"),
        UNDERLINE("__"),
        STRIKE("--");

        private static ArrayList<String> keyset = MsgStyle.createKeySet();

        private boolean active = false;
        private String key;

        private MsgStyle(String key) {
            this.key = key;
        }

        public String getKey() {
            return this.key;
        }

        public void swapActive() {
            this.active = !this.active;
        }

        private static MsgStyle getStyle(String key) {
            for (MsgStyle style: MsgStyle.values()) {
                if (style.getKey().equals(key)) return style;
            }

            return null;
        }

        private static ArrayList<String> createKeySet() {
            var keys = new ArrayList<String>();

            for (MsgStyle style: MsgStyle.values()) {
                keys.add(style.getKey());
            }

            return keys;
        }

        private static void resetActive() {
            for (MsgStyle style: MsgStyle.values()) {
                style.active = false;
            }
        }
    }
    
}
