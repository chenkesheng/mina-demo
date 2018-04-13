package com.mina;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

/**
 * @Author: cks
 * @Date: Created by 16:12 2018/4/11
 * @Package: com.mina
 * @Description:
 */
public class MyClientHandler extends IoHandlerAdapter {
    @Override
    public void exceptionCaught(IoSession session, Throwable cause) {
        System.out.println("exceptionCaught");
    }

    @Override
    public void messageReceived(IoSession session, Object message) {
        String msg = (String) message;
        System.out.println("客户端收到数据：" + msg);
    }

//    @Override
//    public void messageSent(IoSession session, Object message) throws Exception {
//        System.out.println("客户端发送数据："+);
//    }
}
