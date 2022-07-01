package com.caojiantao.rpc.provider.init;

import com.caojiantao.rpc.common.RpcService;
import com.caojiantao.rpc.registry.Registry;
import com.caojiantao.rpc.registry.entity.ServiceInfo;
import com.caojiantao.rpc.registry.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Proxy;

@Slf4j
public class ProviderRegister implements BeanPostProcessor {

    @Autowired
    private Registry registry;
    @Autowired
    private ApplicationContext context;

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
        ServiceInfo serviceInfo = new ServiceInfo();
        serviceInfo.setApp(context.getId());
        serviceInfo.setIp(IpUtils.getHostIp());
        serviceInfo.setPort(8888);
        serviceInfo.setName(service.getName());
        registry.register(serviceInfo);
        log.info("[rpc-provider] 注册服务 {}", serviceInfo.getName());
        return bean;
    }
}
