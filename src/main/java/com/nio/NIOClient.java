package com.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author: cks
 * @Date: Created by 14:56 2018/3/13
 * @Package: com.nio
 * @Description:
 */
public class NIOClient {

    private static int flag = 1;

    private static int blockSize = 4096;
    //开辟发送数据的缓冲区
    private static ByteBuffer sendBuffer = ByteBuffer.allocate(blockSize);
    //开辟接收数据的缓冲区
    private static ByteBuffer receiveBuffer = ByteBuffer.allocate(blockSize);

    private final static InetSocketAddress serverAddress = new InetSocketAddress("127.0.0.1", 7080);

    private static int count = 0;

    public static void main(String[] args) throws IOException {
        //打开通道
        SocketChannel socketChannel = SocketChannel.open();
        //设置是否阻塞
        socketChannel.configureBlocking(false);
        //打开选择器
        Selector selector = Selector.open();
        //注册事件后，证明可以开始连接//用于套接字连接操作的操作集位
        socketChannel.register(selector, SelectionKey.OP_CONNECT);
        //连接服务端
        socketChannel.connect(serverAddress);

        Set<SelectionKey> selectionKeys;

        Iterator<SelectionKey> it;

        SelectionKey selectionKey;

        SocketChannel client;

        String receiveText;

        String sendText;
        while (true) {
            selector.select();
            //获取所有的keys
            selectionKeys = selector.selectedKeys();
            it = selectionKeys.iterator();
            while (it.hasNext()) {
                selectionKey = it.next();
                if (selectionKey.isConnectable()) {
                    System.out.println("客户端开始发起连接");
                    //获取通道连接
                    client = (SocketChannel) selectionKey.channel();
                    //是否连接完成
                    if (client.isConnectionPending()) {
                        client.finishConnect();
                        System.out.println("客户端完成连接操作");
                        //清除缓冲区
                        sendBuffer.clear();
                        //写进缓冲区
                        sendBuffer.put("Hello server".getBytes());
                        //把缓冲区里的数据发送出去
                        sendBuffer.flip();
                        client.write(sendBuffer);
                    }
                    client.register(selector, SelectionKey.OP_READ);
                }
                if (selectionKey.isReadable()) {
                    client = (SocketChannel) selectionKey.channel();
                    receiveBuffer.clear();
                    count = client.read(receiveBuffer);
                    if (count > 0) {
                        receiveText = new String(receiveBuffer.array(),0,count);
                        System.out.println("客户端接收服务端的数据：" + receiveText);
                        client.register(selector,SelectionKey.OP_WRITE);
                    }
                }
                if (selectionKey.isWritable()) {
                    sendBuffer.clear();
                    client = (SocketChannel) selectionKey.channel();
                    sendText = "msg send to server ->" + flag++;
                    sendBuffer.put(sendText.getBytes());
                    sendBuffer.flip();
                    client.write(sendBuffer);
                    System.out.println("服务端发送数据给客户端" + sendText);
                    client.register(selector, SelectionKey.OP_READ);
                }
            }
            selectionKeys.clear();
        }
    }
}
