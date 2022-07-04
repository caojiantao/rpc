package com.caojiantao.rpc.provider.io;

import com.caojiantao.rpc.common.utils.SpringUtils;
import com.caojiantao.rpc.transport.protocol.EMessageType;
import com.caojiantao.rpc.transport.protocol.Message;
import com.caojiantao.rpc.transport.protocol.MessageHeader;
import com.caojiantao.rpc.transport.protocol.RpcRequest;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

@Slf4j
public class ServerHandler extends SimpleChannelInboundHandler<Message<RpcRequest>> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message<RpcRequest> reqMsg) throws Exception {
        log.info("[rpc-provider] 服务端收到消息 {}", reqMsg);
        MessageHeader reqHeader = reqMsg.getHeader();
        RpcRequest rpcReq = reqMsg.getBody();
        Method method = ReflectionUtils.findMethod(rpcReq.getClazz(), rpcReq.getName(), rpcReq.getParameterTypes());
        Object[] args = rpcReq.getArgs();
        ApplicationContext context = SpringUtils.getContext();
        Object result = method.invoke(context.getBean(rpcReq.getClazz()), args);
        Message<Object> respMsg = new Message<>();
        MessageHeader respMsgHeader = new MessageHeader();
        respMsgHeader.setTraceId(reqHeader.getTraceId());
        respMsgHeader.setType(EMessageType.RESPONSE);
        respMsg.setHeader(respMsgHeader);
        respMsg.setBody(result);
        ChannelFuture sendFuture = ctx.channel().writeAndFlush(respMsg).sync();
        if (sendFuture.isSuccess()) {
            log.info("[rpc-provider] 服务端响应成功 {}", respMsg);
        } else {
            log.info("[rpc-provider] 服务端响应失败 {}", respMsg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        log.error("[rpc-provider] 服务端处理消息异常", cause);
    }
}
