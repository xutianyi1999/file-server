package server_common;

import io.netty.util.concurrent.DefaultEventExecutorGroup;

public class ServerCommons {
    public static String[] keys;
    public static int port;

    public static final String DELIMIT_DECODER = "delimitDecoder";
    public static final String SECURITY_HANDLER = "securityHandler";
    public static final String TASK_HANDLER = "taskHandler";
    public static final String CHUNKED_HANDLER = "chunkedHandler";
    public static final String BYTE_ENCODER = "byteEncoder";

    public static final DefaultEventExecutorGroup DEFAULT_EVENT_EXECUTOR_GROUP = new DefaultEventExecutorGroup(16);
}
