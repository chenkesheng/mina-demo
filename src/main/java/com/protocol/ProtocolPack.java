package com.protocol;

/**
 * @Author: cks
 * @Date: Created by 15:36 2018/4/18
 * @Package: com.protocal
 * @Description: 协议包
 */
public class ProtocolPack {
    private int length;
    private byte flag;
    private String content;

    public ProtocolPack(byte flag, String content) {
        this.flag = flag;
        this.content = content;
        int len = content == null ? 0 : content.getBytes().length;
        this.length = 5 + len;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public byte getFlag() {
        return flag;
    }

    public void setFlag(byte flag) {
        this.flag = flag;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ProtocalPack{" +
                "length=" + length +
                ", flag=" + flag +
                ", content='" + content + '\'' +
                '}';
    }
}
