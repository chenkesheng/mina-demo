package com.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;

/**
 * @Author: cks
 * @Date: Created by 9:23 2018/4/11
 * @Package: com.aio
 * @Description:
 */
public class AioServer {

    public AioServer(int port) throws Exception {
        final AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(port));
        serverSocketChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {
            @Override
            public void completed(AsynchronousSocketChannel channel, Void vi) {
                serverSocketChannel.accept(null, this);//接收下一个连接
                try {
                    handler(channel);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable exc, Void vi) {
                System.out.println("异步IO失败");
            }
        });
    }

    public void handler(AsynchronousSocketChannel channel) throws Exception {
        ByteBuffer byteBuffer = ByteBuffer.allocate(32);
        channel.read(byteBuffer).get();
        byteBuffer.flip();
        System.out.println("服务端接收:" + byteBuffer.get());
    }

    public static void main(String[] args) throws Exception {
        int port = 7080;
        AioServer aioServer = new AioServer(port);
        System.out.println("服务端监听端口:" + port);
        Thread.sleep(10000);

    }
}
