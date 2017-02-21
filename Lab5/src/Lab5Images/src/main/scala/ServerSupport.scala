import org.apache.log4j.{Level, Logger}
import org.apache.spark.mllib.clustering.KMeansModel
import org.apache.spark.mllib.tree.model.RandomForestModel
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by AC010168 on 2/20/2017.
  */
object ServerSupport {
  val KMEANS_PATH         = "data/model/clusters"
  val RANDOM_FOREST_PATH  = "data/model/nbmodel"

  val IMAGE_CATEGORIES    = List("bookcase", "dc", "firefly", "lotr", "shelf", "starwars", "table", "weapon", "who")

  def testImage(imageName: String):String = {
    // I get it now.  These generate way too much logging
    Logger.getLogger("org").setLevel(Level.OFF);
    Logger.getLogger("akka").setLevel(Level.OFF);

    System.setProperty("hadoop.home.dir","C:\\Users\\ac010168\\workspaces\\umkc\\winutils");

    val conf = new SparkConf()
      .setAppName(s"IPApp")
      .setMaster("local[*]")
      .set("spark.executor.memory", "6g")
      .set("spark.driver.memory", "6g")
    val sparkConf = new SparkConf().setAppName("Lab5Server").setMaster("local[*]")
    val sc = SparkContext.getOrCreate(sparkConf);

    val result = testImageClassification(sc, imageName);
    println(result);
    result
  }

  def testImageClassification(sc: SparkContext, path: String): String = {
    val cModel  = KMeansModel.load(sc, KMEANS_PATH);
    val vocab   = ImageUtils.vectorsToMat(cModel.clusterCenters);
    val desc    = ImageUtils.bowDescriptors(path, vocab);
    val hist    = ImageUtils.matToVector(desc);

    println ("-- Histogram Size : " + hist.size);
    println (hist.toArray.mkString(" "));

    val rfModel = RandomForestModel.load(sc, RANDOM_FOREST_PATH);
    val predict = rfModel.predict(hist);

    (s"Test image predicted as : " + IMAGE_CATEGORIES(predict.toInt))
  }

}
