package com.caojiantao.rpc.registry.entity;

import lombok.Data;
import lombok.SneakyThrows;
import org.apache.curator.x.discovery.ServiceInstance;

/**
 * @author caojiantao
 */
@Data
public class ServiceInfo {

    private String app;
    private String name;
    private String ip;
    private Integer port;

    @SneakyThrows
    public ServiceInstance<ServiceInfo> toInstance() {
        return ServiceInstance.<ServiceInfo>builder()
                .name(name)
                .address(ip)
                .port(port)
                .payload(this)
                .build();
    }
}
