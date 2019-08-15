import common.entity.Message;
import common.entity.Type;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Test {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Message message = new Message();
        message.setPath("aaaa");
        message.setType(Type.DOWNLOAD);
        long l = System.currentTimeMillis();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(message);
        objectOutputStream.flush();
        byte[] bytes = byteArrayOutputStream.toByteArray();
        System.out.println(System.currentTimeMillis() - l);
        System.out.println(new String(bytes));
//        System.out.println(bytes.length);
//        Socket socket = new Socket("127.0.0.1", 1999);
//        OutputStream outputStream = socket.getOutputStream();
//        outputStream.write(bytes);
//        outputStream.write('\n');
//        InputStream inputStream = socket.getInputStream();
//
//        byte[] bytes2 = inputStream.readAllBytes();
//        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes2);
//        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
//        Message message2 = (Message) objectInputStream.readObject();
//
//        for (FileInfo fileInfo : message2.getFileInfoList()) {
//            System.out.println(fileInfo.isDirectory());
//            System.out.println(fileInfo.getFileName());
//            System.out.println(fileInfo.getFileLength());
//        }
    }
}
