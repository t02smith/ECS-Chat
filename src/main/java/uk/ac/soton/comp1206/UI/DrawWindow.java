package uk.ac.soton.comp1206.UI;

import javafx.application.Platform;
import javafx.scene.layout.StackPane;
import uk.ac.soton.comp1206.App;
import uk.ac.soton.comp1206.Network.Communicator;
import uk.ac.soton.comp1206.Network.PaintMessage;
import uk.ac.soton.comp1206.UI.Components.wbComponents.Whiteboard;
import uk.ac.soton.comp1206.Utility.Utility;
import uk.ac.soton.comp1206.UI.Components.wbComponents.WbToolbar;

public class DrawWindow extends Window { 
    private final Communicator communicator;

    private Whiteboard canvas;
    private WbToolbar toolbar;

    public DrawWindow(App app, Communicator communicator) {
        super("ECS Draw", 650, 500);
        this.scene.getStylesheets().add(Utility.getCSSFile("DrawWindow.css"));

        //Closes just the draw window when exited
        this.setOnClose(() -> {
            logger.info("Closing Draw Window");
            this.stage.close();
            app.setDrawWindowStatus(false);
        });

        this.communicator = communicator;

        //Add draw message listener
        this.communicator.addListener((message) -> Platform.runLater(() -> {
            if (message.startsWith("DRAW")) {
                this.receiveDraw(message);
            }
        }));

        //Create whiteboard
        this.canvas = new Whiteboard(600, 500);
        this.canvas.addSendDrawingListener(drawing -> {
            this.communicator.send(drawing.composeMessage());
        });

        //whiteboard holder
        var holder = new StackPane(this.canvas);
        holder.setId("whiteboard");

        //Lets the whiteboard resize
        holder.widthProperty().addListener(event -> {
            this.canvas.setWidth(0.96*holder.getWidth());
        });

        holder.heightProperty().addListener(event -> {
            this.canvas.setHeight(0.9*holder.getHeight());
        });

        //Whiteboard tools
        this.toolbar = new WbToolbar();

        this.toolbar.addLineWidthListener(width -> {
            this.canvas.changeLineWidth(width);
            logger.info("Line width changed to: {}", width);
        });

        this.toolbar.addChangeColourListener(colour -> {
            this.canvas.changeColour(colour);
            logger.info("Colour changed to: {}", colour);
        });

        this.root.setCenter(holder);
        this.root.setBottom(this.toolbar);

        
    }

    private void receiveDraw(String message) {
        String drawing = message.replace("DRAW", "").trim();
        logger.info("Received drawing: {}", drawing);

        var pm = new PaintMessage(drawing);
        pm.paint(this.canvas.getGraphicsContext2D());
    }

}
