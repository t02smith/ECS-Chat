package uk.ac.soton.comp1206.UI;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import uk.ac.soton.comp1206.Network.Communicator;
import uk.ac.soton.comp1206.UI.Components.WindowOptions;
import uk.ac.soton.comp1206.UI.Components.wbComponents.Whiteboard;
import uk.ac.soton.comp1206.Utility.Utility;
import uk.ac.soton.comp1206.Utility.Listeners.CloseWindowListener;

public abstract class Window {
    protected static final Logger logger = LogManager.getLogger(Window.class);

    protected Communicator communicator;

    protected BorderPane root = new BorderPane();
    protected Scene scene;
    protected Stage stage = new Stage();

    protected WindowOptions windowOptions;

    protected double xOffset = 0;
    protected double yOffset = 0;

    /**
     * Constructor with specified on close function
     * @param title Name of the window
     * @param width Width of the window
     * @param height Height of the window
     * @param onClose Do this when the window is closed
     */
    public Window(String title, double width, double height, Communicator communicator, CloseWindowListener onClose) {
        //Sets the basic styles for the window
        this.root.setId("border-pane");
        this.scene = new Scene(this.root, width, height);
        this.scene.getStylesheets().add(this.getClass().getResource("/style/Common.css").toExternalForm());

        this.stage.setScene(this.scene);

        //Initialises the window 
        this.stage.setTitle(title);
        this.stage.getIcons().add(Utility.getImage("ECS.png"));
        this.stage.initStyle(StageStyle.UNDECORATED);

        //Custom window options
        this.windowOptions = new WindowOptions(this.scene, title, onClose);
        this.root.setTop(this.windowOptions);

        //Drag and moving the window
        this.root.setOnMousePressed(event -> {
            if (!(event.getTarget() instanceof Whiteboard)) {
                this.xOffset = event.getSceneX();
                this.yOffset = event.getSceneY();
            }
        });

        this.root.setOnMouseDragged(event -> {
            if (!(event.getTarget() instanceof Whiteboard)) {
                this.stage.setX(event.getScreenX() - this.xOffset);
                this.stage.setY(event.getScreenY() - this.yOffset);
            }
        });

        this.stage.show();
        this.stage.centerOnScreen();

        this.communicator = communicator;
    }

    /**
     * Constructor with default close
     * @param title
     * @param width
     * @param height
     */
    public Window(String title, double width, double height, Communicator communicator) {
        this(title, width, height, communicator, () -> {
            logger.info("{} closing.", title);
            System.exit(0);
            
        });
    }

    /**
     * Constructor for a window without any communicator
     * @param title
     * @param width
     * @param height
     */
    public Window(String title, double width, double height) {
        this(title, width, height, null);
    }

    public void setOnClose(CloseWindowListener cwl) {
        this.windowOptions.setOnClose(cwl);
    }

    public void openSidebar(Node sidebar) {
        var translate = new TranslateTransition(new Duration(500), sidebar);
        translate.setFromX(150);
        translate.setByX(-150);

        translate.play();
    }
    
    public void closeSidebar(Node sidebar, EventHandler<ActionEvent> endEvent) {
        var translate = new TranslateTransition(new Duration(500), sidebar);
        translate.setByX(150);
        translate.setOnFinished(endEvent);

        translate.play();
    }


    public Scene getScene() {
        return this.scene;
    }

    public Node getRoot() {
        return this.root;
    }
}
