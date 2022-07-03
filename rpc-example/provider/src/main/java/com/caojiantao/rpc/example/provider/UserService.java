package com.caojiantao.rpc.example.provider;

import com.caojiantao.rpc.example.provider.api.IUserService;
import com.caojiantao.rpc.example.provider.api.User;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class UserService implements IUserService {

    private AtomicInteger idGenerator = new AtomicInteger();
    private Map<Integer, User> userMap = Maps.newHashMap();

    @Override
    public Integer create(User user) {
        int id = idGenerator.incrementAndGet();
        user.setId(id);
        userMap.putIfAbsent(id, user);
        return id;
    }

    @Override
    public User findById(Integer id) {
        return userMap.get(id);
    }

    @Override
    public Boolean updateName(Integer id, String name) {
        User user = userMap.get(id);
        if (Objects.isNull(user)) {
            return Boolean.FALSE;
        }
        user.setName(name);
        return Boolean.TRUE;
    }
}
