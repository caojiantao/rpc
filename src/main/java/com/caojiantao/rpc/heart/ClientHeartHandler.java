package com.caojiantao.rpc.heart;

import com.caojiantao.rpc.protocol.EMessageType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @author caojiantao
 */
@Slf4j
public class ClientHeartHandler extends AbsHeartHandler {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.WRITER_IDLE) {
                log.info("[rpc-core] 客户端空闲，发送心跳包");
                sendHeart(ctx, EMessageType.PING);
            } else if (state == IdleState.READER_IDLE) {
                log.info("[rpc-core] 服务端响应超时，关闭此连接");
                ctx.channel().close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
