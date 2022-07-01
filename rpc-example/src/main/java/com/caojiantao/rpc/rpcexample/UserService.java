package com.caojiantao.rpc.rpcexample;

import org.springframework.stereotype.Service;

/**
 * @author caojiantao
 */
@Service
public class UserService implements IUserService {

    public String sayHello(String msg) {
        return "impl";
    }
}
