package rwoo.study.spark.rdd;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class MapTest {
    private JavaSparkContext sc;
    private List<String> inputList;
    private JavaRDD<String> inputRDD;
    private JavaRDD<String> outputRDD;

    @Before
    public void setUp() {
        sc = new JavaSparkContext(new SparkConf()
                .setMaster("local[*]")
                .setAppName("MapTest"));
        inputList = new ArrayList<>();
        inputList.add("I am a boy");
        inputList.add("You are a girl");
        inputRDD = sc.parallelize(inputList);
        inputRDD.foreach(v -> System.out.println(v));
//        You are a girl
//        I am a boy
    }

    @After
    public void after() {
        outputRDD.foreach(v -> System.out.println(v));
//        you are a girl
//        i am a boy
        sc.close();
    }

    @Test
    public void testMap() {
        outputRDD = inputRDD.map(e -> e.toLowerCase());
    }
}
