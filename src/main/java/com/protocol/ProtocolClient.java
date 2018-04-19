package com.protocol;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * @Author: cks
 * @Date: Created by 14:12 2018/4/19
 * @Package: com.protocol
 * @Description:
 */
public class ProtocolClient {

    private static final String host = "127.0.0.1";

    private static final int port = 7080;

    static long counter = 0;

    final static int file = 100;

    static long start = 0;

    public static void main(String[] args) {
        start = System.currentTimeMillis();
        IoConnector connector = new NioSocketConnector();
        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ProtocolFactory(Charset.forName("UTF-8"))));
        connector.getSessionConfig().setReadBufferSize(1024);
        connector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
        connector.setHandler(new MyClientHandler());
        ConnectFuture future = connector.connect(new InetSocketAddress(host, port));
        future.addListener(new IoFutureListener<ConnectFuture>() {
            @Override
            public void operationComplete(ConnectFuture connectFuture) {
                if (connectFuture.isConnected()) {
                    IoSession session = connectFuture.getSession();
                    sendData(session);
                }
            }
        });
    }

    public static void sendData(IoSession session) {
        for (int i = 0; i < file; i++) {
            String connect = "cks" + i;
            ProtocolPack pack = new ProtocolPack((byte) i, connect);
            session.write(pack);
            System.out.println("客户端发送数据:" + pack);
        }
    }
}

class MyClientHandler extends IoHandlerAdapter {

    public MyClientHandler() {
        super();
    }


    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        if (status == IdleStatus.READER_IDLE){
            session.closeNow();
        }
    }


    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        ProtocolPack pack = (ProtocolPack) message;
        System.out.println("client ->" + pack);
    }
}