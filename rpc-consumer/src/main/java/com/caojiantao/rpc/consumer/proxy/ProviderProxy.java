package com.caojiantao.rpc.consumer.proxy;

import lombok.Data;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author caojiantao
 */
@Data
public class ProviderProxy<T> implements InvocationHandler {

    private Class<T> clazz;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        boolean objectMethod = ReflectionUtils.isObjectMethod(method);
        if (objectMethod) {
            return null;
        }
        // 客户端发起调用
        System.out.println(method + "," + Arrays.toString(args));
        return args[0];
    }
}
