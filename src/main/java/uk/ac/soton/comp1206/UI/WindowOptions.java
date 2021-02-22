package uk.ac.soton.comp1206.UI;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Duration;
import uk.ac.soton.comp1206.Utility.Listeners.CloseWindowListener;

public class WindowOptions extends HBox {
    private static final Logger logger = LogManager.getLogger(WindowOptions.class);
    private CloseWindowListener cwl;

    public WindowOptions(Scene scene, String name, CloseWindowListener onClose) {
        this.getStyleClass().add("window-bar");

        var empty = new Region();
        HBox.setHgrow(empty, Priority.ALWAYS);

        //Window title

        var title = new Label(name);
        title.getStyleClass().add("window-title");

        //minimise

        var min = new Button("-");
        min.getStyleClass().addAll("minimise-window", "top-button");

        min.setOnAction(event -> {
            var window = (Stage)scene.getWindow();

            var timeline = new Timeline();
            timeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(150), new KeyValue(window.opacityProperty(), 0))
            );

            timeline.setOnFinished(e -> {
                window.setIconified(true);
                window.setOpacity(1);
            });
            timeline.play();

            logger.info("Window minimised");
        });

        //expand window

        var expand = new Button("[ ]");
        expand.getStyleClass().addAll("expand-window", "top-button");

        //close

        var close = new Button("X");
        close.getStyleClass().addAll("close-window", "top-button");

        close.setOnAction(event -> {
            this.cwl.close();
        });

        //misc

        this.getChildren().addAll(title, empty, min, expand, close);
    }

    public void addCloseListener(CloseWindowListener cwl) {
        this.cwl = cwl;
    }
}
