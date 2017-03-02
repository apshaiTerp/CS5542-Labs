import Jama.Matrix;
import org.openimaj.feature.local.list.LocalFeatureList;
import org.openimaj.feature.local.matcher.FastBasicKeypointMatcher;
import org.openimaj.feature.local.matcher.consistent.ConsistentLocalFeatureMatcher2d;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.colour.Transforms;
import org.openimaj.image.feature.local.engine.DoGSIFTEngine;
import org.openimaj.image.feature.local.keypoints.Keypoint;
import org.openimaj.image.renderer.MBFImageRenderer;
import org.openimaj.math.geometry.point.Point2d;
import org.openimaj.math.geometry.transforms.HomographyModel;
import org.openimaj.math.geometry.transforms.HomographyRefinement;
import org.openimaj.math.geometry.transforms.MatrixTransformProvider;
import org.openimaj.math.geometry.transforms.check.TransformMatrixConditionCheck;
import org.openimaj.math.geometry.transforms.estimation.RobustHomographyEstimator;
import org.openimaj.math.model.fit.RANSAC;
import org.openimaj.video.Video;
import org.openimaj.video.xuggle.XuggleVideo;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
//import org.openimaj.math.geometry.shape.Polygon;

/**
 * Created by Naga on 16-09-2016.
 */
public class ObjectDetection {

    public static void main(String args[]) throws IOException {
        ObjectMainDetection objectMainDetection = new ObjectMainDetection();
    }
}

class ObjectMainDetection {
    private ConsistentLocalFeatureMatcher2d<Keypoint> matcher1;
    private ConsistentLocalFeatureMatcher2d<Keypoint> matcher2;
    private ConsistentLocalFeatureMatcher2d<Keypoint> matcher3;
    private ConsistentLocalFeatureMatcher2d<Keypoint> matcher4;
    private ConsistentLocalFeatureMatcher2d<Keypoint> matcher5;
    private ConsistentLocalFeatureMatcher2d<Keypoint> matcher6;
    private ConsistentLocalFeatureMatcher2d<Keypoint> matcher7;
    private ConsistentLocalFeatureMatcher2d<Keypoint> matcher8;
    private ConsistentLocalFeatureMatcher2d<Keypoint> matcher9;
    private ConsistentLocalFeatureMatcher2d<Keypoint> matcher10;
    private ConsistentLocalFeatureMatcher2d<Keypoint> matcher11;
    private ConsistentLocalFeatureMatcher2d<Keypoint> matcher12;
    private ConsistentLocalFeatureMatcher2d<Keypoint> matcher13;
    final DoGSIFTEngine engine;

    private MBFImage modelImage1, modelImage2, modelImage3, modelImage4, modelImage5, modelImage6, modelImage7, modelImage8,
            modelImage9, modelImage10, modelImage11, modelImage12, modelImage13;


    public ObjectMainDetection() throws IOException {
        this.engine = new DoGSIFTEngine();
        this.engine.getOptions().setDoubleInitialImage(true);
        this.matcher1 = new ConsistentLocalFeatureMatcher2d<Keypoint>(
                new FastBasicKeypointMatcher<Keypoint>(8));
        this.matcher2 = new ConsistentLocalFeatureMatcher2d<Keypoint>(
                new FastBasicKeypointMatcher<Keypoint>(8));
        this.matcher3 = new ConsistentLocalFeatureMatcher2d<Keypoint>(
                new FastBasicKeypointMatcher<Keypoint>(8));
        this.matcher4 = new ConsistentLocalFeatureMatcher2d<Keypoint>(
                new FastBasicKeypointMatcher<Keypoint>(8));

        this.matcher5 = new ConsistentLocalFeatureMatcher2d<Keypoint>(
                new FastBasicKeypointMatcher<Keypoint>(8));
        this.matcher6 = new ConsistentLocalFeatureMatcher2d<Keypoint>(
                new FastBasicKeypointMatcher<Keypoint>(8));
        this.matcher7 = new ConsistentLocalFeatureMatcher2d<Keypoint>(
                new FastBasicKeypointMatcher<Keypoint>(8));
        this.matcher8 = new ConsistentLocalFeatureMatcher2d<Keypoint>(
                new FastBasicKeypointMatcher<Keypoint>(8));

        this.matcher9  = new ConsistentLocalFeatureMatcher2d<Keypoint>(
                new FastBasicKeypointMatcher<Keypoint>(8));
        this.matcher10 = new ConsistentLocalFeatureMatcher2d<Keypoint>(
                new FastBasicKeypointMatcher<Keypoint>(8));
        this.matcher11 = new ConsistentLocalFeatureMatcher2d<Keypoint>(
                new FastBasicKeypointMatcher<Keypoint>(8));
        this.matcher12 = new ConsistentLocalFeatureMatcher2d<Keypoint>(
                new FastBasicKeypointMatcher<Keypoint>(8));
        this.matcher13 = new ConsistentLocalFeatureMatcher2d<Keypoint>(
                new FastBasicKeypointMatcher<Keypoint>(8));

        final RobustHomographyEstimator ransac = new RobustHomographyEstimator(0.5, 1500,
                new RANSAC.PercentageInliersStoppingCondition(0.6), HomographyRefinement.NONE,
                new TransformMatrixConditionCheck<HomographyModel>(10000));
        this.matcher1.setFittingModel(ransac);
        this.matcher2.setFittingModel(ransac);
        this.matcher3.setFittingModel(ransac);
        this.matcher4.setFittingModel(ransac);
        this.matcher5.setFittingModel(ransac);
        this.matcher6.setFittingModel(ransac);
        this.matcher7.setFittingModel(ransac);
        this.matcher8.setFittingModel(ransac);
        this.matcher9.setFittingModel(ransac);
        this.matcher10.setFittingModel(ransac);
        this.matcher11.setFittingModel(ransac);
        this.matcher12.setFittingModel(ransac);
        this.matcher13.setFittingModel(ransac);

        LoadReferenceObject();

        StartVideo();
    }

    public void StartVideo() throws IOException {
        Video<MBFImage> video = new XuggleVideo(new File("data2/JW1.wmv"));
        int count1 = 0, count2 = 0, count3 = 0; int count4 = 0;
        int count5 = 0, count6 = 0, count7 = 0; int count8 = 0;
        int count9 = 0, count10 = 0, count11 = 0; int count12 = 0, count13 = 0;

        String o1 = "output2/features.txt";
        FileWriter fw = new FileWriter(o1);
        BufferedWriter bw = new BufferedWriter(fw);
        for (MBFImage mbfImage : video) {
            final LocalFeatureList<Keypoint> kpl = this.engine.findFeatures(Transforms.calculateIntensityNTSC(mbfImage));
            final MBFImageRenderer renderer = mbfImage.createRenderer();
            renderer.drawPoints(kpl, RGBColour.MAGENTA, 3);

            if (this.matcher1.findMatches(kpl)
                    && ((MatrixTransformProvider) this.matcher1.getModel()).getTransform().cond() < 1e6 ) {
                try {
                    final Matrix boundsToPoly = ((MatrixTransformProvider) this.matcher1.getModel()).getTransform()
                            .inverse();

                    if (modelImage1.getBounds().transform(boundsToPoly).isConvex()) {

                        renderer.drawShape(this.modelImage1.getBounds().transform(boundsToPoly), 3, RGBColour.RED);

                        if(count1 <= 20){
                            List<Point2d> vertices = this.modelImage1.getBounds().transform(boundsToPoly).asPolygon().getVertices();
                            int x[] = new int[4], y[] = new int[4];
                            for (int i = 0; i < vertices.size(); i++) {
                                x[i] = (int) vertices.get(i).getX();
                                y[i] = (int) vertices.get(i).getY();
                            }
                            Polygon polygon = new Polygon(x, y, 4);
                            for (int i = 0; i < kpl.size(); i++) {
                                if (polygon.contains(kpl.get(i).getX(), kpl.get(i).getY())) {
                                    double c[] = kpl.get(i).getFeatureVector().asDoubleVector();
                                    bw.write("0,");
                                    for (int j = 0; j < c.length; j++) {
                                        bw.write(c[j] + " ");
                                    }
                                    bw.newLine();
                                }
                            }

                            count1++;
                        }

                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }
            if (this.matcher2.findMatches(kpl)
                    && ((MatrixTransformProvider) this.matcher2.getModel()).getTransform().cond() < 1e6 ) {
                try {
                    final Matrix boundsToPoly = ((MatrixTransformProvider) this.matcher2.getModel()).getTransform()
                            .inverse();

                    renderer.drawShape(this.modelImage2.getBounds().transform(boundsToPoly), 3, RGBColour.RED);

                    if(count2 <= 20){
                        if (modelImage2.getBounds().transform(boundsToPoly).isConvex()) {
                            List<Point2d> vertices = this.modelImage2.getBounds().transform(boundsToPoly).asPolygon().getVertices();
                            int x[] = new int[4], y[] = new int[4];
                            for (int i = 0; i < vertices.size(); i++) {
                                x[i] = (int) vertices.get(i).getX();
                                y[i] = (int) vertices.get(i).getY();
                            }
                            Polygon polygon = new Polygon(x, y, 4);
                            for (int i = 0; i < kpl.size(); i++) {
                                if (polygon.contains(kpl.get(i).getX(), kpl.get(i).getY())) {
                                    double c[] = kpl.get(i).getFeatureVector().asDoubleVector();
                                    bw.write("1,");
                                    for (int j = 0; j < c.length; j++) {
                                        bw.write(c[j] + " ");
                                    }
                                    bw.newLine();
                                }
                            }

                            count2++;
                    }

                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }
            if (this.matcher3.findMatches(kpl)
                    && ((MatrixTransformProvider) this.matcher3.getModel()).getTransform().cond() < 1e6) {
                try {
                    final Matrix boundsToPoly = ((MatrixTransformProvider) this.matcher3.getModel()).getTransform()
                            .inverse();

                    if (modelImage3.getBounds().transform(boundsToPoly).isConvex()) {

                        renderer.drawShape(this.modelImage3.getBounds().transform(boundsToPoly), 3, RGBColour.RED);
                        if (count3 <= 20){
                            List<Point2d> vertices = this.modelImage3.getBounds().transform(boundsToPoly).asPolygon().getVertices();
                            int x[] = new int[4], y[] = new int[4];
                            for (int i = 0; i < vertices.size(); i++) {
                                x[i] = (int) vertices.get(i).getX();
                                y[i] = (int) vertices.get(i).getY();
                            }
                            Polygon polygon = new Polygon(x, y, 4);
                            for (int i = 0; i < kpl.size(); i++) {
                                if (polygon.contains(kpl.get(i).getX(), kpl.get(i).getY())) {
                                    double c[] = kpl.get(i).getFeatureVector().asDoubleVector();
                                    bw.write("2,");
                                    for (int j = 0; j < c.length; j++) {
                                        bw.write(c[j] + " ");
                                    }
                                    bw.newLine();
                                }
                            }

                            count3++;
                        }

                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }
            if (this.matcher4.findMatches(kpl)
                    && ((MatrixTransformProvider) this.matcher4.getModel()).getTransform().cond() < 1e6 ) {
                try {
                    final Matrix boundsToPoly = ((MatrixTransformProvider) this.matcher4.getModel()).getTransform()
                            .inverse();

                    if (modelImage4.getBounds().transform(boundsToPoly).isConvex()) {

                        renderer.drawShape(this.modelImage4.getBounds().transform(boundsToPoly), 3, RGBColour.RED);

                        if(count4 <= 20){
                            List<Point2d> vertices = this.modelImage4.getBounds().transform(boundsToPoly).asPolygon().getVertices();
                            int x[] = new int[4], y[] = new int[4];
                            for (int i = 0; i < vertices.size(); i++) {
                                x[i] = (int) vertices.get(i).getX();
                                y[i] = (int) vertices.get(i).getY();
                            }
                            Polygon polygon = new Polygon(x, y, 4);
                            for (int i = 0; i < kpl.size(); i++) {
                                if (polygon.contains(kpl.get(i).getX(), kpl.get(i).getY())) {
                                    double c[] = kpl.get(i).getFeatureVector().asDoubleVector();
                                    bw.write("3,");
                                    for (int j = 0; j < c.length; j++) {
                                        bw.write(c[j] + " ");
                                    }
                                    bw.newLine();
                                }
                            }

                            count4++;
                        }

                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }
            if (this.matcher5.findMatches(kpl)
                    && ((MatrixTransformProvider) this.matcher5.getModel()).getTransform().cond() < 1e6 ) {
                try {
                    final Matrix boundsToPoly = ((MatrixTransformProvider) this.matcher5.getModel()).getTransform()
                            .inverse();

                    if (modelImage5.getBounds().transform(boundsToPoly).isConvex()) {

                        renderer.drawShape(this.modelImage5.getBounds().transform(boundsToPoly), 3, RGBColour.RED);

                        if(count1 <= 20){
                            List<Point2d> vertices = this.modelImage5.getBounds().transform(boundsToPoly).asPolygon().getVertices();
                            int x[] = new int[4], y[] = new int[4];
                            for (int i = 0; i < vertices.size(); i++) {
                                x[i] = (int) vertices.get(i).getX();
                                y[i] = (int) vertices.get(i).getY();
                            }
                            Polygon polygon = new Polygon(x, y, 4);
                            for (int i = 0; i < kpl.size(); i++) {
                                if (polygon.contains(kpl.get(i).getX(), kpl.get(i).getY())) {
                                    double c[] = kpl.get(i).getFeatureVector().asDoubleVector();
                                    bw.write("4,");
                                    for (int j = 0; j < c.length; j++) {
                                        bw.write(c[j] + " ");
                                    }
                                    bw.newLine();
                                }
                            }

                            count5++;
                        }

                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }
            if (this.matcher6.findMatches(kpl)
                    && ((MatrixTransformProvider) this.matcher6.getModel()).getTransform().cond() < 1e6 ) {
                try {
                    final Matrix boundsToPoly = ((MatrixTransformProvider) this.matcher6.getModel()).getTransform()
                            .inverse();

                    renderer.drawShape(this.modelImage6.getBounds().transform(boundsToPoly), 3, RGBColour.RED);

                    if(count6 <= 20){
                        if (modelImage6.getBounds().transform(boundsToPoly).isConvex()) {
                            List<Point2d> vertices = this.modelImage6.getBounds().transform(boundsToPoly).asPolygon().getVertices();
                            int x[] = new int[4], y[] = new int[4];
                            for (int i = 0; i < vertices.size(); i++) {
                                x[i] = (int) vertices.get(i).getX();
                                y[i] = (int) vertices.get(i).getY();
                            }
                            Polygon polygon = new Polygon(x, y, 4);
                            for (int i = 0; i < kpl.size(); i++) {
                                if (polygon.contains(kpl.get(i).getX(), kpl.get(i).getY())) {
                                    double c[] = kpl.get(i).getFeatureVector().asDoubleVector();
                                    bw.write("5,");
                                    for (int j = 0; j < c.length; j++) {
                                        bw.write(c[j] + " ");
                                    }
                                    bw.newLine();
                                }
                            }

                            count6++;
                        }

                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }
            if (this.matcher7.findMatches(kpl)
                    && ((MatrixTransformProvider) this.matcher7.getModel()).getTransform().cond() < 1e6) {
                try {
                    final Matrix boundsToPoly = ((MatrixTransformProvider) this.matcher7.getModel()).getTransform()
                            .inverse();

                    if (modelImage7.getBounds().transform(boundsToPoly).isConvex()) {

                        renderer.drawShape(this.modelImage7.getBounds().transform(boundsToPoly), 3, RGBColour.RED);
                        if (count7 <= 20){
                            List<Point2d> vertices = this.modelImage7.getBounds().transform(boundsToPoly).asPolygon().getVertices();
                            int x[] = new int[4], y[] = new int[4];
                            for (int i = 0; i < vertices.size(); i++) {
                                x[i] = (int) vertices.get(i).getX();
                                y[i] = (int) vertices.get(i).getY();
                            }
                            Polygon polygon = new Polygon(x, y, 4);
                            for (int i = 0; i < kpl.size(); i++) {
                                if (polygon.contains(kpl.get(i).getX(), kpl.get(i).getY())) {
                                    double c[] = kpl.get(i).getFeatureVector().asDoubleVector();
                                    bw.write("6,");
                                    for (int j = 0; j < c.length; j++) {
                                        bw.write(c[j] + " ");
                                    }
                                    bw.newLine();
                                }
                            }

                            count7++;
                        }

                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }
            if (this.matcher8.findMatches(kpl)
                    && ((MatrixTransformProvider) this.matcher8.getModel()).getTransform().cond() < 1e6 ) {
                try {
                    final Matrix boundsToPoly = ((MatrixTransformProvider) this.matcher8.getModel()).getTransform()
                            .inverse();

                    if (modelImage8.getBounds().transform(boundsToPoly).isConvex()) {

                        renderer.drawShape(this.modelImage8.getBounds().transform(boundsToPoly), 3, RGBColour.RED);

                        if(count8 <= 20){
                            List<Point2d> vertices = this.modelImage8.getBounds().transform(boundsToPoly).asPolygon().getVertices();
                            int x[] = new int[4], y[] = new int[4];
                            for (int i = 0; i < vertices.size(); i++) {
                                x[i] = (int) vertices.get(i).getX();
                                y[i] = (int) vertices.get(i).getY();
                            }
                            Polygon polygon = new Polygon(x, y, 4);
                            for (int i = 0; i < kpl.size(); i++) {
                                if (polygon.contains(kpl.get(i).getX(), kpl.get(i).getY())) {
                                    double c[] = kpl.get(i).getFeatureVector().asDoubleVector();
                                    bw.write("7,");
                                    for (int j = 0; j < c.length; j++) {
                                        bw.write(c[j] + " ");
                                    }
                                    bw.newLine();
                                }
                            }

                            count8++;
                        }

                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }
            if (this.matcher9.findMatches(kpl)
                    && ((MatrixTransformProvider) this.matcher9.getModel()).getTransform().cond() < 1e6 ) {
                try {
                    final Matrix boundsToPoly = ((MatrixTransformProvider) this.matcher9.getModel()).getTransform()
                            .inverse();

                    if (modelImage9.getBounds().transform(boundsToPoly).isConvex()) {

                        renderer.drawShape(this.modelImage9.getBounds().transform(boundsToPoly), 3, RGBColour.RED);

                        if(count9 <= 20){
                            List<Point2d> vertices = this.modelImage9.getBounds().transform(boundsToPoly).asPolygon().getVertices();
                            int x[] = new int[4], y[] = new int[4];
                            for (int i = 0; i < vertices.size(); i++) {
                                x[i] = (int) vertices.get(i).getX();
                                y[i] = (int) vertices.get(i).getY();
                            }
                            Polygon polygon = new Polygon(x, y, 4);
                            for (int i = 0; i < kpl.size(); i++) {
                                if (polygon.contains(kpl.get(i).getX(), kpl.get(i).getY())) {
                                    double c[] = kpl.get(i).getFeatureVector().asDoubleVector();
                                    bw.write("8,");
                                    for (int j = 0; j < c.length; j++) {
                                        bw.write(c[j] + " ");
                                    }
                                    bw.newLine();
                                }
                            }

                            count9++;
                        }

                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }
            if (this.matcher10.findMatches(kpl)
                    && ((MatrixTransformProvider) this.matcher10.getModel()).getTransform().cond() < 1e6 ) {
                try {
                    final Matrix boundsToPoly = ((MatrixTransformProvider) this.matcher10.getModel()).getTransform()
                            .inverse();

                    renderer.drawShape(this.modelImage10.getBounds().transform(boundsToPoly), 3, RGBColour.RED);

                    if(count2 <= 20){
                        if (modelImage10.getBounds().transform(boundsToPoly).isConvex()) {
                            List<Point2d> vertices = this.modelImage10.getBounds().transform(boundsToPoly).asPolygon().getVertices();
                            int x[] = new int[4], y[] = new int[4];
                            for (int i = 0; i < vertices.size(); i++) {
                                x[i] = (int) vertices.get(i).getX();
                                y[i] = (int) vertices.get(i).getY();
                            }
                            Polygon polygon = new Polygon(x, y, 4);
                            for (int i = 0; i < kpl.size(); i++) {
                                if (polygon.contains(kpl.get(i).getX(), kpl.get(i).getY())) {
                                    double c[] = kpl.get(i).getFeatureVector().asDoubleVector();
                                    bw.write("9,");
                                    for (int j = 0; j < c.length; j++) {
                                        bw.write(c[j] + " ");
                                    }
                                    bw.newLine();
                                }
                            }

                            count10++;
                        }

                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }
            if (this.matcher11.findMatches(kpl)
                    && ((MatrixTransformProvider) this.matcher11.getModel()).getTransform().cond() < 1e6) {
                try {
                    final Matrix boundsToPoly = ((MatrixTransformProvider) this.matcher11.getModel()).getTransform()
                            .inverse();

                    if (modelImage11.getBounds().transform(boundsToPoly).isConvex()) {

                        renderer.drawShape(this.modelImage11.getBounds().transform(boundsToPoly), 3, RGBColour.RED);
                        if (count11 <= 20){
                            List<Point2d> vertices = this.modelImage11.getBounds().transform(boundsToPoly).asPolygon().getVertices();
                            int x[] = new int[4], y[] = new int[4];
                            for (int i = 0; i < vertices.size(); i++) {
                                x[i] = (int) vertices.get(i).getX();
                                y[i] = (int) vertices.get(i).getY();
                            }
                            Polygon polygon = new Polygon(x, y, 4);
                            for (int i = 0; i < kpl.size(); i++) {
                                if (polygon.contains(kpl.get(i).getX(), kpl.get(i).getY())) {
                                    double c[] = kpl.get(i).getFeatureVector().asDoubleVector();
                                    bw.write("10,");
                                    for (int j = 0; j < c.length; j++) {
                                        bw.write(c[j] + " ");
                                    }
                                    bw.newLine();
                                }
                            }

                            count11++;
                        }

                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }
            if (this.matcher12.findMatches(kpl)
                    && ((MatrixTransformProvider) this.matcher12.getModel()).getTransform().cond() < 1e6 ) {
                try {
                    final Matrix boundsToPoly = ((MatrixTransformProvider) this.matcher12.getModel()).getTransform()
                            .inverse();

                    if (modelImage12.getBounds().transform(boundsToPoly).isConvex()) {

                        renderer.drawShape(this.modelImage12.getBounds().transform(boundsToPoly), 3, RGBColour.RED);

                        if(count12 <= 20){
                            List<Point2d> vertices = this.modelImage12.getBounds().transform(boundsToPoly).asPolygon().getVertices();
                            int x[] = new int[4], y[] = new int[4];
                            for (int i = 0; i < vertices.size(); i++) {
                                x[i] = (int) vertices.get(i).getX();
                                y[i] = (int) vertices.get(i).getY();
                            }
                            Polygon polygon = new Polygon(x, y, 4);
                            for (int i = 0; i < kpl.size(); i++) {
                                if (polygon.contains(kpl.get(i).getX(), kpl.get(i).getY())) {
                                    double c[] = kpl.get(i).getFeatureVector().asDoubleVector();
                                    bw.write("11,");
                                    for (int j = 0; j < c.length; j++) {
                                        bw.write(c[j] + " ");
                                    }
                                    bw.newLine();
                                }
                            }

                            count12++;
                        }

                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }
            if (this.matcher13.findMatches(kpl)
                    && ((MatrixTransformProvider) this.matcher13.getModel()).getTransform().cond() < 1e6 ) {
                try {
                    final Matrix boundsToPoly = ((MatrixTransformProvider) this.matcher13.getModel()).getTransform()
                            .inverse();

                    if (modelImage13.getBounds().transform(boundsToPoly).isConvex()) {

                        renderer.drawShape(this.modelImage13.getBounds().transform(boundsToPoly), 3, RGBColour.RED);

                        if(count13 <= 20){
                            List<Point2d> vertices = this.modelImage13.getBounds().transform(boundsToPoly).asPolygon().getVertices();
                            int x[] = new int[4], y[] = new int[4];
                            for (int i = 0; i < vertices.size(); i++) {
                                x[i] = (int) vertices.get(i).getX();
                                y[i] = (int) vertices.get(i).getY();
                            }
                            Polygon polygon = new Polygon(x, y, 4);
                            for (int i = 0; i < kpl.size(); i++) {
                                if (polygon.contains(kpl.get(i).getX(), kpl.get(i).getY())) {
                                    double c[] = kpl.get(i).getFeatureVector().asDoubleVector();
                                    bw.write("12,");
                                    for (int j = 0; j < c.length; j++) {
                                        bw.write(c[j] + " ");
                                    }
                                    bw.newLine();
                                }
                            }

                            count13++;
                        }

                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }

            DisplayUtilities.displayName(mbfImage, "Image");
        }
        bw.close();
    }

    public void LoadReferenceObject() {

        final DoGSIFTEngine engine = new DoGSIFTEngine();
        engine.getOptions().setDoubleInitialImage(true);

        try {
            modelImage1 = ImageUtilities.readMBF(new File("data2/dino1.jpg"));
            modelImage2 = ImageUtilities.readMBF(new File("data2/dino3.jpg"));
            modelImage3 = ImageUtilities.readMBF(new File("data2/dino4.jpg"));
            modelImage4 = ImageUtilities.readMBF(new File("data2/dino5.jpg"));

            modelImage5 = ImageUtilities.readMBF(new File("data2/coworker1.jpg"));
            modelImage6 = ImageUtilities.readMBF(new File("data2/coworker3.jpg"));
            modelImage7 = ImageUtilities.readMBF(new File("data2/coworker4.jpg"));
            modelImage8 = ImageUtilities.readMBF(new File("data2/coworker5.jpg"));

            modelImage9  = ImageUtilities.readMBF(new File("data2/pratt1.jpg"));
            modelImage10 = ImageUtilities.readMBF(new File("data2/pratt2.jpg"));
            modelImage11 = ImageUtilities.readMBF(new File("data2/pratt4.jpg"));
            modelImage12 = ImageUtilities.readMBF(new File("data2/pratt5.jpg"));
            modelImage13 = ImageUtilities.readMBF(new File("data2/pratt6.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        FImage modelF1 = Transforms.calculateIntensityNTSC(modelImage1);
        this.matcher1.setModelFeatures(engine.findFeatures(modelF1));

        FImage modelF2 = Transforms.calculateIntensityNTSC(modelImage2);
        this.matcher2.setModelFeatures(engine.findFeatures(modelF2));

        FImage modelF3 = Transforms.calculateIntensityNTSC(modelImage3);
        this.matcher3.setModelFeatures(engine.findFeatures(modelF3));

        FImage modelF4 = Transforms.calculateIntensityNTSC(modelImage4);
        this.matcher4.setModelFeatures(engine.findFeatures(modelF4));

        FImage modelF5 = Transforms.calculateIntensityNTSC(modelImage5);
        this.matcher5.setModelFeatures(engine.findFeatures(modelF5));

        FImage modelF6 = Transforms.calculateIntensityNTSC(modelImage6);
        this.matcher6.setModelFeatures(engine.findFeatures(modelF6));

        FImage modelF7 = Transforms.calculateIntensityNTSC(modelImage7);
        this.matcher7.setModelFeatures(engine.findFeatures(modelF7));

        FImage modelF8 = Transforms.calculateIntensityNTSC(modelImage8);
        this.matcher8.setModelFeatures(engine.findFeatures(modelF8));

        FImage modelF9 = Transforms.calculateIntensityNTSC(modelImage9);
        this.matcher9.setModelFeatures(engine.findFeatures(modelF9));

        FImage modelF10 = Transforms.calculateIntensityNTSC(modelImage10);
        this.matcher10.setModelFeatures(engine.findFeatures(modelF10));

        FImage modelF11 = Transforms.calculateIntensityNTSC(modelImage11);
        this.matcher11.setModelFeatures(engine.findFeatures(modelF11));

        FImage modelF12 = Transforms.calculateIntensityNTSC(modelImage12);
        this.matcher12.setModelFeatures(engine.findFeatures(modelF12));

        FImage modelF13 = Transforms.calculateIntensityNTSC(modelImage13);
        this.matcher13.setModelFeatures(engine.findFeatures(modelF13));
    }
}
