package uk.ac.soton.comp1206.UI.Components.chatComponents;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

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

        var styling = new HashMap<String, Boolean>();
        styling.put("bold", false);
        styling.put("underline", false);
        styling.put("italics", false);
        styling.put("link", false);
        styling.put("strike", false);

        for (String word: words) {
            var text = this.formatWord(new Text(), word, styling);

            if (this.styleClose) {
                var space = new Text(" ");
                msgContent.getChildren().addAll(text, space);
            } else {
                msgContent.getChildren().add(text);
            }
        }

        return msgContent;
    }

    public Text formatWord(Text text, String word, HashMap<String, Boolean> styling) {       

        //check start of string
        if (word.startsWith("__")) {
            styling.replace("underline", !styling.get("underline"));
            return formatWord(text, word.substring(2), styling);
        } else if (word.startsWith("**")) {
            styling.replace("bold", !styling.get("bold"));
            return formatWord(text, word.substring(2), styling);
        } else if (word.startsWith("++")) {
            styling.replace("italics", !styling.get("italics"));
            return formatWord(text, word.substring(2), styling);
        } else if (word.startsWith("--")) {
            styling.replace("strike", !styling.get("strike"));
            return formatWord(text, word.substring(2), styling);
        }
        
        //Check end of string
        else if (word.endsWith("__")) {
            this.styleClose = true;
            text.getStyleClass().add("underline");
            styling.replace("underline", !styling.get("underline"));
            return formatWord(text, word.substring(0, word.length()-2), styling);
        } else if (word.endsWith("**")) {
            this.styleClose = true;
            text.getStyleClass().add("bold");
            styling.replace("bold", !styling.get("bold"));
            return formatWord(text, word.substring(0, word.length()-2), styling);
        } else if (word.endsWith("++")) {
            this.styleClose = true;
            text.getStyleClass().add("italics");
            styling.replace("italics", !styling.get("italics"));
            return formatWord(text, word.substring(0, word.length()-2), styling);
        } else if (word.endsWith("--")) {
            this.styleClose = true;
            text.getStyleClass().add("strike");
            styling.replace("strike", !styling.get("strike"));
            return formatWord(text, word.substring(0, word.length()-2), styling);
        }
        
        //Links
        else if (word.matches("(http[s]?:\\/\\/)?(www\\.)?(\\w+)(\\.\\w+[/]?)+")) {
            return this.createLink(text, word);
        }
        
        //If there is not styling
        else {
            for (String style: styling.keySet()) {
                if (styling.get(style)) text.getStyleClass().add(style);
            }
            text.setText(this.styleClose ? word: word + " ");
            return text;
        }
    }
    

    private Label createMsgTime() {
        var time = new Label(formatter.format(LocalDateTime.now()));
        time.setMinWidth(40);
        time.setId("time");

        return time;
    }

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
    
}
