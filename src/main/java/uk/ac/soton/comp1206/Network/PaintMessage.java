package uk.ac.soton.comp1206.Network;

import java.util.LinkedHashSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class PaintMessage {
    private static final Logger logger = LogManager.getLogger(PaintMessage.class);

    private Color colour = Color.BLACK;
    private LinkedHashSet<Point2D> points = new LinkedHashSet<>();

    /**
     * Contructor for a new paint message
     * @param colour
     */
    public PaintMessage(Color colour) {
        this.colour = colour;
        logger.info(colour);
    }

    /**
     * Constructor for received drawing
     * @param drawing
     */
    public PaintMessage(String drawing) {
        String[] drawArr = drawing.split(" ");
        this.colour = Color.web(drawArr[0]);

        //Adds all values into array
        for (int i = 1; i < drawArr.length; i++) {
            var xy = drawArr[i].split(",");
            this.points.add(new Point2D(Double.parseDouble(xy[0]), Double.parseDouble(xy[1])));
        }

    }

    public void paint(GraphicsContext gc) {
        gc.setFill(this.colour);
        gc.setStroke(this.colour);

        gc.beginPath();
        for (Point2D point: this.points) {
            gc.lineTo(point.getX(), point.getY());
            gc.stroke();
        }
        gc.closePath();
    }

    public String composeMessage() {
        StringBuilder msg = new StringBuilder("DRAW #");
        msg.append(this.colour.toString().substring(2));

        for (Point2D point: this.points) {
            msg.append(String.format(" %s,%s", point.getX(), point.getY()));
        }

        return msg.toString();
    }

    public void addPoint(double x, double y) {
        logger.info("Point added <{}, {}>", x, y);
        this.points.add(new Point2D(x, y));
    }

    public void changeColour(Color colour) {
        this.colour = colour;
        logger.info("Colour changed to {}", colour);
    }

    public LinkedHashSet<Point2D> getPoints() {
        return this.points;
    }
}
