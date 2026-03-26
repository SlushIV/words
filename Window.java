import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class Window extends Application {

    private final WordEngine engine = new WordEngine();
    private final List<Guess> history = new ArrayList<>();
    private Word target;

    @Override
    public void start(Stage stage) {

        gameSetup();

        TextField inputField = new TextField();
        inputField.setPromptText("Type your word here...");

        Button guessButton = new Button("SUBMIT GUESS");

        Label status = new Label("Waiting for your first guess...");
        status.setPrefWidth(600);
        status.setMaxWidth(600);
        status.setPrefHeight(50);

        VBox guessContainer = new VBox(5);
        guessContainer.setAlignment(Pos.CENTER);

        ScrollPane scrollPane = new ScrollPane(guessContainer);
        scrollPane.setFitToWidth(true);      // Makes the VBox match the ScrollPane's width
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Hide horizontal
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Hide vertical

        // on guess button press
        guessButton.setOnAction(e -> {
            String input = inputField.getText().toLowerCase().trim();
            Word userWord = engine.getWord(input);

            // invalid guess
            if (userWord == null) {
                status.setText("word not found");
            }

            // correct guess
            else if (userWord.equals(target)) {
                status.setText("CONGRATS! You found it! The word was: " + target.getText());

            // incorrect guess
            } else {

                // calculating score
                int score = engine.getRanking(input);
                Guess userGuess = new Guess(engine.getWord(input), score);

                if (history.contains(userGuess)) {
                    status.setText("already guessed dumbahh");

                } else {
                    status.setText("try again");

                    history.add(userGuess);
                    Collections.sort(history);
                    int index = history.indexOf(userGuess);

                    Label left = new Label(input);
                    Label right = new Label(score + "");
                    Region spacer = new Region();

                    HBox.setHgrow(spacer, Priority.ALWAYS); // This forces the spacer to take all available space
                    HBox guess = new HBox(left, spacer, right);
                    guess.getStyleClass().add("guess-row");

                    guessContainer.getChildren().add(index, guess);
                }
            }
            inputField.clear();
        });

        // vertical layout
        VBox layout = new VBox(25, inputField, guessButton, status, scrollPane);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setMaxWidth(900);
        layout.setPadding(new Insets(30));

        Scene scene = new Scene(layout, 1000, 700);

        // LINK YOUR CSS FILE HERE
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());

        stage.setScene(scene);
        stage.setTitle("Word Similarity Game");
        stage.show();
    }

    private void gameSetup() {
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
}
