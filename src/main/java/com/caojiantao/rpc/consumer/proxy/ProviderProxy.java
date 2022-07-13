package com.caojiantao.rpc.consumer.proxy;

import com.caojiantao.rpc.RpcService;
import com.caojiantao.rpc.balancer.IBalancer;
import com.caojiantao.rpc.consumer.io.Client;
import com.caojiantao.rpc.consumer.io.ClientFactory;
import com.caojiantao.rpc.protocol.RpcRequest;
import com.caojiantao.rpc.registry.IRegistry;
import com.caojiantao.rpc.registry.ServiceInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author caojiantao
 */
@Slf4j
@AllArgsConstructor
public class ProviderProxy<T> implements InvocationHandler {

    private ListableBeanFactory beanFactory;

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
        IRegistry registry = beanFactory.getBean(IRegistry.class);
        IBalancer<ServiceInfo> balancer = beanFactory.getBean(rpcService.balancer());
        if (broadcast) {
            List<Client> clientList = registry.list(service).stream().map(ClientFactory::create).collect(Collectors.toList());
            clientList.forEach(item -> item.sendRequest(request, false));
            return null;
        } else {
            ServiceInfo serviceInfo = registry.load(service, balancer);
            if (Objects.isNull(serviceInfo)) {
                log.error("[rpc-consumer] 找不到可用节点 {}", request.getClazz());
                return null;
            }
            Client client = ClientFactory.create(serviceInfo);
            return client.sendRequest(request, true);
        }
    }
}
