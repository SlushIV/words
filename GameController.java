import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameController {

    private final WordEngine engine = new WordEngine();
    private final List<Guess> history = new ArrayList<>();
    private Word target;
    private final Window gameWindow;

    private boolean started = false;

    // time tweakin (maybe add modes later???)
    private Timeline timer;
    private int secondsRemaining = 180;
    private boolean isGameOver = false;

    // colors
    private static final Color COLD_COLOR = Color.web("#161b22"); // base row background
    private static final Color WARM_COLOR = Color.web("#6699CC"); // blue grey ish
    private static final Color HOT_COLOR  = Color.web("#0066FF"); // pure red

    public GameController(Window window) {
        gameWindow = window;
    }

    // -1 invalid, 0 correct, 1 incorrect already guessed, 2 new incorrect guess
    public int processGuess(String input) {
        // not fully sure what to return yet or if to return anything tbh

        // start timer on first guess
        if (!started) {
            startTimer();
            started = true;
        }

        Word userWord = engine.getWord(input);

        // invalid guess
        if (userWord == null) {
            gameWindow.statusLabel.setText("word not found");
            return -1;
        }

        // correct guess
        else if (userWord.equals(target)) {
            gameWindow.statusLabel.setText("CONGRATS! You found it! The word was: " + target.getText());
            stopTimer();
            return 0;

        // incorrect guess
        } else {

            // calculating score
            int score = engine.getRanking(input);
            Guess userGuess = new Guess(engine.getWord(input), score);

            if (history.contains(userGuess)) {
                gameWindow.statusLabel.setText("already guessed dumbahh");
                return 1;

            } else {
                gameWindow.statusLabel.setText("try again");

                history.add(userGuess);
                Collections.sort(history);
                int index = history.indexOf(userGuess); // needed to insert guess hbox in correct spot in guessContainer

                // update guess count
                gameWindow.guessCountLabel.setText("Guesses: " + history.size());

                // add guess to gameWindow
                HBox guessRow = createGuessRow(input, score);
                gameWindow.guessContainer.getChildren().add(index, guessRow);
                return 2;
            }
        }
    }

    private HBox createGuessRow(String input, int rank) {
        Label left = new Label(input);
        Label right = new Label(rank + "");
        Region spacer = new Region();

        HBox.setHgrow(spacer, Priority.ALWAYS); // This forces the spacer to take all available space
        HBox row = new HBox(left, spacer, right);
        row.getStyleClass().add("guess-row");

        // hover zoom
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(150), row);
        scaleUp.setToX(1.025); // 102.5% size
        scaleUp.setToY(1.025);
        scaleUp.setInterpolator(Interpolator.EASE_OUT); // easing out

        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(150), row);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);
        scaleDown.setInterpolator(Interpolator.EASE_OUT);

        row.setOnMouseEntered(e -> {
            scaleDown.stop();
            scaleUp.playFromStart();
        });

        row.setOnMouseExited(e -> {
            scaleUp.stop();
            scaleDown.playFromStart();
        });

        applyRankColor(row, rank);

        return row;
    }

    public static void applyRankColor(HBox guessRow, int rank) {
        // calculate logarithmic factor (1 -> factor=1.0, 400000 -> factor=0.0)
        Color color = getColor(rank);

        // convert JavaFX color to CSS hex string (#RRGGBB)
        String hex = String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));

        // apply
        guessRow.setStyle("-fx-background-color: " + hex + ";");
    }

    private static Color getColor(int rank) {
        double factor = 1.0 - (Math.log(rank) / Math.log(400000));

        // color blending for distinct warmth thresholds
        Color color;
        if (factor > 0.5) {
            // interpolate between WARM and HOT (somewhat close)
            double t = (factor - 0.5) * 2.0;
            color = WARM_COLOR.interpolate(HOT_COLOR, t);
        } else {
            // interpolate between COLD and WARM (not really close)
            double t = factor * 2.0;
            color = COLD_COLOR.interpolate(WARM_COLOR, t);
        }
        return color;
    }

    public void gameSetup() {
        // dictionary setup
        try {
            System.out.println("Loading dictionary...");
            engine.loadVectors("glove.6B.50d.txt");
        } catch (IOException e) {
            System.err.println("file error");
            return;
        }

        // common words setup
        try {
            System.out.println("Loading common words...");
            engine.loadCommonWords("google-10000-english.txt");
        } catch (IOException e) {
            System.err.println("file error");
            return;
        }

        // secret word setup
        target = engine.selectRandomWord();
        System.out.println(target.getText()); // for testing, remove in production

        // calculate similarity ranking for all words
        engine.rankSimilarity(target);
    }

    // javafx timeline
    private void startTimer() {
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            secondsRemaining--;
            updateTimerDisplay();

            if (secondsRemaining <= 0) {
                stopTimer();
                isGameOver = true;
                gameWindow.statusLabel.setText("TIME'S UP! The word was: " + target.getText());
                gameWindow.statusLabel.setStyle("-fx-border-color: #ef4444; -fx-text-fill: #f87171;");
                gameWindow.inputField.setDisable(true);
                gameWindow.guessButton.setDisable(true);
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    private void stopTimer() {
        if (timer != null) {
            timer.stop();
        }
    }

    private void updateTimerDisplay() {
        int minutes = secondsRemaining / 60;
        int seconds = secondsRemaining % 60;
        gameWindow.timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }
}
