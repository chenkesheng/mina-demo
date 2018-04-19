package com.protocol;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

import java.nio.charset.Charset;

/**
 * @Author: cks
 * @Date: Created by 13:45 2018/4/19
 * @Package: com.protocol
 * @Description:
 */
public class ProtocolFactory implements ProtocolCodecFactory {

    private final MyProtocolDecoder decoder;

    private final MyProtocolEncoder encoder;

    public ProtocolFactory(Charset charset) {
        encoder = new MyProtocolEncoder(charset);
        decoder = new MyProtocolDecoder(charset);
    }

    @Override
    public ProtocolEncoder getEncoder(IoSession session) {
        return encoder;
    }

    @Override
    public ProtocolDecoder getDecoder(IoSession session) {
        return decoder;
    }
}
