import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
        inputField.setMaxWidth(600);
        inputField.setPrefWidth(600);
        inputField.setPrefHeight(50);

        Button guessButton = new Button("SUBMIT GUESS");
        guessButton.setMaxWidth(600);
        guessButton.setPrefWidth(600);
        guessButton.setPrefHeight(50);

        TextArea historyDisplay = new TextArea("Waiting for your first guess...");
        historyDisplay.setEditable(false);
        historyDisplay.setPrefHeight(400);
        historyDisplay.setMaxWidth(600);

        // on guess button press
        guessButton.setOnAction(e -> {
            String input = inputField.getText().toLowerCase().trim();
            Word userWord = engine.getWord(input);

            // invalid guess
            if (userWord == null) {
                System.out.println("Word not in dictionary. Try another.");
            }

            // correct guess
            else if (userWord.equals(target)) {
                historyDisplay.setText("\nCONGRATS! You found it! The word was: " + target.getText());
                System.out.println("equal");

            // incorrect guess
            } else {

                // calculating score
                int score = engine.getRanking(input);
                Guess userGuess = new Guess(engine.getWord(input), score);

                if (history.contains(userGuess)) {
                    // idk some indicator placeholder

                } else {
                    history.add(userGuess);
                    Collections.sort(history);

                    // Update the UI text area
                    StringBuilder sb = new StringBuilder("--- Guesses ---\n");
                    for (Guess g : history) {
                        sb.append(g.getWord().getText()).append(" (").append(g.getScore()).append(")\n");
                    }
                    historyDisplay.setText(sb.toString());
                }
            }
            inputField.clear();
        });

        // vertical layout
        VBox layout = new VBox(25, inputField, guessButton, historyDisplay);
        layout.setAlignment(Pos.CENTER);
        layout.setMaxWidth(800);
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
