import common.entity.Message;
import common.entity.Type;

import java.io.*;
import java.net.Socket;

public class Test {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Message message = new Message();
        message.setPath("aaaa");
        message.setType(Type.DOWNLOAD);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(message);
        objectOutputStream.flush();
        byte[] bytes = byteArrayOutputStream.toByteArray();

        Socket socket = new Socket("127.0.0.1", 1999);
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(bytes);
        outputStream.write('\n');
        InputStream inputStream = socket.getInputStream();

        byte[] bytes1 = inputStream.readAllBytes();
        System.out.println(new String(bytes1));
    }
}
