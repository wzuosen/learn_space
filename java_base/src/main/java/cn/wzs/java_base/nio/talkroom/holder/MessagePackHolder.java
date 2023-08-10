package cn.wzs.java_base.nio.talkroom.holder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MessagePackHolder {

    private static final Map<Long, List<byte[]>> PACK = new ConcurrentHashMap<>();

    public static void put(Long messageId, byte[] data) {
        List<byte[]> bytes = PACK.computeIfAbsent(messageId, k -> new ArrayList<>());
        bytes.add(data);
    }

    public static List<byte[]> get(Long messageId) {
        return PACK.get(messageId);
    }

    public static void clear(Long messageId) {
        PACK.remove(messageId);
    }

    public static boolean havPack(Long messageId) {
        return PACK.containsKey(messageId);
    }
}
