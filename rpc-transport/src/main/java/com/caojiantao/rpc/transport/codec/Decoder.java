package com.caojiantao.rpc.transport.codec;

import com.caojiantao.rpc.transport.protocol.Constants;
import com.caojiantao.rpc.transport.protocol.EMessageType;
import com.caojiantao.rpc.transport.protocol.Message;
import com.caojiantao.rpc.transport.protocol.MessageHeader;
import com.caojiantao.rpc.transport.serialize.ESerializeType;
import com.caojiantao.rpc.transport.serialize.ISerialization;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author caojiantao
 */
public class Decoder extends ByteToMessageDecoder {

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
        long id = byteBuf.readLong();
        byte version = byteBuf.readByte();
        byte serializeValue = byteBuf.readByte();
        byte type = byteBuf.readByte();
        int length = byteBuf.readInt();
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);

        Message message = new Message();
        MessageHeader header = new MessageHeader();
        header.setMagic(magic);
        header.setId(id);
        header.setVersion(version);
        ESerializeType serialize = ESerializeType.ofValue(serializeValue);
        header.setSerialize(serialize);
        header.setType(EMessageType.ofValue(type));
        header.setLength(length);
        message.setHeader(header);

        ISerialization serialization = serialize.clazz.newInstance();
        // todo 
    }
}
