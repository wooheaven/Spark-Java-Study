package rwoo.study.spark.javardd;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TakeOrderedTest {
    private JavaSparkContext sc;
    private JavaRDD<Integer> rddA;
    private List<Integer> result;

    @Before
    public void setUp() {
        sc = new JavaSparkContext("local[*]", "JavaRDD.takeOrdered");
        rddA = sc.parallelize(Arrays.asList(1, 2, 3, 2, 3, 10));
    }

    @After
    public void after() {
        assertEquals(Arrays.asList(1, 2, 3, 2, 3, 10), rddA.collect());
        sc.close();
    }

    @Test
    public void testTakeOrdered() {
        result = rddA.takeOrdered(3);
        assertEquals(Arrays.asList(1, 2, 2), result);
    }

    @Test
    public void testTakeOrdered_by_CustomComparator() {
        result = rddA.takeOrdered(3, new CustomComparatorToString());
        assertEquals("[1, 10, 2]", result.toString());
    }

    private static class CustomComparatorToString implements Comparator<Integer>, Serializable {
        @Override
        public int compare(Integer a, Integer b) {
            return a.toString().compareTo(b.toString());
        }
    }
}
