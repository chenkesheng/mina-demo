package com.protocol;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import java.nio.charset.Charset;

/**
 * @Author: cks
 * @Date: Created by 15:43 2018/4/18
 * @Package: com.protocal
 * @Description: 编码器->主要是把对象转换成字节流
 */
public class MyProtocolEncoder extends ProtocolEncoderAdapter {

    private final Charset charset;

    public MyProtocolEncoder(Charset charset) {
        this.charset = charset;
    }

    @Override
    public void encode(IoSession session, Object message, ProtocolEncoderOutput protocolEncoderOutput) throws Exception {
        ProtocolPack protocolPack = (ProtocolPack) message;
        IoBuffer buffer = IoBuffer.allocate(protocolPack.getLength());
        buffer.setAutoExpand(true);
        buffer.putInt(protocolPack.getLength());
        buffer.put(protocolPack.getFlag());
        if (protocolPack.getContent() != null) {
            buffer.put(protocolPack.getContent().getBytes());
        }
        buffer.flip();
        protocolEncoderOutput.write(buffer);
    }
}
