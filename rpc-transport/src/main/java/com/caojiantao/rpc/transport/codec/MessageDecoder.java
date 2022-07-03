package com.caojiantao.rpc.transport.codec;

import com.caojiantao.rpc.transport.protocol.*;
import com.caojiantao.rpc.transport.serialize.ESerializeType;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;
import java.util.Objects;

/**
 * @author caojiantao
 */
public class MessageDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (!byteBuf.isReadable()) {
            return;
        }
        byteBuf.markReaderIndex();
        byte magic = byteBuf.readByte();
        if (magic != Constants.MAGIC) {
            return;
        }
        byte[] traceIdBytes = new byte[32];
        byteBuf.readBytes(traceIdBytes);
        String traceId = new String(traceIdBytes);
        byte version = byteBuf.readByte();
        byte serializeValue = byteBuf.readByte();
        byte type = byteBuf.readByte();
        int length = byteBuf.readInt();
        if (byteBuf.readableBytes() < length) {
            byteBuf.resetReaderIndex();
            return;
        }
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);

        Message message = new Message();
        MessageHeader header = new MessageHeader();
        header.setMagic(magic);
        header.setTraceId(traceId);
        header.setVersion(version);
        ESerializeType serialize = ESerializeType.ofValue(serializeValue);
        header.setSerialize(serialize);
        EMessageType messageType = EMessageType.ofValue(type);
        header.setType(messageType);
        header.setLength(length);
        message.setHeader(header);
        message.setBytes(bytes);
        list.add(message);
    }
}
