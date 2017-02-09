import org.apache.log4j.{Level, Logger}
import org.apache.spark.mllib.clustering.KMeans
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by AC010168 on 2/4/2017.
  */
object ChimpKMeans {

  def main(args: Array[String]): Unit = {
    // I get it now.  These generate way too much logging
    Logger.getLogger("org").setLevel(Level.OFF);
    Logger.getLogger("akka").setLevel(Level.OFF);

    System.setProperty("hadoop.home.dir","C:\\Users\\ac010168\\workspaces\\umkc\\winutils");
    val sparkConf = new SparkConf().setAppName("ChimpLinearRegression").setMaster("local[*]")
    val sc = new SparkContext(sparkConf)

    val kMeansData = sc.textFile("data/kmeanDataSet.txt");
    val parsedData = kMeansData.map(s => Vectors.dense(s.split(',').map(_.toDouble))).cache()

    //Let's assume 4 major social groups, so we'll try to generate 4 clusters
    // Cluster the data into two classes using KMeans
    val numClusters = 6
    val numIterations = 50
    val clusters = KMeans.train(parsedData, numClusters, numIterations)

    // Evaluate clustering by computing Within Set Sum of Squared Errors
    val WSSSE = clusters.computeCost(parsedData)
    println("Within Set Sum of Squared Errors = " + WSSSE)

    //Look at how the clusters are in training data by making predictions
    println("Clustering on training data: ")
    clusters.predict(parsedData).zip(parsedData).foreach(f=>println(f._2,f._1))

    // Save and load model
    clusters.save(sc, "data/KMeansModel")
  }
}
