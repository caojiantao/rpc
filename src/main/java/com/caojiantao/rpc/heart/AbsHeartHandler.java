package com.caojiantao.rpc.heart;

import com.caojiantao.rpc.protocol.EMessageType;
import com.caojiantao.rpc.protocol.Message;
import com.caojiantao.rpc.protocol.MessageHeader;
import com.caojiantao.rpc.protocol.RpcRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author caojiantao
 */
@Slf4j
public abstract class AbsHeartHandler extends SimpleChannelInboundHandler<Message<Void>> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message<Void> msg) throws Exception {
        MessageHeader header = msg.getHeader();
        String address = ctx.channel().remoteAddress().toString();
        if (EMessageType.PING.equals(header.getType())) {
            log.info("[rpc-core] 收到客户端心跳，响应 ACK {}", address);
            sendHeart(ctx, EMessageType.PONG);
        } else if (EMessageType.PONG.equals(header.getType())) {
            log.info("[rpc-core] 收到服务端心跳 ACK {}", address);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    public void sendHeart(ChannelHandlerContext ctx, EMessageType messageType) {
        Message<RpcRequest> message = new Message<>();
        MessageHeader header = new MessageHeader();
        header.setType(messageType);
        message.setHeader(header);
        message.setBody(null);
        ctx.channel().writeAndFlush(message);
    }
}
