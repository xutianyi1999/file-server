package handler.model;

import common.entity.FileInfo;
import common.entity.Message;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileListHandler extends SimpleChannelInboundHandler<Message> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) {
        File path;

        try {
            path = Paths.get("./", message.getPath()).toFile();
        } catch (Exception e) {
            ChannelFuture channelFuture = channelHandlerContext.writeAndFlush(message);
            channelFuture.addListener(ChannelFutureListener.CLOSE);
            return;
        }

        if (path.isDirectory()) {
            File[] files = path.listFiles();

            if (files != null) {
                ArrayList<FileInfo> fileInfoList = new ArrayList<>();

                for (File file : files) {
                    FileInfo fileInfo = new FileInfo();
                    fileInfo.setDirectory(file.isDirectory());
                    fileInfo.setFileName(file.getName());
                    fileInfo.setFileLength(file.length());
                    fileInfoList.add(fileInfo);
                }
                message.setFileInfoList(fileInfoList);
            }
        }

        ChannelFuture channelFuture = channelHandlerContext.writeAndFlush(message);
        channelFuture.addListener(ChannelFutureListener.CLOSE);
    }
}
