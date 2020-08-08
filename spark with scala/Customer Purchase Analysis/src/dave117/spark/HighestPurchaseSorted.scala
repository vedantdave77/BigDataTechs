package dave117.spark

import org.apache.spark._
import org.apache.spark.SparkContext._
import org.apache.log4j._
import scala.math

object HighestPurchaseSorted {
  def customerPurchasePrice(line:String) = {
    val fields = line.split(",")
    val customerID = fields(0).toInt
    val amount =fields(2).toFloat
    (customerID,amount)
  }
  
  def main(args: Array[String]) {
    Logger.getLogger("org").setLevel(Level.ERROR)                                    // Logger 
    val sc = new SparkContext("local[*]","Total Purchase by Customer")               // parallelize with local 4 core 
    val input = sc.textFile("../customer-orders.csv")                                // call data 
    val mappedInput = input.map(customerPurchasePrice)                               // mapping job (pre executed)
    val totalByCustomer = mappedInput.reduceByKey( (x,y) => (x+y))                   // reduce job (by key adding)
    val flipped = totalByCustomer.map(x => (x._2,x._1))
    val totalCustomerSorted = flipped.sortByKey()
    val results = totalCustomerSorted.collect()
    results.foreach(println)
  }
}