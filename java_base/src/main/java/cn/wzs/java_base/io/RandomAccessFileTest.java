package cn.wzs.java_base.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.Random;

public class RandomAccessFileTest {

    static String filePath = "C:\\Users\\清流浅溪\\Desktop\\data\\redis.pdf";

    static String filePathDemo = "D:\\work\\code\\learn_space\\java_base\\src\\main\\resources\\randomAccess.txt";

    public static void main(String[] args) throws Exception {
//        testRandomAccessFile();
        testBreakpointContinuation("C:\\Users\\清流浅溪\\Desktop\\data", "redis.pdf");
    }

    /**
     * RandomAccessFile可以访问文件的任意位置
     * mode:r、rw、rws、rwd
     */
    static void testRandomAccessFile() throws Exception {
        RandomAccessFile randomAccessFile = new RandomAccessFile(new File(filePathDemo), "rw");
        randomAccessFile.seek(2L);
        System.out.println(randomAccessFile.getFilePointer());
        byte[] buffer = new byte[5];
        int len = randomAccessFile.read(buffer);
        System.out.println(new String(buffer, 0, len));
//        randomAccessFile.write(new byte[]{'H', 'E', 'L', 'L', 'O'});
        // seek用于指定文件读取的位置
        randomAccessFile.seek(0L);
        len = randomAccessFile.read(buffer);
        System.out.println(new String(buffer, 0, len));
        randomAccessFile.close();
    }

    /**
     * 通过RandomAccessFile实现断点续传
     * 1、日志文件记录当前写入的文件位置
     * 2、重新上传时，先检查日志文件是否存在，不存在则是重新上传，存在则是续传
     * 在创建RandomAccessFile对象时，会自动创建不存在的文件
     */
    static void testBreakpointContinuation(String dir, String fileName) throws Exception {
        RandomAccessFile log = new RandomAccessFile(new File(dir + File.separator + "process.log"), "rw");
        long startIndex;
        try {
            startIndex = log.readLong();
        } catch (Exception e) {
            startIndex = 0L;
        }
        System.out.println(startIndex);

        File sourceFile = new File(dir + File.separator + fileName);
        if (!sourceFile.exists()) {
            throw new FileNotFoundException(dir + "下" + fileName);
        }
        RandomAccessFile sourceFileAccess = new RandomAccessFile(sourceFile, "rw");

        RandomAccessFile copyFileAccess = new RandomAccessFile(new File(dir + File.separator + "copy_" + fileName), "rw");
        // 初始化开始位置
        copyFileAccess.seek(startIndex);
        sourceFileAccess.seek(startIndex);
        // 读取写入文件
        byte[] buffer = new byte[1024];

        int size = 10;
        int len;
        while ((len = sourceFileAccess.read(buffer)) != -1) {
            copyFileAccess.write(buffer, 0, len);
            startIndex = startIndex + len;
            log.seek(0);
            log.writeLong(startIndex);
            size--;
//            if (size == 0) {
//                System.out.println("模拟中断");
//                break;
//            }
        }
        log.close();
        sourceFileAccess.close();
        copyFileAccess.close();
    }
}
