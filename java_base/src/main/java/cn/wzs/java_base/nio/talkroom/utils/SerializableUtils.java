package cn.wzs.java_base.nio.talkroom.utils;

import cn.wzs.java_base.nio.talkroom.base.AbstractMessage;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.nio.charset.StandardCharsets;

public class SerializableUtils {

    public static byte[] serializable(AbstractMessage message) {
        return JSON.toJSONString(message).getBytes(StandardCharsets.UTF_8);
    }

    @SuppressWarnings("all")
    public static AbstractMessage deserialization(byte[] data) {
        try {
            String jsonStr = new String(data);
            JSONObject jsonObject = JSON.parseObject(jsonStr);
            Class clazz = Class.forName(jsonObject.getString("clazz"));
            return (AbstractMessage) JSON.parseObject(jsonStr, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
