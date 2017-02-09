import org.apache.log4j.{Level, Logger}
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.{LabeledPoint, LinearRegressionWithSGD}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by AC010168 on 2/4/2017.
  */
object ChimpLinearRegression {

  def main(args: Array[String]): Unit = {
    // I get it now.  These generate way too much logging
    Logger.getLogger("org").setLevel(Level.OFF);
    Logger.getLogger("akka").setLevel(Level.OFF);

    System.setProperty("hadoop.home.dir","C:\\Users\\ac010168\\workspaces\\umkc\\winutils");
    val sparkConf = new SparkConf().setAppName("ChimpLinearRegression").setMaster("local[*]")
    val sc = new SparkContext(sparkConf)

    //Our data set consists of tuples in the form of (daily_high_temp, num_interactions)
    val linearData = sc.textFile("data/linearDataSet.txt");
    val parsedData = linearData.map { line =>
      val parts = line.split(',')
      LabeledPoint(parts(0).toDouble, Vectors.dense(parts(1).split(' ').map(_.toDouble)))
    }.cache()

    val Array(train, test) = parsedData.randomSplit(Array(0.95, 0.05))

    //Build the model and define the settings
    val numIterations = 100
    val stepSize = 0.00000001
    val chimpModel = LinearRegressionWithSGD.train(train, numIterations, stepSize)

    // Evaluate model on training examples and compute training error
    val valuesAndPreds = train.map { point =>
      val prediction = chimpModel.predict(point.features)
      (point.label, prediction)
    }
    val MSE = valuesAndPreds.map{ case(v, p) => math.pow((v - p), 2) }.mean()
    println("training Mean Squared Error = " + MSE)

    val valuesAndPreds2 = test.map { point =>
      val prediction = chimpModel.predict(point.features)
      (point.label, prediction)
    }
    val MSE2 = valuesAndPreds2.map{ case(v, p) => math.pow((v - p), 2) }.mean()
    println("test Mean Squared Error = " + MSE2)

    // Save and load model
    chimpModel.save(sc, "data\\ChimpLinearRegressionModel")
  }
}
