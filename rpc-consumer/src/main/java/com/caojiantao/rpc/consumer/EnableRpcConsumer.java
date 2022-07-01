package com.caojiantao.rpc.consumer;

import com.caojiantao.rpc.consumer.init.ProviderFetch;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author caojiantao
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(ProviderFetch.class)
public @interface EnableRpcConsumer {

    String[] basePackages() default {};
}
