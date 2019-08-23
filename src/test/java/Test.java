import java.io.File;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static common.constants.MessageConf.SUCCESS;
import static common.constants.MessageConf.UPDATE;

public class Test {

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
        }

        ByteBuffer message = ByteBuffer.allocate(1024);
        RandomAccessFile randomAccessFile = new RandomAccessFile(new File("C:\\Users\\xutia\\Downloads\\confluent-5.3.0-2.12.tar.gz"), "rw");
        long fileLength = randomAccessFile.length();
        message.put(UPDATE);
        message.putLong(fileLength);
        message.put("confluent-5.3.0-2.12.tar.gz\n".getBytes(StandardCharsets.UTF_8));
        socketChannel.write(message.flip());

        socketChannel.read(status.flip().clear());
        status.flip().get(statusBytes);
        if (!Arrays.equals(statusBytes, SUCCESS)) {
            return;
        }

        FileChannel fileChannel = randomAccessFile.getChannel();
        long position = 0;

        while (position < fileLength) {
            position += fileChannel.transferTo(position, fileLength, socketChannel);
        }

        socketChannel.read(status.flip().clear());
        status.flip().get(statusBytes);

        if (Arrays.equals(statusBytes, SUCCESS)) {
            System.out.println("true");
        }
        socketChannel.close();
    }
}
