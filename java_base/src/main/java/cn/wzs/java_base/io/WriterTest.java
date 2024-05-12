package cn.wzs.java_base.io;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

public class WriterTest {

    static String filePath = "D:\\work\\code\\learn_space\\java_base\\src\\main\\resources\\FileWriter.txt";

    public static void main(String[] args) throws Exception {
        testFileWriter();
    }

    static void testFileWriter() throws Exception {
        Writer writer = null;
        try {
            File targetFile = new File(filePath);
            if (targetFile.exists()) {
                targetFile.delete();
                targetFile.createNewFile();
            } else {
                targetFile.createNewFile();
            }
            writer = new FileWriter(targetFile);
            String text = "good good study day day up testFileWriter() 你好";
            writer.write(text, 0, text.length());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }
    }
}
