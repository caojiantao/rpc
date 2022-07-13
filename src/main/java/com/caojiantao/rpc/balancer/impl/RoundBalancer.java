package com.caojiantao.rpc.balancer.impl;

import com.caojiantao.rpc.balancer.IBalancer;
import com.caojiantao.rpc.registry.ServiceInfo;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author caojiantao
 */
public class RoundBalancer implements IBalancer<ServiceInfo> {

    private final AtomicInteger index = new AtomicInteger();

    @Override
    public ServiceInfo choose(List<ServiceInfo> list) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        int thisIndex = Math.abs(index.getAndIncrement());
        return list.get(thisIndex % list.size());
    }
}
