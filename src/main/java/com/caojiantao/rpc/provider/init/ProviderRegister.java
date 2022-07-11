package com.caojiantao.rpc.provider.init;

import com.caojiantao.rpc.RpcService;
import com.caojiantao.rpc.config.RpcConfig;
import com.caojiantao.rpc.registry.IRegistry;
import com.caojiantao.rpc.registry.ServiceInfo;
import com.caojiantao.rpc.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Proxy;

@Slf4j
public class ProviderRegister implements BeanPostProcessor {

    @Autowired
    private IRegistry registry;
    @Autowired
    private ApplicationContext context;

    @Autowired
    private RpcConfig config;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?>[] interfaces = bean.getClass().getInterfaces();
        if (bean instanceof Proxy || interfaces.length == 0) {
            return bean;
        }
        boolean flag = false;
        Class<?> service = null;
        for (Class<?> clazz : interfaces) {
            if (clazz.isAnnotationPresent(RpcService.class)) {
                flag = true;
                service = clazz;
                break;
            }
        }
        if (!flag) {
            return bean;
        }
        String host = IpUtils.getHostIp();
        Integer port = config.getProvider().getPort();
        ServiceInfo serviceInfo = new ServiceInfo();
        serviceInfo.setApp(context.getId());
        serviceInfo.setHost(host);
        serviceInfo.setPort(port);
        serviceInfo.setName(service.getName());
        registry.register(serviceInfo);
        log.info("[rpc-core] 注册服务 {} {}:{}", serviceInfo.getName(), host, port);
        return bean;
    }
}
