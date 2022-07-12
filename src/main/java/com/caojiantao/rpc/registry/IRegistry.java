package com.caojiantao.rpc.registry;

import java.util.List;

/**
 * @author caojiantao
 */
public interface IRegistry {

    void register(ServiceInfo serviceInfo);

    void unregister(ServiceInfo serviceInfo);

    ServiceInfo load(String service);

    List<ServiceInfo> list(String service);
}
