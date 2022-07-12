package com.caojiantao.rpc.consumer.io;

import com.caojiantao.rpc.protocol.Message;
import com.caojiantao.rpc.protocol.MessageHeader;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class ClientHandler extends SimpleChannelInboundHandler<Message<Object>> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message<Object> msg) throws Exception {
        log.info("[rpc-consumer] 客户端收到消息 {}", msg);
        MessageHeader header = msg.getHeader();
        RequestFuture future = ClientRequestManager.getFuture(header.getTraceId());
        Optional.ofNullable(future).ifPresent(item -> item.set(msg));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        log.error("[rpc-consumer] 客户端处理消息异常", cause);
    }
}
