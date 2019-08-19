package server_common;

import java.util.ArrayList;
import java.util.Random;

public class ServerCommons {
    public static Random random = new Random();
    public static ArrayList<byte[]> sessions = new ArrayList<>();
    public static String[] keys;
    public static int port;
}
