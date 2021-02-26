package uk.ac.soton.comp1206.UI;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import uk.ac.soton.comp1206.App;
import uk.ac.soton.comp1206.Network.Communicator;
import uk.ac.soton.comp1206.UI.Components.clickerComponents.Tile;
import uk.ac.soton.comp1206.Utility.Utility;

public class TileClickerWindow extends Window {
    private Tile currentColour;
    private Label scoreLbl;

    private String[] colours = {"red", "yellow", "blue", "green", "purple", "orange"};

    private ScrollPane boardPane = new ScrollPane();

    private int score = 0;

    private App app;

    public TileClickerWindow(App app, Communicator communicator) {
        super("ECS Squares", 600, 525, communicator);
        this.communicator.send("SCORES");
        this.scene.getStylesheets().add(Utility.getCSSFile("TileWindow.css"));

        this.setOnClose(() -> {
            logger.info("Closing game window");
            this.stage.close();
            app.setTileWindowStatus(false);
            this.submitResult();
        });

        this.communicator.addListener(message -> {
            if (message.startsWith("SCORES")) {
                this.fillLeaderboard(message);
            }
        });

        this.app = app;

        this.root.setRight(this.rightPane());
        this.root.setCenter(this.createBoard());
        
    }

    private void submitResult() {
        if (this.score == 0) return;
        var result = String.format("SCORE %s %d", this.app.getUsername(), this.score);
        this.communicator.send(result);
    }


    /**
     * Creates the game board
     * @return
     */
    private GridPane createBoard() {
        var side = 384;
        var grid = new GridPane();
        grid.getStyleClass().add("tile-grid");

        //Creates a 6x6 grid
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                var tile = new Tile(side/6, this.colours);

                //Each tile will change colour when you click the correct colour
                tile.setOnAction(event -> {
                    if (this.currentColour.getColour().equals(tile.getColour())) {
                        tile.changeColour(this.colours);
                        this.score += 5;
                    } else {
                        if (score > 0) this.score--;
                    }
                    this.updateScore();
                    
                });

                grid.add(tile, i, j);
            }
        }

        return grid;
    }

    /**
     * Components for the side bar of the game
     * Will show the current colour, current score and a leaderboard
     * @return
     */
    private VBox rightPane() {
        //Current colour
        this.currentColour = new Tile(100, this.colours);
        this.currentColour.getStyleClass().add("current-colour");
        this.currentColour.setAlignment(Pos.CENTER);

        //Current score
        this.scoreLbl = new Label("Score: " + this.score);
        this.scoreLbl.getStyleClass().add("score-label");

        //Game over
        var gameOver = new Label("GAME OVER");
        gameOver.getStyleClass().add("game-over-label");

        //Right pane
        var vbox = new VBox(this.currentColour, this.scoreLbl, this.boardPane);
        vbox.getStyleClass().add("right-pane");

        //Sets the current colour to change every 2 seconds
        var timeline = new Timeline(
            new KeyFrame(
                Duration.millis(2500),
                event -> {
                    this.currentColour.changeColour(this.colours);
                })
        );

        //Play for a minute
        timeline.setCycleCount(1);

        timeline.setOnFinished(event -> {
            logger.info("Game finished with a score {}.", this.score);
            this.submitResult();

            var gameOverScreen = new VBox(gameOver);
            gameOverScreen.getChildren().addAll(vbox.getChildren());
            gameOverScreen.getStyleClass().add("game-over-screen");

            this.root.setCenter(gameOverScreen);
            this.root.setRight(null);

        });

        timeline.play();

        return vbox;
    }

    private void updateScore() {
        this.scoreLbl.setText("Score: " + this.score);
    }

    
    private void fillLeaderboard(String message) {
        String[] scores = message.split("\n");
        var vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        
        String[] eachScore;
        for (int i = 1; i < scores.length && i < 11; i++) {
            eachScore = scores[i].split("=");

            var userLbl = new Label(
                String.format("%-2d. %-12s %s", i, eachScore[0], eachScore[1])
            );

            userLbl.getStyleClass().add("user-score-label");
            userLbl.setTextAlignment(TextAlignment.CENTER);
            vbox.getChildren().add(userLbl);
        }

        Platform.runLater(() -> {this.boardPane.setContent(vbox);});

    }

}
