import java.io.File;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static common.constants.MessageConf.DOWNLOAD;
import static common.constants.MessageConf.SUCCESS;

public class Test {

    private ByteBuffer fileLength;

    public static void main(String[] args) throws Exception {
        byte[] statusBytes = new byte[5];
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 19998));

        //连接
        byte[] key = "123456\n".getBytes(StandardCharsets.UTF_8);
        socketChannel.write(ByteBuffer.wrap(key));

        //返回
        ByteBuffer status = ByteBuffer.allocate(5);
        socketChannel.read(status);

        status.flip().get(statusBytes);

        if (!Arrays.equals(statusBytes, SUCCESS)) {
            return;
        } else {
            System.out.println("true");
        }

        ByteBuffer message = ByteBuffer.allocate(1024);
        message.put(DOWNLOAD);
        message.put("confluent-5.3.0-2.12.tar.gz\n".getBytes(StandardCharsets.UTF_8));
        socketChannel.write(message.flip());
        ByteBuffer fileLength = ByteBuffer.allocate(8);
        socketChannel.read(fileLength);
        FileChannel fileChannel = new RandomAccessFile(new File("C:\\Users\\xutia\\Desktop\\download\\test.tar.gz"), "rw").getChannel();
        fileChannel.transferFrom(socketChannel, 0, fileLength.flip().getLong());
        socketChannel.read(fileLength);
    }
}
