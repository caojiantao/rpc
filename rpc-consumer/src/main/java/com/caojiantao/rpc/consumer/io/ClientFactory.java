package com.caojiantao.rpc.consumer.io;

import com.caojiantao.rpc.registry.entity.ServiceInfo;
import com.google.common.collect.Maps;

import java.util.Map;

public class ClientFactory {

    private static Map<String, Client> clientMap = Maps.newConcurrentMap();

    public static Client create(ServiceInfo serviceInfo) {
        String key = serviceInfo.getHost() + ":" + serviceInfo.getPort();
        if (!clientMap.containsKey(key)) {
            synchronized (ClientFactory.class) {
                if (!clientMap.containsKey(key)) {
                    Client client = new Client(serviceInfo.getHost(), serviceInfo.getPort());
                    client.connect();
                    clientMap.put(key, client);
                }
            }
        }
        return clientMap.get(key);
    }
}
