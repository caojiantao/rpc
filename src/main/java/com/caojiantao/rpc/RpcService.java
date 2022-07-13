package com.caojiantao.rpc;

import com.caojiantao.rpc.balancer.IBalancer;
import com.caojiantao.rpc.balancer.impl.RandomBalancer;
import com.caojiantao.rpc.registry.ServiceInfo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author caojiantao
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcService {

    boolean broadcast() default false;

    Class<? extends IBalancer<ServiceInfo>> balancer() default RandomBalancer.class;
}
