import java.util.List;

/**
 * Created by AC010168 on 2/8/2017.
 */
public class SummaryGenerator {

    public static void execute(List<WordData> mostUsedTerms) throws Exception {
        System.out.println ("There were " + mostUsedTerms.size() + " unique annotations found");

        String result = "";
        System.out.println ("Displaying the top 10 most frequent words");
        //We're going to use the most popular 6 words
        for (int i = 0; i < 10; i++) {
            WordData data = mostUsedTerms.get(i);
            System.out.println (data.getWord() + ": " + data.getWeight());
            if (i > 0)
                result += ", ";
            result += data.getWord();
        }

        System.out.println ("This video features the following elements: " + result);
    }
}
