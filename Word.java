import java.util.Objects;

public class Word {

    private String text;
    private float[] vector;

    public Word(String text, float[] vector) {
        this.text = text;
        this.vector = vector;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Word word = (Word) o;
        // compare text of the words
        return Objects.equals(this.text, word.text);
    }

    public String getText() {
        return text;
    }

    public float[] getVector() {
        return vector;
    }
}
