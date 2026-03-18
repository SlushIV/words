public class Word {

    private String text;
    private int similarity;

    // init words with similarity 0 because target word unknown
    public Word(String text) {
        this.text = text;
        this.similarity = 0;
    }

    public void calculateSimilarity(String other) {
        // using vectors calculate how similar words are
        this.setSimilarity(similarity);
    }

    public String getText() {
        return text;
    }

    public int getSimilarity() {
        return similarity;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setSimilarity(int similarity) {
        this.similarity = similarity;
    }
    
}
