package cn.wzs.java_base.nio.talkroom.demo;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 客户端方法
 */
public class ChatClient {

    /**
     * 启动客户端方法
     * @param name 客户名称
     * @throws IOException 异常
     */
    public void startClient(String name) throws IOException{
        // 连接服务端
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1",8000));
        // 接收服务端相应数据
        Selector selector = Selector.open();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        // 创建线程
        new Thread(new ClientThread(selector)).start();
        // 向服务端发送消息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String msg = scanner.nextLine();
            if (msg.length() >0){
                socketChannel.write(UTF_8.encode(name+":"+msg));
            }
        }
    }

    public static void main(String[] args) {
        try{
            new ChatClient().startClient("111");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}


