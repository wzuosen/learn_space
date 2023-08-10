package cn.wzs.java_base.nio.talkroom.helper;

import cn.wzs.java_base.nio.talkroom.base.*;
import cn.wzs.java_base.nio.talkroom.holder.TalkChannelHolder;
import cn.wzs.java_base.nio.talkroom.holder.TalkUserHolder;
import cn.wzs.java_base.nio.talkroom.utils.MessageBuilderUtils;
import cn.wzs.java_base.nio.talkroom.utils.SerializableUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class MessageHelper {

    public static final Integer _1M = 1024 * 1024;

    private static final TalkUserHolder talkUserHolder = new TalkUserHolder();

    private static final FileHelper fileHelper = new FileHelper();

    public static void singleAll(TalkMessage talkMessage) {
        Collection<TalkUser> allUsers = talkUserHolder.getAllUsersIgnore(talkMessage.getFrom());
//        Collection<TalkUser> allUsers = talkUserHolder.getAllUsersIgnore();
        for (TalkUser user : allUsers) {
            SocketChannel channel = user.getChannel();
            if (channel.isConnected()) {
                TalkMessage message = MessageBuilderUtils.copyMessage(talkMessage);
                message.setTo(user.getCode());
                sendMessageChannel(SerializableUtils.serializable(message), user.getChannel());
            }
        }
    }

    public static void single(TalkMessage talkMessage) {
        TalkUser user = talkUserHolder.getUser(talkMessage.getTo());
        SocketChannel channel = user.getChannel();
        if (channel.isConnected()) {
            TalkMessage message = MessageBuilderUtils.copyMessage(talkMessage);
            message.setTo(user.getCode());
            sendMessageChannel(SerializableUtils.serializable(message), user.getChannel());
        }
    }

    public static void sendMessageChannel(byte[] data, SocketChannel channel) {
        try {
            if (channel.isConnected()) {
                ByteBuffer byteBuffer = ByteBuffer.allocate(_1M);
                int total = data.length;
                byteBuffer.putInt(total);
                byteBuffer.put(data);
                byteBuffer.flip();
                channel.write(byteBuffer);
            }
        } catch (Exception e) {
            // ignore
            e.printStackTrace();
        }
    }

    public static List<AbstractMessage> readByteMessageFromChannel(SocketChannel channel) {
        List<AbstractMessage> messageList = new ArrayList<>();
        try {

            ByteBuffer allocate = ByteBuffer.allocate(_1M);
            // 提取前置,用于处理沾包、拆包问题
            int lastWriteIndex = -1;
            byte[] data = null;
            while (true) {
                int read = channel.read(allocate);
                if (read == -1) {
                    break;
                }
                allocate.flip();
                while (true) {
                    if (allocate.position() == allocate.limit()) {
                        // 数据读取完成跳出循环
                        break;
                    }
                    if (lastWriteIndex != -1) {
                        allocate.get(data, lastWriteIndex, data.length - lastWriteIndex);
                        lastWriteIndex = -1;
                    } else {
                        // 沾包拆包处理
                        int total = allocate.getInt();
                        data = new byte[total];
                        if (allocate.position() + total > allocate.limit()) {
                            lastWriteIndex = allocate.limit() - allocate.position();
                            // 有拆包发生,则先读一部分数据到数组中
                            allocate.get(data, 0, lastWriteIndex);
                            continue;
                        } else {
                            allocate.get(data);
                        }
                    }
                    AbstractMessage deserialization = SerializableUtils.deserialization(data);
                    messageList.add(deserialization);
                }
                if (lastWriteIndex == -1) {
                    break;
                }
                allocate.clear();
            }
        } catch (IOException ioException) {
            String code = TalkChannelHolder.getCode(channel);
            TalkChannelHolder.remove(channel);
            talkUserHolder.removeUser(code);
            try {
                channel.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return messageList;
    }


    public static void serverHandlerMessage(AbstractMessage message, SocketChannel channel) {
        if (message == null) {
            return;
        }
        switch (message.getType()) {
            case REGISTRY:
                // 上线
                addUser(message, channel);
                break;
            case TALK:
                singleMessage((TalkMessage) message);
                break;
            case UN_REGISTRY:
                // 下线
                removeUser(message);
                break;
            case UPLOAD_FILE:
                uploadFile((FileMessage) message);
                break;
            case COMMAND:
                handlerCommand((CommandMessage) message);
                break;
            default:
                //
        }
    }

    public static void addUser(AbstractMessage message, SocketChannel channel) {
        String code = message.getFrom();
        TalkUser user = new TalkUser();
        user.setCode(code);
        user.setChannel(channel);
        user.setLoginTimeStamp(System.currentTimeMillis());
        try {
            InetSocketAddress address = (InetSocketAddress) channel.getRemoteAddress();
            user.setIp(address.getHostString());
            user.setPort(address.getPort());
        } catch (Exception e) {
            // ignore
        }
        talkUserHolder.addUser(user);
        TalkChannelHolder.put(channel, code);
        singleAll(MessageBuilderUtils.addMessage(code));
    }

    public static void removeUser(AbstractMessage message) {
        String code = message.getFrom();
        try {
            talkUserHolder.getUser(code).getChannel().close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        talkUserHolder.removeUser(code);
        singleAll(MessageBuilderUtils.removeMessage(code));
    }

    public static void singleMessage(TalkMessage message) {
        if (Objects.isNull(message.getTo())) {
            singleAll(message);
        } else {
            single(message);
        }
    }

    public static void showMessage(List<AbstractMessage> messageList) {
        for (AbstractMessage abstractMessage : messageList) {
            switch (abstractMessage.getType()) {
                case TALK:
                    TalkMessage message = (TalkMessage) abstractMessage;
                    System.out.println("【" + message.getTime() + "】【" + message.getFrom() + "】:" + message.getContext());
                    break;
                case UPLOAD_FILE:
                    FileMessage fileMessage = (FileMessage) abstractMessage;
                    if (fileMessage.getDone()) {
                        System.out.println("【" + fileMessage.getTime() + "】【" + fileMessage.getFrom() + "】" + "upload:" + fileMessage.getFileName());
                    }
                    break;
                case NOTICE:
                    TalkMessage noticeMessage = (TalkMessage) abstractMessage;
                    System.out.println("【" + noticeMessage.getTime() + "】" + noticeMessage.getContext());
                    break;
                default:
            }
        }
    }

    public static void send(AbstractMessage message, SocketChannel channel) {
        byte[] data = SerializableUtils.serializable(message);
        sendMessageChannel(data, channel);
    }

    public static void uploadFile(FileMessage fileMessage) {
        fileHelper.receiveFile(fileMessage);
        if (fileMessage.getDone()) {
            TalkMessage message = MessageBuilderUtils.uploadMessage(fileMessage.getFileName());
            singleAll(message);
        }
    }

    public static void handlerCommand(CommandMessage message) {
        String command = message.getCommand();
        CommandType commandType = CommandType.getCommandType(command);
        if (commandType == null) {
            return;
        }
        String from = message.getFrom();
        switch (commandType) {
            case LSF:
                List<String> files = FileHelper.getFiles();
                TalkMessage resp = MessageBuilderUtils.sendMessage(from, files.toString());
                resp.setFrom("system");
                single(resp);
                break;
            case DOWNLOAD_FILE:
                String downloadFileName = (String) message.getTarget();
                FileHelper.uploadFile("system", talkUserHolder.getUser(from).getChannel(), FileHelper.TEMP + downloadFileName);
                System.out.println("send file end...");
                break;
            case DELETE_FILE:
                String deleteFileName = (String) message.getTarget();
                FileHelper.deleteFile(deleteFileName);
                TalkMessage fileDeleteNoticeMessage = MessageBuilderUtils.noticeMessage("file:" + deleteFileName + " is deleted");
                singleAll(fileDeleteNoticeMessage);
                break;
            default:
                break;
        }
    }

    public static void clientHandlerMessage(AbstractMessage message, SocketChannel channel) {
        if (message == null) {
            return;
        }
        switch (message.getType()) {
            case UPLOAD_FILE:
                fileHelper.receiveFile((FileMessage) message, "C:\\Users\\清流浅溪\\Desktop\\files\\client\\");
                break;
            default:
        }
    }
}
