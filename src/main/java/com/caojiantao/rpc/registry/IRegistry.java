package com.caojiantao.rpc.registry;

import com.caojiantao.rpc.balancer.IBalancer;

import java.util.List;

/**
 * @author caojiantao
 */
public interface IRegistry {

    void register(ServiceInfo serviceInfo);

    void unregister(ServiceInfo serviceInfo);

    ServiceInfo load(String service, IBalancer<ServiceInfo> balancer);

    List<ServiceInfo> list(String service);
}
