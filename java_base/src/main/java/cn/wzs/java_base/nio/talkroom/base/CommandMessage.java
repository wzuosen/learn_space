package cn.wzs.java_base.nio.talkroom.base;

import lombok.Data;

@Data
public class CommandMessage extends AbstractMessage{

    public CommandMessage() {
        super(CommandMessage.class);
        setType(MessageType.COMMAND);
    }

    private String command;

    private Object target;
}
