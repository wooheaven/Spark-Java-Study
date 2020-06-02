package rwoo.study.spark.javarddlike;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MapPartitionsTest {
    private JavaSparkContext sc;
    private JavaRDD<Integer> rddA;
    private JavaRDD<Integer> rddB;

    @Before
    public void setUp() {
        sc = new JavaSparkContext("local[*]", "JavaRDDLike.mapPartitions");
        rddA = sc.parallelize(Arrays.asList(1, 2, 3, 4, 5, 6, 7), 3);
    }

    @After
    public void after() {
    /*
    [PARTITIONS] of RDD -> [SUM of PARTITIONS] of RDD
    [1, 2],             -> [3]
    [3, 4],             -> [7]
    [5, 6, 7]           -> [18]
     */
        assertEquals("[[1, 2], [3, 4], [5, 6, 7]]", rddA.glom().collect().toString());
        assertEquals("[[3], [7], [18]]", rddB.glom().collect().toString());
        sc.close();
    }

    @Test
    public void testMapPartitions_with_Implicit() {
        rddB = rddA.mapPartitions(partition -> {
            List<Integer> partitionSum = new ArrayList<>();
            int sum = 0;
            while (partition.hasNext()) {
                sum += partition.next();
            }
            partitionSum.add(sum);
            return partitionSum.iterator();
        });
    }

    @Test
    public void testMapPartitions_with_Explicit() {
        FlatMapFunction<Iterator<Integer>, Integer> f = partition -> {
            List<Integer> partitionSum = new ArrayList<>();
            int sum = 0;
            while (partition.hasNext()) {
                sum += partition.next();
            }
            partitionSum.add(sum);
            return partitionSum.iterator();
        };
        rddB = rddA.mapPartitions(f);
    }

    @Test
    public void testMapPartitions_with_CustomFlatMapFunction() {
        rddB = rddA.mapPartitions(new CustomFlatMapFunction());
    }

    private static class CustomFlatMapFunction implements FlatMapFunction<Iterator<Integer>, Integer> {
        @Override
        public Iterator<Integer> call(Iterator<Integer> partition) throws Exception {
            List<Integer> partitionSum = new ArrayList<>();
            int sum = 0;
            while (partition.hasNext()) {
                sum += partition.next();
            }
            partitionSum.add(sum);
            return partitionSum.iterator();
        }
    }
}
