package cn.wzs.java_base.nio.talkroom.helper;

import cn.wzs.java_base.nio.talkroom.base.FileMessage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class FileHelper {

    private final Map<String, List<byte[]>> uploadCache = new ConcurrentHashMap<>();

    public static final String TEMP = "C:\\Users\\清流浅溪\\Desktop\\files\\server\\";

    public void receiveFile(FileMessage message) {
        receiveFile(message, TEMP);
    }

    public void receiveFile(FileMessage message, String tempDir) {
        try {
            String fileName = message.getFileName();
            if (message.getDone()) {
                // 已传输完成
                List<byte[]> bytes = uploadCache.get(fileName);
                File file = new File(tempDir + fileName);
                FileOutputStream outputStream = new FileOutputStream(file);
                for (byte[] b : bytes) {
                    outputStream.write(b);
                }
                outputStream.write(message.getData());
                outputStream.flush();
                outputStream.close();
                uploadCache.remove(fileName);
            } else {
                List<byte[]> bytes = uploadCache.computeIfAbsent(fileName, k -> new ArrayList<>());
                bytes.add(message.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void uploadFile(String code, SocketChannel channel, String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }
        try {
            long total = file.length();
            String[] paths = filePath.split("\\\\");
            // 保证tcp窗口一次性发送完,单次发送512kb
            int capacity = 20 * 1024;
            byte[] buf = new byte[capacity];
            FileInputStream inputStream = new FileInputStream(file);
            int read;
            while ((read = inputStream.read(buf)) != -1) {
                FileMessage message = new FileMessage();
                message.setFrom(code);
                message.setFileName(paths[paths.length - 1]);
                message.setTotal(total);
                byte[] dest;
                dest = new byte[read];
                System.arraycopy(buf, 0, dest, 0, read);
                message.setData(dest);
                total -= read;
                if (total == 0L) {
                    message.setDone(true);
                }
                MessageHelper.send(message, channel);
            }
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static List<String> getFiles() {
        File file = new File(TEMP);
        if (file.isDirectory()) {
            String[] list = file.list();
            if (list != null) {
                return Arrays.asList(list);
            }
        }
        return Collections.emptyList();
    }


    public static void deleteFile(String fileName) {
        File file = new File(TEMP + fileName);
        if (file.exists()) {
            file.delete();
        }
    }
}
