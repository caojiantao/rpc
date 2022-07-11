package com.caojiantao.rpc.provider.io;

import com.caojiantao.rpc.codec.MessageDecoder;
import com.caojiantao.rpc.codec.MessageEncoder;
import com.caojiantao.rpc.config.RpcConfig;
import com.caojiantao.rpc.utils.IpUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class Server implements InitializingBean, DisposableBean {

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    @Autowired
    private RpcConfig rpcConfig;
    @Autowired
    private ListableBeanFactory beanFactory;

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
                        pipeline.addLast(new ServerHandler(beanFactory));
                        pipeline.addLast(new MessageEncoder());
                    }
                });
        String host = IpUtils.getHostIp();
        Integer port = rpcConfig.getProvider().getPort();
        ChannelFuture channelFuture = bootstrap.bind(host, port).sync();
        return channelFuture.isSuccess();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Integer port = rpcConfig.getProvider().getPort();
        boolean success = this.start();
        if (success) {
            log.info("[rpc-provider] netty 启动成功 {}:{}", IpUtils.getHostIp(), port);
        } else {
            log.error("[rpc-provider] netty 启动失败 {}:{}", IpUtils.getHostIp(), port);
        }
    }

    @Override
    public void destroy() throws Exception {
        this.bossGroup.shutdownGracefully().sync();
        this.workerGroup.shutdownGracefully().sync();
        log.info("[rpc-provider] 服务正常关闭");
    }
}
