package com.caojiantao.rpc.codec;

import com.caojiantao.rpc.protocol.Constants;
import com.caojiantao.rpc.protocol.EMessageType;
import com.caojiantao.rpc.protocol.Message;
import com.caojiantao.rpc.protocol.MessageHeader;
import com.caojiantao.rpc.serialize.ESerializeType;
import com.caojiantao.rpc.serialize.ISerialization;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

/**
 * @author caojiantao
 */
@Slf4j
public class MessageDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (!byteBuf.isReadable()) {
            return;
        }
        byteBuf.markReaderIndex();
        // 参数解析
        byte magic = byteBuf.readByte();
        if (magic != Constants.MAGIC) {
            log.error("[rpc-core] RPC 消息报文非法");
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
        // 构建 RPC 消息
        Message<Object> message = new Message<>();
        MessageHeader header = new MessageHeader();
        header.setMagic(magic);
        header.setTraceId(traceId);
        header.setVersion(version);
        ESerializeType serialize = ESerializeType.ofValue(serializeValue);
        if (Objects.isNull(serialize)) {
            log.error("[rpc-core] RPC 序列化协议非法");
            return;
        }
        header.setSerialize(serialize);
        EMessageType messageType = EMessageType.ofValue(type);
        header.setType(messageType);
        header.setLength(length);
        message.setHeader(header);
        // 反序列化，携带 class 信息
        ISerialization serialization = serialize.getSerialization();
        Object body = serialization.deserialize(bytes, Object.class);
        message.setBody(body);
        list.add(message);
    }
}
