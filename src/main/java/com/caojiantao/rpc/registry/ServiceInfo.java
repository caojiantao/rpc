package com.caojiantao.rpc.registry;

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
    private String host;
    private Integer port;

    @SneakyThrows
    public ServiceInstance<ServiceInfo> toInstance() {
        return ServiceInstance.<ServiceInfo>builder()
                .name(name)
                .address(host)
                .port(port)
                .payload(this)
                .build();
    }
}
