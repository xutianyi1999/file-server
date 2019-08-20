package common.constants;

import java.nio.charset.StandardCharsets;

public final class MessageConf {

    // status
    public static short SUCCESS = 10001;
    public static short ERROR = 10000;

    // Basic type
    public static short DOWNLOAD = 10010;
    public static short UPDATE = 10011;
    public static short FILE_LIST = 10012;

    // Message type delimiter
    public static byte[] MESSAGE_DELIMITER = "34c9Vo9eEG0iBwe0".getBytes(StandardCharsets.UTF_8);
}
