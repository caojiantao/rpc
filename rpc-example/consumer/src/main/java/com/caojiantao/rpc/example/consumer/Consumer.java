package com.caojiantao.rpc.example.consumer;

import com.caojiantao.rpc.consumer.EnableRpcConsumer;
import com.caojiantao.rpc.example.provider.api.IUserService;
import com.caojiantao.rpc.example.provider.api.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
@EnableRpcConsumer("com.caojiantao")
@SpringBootApplication
public class Consumer {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Consumer.class, args);
        IUserService userService = context.getBean(IUserService.class);

        User user = new User();
        user.setName("曹建涛");
        Integer id = userService.create(user);
        log.info("创建用户 {}", id);

        user = userService.findById(id);
        log.info("查找用户 {}", user);

        user = userService.findById(0);
        log.info("查找用户 {}", user);

        Boolean update = userService.updateName(id, "陈丽沙");
        log.info("更新用户 {}", update);
    }
}
