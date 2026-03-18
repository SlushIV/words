import java.io.*;
import java.util.*;

public class WordEngine {
    private Map<String, Word> dictionaryMap = new HashMap<>();

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

    public Word getWord(String key) {
        return dictionaryMap.get(key.toLowerCase());
    }
}