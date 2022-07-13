package com.caojiantao.rpc.balancer.impl;

import com.caojiantao.rpc.balancer.IBalancer;
import com.caojiantao.rpc.registry.ServiceInfo;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Random;

/**
 * @author caojiantao
 */
public class WeightedRandomBalancer implements IBalancer<ServiceInfo> {

    private final Random random = new Random();

    @Override
    public ServiceInfo choose(List<ServiceInfo> list) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        int sum = list.stream().mapToInt(ServiceInfo::getWeight).sum();
        int sumIndex = random.nextInt(sum);
        int index = 0;
        while (index < list.size()) {
            ServiceInfo serviceInfo = list.get(index);
            if (sumIndex < serviceInfo.getWeight()) {
                break;
            }
            index++;
        }
        return list.get(index);
    }
}
