package uk.ac.soton.comp1206.UI.Components.wbComponents;

import javafx.geometry.Pos;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import uk.ac.soton.comp1206.Utility.Listeners.Whiteboard.ChangeColourListener;

public class WbToolbar extends HBox {
    private ColorPicker colorPicker;

    private ChangeColourListener ccl;
    //private ChangeLineWidthListener clw;

    public WbToolbar() {
        /*
        //Removed -> messages dont support different width
        this.penSize = new Slider(0, 20, 8);
        this.penSize.setShowTickLabels(true);
        this.penSize.setShowTickMarks(true);
        this.penSize.setMajorTickUnit(5);
        this.penSize.setBlockIncrement(1);
        this.penSize.setSnapToTicks(true);

        
        this.penSize.valueProperty().addListener(event -> {
            this.clw.change(this.penSize.getValue());
        });*/

        this.colorPicker = new ColorPicker(Color.BLACK);
        this.colorPicker.setOnAction(event -> {
            this.ccl.changeColour(this.colorPicker.getValue());
        });

        this.getChildren().addAll(this.colorPicker);
        this.setAlignment(Pos.CENTER);
    }

    public void addChangeColourListener(ChangeColourListener ccl) {
        this.ccl = ccl;
    }

    /*
    public void addLineWidthListener(ChangeLineWidthListener clw) {
        this.clw = clw;
    }*/

}
