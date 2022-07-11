package com.caojiantao.rpc.consumer.proxy;

import com.caojiantao.rpc.consumer.io.Client;
import com.caojiantao.rpc.consumer.io.ClientFactory;
import com.caojiantao.rpc.protocol.RpcRequest;
import com.caojiantao.rpc.registry.IRegistry;
import com.caojiantao.rpc.registry.ServiceInfo;
import lombok.AllArgsConstructor;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author caojiantao
 */
@AllArgsConstructor
public class ProviderProxy<T> implements InvocationHandler {

    private IRegistry registry;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        boolean objectMethod = ReflectionUtils.isObjectMethod(method);
        if (objectMethod) {
            return method.invoke(proxy, args);
        }
        ServiceInfo serviceInfo = registry.load(method.getDeclaringClass().getName());
        if (Objects.isNull(serviceInfo)) {
            return null;
        }
        Client client = ClientFactory.create(serviceInfo);
        RpcRequest request = RpcRequest.builder()
                .clazz(method.getDeclaringClass())
                .name(method.getName())
                .args(args)
                .parameterTypes(method.getParameterTypes())
                .build();
        return client.sendRequest(request);
    }
}
