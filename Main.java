import java.util.*;
import java.io.*;

public class Main {

    public static double calculateSimilarity(float[] vecA, float[] vecB) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        for (int i = 0; i < vecA.length; i++) {
            dotProduct += vecA[i] * vecB[i];
            normA += Math.pow(vecA[i], 2);
            normB += Math.pow(vecB[i], 2);
        }

        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }

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
            double score = calculateSimilarity(target.getVector(), userWord.getVector());
            history.add(new Guess(userWord, score));

            // sort for ranking
            Collections.sort(history);

            // print guesses
            System.out.println("\n--- Top Guesses ---");
            for (int i = 0; i < history.size(); i++) {
                Guess g = history.get(i);
                System.out.printf("%d. %s (%.2f%%)\n", (i+1), g.getWord().getText(), g.getSimilarity() * 100);
            }
        }
        scanner.close();
    }
}