package com.protocol;


import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * @Author: cks
 * @Date: Created by 15:53 2018/4/18
 * @Package: com.protocol
 * @Description: 解码器 ->把字节流转换成编码对象交给handler业务层处理
 */
public class MyProtocolDecoder implements ProtocolDecoder {

    private final AttributeKey CONTEXT = new AttributeKey(this.getClass(), "context");

    private final Charset charset;

    private int maxPackLength = 100;

    public int getMaxPackLength() {
        return maxPackLength;
    }

    public void setMaxPackLength(int maxPackLength) {
        if (maxPackLength < 0) {
            throw new IllegalArgumentException("maxPackLength参数:" + maxPackLength);
        }
        this.maxPackLength = maxPackLength;
    }

    public MyProtocolDecoder() {
        this(Charset.defaultCharset());
    }

    public MyProtocolDecoder(Charset charset) {
        this.charset = charset;
    }

    public Context getContext(IoSession session) {
        Context context = (Context) session.getAttribute(CONTEXT);
        if (context == null) {
            context = new Context();
            session.setAttribute(CONTEXT, context);
        }
        return context;
    }


    @Override
    public void decode(IoSession session, IoBuffer buffer, ProtocolDecoderOutput output) throws Exception {
        final int packHeaderLength = 5;
        Context context = this.getContext(session);
        context.append(buffer);
        IoBuffer buf = context.getBuffer();
        buf.flip();
        while (buf.remaining() >= packHeaderLength) {
            buf.mark();
            int length = buf.getInt();
            byte flag = buf.get();
            if (length < 0 || length > maxPackLength) {
                buf.reset();
                break;
            } else if (length >= packHeaderLength && length - packHeaderLength <= buf.remaining()) {
                int oldLimit = buf.limit();
                buf.limit(buf.position() + length - packHeaderLength);
                String ctx = buf.getString(context.getDecoder());
                buf.limit(oldLimit);
                ProtocolPack pack = new ProtocolPack(flag, ctx);
                output.write(pack);
            } else {//半包
                buf.clear();
                break;
            }
        }
        if (buf.hasRemaining()) {
            IoBuffer ioBuffer = IoBuffer.allocate(maxPackLength).setAutoExpand(true);
            ioBuffer.put(buf);
            ioBuffer.flip();
            buf.reset();
            buf.put(ioBuffer);
        } else {
            buf.reset();
        }
    }

    @Override
    public void finishDecode(IoSession session, ProtocolDecoderOutput output) throws Exception {

    }

    @Override
    public void dispose(IoSession session) throws Exception {

        Context context = (Context) session.getAttribute(CONTEXT);
        if (context != null) {
            session.removeAttribute(CONTEXT);
        }
    }

    private class Context {
        private final CharsetDecoder decoder;
        private IoBuffer buffer;

        public void append(IoBuffer in) {
            this.getBuffer().put(in);
        }

        public Context() {
            decoder = charset.newDecoder();
            buffer = IoBuffer.allocate(80).setAutoExpand(true);
        }

        public CharsetDecoder getDecoder() {
            return decoder;
        }

        public void reset() {
            decoder.reset();
        }

        public IoBuffer getBuffer() {
            return buffer;
        }

        public void setBuffer(IoBuffer buffer) {
            this.buffer = buffer;
        }
    }
}
