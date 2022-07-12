package com.caojiantao.rpc.consumer.io;

import com.caojiantao.rpc.codec.MessageDecoder;
import com.caojiantao.rpc.codec.MessageEncoder;
import com.caojiantao.rpc.heart.ClientHeartHandler;
import com.caojiantao.rpc.protocol.EMessageType;
import com.caojiantao.rpc.protocol.Message;
import com.caojiantao.rpc.protocol.MessageHeader;
import com.caojiantao.rpc.protocol.RpcRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class Client {

    private EventLoopGroup workGroup;
    private Integer timeout = 3000;
    private Channel channel;

    private String host;
    private Integer port;

    public Client(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    @SneakyThrows
    public void connect() {
        workGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeout)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        // 20 秒之内服务器没有响应则关闭连接
                        // 10 秒之内没有请求则发送一个心跳包
                        ch.pipeline()
                                .addLast(new IdleStateHandler(20, 10, 0, TimeUnit.SECONDS))
                                .addLast(new MessageDecoder())
                                .addLast(new ClientHeartHandler())
                                .addLast(new ClientHandler())
                                .addLast(new MessageEncoder());
                    }
                });
        ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
        if (channelFuture.isSuccess()) {
            log.info("[rpc-consumer] 建立连接成功 {}:{}", host, port);
        } else {
            log.error("[rpc-consumer] 建立连接失败 {}:{}", host, port);
        }
        this.channel = channelFuture.channel();
    }

    @SneakyThrows
    public Object sendRequest(RpcRequest request) {
        Message<RpcRequest> message = new Message<>();
        MessageHeader header = new MessageHeader();
        header.setType(EMessageType.REQ);
        message.setHeader(header);
        message.setBody(request);
        ChannelFuture channelFuture = channel.writeAndFlush(message).sync();
        if (channelFuture.isSuccess()) {
            log.info("[rpc-consumer] 发起请求成功 {}", request);
        } else {
            log.error("[rpc-consumer] 发起请求失败 {}", request);
        }
        RequestFuture requestFuture = new RequestFuture(message);
        return requestFuture.get(1, TimeUnit.SECONDS);
    }
}
