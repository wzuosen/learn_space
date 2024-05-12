package cn.wzs.java_base.io;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class OutputStreamTest {

    public static String ObjectFilePath = "D:\\work\\code\\learn_space\\java_base\\src\\main\\resources\\object.data";

    static String writeFilePath = "D:\\work\\code\\learn_space\\java_base\\src\\main\\resources\\output.txt";

    public static void main(String[] args) throws Exception {
//        testFileOutStream();
        testDataOutputStream();
//        testBufferedOutputStream();
//        testObjectOutputStream();
    }

    /**
     * 文件输出流
     *
     * @throws Exception
     */
    static void testFileOutStream() throws Exception {
        OutputStream outputStream = null;
        try {
            File targetFile = new File(writeFilePath);
            if (!targetFile.exists()) {
                targetFile.createNewFile();
            }
            outputStream = new FileOutputStream(targetFile);
            String text = "good good study day day up";
            byte[] bytes = text.getBytes();
            outputStream.write(bytes, 0, bytes.length);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }

    /**
     * 支持java基本类型的输出流
     *
     * @throws Exception
     */
    static void testDataOutputStream() throws Exception {
        DataOutputStream outputStream = null;
        try {
            File targetFile = new File(writeFilePath);
            if (!targetFile.exists()) {
                targetFile.createNewFile();
            } else {
                targetFile.delete();
                targetFile.createNewFile();
            }
            outputStream = new DataOutputStream(new FileOutputStream(targetFile));
            String text = "good good study day day up testDataOutputStream()";
            byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
            outputStream.write(bytes, 0, bytes.length);
            // 额外写个true
            outputStream.writeChars("11");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }

    /**
     * 缓冲输出流
     *
     * @throws Exception
     */
    static void testBufferedOutputStream() throws Exception {
        BufferedOutputStream bufferedOutputStream = null;
        OutputStream outputStream = null;
        try {
            File targetFile = new File(writeFilePath);
            if (!targetFile.exists()) {
                targetFile.createNewFile();
            } else {
                targetFile.delete();
                targetFile.createNewFile();
            }
            outputStream = new FileOutputStream(targetFile);
            bufferedOutputStream = new BufferedOutputStream(outputStream);
            String text = "good good study day day up (testBufferedOutputStream)";
            byte[] bytes = text.getBytes();
            bufferedOutputStream.write(bytes, 0, bytes.length);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufferedOutputStream != null) {
                bufferedOutputStream.close();
            }
        }
    }


    /**
     * 对象输出流
     *
     * @throws Exception
     */
    static void testObjectOutputStream() throws Exception {
        ObjectOutputStream outputStream = null;
        try {
            outputStream = new ObjectOutputStream(new FileOutputStream(ObjectFilePath));
            InputStreamTest inputStreamTest = new InputStreamTest();
            outputStream.writeObject(inputStreamTest);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }

    }
}
