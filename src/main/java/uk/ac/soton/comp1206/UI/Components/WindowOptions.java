package uk.ac.soton.comp1206.UI.Components;

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

    private Button minimise;
    private Button expand;
    private Button close;

    public WindowOptions(Scene scene, String name, CloseWindowListener onClose) {
        this.getStyleClass().add("window-bar");

        var empty = new Region();
        HBox.setHgrow(empty, Priority.ALWAYS);

        //Window title

        var title = new Label(name);
        title.getStyleClass().add("window-title");

        //minimise

        this.minimise = new Button("-");
        this.minimise.getStyleClass().addAll("minimise-window", "top-button");

        this.minimise.setOnAction(event -> {
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

        this.expand = new Button("[ ]");
        this.expand.getStyleClass().addAll("expand-window", "top-button");

        //close

        this.close = new Button("X");
        this.close.getStyleClass().addAll("close-window", "top-button");

        this.close.setOnAction(event -> {
            onClose.close();
        });

        //misc
        this.getChildren().addAll(title, empty, this.minimise, this.expand, this.close);
    }

    /**
     * Used to change the onclose method after initialising
     * @param cwl New close method
     */
    public void setOnClose(CloseWindowListener cwl) {
        this.close.setOnAction(event -> {
            cwl.close();
        });
    }

}
