package cn.wzs.java_base.nio.talkroom.base;

import lombok.Data;

@Data
public class TalkMessage extends AbstractMessage{

    private String context;

    public TalkMessage() {
        this(TalkMessage.class);
    }

    public TalkMessage(Class<? extends TalkMessage> clazz) {
        super(clazz);
        setType(MessageType.TALK);
    }
}
