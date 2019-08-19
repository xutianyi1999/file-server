package handler.model;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import static common.constants.MessageConf.SESSION_LENGTH;
import static server_common.ServerCommons.random;

public class ConnectionHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        byte[] bytes = new byte[SESSION_LENGTH];
        random.nextBytes(bytes);
    }
}
