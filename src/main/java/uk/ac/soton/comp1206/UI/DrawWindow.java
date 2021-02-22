package uk.ac.soton.comp1206.UI;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import uk.ac.soton.comp1206.App;
import uk.ac.soton.comp1206.Network.Communicator;
import uk.ac.soton.comp1206.Network.PaintMessage;
import uk.ac.soton.comp1206.UI.Components.wbComponents.Whiteboard;
import uk.ac.soton.comp1206.UI.Components.wbComponents.WbToolbar;

public class DrawWindow {
    private static final Logger logger = LogManager.getLogger(ChatWindow.class);
 
    private final App app;
    private final Scene scene;
    private final Communicator communicator;

    private Whiteboard canvas;
    private WbToolbar toolbar;

    private BorderPane root;

    private double xOffset = 0;
    private double yOffset = 0;

    public DrawWindow(App app, Communicator communicator) {
        this.app = app;
        this.communicator = communicator;

        //Add draw message listener
        this.communicator.addListener((message) -> Platform.runLater(() -> {
            if (message.startsWith("DRAW")) {
                this.receiveDraw(message);
            }
        }));

        this.canvas = new Whiteboard(600, 500);

        this.canvas.addSendDrawingListener(drawing -> {
            this.communicator.send(drawing.composeMessage());
        });
        
        //set up for whiteboard

        var holder = new StackPane(this.canvas);
        holder.setId("whiteboard");

        //Lets the whiteboard resize
        holder.widthProperty().addListener(event -> {
            this.canvas.setWidth(0.96*holder.getWidth());
        });

        holder.heightProperty().addListener(event -> {
            this.canvas.setHeight(0.9*holder.getHeight());
        });

        //Toolbar
        this.toolbar = new WbToolbar();

        this.toolbar.addLineWidthListener(width -> {
            this.canvas.changeLineWidth(width);
            logger.info("Line width changed to: {}", width);
        });

        this.toolbar.addChangeColourListener(colour -> {
            this.canvas.changeColour(colour);
            logger.info("Colour changed to: {}", colour);
        });

        this.root = new BorderPane(holder);
        this.root.setBottom(this.toolbar);

        this.root.setId("wb-border-pane");
        this.scene = new Scene(this.root, 650, 500);

        //Window options
        var windowOptions = new WindowOptions(this.scene, "ECS Draw", () -> {
            this.app.closeWhiteboard();
            logger.info("Whiteboard closed.");
        });

        this.root.setTop(windowOptions);

        this.scene.getStylesheets().add(this.getClass().getResource("/style/DrawWindow.css").toExternalForm());
        this.scene.getStylesheets().add(this.getClass().getResource("/style/Common.css").toExternalForm());

    }

    private void receiveDraw(String message) {
        String drawing = message.replace("DRAW", "").trim();
        logger.info("Received drawing: {}", drawing);

        var pm = new PaintMessage(drawing);
        pm.paint(this.canvas.getGraphicsContext2D());
    }

    public Scene getScene() {
        return this.scene;
    }

    public Node getRoot() {
        return this.root;
    }

    public double getXOffset() {
        return this.xOffset;
    }

    public void setXOffset(double newVal) {
        this.xOffset = newVal;
    }

    public double getYOffset() {
        return this.yOffset;
    }

    public void setYOffset(double newVal) {
        this.yOffset = newVal;
    }

}
