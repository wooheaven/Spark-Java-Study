package rwoo.study.spark.rdd.pairrddfunctions

import org.apache.spark.{HashPartitioner, Partitioner, SparkContext}

object PartitionByTest {

  def main(args: Array[String]): Unit = {
    val sc = new SparkContext("local[*]", "PairRDDFunctions.partitionBy")
    val rddA = sc.parallelize(Array("James", "Fred", "Anna", "John")).keyBy(x => x.charAt(0)).sortByKey()
    val rddB = rddA.partitionBy(new HashPartitioner(2))
    val rddC = rddA.partitionBy(new Partitioner() {
      override def numPartitions: Int = 2

      override def getPartition(key: Any): Int = {
        if (key.asInstanceOf[Char] < 'H') 0 else 1
      }
    })

    println(customToString(rddA.glom().collect()))
    println(customToString(rddB.glom().collect()))
    println(customToString(rddC.glom().collect()))

    def customToString(a: Array[Array[(Char, String)]]): String = {
      val str = for (l <- a) yield l.mkString("{", ",", "}")
      str.mkString("{\n\t", ",\n\t", "\n}")
    }
  }
}
