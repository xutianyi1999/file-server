package common.constants;

import java.nio.charset.StandardCharsets;

public final class MessageConf {

    // status
    public static byte[] SUCCESS = "10001".getBytes(StandardCharsets.UTF_8);
    public static byte[] ERROR = "10000".getBytes(StandardCharsets.UTF_8);

    // Basic type
    public static byte[] DOWNLOAD = "10010".getBytes(StandardCharsets.UTF_8);
    public static byte[] UPDATE = "10011".getBytes(StandardCharsets.UTF_8);
    public static byte[] FILE_LIST = "10012".getBytes(StandardCharsets.UTF_8);

    public static int MESSAGE_HEAD_LENGTH = 5;
}
