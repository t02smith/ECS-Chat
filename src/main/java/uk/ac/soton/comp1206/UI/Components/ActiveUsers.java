package uk.ac.soton.comp1206.UI.Components;

import java.util.ArrayList;
import java.util.Collections;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import uk.ac.soton.comp1206.User;

public class ActiveUsers extends ScrollPane {
    private static final Logger logger = LogManager.getLogger(Settings.class);
    private ArrayList<User> users;
    private Label title;

    public ActiveUsers() {
        this.users = new ArrayList<>();

        this.title = new Label("Active:");
        this.title.setId("user-board-name");

        this.setMaxWidth(85);

        this.setId("active-users-pane");
    }

    public void addUser(User user) {
        if (!this.users.contains(user)) {
            this.users.add(user);
        }
    }

    public void updateUsers() {
        var activeList = new VBox(this.title);
        Collections.sort(this.users);

        for (User user: this.users) {
            var nextUser = new VBox();

            var name = new Label(user.getName());
            name.setId("user-active-name");

            var lastActive = (int)Math.floor((System.currentTimeMillis() - user.getLastActive())/60000);

            Label lastActiveLbl;
            if (lastActive == 0) {
                lastActiveLbl = new Label("Online");
                lastActiveLbl.setId("active-now");
            } else if (lastActive == 1) {
                lastActiveLbl = new Label("1 minute ago");
                lastActiveLbl.setId("last-active");
            } else if (lastActive <= 10) {
                lastActiveLbl = new Label(((Integer)lastActive).toString() + " minutes ago");
                lastActiveLbl.setId("last-active");
            } else {
                lastActiveLbl = new Label("Offline");
                lastActiveLbl.setId("last-active");
            }

            nextUser.getChildren().addAll(name, lastActiveLbl);
            activeList.getChildren().add(nextUser);
        }

        logger.info("Updating active user list.");
        this.setContent(activeList);
    }

    public int getUserNum() {
        return this.users.size();
    }


}
