package codec;

import common.entity.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.List;

public class MessageDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int size = byteBuf.readableBytes();
        byte[] bytes = new byte[size];
        byteBuf.readBytes(bytes);

        try (
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)
        ) {
            Object object = objectInputStream.readObject();

            if (object instanceof Message) {
                list.add(object);
            } else {
                channelHandlerContext.close();
            }
        } catch (Exception e) {
            channelHandlerContext.close();
            throw e;
        }
    }
}
