package uk.ac.soton.comp1206.UI.Components.clickerComponents;

import java.util.Random;

import javafx.scene.control.Button;

public class Tile extends Button {
    private String colour = "";

    public Tile(double side, String[] colours) {
        this.setMinHeight(side);
        this.setMinWidth(side);

        this.getStyleClass().add("tile");
        this.changeColour(colours);
    }

    /**
     * Changes the buttons colour at random
     */
    public void changeColour(String[] colours) {
        String newColour;
        do {
            newColour = colours[new Random().nextInt(colours.length)];
        } while (this.colour.equals(newColour));

        

        this.setStyle("-fx-background-color: " + newColour);
        this.colour = newColour;

    }

    public String getColour() {
        return this.colour;
    }
}
