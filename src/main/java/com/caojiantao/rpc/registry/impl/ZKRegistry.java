package com.caojiantao.rpc.registry.impl;

import com.caojiantao.rpc.registry.IRegistry;
import com.caojiantao.rpc.registry.ServiceInfo;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceProvider;
import org.apache.curator.x.discovery.strategies.RandomStrategy;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author caojiantao
 */
@Slf4j
public class ZKRegistry implements IRegistry {

    private ServiceDiscovery<ServiceInfo> discovery;

    public ZKRegistry(CuratorFramework curator) throws Exception {
        this.discovery = ServiceDiscoveryBuilder.builder(ServiceInfo.class)
                .client(curator)
                .basePath("/services")
                .build();
        this.discovery.start();
    }

    @SneakyThrows
    @Override
    public void register(ServiceInfo serviceInfo) {
        discovery.registerService(serviceInfo.toInstance());
    }

    @SneakyThrows
    @Override
    public void unregister(ServiceInfo serviceInfo) {
        discovery.unregisterService(serviceInfo.toInstance());
    }

    @Override
    public ServiceInfo load(String service) {
        ServiceInfo serviceInfo = null;
        try (ServiceProvider<ServiceInfo> provider = discovery.serviceProviderBuilder()
                // todo 负载策略
                .providerStrategy(new RandomStrategy<>())
                .serviceName(service)
                .build()) {
            provider.start();
            ServiceInstance<ServiceInfo> instance = provider.getInstance();
            if (Objects.nonNull(instance)) {
                serviceInfo = instance.getPayload();
            }
        } catch (Exception e) {
            log.error("[rpc-registry] 获取服务信息异常", e);
        }
        return serviceInfo;
    }

    @SneakyThrows
    @Override
    public List<ServiceInfo> list(String service) {
        Collection<ServiceInstance<ServiceInfo>> instances = discovery.queryForInstances(service);
        return instances.stream().map(ServiceInstance::getPayload).collect(Collectors.toList());
    }
}
