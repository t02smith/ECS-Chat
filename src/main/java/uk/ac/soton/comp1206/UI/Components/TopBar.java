package uk.ac.soton.comp1206.UI.Components;

import java.util.HashMap;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import uk.ac.soton.comp1206.Utility.Utility;
import uk.ac.soton.comp1206.Utility.Listeners.TopBar.QuitListener;
import uk.ac.soton.comp1206.Utility.Listeners.TopBar.TopBarListener;

public class TopBar extends HBox {
    private Label title;

    private QuitListener quitListener;
    private HashMap<String, TopBarListener> listeners = new HashMap<>();

    private Button settings;
    private Button users;
    private Button whiteboard;

    public TopBar() {
        this.setId("title-bar");

        this.title = new Label("ECS Chat");
        this.title.setId("title");

        this.settings = this.createBtn("settings", "settings.png");
        this.users = this.createBtn("users", "people.png");
        this.whiteboard = this.createBtn("whiteboard", "WhiteBoard.png");

        var empty = new Region();
        HBox.setHgrow(empty, Priority.ALWAYS);

        this.getChildren().addAll(this.title, empty, this.whiteboard, this.users, this.settings);
    }

    public void addListener(String name, TopBarListener tbl) {
        this.listeners.put(name, tbl);
    }

    public void addQuitListener(QuitListener ql) {
        this.quitListener = ql;
    }

    private Button createBtn(String name, String img) {
        var icon = new ImageView(Utility.getImage(img));
        icon.setPreserveRatio(true);
        icon.setFitHeight(30);

        var btn = new Button();
        btn.setGraphic(icon);
        btn.setId("top-bar-btn");
        btn.setOnAction(event -> {
            this.listeners.get(name).open();
        });

        return btn;
    }

}
