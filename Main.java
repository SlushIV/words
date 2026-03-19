import java.util.*;
import java.io.*;

public class Main {

    public static void main(String[] args) {

        WordEngine engine = new WordEngine();
        Scanner scanner = new Scanner(System.in);
        List<Guess> history = new ArrayList<>();

        // dictionary setup
        try {
            System.out.println("Loading dictionary...");
            engine.loadVectors("glove.6B.50d.txt");
        } catch (IOException e) {
            System.err.println("file error");
            scanner.close();
            return;
        }

        // common words setup
        try {
            System.out.println("Loading common words...");
            engine.loadCommonWords("google-10000-english.txt");
        } catch (IOException e) {
            System.err.println("file error");
            scanner.close();
            return;
        }

        // secret word setup
        Word target = engine.selectRandomWord();
        System.out.println(target.getText()); // for testing, remove in production

        // calculate similarity ranking for all words
        engine.rankSimilarity(target);

        // game
        System.out.println("\n--- Game Started! ---");

        while (true) {
            System.out.print("\nEnter guess: ");
            String input = scanner.nextLine().toLowerCase().trim();

            // correct guess
            if (input.equals(target.getText())) {
                System.out.println("CONGRATS! You found the word: " + target.getText());
                break;
            }

            // invalid guess
            Word userWord = engine.getWord(input);
            if (userWord == null) {
                System.out.println("Word not in dictionary. Try another.");
                continue;
            }

            // already guessed
            for (Guess g : history) {
                if (g.getWord().equals(userWord)) {
                    System.out.println("You've already guessed that word. Try another.");
                    continue;
                }
            }

            // incorrect guess -> calculate similarity and add to history
            int score = engine.getRanking(input);
            history.add(new Guess(userWord, score));

            // sort for ranking
            Collections.sort(history);

            // print guesses from history list
            System.out.println("\n--- Guesses ---");
            for (int i = 0; i < history.size(); i++) {
                Guess g = history.get(i);
                System.out.printf("%d. %s (%d)\n", (i+1), g.getWord().getText(), g.getScore());
            }
        }
        scanner.close();
    }
}