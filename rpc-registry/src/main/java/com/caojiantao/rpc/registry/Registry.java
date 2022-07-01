package com.caojiantao.rpc.registry;

import com.caojiantao.rpc.registry.entity.ServiceInfo;
import lombok.SneakyThrows;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceProvider;
import org.apache.curator.x.discovery.strategies.RandomStrategy;

import java.util.Objects;

/**
 * @author caojiantao
 */
public class Registry {

    private ServiceDiscovery<ServiceInfo> discovery;

    public Registry(CuratorFramework curator) throws Exception {
        this.discovery = ServiceDiscoveryBuilder.builder(ServiceInfo.class)
                .client(curator)
                .basePath("/services")
                .build();
        this.discovery.start();
    }

    @SneakyThrows
    public void register(ServiceInfo serviceInfo) {
        discovery.registerService(serviceInfo.toInstance());
    }

    @SneakyThrows
    public void unregister(ServiceInfo serviceInfo) {
        discovery.unregisterService(serviceInfo.toInstance());
    }

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

        }
        return serviceInfo;
    }
}
