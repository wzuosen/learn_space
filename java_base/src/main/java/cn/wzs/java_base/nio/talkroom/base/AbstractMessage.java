package cn.wzs.java_base.nio.talkroom.base;

import lombok.Data;

import java.io.Serializable;

@Data
public class AbstractMessage implements Serializable {
    public AbstractMessage(Class<? extends AbstractMessage> clazz) {
        this.clazz = clazz;
    }

    public AbstractMessage(){

    }

    private String from;

    private String to;

    private String time;

    private MessageType type;

    private Class<? extends AbstractMessage> clazz;
}
