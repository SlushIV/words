public class Word {

    private String text;
    private float[] vector;

    public Word(String text, float[] vector) {
        this.text = text;
        this.vector = vector;
    }

    public String getText() {
        return text;
    }

    public float[] getVector() {
        return vector;
    }
}
