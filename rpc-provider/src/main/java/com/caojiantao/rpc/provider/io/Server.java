package com.caojiantao.rpc.provider.io;

import com.caojiantao.rpc.common.utils.IpUtils;
import com.caojiantao.rpc.transport.codec.MessageDecoder;
import com.caojiantao.rpc.transport.codec.MessageEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
public class Server implements InitializingBean, DisposableBean {

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    @Value("${rpc.provider.port:10086}")
    private Integer port;

    public Server() {
        this.bossGroup = new NioEventLoopGroup();
        this.workerGroup = new NioEventLoopGroup();
    }

    @SneakyThrows
    public boolean start() {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast(new MessageDecoder());
                        pipeline.addLast(new ServerHandler());
                        pipeline.addLast(new MessageEncoder());
                    }
                });
        String host = IpUtils.getHostIp();
        ChannelFuture channelFuture = bootstrap.bind(host, port).sync();
        return channelFuture.isSuccess();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        boolean success = this.start();
        if (success) {
            log.info("[rpc-provider] 服务启动成功 {} {}", IpUtils.getHostIp(), port);
        } else {
            log.error("[rpc-provider] 服务启动失败 {} {}", IpUtils.getHostIp(), port);
        }
    }

    @Override
    public void destroy() throws Exception {
        this.bossGroup.shutdownGracefully().sync();
        this.workerGroup.shutdownGracefully().sync();
        log.info("[rpc-provider] 服务正常关闭");
    }
}
