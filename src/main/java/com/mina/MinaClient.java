package com.mina;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * @Author: cks
 * @Date: Created by 16:05 2018/4/11
 * @Package: com.mina
 * @Description:
 */
public class MinaClient {

    private static String host = "127.0.0.1";
    private static int port = 7080;

    public static void main(String[] args) {
        IoSession ioSession;
        IoConnector connector = new NioSocketConnector();
        connector.setConnectTimeoutMillis(3000);
        //设置过滤器
        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(
                new TextLineCodecFactory(Charset.forName("UTF-8"),
                        LineDelimiter.WINDOWS.getValue(),
                        LineDelimiter.WINDOWS.getValue())));
        connector.getFilterChain().addFirst("filter",new MyClientFilter() );
        connector.setHandler(new MyClientHandler());
        ConnectFuture connectFuture = connector.connect(new InetSocketAddress(host, port));
        //等待连接 在这阻塞
        connectFuture.awaitUninterruptibly();
        ioSession = connectFuture.getSession();
        ioSession.write("你好!mina");
        //等待关闭连接
        ioSession.getCloseFuture().awaitUninterruptibly();
        //关闭连接
        connector.dispose();
    }
}
