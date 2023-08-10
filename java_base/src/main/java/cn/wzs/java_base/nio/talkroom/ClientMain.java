package cn.wzs.java_base.nio.talkroom;

public class ClientMain {

    public static void main(String[] args) {
        TalkRoomClient talkRoomClient = new TalkRoomClient("127.0.0.1", 8000);
        talkRoomClient.start();
    }
}
