package cn.wzs.java_base.io;


import java.io.*;

public class InputStreamTest implements Serializable {

    static String filePath = "D:\\work\\code\\learn_space\\java_base\\src\\main\\resources\\FileInput.txt";

    public static void main(String[] args) throws Exception {
        testFileInputStream();
        testBufferedInputStream();
        testDataInputStream();
        testObjectInputStream();
    }

    /**
     * 测试文件输入流
     *
     * @throws Exception
     */
    static void testFileInputStream() throws Exception {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(new File(filePath));
            System.out.println("available:" + inputStream.available());
            System.out.println("skip:" + inputStream.skip(2L));
            int d;
            while ((d = inputStream.read()) != -1) {
                System.out.print((char) d);
            }
            System.out.println();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }


    /**
     * BufferedInputStream缓冲的文件输入流
     * 普通的FileInputStream读取字节时是单个单个读取，速度慢，而BufferedFileInputStream内置缓冲的字节数组，一次读取多个数据再慢慢返回使用
     * 提高程序性能
     *
     * @throws Exception
     */
    static void testBufferedInputStream() throws Exception {
        BufferedInputStream bufferedInputStream = null;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(new File(filePath));
            bufferedInputStream = new BufferedInputStream(fileInputStream);
            bufferedInputStream.skip(2L);
            int d;
            while ((d = bufferedInputStream.read()) != -1) {
                System.out.print((char) d);
            }
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufferedInputStream != null) {
                // 包装流在close时会将包装的流同样关闭
                bufferedInputStream.close();
            }
        }
    }


    /**
     * 继承了FileInputStream, 扩展了java的八种基本类型数据读取
     *
     * @throws Exception
     */
    static void testDataInputStream() throws Exception {
        DataInputStream dataInputStream = null;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(new File(filePath));
            dataInputStream = new DataInputStream(fileInputStream);
            System.out.println(dataInputStream.readBoolean());
            int d;
            while ((d = dataInputStream.read()) != -1) {
                System.out.print((char) d);
            }
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dataInputStream != null) {
                // 包装流在close时会将包装的流同样关闭
                dataInputStream.close();
            }
        }
    }

    /**
     * 用于读取java序列化后的文件并转换成object
     *
     * @throws Exception
     */
    static void testObjectInputStream() throws Exception {
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(new FileInputStream(OutputStreamTest.ObjectFilePath));
            InputStreamTest obj = (InputStreamTest) objectInputStream.readObject();
            System.out.println(obj);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (objectInputStream != null) {
                // 包装流在close时会将包装的流同样关闭
                objectInputStream.close();
            }
        }
    }
}
