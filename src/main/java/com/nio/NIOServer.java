package com.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author: cks
 * @Date: Created by 13:52 2018/3/13
 * @Package: com.nio
 * @Description:
 */
public class NIOServer {
    private int flag = 1;

    private int blockSize = 4096;
    //开辟发送数据的缓冲区
    private ByteBuffer sendBuffer = ByteBuffer.allocate(blockSize);
    //开辟接收数据的缓冲区
    private ByteBuffer receiveBuffer = ByteBuffer.allocate(blockSize);

    private Selector selector;

    public NIOServer(int port) throws IOException {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //设置是否阻塞
        serverSocketChannel.configureBlocking(false);
        //获取socket对象
        ServerSocket serverSocket = serverSocketChannel.socket();
        //绑定ip端口
        serverSocket.bind(new InetSocketAddress(port));
        //打开选择器
        selector = Selector.open();
        //注册到socket通道中
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("Server start -->" + port);
    }

    //监听事件
    public void listen() throws IOException {
        //轮询选择器
        while (true) {
            //获取事件列表
            selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> it = keys.iterator();
            while (it.hasNext()) {
                SelectionKey selectionKey = it.next();
                it.remove();
                //业务逻辑
                handlerKey(selectionKey);
            }
        }
    }

    public void handlerKey(SelectionKey selectionKey) throws IOException {
        ServerSocketChannel server;
        SocketChannel client;
        String receiveText;
        String sendText;
        int count;
        //判断是否可以接收
        if (selectionKey.isAcceptable()) {
            //接收的channel是服务端的channel
            server = (ServerSocketChannel) selectionKey.channel();
            //代表可以接收客户端的连接，获取客户端的对象
            client = server.accept();
            //设置是否阻塞
            client.configureBlocking(false);
            //把客户端注册给selector
            client.register(selector, SelectionKey.OP_READ);
            //判断是否可读
        } else if (selectionKey.isReadable()) {
            //客户端的channel
            client = (SocketChannel) selectionKey.channel();
            count = client.read(receiveBuffer);
            if (count > 0) {
                //读取接收缓冲区的数据->也就是客户端发送过来的数据
                receiveText = new String(receiveBuffer.array(), 0, count);
                System.out.println("服务端接收到客户端的数据" + receiveText);
                //把客户端注册给selector
                client.register(selector, SelectionKey.OP_WRITE);
            }
            //判断是否可写
        } else if (selectionKey.isWritable()) {
            //清空写缓冲区
            sendBuffer.clear();
            //获取客户端的channel
            client = (SocketChannel) selectionKey.channel();
            //发送的数据
            sendText = "msg send to client:" + flag++;
            //把数据写到缓冲区里
            sendBuffer.put(sendText.getBytes());
            //把缓冲区里的数据发送出去
            sendBuffer.flip();
            client.write(sendBuffer);
            System.out.println("服务端发送数据给了客户端" + sendText);
        }
    }

    public static void main(String[] args) throws IOException {
        int port = 7080;
        NIOServer server = new NIOServer(port);
        server.listen();
    }
}
