package com.caojiantao.rpc.consumer.io;

import com.caojiantao.rpc.common.utils.JsonUtils;
import com.caojiantao.rpc.transport.protocol.Message;
import com.caojiantao.rpc.transport.protocol.RpcRequest;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class RequestFuture {

    private Message<RpcRequest> request;
    private Message<Object> response;
    private Thread thread;

    public RequestFuture(Message<RpcRequest> request) {
        this.request = request;
        this.thread = Thread.currentThread();
        ClientRequestManager.addFuture(request.getHeader().getTraceId(), this);
    }

    public Object get(long duration, TimeUnit unit) {
        LockSupport.parkNanos(unit.toNanos(duration));
        RpcRequest rpcRequest = request.getBody();
        Class<?> returnType = rpcRequest.getReturnType();
        Object result = null;
        if (Objects.nonNull(response) && !Objects.equals(Void.class, returnType)) {
            result = JsonUtils.parse(response.getBytes(), returnType);
        }
        ClientRequestManager.removeFuture(request.getHeader().getTraceId());
        return result;
    }

    public void set(Message response) {
        this.response = response;
        LockSupport.unpark(thread);
    }
}
