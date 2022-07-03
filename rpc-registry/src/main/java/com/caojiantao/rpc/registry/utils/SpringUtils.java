package com.caojiantao.rpc.registry.utils;

import org.springframework.context.ApplicationContext;

public class SpringUtils {

    private static ApplicationContext context;

    public SpringUtils(ApplicationContext context) {
        SpringUtils.context = context;
    }

    public static ApplicationContext getContext() {
        return context;
    }
}
