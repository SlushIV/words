public class Guess implements Comparable<Guess> {
    private final Word word;
    private final int score;

    public Guess(Word word, int score) {
        this.word = word;
        this.score = score;
    }

    // This allows you to sort guesses from "closest" to "farthest"
    @Override
    public int compareTo(Guess other) {
        return Integer.compare(this.score, other.score);
    }

    public Word getWord() {
        return word;
    }

    public int getScore() {
        return score;
    }
}