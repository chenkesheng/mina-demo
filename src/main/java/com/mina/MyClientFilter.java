package com.mina;

import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequest;

/**
 * @Author: cks
 * @Date: Created by 15:33 2018/4/12
 * @Package: com.mina
 * @Description:
 */
public class MyClientFilter extends IoFilterAdapter {
    @Override
    public void messageReceived(NextFilter nextFilter, IoSession session, Object message) {
        System.out.println("MyClientFilter -> messageReceived");
        nextFilter.messageReceived(session, message);
    }

    @Override
    public void messageSent(NextFilter nextFilter, IoSession session, WriteRequest writeRequest) {
        System.out.println("MyClientFilter -> messageSent");
        nextFilter.messageSent(session, writeRequest);
    }
}
