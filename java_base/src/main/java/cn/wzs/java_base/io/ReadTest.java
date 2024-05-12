package cn.wzs.java_base.io;

import java.io.File;
import java.io.FileReader;

public class ReadTest {

    static String filePath = "D:\\work\\code\\learn_space\\java_base\\src\\main\\resources\\FileInput.txt";

    public static void main(String[] args) throws Exception {
        testFileRead();
    }

    /**
     * 读取文件的字符流
     *
     * @throws Exception
     */
    static void testFileRead() throws Exception {
        FileReader reader = null;
        try {
            reader = new FileReader(filePath);
            char[] buffer = new char[1024];
            int len = reader.read(buffer);
            System.out.println(new String(buffer, 0, len));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }
}
