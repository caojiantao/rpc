package com.caojiantao.rpc.provider;

import com.caojiantao.rpc.config.RpcConfig;
import com.caojiantao.rpc.provider.init.ProviderRegister;
import com.caojiantao.rpc.provider.io.Server;
import com.caojiantao.rpc.registry.RegistryConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author caojiantao
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@EnableConfigurationProperties({RpcConfig.class})
@Import({RegistryConfiguration.class, ProviderRegister.class, Server.class})
public @interface EnableRpcProvider {

}
