package handler;

import codec.MessageEncoder;
import common.entity.Message;
import common.entity.Type;
import handler.model.DownloadHandler;
import handler.model.FileListHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

public class ReceiveHandler extends SimpleChannelInboundHandler<Message> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {
        if (message.getType() == Type.DOWNLOAD) {
            channelHandlerContext.pipeline().addLast(
                    new StringEncoder(),
                    new ChunkedWriteHandler(),
                    new DownloadHandler()
            );
            channelHandlerContext.fireChannelRead(message);
        } else if (message.getType() == Type.UPDATE) {
        } else if (message.getType() == Type.SELECT) {
            channelHandlerContext.pipeline().addLast(
                    new MessageEncoder(),
                    new FileListHandler()
            );
            channelHandlerContext.fireChannelRead(message);
        }
    }
}
