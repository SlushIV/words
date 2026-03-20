import javafx.application.Application;
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
    public void start(Stage stage) throws Exception {
        System.out.println("works");
        gameSetup();

        // --- UI SETUP ---
        TextField inputField = new TextField();
        inputField.setPromptText("Enter guess...");

        Button guessButton = new Button("Guess");
        TextArea historyDisplay = new TextArea("History will appear here...");
        historyDisplay.setEditable(false);

        // on guess button press
        guessButton.setOnAction(e -> {
            String input = inputField.getText().toLowerCase().trim();
            Word userWord = engine.getWord(input);

            if (userWord == null) {
                System.out.println("Word not in dictionary. Try another.");
            }

            // correct guess
            else if (userWord.equals(target)) {
                historyDisplay.appendText("\nCONGRATS! You found it!");

            // incorrect guess
            } else {

                // calculating score
                int score = engine.getRanking(input);
                Guess userGuess = new Guess(engine.getWord(input), score);

                if (!history.contains(userGuess)) {
                    history.add(userGuess);
                    Collections.sort(history);
                }

                // Update the UI text area
                StringBuilder sb = new StringBuilder("--- Guesses ---\n");
                for (Guess g : history) {
                    sb.append(g.getWord().getText()).append(" (").append(g.getScore()).append(")\n");
                }
                historyDisplay.setText(sb.toString());
            }
            inputField.clear();
        });

        // Display everything in a vertical column
        VBox layout = new VBox(10, inputField, guessButton, historyDisplay);
        Scene scene = new Scene(layout, 400, 500);

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
        Word target = engine.selectRandomWord();
        System.out.println(target.getText()); // for testing, remove in production

        // calculate similarity ranking for all words
        engine.rankSimilarity(target);
    }
}
