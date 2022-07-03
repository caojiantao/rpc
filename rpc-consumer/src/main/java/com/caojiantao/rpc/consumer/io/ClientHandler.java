package com.caojiantao.rpc.consumer.io;

import com.caojiantao.rpc.transport.protocol.Message;
import com.caojiantao.rpc.transport.protocol.MessageHeader;
import com.caojiantao.rpc.transport.protocol.RpcResponse;
import com.google.common.collect.Maps;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class ClientHandler extends SimpleChannelInboundHandler<Message> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        MessageHeader header = msg.getHeader();
        log.info("[rpc-consumer] 客户端收到消息 {}", header);
        RequestFuture future = ClientRequestManager.getFuture(header.getTraceId());
        if (Objects.nonNull(future)) {
            future.set(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        log.error("[rpc-consumer] 客户端处理消息异常", cause);
    }
}
