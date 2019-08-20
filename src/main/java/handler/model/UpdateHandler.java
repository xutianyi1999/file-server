package handler.model;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.RandomAccessFile;

import static common.constants.MessageConf.SUCCESS;

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
        randomAccessFile.write(bytes);

        if (randomAccessFile.getFilePointer() >= fileLength) {
            randomAccessFile.close();
            ctx.writeAndFlush(SUCCESS);
        }
    }
}
