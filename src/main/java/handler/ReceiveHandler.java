package handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Arrays;

import static common.constants.MessageConf.*;
import static server_common.ServerSessions.sessions;

public class ReceiveHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf message) throws Exception {
        short keyType = message.readShort();

        if (keyType == CONNECT) {
            StringBuilder key = new StringBuilder();

            while (message.isReadable(2)) {
                char c = message.readChar();

                if (c == CONNECT_KEY_DELIMITER) {
                    break;
                } else {
                    key.append(c);
                }
            }
            // TODO 待
        } else if (keyType == SESSION) {
            byte[] session = new byte[SESSION_LENGTH];
            message.readBytes(session);

            boolean flag = false;

            for (byte[] sessionTemp : sessions) {
                if (Arrays.equals(sessionTemp, session)) {
                    flag = true;
                    break;
                }
            }

            if (flag) {
                channelHandlerContext.fireChannelRead(message);
            } else {
                channelHandlerContext.close();
            }
        } else {
            channelHandlerContext.close();
        }
    }
}
