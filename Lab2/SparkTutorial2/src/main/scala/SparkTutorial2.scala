import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Adam Carter for CS 5542 Lab Tutorial 2 - 01/27/17
  *
  * This program is an extension of WordCount.  We read in a file, the contents of the main
  * Wheel of Time wiki page.  We execute the basic WordCount logic, though I've added a UDF
  * in the middle to 'sanitize' the output (casts all words to lower case, strip punctuation,
  * etc).  I then sort the results by frequency of usage, then cut to take the top 25 terms
  * found.
  */
object SparkTutorial2 {

  def main(args: Array[String]): Unit = {
    //Spark Config
    System.setProperty("hadoop.home.dir","C:\\Users\\ac010168\\workspaces\\umkc\\winutils");
    val sparkConf = new SparkConf().setAppName("SparkWordCount").setMaster("local[*]")
    val sc = new SparkContext(sparkConf)

    //The contents of the Wheel of Time wikipedia entry, slightly modified
    val input = sc.textFile("wotwiki.txt")

    //Split the line into single words
    val flatRDD = input.flatMap(line=>{line.split(" ")})

    //Map each word to it's sanitized output and value of 1 for wordcount
    val wordRDD = flatRDD.map(word=>(sanitizeWord(word),1)).cache()

    //Use the shorthand syntax for reduceByKey
    val reduceRDD = wordRDD.reduceByKey(_+_)

    //Get a Quick Count of the number of words found
    val totalWordsFound = reduceRDD.count()
    println ("There were " + totalWordsFound + " total unique words found")

    //Sort the list by value
    val sortedRDD = reduceRDD.sortBy(tuple=>tuple._2, false)

    //Write first results to file
    sortedRDD.saveAsTextFile("wotoutput")

    //Grab the Top 25 results
    val topX = sortedRDD.take(25)

    println ("There were " + totalWordsFound + " total unique words found")
    println ("The Top 25 Search Terms in the Wheel of Time wiki page")
    topX.foreach{case(word,count)=>{
      println (word + "  :  " + count)
    }}
  }

  /**
    * Helper method to sanitize my string values
    * @param word The word we want to clean up
    * @return The sanizited word value
    */
  def sanitizeWord(word:String): String = {
    var newWord:String = word.toLowerCase
    newWord = newWord.replaceAll("[^A-Za-z0-9\\.\'-]", "");
    return newWord
  }
}