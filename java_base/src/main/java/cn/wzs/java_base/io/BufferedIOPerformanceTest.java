package cn.wzs.java_base.io;


import java.io.*;

public class BufferedIOPerformanceTest {

    public BufferedIOPerformanceTest(String filePath, String targetFileDir, String fileSuffix) {
        this.fileSuffix = fileSuffix;
        this.filePath = filePath;
        this.targetFileDir = targetFileDir;
    }

    private final String fileSuffix;

    private final String filePath;

    private final String targetFileDir;

    /**
     * buffered比普通的io快三倍以上
     *
     * @param type
     * @throws Exception
     */
    private void copyFile(int type) throws Exception {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            String targetFileName = targetFileDir + File.separator + type + fileSuffix;
            File targetFile = new File(targetFileName);
            if (targetFile.exists()) {
                targetFile.delete();
            }
            targetFile.createNewFile();
            if (type == 0) {
                // 正常的io流
                inputStream = new FileInputStream(filePath);
                outputStream = new FileOutputStream(targetFile);
            } else {
                // buffer缓存区io流
                inputStream = new BufferedInputStream(new FileInputStream(filePath));
                outputStream = new BufferedOutputStream(new FileOutputStream(targetFile));
            }

            //





            long start = System.currentTimeMillis();

            // 自己组建buffer数组copy文件，二者性能差距只有三四背左右
            // type:0 cost5624ms
            // type:1 cost1737ms
//            byte[] buffer = new byte[1024];
//            int count;
//            while ((count = inputStream.read(buffer)) > 0) {
//                outputStream.write(buffer, 0, count);
//            }


            int data;
            while ((data = inputStream.read()) != -1) {
                outputStream.write(data);
            }
            long end = System.currentTimeMillis();
            System.out.println("type:" + type + " cost" + (end - start) + "ms");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        String filePath = "C:\\Users\\清流浅溪\\Desktop\\data\\from\\java360天精通.mp4";
        String targetFileDir = "C:\\Users\\清流浅溪\\Desktop\\data\\to\\";
        BufferedIOPerformanceTest test = new BufferedIOPerformanceTest(filePath, targetFileDir, ".mp4");
        test.copyFile(0);
        test.copyFile(1);
    }
}
