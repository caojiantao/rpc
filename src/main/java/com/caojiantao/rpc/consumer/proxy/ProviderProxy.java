package com.caojiantao.rpc.consumer.proxy;

import com.caojiantao.rpc.consumer.io.Client;
import com.caojiantao.rpc.consumer.io.ClientFactory;
import com.caojiantao.rpc.protocol.RpcRequest;
import com.caojiantao.rpc.registry.IRegistry;
import com.caojiantao.rpc.registry.ServiceInfo;
import com.caojiantao.rpc.utils.SpringUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author caojiantao
 */
@Data
@Slf4j
public class ProviderProxy<T> implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        boolean objectMethod = ReflectionUtils.isObjectMethod(method);
        if (objectMethod) {
            return method.invoke(proxy, args);
        }
        ApplicationContext context = SpringUtils.getContext();
        IRegistry registry = context.getBean(IRegistry.class);
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
