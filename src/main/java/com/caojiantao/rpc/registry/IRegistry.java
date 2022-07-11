package com.caojiantao.rpc.registry;

/**
 * @author caojiantao
 */
public interface IRegistry {

    void register(ServiceInfo serviceInfo);

    void unregister(ServiceInfo serviceInfo);

    ServiceInfo load(String service);
}
