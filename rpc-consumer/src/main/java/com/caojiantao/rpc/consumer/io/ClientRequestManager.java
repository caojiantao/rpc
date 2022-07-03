package com.caojiantao.rpc.consumer.io;

import com.google.common.collect.Maps;

import java.util.Map;

public class ClientRequestManager {

    private static Map<String, RequestFuture> futureMap = Maps.newConcurrentMap();

    public static void addFuture(String traceId, RequestFuture future) {
        futureMap.put(traceId, future);
    }

    public static void removeFuture(String traceId) {
        futureMap.remove(traceId);
    }

    public static RequestFuture getFuture(String traceId) {
        return futureMap.get(traceId);
    }
}
