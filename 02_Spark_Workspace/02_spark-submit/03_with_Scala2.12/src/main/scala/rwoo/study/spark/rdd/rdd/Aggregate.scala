package rwoo.study.spark.rdd.rdd

import org.apache.spark.SparkContext

object AggregateTest {

  def main(args: Array[String]): Unit = {
    val list = List(1, 2, 3, 4, 5, 6, 7, 8, 9)
    var sum = 0
    var product = 1
    for (v <- list) {
      sum = sum + v
      product = product * v
      println(v, sum, product)
    }

    val sc = new SparkContext("local[*]", "RDD.aggregate")
    val a = sc.parallelize(Array(1, 2, 3, 4, 5, 6, 7, 8, 9), 3)

    def initialValue = (0, 1)

    def seqOp = (data: (Int, Int), item: Int)
    => (data._1 + item, data._2 * item)

    def combOp = (d1: (Int, Int), d2: (Int, Int))
    => (d1._1 + d2._1, d1._2 * d2._2)

    val b = a.aggregate(initialValue)(seqOp, combOp)

    println(customToString(a.glom().collect()))
    println(customToString2(b))

    def customToString(a: Array[Array[Int]]): String = {
      val str = for (l <- a) yield l.mkString("[", ",", "]")
      str.mkString("{\n\t", ",\n\t", "\n}")
    }

    def customToString2(a: (Int, Int)): String = {
      val str = a._1.toString() + " : " + a._2.toString()
      str
    }
  }
}
