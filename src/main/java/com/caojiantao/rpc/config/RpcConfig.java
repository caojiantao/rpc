package com.caojiantao.rpc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author caojiantao
 */
@Data
@ConfigurationProperties(prefix = "rpc")
public class RpcConfig {

    private RegistryConfig registry;
    private ProviderConfig provider;
    private ConsumerConfig consumer;
}
