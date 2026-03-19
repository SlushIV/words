import java.io.*;
import java.util.*;

public class WordEngine {
    private Map<String, Word> dictionaryMap = new HashMap<>();
    private Map<String, Integer> wordRanking = new HashMap<>();
    List<String> commonWords = new ArrayList<>();

    // selects a random word from the dictionary
    public Word selectRandomWord() {
        Random rand = new Random();

        // only select common words as target
        String key = commonWords.get(rand.nextInt(commonWords.size()));
        return dictionaryMap.get(key);
    }

    public void loadCommonWords(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));

        String line;
        while ((line = reader.readLine()) != null) {

            String[] parts = line.split(" ");

            for (String word : parts) {
                commonWords.add(word.toLowerCase());
            }
        }
        reader.close();
        System.out.println("Loaded " + commonWords.size() + " common words.");
    }

    public void loadVectors(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));

        String line;
        while ((line = reader.readLine()) != null) {

            String[] parts = line.split(" ");

            // word text
            String text = parts[0];

            // word vector
            float[] vector = new float[parts.length - 1];
            for (int i = 1; i < parts.length; i++) {
                vector[i - 1] = Float.parseFloat(parts[i]);
            }
            
            // create word object and map text to the object
            Word word = new Word(text, vector);
            dictionaryMap.put(text.toLowerCase(), word);
        }
        reader.close();
        System.out.println("Loaded " + dictionaryMap.size() + " words.");
    }

    public void rankSimilarity(Word target) {
    
        // calculate similarity for all words and sort them
        List<Word> words = new ArrayList<>(dictionaryMap.values());
        words.sort((word1, word2) -> Double.compare(
            calculateSimilarity(target.getVector(), word2.getVector()),
            calculateSimilarity(target.getVector(), word1.getVector())
        ));

        // store ranking in map
        wordRanking.clear();
        for (int i = 0; i < words.size(); i++) {
            wordRanking.put(words.get(i).getText(), i);
        }

    }

    public Word getWord(String key) {
        return dictionaryMap.get(key.toLowerCase());
    }

    public int getRanking(String key) {
        return wordRanking.get(key.toLowerCase());
    }

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

}