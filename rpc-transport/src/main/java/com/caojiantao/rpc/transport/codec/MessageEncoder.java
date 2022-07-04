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
public class MessageEncoder extends MessageToByteEncoder<Message> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf byteBuf) throws Exception {
        MessageHeader header = message.getHeader();
        byteBuf.writeByte(header.getMagic());
        byteBuf.writeBytes(header.getTraceId().getBytes());
        byteBuf.writeByte(header.getVersion());
        ESerializeType serialize = header.getSerialize();
        byteBuf.writeByte(serialize.getValue());
        byteBuf.writeByte(header.getType().getValue());
        ISerialization serialization = serialize.getSerialization();
        byte[] body = serialization.serialize(message.getBody());
        byteBuf.writeInt(body.length);
        byteBuf.writeBytes(body);
    }
}
