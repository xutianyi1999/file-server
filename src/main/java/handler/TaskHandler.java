package handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

import static common.constants.MessageConf.*;
import static server_common.ServerCommons.DELIMIT_DECODER;

public class TaskHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        if (msg.isReadable(2)) {
            char type = msg.readChar();

            if (type == DOWNLOAD) {
                File file = getFile(msg);

                if (file == null) {
                    return;
                }

                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");

                long fileLength = randomAccessFile.length();
//                ByteBuf byteBuf = ctx.alloc().buffer().writeLong(fileLength);
//                byte[] bytes = new byte[byteBuf.readableBytes()];
//                System.out.println(byteBuf.readableBytes());
//                byteBuf.readBytes(bytes).release();
                ctx.write(fileLength);

                ctx.writeAndFlush(new DefaultFileRegion(randomAccessFile.getChannel(), 0, fileLength)).addListener((ChannelFutureListener) future -> {
                    randomAccessFile.close();
                });
            } else if (type == UPDATE) {
                ctx.writeAndFlush(SUCCESS);
                ChannelPipeline pipeline = ctx.pipeline();
                pipeline
                        .remove(this)
                        .remove(DELIMIT_DECODER);
//                pipeline.addLast(DEFAULT_EVENT_EXECUTOR_GROUP, UpdateHandler());
            } else if (type == FILE_LIST) {

            } else {
                ctx.close();
            }
        } else {
            ctx.close();
        }
    }

    private File getFile(ByteBuf message) {
        byte[] bytes = new byte[message.readableBytes()];
        File file = Paths.get("./", new String(bytes, StandardCharsets.UTF_8)).toFile();

        if (file.isFile()) {
            return file;
        } else {
            return null;
        }
    }
}
