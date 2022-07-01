package com.caojiantao.rpc.transport.codec;

import com.caojiantao.rpc.transport.protocol.Message;
import com.caojiantao.rpc.transport.protocol.MessageHeader;
import com.caojiantao.rpc.transport.serialize.ESerializeType;
import com.caojiantao.rpc.transport.serialize.ISerialization;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author caojiantao
 */
public class Encoder extends MessageToByteEncoder<Message> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf byteBuf) throws Exception {
        MessageHeader header = message.getHeader();
        byteBuf.writeByte(header.getMagic());
        byteBuf.writeLong(header.getId());
        byteBuf.writeByte(header.getVersion());
        // 序列化类型
        ESerializeType serialize = header.getSerialize();
        byteBuf.writeByte(serialize.value);
        byteBuf.writeByte(header.getType().value);
        ISerialization serialization = serialize.clazz.newInstance();
        byte[] body = serialization.serialize(message.getBody());
        // 长度
        byteBuf.writeLong(body.length);
        // 内容
        byteBuf.writeBytes(body);
    }
}
