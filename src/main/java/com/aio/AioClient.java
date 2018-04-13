package com.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.Future;

/**
 * @Author: cks
 * @Date: Created by 9:38 2018/4/11
 * @Package: com.aio
 * @Description:
 */
public class AioClient {

    private AsynchronousSocketChannel client;

    public AioClient(String host, int port) throws Exception {
        client = AsynchronousSocketChannel.open();
        Future<?> future = client.connect(new InetSocketAddress(host, port));
        future.get();
        System.out.println(future.get());
    }

    public void write(byte b) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(32);
        byteBuffer.put(b);
        byteBuffer.flip();
        client.write(byteBuffer);
    }

    public static void main(String[] args) throws Exception {
        String host = "127.0.0.1";
        int port = 7080;
        AioClient aioClient = new AioClient(host, port);
        aioClient.write((byte) 11);
    }
}
