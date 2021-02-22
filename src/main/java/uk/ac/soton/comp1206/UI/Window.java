package uk.ac.soton.comp1206.UI;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import uk.ac.soton.comp1206.UI.Components.wbComponents.Whiteboard;
import uk.ac.soton.comp1206.Utility.Utility;
import uk.ac.soton.comp1206.Utility.Listeners.CloseWindowListener;

public class Window {
    protected static final Logger logger = LogManager.getLogger(Window.class);

    protected BorderPane root = new BorderPane();
    protected Scene scene;
    protected Stage stage = new Stage();

    protected double xOffset = 0;
    protected double yOffset = 0;

    /**
     * Constructor with specified on close function
     * @param title Name of the window
     * @param width Width of the window
     * @param height Height of the window
     * @param onClose Do this when the window is closed
     */
    public Window(String title, double width, double height, CloseWindowListener onClose) {
        //Sets the basic styles for the window
        this.root.setId("border-pane");
        this.scene.getStylesheets().add(this.getClass().getResource("/style/Common.css").toExternalForm());

        this.scene = new Scene(this.root, width, height);

        //Initialises the window 
        this.stage.setTitle(title);
        this.stage.getIcons().add(Utility.getImage("ECS.png"));
        this.stage.initStyle(StageStyle.UNDECORATED);

        //Custom window options
        this.root.setTop(new WindowOptions(this.scene, title, onClose));

        //Drag and moving the window
        this.root.setOnMousePressed(event -> {
            if (!(event.getTarget() instanceof Whiteboard)) {
                this.xOffset = event.getSceneX();
                this.yOffset = event.getSceneY();
            }
        });

        this.root.setOnMouseDragged(event -> {
            if (!(event.getTarget() instanceof Whiteboard)) {
                this.xOffset = event.getScreenX() - this.xOffset;
                this.yOffset = event.getScreenY() - this.yOffset;
            }
        });
    }

    /**
     * Constructor with default close
     * @param title
     * @param width
     * @param height
     */
    public Window(String title, double width, double height) {
        this(title, width, height, () -> {
            System.exit(0);
            logger.info("{} closing.", title);
        });
    }
}
