package com.caojiantao.rpc.example.provider.api;

import com.caojiantao.rpc.common.RpcService;

@RpcService
public interface IUserService {

    Integer create(User user);

    User findById(Integer id);

    Boolean updateName(Integer id, String name);
}
