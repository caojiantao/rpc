package com.caojiantao.rpc.registry.impl;

import com.caojiantao.rpc.balancer.IBalancer;
import com.caojiantao.rpc.registry.IRegistry;
import com.caojiantao.rpc.registry.ServiceInfo;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceProvider;

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
        ServiceInstance<ServiceInfo> instance = toInstance(serviceInfo);
        discovery.registerService(instance);
    }

    @SneakyThrows
    @Override
    public void unregister(ServiceInfo serviceInfo) {
        ServiceInstance<ServiceInfo> instance = toInstance(serviceInfo);
        discovery.unregisterService(instance);
    }

    @Override
    public ServiceInfo load(String service, IBalancer<ServiceInfo> balancer) {
        ServiceInfo serviceInfo = null;
        try (ServiceProvider<ServiceInfo> provider = discovery.serviceProviderBuilder()
                // see org.apache.curator.x.discovery.strategies.RandomStrategy
                .providerStrategy(instanceProvider -> {
                    List<ServiceInstance<ServiceInfo>> list = instanceProvider.getInstances();
                    List<ServiceInfo> payloadList = list.stream().map(ServiceInstance::getPayload).collect(Collectors.toList());
                    ServiceInfo payload = balancer.choose(payloadList);
                    return toInstance(payload);
                })
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

    @SneakyThrows
    public ServiceInstance<ServiceInfo> toInstance(ServiceInfo serviceInfo) {
        return ServiceInstance.<ServiceInfo>builder()
                .name(serviceInfo.getName())
                .address(serviceInfo.getHost())
                .port(serviceInfo.getPort())
                .payload(serviceInfo)
                .build();
    }
}
