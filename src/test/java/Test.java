import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Test {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Socket socket = new Socket("127.0.0.1", 19998);
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write("123456\n".getBytes(StandardCharsets.UTF_8));
        InputStream inputStream = socket.getInputStream();
        ArrayList<Byte> bytes = new ArrayList<>();

        long l = System.currentTimeMillis();
        while (true) {
            byte read = (byte) inputStream.read();
            if (read == '\n') {
                break;
            } else {
                bytes.add(read);
            }
        }

        byte[] bytes2 = new byte[bytes.size()];

        for (int i = 0; i < bytes2.length; i++) {
            bytes2[i] = bytes.get(i);
        }
        System.out.println(System.currentTimeMillis() - l);
        String s = new String(bytes2);
        System.out.println(s);
    }
}
