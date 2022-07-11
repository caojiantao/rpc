package com.caojiantao.rpc.registry;

import com.caojiantao.rpc.config.RegistryConfig;
import com.caojiantao.rpc.config.RpcConfig;
import com.caojiantao.rpc.registry.impl.ZKRegistry;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RegistryConfiguration {

    @Bean(initMethod = "start", destroyMethod = "close")
    @ConditionalOnClass(CuratorFramework.class)
    public CuratorFramework curator(RpcConfig rpcConfig) {
        RegistryConfig registry = rpcConfig.getRegistry();
        return CuratorFrameworkFactory.builder()
                .connectString(registry.getAddress())
                .retryPolicy(new ExponentialBackoffRetry(1000, 5))
                .build();
    }

    @Bean
    @ConditionalOnBean(CuratorFramework.class)
    public ZKRegistry registry(CuratorFramework curator) throws Exception {
        return new ZKRegistry(curator);
    }
}
