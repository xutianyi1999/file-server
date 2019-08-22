import com.alibaba.fastjson.JSON;
import handler.SecurityHandler;
import handler.TaskHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import server_common.FileServerConfigEntity;
import server_common.ServerCommons;

import java.io.IOException;
import java.io.InputStream;

import static server_common.ServerCommons.*;

public class Main {

    public static void main(String[] args) {
        load();
        start();
    }

    private static void load() {
        try (InputStream inputStream = Main.class.getResourceAsStream("/file-server-config.json")) {
            FileServerConfigEntity fileServerConfigEntity = JSON.parseObject(inputStream, FileServerConfigEntity.class);
            ServerCommons.keys = fileServerConfigEntity.getKeys();
            ServerCommons.port = fileServerConfigEntity.getPort();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void start() {
        boolean isLinux = System.getProperty("os.name").contains("Linux");

        EventLoopGroup bossGroup;
        EventLoopGroup workerGroup;
        Class<? extends ServerSocketChannel> serverSocketChannel;

        if (isLinux) {
            System.out.println("Epoll Model");
            bossGroup = new EpollEventLoopGroup();
            workerGroup = new EpollEventLoopGroup();
            serverSocketChannel = EpollServerSocketChannel.class;
        } else {
            System.out.println("NIO Model");
            bossGroup = new NioEventLoopGroup();
            workerGroup = new NioEventLoopGroup();
            serverSocketChannel = NioServerSocketChannel.class;
        }

        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap.group(bossGroup, workerGroup)
                .channel(serverSocketChannel)
                .handler(new LoggingHandler(LogLevel.ERROR))
                .childHandler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        socketChannel.pipeline()
                                .addLast(BYTE_ENCODER, new ByteArrayEncoder())
                                .addLast(CHUNKED_HANDLER, new ChunkedWriteHandler())
                                .addLast(DELIMIT_DECODER, new LineBasedFrameDecoder(1024))
                                .addLast(SECURITY_HANDLER, new SecurityHandler())
                                .addLast(TASK_HANDLER, new TaskHandler());
                    }
                });

        try {
            Channel channel = serverBootstrap.bind(ServerCommons.port).sync().channel();
            channel.closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
