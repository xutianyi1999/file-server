package handler;

import handler.model.UpdateHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static common.constants.MessageConf.*;
import static server_common.ServerCommons.*;

public class TaskHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        if (msg.isReadable(MESSAGE_HEAD_LENGTH)) {
            byte[] type = new byte[MESSAGE_HEAD_LENGTH];
            msg.readBytes(type);

            if (Arrays.equals(type, DOWNLOAD)) {
                File file = getFile(msg);

                if (file == null) {
                    return;
                }

                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");

                long fileLength = randomAccessFile.length();
                ByteBuf byteBuf = ctx.alloc().buffer().writeLong(fileLength);
                byte[] bytes = new byte[byteBuf.readableBytes()];
                byteBuf.readBytes(bytes).release();
                ctx.write(bytes);
                ctx.writeAndFlush(new DefaultFileRegion(randomAccessFile.getChannel(), 0, fileLength)).addListener((ChannelFutureListener) future -> randomAccessFile.close());
            } else if (Arrays.equals(type, UPDATE)) {
                long fileLength;

                if (msg.isReadable(8)) {
                    fileLength = msg.readLong();
                } else {
                    return;
                }

                byte[] bytes = new byte[msg.readableBytes()];
                msg.readBytes(bytes);
                String filePath = new String(bytes, StandardCharsets.UTF_8);
                RandomAccessFile accessFile = getAccessFile(filePath);

                if (accessFile == null) {
                    return;
                }

                ctx.writeAndFlush(SUCCESS).addListener((ChannelFutureListener) future -> {
                    ChannelPipeline pipeline = ctx.pipeline();
                    pipeline.remove(TASK_HANDLER);
                    pipeline.remove(DELIMIT_DECODER);
                    pipeline.addLast(DEFAULT_EVENT_EXECUTOR_GROUP, UPDATE_HANDLER, new UpdateHandler(accessFile, fileLength));
                });

            } else if (Arrays.equals(type, FILE_LIST)) {

            } else {
                ctx.close();
            }
        } else {
            ctx.close();
        }
    }

    private File getFile(ByteBuf message) {
        byte[] bytes = new byte[message.readableBytes()];
        message.readBytes(bytes);
        File file = new File("./", new String(bytes, StandardCharsets.UTF_8));

        if (file.isFile()) {
            return file;
        } else {
            return null;
        }
    }

    // TODO basic
    private RandomAccessFile getAccessFile(String filePath) throws FileNotFoundException {
        File file = new File("./", filePath);
        File parentFile = file.getParentFile();

        if (!parentFile.exists()) {
            if (!parentFile.mkdirs()) {
                return null;
            }
        }

        return new RandomAccessFile(file, "rw");
    }
}
