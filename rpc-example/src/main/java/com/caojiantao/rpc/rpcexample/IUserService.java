package com.caojiantao.rpc.rpcexample;


import com.caojiantao.rpc.common.RpcService;

/**
 * @author caojiantao
 */
@RpcService
public interface IUserService {

    String sayHello(String msg);
}
