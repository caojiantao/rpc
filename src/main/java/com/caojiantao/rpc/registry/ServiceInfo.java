package com.caojiantao.rpc.registry;

import lombok.Data;

/**
 * @author caojiantao
 */
@Data
public class ServiceInfo {

    private String app;
    private String name;
    private String host;
    private Integer port;
    // todo 加权随机
    private Integer weight;
}
