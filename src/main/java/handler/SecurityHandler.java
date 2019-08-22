package handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import server_common.ServerCommons;

import java.nio.charset.StandardCharsets;

import static server_common.ServerCommons.SECURITY_HANDLER;

public class SecurityHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf message) {
        byte[] bytes = new byte[message.readableBytes()];
        message.readBytes(bytes);
        String key = new String(bytes, StandardCharsets.UTF_8);

        boolean flag = false;

        for (String tempKey : ServerCommons.keys) {
            if (tempKey.equals(key)) {
                flag = true;
                break;
            }
        }

        if (flag) {
            ctx.write(ctx.channel().remoteAddress().toString().getBytes(StandardCharsets.UTF_8));
            ctx.writeAndFlush("\n".getBytes(StandardCharsets.UTF_8)).addListener((ChannelFutureListener) future -> ctx.pipeline().remove(SECURITY_HANDLER));
        } else {
            ctx.close();
        }
    }
}
