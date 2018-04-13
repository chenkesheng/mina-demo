package com.mina;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * @Author: cks
 * @Date: Created by 15:03 2018/4/11
 * @Package: com.mina
 * @Description:
 */
public class MinaServer {
    static int port = 7080;
    static SocketAcceptor accepter;

    public static void main(String[] args) {
        try {
            accepter = new NioSocketAcceptor();
            //设置编码过滤器
            accepter.getFilterChain().addLast("codec", new ProtocolCodecFilter(
                    new TextLineCodecFactory(Charset.forName("UTF-8"),
                            LineDelimiter.WINDOWS.getValue(),
                            LineDelimiter.WINDOWS.getValue())));

            accepter.getFilterChain().addFirst("filter", new MyServerFilter());
            accepter.getSessionConfig().setReceiveBufferSize(1024);
            accepter.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
            accepter.setHandler(new MyHandler());
            accepter.bind(new InetSocketAddress(port));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Server start ->" + port);
    }


}
