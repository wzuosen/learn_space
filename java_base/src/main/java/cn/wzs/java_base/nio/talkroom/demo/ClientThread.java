package cn.wzs.java_base.nio.talkroom.demo;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 客户端线程
 */
public class ClientThread implements Runnable{
    private Selector selector;
    public ClientThread(Selector selector){
        this.selector = selector;
    }
    @Override
    public void run() {
        try{
            for (;;) {
                // 获取Channel数量
                int readChannels = selector.select();
                if (readChannels == 0) {
                    continue;
                }
                // 获取可用的channel
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                // 遍历集合
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()){
                    SelectionKey selectionKey = iterator.next();
                    // 移除Set集合当前SelectionKey
                    iterator.remove();
                    // 如果是可读状态
                    if (selectionKey.isReadable()){
                        readOperator(selector,selectionKey);
                    }
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 处理可读状态操作
     *
     * @param selector     选择器
     * @param selectionKey key
     */
    private void readOperator(Selector selector, SelectionKey selectionKey) throws IOException {
        // 1. 从SelectionKey获取已经就绪的通道
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

        // 2. 创建Buffer
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        // 3. 循环读取客户端消息
        int readLength = socketChannel.read(buffer);
        String message = "";
        if (readLength > 0) {
            // 切换读模式
            buffer.flip();
            // 读取内容
            message += UTF_8.decode(buffer);
        }
        // 4. 将Channel再次注册到选择器上，监听可读状态
        socketChannel.register(selector, SelectionKey.OP_READ);
        // 5. 把客户端发送消息，广播到其他客户端
        if (message.length() > 0) {
            // 广播给其他客户端
            System.out.println(message);
            castOtherClient(message, selector, socketChannel);
        }
    }

    /**
     * 将消息广播到其他客户端
     *
     * @param message       消息内容
     * @param selector      选择器
     * @param socketChannel 通道
     */
    private void castOtherClient(String message, Selector selector, SocketChannel socketChannel) throws IOException {
        // 1. 获取所有已经接入的channel
        Set<SelectionKey> selectionKeySet = selector.keys();
        // 2. 循环向所有channel广播消息
        for (SelectionKey selectionKey : selectionKeySet) {
            // 获取每个Channel
            SelectableChannel tarChannel = selectionKey.channel();
            // 不需要给自己发送
            if (tarChannel instanceof SocketChannel && tarChannel != socketChannel) {
                ((SocketChannel) tarChannel).write(UTF_8.encode(message));
            }
        }
    }
}

