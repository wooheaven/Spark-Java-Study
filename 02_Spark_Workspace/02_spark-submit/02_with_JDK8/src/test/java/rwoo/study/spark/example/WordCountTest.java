package rwoo.study.spark.example;

import org.junit.Before;
import org.junit.Test;
import rwoo.study.spark.util.FileUtil;

import java.io.File;

public class WordCountTest extends FileUtil {
    private WordCount wordCount;
    private String[] args;

    @Before
    public void setUp() throws Exception {
        wordCount = new WordCount();
        args = new String[3];
        args[0] = "src/test/resources/input/sample.txt";
        args[1] = "src/test/resources/output/WordCount";
        args[2] = "local[*]";
        this.deleteDirectory(new File(args[1]));
    }

    @Test
    public void testWordCountResultFile() throws Exception {
        wordCount.main(args);
    }
}