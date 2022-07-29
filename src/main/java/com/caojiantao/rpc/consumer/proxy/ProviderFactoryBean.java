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
        ProviderHandler handler = new ProviderHandler(beanFactory);
        Object proxy = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, handler);
        return (T) proxy;
    }

    @Override
    public Class<?> getObjectType() {
        return clazz;
    }
}
