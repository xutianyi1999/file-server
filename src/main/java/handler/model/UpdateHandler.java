package handler.model;

import handler.TaskHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.LineBasedFrameDecoder;

import java.io.RandomAccessFile;

import static common.constants.MessageConf.SUCCESS;
import static server_common.ServerCommons.*;

public class UpdateHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private RandomAccessFile randomAccessFile;
    private long fileLength;

    public UpdateHandler(RandomAccessFile randomAccessFile, long fileLength) {
        this.randomAccessFile = randomAccessFile;
        this.fileLength = fileLength;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        byte[] bytes = new byte[msg.readableBytes()];
        msg.readBytes(bytes);
        randomAccessFile.write(bytes);

        if (randomAccessFile.getFilePointer() >= fileLength) {
            randomAccessFile.close();

            ctx.writeAndFlush(SUCCESS).addListener((ChannelFutureListener) future -> {
                ctx.pipeline().remove(UPDATE_HANDLER);
                ctx.pipeline()
                        .addLast(DELIMIT_DECODER, new LineBasedFrameDecoder(1024))
                        .addLast(TASK_HANDLER, new TaskHandler());
            });
        }
    }
}
