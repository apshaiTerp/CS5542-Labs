import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.api.ClarifaiResponse;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.input.image.ClarifaiImage;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;
import okhttp3.OkHttpClient;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;

import java.io.File;
import java.util.*;

/**
 * Created by AC010168 on 2/7/2017.
 */
public class ImageAnnotater {

    private final static String INPUT_ROOT = "output/mainframes";

    public static List<WordData> execute() throws Exception {
        List<WordData> mostUsedTerms = new LinkedList<WordData>();
        try {
            final ClarifaiClient client = new ClarifaiBuilder("FAvm9tkD6_K7wWDlA6eidzFBmgXO3LgKqSVTmS7j",
                    "7y6nYIoYbjljAToHf9E9Gkm6InMriR7bkBjGHvLe").client(new OkHttpClient()).buildSync(); // or use .build() to get a Future<ClarifaiClient>
            client.getToken();

            File fileRoot = new File(INPUT_ROOT);
            File[] allImages = fileRoot.listFiles();

            for (int i = 0; i < allImages.length; i++) {
                ClarifaiResponse response =
                        client.getDefaultModels().generalModel().predict().withInputs(
                        ClarifaiInput.forImage(ClarifaiImage.of(allImages[i]))).executeSync();
                List<ClarifaiOutput<Concept>> predictions = (List<ClarifaiOutput<Concept>>)response.get();

                MBFImage image = ImageUtilities.readMBF(allImages[i]);

                System.out.println ("************  " + allImages[i] + "  ************");
                List<Concept> data = predictions.get(0).data();
                for (int j = 0; j < data.size(); j++) {
                    System.out.println (data.get(j).name() + " - " + data.get(j).value());

                    boolean found = false;
                    for (int k = 0; k < mostUsedTerms.size(); k++) {
                        if (mostUsedTerms.get(k).getWord().equalsIgnoreCase(data.get(j).name())) {
                            WordData updateData = mostUsedTerms.get(k);
                            updateData.setWeight(updateData.getWeight() + data.get(j).value());
                            mostUsedTerms.set(k, updateData);
                            found = true;
                            break;
                        }
                    }
                    if (!found)
                        mostUsedTerms.add(new WordData(data.get(j).name(), data.get(j).value()));
                }
            }
            Collections.sort(mostUsedTerms);
            return mostUsedTerms;
        } catch (Throwable t) {
            System.out.println ("Problems doing image annotation: " + t.getMessage());
            t.printStackTrace();
            throw new RuntimeException("There were problems in the Image Annotation", t);
        }
    }
}
