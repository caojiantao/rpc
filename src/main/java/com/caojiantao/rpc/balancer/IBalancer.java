package com.caojiantao.rpc.balancer;

import java.util.List;

/**
 * @author caojiantao
 */
public interface IBalancer<T> {

    T choose(List<T> list);
}
