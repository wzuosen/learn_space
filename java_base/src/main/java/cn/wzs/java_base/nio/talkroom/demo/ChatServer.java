package cn.wzs.java_base.nio.talkroom.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 聊天室 服务端
 */
public class ChatServer {
    /**
     * 服务端启动方法
     *
     * @throws IOException 异常信息
     */
    public void startServer() throws IOException {
        // 1. 创建Selector选择器
        Selector selector = Selector.open();

        // 2. 创建ServerSocketChannel通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 3. 为Channel通道绑定监听端口
        serverSocketChannel.bind(new InetSocketAddress(8000));
        // 设置为非阻塞模式
        serverSocketChannel.configureBlocking(false);

        // 4. 将Channel通道注册到selector选择器上
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("服务器已经启动成功了");

        // 5. 循环，等待有新链接进入
        for (; ; ) {
            // 获取Channel数量
            int readChannels = selector.select();
            if (readChannels == 0) {
                continue;
            }
            // 获取可用的Channel
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            // 遍历集合
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                // 移除set结合当前selectionKey
                iterator.remove();
                // 6. 根据就绪状态，调用对应方法实现具体业务操作
                // 6.1 如果是accept状态
                if (selectionKey.isAcceptable()) {
                    acceptOperator(serverSocketChannel, selector);
                }
                // 6.2 如果是可读状态
                if (selectionKey.isReadable()) {
                    readOperator(selector, selectionKey);
                }
            }
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

    /**
     * 处理accept状态操作
     *
     * @param serverSocketChannel 通道
     * @param selector            选择器
     */
    private void acceptOperator(ServerSocketChannel serverSocketChannel, Selector selector) throws IOException{
        // 1. 接入状态，创建SocketCh
        SocketChannel socketChannel = serverSocketChannel.accept();

        // 2. 把SocketChannel设置非阻塞模式
        socketChannel.configureBlocking(false);

        // 3. 把Channel注册到Selector选择器上，监听可读状态
        socketChannel.register(selector,SelectionKey.OP_READ);

        // 4. 客户端回复消息
        socketChannel.write(UTF_8.encode("欢迎进入聊天室，请注意隐私安全。"));
    }

    /**
     * 主方法启动
     *
     * @param args 参数
     */
    public static void main(String[] args) {
        try {
            new ChatServer().startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


