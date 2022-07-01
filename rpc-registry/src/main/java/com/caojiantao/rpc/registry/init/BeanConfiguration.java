package com.caojiantao.rpc.registry.init;

import com.caojiantao.rpc.registry.Registry;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Value("${zookeeper.connectString}")
    private String connectString;

    @Bean
    public CuratorFramework curator() {
        CuratorFramework curator = CuratorFrameworkFactory.builder()
                .connectString(connectString)
                .retryPolicy(new ExponentialBackoffRetry(1000, 5))
                .build();
        curator.start();
        return curator;
    }

    @Bean
    public Registry registry(CuratorFramework curator) throws Exception {
        return new Registry(curator);
    }
}
