package com.caojiantao.rpc.consumer;

import com.caojiantao.rpc.consumer.init.ProviderDiscovery;
import com.caojiantao.rpc.consumer.io.ClientHandler;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author caojiantao
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({ProviderDiscovery.class, ClientHandler.class})
public @interface EnableRpcConsumer {

    @AliasFor("basePackages")
    String[] value() default {};

    @AliasFor("value")
    String[] basePackages() default {};
}
