package com.caojiantao.rpc.rpcexample;

import com.caojiantao.rpc.consumer.EnableRpcConsumer;
import com.caojiantao.rpc.provider.EnableRpcProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@EnableRpcProvider
@EnableRpcConsumer(basePackages = {"com.caojiantao"})
@SpringBootApplication
public class RpcExampleApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(RpcExampleApplication.class, args);
        IUserService service = context.getBean(IUserService.class);
        String msg = service.sayHello("hello world");
        System.out.println(msg);
    }

}
