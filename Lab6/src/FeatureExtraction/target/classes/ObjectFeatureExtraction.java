import org.openimaj.feature.local.list.LocalFeatureList;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.feature.local.engine.DoGSIFTEngine;
import org.openimaj.image.feature.local.keypoints.Keypoint;

import java.io.*;

/**
 * Created by Naga on 20-09-2016.
 */
public class ObjectFeatureExtraction {

    //TODO - Will need to improve to run for all three image types

    public static void main(String args[]) throws IOException {
        String inputFolder = "data2/";
        String outputFolder = "output2/";
        String[] IMAGE_CATEGORIES = {"Dinosaur", "Coworker", "Pratt"};

        int input_class = 0;

        MBFImage mbfImage1 = ImageUtilities.readMBF(new File(inputFolder + "dino2.jpg"));
        MBFImage mbfImage2 = ImageUtilities.readMBF(new File(inputFolder + "coworker2.jpg"));
        MBFImage mbfImage3 = ImageUtilities.readMBF(new File(inputFolder + "pratt3.jpg"));

        DoGSIFTEngine doGSIFTEngine = new DoGSIFTEngine();

        LocalFeatureList<Keypoint> features1 = doGSIFTEngine.findFeatures(mbfImage1.flatten());
        LocalFeatureList<Keypoint> features2 = doGSIFTEngine.findFeatures(mbfImage2.flatten());
        LocalFeatureList<Keypoint> features3 = doGSIFTEngine.findFeatures(mbfImage3.flatten());

        FileWriter fw = new FileWriter(outputFolder + "testfeatures.txt");
        BufferedWriter bw = new BufferedWriter(fw);

        for (int i = 0; i < features1.size(); i++) {
            double c[] = features1.get(i).getFeatureVector().asDoubleVector();
            bw.write(input_class + ",");
            for (int j = 0; j < c.length; j++) {
                bw.write(c[j] + " ");
            }
            bw.newLine();
        }
        bw.flush();
        input_class++;

        for (int i = 0; i < features2.size(); i++) {
            double c[] = features2.get(i).getFeatureVector().asDoubleVector();
            bw.write(input_class + ",");
            for (int j = 0; j < c.length; j++) {
                bw.write(c[j] + " ");
            }
            bw.newLine();
        }
        bw.flush();
        input_class++;

        for (int i = 0; i < features3.size(); i++) {
            double c[] = features3.get(i).getFeatureVector().asDoubleVector();
            bw.write(input_class + ",");
            for (int j = 0; j < c.length; j++) {
                bw.write(c[j] + " ");
            }
            bw.newLine();
        }
        bw.flush();
        bw.close();
    }


}
