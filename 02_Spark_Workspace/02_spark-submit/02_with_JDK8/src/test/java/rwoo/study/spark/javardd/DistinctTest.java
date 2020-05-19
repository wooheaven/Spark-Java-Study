package rwoo.study.spark.javardd;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class DistinctTest {
    private JavaSparkContext sc;
    private JavaRDD<Integer> rddA;
    private JavaRDD<Integer> rddB;

    @Before
    public void setUp() {
        sc = new JavaSparkContext("local[*]", "JavaRDD.distinct");
        rddA = sc.parallelize(Arrays.asList(1, 2, 3, 2));
    }

    @After
    public void after() {
        assertEquals(Arrays.asList(1, 2, 3, 2), rddA.collect());
        assertEquals(Arrays.asList(1, 2, 3), rddB.collect());
        sc.close();
    }

    @Test
    public void testDistinct() {
        rddB = rddA.distinct().sortBy(v -> v, true, rddA.getNumPartitions());
    }
}
