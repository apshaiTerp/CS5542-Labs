import org.openimaj.feature.local.list.LocalFeatureList;
import org.openimaj.feature.local.matcher.FastBasicKeypointMatcher;
import org.openimaj.feature.local.matcher.LocalFeatureMatcher;
import org.openimaj.feature.local.matcher.consistent.ConsistentLocalFeatureMatcher2d;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.feature.local.engine.DoGSIFTEngine;
import org.openimaj.image.feature.local.keypoints.Keypoint;
import org.openimaj.math.geometry.transforms.estimation.RobustAffineTransformEstimator;
import org.openimaj.math.model.fit.RANSAC;
import org.openimaj.video.Video;
import org.openimaj.video.xuggle.XuggleVideo;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by AC010168 on 2/7/2017.
 */
public class KeyFrameDetector {
    private static Video<MBFImage> video;
    private static List<MBFImage>  imageList   = new ArrayList<MBFImage>();
    private static List<Long>      timeStamp   = new ArrayList<Long>();
    private static List<Double>    mainPoints  = new ArrayList<Double>();

    private static final String    FILE_PATH   = "input/JW1.wmv";
    private static final String    OUTPUT_ROOT = "output/frames/frame";
    private static final String    MAIN_ROOT   = "output/mainframes/";

    public static void execute() throws RuntimeException {
        try {
            video = new XuggleVideo(new File(FILE_PATH));

            int imageCount = 0;
            for (MBFImage image : video) {
                BufferedImage bufferedFrame = ImageUtilities.createBufferedImage(image);
                imageCount++;

                String imageName = OUTPUT_ROOT + imageCount + ".jpg";
                File outputFile = new File(imageName);
                ImageIO.write(bufferedFrame, "jpg", outputFile);

                MBFImage cloneImage = image.clone();
                imageList.add(cloneImage);
                timeStamp.add(video.getTimeStamp());
            }

        } catch (Throwable t) {
            System.out.println ("Something bad happened during frame extraction: " + t.getMessage());
            t.printStackTrace();
            throw new RuntimeException("An extraction error was trapped", t);
        }

        try {
            for (int i = 0; i < imageList.size() - 1; i++) {
                MBFImage firstFrame  = imageList.get(i);
                MBFImage secondFrame = imageList.get(i + 1);

                DoGSIFTEngine engine = new DoGSIFTEngine();

                LocalFeatureList<Keypoint> firstKeypoints  = engine.findFeatures(firstFrame.flatten());
                LocalFeatureList<Keypoint> secondKeypoints = engine.findFeatures(secondFrame.flatten());

                RobustAffineTransformEstimator modelFitter = new RobustAffineTransformEstimator(5.0, 1500,
                        new RANSAC.PercentageInliersStoppingCondition(0.5));

                LocalFeatureMatcher<Keypoint> matcher = new ConsistentLocalFeatureMatcher2d<Keypoint>(
                        new FastBasicKeypointMatcher<Keypoint>(8), modelFitter);

                matcher.setModelFeatures(firstKeypoints);
                matcher.findMatches(secondKeypoints);

                double size = matcher.getMatches().size();
                mainPoints.add(size);

                System.out.println("Image " + (i + 1) + " of " + imageList.size() + ": " + size);
            }

            double max = Collections.max(mainPoints);
            for (int i = 0; i < mainPoints.size(); i++) {
                if ((i == 0) || ((mainPoints.get(i) / max) < 0.01)) {
                    BufferedImage bufferedFrame = ImageUtilities.createBufferedImageForDisplay(imageList.get(i + 1));

                    double imagePrefix = (mainPoints.get(i) / max);
                    String imageName   = MAIN_ROOT + i + "_" + imagePrefix + ".jpg";
                    File outputFile    = new File(imageName);

                    System.out.println ("Writing mainFrame for Image " + i);
                    ImageIO.write(bufferedFrame, "jpg", outputFile);
                }
            }

        } catch (Throwable t) {
            System.out.println ("Something bad happened during key frame identification: " + t.getMessage());
            t.printStackTrace();
            throw new RuntimeException("An identification error was trapped", t);
        }

    }


}
