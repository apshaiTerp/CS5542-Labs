/**
 * Created by AC010168 on 2/8/2017.
 */
public class WordData implements Comparable<WordData> {

    private String word;

    private double weight;

    public WordData(String word, double weight) {
        this.word   = word;
        this.weight = weight;
    }

    public int compareTo(WordData wordData) {
        double result = wordData.weight - this.weight;
        if (result == 0) return word.compareTo(wordData.word);
        else if (result > 0) return 1;
        else return -1;
    }

    public String getWord() {
        return word;
    }

    public double getWeight() {
        return weight;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

}
