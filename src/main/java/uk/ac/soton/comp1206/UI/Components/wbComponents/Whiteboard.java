package uk.ac.soton.comp1206.UI.Components.wbComponents;

import java.util.ArrayList;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import uk.ac.soton.comp1206.Network.PaintMessage;
import uk.ac.soton.comp1206.Utility.Listeners.Whiteboard.SendDrawingListener;

public class Whiteboard extends Canvas {
    private GraphicsContext gc = this.getGraphicsContext2D();
    ArrayList<PaintMessage> messages = new ArrayList<>();

    private PaintMessage drawing;
    private Color colour;

    private SendDrawingListener sdl;

    public Whiteboard(double width, double height) {
        super(width, height);
        this.setId("whiteboard");

        this.changeColour(Color.BLACK);
        this.gc.setLineWidth(8);

        this.setOnMousePressed(event -> {
            this.drawing = new PaintMessage(this.colour);
            this.gc.beginPath();
            this.drawing.addPoint(event.getX(), event.getY());
        });

        this.setOnMouseDragged(event -> {
            this.gc.lineTo(event.getX(), event.getY());
            this.gc.stroke();
            this.drawing.addPoint(event.getX(), event.getY());
            
        });

        this.setOnMouseReleased(event -> {
            this.gc.closePath();
            if (!this.colour.equals(Color.TRANSPARENT)) {
                this.messages.add(this.drawing);
                this.sdl.send(this.drawing);
            }
        });

    }

    public void addSendDrawingListener(SendDrawingListener sdl) {
        this.sdl = sdl;
    }

    //Tool modifiers

    public void changeColour(Color colour) {
        this.gc.setFill(colour);
        this.gc.setStroke(colour);
        this.colour = colour;
    }

    public void changeLineWidth(double width) {
        this.gc.setLineWidth(width);
    }

}
