package handler;

import handler.model.ConnectionHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;

import static common.constants.MessageConf.*;

public class SelectHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        if (msg.isReadable(2)) {
            char type = msg.readChar();
            ChannelPipeline pipeline = ctx.pipeline();

            if (type == CONNECT) {
                pipeline.addLast(
                        new ConnectionHandler()
                );
                ctx.fireChannelRead(msg);
            } else if (type == DOWNLOAD) {

            } else if (type == UPDATE) {

            } else if (type == FILE_LIST) {

            } else {
                ctx.close();
            }
        } else {
            ctx.close();
        }
    }
}
