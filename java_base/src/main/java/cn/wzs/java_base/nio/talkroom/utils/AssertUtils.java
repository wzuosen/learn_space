package cn.wzs.java_base.nio.talkroom.utils;

public class AssertUtils {

    public static void state(boolean state, String message) {
        if (state) {
            throw new IllegalStateException(message);
        }
    }

    public static void notNull(Object obj) {
        state(obj == null, "obj is null");
    }
}
