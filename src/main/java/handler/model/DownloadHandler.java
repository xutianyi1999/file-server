package handler.model;

import common.entity.Message;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class DownloadHandler extends SimpleChannelInboundHandler<Message> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {
        ChannelFuture channelFuture = channelHandlerContext.writeAndFlush(message.getPath());
        channelFuture.addListener(ChannelFutureListener.CLOSE);
    }
}
