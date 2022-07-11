package com.caojiantao.rpc.provider.io;

import com.caojiantao.rpc.protocol.EMessageType;
import com.caojiantao.rpc.protocol.Message;
import com.caojiantao.rpc.protocol.MessageHeader;
import com.caojiantao.rpc.protocol.RpcRequest;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

@Slf4j
@AllArgsConstructor
public class ServerHandler extends SimpleChannelInboundHandler<Message<RpcRequest>> {

    private ListableBeanFactory beanFactory;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message<RpcRequest> reqMsg) throws Exception {
        log.info("[rpc-provider] 服务端收到消息 {}", reqMsg);
        MessageHeader reqHeader = reqMsg.getHeader();
        RpcRequest rpcReq = reqMsg.getBody();
        Method method = ReflectionUtils.findMethod(rpcReq.getClazz(), rpcReq.getName(), rpcReq.getParameterTypes());
        Object[] args = rpcReq.getArgs();
        Object service = beanFactory.getBean(rpcReq.getClazz());
        Object result = method.invoke(service, args);
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
