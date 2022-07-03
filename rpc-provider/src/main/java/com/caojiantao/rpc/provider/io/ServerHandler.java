package com.caojiantao.rpc.provider.io;

import com.caojiantao.rpc.common.utils.JsonUtils;
import com.caojiantao.rpc.registry.utils.SpringUtils;
import com.caojiantao.rpc.transport.protocol.EMessageType;
import com.caojiantao.rpc.transport.protocol.Message;
import com.caojiantao.rpc.transport.protocol.MessageHeader;
import com.caojiantao.rpc.transport.protocol.RpcRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Objects;

@Slf4j
public class ServerHandler extends SimpleChannelInboundHandler<Message> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        log.info("[rpc-provider] 服务端收到消息 {}", msg.getHeader());
        MessageHeader header = msg.getHeader();
        RpcRequest request = header.getSerialize().serialization.deserialize(msg.getBytes(), RpcRequest.class);
        ApplicationContext context = SpringUtils.getContext();
        Method method = ReflectionUtils.findMethod(request.getClazz(), request.getName(), request.getParameterTypes());
        int argsLength = request.getParameterTypes().length;
        Object[] args = new Object[argsLength];
        for (int i = 0; i < argsLength; i++) {
            byte[] argBytes = JsonUtils.bytes(request.getArgs()[i]);
            Class<?> argType = request.getParameterTypes()[i];
            Object arg = JsonUtils.parse(argBytes, argType);
            args[i] = arg;
        }
        Object result = method.invoke(context.getBean(request.getClazz()), args);
        Message<Object> respMsg = new Message<>();
        MessageHeader respMsgHeader = new MessageHeader();
        respMsgHeader.setTraceId(header.getTraceId());
        respMsgHeader.setType(EMessageType.RESPONSE);
        respMsg.setHeader(respMsgHeader);
        respMsg.setBody(result);

        ctx.channel().writeAndFlush(respMsg).sync();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        log.error("[rpc-provider] 服务端处理消息异常", cause);
    }
}
