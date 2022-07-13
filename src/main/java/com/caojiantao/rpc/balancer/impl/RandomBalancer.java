package com.caojiantao.rpc.balancer.impl;

import com.caojiantao.rpc.balancer.IBalancer;
import com.caojiantao.rpc.registry.ServiceInfo;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Random;

/**
 * @author caojiantao
 */
public class RandomBalancer implements IBalancer<ServiceInfo> {

    private final Random random = new Random();

    @Override
    public ServiceInfo choose(List<ServiceInfo> list) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        int thisIndex = random.nextInt(list.size());
        return list.get(thisIndex);
    }
}
