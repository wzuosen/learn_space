package cn.wzs.java_base.nio.talkroom.utils;

import cn.wzs.java_base.nio.talkroom.base.CommandMessage;
import cn.wzs.java_base.nio.talkroom.base.MessageType;
import cn.wzs.java_base.nio.talkroom.base.TalkMessage;

public class MessageBuilderUtils {

    public static TalkMessage copyMessage(TalkMessage originMessage) {
        TalkMessage newMessage = new TalkMessage();
        newMessage.setContext(originMessage.getContext());
        newMessage.setFrom(originMessage.getFrom());
        newMessage.setTo(originMessage.getTo());
        newMessage.setTime(originMessage.getTime());
        newMessage.setType(originMessage.getType());
        return newMessage;
    }

    public static TalkMessage addMessage(String code) {
        TalkMessage addMessage = new TalkMessage();
        addMessage.setFrom(code);
        addMessage.setContext("欢迎[" + code + "]" + "加入!");
        addMessage.setTime(DateTimeUtils.nowTime());
        addMessage.setType(MessageType.TALK);
        return addMessage;
    }

    public static TalkMessage removeMessage(String code) {
        TalkMessage addMessage = new TalkMessage();
        addMessage.setFrom(code);
        addMessage.setContext("[" + code + "]" + "离开!");
        addMessage.setTime(DateTimeUtils.nowTime());
        addMessage.setType(MessageType.TALK);
        return addMessage;
    }


    public static TalkMessage talkMessage(String code, String context) {
        TalkMessage addMessage = new TalkMessage();
        addMessage.setFrom(code);
        addMessage.setContext(context);
        addMessage.setTime(DateTimeUtils.nowTime());
        addMessage.setType(MessageType.TALK);
        return addMessage;
    }

    public static TalkMessage sendMessage(String code, String context) {
        TalkMessage addMessage = new TalkMessage();
        addMessage.setTo(code);
        addMessage.setContext(context);
        addMessage.setTime(DateTimeUtils.nowTime());
        addMessage.setType(MessageType.TALK);
        return addMessage;
    }


    public static TalkMessage uploadMessage(String fileName) {
        TalkMessage addMessage = new TalkMessage();
        addMessage.setContext("file " + fileName + " upload success");
        addMessage.setTime(DateTimeUtils.nowTime());
        addMessage.setType(MessageType.NOTICE);
        return addMessage;
    }

    public static CommandMessage commandMessage(String code, String command) {
        CommandMessage commandMessage = new CommandMessage();
        commandMessage.setFrom(code);
        commandMessage.setCommand(command);
        commandMessage.setTime(DateTimeUtils.nowTime());
        return commandMessage;
    }

    public static TalkMessage registryMessage(String code) {
        TalkMessage message = new TalkMessage();
        message.setFrom(code);
        message.setTime(DateTimeUtils.nowTime());
        message.setType(MessageType.REGISTRY);
        return message;
    }

    public static TalkMessage unRegistryMessage(String code) {
        TalkMessage message = new TalkMessage();
        message.setFrom(code);
        message.setTime(DateTimeUtils.nowTime());
        message.setType(MessageType.UN_REGISTRY);
        return message;
    }

    public static TalkMessage noticeMessage(String content) {
        TalkMessage message = new TalkMessage();
        message.setFrom("system");
        message.setType(MessageType.NOTICE);
        message.setTime(DateTimeUtils.nowTime());
        message.setContext(content);
        return message;
    }
}
