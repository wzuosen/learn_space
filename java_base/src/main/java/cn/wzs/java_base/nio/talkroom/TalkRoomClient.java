package cn.wzs.java_base.nio.talkroom;


import cn.wzs.java_base.nio.talkroom.base.AbstractMessage;
import cn.wzs.java_base.nio.talkroom.base.CommandMessage;
import cn.wzs.java_base.nio.talkroom.base.CommandType;
import cn.wzs.java_base.nio.talkroom.base.TalkMessage;
import cn.wzs.java_base.nio.talkroom.constants.StringKit;
import cn.wzs.java_base.nio.talkroom.helper.CmdLogHelper;
import cn.wzs.java_base.nio.talkroom.helper.FileHelper;
import cn.wzs.java_base.nio.talkroom.helper.MessageHelper;
import cn.wzs.java_base.nio.talkroom.utils.MessageBuilderUtils;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 聊天室客户端
 */
public class TalkRoomClient {

    /**
     * 聊天室基础属性
     */
    private String ip;
    private Integer port;
    private String code;
    /**
     * 服务器channel,用于发送消息
     */
    private SocketChannel channel;

    /**
     * 链接状态
     */
    private final AtomicInteger state;

    public TalkRoomClient(String ip, Integer port, String code) {
        this.ip = ip;
        this.port = port;
        this.code = code;
        state = new AtomicInteger(0);
    }

    public TalkRoomClient(String ip, Integer port) {
        this(ip, port, null);
    }

    public TalkRoomClient() {
        this(null, null, null);
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        init(scanner);
        new Thread(() -> cmd(scanner)).start();
        try {
            connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化必要属性
     *
     * @param scanner 输入流
     */
    void init(Scanner scanner) {
        if (Objects.isNull(ip)) {
            CmdLogHelper.println("请输入服务器IP地址:");
            ip = scanner.nextLine();
        }
        if (Objects.isNull(port)) {
            CmdLogHelper.println("请输入服务器端口:");
            String portStr = scanner.nextLine();
            port = Integer.parseInt(portStr);
        }
        if (Objects.isNull(code)) {
            CmdLogHelper.println("请输入登陆用户名:");
            code = scanner.nextLine();
        }
    }

    void cmd(Scanner scanner) {
        init(scanner);
        while (state.get() == 0) {
            try {
                TimeUnit.SECONDS.sleep(1L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("----------------------------------------");
        System.out.println("connect success");
        registry();
        while (true) {
            String msg = scanner.nextLine();
            if (CommandType.isCommand(msg)) {
                CommandType command = CommandType.getCommandType(msg);
                String[] params = msg.split(StringKit.BLANK);
                switch (command) {
                    case EXIT:
                        // 退出
                        state.set(-1);
                        unRegistry();
                        break;
                    case LSF:
                        // 展示上传文件列表
                        MessageHelper.send(MessageBuilderUtils.commandMessage(code, CommandType.LSF.getCommand()), channel);
                        break;
                    case SINGLE:
                        // 私聊
                        TalkMessage singleMessage = MessageBuilderUtils.talkMessage(code, params[2]);
                        singleMessage.setTo(params[1]);
                        MessageHelper.send(singleMessage, channel);
                        break;
                    case DELETE_FILE:
                        // 删除文件
                        CommandMessage deleteFileMessage = MessageBuilderUtils.commandMessage(code, CommandType.DELETE_FILE.getCommand());
                        deleteFileMessage.setTarget(params[1]);
                        MessageHelper.send(deleteFileMessage, channel);
                        break;
                    case DOWNLOAD_FILE:
                        // 下载文件
                        CommandMessage downloadFileCommand = MessageBuilderUtils.commandMessage(code, CommandType.DOWNLOAD_FILE.getCommand());
                        downloadFileCommand.setTarget(params[1]);
                        MessageHelper.send(downloadFileCommand, channel);
                        break;
                    case UPLOAD_FILE:
                        // 上传文件
                        FileHelper.uploadFile(code, channel, params[1]);
                        break;
                    default:
                }
            } else {
                TalkMessage message = MessageBuilderUtils.talkMessage(code, msg);
                MessageHelper.send(message, channel);
            }
            if (state.get() == -1) {
                break;
            }
        }
        System.out.println("客户端【" + code + "】退出！");
    }


    void connect() throws Exception {
        SocketChannel clientChannel = SocketChannel.open();
        Selector selector = Selector.open();
        // 设置非阻塞模式
        clientChannel.configureBlocking(false);
        clientChannel.connect(new InetSocketAddress(ip, port));

        clientChannel.register(selector, SelectionKey.OP_CONNECT);
        while (true) {
            selector.select(1000L);
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                if (key.isConnectable()) {
                    // 链接服务器成功
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    if (socketChannel.finishConnect()) {
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ);
                        channel = socketChannel;
                        state.set(1);
                    }
                }
                if (key.isReadable()) {
                    List<AbstractMessage> messageList = MessageHelper.readByteMessageFromChannel(channel);
                    for (AbstractMessage message : messageList) {
                        MessageHelper.clientHandlerMessage(message, channel);
                    }
                    MessageHelper.showMessage(messageList);
                }
            }
            if (state.get() == -1) {
                // 退出销毁资源
                clientChannel.close();
                selector.close();
                channel.close();
                break;
            }
        }
    }

    void registry() {
        TalkMessage message = MessageBuilderUtils.registryMessage(code);
        MessageHelper.send(message, channel);
    }

    void unRegistry() {
        TalkMessage message = MessageBuilderUtils.unRegistryMessage(code);
        MessageHelper.send(message, channel);
    }
}
