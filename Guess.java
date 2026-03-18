public class Guess implements Comparable<Guess> {
    private final Word word;
    private final double similarity;

    public Guess(Word word, double similarity) {
        this.word = word;
        this.similarity = similarity;
    }

    // This allows you to sort guesses from "closest" to "farthest"
    @Override
    public int compareTo(Guess other) {
        return Double.compare(other.similarity, this.similarity);
    }

    public Word getWord() {
        return word;
    }

    public double getSimilarity() {
        return similarity;
    }
}