package com.caojiantao.rpc.consumer.proxy;

import com.caojiantao.rpc.RpcService;
import com.caojiantao.rpc.consumer.io.Client;
import com.caojiantao.rpc.consumer.io.ClientFactory;
import com.caojiantao.rpc.protocol.RpcRequest;
import com.caojiantao.rpc.registry.IRegistry;
import com.caojiantao.rpc.registry.ServiceInfo;
import lombok.AllArgsConstructor;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
        String service = method.getDeclaringClass().getName();
        RpcService rpcService = method.getDeclaringClass().getAnnotation(RpcService.class);
        boolean broadcast = rpcService.broadcast();
        RpcRequest request = RpcRequest.builder()
                .clazz(method.getDeclaringClass())
                .name(method.getName())
                .args(args)
                .parameterTypes(method.getParameterTypes())
                .build();
        if (broadcast) {
            List<Client> clientList = registry.list(service).stream().map(ClientFactory::create).collect(Collectors.toList());
            clientList.forEach(item -> item.sendRequest(request, false));
            return null;
        } else {
            ServiceInfo serviceInfo = registry.load(service);
            if (Objects.isNull(serviceInfo)) {
                return null;
            }
            Client client = ClientFactory.create(serviceInfo);
            return client.sendRequest(request, true);
        }
    }
}
