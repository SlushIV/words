import java.util.*;

public class Main {

    public static void main(String[] args) {
            
        Random random = new Random();

        // init random target word
        String targetWord = "";

        // init words
        Word[] words = new Word[10];

        for (int i = 0; i < words.length; i++) {
            words[i] = new Word("text");
            words[i].calculateSimilarity(targetWord);
        }

    }
}