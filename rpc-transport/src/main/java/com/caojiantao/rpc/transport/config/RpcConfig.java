package com.caojiantao.rpc.transport.config;

import lombok.Data;

/**
 * @author caojiantao
 */
@Data
public class RpcConfig {

    private RegistryConfig registry;
    private ProviderConfig provider;
    private ConsumerConfig consumer;
}
