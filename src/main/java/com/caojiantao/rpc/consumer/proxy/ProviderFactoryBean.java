package com.caojiantao.rpc.consumer.proxy;

import lombok.Data;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.ListableBeanFactory;

import java.lang.reflect.Proxy;

/**
 * @author caojiantao
 */
@Data
public class ProviderFactoryBean<T> implements FactoryBean<T> {

    private Class<T> clazz;
    private ListableBeanFactory beanFactory;

    @Override
    public T getObject() throws Exception {
        ProviderProxy proxy = new ProviderProxy(beanFactory);
        Object o = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, proxy);
        return (T) o;
    }

    @Override
    public Class<?> getObjectType() {
        return clazz;
    }
}
