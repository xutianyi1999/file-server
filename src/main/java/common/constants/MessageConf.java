package common.constants;

import java.nio.charset.StandardCharsets;

public final class MessageConf {

    // Key type
    public static short CONNECT = 10000;
    public static short SESSION = 10001;

    // Length
    public static int SESSION_LENGTH = 16;

    // Basic type
    public static short DOWNLOAD = 10010;
    public static short UPDATE = 10011;
    public static short FILE_LIST = 10012;
    public static short FILE_BLOCK = 10013;

    // Message type delimiter
    public static byte[] MESSAGE_DELIMITER = "34c9Vo9eEG0iBwe0".getBytes(StandardCharsets.UTF_8);
    public static char CONNECT_KEY_DELIMITER = '\n';
}
