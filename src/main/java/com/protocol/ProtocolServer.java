package com.protocol;

import com.mina.MyHandler;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * @Author: cks
 * @Date: Created by 13:51 2018/4/19
 * @Package: com.protocol
 * @Description:
 */
public class ProtocolServer {
    private static final int port = 7080;

    public static void main(String[] args) throws IOException {
        IoAcceptor acceptor = new NioSocketAcceptor();
        acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ProtocolFactory(Charset.forName("UTF-8"))));
        acceptor.getSessionConfig().setReadBufferSize(1024);
        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
        acceptor.setHandler(new MyHandler());
        acceptor.bind(new InetSocketAddress(port));
        System.out.println("Server start->->->");
    }

    static class MyHandler extends IoHandlerAdapter {

        @Override
        public void sessionCreated(IoSession session) throws Exception {
            super.sessionCreated(session);
        }

        @Override
        public void sessionOpened(IoSession session) throws Exception {
            super.sessionOpened(session);
        }

        @Override
        public void sessionClosed(IoSession session) throws Exception {
            super.sessionClosed(session);
        }

        @Override
        public void sessionIdle(IoSession session, IdleStatus status) {
            System.out.println("server -> sessionIdle");
        }

        @Override
        public void exceptionCaught(IoSession session, Throwable cause) {
            System.out.println("server ->exceptionCaught ");
        }

        @Override
        public void messageReceived(IoSession session, Object message) {
            ProtocolPack pack = (ProtocolPack) message;
            System.out.println("服务端接收:" + pack);
        }

        @Override
        public void messageSent(IoSession session, Object message) throws Exception {
            super.messageSent(session, message);
        }

        @Override
        public void inputClosed(IoSession session) throws Exception {
            super.inputClosed(session);
        }
    }
}
