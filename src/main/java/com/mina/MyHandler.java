package com.mina;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import java.util.Date;

/**
 * @Author: cks
 * @Date: Created by 15:39 2018/4/11
 * @Package: com.mina
 * @Description: 自定义处理器
 */
public class MyHandler extends IoHandlerAdapter {

    public MyHandler() {
        super();
    }

    @Override
    public void sessionCreated(IoSession session) {
        System.out.println("sessionCreated");
    }

    @Override
    public void sessionOpened(IoSession session) {
        System.out.println("sessionOpened");
    }

    @Override
    public void sessionClosed(IoSession session) {
        System.out.println("sessionClosed");
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) {
        System.out.println("sessionIdle");
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) {
        System.out.println("exceptionCaught");
    }

    @Override
    public void messageReceived(IoSession session, Object message) {
        String msg = (String) message;
        System.out.println("服务端接收到数据：" + msg);
//        if (msg.equals("exit")){
//            session.closeNow();
//        }
        Date date = new Date();
        session.write(date);
    }

    @Override
    public void messageSent(IoSession session, Object message) {
        System.out.println("messageSent");
        //发送完就关闭这是短连接的处理方式
        session.closeNow();
    }

    @Override
    public void inputClosed(IoSession session) throws Exception {
        super.inputClosed(session);
    }
}
