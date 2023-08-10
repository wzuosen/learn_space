package cn.wzs.java_base.nio.talkroom;

import cn.wzs.java_base.nio.talkroom.base.AbstractMessage;
import cn.wzs.java_base.nio.talkroom.helper.MessageHelper;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class TalkRoomServer {

    public void start() {
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            Selector selector = Selector.open();
            serverSocketChannel.bind(new InetSocketAddress(8000));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            while (true) {
                selector.select(1000L);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (key.isAcceptable()) {
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ);
                    }
                    if (key.isReadable()) {
                        SocketChannel channel = (SocketChannel) key.channel();
                        List<AbstractMessage> messageList = MessageHelper.readByteMessageFromChannel(channel);
                        MessageHelper.showMessage(messageList);
                        for (AbstractMessage message : messageList) {
                            MessageHelper.serverHandlerMessage(message, channel);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
