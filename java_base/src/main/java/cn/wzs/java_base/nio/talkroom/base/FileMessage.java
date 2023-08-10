package cn.wzs.java_base.nio.talkroom.base;

import cn.wzs.java_base.nio.talkroom.utils.DateTimeUtils;
import lombok.Data;

@Data
public class FileMessage extends AbstractMessage {

    public FileMessage() {
        super(FileMessage.class);
        setType(MessageType.UPLOAD_FILE);
        setDone(false);
        setTime(DateTimeUtils.nowTime());
    }

    private String fileName;

    private Boolean done;

    private byte[] data;

    private Long total;
}
