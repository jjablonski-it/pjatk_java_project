import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.*;

public class Controller {
    private final int easy = 5;
    private final int medium = 10;
    private final int hard = 20;
    private Timer timer;
    private int seconds;

    public static int bombs;

    @FXML
    private GridPane grid;
    @FXML
    private Button mode_easy;
    @FXML
    private Button mode_medium;
    @FXML
    private Button mode_hard;
    @FXML
    private Label info;
    @FXML
    private Label time;

    private Tile[][] tiles;

    @FXML
    public void initialize() {
        Tile.setInfo(info);
        mode_easy.setOnAction(e -> setBombs(easy));
        mode_medium.setOnAction(e -> setBombs(medium));
        mode_hard.setOnAction(e -> setBombs(hard));

        setBombs(easy);
    }

    public void setTiles() {
        Minesweeper.GameOver = false;
        startTimer();
        seconds = 0;

        tiles = new Tile[10][10];
        grid.getChildren().clear();
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                Tile tile = new Tile(50, 50);
                tiles[y][x] = tile;
                grid.add(tile, y, x);
            }
        }
        setNeighbours();
    }

    void setNeighbours() {
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                // Tile
                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        if (j == 0 && i == 0) continue;
                        if (y + i >= 0 && y + i < 10) {
                            if (x + j >= 0 && x + j < 10) {
                                tiles[y][x].addNeighbour(tiles[y + i][x + j]);
                            }
                        }
                    }
                }
            }
        }
    }

    public void setBombs(int amount) {
        bombs = amount;
        setTiles();
        List<Tile> bombs = new ArrayList<Tile>(amount);
        Random rand = new Random();
        do {
            int rX = rand.nextInt(10);
            int rY = rand.nextInt(10);
            tiles[rY][rX].setBomb();
            bombs.add(tiles[rY][rX]);
        } while (bombs.size() < amount);
        setValues();
    }

    public void setValues() {
        for (Tile[] row : tiles) {
            for (Tile t : row) {
                t.countNeighbourBombs();
            }
        }
    }

    public void startTimer() {
        if (timer != null)
            timer.cancel();
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    if (Minesweeper.GameOver) {
                        seconds--;
                        this.cancel();
                    }
                    seconds++;
                    int sec = seconds % 60;
                    Platform.runLater(() -> time.setText(seconds / 60 + ":" + (sec < 10 ? "0" + sec : sec)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        timer.scheduleAtFixedRate(task, 1000, 1000);
    }
}
