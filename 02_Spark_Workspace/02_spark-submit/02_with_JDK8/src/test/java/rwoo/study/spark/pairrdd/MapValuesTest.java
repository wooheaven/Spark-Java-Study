package rwoo.study.spark.pairrdd;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import rwoo.study.spark.function.CustomFunction;
import scala.Tuple2;

import java.io.Serializable;

public class MapValuesTest implements Serializable {
    private JavaSparkContext sc;
    private JavaPairRDD<String, Iterable<String>> links;

    @Before
    public void setUp() {
        sc = new JavaSparkContext(new SparkConf()
                .setMaster("local[*]")
                .setAppName("PartitionByTest")
                .set("spark.driver.allowMultipleContexts", "true"));
        JavaRDD<String> lines = sc.textFile("src/test/resources/input/PartitionByTest/");
        links = lines.mapToPair(line -> {
            String[] parts = line.split(" ");
            return new Tuple2<String, String>(parts[0], parts[1]);
        }).groupByKey().cache();
        links.foreach(link -> System.out.println(link));
//        (B,[A])
//        (A,[C, D])
//        (D,[B, C])
//        (C,[A])
    }

    @After
    public void after() {
        sc.close();
    }

    @Test
    public void test_MapValues_with_Implicit_Function() {
        JavaPairRDD<String, Tuple2<String, Double>> ranks = links.mapValues(v ->
            new Tuple2<>(v.toString(), 1.0)
        );
        ranks.foreach(rank -> System.out.println(rank));
//        (B,([A],1.0))
//        (A,([C, D],1.0))
//        (D,([B, C],1.0))
//        (C,([A],1.0))
    }

    @Test
    public void test_MapValues_with_Explicit_Function() {
        JavaPairRDD<String, Tuple2<String, Double>> ranks = links.mapValues(new CustomFunction());
        ranks.foreach(rank -> System.out.println(rank));
    }
}
