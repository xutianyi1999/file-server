import com.alibaba.fastjson.JSON;
import handler.SecurityHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import server_common.FileServerConfigEntity;
import server_common.ServerCommons;

import java.io.IOException;
import java.io.InputStream;

import static common.constants.MessageConf.MESSAGE_DELIMITER;

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
                        ChannelPipeline pipeline = socketChannel.pipeline();

                        pipeline.addLast(
                                new DelimiterBasedFrameDecoder(Integer.MAX_VALUE, socketChannel.alloc().buffer().writeBytes(MESSAGE_DELIMITER)),
                                new SecurityHandler()
                        );
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
