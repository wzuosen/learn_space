package cn.wzs.java_base.nio.talkroom.holder;

import java.nio.channels.Channel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TalkChannelHolder {

    private static final Map<Channel, String> CHANNEL_HOLDER = new ConcurrentHashMap<>();

    public static String getCode(Channel channel) {
        return CHANNEL_HOLDER.get(channel);
    }

    public static String remove(Channel channel) {
        return CHANNEL_HOLDER.remove(channel);
    }

    public static void put(Channel channel, String code) {
        CHANNEL_HOLDER.put(channel, code);
    }
}
