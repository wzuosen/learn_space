package cn.wzs.java_base.nio.talkroom.base;

import lombok.Data;

import java.nio.channels.SocketChannel;

@Data
public class TalkUser {

    private String code;

    private String ip;

    private Integer port;

    private Long loginTimeStamp;

    private SocketChannel channel;

}
