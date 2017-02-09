import java.util.List;

/**
 * Created by AC010168 on 2/7/2017.
 */
public class VideoSummary {

    public static void main(String[] args) {
        //We need to start by parsing our video into images

        try {
            KeyFrameDetector.execute();
        } catch (Throwable t) {
            System.out.println ("KeyFrameDetector threw an Exception.  Exiting Program");
            return;
        }

        List<WordData> mostUsedTerms = null;
        try {
            mostUsedTerms = ImageAnnotater.execute();
        } catch (Throwable t) {
            System.out.println ("ImageAnnotater threw an Exception.  Exiting Program");
            return;
        }

        try {
            SummaryGenerator.execute(mostUsedTerms);
        } catch (Throwable t) {
            System.out.println ("SummaryGenerator threw an Exception.  Exiting Program");
            return;
        }
    }
}
