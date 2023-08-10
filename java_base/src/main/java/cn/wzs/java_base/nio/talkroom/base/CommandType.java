package cn.wzs.java_base.nio.talkroom.base;

public enum CommandType {

    LSF("lsf"), UPLOAD_FILE("su"), DOWNLOAD_FILE("sd"), DELETE_FILE("del"), EXIT("exit"), SINGLE("@");

    CommandType(String command) {
        this.command = command;
    }

    private String command;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public static CommandType getCommandType(String command) {
        if (command == null || command.equals("")) {
            return null;
        }
        command = command.split(" ")[0];
        command = command.toLowerCase();
        for (CommandType ct : values()) {
            if (command.equals(ct.getCommand())) {
                return ct;
            }
        }
        return null;
    }

    public static boolean isCommand(String msg) {
        return null != getCommandType(msg);
    }
}
