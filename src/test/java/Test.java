import java.io.IOException;
import java.io.RandomAccessFile;

public class Test {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        RandomAccessFile randomAccessFile = new RandomAccessFile("./aaa.txt", "rw");
        randomAccessFile.writeInt(1);
        randomAccessFile.writeInt(1);
        long filePointer = randomAccessFile.getFilePointer();
        System.out.println(filePointer);

//        Socket socket = new Socket("127.0.0.1", 19998);
//        OutputStream outputStream = socket.getOutputStream();
//        outputStream.write("QQ85100636".getBytes());
//        outputStream.write(MESSAGE_DELIMITER);
//
//        outputStream.write("QQ85100636".getBytes());
//        outputStream.write(MESSAGE_DELIMITER);
//        outputStream.write("aaa".getBytes());
//        outputStream.write(MESSAGE_DELIMITER);
    }
}
