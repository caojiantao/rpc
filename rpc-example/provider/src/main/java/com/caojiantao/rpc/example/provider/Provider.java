package com.caojiantao.rpc.example.provider;

import com.caojiantao.rpc.provider.EnableRpcProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@EnableRpcProvider
@SpringBootApplication
public class Provider {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Provider.class, args);
    }
}
