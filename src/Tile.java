import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;

import java.util.ArrayList;
import java.util.List;

public class Tile extends Button {
    private boolean bomb;
    private boolean clicked;
    private List<Tile> neighbours;
    private int bombsCount;
    private boolean flagged;
    private static int tilesCount;
    private static Label info;

    public static void setInfo(Label info) {
        Tile.info = info;
    }

    public Tile(float width, float height) {
        super(null);
        this.setPrefHeight(height);
        this.setPrefWidth(width);
        this.setStyle("-fx-font-size:20");

        // Default state
        this.bomb = false;
        this.clicked = false;
        this.neighbours = new ArrayList<Tile>(8);
        this.bombsCount = 0;
        this.flagged = false;
        tilesCount = 100;

        // Set event
        this.setOnMouseClicked(event -> {
            MouseButton type = event.getButton();
            if (type == MouseButton.PRIMARY)
                click();
            if (type == MouseButton.SECONDARY)
                flag();
        });
    }

    // Set flag
    private void flag() {
        if (!flagged) {
            this.setText("F");
            flagged = true;
        } else {
            this.setText("");
            flagged = false;
        }
    }

    // Primary click
    public void click() {
        if (!Minesweeper.GameOver) {
            if (!clicked && !flagged) {
                if (bombsCount > 0) {
                    this.setText(String.valueOf(bombsCount));
                } else if (bomb) {
                    this.setText("X");
                    Minesweeper.GameOver = true;
                    info.setText("Game over!");
                }

                this.setDisable(true);
                this.clicked = true;
                tilesCount--;
                int totalLeft = tilesCount - Controller.bombs;
                if (!Minesweeper.GameOver)
                    if (totalLeft > 0)
                        info.setText("Tiles left: " + totalLeft);
                    else {
                        info.setText("Victory!");
                        Minesweeper.GameOver = true;
                    }

                if (!bomb)
                    for (Tile t : neighbours) {
                        if (!t.clicked && !t.bomb && bombsCount == 0)
                            t.click();
                    }
            }
        }
    }

    public void addNeighbour(Tile t) {
        this.neighbours.add(t);
    }

    public void setBomb() {
        this.bomb = true;
    }

    public void countNeighbourBombs() {
        if (!bomb)
            for (Tile t : neighbours) {
                if (t.bomb) {
                    this.bombsCount++;
                }
            }
    }
}
